package demand;

import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/12/2 10:11
 * @Version: 1.0
 */
public class RequestSqlThread extends Thread {
    private Thread t;
    public static ConcurrentLinkedDeque<String> resultList = new ConcurrentLinkedDeque<>();;

    private String threadName;
    private List<String> list;

    private Statement stmt;

    private String sql;


    public RequestSqlThread(String threadName, List<String> list) {

        this.threadName = threadName;
        this.list = list;

    }

    public RequestSqlThread(String threadName, List<String> list, Statement stmt, String sql) {
        this(threadName, list);
        this.stmt = stmt;
        this.sql = sql;

    }


    @Override
    public void run() {
//        List<String[]> subList = list.subList(startIndex, endIndex);
        System.out.println(threadName + "处理了" + list.size() + "条！");
        for (String str : list) {
            String id = str.split("\t")[0];
            ResultSet resultSet = null;
            try {
                resultSet = stmt.executeQuery(sql + "'" + id + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (resultSet.next() && resultSet.getString("XGSJ").contains("2019")) {
                    resultList.add(str);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}

