import config.ApplicationProperties;
import demand.general.process.ArticleProcess;
import demand.general.process.process_way.InvalidProcess;
import org.junit.Test;
import util.FileUtil;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/12 18:29
 * @Version: 1.0
 */
public class PatternTest {
    ApplicationProperties aps = new ApplicationProperties();
    @Test
    public void dateTest() {
        String pattern = "(.*){0}([0-9]{1,4}(年)?([上下]半)?年([0-9]{1,2}月)?([0-9]{1,2}日)?)";
        Pattern p = Pattern.compile(pattern);

        String content = "    2019年6月1日东方精工上半年实现营业收入约43.91亿元，同比变动幅度约为93.89%；实现归属于上市公司股东的净利润约1.82亿元，同比变动幅度约为-11.81%。普莱德2019年上半年实现动力电池系统装机量约为3.2Gwh，装机套数约为6万套。\n" +
                "\n" +
                "    8月22日，东方精工（002611）发布2019年半年度报告，上半年，公司整体实现营业收入约43.91亿元，同比变动幅度约为93.89%；实现归属于上市公司股东的净利润约1.82亿元，同比变动幅度约为-11.81%；本报告期公司经营活动产生的现金流量净额约为14.19亿元，同比变动幅度约为249.83%；本报告期公司加权平均净资产收益率为4.47%，同比变动幅度约为1.81%。\n" +
                "\n" +
                "    东方精工表示，报告期归属于上市公司股东的净利润同比出现下降，主要原因如下：（1）在行业补贴大幅退坡、行业市场竞争加剧的背景下，报告期内普莱德毛利率水平出现下滑，同比下降近5%；（2）报告期公司期间费用整体有所增加，主要原因包括：2019年上半年子公司普莱德在业务规模扩大的同时，自身运营费用明显增长；为落实东方精工“2018-2022五年战略规划”，公司从去年2季度起加强了团队组织能力提升和核心人才的引进的相关工作，上半年普莱德原股东与公司就普莱德2018年业绩产生争议，上述事项产生了相关费用。\n" +
                "\n" +
                "    东方精工主营业务划分为“高端智能装备”和“汽车核心零部件”两大板块，报告期内，公司汽车核心零部件板块以新能源汽车动力电池系统为主营产业，业务主体是普莱德。普莱德专业从事新能源汽车动力电池Pack系统的设计、研发、生产、销售与服务，致力于为新能源汽车生产厂商提供动力电池整体解决方案，是国内第三方动力电池Pack供应商。\n" +
                "\n" +
                "    普莱德的主要产品是动力电池Pack产品，其产品应用于乘用车、商用车等各种类型的新能源汽车，是新能源汽车动力系统的核心部件。此外，动力电池Pack还能应用于储能、梯次利用等领域。\n" +
                "\n" +
                "    2019年3月新版国补政策出台，新能源汽车补贴再次大幅退坡，进一步加剧了新能源汽车产业的竞争程度；政策规定2019年6月25日前为过渡期，导致整车厂纷纷在过渡期结束前抢装。根据普莱德管理层提报的数据，普莱德2019年上半年实现动力电池系统装机量约为3.2Gwh，装机套数约为6万套，同比增幅与行业整体增幅基本持平。上半年，公司完成了3条新建模组线的建设，并已形成了14条模组线、5条Pack线的整体产能规模。\n" +
                "\n" +
                "    另外，根据普莱德管理层提报的数据，2019年上半年，普莱德来自于核心客户、关联法人北汽集团旗下企业（包括北汽新能源）的销售收入约占报告期普莱德整体销售收入的95%以上。与此同时，普莱德也延续了对核心供应商、关联法人宁德时代在电芯采购方面的高度依赖。" +
                "4444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444" +
                "44444444444444444444444444444444444444444444444444444444444" +
                "555555555555555555555555555555555555555555555555555555555555555555555555555555555555555" +
                "6666666666666666666664";

        Matcher m = p.matcher(content);
        StringBuffer stringBuffer = new StringBuffer("");
        while (m.find()) {
            String deleteStr = m.group();
            int index = content.indexOf(deleteStr);

            stringBuffer.append(content.substring(0, index));
            content = content.substring(index + deleteStr.length(), content.length());
            System.out.println(m.group());
            m = p.matcher(content);
        }
        System.out.println(stringBuffer.toString());

        content = stringBuffer.toString();
        stringBuffer = new StringBuffer("");
        char[] chars = content.toCharArray();
        System.out.println(chars.length);
        System.out.println(content.length());
//        //35  38   45  57    64  126

        for (char c : chars) {
            if (c >= 35 && c <= 38 || c >= 45 && c <= 57 || c >= 64 && c <= 126) {

                int index = content.indexOf(c);

                stringBuffer.append(content.substring(0, index));
                content = content.substring(index + 1, content.length());
            }
        }
        System.out.println(stringBuffer.toString());

    }

