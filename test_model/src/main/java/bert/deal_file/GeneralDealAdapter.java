package bert.deal_file;

import deal.DealFileWay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/9/12 16:59
 * @Version: 1.0
 */
public class GeneralDealAdapter implements DealFile2Strings {
    private DealFileWay dealFile;

    public GeneralDealAdapter(DealFileWay dealFile) {
        this.dealFile = dealFile;
    }

    @Override
    public List<String[]> dealFile(String path) throws IOException {

        List<String[]> resultList = new ArrayList<>();

        List<String> articleList = dealFile.extractedValue(path);
        for (String s : articleList) {
            resultList.add(s.split("\t"));
        }
        return resultList;
    }
}
