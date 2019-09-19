package bert.deal_file;

import pool.DealFileModify;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理文件适配器类，
 * 适配获取List<String[]>数据的方法
 *
 * @Author: zhuzw
 * @Date: 2019/9/3 17:18
 * @Version: 1.0
 */
public class DealFileAdapter implements DealFile2Strings {
    private DealFileModify dealFileModify;

    public DealFileAdapter(DealFileModify dealFileModify) {
        this.dealFileModify = dealFileModify;
    }

    @Override
    public List<String[]> dealFile(String path) {
        List<String[]> resultList = new ArrayList<>();

        List<String> articleList = dealFileModify.dealFile(path);
        for (String s : articleList) {
            resultList.add(s.split("\t"));
        }
        return resultList;
    }
}
