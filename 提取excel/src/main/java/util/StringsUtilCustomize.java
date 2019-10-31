package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * 标点集合
     */
    private static String[] punctuations = {"，",
            ",",
            ";", "；", "。", ".", "？", "?", "!", "!",};
    /**
     * 句子级别的标点
     */
    private static String[] sentencePunctuations = {";", "；", "。", ".", "？", "?", "!", "!",};

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断列是否为null
     *
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
     *
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
     * @param afterMark  标识字符串之后的mark（删除）
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
     * 通过标识字符串，删除beforeMark、afterMark之间的字符串（第一次匹配的字符串）
     *
     * @param str        需要处理的字符串
     * @param beforeMark 标识字符串前的标识
     * @param afterMark  标识字符串后的标识
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
            } else {
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
     * @param str        待处理的字符串
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
     * @param str        待处理的字符串
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
     * @param str        待处理的字符串
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


    public static String substringByDeleteBlockAll(String str, String markString) {
        String tempStr = substringByDeleteBlock(str, markString);
        while (tempStr.length() < str.length()) {
            str = tempStr;
            tempStr = substringByDeleteBlock(str, markString);
        }
        return tempStr;
    }

    public static String substringByDeleteBlockAll(String str, String markString, String level) {
        String tempStr = substringByDeleteBlock(str, markString, level);
        while (tempStr.length() < str.length()) {
            str = tempStr;
            tempStr = substringByDeleteBlock(str, markString, level);
        }
        return tempStr;
    }

    /**
     * 删除标识符字符串所在的句子（标点符号之间）段
     *
     * @param str
     * @param markString
     * @return
     */
    public static String substringByDeleteBlock(String str, String markString) {
        return substringByDeleteBlock(str, markString, "");
    }


    /**
     * 删除标识符字符串所在的句子（标点符号之间）段
     *
     * @param str
     * @param markString
     * @return
     */
    public static String substringByDeleteBlock(String str, String markString, String level) {
        //表示的位置
        int markIndex = str.indexOf(markString);
        if (-1 != markIndex) {  //存在 markString 内容
            //markString 前面的内容
            String beforeStr = str.substring(0, markIndex);
            //markString后面的内容
            String afterStr = str.substring(markIndex + markString.length(), str.length());

            //前半部标点的Index集合
            List<Integer> beforePunctuationIndexs = new ArrayList<>();
            beforePunctuationIndexs = getPunctuationIndexs(beforeStr, beforePunctuationIndexs, "last", level);
            //后半部标点的Index集合
            List<Integer> afterPunctuationIndexs = new ArrayList<>();
            afterPunctuationIndexs = getPunctuationIndexs(afterStr, afterPunctuationIndexs, "", level);

            beforePunctuationIndexs.sort(Integer::compareTo);
            afterPunctuationIndexs.sort(Integer::compareTo);
            //获取表示字符串及前后标点的位置
            int beforePunctuationIndex = beforePunctuationIndexs.get(beforePunctuationIndexs.size() - 1);
            //获取第一个非-1的值
            int afterPunctuationIndexOfList = afterPunctuationIndexs.lastIndexOf(-1);
            if (afterPunctuationIndexOfList < afterPunctuationIndexs.size() - 1) {
                afterPunctuationIndexOfList++;
            }
            int afterPunctuationIndex = afterPunctuationIndexs.get(afterPunctuationIndexOfList);

            //截取字符串,前面保留标点，后面去除标点
            beforeStr = beforeStr.substring(0, beforePunctuationIndex + 1);
            //判断afterPunctuationIndex是否为afterStr最后一位；
//            if (afterPunctuationIndex < afterStr.length() - 1) {
            //substring允许string.substring(string.length(), string.length())范围的操作，但值不能再大
            afterStr = afterStr.substring(afterPunctuationIndex + 1, afterStr.length());
//            } else {
//                afterStr = "";
//            }
            str = beforeStr + afterStr;
        }
        return str;
    }


    /**
     * 获取字符串中，首次匹配标点位置的集合
     *
     * @param str
     * @param punctuationIndexs
     * @return
     */
    private static List<Integer> getPunctuationIndexs(String str, List<Integer> punctuationIndexs) {
        return getPunctuationIndexs(str, punctuationIndexs, "", "");
    }


    /**
     * 获取字符串中，标点位置的集合
     *
     * @param str
     * @param punctuationIndexs 标点位置存储的集合
     * @param category          寻找标点位置的类别。为last时，匹配最后一次，否则匹配第一次
     * @param level 匹配符号的级别
     * @return
     */
    private static List<Integer> getPunctuationIndexs(String str, List<Integer> punctuationIndexs, String category, String level) {
        String[] tempPunctuations;
        if ("sentence".equalsIgnoreCase(level)) {
            tempPunctuations = sentencePunctuations;
        }else {
            tempPunctuations = punctuations;
        }
        if (null == punctuationIndexs) {
            punctuationIndexs = new ArrayList<>();
        }
        for (String punctuation : tempPunctuations) {
            int punctuationIndex = -1;
            if ("last".equalsIgnoreCase(category)) {
                punctuationIndex = str.lastIndexOf(punctuation);
            } else {
                punctuationIndex = str.indexOf(punctuation);
            }
            punctuationIndexs.add(punctuationIndex);
        }
        return punctuationIndexs;
    }

    public static boolean isLetter(char c) {
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
            return true;
        }
        return false;
    }

    public static boolean isLetterOrNumber(char c) {
        boolean isNumber = Character.isDigit(c);
        boolean isLetter = Character.isLetter(c);
        if (isNumber || isLetter) {
            return true;
        }
        return false;
    }

    /**
     * 删除字符串中的空格（不删除英文字母间的空格）
     * @param str
     * @return
     */
    public static String substringByDeleteSpace(String str) {
        str = str.trim();
        while(-1 != str.indexOf("  ")) {
            str = str.replaceAll(" {2,}", " ");
        }

        char[] charsOfContent = str.toCharArray();
        for (int i = 0; i < charsOfContent.length; i++) {
            if (' ' == charsOfContent[i]) {
                char beforeChar = '#';
                char afterChar = '#';
                if (i - 1 >= 0) {
                    beforeChar = charsOfContent[i - 1];
                }
                if (i + 1 < str.length()) {
                    afterChar = charsOfContent[i + 1];
                }

                //判断前后是否为字母     -- \0 也作为判断依据，解决字母中出现多个空格后删除情况
                if (! (StringsUtilCustomize.isLetterOrNumber(beforeChar) && StringsUtilCustomize.isLetterOrNumber(afterChar))) {    //不是字母
                    charsOfContent[i] = '~';
                }
            }
        }
        str = new String(charsOfContent);
        str = str.replace("~", "");

        return str;
    }

    /**
     * 切分句子
     * @param str
     * @param sentenceSeparator
     * @return
     */
    public static List<String> splitSentence(String str, String sentenceSeparator) {
        List<String> sentences = new ArrayList<>();
        //第一个for切分段落
        for (String line : str.split("[\r\n]")) {
            line = StringUtils.strip(line.trim(), "　");
            if (line.length() == 0) {
                continue;
            }
            //第二个for切分句子
            for (String sentence : line.split(sentenceSeparator)) {
                sentence = sentence.trim();
                if (sentence.length() == 0) {
                    continue;
                }
                sentences.add(sentence);
            }
        }
        return sentences;
    }
}
