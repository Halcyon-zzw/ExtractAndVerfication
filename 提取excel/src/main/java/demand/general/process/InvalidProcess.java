package demand.general.process;

import util.StringsUtilCustomize;

/**
 * 删除无效内容
 * <p>
 * bug:
 *      存在删除后content为null的情况
 * @Author: zhuzw
 * @Date: 2019/10/30 16:57
 * @Version: 1.0
 */
public class InvalidProcess implements ProcessWay {
    /**
     * 字符串的最小长度
     */
    private final int lessLength = 20;

    @Override
    public String process(String str) {
        //省略号字符串
        String elipsis1String = "............";
        String elipsis2String = "------------";
        //“存在风险字符串”
        String riskString = "注意投资风险";

//        String tempContent = str;

        String[] beginStrings = {"电", "讯", "获悉", "公告"};
        //删除开头20字符内的 beginStrings
        for (String beginString : beginStrings) {
            int beginIndex = str.indexOf(beginString);
            if (beginIndex > 20) {
                str = StringsUtilCustomize.substringByDeleteBefore(str, beginString);
            }
        }

        //删除开头100字符内"有限公司"之前内容
        int comptyIndex = str.indexOf("有限公司");
        if (comptyIndex < 50 && !str.endsWith("有限公司") && str.length() - comptyIndex - 4 > lessLength) {
            //位置在50以内，且不已“有限公司结尾” 且删除后长度大于lessLength
            str = StringsUtilCustomize.substringByDeleteBefore(str, "有限公司");
        }

        //删除目录内容及之前的内容
        if (!str.endsWith(".............") && !str.endsWith("-------------")) {
            //文章不以"..........."或"----------"结尾

            //省略号位置
            int elipsisIndex_1 = str.lastIndexOf(elipsis1String);
            int elipsisIndex_2 = str.lastIndexOf(elipsis2String);
            if (str.length() - elipsisIndex_1 - elipsis1String.length() > lessLength && str.length() - elipsisIndex_2 - elipsis2String.length() > lessLength) {
                //省略号位置在文末最小长度以外字符以外

                if (str.indexOf("目录") < elipsisIndex_1 || str.indexOf("目录") < elipsisIndex_2){
                    //只删除存在目录的情况
                    str = StringsUtilCustomize.substringByDeleteBeforeLast(str, ".............");
                    str = StringsUtilCustomize.substringByDeleteBeforeLast(str, "-------------");
                }

            }
        }

        //删除 “特此公告”及之后内容
        str = StringsUtilCustomize.substringByDeleteAfterLast(str, "特此公告");

        //待删除句子级的标识
        String[] sentenceStrings = {"虚假记载、误导性陈述", "备份文件", "本公司董事会研究确定",
                "信息披露", "信息为准", "简历如下", "敬请投资者", "理行投资", "持股情况"};

        for (String sentenceString : sentenceStrings) {
            str = StringsUtilCustomize.substringByDeleteBlockAll(str, sentenceString, "level");
        }

        //删除声明内容 =>  删除“虚假记载、误导性陈述”上一个"。"至下一个"。"的内容
//        str = StringsUtilCustomize.substringByDeleteAll(str, "虚假记载、误导性陈述", "。", "。");
//        //删除“备份文件” 前一个"、"和下一个"。"之间的内容
//        str = StringsUtilCustomize.substringByDelete(str, "备份文件", "、", "。");

        //删除“注意投资风险”
        int riskIndex = str.lastIndexOf(riskString);
        if (str.length() - riskIndex - riskString.length() > lessLength) {
            //替换后字符串长度大于lessLength

            str = StringsUtilCustomize.substringByDeleteBlockAll(str, riskString, "level");
        } else {
            str = str.replaceAll(riskString, "");
        }

        //直接删除的字符串集合
        String[] deleteStrings = { "..", "--"};
        for (String deleteString : deleteStrings) {
            str = str.replace(deleteString, "");
        }

        //删除标识字符串所在块
        String[] blockStrings = {"公告", "邮编", "申报时间", "联系电话", "虚假记载、误导性陈述",
                "敬请广大投资者", "联系电话", "证券代码", "债券代码", "数据显示", "资料显示",
                "报告显示", "公告显示"};
        for (String blockString : blockStrings) {
            str = StringsUtilCustomize.substringByDeleteBlockAll(str, blockString);
        }

        //删除最后结尾非标点的内容
        if (str.length() > 0) {
            String lastString = str.substring(str.length() - 1, str.length());
            String endMark = "，。？.?”！!";
            boolean endOfMark = endMark.contains(lastString);
            if (!endOfMark) {
                int lastProdIndex = str.lastIndexOf("。");
                if (-1 != lastProdIndex) {
                    //不止一句话
                    str = str.substring(0, lastProdIndex + 1);
                }
            }
        }

        return str;
    }
}
