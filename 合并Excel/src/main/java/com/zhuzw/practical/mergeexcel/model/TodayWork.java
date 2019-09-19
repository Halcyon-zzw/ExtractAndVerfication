package com.zhuzw.practical.mergeexcel.model;

import lombok.Data;

/**
 * 今日工作情况
 */
@Data
public class TodayWork {
    //工作内容
    private String woekContent;
    //开始时间
    private String startDate;
    //结束时间
    private String endDate;
    //任务状态
    private String taskStatus;
    //完成进度
    private String completionRatio;
    //负责人
    private String name;
    //交付物
    private String projectDeliverables;
    //备注
    private String notes;
}
