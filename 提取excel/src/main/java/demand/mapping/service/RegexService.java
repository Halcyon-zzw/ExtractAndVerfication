package demand.mapping.service;

import demand.mapping.model.RegexInfo;
import demand.mapping.model.RuleKeyword;
import demand.mapping.model.RuleRegex;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 正则生成服务
 *
 * @Author: zhuzw
 * @Date: 2019/11/22 17:40
 * @Version: 1.0
 */
public class RegexService {

    /**
     * 对于某一事件分类，通过剔除规则对所有的规则进行分类
     *
     * @param ruleKeywordList 某事件分类下的所有关键词（包含、剔除、剔除多个）
     * @return map形式
     * key: excludeKeyword + "\t" + excludeMultipleKeyword
     * value: includeKeyword
     */
    public HashMap<String, List<String>> ruleClassificationByExcludeRule(List<RuleKeyword> ruleKeywordList) {
        HashMap<String, List<String>> ruleClassification = new HashMap<>();
        for (RuleKeyword ruleKeyword : ruleKeywordList) {
            String key = ruleKeyword.getExcludeKeyword() + "\t" + ruleKeyword.getExcludeMultipleKeyword();
            if (null == ruleClassification.get(key)) {
                ruleClassification.put(key, new ArrayList<>());
            }
            ruleClassification.get(key).add(ruleKeyword.getIncludeKeyword());
        }
        return ruleClassification;
    }


    /**
     * 通过原始的关键词规则生成某一分类的List<RuleRegex>
     *
     * @param ruleKeywordList 所有分类的规则关键词list
     *                        earch形式：include: #处置#资产#
     *                        exclude: 名称变更#会计政策#变更名称#名称发生变更
     *                        excludeMultipe: 函|回复#函|答复#法律|意见#审计报告
     * @return 某一分类的所有RuleRegex
     */
    public List<RuleRegex> createRuleRegex(List<RuleKeyword> ruleKeywordList) {
        //通过剔除类型进行规则分类
        HashMap<String, List<String>> ruleClassificationByExcludeRule = ruleClassificationByExcludeRule(ruleKeywordList);

        List<RuleRegex> ruleRegexList = null;
        for (Map.Entry<String, List<String>> entry : ruleClassificationByExcludeRule.entrySet()) {
            //一个key一类规则
            String[] excludeKeywordArr = entry.getKey().split("\t");
            String excludeKeyword = excludeKeywordArr[0];
            String excludeMultipleKeyword = excludeKeywordArr[1];
            RuleRegex ruleRegex = new RuleRegex();
            ruleRegex.setExcludeKeywordList(getExcludeList(excludeKeyword));
            ruleRegex.setExcludeRegexList(getExcludeRegex(excludeMultipleKeyword));
            ruleRegex.setIncludeRegexList(createRegexByEarchKeywordListImprove(entry.getValue()));
            if (null == ruleRegexList) {
                ruleRegexList = new ArrayList<>();
            }
            ruleRegexList.add(ruleRegex);
        }
        return ruleRegexList;
    }


    /**
     * 通过某类别所有关键词生成该类别的正则集合
     * TODO 算法优化
     *
     * @param excludeMulipleKeywordList 某一类的关键词list   earch例：#暂停#资质#
     * @return 该类别的正则集合
     */
    private List<String> createRegexByEarchKeywordList(List<String> excludeMulipleKeywordList) {

        //包含关键词数组（{暂停，资质}）的集合
        List<String[]> keywordArrList = excludeMulipleKeywordList.stream()
                .map(earch ->
                        //按#分割，并过滤null，转List后转Array，并强转成String[]
                        (Arrays.stream(earch.split("#"))
                                .filter(keyword -> !StringUtils.isEmpty(keyword))
                                .collect(Collectors.toList())
                                .toArray(new String[0])
                        )
                ).collect(Collectors.toList());
        //开头相同的集合
        HashMap<String, List<String>> beginSameMap = getBeginSameMap(keywordArrList);
        //结尾相同结合
        HashMap<String, List<String>> endSameMap = getEndSameMap(keywordArrList);
        //获取剩下的不同结果
        HashMap<String, List<String>> noSameMap = getNoSameMap(keywordArrList, endSameMap);
        List<String> regexList = new ArrayList<>();
        List<String> tempRegexList = createRegexByKeywordMap(beginSameMap);
        if (null != tempRegexList) {
            regexList.addAll(tempRegexList);
        }
        tempRegexList = createRegexByKeywordMap(endSameMap, true);
        if (null != tempRegexList) {
            regexList.addAll(tempRegexList);
        }
        tempRegexList = createRegexByKeywordMap(noSameMap);
        if (null != tempRegexList) {
            regexList.addAll(tempRegexList);
        }

        return regexList;
    }

