package process;

/**
 * 是否处理列接口
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 9:07
 * @Version: 1.0
 */
public interface ColumnProcess {
    /**
     * 判断文件某一列是否需要处理
     *
     * @param object
     * @return
     */
    public boolean isProcess(Object object);
}
