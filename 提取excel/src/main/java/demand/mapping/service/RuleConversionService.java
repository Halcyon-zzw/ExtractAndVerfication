package demand.mapping.service;

import demand.mapping.model.RuleInfo;
import demand.mapping.model.RuleKeyword;
import util.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则转换服务
 *
 * @Author: zhuzw
 * @Date: 2019/11/28 17:25
 * @Version: 1.0
 */
public class RuleConversionService {

    /**
     * 原始数据转为RuleInfo
     *
     * @param origialInfoList 原始数据,数据形式：categoryId   categoryName    includeKeyword  excludeKeyword  excludeMultipleKeyword2
     * @return
     */
    public List<RuleInfo> createRuleInfo(List<String> origialInfoList) {
        //每个分类下的关键词的Map
        HashMap<String, List<RuleKeyword>> categoryMap = new HashMap<>();
        for (String origial : origialInfoList) {
            String[] origialArr = origial.split("\t");
            RuleKeyword ruleKeyword = new RuleKeyword();
            ruleKeyword.setIncludeKeyword(origialArr[2]);
            ruleKeyword.setExcludeKeyword(origialArr[3]);
            ruleKeyword.setExcludeMultipleKeyword(origialArr[4]);
            if (null == categoryMap.get(origialArr[0] + "\t" + origialArr[1])) {
                //id + name做为key
                categoryMap.put(origialArr[0] + "\t" + origialArr[1], new ArrayList<>());
            }
            categoryMap.get(origialArr[0] + "\t" + origialArr[1]).add(ruleKeyword);
        }

        //将map集合内容提取出并构建成List<RuleInfo>类型
        List<RuleInfo> ruleInfoList = categoryMap.entrySet().stream().map(entry -> {
            String[] idAndName = entry.getKey().split("\t");
            RuleInfo ruleInfo = new RuleInfo();
            ruleInfo.setCategoryId(idAndName[0]);
            ruleInfo.setCategoryName(idAndName[1]);
            ruleInfo.setRuleKeywordList(entry.getValue());
            return ruleInfo;
        }).collect(Collectors.toList());

        return ruleInfoList;
    }
}
