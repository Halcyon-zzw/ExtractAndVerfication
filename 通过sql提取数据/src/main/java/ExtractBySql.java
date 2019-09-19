import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.service.ExportTest.from;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/9/4 18:46
 * @Version: 1.0
 */
public class ExtractBySql {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        extract();
    }

    private static void extract() throws ClassNotFoundException, SQLException, IOException {
        String to = "E:\\下载\\钉钉文件\\工作资料\\create\\栏目分类\\time9-4\\栏目数据_9-4.csv";

        //数据库地址
        String databaseUrl = "jdbc:sqlserver://10.102.0.150:1433;DatabaseName=JYDB";
//        String databaseUrl = "jdbc:sqlserver://10.101.0.212:1433;DatabaseName=JYDB";

        Connection conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        //连接，设置用户名和密码
        conn = DriverManager.getConnection(databaseUrl, "JYBD_READ", "JYBD_READ");
        final Statement stmt = conn.createStatement();

        CSVWriter writer = new CSVWriter(new FileWriter(to));

        String sql = "select ID, Category, InfoTitle, Content from\n" +
                "\t(select ROW_NUMBER()  over(partition by Category order by InsertTime DESC) as rnum, *\n" +
                "\tfrom NI_NewsInfo) as T\n" +
                "where T.rnum <= 10";

        ResultSet resultSet = stmt.executeQuery(sql);
        ColomuData colomuData = new ColomuData();
        while (resultSet.next()) {
            colomuData.setId(resultSet.getString("ID"));
            colomuData.setCategory(resultSet.getString("Category"));
            colomuData.setTitle(resultSet.getString("InfoTitle"));
            colomuData.setContent(resultSet.getString("Content"));

            writer.writeNext(colomuData.toStrings());
        }
    }


}