    /**
     * 生成正则
     * @param keywordList 原始关键词列表   keyword1#keyword2
     * @return
     * @version 1.0
     *
     * 后部分正则和单词正则使用两层括号，使group(1)做为开始匹配到的词（group(0)不用）
     * @version 2.0
     */
    private List<String> createRegexByEarchKeywordListImprove(List<String> keywordList) {
        
        List<RegexInfo> regexInfoList = new ArrayList<>();
        StringBuffer singleKeywordRegexBuffer = new StringBuffer("");

        //按#分割并过滤为空的元素，最后放入String[]中，并过滤分割后的空格
        List<String[]> keywordArrList = keywordList.stream()
                .map(earch ->
                        //按#分割，并过滤null，转List后转Array，并强转成String[]
                        (Arrays.stream(earch.split("#"))
                                .filter(keyword -> !StringUtils.isEmpty(keyword))
                                .filter(keyword -> !" ".equalsIgnoreCase(earch))
                                .collect(Collectors.toList())
                                .toArray(new String[0])
                        )
                ).collect(Collectors.toList());


        List<String> singleKeywordList = new ArrayList<>();
        //三词正则
        List<String> threeRegexList = new ArrayList<>();
        for (String[] keywordArr : keywordArrList) {
            boolean haveSame = false;

            if (keywordArr.length >= 3) {
                StringBuffer regexStringBuffer = new StringBuffer("");
                for (int i = 0; i < keywordArr.length; i++) {
                    if (i != keywordArr.length - 1) {
                        regexStringBuffer.append("(" + keywordArr[i] + ")" + ".*?");
                    }else {
                        //最后一次
                        regexStringBuffer.append("((" + keywordArr[i] + "))");
                    }
                }
                threeRegexList.add(regexStringBuffer.toString());
            }
            if (1 == keywordArr.length || StringUtils.isEmpty(keywordArr[1])) {
                //keyword仅一个词的情况
                //去重
                if (!singleKeywordList.contains(keywordArr[0])) {
                    singleKeywordList.add(keywordArr[0]);
                }
                continue;
            }
            int j = 0;
            //从现“正则”中查找是否可以合并
            for (RegexInfo regexInfo : regexInfoList) {
                if (!regexInfo.getStartRegex().contains("#")
                        && keywordArr[0].equalsIgnoreCase(regexInfo.getStartRegex()) 
                        && !regexInfo.getEndRegex().contains(keywordArr[1])) {
                    //开头相同：开头不包含#（就一个词） && 开头相同 && 结尾不被包含在RegexInfo中
                    //拼正则
                    regexInfo.setEndRegex(regexInfo.getEndRegex() + "#" + keywordArr[1]);
                    haveSame = true;
                    break;
                }

                if (!regexInfo.getEndRegex().contains("#")
                        && keywordArr[1].equalsIgnoreCase(regexInfo.getEndRegex())
                        && !regexInfo.getStartRegex().contains(keywordArr[0])) {
                    //结尾相同：结尾不包含#（就一个词） && 结尾相同 && 开头不被包含在RegexInfo中
                    //拼正则
                    regexInfo.setStartRegex(regexInfo.getStartRegex() + "#" + keywordArr[0]);
                    haveSame = true;
                    break;
                }
            }
            //不存在可合并的“正则”，生成新的“正则”
            if (!haveSame) {
                RegexInfo regexInfo = new RegexInfo();
                regexInfo.setStartRegex(keywordArr[0]);
                regexInfo.setEndRegex(keywordArr[1]);
                regexInfoList.add(regexInfo);
            }
        }

        //regexInfo转正则
        List<String> resultRegexList = regexInfoList.stream()
                .map(RegexInfo::toRegex)
                .collect(Collectors.toList());


        //单词转正则
        boolean firstTime = true;
        for (String singleKeyword : singleKeywordList) {
            if (firstTime) {
                //第一次进入
                firstTime = false;
                singleKeywordRegexBuffer.append(singleKeyword);
            }else {
                singleKeywordRegexBuffer.append("|" + singleKeyword);
            }
        }
        //排除singleKeyword没有内容的情况
        if (0 != singleKeywordRegexBuffer.length()) {
            //单词正则外面加上两层括号
            resultRegexList.add("((" + singleKeywordRegexBuffer.toString() + "))");
        }

        //加入三词正则
        resultRegexList.addAll(threeRegexList);

        //过滤empty
        return resultRegexList.stream().filter(s -> !StringUtils.isEmpty(s)).collect(Collectors.toList());
    }

