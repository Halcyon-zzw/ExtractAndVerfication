package demand.general;

import lombok.Data;
import model.LabelAndCount;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数量控制
 *
 * @Author: zhuzw
 * @Date: 2019/9/10 14:53
 * @Version: 1.0
 */
@Data
public class CountControl {

    /**
     * 用于控制
     */
    private HashMap<String, Integer> controlMap = new HashMap<>();

    /**
     * 用于存储文章长度范围
     */
    private HashMap<String, Integer> rangeMap = new HashMap<>();

    /**
     * 统计分类范围
     */
    private HashMap<String, Integer> categoriesMap = new HashMap<>();

    /**
     * 用于单文件提取数据
     */
    public CountControl() {
    }

    /**
     * 构造函数，可以传入hashMap以控制每个分类提取的数量
     *
     * @param controlMap
     */
    public CountControl(HashMap<String, Integer> controlMap) {
        this.controlMap = controlMap;
    }

    /**
     * 获取下一步的操作状态
     *
     * @param label       控制的key
     * @param count       数量, 为-1时，表示提取所有数据
     * @param categoryNum 分类数量
     * @return 1、数据量不够，继续操作
     * 0、该分类数据量够，跳过操作
     * -1、数据量收集完毕，终止操作
     */
    public int operationStatus(String label, int count, int categoryNum) {

        if (null == controlMap.get(label)) {
            //初始化
            controlMap.put(label, 0);
        }
        if (-1 == count) {
            //提取全部数据
            controlMap.put(label, controlMap.get(label) + 1);
            return 1;
        }
        //-1、所有数据够
        int toolNum = 0;
        for (Map.Entry<String, Integer> entry : controlMap.entrySet()) {
            toolNum += entry.getValue();
        }
        if (toolNum >= count * categoryNum) {
            return -1;
        }
        //1、数据不够情况
        if (controlMap.get(label) < count) {
            controlMap.put(label, controlMap.get(label) + 1);
            return 1;
        }
        //0、该类分类数据够情况
        return 0;
    }


    /**
     * 过滤数据量少的数据
     *
     * @param tempResultMap 存储label，label+acticle的map
     * @param lessCount     过滤的数据量
     * @return
     */
    public HashMap<String, List<String>> filterLess(HashMap<String, List<String>> tempResultMap, int lessCount) {
        //过滤数量少的数据，并将结果返回成原来的map中
        tempResultMap = tempResultMap.entrySet().stream()
                .filter(tempResult -> tempResult.getValue().size() > lessCount)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));


        //controlMap过滤数据少的数据，并将结果返回至map中
        controlMap = controlMap.entrySet().stream()
                .filter(tempResult -> tempResult.getValue() > lessCount)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));

        return tempResultMap;
    }

    /**
     * 获取提取（统计）情况
     *
     * @return
     */
    public List<String> getExtract() {
        List<String> resultList = new ArrayList<>();
        resultList.addAll(getCategoriesStatestics());
        resultList.addAll(getRangeStatestics());
        return resultList;
    }


    /**
     * 获取类别提取情况
     * @return 返回形式  label: 数量
     */
    public List<String> getCategoriesStatestics() {
        //controlMap转成List<LabelAndCount>对象后对label排序，后转成List<String>对象
        List<String> resultList = controlMap.entrySet().stream()
                .map(temp -> new LabelAndCount(Integer.valueOf(temp.getKey()), temp.getValue()))
                .sorted(Comparator.comparing(LabelAndCount::getLabel))
                .map(LabelAndCount::toString)
                .collect(Collectors.toList());

        return resultList;
    }

    /**
     * 获取范围提取情况
     *
     * @return
     */
    public List<String> getRangeStatestics() {
        List<String> resultList = rangeMap.entrySet().stream()
                .map(temp -> temp.getKey() + "：" + temp.getValue())
                .sorted().collect(Collectors.toList());
        return resultList;
    }

    /**
     * 统计文章长度范围
     * 0 - 100
     * 100 - 200
     * 200 - 300
     * 300 - 400
     * 400 - 500
     * 500以上
     *
     * @param article
     */
    public void rangeStatistics(String article) {
        int articleLenKey = article.length() / 100;
        articleLenKey *= 100;

        String rangeKey = articleLenKey + "-" + (articleLenKey + 100);

        if (null == rangeMap.get(rangeKey)) {
            rangeMap.put(rangeKey, 0);
        }
        rangeMap.put(rangeKey, rangeMap.get(rangeKey) + 1);
    }

    public void categoriesStatistics(String category) {
        if (null == categoriesMap) {
            categoriesMap.put(category, 0);
        }
        categoriesMap.put(category, categoriesMap.get(category) + 1);
    }
}
