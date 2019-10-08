package demand.emotion_and_grade_improve;

import com.csvreader.CsvReader;
import config.ApplicationProperties;
import deal.DealFileWay;
import demand.emotion_and_grade_improve.EmotionAndGradeDataProcess;
import demand.general.CountControl;
import demand.general.RowValueProcess;
import org.apache.commons.lang3.StringUtils;
import util.FileUtil;
import util.StringsUtilCustomize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/9/23 16:12
 * @Version: 1.0
 */
public class EmotionAndGradeDeal implements DealFileWay {
    /**
     * 变量
     */
    private final ApplicationProperties aps = new ApplicationProperties();

    /**
     * 行数据处理
     */
    private RowValueProcess dataProcess = new EmotionAndGradeDataProcess();

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


    public EmotionAndGradeDeal (Object labelHeader, Object titleHeader) {
        this.labelHeader = labelHeader;
        this.titleHeader = titleHeader;
    }

    public EmotionAndGradeDeal (Object[] labelHeaders, Object titleHeader) {
        this.labelHeaders = labelHeaders;
        this.titleHeader = titleHeader;
    }

    public EmotionAndGradeDeal(Object labelHeader, Object titleHeader, Object contentHeader) {
        this.labelHeader = labelHeader;
        this.titleHeader = titleHeader;
        this.contentHeader = contentHeader;
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
        CsvReader csvReader = FileUtil.getCsvReader(csvPath);
        System.out.println("开始处理...");


        String title = "";
        String content = "";
        // 逐条读取记录，直至读完
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

                String label = dataProcess.getLabel(labels.get(0), labels.get(1));

                //数量控制
                int operationStatus = countControl.operationStatus(label, aps.getEmotionAndGradeProperties().getDataCount(), aps.getEmotionAndGradeProperties().getLabelNumber());
                if (-1 == operationStatus) {
                    break;
                } else if (0 == operationStatus) {
                    continue;
                }

                //提取一行数据 - 变
                String rowValue = dataProcess.extractRowValue(new String[]{labels.get(0), labels.get(1)}, title, content);

                if (null == tempResultMap.get(label)) {
                    tempResultMap.put(label, new ArrayList<String>());
                }
                tempResultMap.get(label).add(rowValue);

                //提醒
                remind(tempResultMap, 5000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != csvReader) {
                csvReader.close();
            }
        }

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
     * @param count      提醒步长
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
