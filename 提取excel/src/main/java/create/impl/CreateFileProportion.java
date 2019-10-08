package create.impl;

import create.CreateFileWay;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 按比率生成文件
 *
 * @Author: zhuzw
 * @Date: 2019/8/8 9:03
 * @Version: 1.0
 */
public class CreateFileProportion implements CreateFileWay {

    /**
     * 比例集
     */
    private int[] proportions;

    /**
     * 路径集合
     */
    private String[] paths;

    /**
     * 存子列表数据
     */
    private List<List<String>> resultList = new ArrayList<>();

    public CreateFileProportion(int[] proportions, String[] paths) {
        this.proportions = proportions;
        this.paths = paths;
    }

    @Override
    public void createFile(Object object) throws IOException {
        List<String> result = (List<String>) object;
        if (0 == result.size()) {
            System.out.println("总数据量为0!");
            return;
        }
        System.out.println("开始生成文件...");
        //随机打乱
        Collections.shuffle(result);

        //获取每个比率数量，并初始化list
        //问题：是否存在数据数量不一致问题问题（取余数是导致数据量变大或变小）
        //解决：总数减去前面数量得到最后一次
        int toolCount = 0;
        for (int i = 0; i < proportions.length; i++) {
            if (i == proportions.length -1) {
                //最后一次
                proportions[i] = result.size() - toolCount;
                resultList.add(new ArrayList<String>());
                break;
            }
            proportions[i] = (int) (result.size() * proportions[i] * 0.1);
            toolCount += proportions[i];

            //list初始化
            resultList.add(new ArrayList<String>());
        }

        //resultList下标，控制数据插入到第几个list
        int listIndex = 0;
        for (int i = 0; i < result.size(); i++) {
            if (resultList.get(listIndex).size() >= proportions[listIndex]) {
                //填满后，下标后移
                listIndex++;
            }
            resultList.get(listIndex).add(result.get(i));
        }

        //遍历生成文件
        for (int i = 0; i < resultList.size(); i++) {
            FileUtil.createFile(resultList.get(i), paths[i]);
        }
        System.out.println("生成文件完成。");
    }
}
