package test;

import com.csvreader.CsvReader;
import config.FileProperties;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计
 *
 * 统计每一类的正确率
 * @Author: zhuzw
 * @Date: 2019/10/10 8:48
 * @Version: 1.0
 */
public class Statistics {

    private static FileProperties fileProperties = new FileProperties();
    private static FileProperties.PrimaryProperties primaryProperties = fileProperties.getPrimaryProperties();

    /**
     * 记录每类分类的数量
     */
    private static HashMap<String, Integer> categoriesMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        statistics();
    }

    /**
     * 统计分类错误情况
     *
     * @throws IOException
     */
    public static void statistics() throws IOException {
        //定义分类类别
        String[] categorys = {"1", "2", "3", "6", "7", "8", "9"};

        //存储错误结果数据
        List<PredictionData> data = new ArrayList<>();
        CsvReader csvReader = FileUtil.getCsvReader(primaryProperties.getCreatePath());
        while (csvReader.readRecord()) {
            PredictionData predictionData = new PredictionData();
            predictionData.setStandard(csvReader.get(0));
            predictionData.setPrediction(csvReader.get(1));
            if (!predictionData.getStandard().equalsIgnoreCase(predictionData.getPrediction())) {
                //标准与预测不同
                predictionData.setContent(csvReader.get(3));
                data.add(predictionData);
            }

            //统计每个分类总数
            if (null == categoriesMap.get(predictionData.getStandard())) {
                categoriesMap.put(predictionData.getStandard(), 0);
            }
            categoriesMap.put(predictionData.getStandard(), categoriesMap.get(predictionData.getStandard()) + 1);
        }

        List<String> errorData = data.stream().map(PredictionData::toString).collect(Collectors.toList());
        //错误数据输出到文件
        FileUtil.createFile(errorData, primaryProperties.getErrorPath());


        //统计出错情况
        HashMap<String, HashMap<String, Integer>> toolMistakeMap = new HashMap<>();
        for (String category : categorys) {
            HashMap<String, Integer> mistakeMap = new HashMap<>();
            for (PredictionData predictionData : data) {
                if (category.equalsIgnoreCase(predictionData.getStandard())) {
                    //一次统计一个类别
                    String mistake = predictionData.getPrediction();
                    if (null == mistakeMap.get(mistake)) {
                        mistakeMap.put(mistake, 0);
                    }
                    mistakeMap.put(mistake, mistakeMap.get(mistake) + 1);
                }
            }
            toolMistakeMap.put(category, mistakeMap);
        }

        int toolCorrectNum = 0;

        int toolErrorNum = 0;
        int toolErrorNum2 = 0;

        List<String> resultList = new ArrayList<>();
        //输出误识别到每一类情况
        for (Map.Entry<String, HashMap<String, Integer>> entryTool : toolMistakeMap.entrySet()) {
            resultList.add("类别" + entryTool.getKey() + ":");
            resultList.add("----");
            resultList.add("误识别情况：");
            
            for (Map.Entry<String, Integer> entry : entryTool.getValue().entrySet()) {
                resultList.add(entry.getKey() + ":" + entry.getValue());
            }
            int toolMistakeNum = entryTool.getValue().entrySet().stream().mapToInt(HashMap.Entry::getValue).sum();
            int toolNum = categoriesMap.get(entryTool.getKey());
            resultList.add("分类总数：" + toolNum);
            resultList.add("总误识别数：" + toolMistakeNum);
            resultList.add("准确率：" + ((toolNum - toolMistakeNum) / (float)toolNum));
            int toolMistakeNum2 = entryTool.getValue().entrySet().stream()
                    .filter(e -> (Integer.valueOf(e.getKey()) - 1) / 3 != (Integer.valueOf(entryTool.getKey()) - 1) / 3)
                    .mapToInt(HashMap.Entry::getValue).sum();
//            int toolNum2 =
            resultList.add("忽略星级错误数：" + toolMistakeNum2);
            resultList.add("-----------------------------");

            toolCorrectNum += toolNum;
            toolErrorNum += toolMistakeNum;
            toolErrorNum2 += toolMistakeNum2;
        }
        resultList.add("-----------------------------");
        resultList.add("-----------------------------");
        resultList.add("准确率：" + ((toolCorrectNum - toolErrorNum) / (float)toolCorrectNum));
        resultList.add("忽略星级准确率：" + ((toolCorrectNum - toolErrorNum2) / (float)toolCorrectNum));

        FileUtil.createFile(resultList, primaryProperties.getStatisticsPath());

    }
}
