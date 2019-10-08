package lambda;

import java.util.Optional;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/10/8 15:50
 * @Version: 1.0
 */
public class OptionalLearn {
    public static void main(String[] args) {
        OptionalLearn optionalLearn = new OptionalLearn();

        Integer value_1 = null;
        Integer value_2 = new Integer(10);

        Optional<Integer> optional_1 = Optional.ofNullable(value_1);
        Optional<Integer> optional_2 = Optional.of(value_2);

        System.out.println(optionalLearn.sum(optional_1, optional_2));
    }

    public Integer sum(Optional<Integer> optional_1, Optional<Integer> optional_2) {
        System.out.println("第一个参数存在：" + optional_1.isPresent());
        System.out.println("第二个参数存在：" + optional_2.isPresent());

        //如果值存在，返回；否则返回默认值
        Integer value_1 = optional_1.orElse(new Integer(0));
        //获取值，值必须存在
        Integer value_2 = optional_2.get();

        return value_1 + value_2;
    }
}
