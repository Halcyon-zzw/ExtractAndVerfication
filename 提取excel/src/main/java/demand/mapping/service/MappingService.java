package demand.mapping.service;

import config.ApplicationCache;
import config.ApplicationProperties;
import demand.mapping.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import util.FileUtil;
import util.ListUtil;
import util.StringsUtilCustomize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 映射服务类
 *
 * @Author: zhuzw
 * @Date: 2019/11/15 17:28
 * @Version: 1.0
 */
@Service
public class MappingService {

    public MappingService() {
        System.out.println("service!!!");
    }

    ApplicationProperties aps = new ApplicationProperties();

    private static List<MappingRule> mappingRuleList;

    RegexService regexService = new RegexService();

    LoadService loadService = new LoadService();


//    String path = "E:\\文件\\工作\\AI\\bert\\direct\\result.tsv";
        String path = "E:\\文件\\工作\\AI\\bert\\direct\\result.txt";

    /**
     * 初始操作，加载匹配规则
     */
    public void init() {

        mappingRuleList = loadRule(this.path);
    }

    public void init(String path) {
        mappingRuleList = loadRule(path);
    }


    /**
     * 情感分类
     *
     * @param title 文章标题
     * @return 对应的情感分类，未匹配到返回null
     */
    public MappingResult classifyByRule(String title) {
        if (null == mappingRuleList) {
            init();
        }
        MappingResult mappingResult = classifyByRule(title, mappingRuleList);
        if (null == mappingResult) {
            return null;
        }
        /**
         * TODO 添加一些相关的操作，如显示被匹配的信息,匹配的类型
         */
//        return mappingResult.getMappingCategory();

        //返回mappingResult，用于测试
        return mappingResult;
    }

    /**
     * 多情感分类,使用默认规则(程序中加载)
     * @param title
     * @param earchCount
     * @return
     */
    public List<MappingResult> multiClassifyByRule(String title, int earchCount) {
        if (null == mappingRuleList) {
            init();
        }
        List<MappingResult> mappingResultList = multiClassifyByRule(title, mappingRuleList, earchCount);

        //提取匹配到的词，并将同类合并
        List<MappingResult> mergeMppingResultList = extractAndMerge(mappingResultList);

        //返回mappingResult，用于测试
        return mappingResultList;
    }

    private List<MappingResult> extractAndMerge(List<MappingResult> mappingResultList) {
        return null;
    }

