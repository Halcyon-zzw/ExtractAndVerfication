package demand.emotion_and_grade_improve;

import config.ApplicationProperties;
import demand.general.ArticleProcess;
import demand.general.process.ProcessWay;
import lombok.Setter;
import util.StringsUtilCustomize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 原始处理，只处理换行、括号等一些格式问题
 *
 * @Author: zhuzw
 * @Date: 2019/11/13 9:56
 * @Version: 1.0
 */
public class OriginalArticleProcess extends ArticleProcess {
    @Setter
    private ArticleProcess articleProcess;

    private ApplicationProperties aps = new ApplicationProperties();

    public OriginalArticleProcess(ProcessWay processWay) {
        super(processWay);
    }

    public OriginalArticleProcess(ProcessWay processWay, ArticleProcess articleProcess) {
        super(processWay);
        this.articleProcess = articleProcess;
    }

    /**
     * 默认直接使用标签；需计算重写该方法
     * TODO 处理标签
     * @param labels
     * @return
     */
<<<<<<< HEAD
//    public String getLabel(String... labels) {
//        return articleProcess.getLabel(labels);
//    }
=======
    public String getLabel(String... labels) {
        return articleProcess.getLabel(labels);
    }
>>>>>>> f3a5f76a346ab516592180b911483953d6b69ff1

    public String getArticle(String title, String content) {

        title = replaceSynbolOfTable(title);
        //处理特殊字符
//        content = dealContent(content);
        content = processWay.process(content);

        String article = title + " " + content;
        //删除括号
        article = StringsUtilCustomize.substringByDeleteBrackets(article);

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

    public void info() {
        System.out.println(">>>返回原始语料。");
        processWay.info();
    }
}
