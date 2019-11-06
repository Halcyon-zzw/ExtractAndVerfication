package demand.general.process;

import lombok.Getter;

/**
 * 不做任何处理
 *
 * @Author: zhuzw
 * @Date: 2019/10/30 17:04
 * @Version: 1.0
 */
public class NoneProcess implements ProcessWay {
    @Getter
    private String type = "none";

    @Override
    public String process(String str) {
        return str;
    }

    @Override
    public void info() {
        System.out.println(">>>原始处理。");
    }
}
