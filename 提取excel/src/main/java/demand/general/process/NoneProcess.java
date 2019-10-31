package demand.general.process;

/**
 * 不做任何处理
 *
 * @Author: zhuzw
 * @Date: 2019/10/30 17:04
 * @Version: 1.0
 */
public class NoneProcess implements ProcessWay {
    @Override
    public String process(String str) {
        return str;
    }
}
