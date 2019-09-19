package demand.general;

/**
 * 行数据处理
 * 使用直接处理，需要计算重写getLabel方法
 *
 * @Author: zhuzw
 * @Date: 2019/9/10 15:30
 * @Version: 1.0
 */
public class RowValueProcess {


    /**
     * 默认直接使用标签；需计算重写该方法
     * @param labels
     * @return
     */
    public String getLabel(String... labels) {
        return labels[0];
    }

    public String getArticle(String title, String content) {
        /**
         * 截取字符的长度
         */
        int articleLength = 10000;
        content = dealString(content);
        String article = title + " " + content;

        //判断长度是否超过
        if (article.length() > articleLength) {
            //截取内容
            article = article.substring(0, articleLength);
        }
        return article;
    }

    /**
     * 提取一行数据
     * @param labels 用于计算标签的参数
     * @param title 标题
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
     * @param tempString 待处理文本
     * @return 处理后文本
     */
    protected String dealString(String tempString) {

        return tempString
                .replaceAll("\b","")
                .replaceAll("\f","")
                .replaceAll("\n","")
                .replaceAll("\r","")
                .replaceAll("\t","");
    }
}
