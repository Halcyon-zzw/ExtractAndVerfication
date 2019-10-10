package request;

import bert.BertRequest;
import bert.single.BertResultSingle;
import config.ApplicationProperties;
import config.PropertiesFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * 分类请求
 *
 * @Author: zhuzw
 * @Date: 2019/9/3 15:01
 * @Version: 1.0
 */
public abstract class ClassificationRequest<T> {
    int num = 0;
    /**
     * 获取请求地址
     */
    String url = TestUrlFactory.getTestUrl("emotion");

    /**
     * 请求参数
     */
    BertRequest bertRequest;

    /**
     * 请求响应类型
     */
    private Class<T> responseType;

    public ClassificationRequest(BertRequest bertRequest, Class<T> responseType) {
        this.bertRequest = bertRequest;
        this.responseType = responseType;
    }


    /**
     * post请求
     * @return
     */
    public Object postForObject() {
        RestTemplate restTemplate = new RestTemplate(); // 使用HttpClient，支持GZIP
        restTemplate.getMessageConverters().set(1,
                new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
        return restTemplate.postForObject(url, bertRequest, responseType);
    }

    /**
     * 从响应结果中获取结果
     * @return
     */
    public abstract BertResultSingle getResult();
}
