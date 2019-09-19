package pool;

import create.CreateFileWay;
import deal.DealFileWay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理文件模板类
 * <p>
 * 提供DealFileWay对象（处理file方式）；
 * 并重写createFile方法
 *
 * @Author: zhuzw
 * @Date: 2019/8/7 19:18
 * @Version: 2.0    模板进一步的提取；添加钩子函数（是否生成文件）
 *
 * @modify:
 *      openFile(String path)  更名   dealFile(String path)
 *      dealFile(String path)  更名   dealAndCreateFile(String path)
 *      删除钩子函数
 * @Version: 3.0
 */
public class DealFile {

    /**
     * 处理file方式
     */
    private DealFileWay dealFileWay;

    /**
     * 生成文件方式
     */
    private CreateFileWay createFileWay;

    public DealFile(DealFileWay dealFileWay, CreateFileWay createFileWay) {
        this.dealFileWay = dealFileWay;
        this.createFileWay = createFileWay;
    }

    /**
     * 可用于获取处理后的数据，不生成文件
     *
     * @param dealFileWay
     */
    public DealFile(DealFileWay dealFileWay) {
        this.dealFileWay = dealFileWay;
    }



    /**
     * 处理并生成文件
     * @param prefixPath
     */
    public void dealAndCreateFile(String prefixPath) throws IOException {
        Object result = dealFile(prefixPath);
        createFileWay.createFile(result);
    }


    /**
     * 处理文件:
     * 当传的参数为文件夹，则处理文件夹下所有文件；
     * 如果参数为单个文件，则处理单个文件
     *
     * @param prefixPath
     * @return 处理文件后返回的数据集
     */
    public List<String> dealFile(String prefixPath) throws IOException {
        List<String> resultList = new ArrayList<>();
        File prefixFile = new File(prefixPath);

        if (prefixFile.isFile()) {
            //处理单个文件
            resultList.addAll(dealFileWay.extractedValue(prefixPath));
            return resultList;
        }

        if (!prefixFile.exists()) {
            //目录不存在,创建目录
            prefixFile.mkdir();
        }
        //批量处理
        File[] files = prefixFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //得到文件路径
                String excelPath = prefixPath + files[i].getName();
                resultList.addAll(dealFileWay.extractedValue(excelPath));
            }
        }
        return resultList;
    }

}
