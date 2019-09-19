package model;

import lombok.Data;
import lombok.ToString;

/**
 * 新闻类
 *
 * @Author: zhuzw
 * @Date: 2019/8/6 19:21
 * @Version: 1.0
 */
@Data
public class News {
    /**
     * 分类代码
     */
    private String id;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 新闻正文
     */
    private String content;

    @Override
    public String toString() {
            return id + "\t" + title + " " + content;
    }
}
