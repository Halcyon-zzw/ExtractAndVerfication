package deal;

import com.csvreader.CsvReader;
import old.AbstractExtractCsvValue;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理文件方式接口
 *
 * @Author: zhuzw
 * @Date: 2019/8/7 19:46
 * @Version: 1.0
 *
 * 经提取，处理文件方式已提取成通用流程
 * 1、获取文件 -> 2、一行一行读 -> 3、是否处理 -> 4、提取一行数据 -> 5、添加数据到list
 * 1、获取文件： 定义获取文件操作类
 * 3、ColumnProcess类
 * 4、Extract一行类
 *
 * 故将接口类更改为抽象类
 *
 * 处理文件方式抽象类
 * @Author: zhuzw
 * @Date: 2019/8/29 9:07
 * @Version: 2.0
 */
public abstract class AbstractDealFileWay {
    //提取数据
    private AbstractExtractCsvValue extractValue;

    public AbstractDealFileWay(AbstractExtractCsvValue extractValue) {
        this.extractValue = extractValue;
    }


    /**
     * 提取文件值，具体提取方法在实现类中实现
     *
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public List<String> extractedValue(String path) {

        //获取csvRead
        CsvReader csvReader = FileUtil.getCsvReader(path);

        extractValue.setCsvReader(csvReader);
        List<String> resultList = new ArrayList<>();


        // 逐条读取记录，直至读完
        try {
            if (isReadHead()) {
                csvReader.readHeaders();
            }

            while (csvReader.readRecord()) {
                //是否处理
                if (!isDeal(csvReader)) {
                    continue;
                }
                //控制数量


                //提取一行数据
                String rowValue = extractValue.extractRowValue();
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
     * 判断是否处理该行
     * @param csvReader 文件处理对象
     * @return
     */
    protected abstract boolean isDeal(Object csvReader);

    /**
     * 是否读标题, 默认true
     * @return
     */
    protected boolean isReadHead() {
        return true;
    }

}