    /**
     * 通过映射规则进行匹配多分类
     *
     * @param title           文章标题
     * @param mappingRuleList 事件分类映射情感分类的规则
     * @param earchCount 每个分类匹配的最大数量
     * @return 返回匹配的信息，未匹配返回null
     */
    public List<MappingResult> multiClassifyByRule(String title, List<MappingRule> mappingRuleList, int earchCount) {

        List<MappingResult> mappingResultList = new ArrayList<>();
//        MappingResult mappingResult = null;
        //遍历每一个分类
        for (MappingRule mappingRule : mappingRuleList) {
            //每个分类下的规则lo
            for (RuleRegex ruleRegex : mappingRule.getRuleRegexList()) {
                //该分类下的包含正则
                for (String includeRegex : ruleRegex.getIncludeRegexList()) {
                    MappingResult mappingResult = new MappingResult(mappingRule.getCategoryId(), mappingRule.getCategoryName());
                    if (StringUtils.isEmpty(includeRegex)) {
                        continue;
                    }
                    Matcher matcher = Pattern.compile(includeRegex).matcher(title);
                    if (matcher.find()) {
                        boolean isExclude = false;
                        includeRegex = markMatched(matcher, includeRegex);
                        mappingResult.setIncludeRegex(includeRegex);
                        //判断剔除词是否包含
                        if (null != ruleRegex.getExcludeKeywordList()) {
                            //TODO 被剔除是否不用再匹配该分类了
                            for (String excludeKeyword : ruleRegex.getExcludeKeywordList()) {
                                if (title.contains(excludeKeyword)) {
                                    //包含剔除关键词，剔除
                                    //TODO  log.info({title}通过{正则}匹配，通过{keyword}剔除)
//                                    System.out.println(title + "match by" + includeRegex + "exclude by" + excludeKeyword);
                                    mappingResult.setCategoryName(mappingRule.getCategoryName() + "->被剔除");
                                    String tempExcludeKeyword = null;
                                    if (null == mappingResult.getExcludeKeyword()) {
                                        tempExcludeKeyword = "";
                                    }else {
                                        tempExcludeKeyword = mappingResult.getExcludeKeyword() + "|||";
                                    }
                                    mappingResult.setExcludeKeyword(tempExcludeKeyword + excludeKeyword);
                                    mappingResult.setType("被剔除");
                                    isExclude = true;
                                    if(ListUtil.countCheck(mappingResultList, earchCount)) {
                                        return mappingResultList;
                                    }
                                }
                            }
                            if (isExclude) {
                                mappingResult.setExcludeRegex(null);
                                mappingResultList.add(mappingResult);
                            }
                        }
                        if (isExclude) {
                            continue;
                        }
                        //判断剔除正则是否包含
                        if (null != ruleRegex.getExcludeRegexList()) {
                            for (String excludeRegex : ruleRegex.getExcludeRegexList()) {
                                Matcher excludeMatcher = Pattern.compile(excludeRegex).matcher(title);
                                if (excludeMatcher.find()) {
                                    //TODO  log.info({title}通过{正则}匹配，通过{正则}剔除)
//                                    System.out.println(title + "match by" + includeRegex + "exclude by" + excludeRegex);
                                    //标注被匹配词
                                    excludeRegex = markMatched(excludeMatcher, excludeRegex);
                                    mappingResult.setCategoryName(mappingRule.getCategoryName() + "->被剔除");
                                    String tempExcludeRegex = null;
                                    if (null == mappingResult.getExcludeRegex()) {
                                        tempExcludeRegex = "";
                                    }else {
                                        tempExcludeRegex = mappingResult.getExcludeRegex() + "|||";
                                    }
                                    mappingResult.setExcludeRegex(tempExcludeRegex + excludeRegex);
                                    mappingResult.setType("被剔除");

                                    isExclude = true;
                                    if(ListUtil.countCheck(mappingResultList, earchCount)) {
                                        return mappingResultList;
                                    }
                                }
                            }
                            if (isExclude) {
                                mappingResult.setExcludeKeyword(null);
                                mappingResultList.add(mappingResult);
                            }

                        }
//                        if (isExclude) {
//                            continue;
//                        }
                        //未被剔除
                        if (!isExclude) {
                            //TODO  log.info({title}通过{正则}匹配)
//                            System.out.println(title + "match by" + includeRegex);
                            if (null != mappingResult.getMappingCategory()) {
                                mappingResult.setMappingCategory(mappingRule.getMappingCategory());
                                mappingResult.setType("匹配");
                            } else {
                                //匹配分类但无映射分类
                                mappingResult.setType("无映射分类");
                            }


                            mappingResultList.add(mappingResult);
                            if (ListUtil.countCheck(mappingResultList, earchCount)) {
                                return mappingResultList;
                            }
                        }
                    }
                }
            }
        }
//        System.out.println(title + " match by None!");
//        mappingResult.setType("未匹配");
        //TODO 返回null or MappingResul对象
        return mappingResultList;
    }

