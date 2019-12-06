package demand.mapping.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 正则开头及结尾信息，多次使用#拼接
 *
 * @Author: zhuzw
 * @Date: 2019/11/26 10:53
 * @Version: 1.0
 *
 * 后部分正则和单词正则使用两层括号，使group(1)做为开始匹配到的词（group(0)不用）
 * @Version: 2.0
 */
@Data
public class RegexInfo {
//    List<String> startPattern = new ArrayList<>();
//
//    List<String> endPattern = new ArrayList<>();

    /**
     * 开头正则信息
     */
    String startRegex;

    /**
     * 结尾正则信息
     */
    String endRegex;


    public String toRegex() {

        startRegex = createRegex(startRegex);
        endRegex = createRegex(endRegex);

        //后部分正则再加一层括号
        endRegex = "(" + endRegex + ")";

        return startRegex + ".*?" + endRegex;
    }

    private String createRegex(String str) {
        String[] strArr = str.split("#");
        StringBuffer regexBuffer = new StringBuffer("(");
        for (int i = 0; i < strArr.length; i++) {
            if (0 == i) {
                regexBuffer.append(strArr[i]);
            }else {
                regexBuffer.append("|" + strArr[i]);
            }
        }
        regexBuffer.append(")");

        return regexBuffer.toString();
    }
}
