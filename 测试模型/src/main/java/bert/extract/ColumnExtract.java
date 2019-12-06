package bert.extract;

import bert.deal_file.DealFile2Strings;
import bert.deal_file.GeneralDealAdapter;
import config.ApplicationProperties;
import deal.DealFileWay;
import old.GeneralCsvDeal;

import java.io.IOException;
import java.util.List;

/**
 * 情感栏目测试数据提取
 *
 * @Author: zhuzw
 * @Date: 2019/10/10 15:10
 * @Version: 1.0
 */
public class ColumnExtract {

    public List<String[]> extract() throws IOException {

        ApplicationProperties applicationProperties = new ApplicationProperties();
        ApplicationProperties.PrimaryProperties primaryProperties = applicationProperties.getPrimaryProperties();;
        primaryProperties.setPath("E:\\文件\\工作\\AI\\bert\\原始语料\\测试数据\\栏目分类测验结果.csv");
        primaryProperties.setHaveHeader(false);
        primaryProperties.setLabel(-1);
        primaryProperties.setTitle(1);
        primaryProperties.setContent(-1);

        String path = primaryProperties.getPath();
        Object labelHeader = primaryProperties.getLabel();
        Object titleHeader = primaryProperties.getTitle();
        Object contentHeader = primaryProperties.getContent();

        DealFileWay dealCsvWay = new GeneralCsvDeal(labelHeader, titleHeader, contentHeader);
        ((GeneralCsvDeal) dealCsvWay).setAps(applicationProperties);

        DealFile2Strings dealFile2Strings = new GeneralDealAdapter(dealCsvWay);
        return dealFile2Strings.dealFile(path);
    }
}
