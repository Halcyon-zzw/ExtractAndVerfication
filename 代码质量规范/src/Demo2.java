import java.util.*;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/8/18 9:45
 * @Version: 1.0
 */
public class Demo2 {
    public static void main(String[] args) throws Exception {
        demo();
    }


    /**
     * 大于100的三位数，求100到 该变量之间满足如下要求的数字之和：
     * <p>
     * 个位数不为7
     * 十位数不为5
     * 百位数 != 3
     */
    public static void demo() throws Exception {
        //输入对象
        Scanner input = new Scanner(System.in);

        Map<Integer, Integer> map = new HashMap<>();
//        map.values()

//        map.get(1).compareTo()
        System.out.println("输入数字：");
        int num = input.nextInt();

        int result = 0;
        for (int i = 101; i <= num; i++) {
            //获得百位
            int baiWei = i / 100;

            //获得十位
            int shiWei = (i / 10) % 10;
            //个位
            int geWei = i % 100;


            //判断
            if (geWei != 7 && shiWei != 5 && baiWei != 3) {
                result += i;
            }

        }

        System.out.println(result);
    }


    public static void mapSort(Map<String, String> map) {
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
        Collections.sort(list, (m1, m2) -> m1.getValue().compareTo(m2.getValue()));

//        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
//            //升序排序
//            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//
//        });
    }


}
