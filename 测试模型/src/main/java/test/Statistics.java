package test;

import com.csvreader.CsvReader;
import config.FileProperties;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计
 *
 * @Author: zhuzw
 * @Date: 2019/10/10 8:48
 * @Version: 1.0
 */
public class Statistics {
    public static void main(String[] args) throws IOException {
        statistics();
    }

    public static void statistics() throws IOException {
        //定义分类类别
        String[] categorys = {"1", "2", "3", "6", "7", "8", "9"};

        //读取数据
        List<StandardAndPrediction> data = new ArrayList<>();
        CsvReader csvReader = FileUtil.getCsvReader(new FileProperties().getCreatePath());
        while (csvReader.readRecord()) {
            StandardAndPrediction standardAndPrediction = new StandardAndPrediction();
            standardAndPrediction.setStandard(csvReader.get(0));
            standardAndPrediction.setPrediction(csvReader.get(1));
            if (!standardAndPrediction.getStandard().equalsIgnoreCase(standardAndPrediction.getPrediction())) {
                //标准与预测不同
                data.add(standardAndPrediction);
            }
        }
        HashMap<String, HashMap<String, Integer>> toolMistakeMap = new HashMap<>();
        for (String category : categorys) {
            HashMap<String, Integer> mistakeMap = new HashMap<>();
            for (StandardAndPrediction standardAndPrediction : data) {
                if (category.equalsIgnoreCase(standardAndPrediction.getStandard())) {
                    //一次统计一个类别
                    String mistake = standardAndPrediction.getPrediction();
                    if (null == mistakeMap.get(mistake)) {
                        mistakeMap.put(mistake, 0);
                    }
                    mistakeMap.put(mistake, mistakeMap.get(mistake) + 1);
                }
            }
            toolMistakeMap.put(category, mistakeMap);
        }

        //输出
        for (Map.Entry<String, HashMap<String, Integer>> entryTool : toolMistakeMap.entrySet()) {
            System.out.println("类别" + entryTool.getKey() + ":");
            System.out.println("----");
            System.out.println("误识别情况：");
            for (Map.Entry<String, Integer> entry : entryTool.getValue().entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
            long toolNum = entryTool.getValue().entrySet().stream().mapToInt(HashMap.Entry::getValue).sum();
            System.out.println("总误识别数：" + toolNum);
            System.out.println("-----------------------------");
        }


    }
}
