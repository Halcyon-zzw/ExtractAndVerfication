package config;

/**
 * excel文件相关属性
 *
 * @Author: zhuzw
 * @Date: 2019/8/6 19:25
 * @Version: 1.0
 */
public class ExcelProperties {

    /**
     * 新闻Id列号
     */
    public final static int ID_COLUMN_INDEX = 1;

    /**
     * 新闻标题列号
     */
    public final static int TITLE_COLUMN_INDEX = 1;

    /**
     * 新闻正文列号
     */
    public final static int CONTENT_COLUMN_INDEX = 1;

    //文件后缀
    public static String suffixName = ".xlsx";

    //待汇总(处理)文件路径
    public static String summariedPath = "E:\\下载\\钉钉文件\\工作资料\\债券舆情语料\\事件分类\\舆情事件分类语料提供-20180910\\";

    //汇总文件路径前缀
    public static String poolPrefixPath = "E:\\下载\\钉钉文件\\工作资料\\债券舆情语料\\";
//    public static String poolPrefixPath = "E:\\下载\\钉钉文件\\工作资料\\债券舆情语料\\事件分类\\";


    //dev文件名
    public static String devTsvName = "dev";

    //tes文件名
    public static String testTsvName = "test";

    //train文件名
    public static String trainTsvName = "train";


    //文件后缀
    public static String suffixPoolName = ".csv";

    //dev文件路径
    public static String devTsvPath =  poolPrefixPath + devTsvName + suffixPoolName;

    //tes文件路径
    public static String testTsvPath =  poolPrefixPath + testTsvName + suffixPoolName;

    //train文件路径
    public static String trainTsvPath =  poolPrefixPath + trainTsvName + suffixPoolName;

}
