package com.zhuzw.practical.mergeexcel.model;

import lombok.Data;

/**
 * 单元格信息
 */
@Data
public class DataInformation {
    /**
     * 单元格行号
     */
    private int rowIndex;
    /**
     * 单元格列号
     */
    private int cellIndex;
    /**
     * 单元格内数据
     */
    private String data;

}