    /**
     * 用<>标记匹配到的正则词
     * improve:     存在前后相同的情况
     * @param matcher
     * @param regex
     * @return
     */
    private String markMatched(Matcher matcher, String regex) {
        for (int i = 1; i < matcher.groupCount(); i++) {
            String matchedWord = matcher.group(i);
            //标记
            int startIndex = regex.indexOf(matchedWord);
            int endIndex = startIndex + matchedWord.length();
            String tempRegex = regex;
            //找到正则中的词，避免存在某些词包含某些词的情况，词的前面和后面必须是符号（"|"或")"），否则继续找
            //特殊情况*ST
            while(!"(*|".contains(String.valueOf(regex.charAt(startIndex - 1))) || !"|)".contains(String.valueOf(regex.charAt(endIndex)))) {
                tempRegex = tempRegex.substring(endIndex, tempRegex.length());
                startIndex = tempRegex.indexOf(matchedWord) + regex.indexOf(tempRegex);
                endIndex = startIndex + matchedWord.length();
                if (-1 == startIndex) {
                    System.out.println(matchedWord);
                    System.out.println(regex);
                    System.exit(-1);
                }
            }
            regex = regex.substring(0, startIndex) + "<" + matchedWord + ">" + regex.substring(endIndex, regex.length());
        }
        return regex;
    }

    /**
     * 通过规则映射进行情感分类
     *
     * @param title           文章标题
     * @param mappingRuleList 事件分类映射情感分类的规则
     * @return 返回匹配的信息，未匹配返回null
     */
    public MappingResult classifyByRule(String title, List<MappingRule> mappingRuleList) {

        List<MappingResult> mappingResultList = multiClassifyByRule(title, mappingRuleList, 1);
        if (ListUtil.isEmpty(mappingResultList)) {
            return null;
        }
        return mappingResultList.get(0);
    }

    public int multiClassifyByRuleWithFile(String path) {

        return classifyByCustomize(path, this.path);
    }


    /**
     * 自定义文章及匹配规则,并将结果写到当前目录文件中
     * @param textPath
     * @param rulePath
     * @return
     */
    public int classifyByCustomize(String textPath, String rulePath) {
        if (null == mappingRuleList) {
            init(rulePath);
        }
        List<String> testList = loadService.loadArticles(textPath);
        Map<String, String> eventMap = loadService.loadEventClassify();
        List<String> resultList = new ArrayList<>();
        int correctCount = 0;
        int classifyCount = 5;
        StringBuffer titleBuff = new StringBuffer("");
        for (int i = 0; i < testList.get(0).split("\t").length; i++) {
            //生成标题
            titleBuff.append("\t");
        }
        titleBuff.append("\t");
        for (int i = 0; i < classifyCount; i++) {
            titleBuff.append("分类" + (i + 1) + "\t");
            titleBuff.append("匹配规则"+ "\t");
            titleBuff.append("剔除关键词"+ "\t");
            titleBuff.append("剔除规则"+ "\t");
        }
        resultList.add(titleBuff.toString());
        for (String str : testList) {
            //title     content...      category
            String[] strArr = str.split("\t");


            List<MappingResult> mappingResultList = new ArrayList<>();
            mappingResultList = multiClassifyByRule(strArr[0], classifyCount);
//            //TODO 添加是否有分类名
//            boolean haveCategory = false;
//            if (haveCategory) {
//                //剔除category
//                String tempStr = str.substring(0, str.lastIndexOf(strArr[strArr.length - 1]));
//                mappingResultList = multiClassifyByRule(tempStr, 5);
//            } else {
//                mappingResultList = multiClassifyByRule(str, 5);
//            }
            strArr[1] = eventMap.get(Float.valueOf(strArr[1]).toString());

            //提取匹配到的关键词
            mappingResultList = extractRuelKeyword(mappingResultList);
            //合并正则
            mappingResultList = mergeMappingRule(mappingResultList);

            StringBuffer textBuffer = new StringBuffer("");

            boolean createTitle = false;

            for (String s : strArr) {

                textBuffer.append(s + "\t");
            }

            if (! ListUtil.isEmpty(mappingResultList)) {
                StringBuffer stringBuffer = new StringBuffer("");
                stringBuffer.append(textBuffer + " ");
                for (MappingResult mappingResult : mappingResultList) {
                    //保存
                    stringBuffer.append("\t" + mappingResult.getCategoryName()
                            + "\t" + mappingResult.getIncludeRegex()
                            + "\t" + mappingResult.getExcludeKeyword()
                            + "\t" + mappingResult.getExcludeRegex());
                }
                resultList.add(stringBuffer.toString());
                correctCount ++;
            }else {
                textBuffer.append(" " + "\t" + null);
                resultList.add(textBuffer.toString());
            }

            if (resultList.size() % 1000 == 0) {
                System.out.println("已匹配：" + resultList.size());
            }
        }

        createFile(resultList, textPath);


        return correctCount;
    }

