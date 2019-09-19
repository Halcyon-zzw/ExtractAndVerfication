package request;

import bert.BertRequest;
import bert.single.BertResultSingle;
import bert.single.BertSingleResponse;

/**
 * 单分类请求
 *
 * @Author: zhuzw
 * @Date: 2019/9/3 15:48
 * @Version: 1.0
 */
public class SingleClassificationRequest extends ClassificationRequest {

    public SingleClassificationRequest(BertRequest bertRequest, Class responseType) {
        super(bertRequest, responseType);
    }

    @Override
    public BertResultSingle getResult() {
        BertResultSingle bertResult = new BertResultSingle();
        BertSingleResponse bertSingleResponse = (BertSingleResponse)super.postForObject();
        bertResult.setScore(bertSingleResponse.getResult().get(0).getScore().get(0).floatValue());
        bertResult.setLabel(bertSingleResponse.getResult().get(0).getPred_label().get(0));
        return bertResult;
    }
}
