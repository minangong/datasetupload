package com.bdilab.datasetupload.controller;


import com.bdilab.datasetupload.service.DatasetService;
import com.bdilab.datasetupload.utils.ColumnType;
import com.bdilab.datasetupload.utils.clickhouse.ClickHouseJdbcUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/dataset/")
public class DatasetController {
    @Autowired
    DatasetService datasetService;


    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ResponseEntity<String> uploadDataset(@RequestBody  MultipartFile file) throws IOException, InterruptedException {
        return ResponseEntity.ok(datasetService.uploadDataset(file));
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseEntity<String> deleteDataset(@RequestBody String tableName){
        return ResponseEntity.ok(datasetService.deleteDataset(tableName));
    }
    @RequestMapping(value = "/preview",method = RequestMethod.POST)
    public ResponseEntity<String> previewDataset(@RequestBody String tableName){
        return ResponseEntity.ok(datasetService.previewDataset(tableName));
    }



}
