package old.emotion_grade;

import com.csvreader.CsvReader;
import config.ApplicationProperties;
import deal.DealFileWay;
import demand.general.process.ArticleProcess;
import demand.general.CountControl;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import util.FileUtil;
import util.StringsUtilCustomize;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提取emotion + grade数据，提升后
 *
 * @Author: zhuzw
 * @Date: 2019/9/23 16:12
 * @Version: 1.0
 */
public class EmotionAndGradeDeal implements DealFileWay {
    @Setter
    char separator = ',';
    /**
     * 变量
     */
    @Setter
    private ApplicationProperties aps;
    /**
     * 行数据处理
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

    /**
     * 需要提取的标题
     */
    private Object[] headers;

    /**
     * 是否提取content
     */
    private boolean contentExtract = false;

    public EmotionAndGradeDeal(Object labelHeader, Object titleHeader) {
        this.labelHeader = labelHeader;
        this.titleHeader = titleHeader;
    }

    public EmotionAndGradeDeal(Object[] labelHeaders, Object titleHeader) {
        this.labelHeaders = labelHeaders;
        this.titleHeader = titleHeader;
    }

    public EmotionAndGradeDeal(Object[] labelHeaders, Object titleHeader, Object contentHeader) {
        this.labelHeaders = labelHeaders;
        this.titleHeader = titleHeader;
        this.contentHeader = contentHeader;
        contentExtract = true;
    }

    public EmotionAndGradeDeal(Object labelHeader, Object titleHeader, Object contentHeader) {
        this.labelHeader = labelHeader;
        this.titleHeader = titleHeader;
        this.contentHeader = contentHeader;
        contentExtract = true;
    }

    /**
     * 从文件中提取数据
     *
     * @param csvPath 文件路径
     * @return 舆情情感\t标题\t内容
     */
    @Override
    public List<String> extractedValue(String csvPath) {
        List<String> resultList = new ArrayList<>();

        HashMap<String, List<String>> tempResultMap = new HashMap<>();

        //获取csvRead
        CsvReader csvReader = FileUtil.getCsvReader(csvPath, separator);
        String title = "";
        String content = "";
        LocalTime localTime1 = LocalTime.now();
        // 逐条读取记录，直至读完
        String temp = "";
        try {
            if (haveHeader()) {
                csvReader.readHeaders();
            }
            while (csvReader.readRecord()) {
                //每次初始化labels数组，解决数量控制死循环bug
                List<String> labels = new ArrayList<>();
                //从文件中获取值 - 不变
                if (haveHeader()) {

                    for (Object labelHeader : labelHeaders) {
                        labels.add(csvReader.get((String) labelHeader));
                    }

                    title = csvReader.get((String) titleHeader);
                    content = csvReader.get((String) contentHeader);

                } else {
                    for (Object labelHeader : labelHeaders) {
                        labels.add(csvReader.get((Integer) labelHeader));
                    }
                    title = csvReader.get((Integer) titleHeader);
                    content = csvReader.get((Integer) contentHeader);
                }


                //是否为空 - 变
                boolean isContinue = false;
                for (String label : labels) {
                    if (StringUtils.isEmpty(label)) {
                        isContinue = true;
                    }
                }
                if (isContinue || StringsUtilCustomize.isEmpty(title)) {
                    continue;
                }
                if (contentExtract) {
                    //判断content是否为空
                    if (StringUtils.isEmpty(content)) {
                        continue;
                    }
                }

                String label = articleProcess.getLabel(labels.get(0), labels.get(1));

                //数量控制
                int operationStatus = countControl.operationStatus(label, aps.getPrimaryProperties().getDataCount(), aps.getPrimaryProperties().getLabelNumber());
                if (-1 == operationStatus) {
                    break;
                } else if (0 == operationStatus) {
                    continue;
                }

                //提取一行数据 - 变
                String rowValue = articleProcess.extractRowValue(new String[]{labels.get(0), labels.get(1)}, title, content);
                temp = rowValue;
                //统计文章长度    (截取前面的标签)

                countControl.rangeStatistics(rowValue.split("\t")[1]);

                if (null == tempResultMap.get(label)) {
                    tempResultMap.put(label, new ArrayList<String>());
                }
                tempResultMap.get(label).add(rowValue);

                //提醒
                remind(tempResultMap, 5000);

            }
        } catch (Exception e) {
            System.out.println(temp);
            e.printStackTrace();
        } finally {
            if (null != csvReader) {
                csvReader.close();
            }
        }
        LocalTime localTime2 = LocalTime.now();
        System.out.println("耗时：" + (localTime2.toSecondOfDay() - localTime1.toSecondOfDay()));
        //数据提取情况
        dataSituation(countControl, tempResultMap);
        //添加数据
        resultList = addData(resultList, tempResultMap);
        return resultList;
    }

    /**
     * 处理提醒
     *
     * @param resultMap
     * @param count     提醒步长
     */
    private void remind(HashMap<String, List<String>> resultMap, int count) {

        int size = 0;
        for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
            size += entry.getValue().size();
        }
        if (0 == size % count) {
            System.out.println("已处理：" + (remindCount += count));
        }
    }

    /**
     * 是否有标题
     *
     * @return
     */
    private boolean haveHeader() {

        return aps.getPrimaryProperties().isHaveHeader();
    }


}
