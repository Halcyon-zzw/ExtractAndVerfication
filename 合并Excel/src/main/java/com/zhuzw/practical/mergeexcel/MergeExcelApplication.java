package com.zhuzw.practical.mergeexcel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigurationProperties(ExcelProperties.class)
@RestController
public class MergeExcelApplication {
    @Autowired
    private  ExcelProperties excelProperties;

    public static void main(String[] args) {
        SpringApplication.run(MergeExcelApplication.class, args);
        System.out.println(ExcelProperties.poolExcelName);
        System.out.println(ExcelProperties.summariedPath);

    }

    @RequestMapping("/date")
    public String testDate() {
        System.out.println(excelProperties.getDate());
        return excelProperties.getDate();
    }


}