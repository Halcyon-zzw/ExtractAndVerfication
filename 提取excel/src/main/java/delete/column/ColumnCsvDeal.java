package delete.column;

import com.csvreader.CsvReader;
import deal.AbstractDealFileWay;
import extract.AbstractExtractCsvValue;
import process.ColumnProcess;
import process.impl.ContentCsvProcess;
import process.impl.TitleCsvProcess;

/**
 * 处理Csv栏目(27个分类，其中包含数量较少的分类待剔除)数据
 *
 * 使用父类的extractValue()，只重写isDeal()方法
 *
 * @Author: zhuzw
 * @Date: 2019/8/29 8:34
 * @Version: 1.0
 */
public class ColumnCsvDeal extends AbstractDealFileWay {
    //文章标题处理
    private final ColumnProcess titleCsvProcess = new TitleCsvProcess();
    //文章内容处理
    private final ColumnProcess contentCsvProcess = new ContentCsvProcess();
    //栏目分类（27类）处理
    private final ColumnProcess columnCsvProcess = new ColumnCsvProcess();


    public ColumnCsvDeal(AbstractExtractCsvValue extractValue) {
        super(extractValue);
    }

    /**
     * 判断是否处理该行
     *
     * @param objectReader
     */
    @Override
    public boolean isDeal(Object objectReader) {
        CsvReader csvReader = (CsvReader)objectReader;
        //需要添加通过下标获取判断
//        return titleCsvProcess.isProcess(csvReader)
//                && contentCsvProcess.isProcess(csvReader)
//                && columnCsvProcess.isProcess(csvReader);
        return columnCsvProcess.isProcess(csvReader);
    }

    /**
     * 没有标题，不读标题
     * @return
     */
    @Override
    public boolean isReadHead() {
        return false;
    }
}
