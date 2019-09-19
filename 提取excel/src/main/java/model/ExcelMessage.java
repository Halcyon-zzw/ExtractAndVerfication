package model;

import lombok.Data;

/**
 * Excel文件模板信息
 *
 * @Author: zhuzw
 * @Date: 2019/8/19 15:31
 * @Version: 1.0
 */
@Data
public class ExcelMessage {
    private int row;

    private int column;

    /**
     * 内容
     */
    private String content;
}
