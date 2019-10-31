package demand.general.process;

import config.ApplicationProperties;
import org.apache.commons.lang3.StringUtils;
import util.StringsUtilCustomize;

import java.io.*;
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
    private ApplicationProperties aps = new ApplicationProperties();

    @Override
    public String process(String str) {
        List<String> keywordList = new ArrayList<>();
        //加载关键字
        String keywordsPath = aps.getKeywordsPath();
        if (keywordList.size() == 0) {
            BufferedReader bReader = null;
            try {
                bReader = new BufferedReader(new InputStreamReader(new FileInputStream(keywordsPath), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line = null;
            int i = 0;
            while (true) {
                try {
                    if (!((line = bReader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!StringUtils.isEmpty(line)) {
                    keywordList.add(line);
                }
                i++;
            }
        }

        //获得句子集合,并过滤长度大于100的句子
        List<String> sentenceList = StringsUtilCustomize.splitSentence(str, aps.getSentenceSeparator())
                .stream().filter(s -> s.length() > 100)
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
