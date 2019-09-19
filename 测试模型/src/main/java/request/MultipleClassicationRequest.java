package request;

import bert.BertRequest;
import bert.BertResponse;
import bert.BertResultMultiple;
import bert.single.BertResultSingle;
import bert.PredictionUnit;
import util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多分类请求
 *
 * @Author: zhuzw
 * @Date: 2019/9/3 15:59
 * @Version: 1.0
 */
public class MultipleClassicationRequest extends ClassificationRequest {
    private List<String> resultList = new ArrayList<>();

    private String path = "E:\\下载\\钉钉文件\\工作资料\\验证结果\\事件多分类\\事件多分类请求缓存.txt";
    private List<List<PredictionUnit>> resonstList = new ArrayList<>();

    public MultipleClassicationRequest(BertRequest bertRequest, Class responseType) {
        super(bertRequest, responseType);
    }

    public BertResponse getResponse() {
        return (BertResponse)super.postForObject();
    }

    @Override
    public BertResultSingle getResult() {
        super.num++;
        if(0 == super.num % 100) {
            System.out.println("已处理: " + super.num);
        }
        List<PredictionUnit> rlist = getResultMultiple(1);

        //从响应数据中提取数据，标签及预测概率
        String label = rlist.get(0).getLabel();
        String score = String.valueOf(rlist.get(0).getScore());
        BertResultSingle bertResult = new BertResultSingle(label, Float.parseFloat(score));

        return bertResult;
    }

    /**
     * 获取多分类结果
     * @param count 获取结果的数量
     * @return
     */
    public List<PredictionUnit> getResultMultiple(int count) {
        BertResponse bertResponse = getResponse();

        //获取所有预测结果集
        Map<String, Double> pred_result = bertResponse.getResult().get(0).getPred_result().get(0);
        //将预测结果map转成List<PredictionUtil>，并按照预测概率降序排序
        List<PredictionUnit> predictionUnitList = pred_result.entrySet().stream().map(o -> {
            return new PredictionUnit(o.getKey(), o.getValue());
        }).sorted(Comparator.comparing(PredictionUnit::getScore).reversed())
                .collect(Collectors.toList());

        //从List结果中取count个分类
        cacheFormat(predictionUnitList.subList(0,count));
        return predictionUnitList.subList(0,count);
    }

    /**
     * 整理成cache格式
     * @return
     */
    public void cacheFormat(List<PredictionUnit> predictionUnitList) {
        String article = bertRequest.getTexts().get(0);
        String resultString = article;
        for (PredictionUnit predictionUnit : predictionUnitList) {
            resultString = resultString + "\t" + predictionUnit.getLabel();
            resultString = resultString + "\t" + predictionUnit.getScore();
            resultList.add(resultString);
        }
    }

    public void createCache() throws IOException {

        FileUtil.createFile(resultList, path);
    }

//    public Map<String, List<PredictionUnit>> getCache() {
//        File file = new File(path);
//        if (! file.exists()) {
//            System.out.println("缓存不存在!");
//            return null;
//        }
//
//        try (FileReader reader = new FileReader(path);
//             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
//        ) {
//            String line;
//            //网友推荐更加简洁的写法
//            while ((line = br.readLine()) != null) {
//                // 一次读入一行数据
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
