package lambda;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * lambda学习
 *
 * @Author: zhuzw
 * @Date: 2019/9/20 13:46
 * @Version: 1.0
 */
public class LambdaLearn {
    public static void main(String[] args) {
//        achieveRunable();
//        achieveMapSort();

        //流操作
        streamOperation();
    }

    private static void achieveMapSort() {
        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("zhuzw", "23");
        nameMap.put("zhuzlz", "26");
        nameMap.put("zhuxz", "25");
        nameMap.put("zhuys", "48");
        nameMap.put("wumx", "47");

        System.out.println("排序前...");
        for (Map.Entry<String, String> map : nameMap.entrySet()) {
            System.out.println(map.getKey() + ":" + map.getValue());
        }

        //排序 1.2  lambda
//        List<Map.Entry<String, String>> nameList = new ArrayList<>(nameMap.entrySet());
//        Collections.sort(nameList, (name1, name2) -> name1.getValue().compareTo(name2.getValue()));

        //1.3 stream方式  降序排序
        List<Map.Entry<String, String>> nameList = nameMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, String>::getValue).reversed())
                .collect(Collectors.toList());

        System.out.println("排序后...");
        for (Map.Entry<String, String> map : nameList) {
            System.out.println(map.getKey() + ":" + map.getValue());
        }
    }

    /**
     * 实用lambda实现Runnable
     */
    public static void achieveRunable() {
        //1.1、使用匿名类实现
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world!");
            }
        }).start();

        //1.2、使用lambda
        new Thread(() -> System.out.println("Hello lambda!")).start();

        //2.1 简单的Runnable
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("2.1 Hello world!");
            }
        };
        runnable1.run();

        //2.2
        Runnable runnable2 = () -> System.out.println("2.2 Hello world!");
        runnable2.run();

    }

    /**
     * 流操作
     */
    public static void streamOperation() {
        //去重
        System.out.println("-----------------去重-----------------");
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        List<Integer> distinctNumbers = numbers.stream().distinct().collect(Collectors.toList());
        System.out.println("去重后...");
        distinctNumbers.forEach(System.out::println);

        //求和
        System.out.println("-----------------求和-----------------");
        int numbersSum = numbers.stream().mapToInt(Integer::intValue).sum();
        System.out.println("nums总和：" + numbersSum);

        //指定数量的流
        int numbersStrSum = numbers.stream().mapToInt(Integer::intValue).limit(3).sum();
        System.out.println("指定数量的nums和：" + numbersStrSum);

        //排序
        System.out.println("-----------------排序-----------------");
        List<Integer> numbersSort = numbers.stream().sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());
        System.out.println("排序后的数据...");
        numbersSort.forEach(System.out::println);

        //filter,获取空字符串的数量
        System.out.println("-----------------过滤-----------------");
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        int count = (int)strings.stream().filter(string -> string.isEmpty()).count();
        System.out.println("空字符串的数量：" + count);

        //TODO Contractors.toMap


        //统计
        System.out.println("-----------------统计-----------------");
        IntSummaryStatistics statistics = numbers.stream().mapToInt(x -> x).summaryStatistics();
        System.out.println("数量：" + statistics.getCount());
        System.out.println("最大值：" + statistics.getMax());
        System.out.println("和：" + statistics.getSum());
        System.out.println("平均值：" + statistics.getAverage());



    }

}
