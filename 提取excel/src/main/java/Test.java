import demand.general.RowValueProcess;
import util.StringsUtilCustomize;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/10/12 15:58
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) {
        String s = "（（公告编号：临2017-099）公告编号：2018-043）司将根据相公司指定的信息披露媒体为巨潮资讯网www.cninfo.com.cn及《中国证券报》、《上海证券报》、《证券日报》和《证券时报》，公司所有信息均以在上述指定媒体披露的信息为准，敬请广大投资者注意投资风险。";
        s = s.replaceAll("\\（", "(");
        s = s.replaceAll("\\）", ")");
        String result = s.replaceAll("\\([^\\(^\\)]*\\)", "");
        System.out.println(s);
        System.out.println(result);

        String content = "因此，本次重大资产重组事项能否获得公司股东大会审议通过及中国证监会的核准，以及最终获得核准的时间均存在不确定性。敬请广大投资者注意投资风险。";
        content = StringsUtilCustomize.substringByDelete(content, "注意投资风险", "。", "。");
        System.out.println(content);

        testPeridOfLast();
    }

    public static void testPeridOfLast() {
        RowValueProcess rowValueProcess = new RowValueProcess();

        String content = "中国网财经8月17日讯 近日，中国银保监会批复同意，时宝东担任合众资产管理股份有限公司的总经理。";
        content = rowValueProcess.deleteInvalid(content);
        System.out.println(content);

        String str = "接口变动，已停止维护";
        System.out.println(str);
    }
}
