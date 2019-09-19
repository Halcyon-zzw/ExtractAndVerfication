package com.zhuzw.practical.mergeexcel.model;

import lombok.Data;

/**
 * 日报汇总-头部
 */
@Data
public class DailySummary {

    /**
     * 当日进展情况
     */
    private String progress;
    /**
     * 所需资源
     */
    private String resource;

}
