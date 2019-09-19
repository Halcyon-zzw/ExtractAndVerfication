package create;

import java.io.IOException;

/**
 * 生成文件方式
 *
 * @Author: zhuzw
 * @Date: 2019/8/8 8:55
 * @Version: 1.0
 */
public interface CreateFileWay {

    /**
     * 将数据集列表生成到文件中
     *
     * @param object 数据集
     * @return 是否操作成功
     */
    void createFile(Object object) throws IOException;
}
