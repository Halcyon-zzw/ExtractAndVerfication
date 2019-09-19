import javax.xml.crypto.Data;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 计算两个日期之间的天数
 *
 * 日期格式：20150105 20141215
 *
 * 结果：21
 *
 * @Author: zhuzw
 * @Date: 2019/8/18 9:15
 * @Version: 1.0
 */
public class Demo {
    public static void main(String[] args) {
        //开始时间
        SimpleDateFormat formator = new SimpleDateFormat("yyyyMMdd");
//        formator.parse("20150105");
//        formator.parse("20160302");
        Date startDate = new Date("20150105");
        Date secDate = new Date("20160302");
        long startDateTime = startDate.getTime();
        long secDateTime = secDate.getTime();
        long l=0L;
        if(secDateTime>startDateTime){
            l=secDateTime-startDateTime;
        }else{
            l=startDateTime-secDateTime;
        }
        long result=l/(1000*60*24*60);
        System.out.println(result);

//        startDate.set
        //结束时间

        //计算时间

        //输出天数
    }
    //判断是否是闰年
//    public Boolean isLeapYear(int year){
//        if(()year%4 == 0 && year%100 = 0 )
//        return false;
//    }

    public static void main2(String[] args) {
        SimpleDateFormat formator = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = formator.parse("20150105");
            Date date2 = formator.parse("20160302");


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




}
