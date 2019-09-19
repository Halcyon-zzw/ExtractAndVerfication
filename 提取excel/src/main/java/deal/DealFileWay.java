package deal;

import java.io.IOException;
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
 * 处理文件方式抽象类(详情见AbstractDealFileWay类)
 * @Author: zhuzw
 * @Date: 2019/8/29 9:07
 * @Version: 2.0
 */
public interface DealFileWay {
    /**
     * 提取文件值，具体提取方法在实现类中实现
     *
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    List<String> extractedValue(String path) throws IOException;
}
