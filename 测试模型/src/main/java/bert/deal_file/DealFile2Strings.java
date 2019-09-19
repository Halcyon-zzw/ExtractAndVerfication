package bert.deal_file;

import pool.DealFileModify;

import java.io.IOException;
import java.util.List;

/**
 * 处理文件接口，文件中的数据不拼接
 * 添加 String[] dealFile2(String path)接口
 *
 * @Author: zhuzw
 * @Date: 2019/9/3 17:07
 * @Version: 1.0
 */
public interface DealFile2Strings {
    List<String[]> dealFile(String path) throws IOException;
}
