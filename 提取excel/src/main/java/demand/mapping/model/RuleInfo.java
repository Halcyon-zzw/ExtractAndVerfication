package demand.mapping.model;

import lombok.Data;

import java.util.List;

/**
 * 规则匹配Info（初始信息）
 *
 * @Author: zhuzw
 * @Date: 2019/11/15 16:47
 * @Version: 1.0
 */
@Data
public class RuleInfo {

    String categoryId;

    /**
     * 类别
     */
    String categoryName;

    /**
     * 该类别下的规则关键词列表
     */
    List<RuleKeyword> ruleKeywordList;

}