    /**
     * 提取正则匹配到的关键词（<>中的内容）
     * @param mappingResultList
     * @return
     */
    private List<MappingResult> extractRuelKeyword(List<MappingResult> mappingResultList) {
        for (MappingResult mappingResult : mappingResultList) {

            String includeRegex = mappingResult.getIncludeRegex();
            includeRegex = extractRuelKeyword(includeRegex);

            String excludeRegex = mappingResult.getExcludeRegex();
            excludeRegex = extractRuelKeyword(excludeRegex);

            mappingResult.setIncludeRegex(includeRegex);
            mappingResult.setExcludeRegex(excludeRegex);
        }
        return mappingResultList;
    }

    private String extractRuelKeyword(String regex) {
        if (null == regex) {
            return null;
        }
        int firstLeftIndex = regex.indexOf("<");
        int firstRightIndex = regex.indexOf(">");
        int secondLeftIndex = regex.lastIndexOf("<");
        int secondRightIndex = regex.lastIndexOf(">");
        if (firstLeftIndex == secondLeftIndex) {
            //一个
            regex = regex.substring(firstLeftIndex, firstRightIndex + 1);
        }else {
            regex = regex.substring(firstLeftIndex, firstRightIndex + 1) + ".*?" + regex.substring(secondLeftIndex, secondRightIndex + 1);
        }
        return regex;
    }


    /**
     * 合并规则匹配结果，分类名相同 && 剔除规则相同的合并includeRegex
     * @param mappingResultList
     */
    private List<MappingResult> mergeMappingRule(List<MappingResult> mappingResultList) {
        List<MappingResult> resultList = new ArrayList<>();
        Map<String, List<MappingResult>> mappingResultMap = new HashMap<>();

        for (MappingResult mappingResult : mappingResultList) {
            String categoryName = mappingResult.getCategoryName();
            String excludeKeyword = StringUtils.isEmpty(mappingResult.getExcludeKeyword()) ? "null" : mappingResult.getExcludeKeyword();
            String excludeRegex = StringUtils.isEmpty(mappingResult.getExcludeRegex()) ? "null" : mappingResult.getExcludeRegex();
            String key = categoryName + "\t" + excludeKeyword + "\t" + excludeRegex;
            if (mappingResultMap.get(key) == null) {
                mappingResultMap.put(key, new ArrayList<>());
            }
            mappingResultMap.get(key).add(mappingResult);

        }

        for (Map.Entry<String, List<MappingResult>> entry : mappingResultMap.entrySet()) {
            String[] keyArr = entry.getKey().split("\t");
            MappingResult mappingResult = new MappingResult();
            mappingResult.setCategoryId(entry.getValue().get(0).getCategoryId());
            mappingResult.setCategoryName(keyArr[0]);
            mappingResult.setExcludeKeyword(keyArr[1]);
            mappingResult.setExcludeRegex(keyArr[2]);

            StringBuffer stringBuffer = new StringBuffer("");
            //合并正则
            for (MappingResult result : entry.getValue()) {
                if (StringUtils.isEmpty(stringBuffer)) {
                    stringBuffer = new StringBuffer(result.getIncludeRegex());
                }else {
                    stringBuffer.append("|||" + result.getIncludeRegex());
                }
            }

            mappingResult.setIncludeRegex(stringBuffer.toString());
            resultList.add(mappingResult);
        }
        return resultList;
    }

