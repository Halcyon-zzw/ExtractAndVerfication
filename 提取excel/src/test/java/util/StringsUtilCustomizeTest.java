package util;

import demand.general.RowValueProcess;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class StringsUtilCustomizeTest {

    @Test
    public void substringByDeleteAll() {
        String content = "     ab     ca    bc    ab  c  你  好   呀    ";
        while(-1 != content.indexOf("  ")) {
            content = content.replaceAll(" {2,}", " ");
        }

        char[] charsOfContent = content.toCharArray();
        for (int i = 0; i < charsOfContent.length; i++) {
            if (' ' == charsOfContent[i]) {
                char beforeChar = '#';
                char afterChar = '#';
                if (i - 1 >= 0) {
                    beforeChar = charsOfContent[i - 1];
                }
                if (i + 1 < content.length()) {
                    afterChar = charsOfContent[i + 1];
                }

                //判断前后是否为字母     -- \0 也作为判断依据，解决字母中出现多个空格后删除情况
                if (! (StringsUtilCustomize.isLetter(beforeChar) && StringsUtilCustomize.isLetter(afterChar))) {    //不是字母
                    charsOfContent[i] = '~';
                }
            }
        }
        content = new String(charsOfContent);
        content = content.replace("~", "");
        System.out.println(content.indexOf("你好呀"));
        System.out.println(content);
    }

    @Test
    public void substringByDeleteBefore() {
        String str = "";
        RowValueProcess rowValueProcess = new RowValueProcess();
        str = rowValueProcess.dealString(str);
        System.out.println(str);
    }

    @Test
    public void substringByDeleteBlock() {
        String str = "    3月13日，新三板挂牌企业鑫聚光电(831881)发布关联交易公告。\n" +
                "\n" +
                "    公告显示，根据经营需要，鑫聚光电拟向东莞银行塘厦支行申请2000万元授信。公司董事长兼总经理拟以个人自有房产为此次贷款提供担保。\n" +
                "\n" +
                "    公开资料显示，东莞市鑫聚光电科技股份有限公司成立于2007年，是一家专业从事工程监理、试验检测、项目管理技术咨询服务的综合型工程咨询企业。";
        str = StringsUtilCustomize.substringByDeleteBlock(str, "公告显示", "");
        System.out.println(str);
    }

    @Test
    public void testRunable() {
        List<String> testList = new ArrayList<>();
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("4");
        testList.add("5");
        testList.add("6");
        testList.add("7");

        int i = 0;
        Runnable runnable_1 = () -> {
            for (String string: testList) {
                System.out.print(string);
            }
        };

//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 200,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(new ThreadPoolExecutor.DiscardOldestPolicy());

        runnable_1.run();
        runnable_1.run();
//        runnable_2.run();

    }
}