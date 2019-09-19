package demand.emotion_and_grade;


import com.csvreader.CsvReader;
import deal.DealFileWay;
import delete.EmotionCsvProcess;
import extract.*;

import process.ColumnProcess;
import process.impl.*;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 提取舆情情感&等级数据
 *
 * @Author: zhuzw
 * @Date: 2019/8/8 13:14
 * @Version: 1.0
 */
public class EmotionAndGradeCsvDeal implements DealFileWay {

    /**
     * 取的舆情情感数据量
     */
    private final int EMOTION_COUNT = 100000;

    private final ColumnProcess titleCsvProcess = new TitleCsvProcess();
    private final ColumnProcess contentCsvProcess = new ContentCsvProcess();
    private final ColumnProcess emotionCsvProcess = new EmotionCsvProcess();
    private final ColumnProcess gradeCsvProcess = new GradeCsvProcess();
    private final ColumnProcess emotionAndGradeCsvProcess = new EmotionAndGradeCsvProcess();
    private final AbstractExtractCsvValue extractEmotionAndGrade = new EmotionAndGradeCsvExtract();

    /**
     * 提取舆情情感数据
     *
     * @param csvPath
     * @return 舆情情感\t标题\t内容
     */
    @Override
    public List<String> extractedValue(String csvPath) {
        List<String> resultList = new ArrayList<>();

        //获取csvRead
        CsvReader csvReader = FileUtil.getCsvReader(csvPath);
        // 逐条读取记录，直至读完
        try {
            csvReader.readHeaders();

            while (csvReader.readRecord()) {
                //是否处理
                if (!isDeal(csvReader)) {
                    continue;
                }
                //提取一行数据
                String rowValue = extractedRowValue(csvReader);
                resultList.add(rowValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != csvReader) {
                csvReader.close();
            }
        }
        return resultList;
    }

    /**
     * 提取一行数据的值
     *
     * @param csvReader 行数据
     * @return
     */
    private String extractedRowValue(CsvReader csvReader) {
        extractEmotionAndGrade.setCsvReader(csvReader);
        return extractEmotionAndGrade.extractRowValue();
    }

    /**
     * 判断是否处理该行
     *
     * @param csvReader
     */
    private boolean isDeal(CsvReader csvReader) throws IOException {
        return titleCsvProcess.isProcess(csvReader)
                && contentCsvProcess.isProcess(csvReader)
                && emotionCsvProcess.isProcess(csvReader)
                && gradeCsvProcess.isProcess(csvReader)
                && emotionAndGradeCsvProcess.isProcess(csvReader);
    }

}
