package com.bdilab.datasetupload.service;

import com.bdilab.datasetupload.utils.ColumnType;
import com.bdilab.datasetupload.utils.clickhouse.ClickHouseJdbcUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Service
public class DatasetServiceImpl implements DatasetService {
    @Resource
    ClickHouseJdbcUtils clickHouseJdbcUtils;

    @Override
    public String uploadDataset(MultipartFile file) throws IOException, InterruptedException {
        //获得文件名，创建表名
        String fileName = file.getOriginalFilename();
        String tableName = fileName.substring(0,fileName.indexOf("."));
        //将上传的文件存于服务其中
        File targetFile = new File("/dataflow/dataupload/data", fileName);
        if (!targetFile.exists()){
            FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
        }
        String line ="";
        String[] columnNames = null;     //列名
        String[] columnValues = null;    //列值
        Map<String,String> nameAndType = new HashMap<>();  //列名对应的类型
        try{
            InputStream bb = file.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(bb);
            BufferedReader br = new BufferedReader(streamReader);
            //默认第一行为列名
            if((line=br.readLine())!=null){
                columnNames = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            }

            //采样前20行，取最长或经度最高的值
            int k =0;
            while((line = br.readLine())!=null && k<=20){
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if(k==0){
                    columnValues = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                }
                for (int i = 0; i < values.length; i++) {
                    if(values[i].length()>columnValues[i].length()){
                        columnValues[i] = values[i];
                    }
                }
                k+=1;
            }

            for (int i = 0; i < columnValues.length; i++) {
                String columnType = ColumnType.getColumnType(columnValues[i]);
                nameAndType.put(columnNames[i],columnType);
            }

            StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS dataflow."
                    + tableName + "(");
            for (int i = 0; i < columnNames.length; i++) {
                stringBuilder.append(columnNames[i]+" "+nameAndType.get(columnNames[i])+",");
            }
            String str = stringBuilder.toString();
            String createSql = str.substring(0,str.length()-1) + ") ENGINE = MergeTree() ORDER BY "
                    + columnNames[0]+" ;";
            //clickHouseJdbcUtils.execute(createSql);

            String cmd1 = "clickhouse-client -h 47.104.202.153 --query=\" " +
                    createSql+
                    "  \" ";
            System.out.println(cmd1);
            Runtime run1 = Runtime.getRuntime();
            String[] cmds1 = { "/bin/sh", "-c", cmd1 };
            Process process1 = run1.exec(cmds1);
            process1.waitFor();
            process1.destroy();

        }catch (Exception e){
            e.printStackTrace();
        }
        String cmd = "clickhouse-client -h 47.104.202.153 --query=\"insert into " +
                "dataflow."+tableName +
                " FORMAT CSVWithNames\" < " +
                "/dataflow/dataupload/data/"+fileName;

        System.out.println(cmd);
        Runtime run = Runtime.getRuntime();
        try {
            String[] cmds = { "/bin/sh", "-c", cmd };
            Process process = run.exec(cmds);
            process.waitFor();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @Override
    public String deleteDataset(String tableName) {
        String createSql = "drop table  " + "dataflow."+tableName +";";
        String cmd1 = "clickhouse-client -h 47.104.202.153 --query=\" " +
                createSql+
                "  \" ";
        System.out.println(cmd1);
        try{
            Runtime run1 = Runtime.getRuntime();
            String[] cmds1 = { "/bin/sh", "-c", cmd1 };
            Process process1 = run1.exec(cmds1);
            process1.waitFor();
            process1.destroy();
        }catch (IOException | InterruptedException e) {
        e.printStackTrace();
        }
        //clickHouseJdbcUtils.execute(createSql);
        System.out.println(createSql);
        return "success";
    }

    @Override
    public String previewDataset(String tableName) {
        return null;
    }
}
