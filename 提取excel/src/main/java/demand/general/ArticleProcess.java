package demand.general;

import config.ApplicationProperties;
import demand.general.process.InvalidProcess;
import demand.general.process.ProcessWay;
import lombok.Getter;
import util.StringsUtilCustomize;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文章数据处理
 *      标题、内容格式处理，
 *      内容删除
 *          1、....数据，
 *          2、括号及其内容
 *          3、删除所有日期
 *          4、所有数字及一些特殊字符
 * 最后结果截取前length长度，或前后截取
 *
 * 使用直接处理，需要计算重写getLabel方法
 *
 * @Author: zhuzw
 * @Date: 2019/9/10 15:30
 * @Version: 1.0
 */
public class ArticleProcess {

    static int count = 0;
    /**
     * 字符串的最小长度
     */
    private final int lessLength = 20;
    private ApplicationProperties aps = new ApplicationProperties();

    private List<String> keywordList = new ArrayList<>();

    @Getter
    protected ProcessWay processWay;

    public ArticleProcess(ProcessWay processWay) {
        this.processWay = processWay;
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

        title = replaceSynbolOfTable(title);
        //处理特殊字符
        content = dealContent(content);
        //TODO 放在文章级别处理报错
        content = deleteDate(content);
        content = processWay.process(content);

        String article = title + " " + content;
        //删除括号
        article = StringsUtilCustomize.substringByDeleteBrackets(article);


        //判断长度是否超过
//        if (article.length() > articleLength) {
//            String resultArticle = "";
//            //截取内容
//            resultArticle = article.substring(0, beginLength);
//            resultArticle += article.substring(article.length() - endLength, article.length());
//
//            return resultArticle;
//        }
        //直接截取文章长度
//        if (article.length() >= aps.getArticleLength()) {
//            article = article.substring(0, aps.getArticleLength());
//        }

        return article;
    }

    /**
     * 删除日期
     * @param article
     * @return
     */
    public String deleteDate(String article) {

        String pattern = "(.*){0}([0-9]{1,4}(年)?([上下]半)?年([0-9]{1,2}月)?([0-9]{1,2}日)?)";
        Pattern p = Pattern.compile(pattern);

        Matcher m = p.matcher(article);
        StringBuffer stringBuffer = new StringBuffer("");
        while (m.find()) {
            String deleteStr = m.group();
//            System.out.println(m.group());
            int index = article.indexOf(deleteStr);

            stringBuffer.append(article.substring(0, index));
            article = article.substring(index + deleteStr.length(), article.length());
            m = p.matcher(article);
        }
        //关键，结束后将后半部分加上
        stringBuffer.append(article);
        article = stringBuffer.toString();
        stringBuffer = new StringBuffer("");
        char[] chars = article.toCharArray();

        for (char c : chars) {
            if (c >= 35 && c <= 38 || c >= 45 && c <= 57 || c >= 64 && c <= 126) {

                int index = article.indexOf(c);

                stringBuffer.append(article.substring(0, index));
                article = article.substring(index + 1, article.length());
            }
        }

        //关键，结束后将后半部分加上
        stringBuffer.append(article);
//        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
    }


    /**
     * 处理string格式，解决生成tsv文件自动换行问题（内容不在一个表格中）
     *
     * 处理文章
     *      1、...数据
     *      2、无效空格
     *      3、\n \r
     * @param content 待处理文本
     * @return 处理后文本
     */
    public String dealContent(String content) {
//        content = content.replace("[.]+", "");
        content = StringsUtilCustomize.substringByDeleteSpace(content);
        return replaceSynbolOfTable(content);
    }


    /**
     * 替换table以上的符号
     *
     * @param str
     * @return
     */
    private String replaceSynbolOfTable(String str) {
        return str
                .replaceAll("\b", "")
                .replaceAll("\f", "")
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .replaceAll("\t", "");
    }

    public void info() {
        System.out.println(">>>处理3分类。");
        processWay.info();
    }
}
