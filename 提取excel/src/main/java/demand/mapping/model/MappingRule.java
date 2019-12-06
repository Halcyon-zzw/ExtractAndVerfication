package demand.mapping.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 事件分类映射至情感分类的规则
 *
 * @Author: zhuzw
 * @Date: 2019/11/15 16:45
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappingRule {
    /**
     * 源分类Id
     */
    String categoryId;

    /**
     * 源分类Id name
     */
    String categoryName;

    /**
     * 映射的分类
     */
    String mappingCategory;

    /**
     * 某一源分类下的所有类型（一类包括包含关键词、剔除关键词、多剔除关键词）的正则
     */
    List<RuleRegex> ruleRegexList;
}
