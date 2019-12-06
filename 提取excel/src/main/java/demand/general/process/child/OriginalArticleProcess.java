package demand.general.process.child;

import config.ApplicationProperties;
import demand.general.process.ArticleProcess;
import demand.general.process.ProcessWay;
import lombok.Setter;
import util.StringsUtilCustomize;

/**
 * 原始处理，
 *      标题、内容格式处理，
 *      内容删除
 *          //1、....数据，
 *          2、删除空格、换行
 *          3、process处理
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
    public String getLabel(String... labels) {
        if (1 == labels.length) {
            return labels[0];
        }
        int label_1 = Integer.parseInt(labels[0]);
        int label_2 = Integer.parseInt(labels[1]);
        //计算label
//        int label = (label_1 - 1) * 3 + label_2;
        int label = label_1 * 100 + label_2;
        //中性4、5、6 统一成 200
        if (2 == label / 100) {
            label = 200;
//            label = 5;
        }
        return String.valueOf(label);
    }

    public String getArticle(String title, String content) {

        title = replaceSynbolOfTable(title);
        //处理特殊字符
        content = dealContent(content);
        content = processWay.process(content);

        //TODO 训练时换回" "
        String article = title + "\t" + content;

        return article;
    }



    /**
     * 处理string格式，解决生成tsv文件自动换行问题（内容不在一个表格中）
     * 中文括号转为英文括号
     *
     * @param content 待处理文本
     * @return 处理后文本
     */
    public String dealContent(String content) {
//        content = content.replace("[.]+", "");
        content = content.replaceAll("\\（", "(");
        content = content.replaceAll("\\）", ")");

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
