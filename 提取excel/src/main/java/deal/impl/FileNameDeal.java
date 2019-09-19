package deal.impl;

import deal.DealFileWay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理文件名,
 *
 *
 * @Author: zhuzw
 * @Date: 2019/8/19 15:21
 * @Version: 1.0
 */
public class FileNameDeal implements DealFileWay {
    /**
     * 提取事件分类代码和事件分类名称
     * 文件名样式:   109010-事件分类-证券戴帽.xlsx
     * @param path 文件路径
     * @return 109010\t证券戴帽 样式数据集的列表
     * @throws IOException
     */
    @Override
    public List<String> extractedValue(String path) throws IOException {
        List<String> resultList = new ArrayList<>();

        File file = new File(path);
        String name = file.getName();
        String[] names = name.split("-");
        //事件分类代码
        String eventClassify = names[0];
        String eventName = names[2].split("\\.")[0];

        resultList.add(eventClassify + "\t" + eventName);
        return resultList;
    }
}