    /**
     * 生成文件
     * @param resultList
     * @param path
     */
    private void createFile(List<String> resultList, String path) {
        String newPath = FileUtil.getParentPath(path) + "\\" + FileUtil.getFileName(path) + "_match.tsv";

        try {
            System.out.println("输出路径：" + newPath);
            if (new File(newPath).exists()) {
                System.out.println("文件存在...");
                ApplicationCache.saveCache(resultList, newPath);
            }else {
                FileUtil.createFile(resultList, newPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            ApplicationCache.saveCache(resultList, newPath);
        }
    }

    private String replaceSynbolOfTableAndSplice(String str) {
        String[] strArr = str.split("\t");
        str = StringsUtilCustomize.replaceSynbolOfTable(str);

        //标题 + \t + 内容 + category
        int categotyIndex = str.lastIndexOf(strArr[strArr.length - 1]);
        String category = str.substring(categotyIndex, str.length());
        str = strArr[0] + "\t" + str.substring(strArr[0].length(), categotyIndex) + "\t" + category;
        return str;
    }


    /**
     * 加载所有分类的匹配规则，以map的形式返回
     *
     * @return Map:
     * key: category
     * value: RuleRegex
     */
    public List<MappingRule> loadRule(String path) {

        //加载关键词
        List<RuleInfo> ruleInfoList = loadRuleInfo(path);
        //加载映射规则
        List<MappingRule> mappingRuleList = new ArrayList<>();
        //分类映射map
        HashMap<String, String> mappingCategoryMap = loadClassifyMappingRule(path);

        for (RuleInfo ruleInfo : ruleInfoList) {
            //一类分类的正则集合
            List<RuleRegex> ruleRegexList =  regexService.createRuleRegex(ruleInfo.getRuleKeywordList());
            String mappingCategory = mappingCategoryMap.get(ruleInfo.getCategoryId() + "\t" + ruleInfo.getCategoryName());
            MappingRule mappingRule = new MappingRule(ruleInfo.getCategoryId(), ruleInfo.getCategoryName(), mappingCategory, ruleRegexList);
            mappingRuleList.add(mappingRule);
        }

        return mappingRuleList;
    }


    /**
     * 从文件中加载关键词
     * 数据形式：categoryId   categoryName    includeKeyword  excludeKeyword  excludeMultipleKeyword2
     *
     * @param path
     * @return
     */
    public List<RuleInfo> loadRuleInfo(String path) {
        //origial形式    categoryId   categoryName    includeKeyword  excludeKeyword  excludeMultipleKeyword2
        List<String> origialInfoList = FileUtil.readAll(path);

        //每个分类下的关键词的Map
        HashMap<String, List<RuleKeyword>> categoryMap = new HashMap<>();
        for (String origial : origialInfoList) {
            //删除空格
            origial = StringsUtilCustomize.substringByDeleteSpace(origial);
            String[] origialArr = origial.split("\t");
            RuleKeyword ruleKeyword = new RuleKeyword();
            ruleKeyword.setIncludeKeyword(origialArr[2]);
            ruleKeyword.setExcludeKeyword(origialArr[3]);
            ruleKeyword.setExcludeMultipleKeyword(origialArr[4]);
            if (null == categoryMap.get(origialArr[0] + "\t" + origialArr[1])) {
                //categoryId + name做为key
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

    /**
     * 加载分类映射规则
     *
     * 数据形式  categoryId  categoryName    mappingCategoty
     * @param path
     * @return 分类映射 map:
     *      key: categotyId + \t + categoryName
     *      value: mappingCategory
     */
    private HashMap<String, String> loadClassifyMappingRule(String path) {
        List<String> origialMappingRuleInfos = FileUtil.readAll(path);
        HashMap<String, String> mappingCategoryMap = new HashMap<>();

        for (String origialMappingRuleInfo : origialMappingRuleInfos) {
            if (StringUtils.isEmpty(origialMappingRuleInfo)) {
                continue;
            }
            String[] origiaArray = origialMappingRuleInfo.split("\t");
            mappingCategoryMap.put(origiaArray[0] + "\t" + origiaArray[1], origiaArray[2]);
        }

//        return mappingCategoryMap;
        return new HashMap<String, String>();
    }
}
