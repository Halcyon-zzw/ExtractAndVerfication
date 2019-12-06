package demand.mapping.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import demand.mapping.model.TermSimplify;
import util.FileUtil;
import util.MapUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/28 13:27
 * @Version: 1.0
 */
public class StatisticsService {

    private LoadService loadService = new LoadService();

    /**
     * 统计某一文件中的特定词性、特定词频的词
     * @param path 文件路径，目前仅支持txt，tsv,csv文件
     * @return
     */
    public Map<TermSimplify, Integer> statisticsWordFrequencyWithFile(String path, List<String> natures, int frequency) {
        List<String> textList = loadValue(path);
        Map<TermSimplify, Integer> resultMap = new LinkedHashMap<>();
        for (String text : textList) {
            resultMap.putAll(statisticsWordFrequency(text, natures, frequency));
        }
        return sortMap(resultMap);
    }

    public Map<TermSimplify, Integer> statisticsWordFrequencyWithFileByFilter(String path, List<String> natures, int frequency) {
        List<String> textList = loadValue(path);
        Map<TermSimplify, Integer> resultMap = new LinkedHashMap<>();
        for (String text : textList) {
            resultMap.putAll(statisticsWordFrequency(text, natures, frequency));
        }
        return sortMap(resultMap);
    }

    /**
     * 统计某一文件中的所有词的词频
     * @param path
     * @return
     */
    public Map<TermSimplify, Integer> statisticsAllWordFrequencyWithFile(String path) {
        List<String> textList = loadValue(path);
        Map<TermSimplify, Integer> resultMap = new LinkedHashMap<>();
        for (String text : textList) {
            resultMap.putAll(statisticsAllWordFrequency(text));
        }
        return sortMap(resultMap);
    }

    /**
     * 统计某一文件中的词频（过滤一些无用词性）
     * @param path
     * @return
     */
    public Map<TermSimplify, Integer> statisticsWordFrequencyWithFileByFilter(String path) {
        List<String> textList = loadValue(path);
        Map<TermSimplify, Integer> resultMap = new LinkedHashMap<>();
        for (String text : textList) {
            resultMap.putAll(statisticsAllWordFrequencyByFilter(text));
        }
        return sortMap(resultMap);
    }

    /**
     * 统计所有词的词频，并按降序排序
     * @param text 需要统计的内容
     * @return termSimplify的次数
     */
    public Map<TermSimplify, Integer> statisticsAllWordFrequency(String text) {
        List<Term> termList = HanLP.segment(text);

        List<TermSimplify> termSimplifyList = termList.stream()
                .map(term -> new TermSimplify(term.word, term.nature.toString()))
                .collect(Collectors.toList());
        Map<TermSimplify, Integer> allWordFrequencyMap = new HashMap<>();
        for (TermSimplify termSimplify : termSimplifyList) {
            if (null == allWordFrequencyMap.get(termSimplify)) {
                allWordFrequencyMap.put(termSimplify, 0);
            }
            allWordFrequencyMap.put(termSimplify, allWordFrequencyMap.get(termSimplify) + 1);
        }

        return sortMap(allWordFrequencyMap);
    }


    /**
     * 统计所有词（过滤了一些无用词，如o：拟声词，u：助词，w：标点符号）的词频，并按降序排序
     * @param text
     * @return
     */
    public Map<TermSimplify, Integer> statisticsAllWordFrequencyByFilter(String text) {

        Map<TermSimplify, Integer> allWordFrequencySortMap = statisticsAllWordFrequency(text);
        //过滤词性为以natureArr中开头的词以及停用词
        return filter(allWordFrequencySortMap);
    }

    /**
     * 过滤一些特殊词性及停用词
     * @param wordFrequencyMap
     * @return
     */
    private Map<TermSimplify, Integer> filter(Map<TermSimplify, Integer> wordFrequencyMap) {
        return wordFrequencyMap.entrySet().stream()
                .filter(entry -> !entry.getKey().getNature().startsWith("o"))
                .filter(entry -> !entry.getKey().getNature().startsWith("u"))
                .filter(entry -> !entry.getKey().getNature().startsWith("w"))
                .filter(entry -> !CoreStopWordDictionary.contains(entry.getKey().getWord().toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    /**
     * 统计所有词（过滤了一些无用词，如o：拟声词，u：助词，w：标点符号）的词频，并按降序排序
     * @param text
     * @return
     */
    public Map<TermSimplify, Integer> statisticsWordFrequencyByFilter(String text, List<String> natureList, int frequency) {

        Map<TermSimplify, Integer> wordFrequencyByFilter = statisticsWordFrequency(text, natureList, frequency);
        //过滤词性为以natureArr中开头的词以及停用词
        return filter(wordFrequencyByFilter);
    }


    /**
     * 统计词频，默认统计名词、动词、形容词词频在5以上的词
     * @param text
     * @return
     */
    public Map<TermSimplify, Integer> statisticsWordFrequency(String text) {
        String[] natureArr = {"n", "v", "a"};
        List<String> natureList = new ArrayList<>(Arrays.asList(natureArr));
        return statisticsWordFrequency(text, natureList, 5);
    }

    /**
     * 统计指定词性的词频
     * @param text
     * @param natureList
     * @return
     */
    public Map<TermSimplify, Integer> statisticsWordFrequency(String text, List<String> natureList) {
        Map<TermSimplify, Integer> wordFrequencySortMap = statisticsAllWordFrequency(text);

        return wordFrequencySortMap.entrySet().stream()
                .filter(entry ->{
                    for (String nature : natureList) {
                        if (entry.getKey().getNature().startsWith(nature)) {
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    /**
     * 统计指定词性的词频达到一定数量的词
     * @param text
     * @param natures 词性列表
     * @param frequency 指定频率以上的才返回
     * @return
     */
    public Map<TermSimplify, Integer> statisticsWordFrequency(String text, List<String> natures, int frequency) {
        //指定词性的Map
        Map<TermSimplify, Integer> wordFrequencySortMap = statisticsWordFrequency(text, natures);
        //过滤词频过少词
        return wordFrequencySortMap.entrySet().stream()
                .filter(entry -> entry.getValue() >= frequency)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }



    private Map<TermSimplify, Integer> sortMap(Map<TermSimplify, Integer> map) {
        Map<TermSimplify, Integer> resultMap = (new MapUtil<TermSimplify, Integer>())
                .sortMapByValue(map, new Comparator<Map.Entry<TermSimplify, Integer>>() {
                    @Override
                    public int compare(Map.Entry<TermSimplify, Integer> o1, Map.Entry<TermSimplify, Integer> o2) {
                        //降序
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
        return resultMap;
    }

    /**
     * 加载需要统计的文本
     * @param path
     * @return
     */
    private List<String> loadValue(String path) {
        return loadService.loadText(path);
    }
}
