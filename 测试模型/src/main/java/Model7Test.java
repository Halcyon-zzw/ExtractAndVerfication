import bert.BertRequest;
import bert.BertResponse;
import bert.PredictionUnit;
import bert.deal_file.DealFileAdapter;
import bert.deal_file.DealFile2Strings;
import bert.deal_file.GeneralDealAdapter;
import bert.extract.ColumnExtract;
import bert.single.BertResultSingle;
import bert.single.BertSingleResponse;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import config.ApplicationProperties;
import config.FileProperties;
import config.PropertiesFactory;
import deal.AbstractDealFileWay;
import deal.DealFileWay;
import delete.column.ColumnCsvDeal;
import delete.column.ColumnCsvExtract;
import demand.emotion_and_grade_improve.EmotionAndGradeDeal;
import demand.general.GeneralCsvDeal;
import pool.DealFile;
import pool.DealFileModify;
import request.ClassificationRequest;
import request.MultipleClassicationRequest;
import request.SingleClassificationRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: ${description}
 * @author: chenyang
 * @create: 2019-01-03
 **/
public class Model7Test {
    //正确的数量
    static int rel = 0;
    static int allCount = 0;
    private static ApplicationProperties aps = new ApplicationProperties();
    private static FileProperties fileProperties = new FileProperties();
    private static String basePath = fileProperties.getBasePath();

    public static void main(String[] args) throws IOException {


        testSingle();

//        testMultiple();
    }


    private static void testMultiple() throws IOException {
        //获取测试数据集
        List<String[]> testList = getTestListEvent();

        //转为map,使用流操作
//        final HashMap<String, List<String>> testMap = new HashMap<>();
//        testList.stream().map(test -> {
//            if (null == testMap.get(test[1])) {
//                //初始化
//                testMap.put(test[1], new ArrayList<>());
//            }
//            testMap.get(test[1]).add(test[0]);
//            return testMap;
//        });
        //测试数据集
        HashMap<String, List<String>> testMap = new HashMap();
        for (String[] strings : testList) {
            if (null == testMap.get(strings[1])) {
                //初始化
                testMap.put(strings[1], new ArrayList<>());
            }
            testMap.get(strings[1]).add(strings[0]);
        }


        //存储测试结果
        List<String[]> resultList = new ArrayList<>();

        for(Map.Entry<String, List<String>> entry : testMap.entrySet()) {
            String[] result = multipleProcess(entry.getKey(), entry.getValue());
            resultList.add(result);
        }

        //数据写入文本
        createFile(resultList);

        System.out.println("totle：" + resultList.size());
        System.out.println("所有分类：" + allCount);
        System.out.println("一个分类：" + rel);
    }



    private static void testSingle() throws IOException {
        //获取测试数据集 -TODO:替换
//        List<String[]> testList = getTestList();
        List<String[]> testList = getTestListEvent();
        //存储测试结果
        List<String[]> resultList = new ArrayList<>();

        for (String[] testStrings : testList) {
            //获取bert请求对象
            BertRequest request = getBertRequest(testStrings[1]);

            //-TODO:替换
            String[] result = singleProcess(testStrings);
//            String[] result = multipleProcess(testStrings);

            resultList.add(result);
        }
        //数据写入文本
        createFile(resultList);

        System.out.println("totle：" + resultList.size() + "  rel:" + rel);
    }

    private static List<String[]> getTestList() throws IOException {
//        String path = basePath + "训练语料\\栏目分类\\prediction.csv";
        String path = fileProperties.getTestPath() ;

        AbstractDealFileWay abstractDealFileWay = new ColumnCsvDeal(new ColumnCsvExtract());
        DealFileModify dealFileModify = new DealFileModify(abstractDealFileWay);
        DealFile2Strings dealFile2Strings = new DealFileAdapter(dealFileModify);

        return dealFile2Strings.dealFile(path);
    }

