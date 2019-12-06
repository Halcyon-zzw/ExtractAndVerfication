package demand.mapping.model;

import lombok.Data;

/**
 * 映射结果
 *
 * @Author: zhuzw
 * @Date: 2019/11/21 16:23
 * @Version: 1.0
 */
@Data
public class MappingResult {

    public MappingResult() {
    }

    public MappingResult(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    String categoryId;

    String categoryName;
    /**
     * 映射的情感分类
     */
    String mappingCategory;

    /**
     * 匹配到的包含正则
     */
    String includeRegex;

    String excludeKeyword;

    String excludeRegex;

    /**
     * 结果类型：匹配、被剔除、无映射分类
     */
    String type;
}