    /**
     * 见重写方法
     *
     * @param map
     * @return
     */
    private List<String> createRegexByKeywordMap(HashMap<String, List<String>> map) {
        return createRegexByKeywordMap(map, false);
    }

    /**
     * 通过每一类关键词规则（开头相同、结尾相同、其他）生成该类的regex list
     * 一个key一个正则
     *
     * @param map    key：相同开头（或结尾）关键词
     *               value：相同key下的其他结尾（或开头）关键词
     * @param invert 开头和结尾是否倒置，存在map key存放的是结尾关键词
     * @return 相同类别（同开头、结尾或无）的一条正则
     */
    private List<String> createRegexByKeywordMap(HashMap<String, List<String>> map, boolean invert) {

        String regex = null;
        List<String> regexList = null;
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {

            if (StringUtils.isEmpty(entry.getKey())) {
                continue;
            }
            String startRegex = entry.getKey();
            StringBuffer endRegexBuffer = new StringBuffer("");
            for (String s : entry.getValue()) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                endRegexBuffer.append(s);
                endRegexBuffer.append("|");
            }
            if (0 == endRegexBuffer.length()) {
                continue;
            }
            //剔除最后多余的 |
            String endRegex = endRegexBuffer.substring(0, endRegexBuffer.length() - 1);
            //加上()
            endRegex = "(" + endRegex + ")";
            if (invert) {
                //倒置开头和结尾
                regex = endRegex + ".*?" + startRegex;
            } else {
                regex = startRegex + ".*?" + endRegex;
            }
            if (null == regexList) {
                regexList = new ArrayList<>();
            }
            regexList.add(regex);
        }

