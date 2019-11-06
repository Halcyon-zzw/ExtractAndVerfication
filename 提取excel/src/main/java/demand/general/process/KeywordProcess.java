package demand.general.process;

import config.ApplicationProperties;
import lombok.Getter;
import lombok.Setter;
import util.FileUtil;
import util.StringsUtilCustomize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽取包含关键字的句子
 *
 * @Author: zhuzw
 * @Date: 2019/10/30 16:53
 * @Version: 1.0
 */
public class KeywordProcess implements ProcessWay {
    List<String> keywordList = new ArrayList<>();
    Set<String> keywordSet = new HashSet<>();
    private ApplicationProperties aps = new ApplicationProperties();

    @Getter
    private String type = "keyword";

    @Override
    public String process(String str) {

        //关键字路径
        String keywordsPath = aps.getKeywordsPath();
        //只需要第一次读取文件
        if (keywordList.size() == 0) {
            try {
                keywordList = FileUtil.readAll(keywordsPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //删除标识字符串所在块
        String[] blockStrings = {"虚假记载、误导性陈述", "证券代码", "债券代码", "备份文件", "本公司董事会研究确定",
                "敬请广大投资者"};
        //获得句子集合,并过滤长度大于200的句子、包含无效词语的句子；删除指定的句子
        List<String> sentenceList = StringsUtilCustomize.splitSentence(str, aps.getSentenceSeparator())
                .stream()
                .filter(s -> s.length() < 200)
                .filter(s -> {
                    for (String blockString : blockStrings) {
                        if (s.contains(blockString)) {
                            //包含无效词语，删除
//                            System.out.println(s);
                            return false;
                        }
                    }
                    return true;
                } )
                .map(s -> {
                    //删除 “特此公告”及之后内容
                    s = StringsUtilCustomize.substringByDeleteAfterLast(s, "特此公告");
                    String[] beginStrings = {"电", "讯", "获悉", "公告", "报道", "介绍", "快报", "消息", "公布", "显示", "称"};
                    //删除开头20字符内的 beginStrings
                    for (String beginString : beginStrings) {
                            int beginIndex = s.indexOf(beginString);
                        if (beginIndex <= 20) {
                            s = StringsUtilCustomize.substringByDeleteBefore(s, beginString);
                        }
                    }
                    return s;
                })
                .map(s -> s + "。")
                .collect(Collectors.toList());

        //提取包含关键字的句子
        StringBuilder resultString = new StringBuilder("");
        for (String sentence : sentenceList) {
            for (String keyword : keywordList) {
                if (sentence.contains(keyword)) {
                    resultString.append(sentence);
//                    System.out.println("关键字：" + keyword);
                    break;
                }
            }
        }
        return resultString.toString();
    }

    @Override
    public void info() {
        System.out.println(">>>关键词提取");
    }
}