    @Test
    public void testDelete() {
        String content = "    中国移动交出了上市以来最差的成绩单。\n" +
                "\n" +
                "    2019年上半年，中国移动上市以来首次出现营收利润双下滑，也是十年以来最大跌幅。财报显示，上半年中国移动实现营业收入3894.27亿元，同比下降0.6％；报告期内实现净利润561.19亿元，同比下滑14.6％。\n" +
                "\n" +
                "    此次导致的营收利润双下滑，中国移动方面总结为四点原因，1、流量红利快速消退；2、市场竞争加剧；3、提速降费持续；4、转型资源投入增加。\n" +
                "\n" +
                "    个人移动市场、家庭市场、政企市场和新业务市场作为最主要的四大核心业务，家庭市场收入同比增长21.3%，政企市场收入同比增长15.7%，新业务市场收入也同比增长了4.1%，但个人移动市场收入却出现了6.2%的同比下滑。\n" +
                "\n" +
                "    从业务占比方面来看，个人移动市场占比高达71.6%，政企客户数占比12.5%；新业务（包括魔百和，咪咕，ICT通信增值服务）占比8.3%。所以，个人移动市场的业绩下滑是上半年中国移动营运收入和通信服务收入下滑的主要原因。\n" +
                "\n" +
                "    业内分析师付亮表示，运营商被要求取消语音漫游费、取消流量“漫游”费、携号转网、提速降费，这成为推动中国移动转型的动力，但短期内，中国移动的业绩下滑却难以避免。\n" +
                "\n" +
                "    据悉，中国移动将优化收入结构（包括重新评估不限流量套餐），以及成本管控（包括关停线下营业厅，控制非生产性支出）希望通过此举实现收入正增长，以及减少利润降幅。\n" +
                "\n" +
                "    业内人士表示，从目前的局面来看，虽然中国移动已经获得工信部发放的5G牌照。但市场的基础建设还是投入期，并且相关终端设备和生态场景构建也在初始阶段。短期内个人移动市场中的数据流量费与移动平均每月每户收入（ARPU）的压力并不会减缓。\n" +
                "\n" +
                "    在5G的建设阶段，中国移动董事长杨杰透露，中国移动年内将建设超过5万个5G基站，实现超过50个城市的5G商用服务。此前，市场十分关注5G是否能带来运营商资本开支的新一轮增长，杨杰谈到，“2020年到2022年是5G投资的主要周期，有三年时间，中国移动会控制总投资不会大幅增加，通过产业链共同努力，这一目标是能够实现的。”\n" +
                "\n" +
                "    与此同时，中国移动同时面临着业绩下滑5G建设投入高峰期，压力可像一般。正因如此，受到行业竞争加剧，导致用户ARPU下降；国家继续推行“提速降费”，导致产品价格下降；5G建设资本开支加大，公司利润下降等一系列影响，国泰君安证券给予“中性”评级，目标价为77.00港元。";
        ArticleProcess articleProcess = new ArticleProcess(new InvalidProcess());
//        content = articleProcess.deleteDate(content);
        content = articleProcess.getArticle("祝志伟", content);
        System.out.println(content);
    }

    @Test
    public void testXun() throws IOException {
        String regex = ".*[\\.。？\\?\\！](.*?(虚假记载、误导性陈述|敬请投资者).*?[\\.。？\\?\\！])";
//        String regex = "^.*[\\.。？\\?\\！](.*?(虚假记载、误导性陈述|敬请投资者).*?[\\.。？\\?\\！])";
//        String regex = "[。？\\?\\！](.*?虚假记载、误导性陈述|敬请投资者.*?[\\.。？\\?\\！])";
      String testPath = aps.getBaseProperties().getBasePath() + "\\test\\test.txt";
        String str = FileUtil.readAll(testPath).get(0);
        Matcher matcher = Pattern.compile(regex).matcher(str);

        if (matcher.find()) {
            System.out.println(matcher.groupCount());
            System.out.println();
            System.out.println();
            System.out.println(matcher.group(1));
            System.out.println();
            System.out.println(matcher.group(2));
//            System.out.println(str.replaceFirst(matcher.group(), ""));
        }
    }

    @Test
    public void testTitle() {
//        String pattern = "(资质|你好).*?((暂停|终止|无))";
//        String pattern = "(资质).*?((无))";
//        String pattern = "(资质|你好).*?((暂停|终止|无))";
//        String pattern = "((澄清|复牌))";
//        String str = "多氟多(002407)澄清称无新能源汽车生产资质 31日无复牌";

        String pattern = "(没有).*?(回购).*?((计划))";
        String str = "深天健(000090):目前公司没有股票回购计划";
        Matcher matcher = Pattern.compile(pattern).matcher(str);


        if (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println(matcher.group(i));
            }
//            System.out.println(str.replaceFirst(matcher.group(), ""));
        }
    }


    @Test
    public void testBaseRegex() {

        String regex = "(([0-9]{6})).*";
        String str = "深109834.0天健(000090):目前公司没有股票回购计划";
        Matcher matcher = Pattern.compile(regex).matcher(str);


        if (matcher.find()) {
            System.out.println("123");
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println(matcher.group(i));
            }
//            System.out.println(str.replaceFirst(matcher.group(), ""));
        }
    }

}
