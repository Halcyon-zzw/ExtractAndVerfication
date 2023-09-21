package com.zhuzw.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author zhuzhiwei
 * @date 2023/9/20 14:40
 */
public class FileUtil {
    /**
     * 提取指定类型的文件
     * @param folderPath
     * @param fileType
     * @return
     */
    public static List<File> extractHtmlFiles(String folderPath, String fileType) {
        return extractHtmlFiles(folderPath, fileType, true);
    }

    /**
     * 提取指定类型的文件
     * @param folderPath
     * @param fileType
     * @return
     */
    public static List<File> extractHtmlFiles(String folderPath, String fileType, boolean ectractSubFlaf) {
        // 创建存储文件的List
        List<File> htmlFiles = new ArrayList<>();
        // 创建文件夹对象
        File folder = new File(folderPath);
        // 获取文件夹下所有文件和子文件夹
        File[] files = folder.listFiles();
        if (files != null) {
            // 遍历文件和子文件夹
            for (File file : files) {
                // 如果是文件夹，递归调用本方法实现深度搜索
                if (ectractSubFlaf && file.isDirectory()) {
                    List<File> subFiles = extractHtmlFiles(file.getAbsolutePath(), fileType, true);
                    htmlFiles.addAll(subFiles);
                }
                // 如果是文件，并且是指定类型，加入结果列表
                else if (file.isFile() && file.getName().endsWith(fileType)) {
                    htmlFiles.add(file);
                }
            }
        }
        return htmlFiles;
    }



    public static String readFileData(File file) {
        StringBuilder fileData = new StringBuilder();
        try {
            // 创建文件读取器
            FileReader fileReader = new FileReader(file);
            // 创建缓冲读取器
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            // 读取文件数据
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileData.append(line);
                fileData.append(System.lineSeparator());
            }
            // 关闭缓冲读取器和文件读取器
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData.toString();
    }


    public static String readFileData(String filePath) {
        File file = new File(filePath);
        return readFileData(file);
    }
}
