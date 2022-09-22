package com.bdilab.datasetupload.service;


import com.bdilab.datasetupload.utils.ColumnType;
import com.bdilab.datasetupload.utils.clickhouse.ClickHouseJdbcUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;



public interface DatasetService {
    public String uploadDataset(MultipartFile file) throws IOException, InterruptedException;

    public String deleteDataset(String tableName);
    public String previewDataset(String tableName);




}
