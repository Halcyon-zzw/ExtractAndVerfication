package request;

import bert.BertRequest;
import bert.single.BertResultSingle;
import bert.single.BertSingleResponse;
import lombok.Data;

/**
 * 单分类请求
 *
 * @Author: zhuzw
 * @Date: 2019/9/3 15:48
 * @Version: 1.0
 */
@Data
public class SingleClassificationRequest extends ClassificationRequest {

    private BertResultSingle bertResult = new BertResultSingle();

    public SingleClassificationRequest() {
        super();

    }


    public SingleClassificationRequest(BertRequest bertRequest, Class responseType) {
        super(bertRequest, responseType);
    }

    @Override
    public BertResultSingle getResult() {

        BertSingleResponse bertSingleResponse = (BertSingleResponse) super.postForObject();
        bertResult.setScore(bertSingleResponse.getResult().get(0).getScore().get(0).floatValue());
        bertResult.setLabel(bertSingleResponse.getResult().get(0).getPred_label().get(0));


        return bertResult;
    }
}