    /**
     * 获取测试数据集
     * @return
     * @throws IOException
     */
    private static List<String[]> getTestListEvent() throws IOException {
        //栏目数据（舆情）数据提取
        ColumnExtract columnExtract =new ColumnExtract();
        return columnExtract.extract();


        //事件分类数据
//        String path = "E:\\下载\\钉钉文件\\工作资料\\bert\\事件分类\\多分类测试语料.csv";
//        String path = fileProperties.getTestPath();
//        ApplicationProperties.PrimaryProperties primaryProperties = PropertiesFactory.getProperties("emotionAndGrade");
//        String path = primaryProperties.getPath();
//        Object labelHeader = primaryProperties.getLabel();
//        Object titleHeader = primaryProperties.getTitle();
//        Object contentHeader = primaryProperties.getContent();
//
//        DealFileWay dealCsvWay = new GeneralCsvDeal(labelHeader, titleHeader, contentHeader);
//        DealFile2Strings dealFile2Strings = new GeneralDealAdapter(dealCsvWay);
//
//        //TODO:修改
//        List<String[]> tempList = dealFile2Strings.dealFile(path);
//        List<String[]> resultList = new ArrayList<>();

        //舆情情感&等级数据
//        String path = aps.getEmotionAndGradeProperties().getPath();
//        Object labelHeader_1 = aps.getEmotionAndGradeProperties().getLabel_1();
//        Object labelHeader_2 = aps.getEmotionAndGradeProperties().getLabel_2();
//        Object[] labelHeaders = {labelHeader_1, labelHeader_2};
//        Object titleHeader = aps.getEmotionAndGradeProperties().getTitle();
//
//        DealFileWay dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader);
//        DealFile2Strings dealFile2Strings = new GeneralDealAdapter(dealFileWay);
//        List<String[]> tempList = dealFile2Strings.dealFile(path);
//
//        List<String[]> resultList = new ArrayList<>();
//
//        //截取未参加训练的数据
//        int num = 0;
//        for (int i = 0; i < 7; i++) {
//            resultList.addAll(tempList.subList((i + 1) * 13000, (i + 1) * 13000 + 1300));
//        }
//
//        return resultList;

    }

    private static String[] multipleProcess(String testString, List<String> labelList) {
        //获取bert请求对象
        BertRequest request = getBertRequest(testString);
        //多分类请求
        ClassificationRequest<BertResponse> classificationRequest = new MultipleClassicationRequest(request, BertResponse.class);

        //获取分类结果
        List<PredictionUnit> predictionUnitList =  ((MultipleClassicationRequest) classificationRequest).getResultMultiple(labelList.size() + 1);


        //获取验证结果标签集
        List<String> labelResopnse = new ArrayList<>();
        for (PredictionUnit predictionUnit : predictionUnitList) {
            labelResopnse.add(predictionUnit.getLabel());
        }

        //对比标签，一个在
        for (String label : labelList) {
            if (labelResopnse.contains(label)) {
                rel++;
                break;
            }
        }
        //对比标签，所有都在
        int tempNum = 0;
        for (String label : labelList) {
            if (! labelResopnse.contains(label)) {
                break;
            }
            tempNum ++;
        }
        if (tempNum == labelList.size()) {
            allCount++;
        }

        int resultSize = labelList.size() + predictionUnitList.size() * 2 + 2;
        String[] result = new String[resultSize];
        int resultIndex = 0;
        for (String label : labelList) {
            result[resultIndex++] = label;
        }
        result[resultIndex++] = "";
        result[resultIndex++] = "";
        for (PredictionUnit predictionUnit : predictionUnitList) {
            result[resultIndex++] = predictionUnit.getLabel();
            result[resultIndex++] = predictionUnit.getScore().toString();
        }


        return result;
    }

    private static String[] singleProcess(String[] testStrings) {
        BertRequest request = getBertRequest(testStrings[1]);

        //单分类请求
        ClassificationRequest<BertSingleResponse> classificationRequest = new SingleClassificationRequest( request, BertSingleResponse.class);

        //获取分类结果
        BertResultSingle bertResult = classificationRequest.getResult();

        //获取测试（标准）标签
//            int label = (Integer.parseInt(ss[2]) - 1) * 3 + Integer.parseInt(ss[3]);
        String label = testStrings[0];
        //对比标签
        if (bertResult.getLabel().equals(String.valueOf(label))) {
            rel++;
        }
        String[] result = new String[3];
        //测试标签
        result[0] = String.valueOf(label);
        //预测标签
        result[1] = bertResult.getLabel();
        //预测概率
        result[2] = String.valueOf(bertResult.getScore());
        return result;
    }

    /**
     * 获取bert请求对象
     *
     * @param article 从文本中提取的一条数据
     * @return
     */
    private static BertRequest getBertRequest(String article) {
        BertRequest bertRequest = new BertRequest();
        //获取文章
        String text = article;
        List<String> list = new ArrayList<>();
        list.add(text);

        bertRequest.setId("1")
                .setTexts(list);
//                .set_tokenized(false);
        return bertRequest;
    }

    /**
     * 将数据集生成csv文件
     *
     * @param resultList 待生成的结果集
     * @throws IOException
     */
    private static void createFile(List<String[]> resultList) throws IOException {
//        CSVWriter writer = new CSVWriter(new FileWriter("E:\\下载\\钉钉文件\\工作资料\\create\\栏目分类\\result.csv"));
        CSVWriter writer = new CSVWriter(new FileWriter(fileProperties.getCreatePath()));

        writer.writeAll(resultList);
        writer.close();
    }
}
