import lombok.Data;

/**
 * 栏目数据
 *
 * @Author: zhuzw
 * @Date: 2019/9/4 18:54
 * @Version: 1.0
 */
@Data
public class ColomuData {

    private String id;

    private String title;

    private String content;

    /**
     * 类别
     */
    private String category;

    public String  toString() {
       return id + "\t" + category + "\t" + title + "\t" + content;
    }

    public String[] toStrings() {
        String[] strings = {
                id,
                category,
                title,
                content
        };
        return strings;
    }


}
