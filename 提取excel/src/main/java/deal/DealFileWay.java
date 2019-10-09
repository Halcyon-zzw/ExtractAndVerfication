package deal;

import demand.general.CountControl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理文件方式接口
 *
 * @Author: zhuzw
 * @Date: 2019/8/7 19:46
 * @Version: 1.0
 *
 * 经提取，处理文件方式已提取成通用流程
 * 1、获取文件 -> 2、一行一行读 -> 3、是否处理 -> 4、提取一行数据 -> 5、添加数据到list
 * 1、获取文件： 定义获取文件操作类
 * 3、ColumnProcess类
 * 4、Extract一行类
 *
 * 故将接口类更改为抽象类
 *
 * 处理文件方式抽象类(详情见AbstractDealFileWay类)
 * @Author: zhuzw
 * @Date: 2019/8/29 9:07
 * @Version: 2.0
 */
public interface DealFileWay {
    /**
     * 提取文件值，具体提取方法在实现类中实现
     *
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    List<String> extractedValue(String path) throws IOException;


    /**
     * 数据提取情况
     * @param countControl 数量控制对象
     * @param tempResultMap 数据提取map
     */
    default void dataSituation(CountControl countControl, HashMap<String, List<String>> tempResultMap) {
        System.out.println("处理完毕!");
        //提取情况,输出提取数据的情况。label: number
        List<String> extractSituation = countControl.getExtract();
        System.out.println("----------数据量情况------------");
        extractSituation.forEach(System.out::println);

    }

    /**
     * 过滤数据量少的数据
     * @param countControl 数据控制对象
     * @param tempResultMap 数据提取map
     * @param dataNum 控制的数据量
     */
    default HashMap<String, List<String>> fileLessData(CountControl countControl, HashMap<String, List<String>> tempResultMap, int dataNum) {
        //值为负数，不过滤
        if (dataNum < 0) {
            return tempResultMap;
        }
        System.out.println("----------过滤数据量少的情况------------");
        tempResultMap = countControl.filterLess(tempResultMap, dataNum);
        List<String> extractSituation = countControl.getExtract();
        extractSituation.forEach(System.out::println);
        return tempResultMap;
    }

    /**
     * 从数据存储map中提取数据
     * @param resultList 结果列表
     * @param tempResultMap 数据提取map
     * @return
     */
    default List<String> addData(List<String> resultList, HashMap<String, List<String>> tempResultMap) {
        //        List<String> resultList = tempResultMap.entrySet().stream()
//                .map(a -> {return a.getValue();})
//                .collect(Collectors.toList());
        //TODO 改用流操作
        for (Map.Entry<String, List<String>> temp : tempResultMap.entrySet()) {
            resultList.addAll(temp.getValue());
        }
        System.out.println("----数据量：" + resultList.size());
        return resultList;
    }
}
