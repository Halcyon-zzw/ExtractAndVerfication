package create.impl;

import create.CreateFileWay;
import util.FileUtil;

import java.io.IOException;
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

    private String path = "";

    public CreateFileDirect(String path) {
        this.path = path;
    }

    @Override
    public void createFile(Object object) throws IOException {

        List<String> result = (List<String>) object;

        //生成文件
        FileUtil.createFile(result, path);
    }
}
