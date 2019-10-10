package config;

import lombok.Data;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/10/9 15:54
 * @Version: 1.0
 */
@Data
public class FileProperties {

    private String basePath = "E:\\文件\\工作\\AI\\bert\\";
    private String testPath = "E:\\文件\\工作\\AI\\bert\\原始语料\\栏目\\测试\\栏目分类数据_验证.csv";
    private String createPath = "E:\\文件\\工作\\AI\\bert\\测试结果\\情感等级_标题\\result.csv";

}
