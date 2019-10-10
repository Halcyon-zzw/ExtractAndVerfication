package request;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/10/9 16:39
 * @Version: 1.0
 */
public class TestUrlFactory {
    public static String getTestUrl(String testName) {
        if (null == testName) {
            return null;
        }
        if (testName.equalsIgnoreCase("emotion")) {
            //7个情感分类
            return "http://10.106.0.51:8007/encode";
        }else if (testName.equalsIgnoreCase("column")) {
            //栏目分类：22分类
            return "http://10.106.0.51:8022/encode";
        }else if (testName.equalsIgnoreCase("event")) {
            //事件分类：62分类
            return "http://10.106.0.51:8062/encode";
        }
        return null;
    }
}
