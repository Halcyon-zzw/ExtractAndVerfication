package demand.general.record;

import com.csvreader.CsvReader;
import lombok.Setter;
import util.FileUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/17 1:12
 * @Version: 1.0
 */
public class CsvRowRecordExtract implements RowRecordExtract {

    private static CsvReader csvReader;
    /**
     * 是否有标题，默认没有
     */
    private boolean haveHearder = false;
    private String path;
    private char separator = ',';
    private Charset encodingFormat = Charset.forName("UTF-8");
    @Setter
    private int startIndex = -1;
    public static int currentIndex = 0;


    public CsvRowRecordExtract() {
    }

    public CsvRowRecordExtract(char separator) {
        this.separator = separator;
    }

    public CsvRowRecordExtract(Charset encodingFormat) {
        this.encodingFormat = encodingFormat;
    }

    public CsvRowRecordExtract(char separator, Charset encodingFormat) {
        this.separator = separator;
        this.encodingFormat = encodingFormat;
    }

    @Override
    public void init(String path) {
        csvReader = FileUtil.getCsvReader(path, separator, encodingFormat);
    }

    public void init(String path, char separator, Charset encodingFormat) {
        csvReader = FileUtil.getCsvReader(path);
    }

    @Override
    public List<String[]> getRawRecords() throws IOException {
        return getRawRecords(1000);
    }

    @Override
    public List<String[]> getRawRecords(int count) throws IOException {
        int tempCount = 0;
        List<String[]> resultList = null;
        if (haveHearder) {
            csvReader.readHeaders();
        }
        //将数量判断条件放在前面，否则出现该行读出后未加到结果集中
        while (tempCount < count && csvReader.readRecord()) {
            if (currentIndex < startIndex) {
                currentIndex ++;
                continue;
            }
            if (null == resultList) {
                resultList = new ArrayList<>();
            }
            resultList.add(csvReader.getValues());
            tempCount++;
            currentIndex ++;
        }
        return resultList;
    }
}
