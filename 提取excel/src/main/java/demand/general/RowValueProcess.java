package demand.general;

import config.ApplicationProperties;
import util.StringsUtilCustomize;

import java.util.function.Function;

/**
 * 行数据处理
 * 使用直接处理，需要计算重写getLabel方法
 *
 * @Author: zhuzw
 * @Date: 2019/9/10 15:30
 * @Version: 1.0
 */
public class RowValueProcess {

    static int count = 0;
    /**
     * 字符串的最小长度
     */
    private final int lessLength = 20;
    private ApplicationProperties aps = new ApplicationProperties();

    /**
     * 默认直接使用标签；需计算重写该方法
     *
     * @param labels
     * @return
     */
    public String getLabel(String... labels) {
        return labels[0];
    }

    public String getArticle(String title, String content) {
        /**
         * 截取字符的长度，拼接时需要一个字符
         */
        int articleLength = aps.getArticleLength();
        int beginLength = aps.getBeginLength();
        int endLength = aps.getEndLength();

        //处理特殊字符
        content = dealString(content);
        //删除无效内容
        content = deleteInvalid(content);
        String article = title + " " + content;


        //删除括号
        article = StringsUtilCustomize.substringByDeleteBrackets(article);

        //判断长度是否超过
        if (article.length() > articleLength) {
            String resultArticle = "";
            //截取内容
            resultArticle = article.substring(0, beginLength);
            resultArticle += article.substring(article.length() - endLength, article.length());

            return resultArticle;
        }
        return article;

    }


    /**
     * 提取一行数据
     *
     * @param labels  用于计算标签的参数
     * @param title   标题
     * @param content 内容
     * @return
     */
    public String extractRowValue(String[] labels, String title, String content) {
        String label = getLabel(labels);
        String article = getArticle(title, content);
        return label + "\t" + article;
    }

    /**
     * 处理string格式，解决生成tsv文件自动换行问题（内容不在一个表格中）
     *
     * @param content 待处理文本
     * @return 处理后文本
     */
    public String dealString(String content) {
        content = content.trim();
        while(-1 != content.indexOf("  ")) {
            content = content.replaceAll(" {2,}", " ");
        }

        char[] charsOfContent = content.toCharArray();
        for (int i = 0; i < charsOfContent.length; i++) {
            if (' ' == charsOfContent[i]) {
                char beforeChar = '#';
                char afterChar = '#';
                if (i - 1 >= 0) {
                    beforeChar = charsOfContent[i - 1];
                }
                if (i + 1 < content.length()) {
                    afterChar = charsOfContent[i + 1];
                }

                //判断前后是否为字母     -- \0 也作为判断依据，解决字母中出现多个空格后删除情况
                if (! (StringsUtilCustomize.isLetter(beforeChar) && StringsUtilCustomize.isLetter(afterChar))) {    //不是字母
                    charsOfContent[i] = '~';
                }
            }
        }
        content = new String(charsOfContent);
        content = content.replace("~", "");

        return content
                .replaceAll("\b", "")
                .replaceAll("\f", "")
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .replaceAll("\t", "");
    }

    /**
     * 删除无效内容
     * <p>
     * bug:
     * 存在content为null的情况
     *
     * @param content
     * @return
     */
    public String deleteInvalid(String content) {

        //省略号字符串
        String elipsis1String = "............";
        String elipsis2String = "------------";
        //“存在风险字符串”
        String riskString = "注意投资风险";

//        String tempContent = content;

        String[] beginStrings = {"电", "讯", "获悉", "公告"};
        //删除开头20字符内的 beginStrings
        for (String beginString : beginStrings) {
            int beginIndex = content.indexOf(beginString);
            if (beginIndex > 20) {
                content = StringsUtilCustomize.substringByDeleteBefore(content, beginString);
            }
        }

        //删除开头100字符内"有限公司"之前内容
        int comptyIndex = content.indexOf("有限公司");
        if (comptyIndex < 50 && !content.endsWith("有限公司") && content.length() - comptyIndex - 4 > lessLength) {
            //位置在50以内，且不已“有限公司结尾” 且删除后长度大于lessLength
            content = StringsUtilCustomize.substringByDeleteBefore(content, "有限公司");
        }

        //删除目录内容及之前的内容
        if (!content.endsWith(".............") && !content.endsWith("-------------")) {
            //文章不以"..........."或"----------"结尾

            //省略号位置
            int elipsisIndex_1 = content.lastIndexOf(elipsis1String);
            int elipsisIndex_2 = content.lastIndexOf(elipsis2String);
            if (content.length() - elipsisIndex_1 - elipsis1String.length() > lessLength && content.length() - elipsisIndex_2 - elipsis2String.length() > lessLength) {
                //省略号位置在文末最小长度以外字符以外

                if (content.indexOf("目录") < elipsisIndex_1 || content.indexOf("目录") < elipsisIndex_2){
                    //只删除存在目录的情况
                    content = StringsUtilCustomize.substringByDeleteBeforeLast(content, ".............");
                    content = StringsUtilCustomize.substringByDeleteBeforeLast(content, "-------------");
                }

            }
        }

        //删除 “特此公告”及之后内容
        content = StringsUtilCustomize.substringByDeleteAfterLast(content, "特此公告");

        //待删除句子级的标识
        String[] sentenceStrings = {"虚假记载、误导性陈述", "备份文件", "本公司董事会研究确定",
                "信息披露", "信息为准", "简历如下", "敬请投资者", "理行投资", "持股情况"};

        for (String sentenceString : sentenceStrings) {
            content = StringsUtilCustomize.substringByDeleteBlockAll(content, sentenceString, "level");
        }

        //删除声明内容 =>  删除“虚假记载、误导性陈述”上一个"。"至下一个"。"的内容
//        content = StringsUtilCustomize.substringByDeleteAll(content, "虚假记载、误导性陈述", "。", "。");
//        //删除“备份文件” 前一个"、"和下一个"。"之间的内容
//        content = StringsUtilCustomize.substringByDelete(content, "备份文件", "、", "。");

        //删除“注意投资风险”
        int riskIndex = content.lastIndexOf(riskString);
        if (content.length() - riskIndex - riskString.length() > lessLength) {
            //替换后字符串长度大于lessLength

            content = StringsUtilCustomize.substringByDeleteBlockAll(content, riskString, "level");
        } else {
            content = content.replaceAll(riskString, "");
        }

        //直接删除的字符串集合
        String[] deleteStrings = { "..", "--"};
        for (String deleteString : deleteStrings) {
            content = content.replace(deleteString, "");
        }

        //删除标识字符串所在块
        String[] blockStrings = {"公告", "邮编", "申报时间", "联系电话", "虚假记载、误导性陈述",
                "敬请广大投资者", "联系电话", "证券代码", "债券代码", "数据显示", "资料显示",
                "报告显示", "公告显示"};
        for (String blockString : blockStrings) {
            content = StringsUtilCustomize.substringByDeleteBlockAll(content, blockString);
        }

        //删除最后结尾非标点的内容
        if (content.length() > 0) {
            String lastString = content.substring(content.length() - 1, content.length());
            String endMark = "，。？.?”！!";
            boolean endOfMark = endMark.contains(lastString);
            if (!endOfMark) {
                int lastProdIndex = content.lastIndexOf("。");
                if (-1 != lastProdIndex) {
                    //不止一句话
                    content = content.substring(0, lastProdIndex + 1);
                }
            }
        }

        return content;
    }
}
