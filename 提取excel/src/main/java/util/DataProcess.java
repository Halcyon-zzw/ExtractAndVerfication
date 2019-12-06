package util;

/**
 * 读取文件数据，数据处理方式
 *
 * @Author: zhuzw
 * @Date: 2019/10/31 10:43
 * @Version: 1.0
 */
public interface DataProcess<T> {
    T process(String string);
}
