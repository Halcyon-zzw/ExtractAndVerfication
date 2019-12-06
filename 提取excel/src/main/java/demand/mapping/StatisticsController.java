package demand.mapping;

import config.ApplicationCache;
import demand.mapping.model.TermSimplify;
import demand.mapping.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/29 10:32
 * @Version: 1.0
 */
@Controller
public class StatisticsController {
    private StatisticsService statisticsService = new StatisticsService();

    /**
     * 统计词的词频，过滤一些特殊词性及特殊词
     * @param path
     */
    @RequestMapping("/statistics")
    public void statisticsWordFrequencyWithFileByFilter(String path) {
        Map<TermSimplify, Integer> wordFrequency = statisticsService.statisticsWordFrequencyWithFileByFilter(path);

        //写入文件
        createFile(wordFrequency, path);
    }

    @RequestMapping("/")
    public void test() {
        System.out.println("Hello zhuzw!!!");
    }


    /**
     * 自定义统计规则，设置统计的词性及词频
     * @param path
     */
    public void statisticsWordFrequencyWithFile(String path, String[] natureArr, int frequency) {
        List<String> natures = new ArrayList<>(Arrays.asList(natureArr));
        Map<TermSimplify, Integer> wordFrequency = statisticsService.statisticsWordFrequencyWithFile(path, natures, frequency);

        //写入文件
        createFile(wordFrequency, path);
    }

    private void createFile(Map<TermSimplify, Integer> wordFrequencyMap, String path) {
        List<String> wordFrequencyList = wordFrequencyMap.entrySet().stream()
                .map(entry -> {
                    return entry.getKey().getWord()
                            + "\t" + entry.getKey().getNature()
                            + "\t" + entry.getValue();
                }).collect(Collectors.toList());

        String newPath = FileUtil.getParentPath(path) + "\\" + FileUtil.getFileName(path) + "_frequency.tsv";

        if (new File(newPath).exists()) {
            System.out.println("文件存在...");
            System.out.println("尝试保存至缓存文件...");
            ApplicationCache.saveCache(wordFrequencyList, newPath);
        } else {
            try {
                FileUtil.createFile(wordFrequencyList, newPath);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("文件被占用...");
                System.out.println("尝试保存至缓存文件...");
                ApplicationCache.saveCache(wordFrequencyList, newPath);
            }
        }
    }
}
