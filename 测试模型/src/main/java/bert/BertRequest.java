package bert;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description: bert服务请求参数
 * @author: chenyang
 * @create: 2019-05-28
 **/
@Data
@Accessors(chain = true)
public class BertRequest {
    private String id;
    private List<String> texts;
//    private boolean is_tokenized;
}
