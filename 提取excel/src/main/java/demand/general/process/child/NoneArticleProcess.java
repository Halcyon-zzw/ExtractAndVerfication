package demand.general.process.child;

import config.ApplicationProperties;
import demand.general.process.ArticleProcess;
import demand.general.process.ProcessWay;
import lombok.Setter;
import util.StringsUtilCustomize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对文章不做处理，仅映射标签
 *
 * TODO 开发未完成,需求搁置
 *
 * @Author: zhuzw
 * @Date: 2019/11/13 9:56
 * @Version: 1.0
 */
public class NoneArticleProcess extends ArticleProcess {
    @Setter
    private ArticleProcess articleProcess;

    private ApplicationProperties aps = new ApplicationProperties();

    public NoneArticleProcess(ProcessWay processWay) {
        super(processWay);
    }

    public NoneArticleProcess(ProcessWay processWay, ArticleProcess articleProcess) {
        super(processWay);
        this.articleProcess = articleProcess;
    }

    /**
     * 默认直接使用标签；需计算重写该方法
     *
     * @param labels
     * @return
     */
    public String getLabel(String... labels) {
        return articleProcess.getLabel(labels);
    }

    public String getArticle(String title, String content) {

        title = replaceSynbolOfTable(title);
        //处理特殊字符
        content = dealContent(content);
        //TODO 放在文章级别处理报错
//        content = deleteDate(content);
        content = processWay.process(content);

        String article = title + " " + content;
        //删除括号
        article = StringsUtilCustomize.substringByDeleteBrackets(article);

        return title + "\t" + content;
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
            System.out.println(m.group());
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
        System.out.println(stringBuffer.toString());
        return stringBuffer.toString();
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
