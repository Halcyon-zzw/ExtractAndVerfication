package com.zhuzw.practical.mergeexcel;

import com.zhuzw.practical.mergeexcel.model.DataInformation;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 相关属性
 * @Author: zhuzw
 * @Date: 2019/8/5 8:57
 * @Version: 1.0
 */
@ConfigurationProperties(prefix = "excel")
@Component
@Data
public class ExcelProperties {

    /**
     * 日报记录初始位置（行号）
     */
    public final static int PROGRESS_ROW_INDEX_INIT = 1;
    public final static int PROBLEM_ROW_INDEX_INIT = 2;
    public final static int TODAY_ROW_INDEX_INIT = 5;
    public final static int TOMORROW_ROW_INDEX_INIT = 25;
    public final static int RISK_ROW_INDEX_INIT = 39;

    //文件后缀
    public static String suffixName = ".xlsx";

    //待汇总文件路径
    public static String summariedPath = "E:\\santi\\20181215\\";

    //汇总文件路径前缀
    public static String poolPrefixPath = "E:\\santi\\日报汇总\\";
    //汇总文件名
    public static String poolExcelName = "三体项目日报-AI开发组-20190805";
    //汇总文件 日期
    private String date = "2019";


    //模板文件前缀路径
    public static String templatePrefixPath = "E:\\santi\\";
    //模板文件名
    public static String templatelName = "三体项目日报-模板-20xxxxxx";
    //模板文件完整路径
    public static String templatePath = templatePrefixPath + templatelName + suffixName;
    //汇总文件完整路径
    public static String poolExcelPath = poolPrefixPath + poolExcelName + suffixName;

    public final static int STAFF_COUNT = 6;

    public static String titleName = "企业情报与经营分析服务项目日报";

}