        return regexList;
    }

    /**
     * 获取没有相同的关键词
     * 所有数据放入map中，再放一遍endSameMap中的数据，长度为1的既为剩下的
     *
     * @param keywordArrList 关键词列表
     * @param endSameMap     结尾相同的关键词列表
     * @return
     */
    private HashMap<String, List<String>> getNoSameMap(List<String[]> keywordArrList, HashMap<String, List<String>> endSameMap) {

        HashMap<String, List<String>> noSameMap = new HashMap<>();
        for (String[] strings : keywordArrList) {
            if (null == strings || StringUtils.isEmpty(strings[0])) {
                //数组不为null ||  开头 index = 0不为null 或""
                continue;
            }
            if (null == noSameMap.get(strings[0])) {
                noSameMap.put(strings[0], new ArrayList<>());
            }
            if (strings.length > 1) {
                noSameMap.get(strings[0]).add(strings[1]);
            } else {
                noSameMap.get(strings[0]).add("");
            }
        }

        for (Map.Entry<String, List<String>> entry : endSameMap.entrySet()) {
            for (String str : entry.getValue()) {
                noSameMap.get(str).add(entry.getKey());
            }
        }
        return noSameMap.entrySet().stream().filter(earch -> earch.getValue().size() == 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));
    }

    /**
     * 获取开头相同的集合（一个关键词的包含在此分类中）
     *
     * @param keywordArrList
     * @return
     */
    private HashMap<String, List<String>> getBeginSameMap(List<String[]> keywordArrList) {
        HashMap<String, List<String>> beginSameMap = new HashMap<>();
        //统计开头相同的关键词
        for (String[] strings : keywordArrList) {
            if (null == strings || StringUtils.isEmpty(strings[0])) {
                //数组不为null ||  开头 index = 0不为null 或""
                continue;
            }
            if (null == beginSameMap.get(strings[0])) {
                beginSameMap.put(strings[0], new ArrayList<>());
            }
            if (strings.length <= 1) {
                beginSameMap.get(strings[0]).add("");
            } else {
                beginSameMap.get(strings[0]).add(strings[1]);
            }

        }
        //剔除长度为1的数据
        beginSameMap = beginSameMap.entrySet().stream().filter(earch -> earch.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));
        return beginSameMap;
    }

    /**
     * 获取结尾相同的集合
     *
     * @param keywordArrList
     * @return
     */
    private HashMap<String, List<String>> getEndSameMap(List<String[]> keywordArrList) {
        HashMap<String, List<String>> endSameMap = new HashMap<>();
        //统计开头相同的关键词
        for (String[] strings : keywordArrList) {
            if (null == strings || strings.length <= 1 || StringUtils.isAnyEmpty(strings[0], strings[1])) {
                //strings为null、长度小于等于1、及每个元素为null或""不处理
                continue;
            }
            if (null == endSameMap.get(strings[1])) {
                endSameMap.put(strings[1], new ArrayList<>());
            }
            endSameMap.get(strings[1]).add(strings[0]);

        }
        //剔除长度为1的数据
        endSameMap = endSameMap.entrySet().stream().filter(earch -> earch.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));
        return endSameMap;
    }

    /**
     * 获取剔除关键词列表
     *
     * @param excludeKeyword 职业资格#任职资格#资格审批#自查#澄清#证书资格
     * @return
     */
    private List<String> getExcludeList(String excludeKeyword) {
        //值为null时返回null
        if ("null".equalsIgnoreCase(excludeKeyword) || StringUtils.isEmpty(excludeKeyword)) {
            return null;
        }
        //按"#"分割并过滤 null 数组,并去重
         return Arrays.stream(excludeKeyword.split("#"))
                 .filter(earch -> !StringUtils.isEmpty(earch))
                 .collect(Collectors.toSet())
                 .stream().collect(Collectors.toList());
    }


    /**
     * 通过原始剔除关键词（多个）生成该类的剔除正则集合
     *
     * @param excludeMultipleKeyword 格式：任职资格|不符#任职资格|不满#不满|任职资格#不符|任职资格
     * @return
     */
    private List<String> getExcludeRegex(String excludeMultipleKeyword) {
        if (StringUtils.isEmpty(excludeMultipleKeyword) || "null".equalsIgnoreCase(excludeMultipleKeyword)) {
            return null;
        }
        //按“#”分割，并过滤empty的元素；再将所有的|替换成#（适配提取关键词的格式）并去重(转set处理)
        //过滤分割后的空格
        List<String> excludeMulipleKeywordList = Arrays.stream(excludeMultipleKeyword.split("#"))
                .filter(earch -> !StringUtils.isEmpty(earch))
                .filter(earch -> !" ".equalsIgnoreCase(earch))
                .map(earch -> earch.replaceAll("\\|", "#"))
                .collect(Collectors.toSet())
                .stream().collect(Collectors.toList());
        //通过每条规则list生成该类正则
        return createRegexByEarchKeywordListImprove(excludeMulipleKeywordList);

    }
}
