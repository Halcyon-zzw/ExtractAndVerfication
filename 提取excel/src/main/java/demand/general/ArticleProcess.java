package demand.general;

import config.ApplicationProperties;
import demand.general.process.ProcessWay;
import org.apache.commons.lang3.StringUtils;
import util.FileUtil;
import util.StringsUtilCustomize;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 行数据处理
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

    private ProcessWay processWay;

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

        content = processWay.process(content);

        //处理特殊字符
        content = dealContent(content);

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
     * 处理string格式，解决生成tsv文件自动换行问题（内容不在一个表格中）
     *
     * @param content 待处理文本
     * @return 处理后文本
     */
    public String dealContent(String content) {
        content = content.replace("[.]+", "");
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

}
