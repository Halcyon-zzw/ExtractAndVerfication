package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description: ${description}
 * @author: fanmj
 * @create: 2019-08-02
 **/
public class ExportTest {
    public static String from = "E:\\语料\\事件补充语料\\事件id.txt";

    public static String to = "E:\\语料\\情感测试语料\\语料3.csv";

    public static void main(String[] args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        String sqlurl = "jdbc:sqlserver://10.105.0.100:1433;DatabaseName=JYPRIME";

        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //连接
        conn = DriverManager.getConnection(sqlurl, "JYPRIME_READ", "JYPRIME_READ");
        CSVWriter writer = new CSVWriter(new FileWriter(to));
        final Statement stmt = conn.createStatement();


        List<String> stream1 = Files.lines(Paths.get(from)).collect(Collectors.toList());
        int i = 0;

        String[] cs = new String[]{"3","1","2"};
        String[] jb = new String[]{"3","1","2"};

        for (String s : cs) {
            for(String j: jb){
//                List<String> str = new ArrayList<>(Arrays.asList(s.split("\t")));
//            String id = str.get(0);
//            int num = Integer.parseInt(str.get(1));
//            if(num < 3000){
                //String sql = "select top 3000 a.ZQBID,a.YQLB,a.YQQG1,a.YQQGJB1,b.YJSJLB,b.EJSJLB,b.SJQG1,b.SJQGJB1,a.YQBT from usrYQZB a join usrYQSJB b on a.ID = b.YQID and b.GKBZ=3 and b.EJSJLB = " + "'" + id + "'" + " and a.XXFBRQ < '2019-08-01' and a.XXFBRQ > '2018-09-11' order by a.XXFBRQ desc";
                String sql = "select top 30 a.XXFBRQ, a.ZQBID,a.YQLB,a.YQQG1,a.YQQGJB1,b.YJSJLB,b.EJSJLB,b.SJQG1,b.SJQGJB1,a.YQBT from usrYQZB a join usrYQSJB b on a.ID = b.YQID and b.GKBZ=3 and YQQG1 = " + s  +" and YQQGJB1 =" + j +" order by a.XXFBRQ desc";
                System.out.println(sql);
                ResultSet resultSet = stmt.executeQuery(sql);
                String EJSJLB = null;
                while (resultSet.next()) {
                    String[] result = new String[8];
                    EJSJLB = resultSet.getString("EJSJLB");
                    result[0] = resultSet.getString("ZQBID");
                    result[1] = resultSet.getString("YQLB");
                    result[2] = resultSet.getString("YQQG1");
                    result[3] = resultSet.getString("YQQGJB1");
                    result[4] = resultSet.getString("YJSJLB");
                    result[5] = resultSet.getString("EJSJLB");
                    result[6] = resultSet.getString("SJQG1");
                    result[7] = resultSet.getString("YQBT");
                    writer.writeNext(result);
                }
                if(EJSJLB != null){
                    i += 1;
                }
            }
//
//            }
        }
        System.out.println("end");
        System.out.println(i);
    }

}
