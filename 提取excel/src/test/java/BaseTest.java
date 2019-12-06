import config.ApplicationProperties;
import org.junit.Test;
import util.FileUtil;
import util.StringsUtilCustomize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/8 10:06
 * @Version: 1.0
 */
public class BaseTest {
    ApplicationProperties aps = new ApplicationProperties();

    @Test
    public void strTest() {
        String str = "特种集成电路贡献巨大 紫光国微净利增超六成 8月22日，紫光国微发布半年报，公司实现营业收入15.59亿元，较上年同期增长48.05%；实现归属于上市公司股东净利润1.93亿元，较上年同期增长61.02%；实现归属于上市公司股东的扣除非经常性损益净利润2.18亿元，较上年同期增长110.66%。报告期末，公司总资产59.93亿元，归属于上市公司股东所有者权益3.97亿元，资产负债率33.59%。公告显示，报告期内公司不断优化技术与产品、加强重点行业市场开拓，集成电路业务继续保持了良好的发展趋势，特别是特种集成电路业务，营业收入和净利润均实现了大幅增长，市场地位进一步提升。同时，公司积极布局智能物联领域的新应用，为持续健康发展提供新动能。紫光国微董秘杜林虎在接受《证券日报》记者采访时表示，公司上半年的业绩增长，主要得益于特种集成电路业务市场开拓取得显著成效，可编程系统集成芯片等重点产品的应用领域不断拓展，使得订单量、销售额均实现大幅增长。同时，智能卡安全芯片业务稳定增长，通信、金融、社保等重点行业均有所突破，金融IC卡增长迅速，eSIM已取得市场先机。紫光国微是紫光集团有限公司旗下的半导体行业上市公司，专注于集成电路芯片设计开发业务，主营产品为集成电路芯片设计与销售，涉及智能安全芯片、高稳定存储器芯片、FPGA、功率半导体器件、超稳晶体频率器件等领域。从产品结构来看，“智能安全芯片”与“特种集成电路”是企业营业收入的主要来源。具体而言，“智能安全芯片”营业收入为6.07亿元，营收占比为38.93%，毛利率为21.84%；“特种集成电路”4.99亿元，营收占比为31.98%，毛利率为73.22%。值得注意的是，2019年上半年，“特种集成电路”营收同比增加117.01%。紫光国微表示，特种集成电路业务增长得益于大客户数量、合同量、销售额方面均实现大幅增长。公司特种集成电路业务高速增长，是公司利润的最大来源。《证券日报》记者注意到，紫光国微特种集成电路业务显著增长的同时，也获得了国家的大力支持。据统计，在2014年-2018年，国家给予特种集成电路的政府补助占紫光国微当年新增的政府补助总额的30%以上，其中2015年占比高达93.8%。根据数据统计，近五年紫光国微营业收入稳步增长，年均复合增长率22%。2018年公司营业收入为24.58亿元，同比增长34.41%，主要受益于智能安全芯片产业的复苏以及存储业务的快速增长。紫光国微的营收结构中毛利率较高业务占比逐渐提高，核心业务已从晶体元器件全面转向芯片设计业务。2019年上半年，公司实现营业收入15.59亿元，较上年同期增长48.05%。数据显示，自2014年紫光国微的营业收入一直保持着稳定的增长。但是，2016年的营业收入增速表现出了一定程度的下降。公司给出的原因为，智能安全芯片业务的国内市场产品同质化严重，在中端产品市场上形成激烈的竞争市场，竞争激烈导致产品毛利率出现明显下降；另外，石英晶体市场需求持续低迷，产品售价持续下跌，公司产品订单严重不足，产品的销量及收入较大幅下降。2017年和2018年，金融IC的国产化芯片替代全面加速。到了2019年上半年，公司实现营业收入继续保持增长。西南证券在研报中指出，公司作为智能安全芯片领域的龙头企业，在SIM卡、eSIM、金融IC方面长期保持着领先优势地位。随着国产化替代加快，公司未来业绩预期较好。根据公开数据显示，紫光国微通过多次战略并购，拓展了集成电路业务和存储业务，其五大芯片业务分别由五家子公司承担。在五家子公司中，不得不提的是西安紫光国芯。6月3日，紫光国微发布公告称，为便于后续与西安紫光国芯的业务合作，公司拟对股权转让方案进行调整，将原转让西安紫光国芯100%股权调整为转让其76%股权给紫光存储。本次股权转让完成后，公司仍持有西安紫光国芯24%股权。对于将西安紫光国芯的转让的原因，西南证券表示，受制于下游代工产能限制，存储业务短期内无法达到规模经济，净利润为负，已影响到正常持续经营。因此，紫光国微拟将子公司西安紫光国芯转让给紫光集团子公司紫光存储，这既有利于减轻紫光国微资金投入压力，改善紫光国微毛利率下降的现状，又能给予存储业务充足的资金支持。";

        String str1 = str.replace("[.]+", "");
        System.out.println(StringsUtilCustomize.deleteStrings(str, str1));
    }

    @Test
    public void testDifferentSet() throws IOException {
        String testPath = aps.getBaseProperties().getBasePath() + "\\test\\test.txt";
        String resultPath = aps.getBaseProperties().getBasePath() + "\\test\\articleResult.txt";

        String str = FileUtil.readAll(testPath).get(0);
        String str2 = FileUtil.readAll(resultPath).get(0);
        System.out.println(StringsUtilCustomize.deleteStrings(str, str2));

    }

    /**
     * 求差集
     *
     * @param str 主串
     * @param pa  副串，为str字串
     * @return
     */
    public String differentSet(String str, String pa) {
        if (str == null || str.length() == 0 || pa == null || pa.length() == 0)
            return "";
        ArrayList<Character> list1 = new ArrayList();
        ArrayList list2 = new ArrayList();
        char strc[] = str.toCharArray();
        char pac[] = pa.toCharArray();
        HashSet set = new HashSet();
        for (int i = 0; i < strc.length; i++)
            list1.add(strc[i]);
        for (int j = 0; j < pac.length; j++)
            list2.add(pac[j]);
        for (int k = 0; k < list2.size(); k++) {
            if (list1.contains(list2.get(k))) {
                list1.remove(list2.get(k));
            }
        }
        StringBuffer stringBuffer = new StringBuffer("");
        for (Character c : list1) {
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }


    @Test
    public void testStringBuff() {
        StringBuffer stringBuffer = new StringBuffer("1");

        System.out.println(stringBuffer.length());
    }
}
