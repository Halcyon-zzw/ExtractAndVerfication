import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.junit.Test;

import java.util.List;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/28 10:02
 * @Version: 1.0
 */
public class HanLPTest {
    @Test
    public void testHanLP() {
//        Statistics statistics = new Statistics();
//        statistics.frequencyStatistics("");
        List<Term> termList = StandardTokenizer.segment("商品和服务,股票上涨和下跌,下跌，下跌");
        termList.forEach(a -> System.out.println(a.getFrequency()));
        System.out.println(termList);
    }
}
