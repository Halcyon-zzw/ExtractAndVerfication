package test;

import bert.BertRequest;
import bert.single.BertResultSingle;
import bert.single.BertSingleResponse;
import request.ClassificationRequest;
import request.SingleClassificationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/10/21 13:54
 * @Version: 1.0
 */
public class RequestThread extends Thread {
    private Thread t;
    public static ConcurrentLinkedDeque<String[]> resultList = new ConcurrentLinkedDeque<>();;
    private BertRequest bertRequest;
    private ClassificationRequest<BertSingleResponse> classificationRequest;
    private String threadName;
    private List<String[]> list;

    private String url;
//    private int startIndex;
//    private int endIndex;
//    public static WebClient webClient = WebClient.create("http://10.106.0.51:8007/encode");

    public RequestThread(String threadName, List<String[]> list) {

        this.threadName = threadName;
        this.list = list;
        bertRequest = new BertRequest();
        classificationRequest = new SingleClassificationRequest(bertRequest, BertSingleResponse.class);
    }

    public RequestThread(String threadName, List<String[]> list, String url) {
        this(threadName, list);
        this.url = url;
    }

    /**
     * 获取bert请求对象
     *
     * @param article 从文本中提取的一条数据
     * @return
     */
    private BertRequest getBertRequest(String article) {
        //if (null == bertRequest) {

        //}

        //获取文章
        String text = article;
        List<String> list = new ArrayList<>();
        list.add(text);

        bertRequest.setId("1")
                .setTexts(list);
//                .set_tokenized(false);
        return bertRequest;
    }

    private  String[] singleProcess(String[] testStrings) {
        BertRequest request = getBertRequest(testStrings[1]);

        //单分类请求
//        if (null == classificationRequest) {

//        }
        classificationRequest.setBertRequest(request);
        classificationRequest.setResponseType(BertSingleResponse.class);
        classificationRequest.setUrl(url);
        //获取分类结果
        BertResultSingle bertResult = classificationRequest.getResult();

//        Mono<BertResultSingle> responseMono = webClient.post().syncBody(request).retrieve().bodyToMono(BertResultSingle.class);
//        BertResultSingle bertResult = responseMono.block();


        //获取测试（标准）标签
//            int label = (Integer.parseInt(ss[2]) - 1) * 3 + Integer.parseInt(ss[3]);
        String label = testStrings[0];
        String[] result = new String[6];
        //标准标签
        result[0] = mappingLabel2(String.valueOf(label));
        //预测标签
        String predictionLabel = bertResult.getLabel();
        result[1] = mappingLabel2(predictionLabel);


        //预测概率
        result[2] = String.valueOf(bertResult.getScore());
        //原文
        result[3] = testStrings[1];

        if (equalsNature(result[0], result[1])) {
            result[4] = "true";
        }else {
            result[4] = "false";
        }

        if (equalsComplete(result[0], result[1])) {
            result[5] = "true";
        }else {
            result[5] = "false";
        }
        return result;
    }

    /**
     * 比较结果正负性正确性
     * @param s
     * @param s1
     */
    private boolean equalsNature(String s, String s1) {
        if (s.substring(0, 2).equalsIgnoreCase(s1.substring(0, 2))) {
            return true;
        }
        return false;
    }

    private boolean equalsComplete(String s, String s1) {
        if (s.equalsIgnoreCase(s1)) {
            return true;
        }
        return false;
    }

    @Override
    public void run() {
//        List<String[]> subList = list.subList(startIndex, endIndex);
        System.out.println(threadName + "处理了" + list.size() + "条！");
        for (String[] testStrings : list) {
//            System.out.println(testStrings[1].substring(0, 20));
            String[] result = singleProcess(testStrings);
//            String[] result = {};
            resultList.add(result);
//            System.out.println(resultList.size());

        }
    }

//    public void start () {
//        System.out.println("Starting " +  threadName );
//        if (t == null) {
//            t = new Thread (this, threadName);
//            t.start ();
//        }
//    }

    private String mappingLabel2(String label) {
        switch (label) {
            case "101":
                return  "负面一星";
            case "102":
                return  "负面二星";
            case "103":
                return  "负面三星";
            case "200":
                return  "中性";
            case "301":
                return  "正面一星";
            case "302":
                return  "正面二星";
            case "303":
                return  "正面三星";
            default:
                return "000";
        }
    }

    private String mappingLabel(String label) {
        switch (label) {
            case "1":
                return  "负面一星";
            case "2":
                return  "负面二星";
            case "3":
                return  "负面三星";
            case "6":
                return  "中性";
            case "7":
                return  "正面一星";
            case "8":
                return  "正面二星";
            case "9":
                return  "正面三星";
            default:
                return "000";
        }
    }
}
