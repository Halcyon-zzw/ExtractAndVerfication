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

    /**
     * 删除字符串中所有的括号，不能删除嵌套括号
     * @param str
     * @return
     */
    public static String substringByDeleteBrackets(String str) {
        //存在左右括号不一致问题，同意中英文括号
        str = str.replaceAll("\\（", "(");
        str = str.replaceAll("\\）", ")");

        str = str.replaceAll("\\（[^\\（^\\）]*\\）", "");
        str = str.replaceAll("\\([^\\(^\\)]*\\)", "");

        return str;
    }


    /**
     * 删除所有出现的地方
     *
     * @param str
     * @param markString 标识字符串，用来查询字符串
     * @param beforeMark 标识字符串之前的mark（保留）
     * @param afterMark 标识字符串之后的mark（删除）
     * @return
     */
    public static String substringByDeleteAll(String str, String markString, String beforeMark, String afterMark) {
        String tempStr = substringByDelete(str, markString, afterMark, beforeMark);
        while (tempStr.length() < str.length()) {
            str = tempStr;
            tempStr = substringByDelete(str, markString, afterMark, beforeMark);

        }
        return tempStr;
    }

    /**
     * 通过标识字符串，删除标识字符串前后的字符串（第一次匹配的字符串）
     *
     * @param str 需要处理的字符串
     * @param beforeMark 标识字符串前的标识
     * @param afterMark 标识字符串后的标识
     * @param markString 标识字符串
     * @return
     */
    public static String substringByDelete(String str, String markString, String beforeMark, String afterMark) {
        int markIndex = str.indexOf(markString);
        if (-1 != markIndex) {
            //存在 markString 内容,
            //markString 前面的内容
            String beforeStr = str.substring(0, markIndex);
            //markString后面的内容
            String afterStr = str.substring(markIndex + markString.length(), str.length());

            //前半部 beforeMark 的Index
            int beforeMarkIndex = beforeStr.lastIndexOf(beforeMark);
            //后半部 afterMark 的Index
            int afterMarkIndex = afterStr.indexOf(afterMark);

            //截除标识间的内容
            if (-1 != beforeMarkIndex) {
                //beforemark存在
                beforeStr = beforeStr.substring(0, beforeMarkIndex + 1);
            } else {
                //不存在则截除所有
                beforeStr = "";
            }

            //处理afterMark未找到情况
            if (-1 != afterMarkIndex) {
                //存在afterMark，afterStr = afterIndex之后的内容
                afterStr = afterStr.substring(afterMarkIndex + 1, afterStr.length());
            }else {
                //不存在afterMark，afterStr = markString之后的内容
                afterStr = afterStr;
            }


            //拼接
            str = beforeStr + afterStr;
        }
        return str;
    }


    /**
     * 删除markString（最后一个匹配）及其之后的内容
     *
     * @param str 待处理的字符串
     * @param markString 标识字符串
     * @return
     */
    public static String substringByDeleteAfterLast(String str, String markString) {
        int markIndex = str.lastIndexOf(markString);
        if (-1 != markIndex) {
            //存在 markString 内容
            str = str.substring(0, markIndex);
        }
        return str;
    }

    /**
     * 删除markString（第一次匹配）及其之前的内容
     *
     * @param str 待处理的字符串
     * @param markString 标识字符串
     * @return
     */
    public static String substringByDeleteBefore(String str, String markString) {
        int markIndex = str.indexOf(markString);
        if (-1 != markIndex) {
            str = str.substring(markIndex + markString.length(), str.length());
        }
        return str;
    }


    /**
     * 删除markString（最后一次匹配）及其之前的内容
     *
     * @param str 待处理的字符串
     * @param markString 标识字符串
     * @return
     */
    public static String substringByDeleteBeforeLast(String str, String markString) {
        int markIndex = str.lastIndexOf(markString);
        if (-1 != markIndex) {
            str = str.substring(markIndex + markString.length(), str.length());
        }
        return str;
    }
}
