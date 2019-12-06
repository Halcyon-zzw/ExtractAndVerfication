package create.impl;

import config.ApplicationCache;
import create.CreateFileWay;
import util.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 直接生成文件
 *
 * @Author: zhuzw
 * @Date: 2019/8/12 14:13
 * @Version: 1.0
 */
public class CreateFileDirect implements CreateFileWay {

    private String createPath = "";

    public CreateFileDirect(String createPath) {
        this.createPath = createPath;
    }

    @Override
    public void createFile(Object object) {

        List<String> result = (List<String>) object;

        //生成文件

        try {
            FileUtil.createFile(result, createPath);
        } catch (IOException e) {
            ApplicationCache.saveCache(result, createPath);
        }
    }
}
