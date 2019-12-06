package config;

import util.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

/**
 * 系统缓存类
 *
 * @Author: zhuzw
 * @Date: 2019/11/5 9:07
 * @Version: 1.0
 */
public class ApplicationCache {

    ApplicationProperties aps = new ApplicationProperties();
    /**
     * 删除无效词语句子缓存
     */
    public static List<String> sentenceListDeleteByInvalid;

    /**
     * 保存文件时文件被占用时，尝试重新保存至新文件
     * @param collection
     * @param path
     */
    public static void saveCache(Collection<String> collection, String path) {
        path = getFileNewPath(path);
        try {
            FileUtil.createFile2(collection, path);
            System.out.println("缓存数据保存成功！");
        } catch (IOException e) {
            System.out.println("缓存数据保存失败！");
            e.printStackTrace();
        }
    }

    /**
     * 保存文件时文件被占用时，尝试重新保存至新文件
     * @param collection
     * @param path
//     */
//    public void saveCache(Collection<String[]> collection, String path) {
//        path = getFileNewPath(path);
//        try {
//            FileUtil.createFile(collection, path);
//            System.out.println("缓存数据保存成功！");
//        } catch (IOException e) {
//            System.out.println("缓存数据保存失败！");
//            e.printStackTrace();
//        }
//    }

    /**
     * 获取新路径
     * @param path
     * @return
     */
    private static String getFileNewPath(String path) {
        Path targetPath = Paths.get(path);
        String dirPath = targetPath.getParent().toString();
        String fileName = targetPath.getFileName().toString().split("\\.")[0];
        String suffixName = "." + targetPath.getFileName().toString().split("\\.")[1];
        //文件名后面加入当前时间戳
        fileName = fileName + "_" + System.currentTimeMillis() + suffixName;
        path = dirPath + "\\" + fileName;
        return path;
    }
}
