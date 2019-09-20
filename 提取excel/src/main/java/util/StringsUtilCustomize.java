package util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 自定义字符串工具类
 *
 * @Author: zhuzw
 * @Date: 2019/9/9 16:42
 * @Version: 1.0
 */
public class StringsUtilCustomize {

    /**
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断列是否为null
     * @param args 需要检测的列
     * @return 列为null 返回true；否则返回false
     */
    public static boolean isEmpty(String... args) {
        for (String column : args) {
            if (StringUtils.isEmpty(column)) {
                return true;
            }
        }
        return false;
    }
}
