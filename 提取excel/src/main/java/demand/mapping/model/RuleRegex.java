package demand.mapping.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个事间分类下的一类正则
 *
 * @Author: zhuzw
 * @Date: 2019/11/19 9:43
 * @Version: 1.0
 */
@Data
public class RuleRegex {
    /**
     * 包含的正则
     */
    List<String> includeRegexList = new ArrayList<>();

    /**
     * 剔除的关键词
     */
    List<String> excludeKeywordList = new ArrayList<>();

    /**
     * 剔除的正则
     */
    List<String> excludeRegexList = new ArrayList<>();
}
