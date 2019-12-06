import config.ApplicationCache;
import config.ApplicationProperties;
import config.factory.ExtractFileFactory;
import config.factory.PropertiesFactory;
import demand.RequestSqlThread;
import demand.agent.GeneralAgent;
import demand.general.record.CsvRowRecordExtract;
import demand.general.record.RowRecordExtract;
import demand.mapping.CmdRunner;
import demand.mapping.service.LoadService;
import org.apache.commons.lang3.StringUtils;
import util.FileUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/18 10:29
 * @Version: 1.0
 */
public class GeneralClient {
    private static GeneralAgent generalAgent = new GeneralAgent();

    private static ApplicationProperties aps = new ApplicationProperties();

    private static LoadService loadService = new LoadService();

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        CmdRunner cmdRunner = new CmdRunner();
        try {
            cmdRunner.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        eventAndEmotionClassify();
//        spliceExtract();
//        articleExtract();

        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));
    }


    /**
     * 提取事件映射情感的映射规则
     *
     * @throws IOException
     */
    private static void spliceEventMappingEmotionExtract() throws IOException {
        aps.setPrimaryProperties(PropertiesFactory.getProperties("splice"));

//        aps.getPrimaryProperties().setExtractIndexs(new int[3, ]);

/**
 *      *      extractIndexs   提取Index集合
 *      *      optionalExtractIndexs 可选的index集合
 *      *      haveHeader:  是否有标题\
 *      *      createFile: 是否生成文件
 *      *      removeRepeat: 是否去重
 */
        RowRecordExtract rowRecordExtract = ExtractFileFactory.getRowRecordExtract("excel");

        generalAgent.spliceExtract(aps, rowRecordExtract);
    }

    /**
     * 自定义提取（结果直接拼接）
     *
     * @throws IOException
     */
    private static void spliceExtract() throws IOException {
        aps.setPrimaryProperties(PropertiesFactory.getProperties("splice"));

        RowRecordExtract rowRecordExtract = ExtractFileFactory.getRowRecordExtract("excel");

        generalAgent.spliceExtract(aps, rowRecordExtract);
    }

    /**
     * 文章提取
     *
     * @throws IOException
     */
    private static void articleExtract() throws IOException, SQLException, ClassNotFoundException {
        String path = "";
        ApplicationProperties aps = setAps(path);

        RowRecordExtract rowRecordExtract = ExtractFileFactory.getRowRecordExtract("csv");

        List<String> resultList = generalAgent.articleExtract(aps, rowRecordExtract);


    }


    /**
     * 按事件分类 * 情感分类   进行分类
     */
    private static void eventAndEmotionClassify() throws ClassNotFoundException {
        String path = "E:\\文件\\工作\\AI\\bert\\原始语料\\舆情语料.csv";
        ApplicationProperties aps = setAps(path);

        RowRecordExtract rowRecordExtract = ExtractFileFactory.getRowRecordExtract("csv");
        ((CsvRowRecordExtract)rowRecordExtract).setStartIndex(304227);

        List<String> resultList = generalAgent.spliceExtract(aps, rowRecordExtract);
        System.out.println("待处理数量：" + resultList.size());
        System.out.println("最后的Id：" + resultList.get(resultList.size() - 1).split("\t")[0]);

        List<String> resultList2019 = null;
        try {
            resultList2019 = find2019List(resultList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("最后的Id：" + resultList2019.get(resultList2019.size() - 1).split("\t")[0]);
        //统计并输出
        statisticsAndPrint(resultList2019);
    }

    private static void statisticsAndPrint(List<String> resultList) {
        String path = "E:\\文件\\工作\\AI\\bert\\原始语料\\分类\\";
        Map<String, String> eventMap = loadService.loadEventClassify();
        HashMap<String, List<String>> resultMap = new HashMap<>();
        for (String str : resultList) {
            String[] strArr = str.split("\t");
            String emotion = strArr[1];
            String emotionGrade = strArr[2];
            String eventId = Float.valueOf(strArr[3]).toString();
            String eventName = eventMap.get(eventId);
            //key: eventId_eventName_emotion_grade      value：all value
            String key = eventId.split("\\.")[0] + "_" + eventName + "_" + emotion + "_" + emotionGrade;
            if (null == resultMap.get(key)) {
                resultMap.put(key, new ArrayList<>());
            }
            resultMap.get(key).add(str);
        }
        //输出
        for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
            String categoryPath = path + entry.getKey() + ".tsv";
            try {
                FileUtil.createFile(entry.getValue(), categoryPath, true);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件出错：" + path);
                ApplicationCache.saveCache(entry.getValue(), categoryPath);
            }
        }

    }

    private static List<String> find2019List(List<String> list) throws ClassNotFoundException, SQLException {
        String databaseUrl = "jdbc:sqlserver://10.105.0.100;DatabaseName=JYPRIME";
        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //连接，设置用户名和密码

        conn = DriverManager.getConnection(databaseUrl, "JYPRIME_READ", "JYPRIME_READ");

        final Statement stmt = conn.createStatement();

        String sql = "select XGSJ from usrYQZB where ZQBID like ";

        System.out.println("开始查找...");

//        int num = 8;
//        //启动多线程
//        int baseNum = list.size() / num;
//        int end = 0;
//        final CountDownLatch latch = new CountDownLatch(num);
//        for (int i = 0; i < num; i++) {
//            int start = i * baseNum;
//            end = start + baseNum;
//            if (i == (num - 1)) {
//                //最后一次，处理所有
//                end = list.size();
//            }
//            RequestSqlThread requestThread = new RequestSqlThread("线程[" + (i + 1) + "] ", list.subList(start, end), stmt,sql);
//            requestThread.start();
//
//        }
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("处理完毕：" );
//        List<String> resultList = RequestSqlThread.resultList.stream().collect(Collectors.toList());


        List<String> resultList = new ArrayList<>();
        int i = 0;
        for (String str : list) {
            String id = str.split("\t")[0];
            ResultSet resultSet = null;
            try {
                resultSet = stmt.executeQuery(sql + "'" + id + "'");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("最后的ID:" + resultList.get(resultList.size() - 1));
                return resultList;
            }
            if (resultSet.next() && resultSet.getString("XGSJ").contains("2019")) {
//                if (StringUtils.isEmpty(resultSet.getString("XGSJ")) && resultSet.getString("XGSJ").contains("2019"))
                    resultList.add(str);
            }
            list.set(i, null);
            i++;
            //提醒
            if(i % 2000 == 0) {
                System.out.println("已处理：" + i);
            }
        }

        //转list
        System.out.println("处理数量：" + resultList.size());
        return resultList;
    }


    private static ApplicationProperties setAps(String path) {
        ApplicationProperties aps = new ApplicationProperties();
        aps.getPrimaryProperties().setPath(path);
        aps.getPrimaryProperties().setExtractIndexs(new int[]{0, 2, 3, 5, 8});
//        aps.getPrimaryProperties().setExtractIndexs(new int[]{0});
        aps.getPrimaryProperties().setOptionalExtractIndexs(new int[]{9});
        aps.getPrimaryProperties().setDataCount(-1);
        aps.getPrimaryProperties().setHaveHeader(true);
        aps.getPrimaryProperties().setCreateFile(false);
        aps.getCreateFile().setDirectPath("E:\\文件\\工作\\AI\\bert\\原始语料\\effective.tsv");
        return aps;
    }
}
