package demand.general;

import config.ApplicationProperties;
import deal.DealFileWay;
import demand.general.process.ArticleProcess;
import demand.general.process.process_way.InvalidProcess;
import demand.general.process.ProcessWay;
import demand.general.record.RowRecordExtract;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 常规数据处理,
 * 仅提取三列label、title、content；
 * 需扩展重写extract方法
 *
 * @Author: zhuzw
 * @Date: 2019/11/16 23:30
 * @Version 2.0
 * 添加构造函数，外部设置提取的目标，解决多次调用问题。运行时决定调用对象
 */
public class GeneralArticleDeal implements DealFileWay {

    /**
     * 变量
     */
    @Setter
    private ApplicationProperties aps = new ApplicationProperties();


    /**
     * 文章处理方式，默认InvalidProcess
     */
    @Setter
    private ProcessWay processWay;
    /**
     * 行数据处理,默认ArticleProcess
     */
    @Setter
    private ArticleProcess articleProcess;

    /**
     * 数量控制
     */
    private CountControl countControl = new CountControl();

    /**
     * 处理的数量
     */
    private int remindCount = 0;

    private Object labelHeader;
    private Object[] labelHeaders;
    private Object titleHeader;
    private Object contentHeader;

    private boolean labelExtract = true;
    private boolean titleExtract = true;
    private boolean contentExtract = true;

    private RowRecordExtract rowRecordExtract;

    @Setter
    private Charset charset = Charset.forName("UTF-8");
    @Setter
    private char separator = ',';

    public GeneralArticleDeal(RowRecordExtract rowRecordExtract, ApplicationProperties aps) {
        //默认删除无效数据
        this(rowRecordExtract, new ArticleProcess(new InvalidProcess()), aps);
    }

    public GeneralArticleDeal(RowRecordExtract rowRecordExtract,
                              ArticleProcess articleProcess,
                              ApplicationProperties aps) {
        this.rowRecordExtract = rowRecordExtract;
        this.aps = aps;
        this.articleProcess = articleProcess;
        init();
    }

    private void init() {
        this.labelHeaders = aps.getPrimaryProperties().getLabels();
        this.titleHeader = aps.getPrimaryProperties().getTitle();
        this.contentHeader = aps.getPrimaryProperties().getContent();
        Object nullObject = "";
        Object zeroObject = -1;
        //设置属性为""或-1时表示不提取该列数据
        if (nullObject.equals(labelHeaders[0]) || zeroObject.equals(labelHeaders[0])) {
            labelExtract = false;
        }
        if (nullObject.equals(titleHeader) || zeroObject.equals(titleHeader)) {
            titleExtract = false;
        }
        if (nullObject.equals(contentHeader) || zeroObject.equals(contentHeader)) {
            contentExtract = false;
        }
    }


    /**
     * 从文件中提取数据
     *
     * @param path 文件路径
     * @return 舆情情感\t标题\t内容
     */
    @Override
    public List<String> extractedValue(String path) {
        //rowRecordExtract初始化
        rowRecordExtract.init(path);

        List<String> resultList = new ArrayList<>();
        HashMap<String, List<String>> tempResultMap = new HashMap<>();
        //待处理数据
        List<String[]> rawRecordList = new ArrayList<>();
        boolean countEnough = false;
        int i = 0;
        //第一次遍历
        boolean firstTime = true;

        System.out.println("开始处理...");
        //读取数据为null时结束
        while (true && rawRecordList != null) {
            if (rawRecordList.size() == i) {
                //获取新的数据列表，i初始化
                try {
                    rawRecordList = rowRecordExtract.getRawRecords();
                } catch (IOException e) {
                    System.out.println("获取数据出错！");
                    e.printStackTrace();
                }
                i = 0;
            }
            if (null == rawRecordList) {
                continue;
            }
            //剔除标题
            if (firstTime) {
                //剔除标题
                excludeHeader(rawRecordList, aps.getPrimaryProperties().isHaveHeader());
                firstTime = false;
                if (rawRecordList.size() == 0) {
                    //剔除后为长度为0
                    System.out.println(path + "  该文件剔除标题后没有内容...");
                    continue;
                }
            }
            String[] rawRecode = rawRecordList.get(i++);


            //每次初始化labels数组，解决数量控制死循环bug
            List<String> labels = new ArrayList<>();
            //从文件中获取值
            String label = "0";
            String title = "";
            String content = "";

            for (Object labelHeader : labelHeaders) {
                int labelIndex = (Integer) labelHeader;
                labels.add(rawRecode[labelIndex]);
            }
            title = rawRecode[(Integer)titleHeader];
            content = rawRecode[(Integer)contentHeader];

            //判断该行是否提取(label和title不为null)
            if (!isDealRow(labels, title)) {
                continue;
            }

            String[] labelArr = new String[labels.size()];
            for (int j = 0; j < labels.size(); j++) {
                labelArr[j] = labels.get(j);
            }
            label = articleProcess.getLabel(labelArr);
            //数量控制
            int operationStatus = countControl.operationStatus(label, aps.getPrimaryProperties().getDataCount(), aps.getPrimaryProperties().getLabelNumber());
            if (-1 == operationStatus) {
                break;
            } else if (0 == operationStatus) {
                continue;
            }

            //提取一行数据

            String rowValue = "";
            try {
                rowValue = articleProcess.extractRowValue(labelArr, title, content);
            }catch (Exception e) {
                System.out.println(e);
                System.out.println("错误label：" + label);
                System.out.println("错误title：" + title);
            }
            if (null == tempResultMap.get(label)) {
                tempResultMap.put(label, new ArrayList<String>());
            }
            tempResultMap.get(label).add(rowValue);
            resultList.add(rowValue);
            //步数提醒
            remind(tempResultMap, 5000);
        }

        //数据提取情况
        dataSituation(countControl, tempResultMap);
        if (aps.getPrimaryProperties().isFilterLessCountData()) {
            //过滤数据少的数据
            tempResultMap = fileLessData(countControl, tempResultMap, aps.getLessDataCount());
            for(Map.Entry<String, List<String>> entry: tempResultMap.entrySet()) {
                resultList.addAll(entry.getValue());
            }
            dataSituation(countControl, tempResultMap);
        }
       return resultList;
    }

    /**
     * 判断是否处理该行，
     *      通过判断labels(仅判断labels[0]，考虑到中性没有情感等级)和title是否为null
     * TODO 是否需要设置属性，
     *      每个提取列设置是否允许null的属性，
     *      通过该属性来判断是否判断
     * @param labels
     * @param title
     * @return
     */
    private boolean isDealRow(List<String> labels, String title) {
        //提取标签 && 标签为null
        if (labelExtract && StringUtils.isEmpty(labels.get(0))) {
            return false;
        }
        //提取标题 && 标签为null
        if (titleExtract && StringUtils.isEmpty(title)) {
            return false;
        }
        return true;
    }


    /**
     * 处理提醒
     *
     * @param resultList
     * @param count      提醒步长
     */
    private void remind(HashMap<String, List<String>> resultList, int count) {
        if (0 == resultList.size() % count) {
            System.out.println("已处理：" + (remindCount += count));
        }
    }
}
