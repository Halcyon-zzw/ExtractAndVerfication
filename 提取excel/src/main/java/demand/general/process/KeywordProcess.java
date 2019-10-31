package demand.general.process;

import config.ApplicationProperties;
import util.FileUtil;
import util.StringsUtilCustomize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private ApplicationProperties aps = new ApplicationProperties();

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

        //获得句子集合,并过滤长度大于200的句子
        List<String> sentenceList = StringsUtilCustomize.splitSentence(str, aps.getSentenceSeparator())
                .stream().filter(s -> s.length() < 200)
                .map(s -> s + "。")
                .collect(Collectors.toList());

        //提取包含关键字的句子
        StringBuilder resultString = new StringBuilder("");
        for (String sentence : sentenceList) {
            for (String keyword : keywordList) {
                if (sentence.contains(keyword)) {
                    resultString.append(sentence);
                    break;
                }
            }
        }
        return resultString.toString();
    }
}
