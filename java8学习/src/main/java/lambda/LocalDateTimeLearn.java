package lambda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/10/8 16:42
 * @Version: 1.0
 */
public class LocalDateTimeLearn {
    public static void main(String[] args) {
        LocalDateTimeLearn localDateTimeLearn = new LocalDateTimeLearn();
        localDateTimeLearn.testLocalDateTimeLearn();
    }

    private void testLocalDateTimeLearn() {
        //获取当前time
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("当前时间：" + currentTime);

        System.out.println("当前日期：" + LocalDate.now());

        //当前日期
        LocalDate date1 = LocalDateTime.now().toLocalDate();
        System.out.println("当前日期：" + date1);

        Month month = currentTime.getMonth();
        //月的第几天
        int day = currentTime.getDayOfMonth();
        int seconds = currentTime.getSecond();
        System.out.println("月: " + month + ", 日: " + day + ", 秒: " + seconds);

        //本年本月10号
        LocalDateTime date2 = currentTime.withDayOfMonth(10);
        System.out.println("date2: " + date2);

        //12 december 2019
        LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
        System.out.println("date3: " + date3);

        //22点15分
        LocalTime date4 = LocalTime.of(22, 15);
        System.out.println("date4: " + date4);

        //解析字符串
        LocalTime date5 = LocalTime.parse("20:15:32");
        System.out.println("date5: " + date5);

    }
}
