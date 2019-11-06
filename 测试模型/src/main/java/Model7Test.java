import bert.BertRequest;
import bert.BertResponse;
import bert.PredictionUnit;
import bert.deal_file.DealFile2Strings;
import bert.deal_file.DealFileAdapter;
import bert.extract.EmotionAndGradeExtract;
import bert.single.BertResultSingle;
import bert.single.BertSingleResponse;
import com.opencsv.CSVWriter;
import config.ApplicationProperties;
import config.FileProperties;
import deal.AbstractDealFileWay;
import delete.column.ColumnCsvDeal;
import delete.column.ColumnCsvExtract;
import pool.DealFileModify;
import request.ClassificationRequest;
import request.MultipleClassicationRequest;
import request.SingleClassificationRequest;
import test.RequestThread;
import util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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

        for (Map.Entry<String, List<String>> entry : testMap.entrySet()) {
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
//        int testNum = 100;
//        testList = testList.subList(0, testNum);

        LocalTime startTime = LocalTime.now();


        int length = testList.size();
        //初始线程数
        int num = 6;
        String[] urls = {
                "http://10.106.0.51:8050/encode", "http://10.106.0.51:8051/encode",
                "http://10.106.0.51:8052/encode", "http://10.106.0.51:8053/encode",
                "http://10.106.0.51:8054/encode", "http://10.106.0.51:8055/encode",
        };

        //启动多线程
        int baseNum = length / num;
        int end = 0;
        final CountDownLatch latch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            int start = i * baseNum;
            end = start + baseNum;
            if (i == (num - 1)) {
                //最后一次，处理所有
                end = length;
            }
            RequestThread requestThread = new RequestThread("线程[" + (i + 1) + "] ", testList.subList(start, end), urls[i]);
            requestThread.start();
//            try {
//
//                requestThread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
//        for (String[] testStrings : testList) {
//            //-TODO:替换  请求
//            String[] result = singleProcess(testStrings);
////            String[] result = multipleProcess(testStrings);
//            resultList.add(result);
//        }
        while (RequestThread.resultList.size() < testList.size()) {
//            System.out.println(RequestThread.resultList.size());
        }
//        System.out.println("hello-------------------------");
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //存储测试结果
        List<String[]> resultList = new ArrayList<>();
        System.out.println("处理长度：" + RequestThread.resultList.size());
        for (String[] strings : RequestThread.resultList) {
            resultList.add(strings);
            //对比标签
            if (strings[0].equals(strings[1])) {
                rel++;
            }
        }
        LocalTime endTime = LocalTime.now();
//        //耗时
        long consumingTime = (long)endTime.toSecondOfDay() - (long)startTime.toSecondOfDay();
        System.out.println("请求结束，耗时：" + consumingTime  + "秒");



        //数据写入文本
        createFile(resultList);

        String resultString = "总量：" + resultList.size() + "\n";
        resultString += "正确数量：" + rel + "\n";
        float correctRate = (float) rel / resultList.size();
        resultString += "正确率：" + correctRate + "\n";
        System.out.println(resultString);
        List<String> temp = new ArrayList<>();
        temp.add(resultString);
        FileUtil.createFile(temp, fileProperties.getCreatePath2());
    }

    private static List<String[]> getTestList() throws IOException {
//        String path = basePath + "训练语料\\栏目分类\\prediction.csv";
        String path = fileProperties.getTestPath();

        AbstractDealFileWay abstractDealFileWay = new ColumnCsvDeal(new ColumnCsvExtract());
        DealFileModify dealFileModify = new DealFileModify(abstractDealFileWay);
        DealFile2Strings dealFile2Strings = new DealFileAdapter(dealFileModify);

        return dealFile2Strings.dealFile(path);
    }

    /**
     * 获取测试数据集
     *
     * @return
     * @throws IOException
     */
    private static List<String[]> getTestListEvent() throws IOException {
        //栏目数据（舆情）数据提取
//        ColumnExtract columnExtract =new ColumnExtract();
//        return columnExtract.extract();

        //舆情情感&等级数据
        EmotionAndGradeExtract emotionAndGradeExtract = new EmotionAndGradeExtract();
        return emotionAndGradeExtract.extract();


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


    }

    private static String[] multipleProcess(String testString, List<String> labelList) {
        //获取bert请求对象
        BertRequest request = getBertRequest(testString);
        //多分类请求
        ClassificationRequest<BertResponse> classificationRequest = new MultipleClassicationRequest(request, BertResponse.class);

        //获取分类结果
        List<PredictionUnit> predictionUnitList = ((MultipleClassicationRequest) classificationRequest).getResultMultiple(labelList.size() + 1);


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
            if (!labelResopnse.contains(label)) {
                break;
            }
            tempNum++;
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
        Path path = Paths.get(fileProperties.getCreatePath());
        File file = path.getParent().toFile();
        if (!file.exists()) {
            file.mkdir();
        }
        CSVWriter writer = new CSVWriter(new FileWriter(fileProperties.getCreatePath()));

        writer.writeAll(resultList);
        writer.close();
    }
}
