package delete;

import deal.DealFileWay;
import model.News;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 舆情情感分类数据处理，Excel文件
 *
 * @Author: zhuzw
 * @Date: 2019/8/7 19:49
 * @Version: 1.0
 */
public class LyricExcelDeal implements DealFileWay {

    /**
     * 处理方式
     *
     * @param row      行数据
     * @param id       资讯编号
     * @return -1: break     0: continue     1:处理
     */
    private static int dealWay(Row row, String id) {
        //剔除lastRowIndex与数据填充不一致的情况
        if (null == row) {
            return -1;
        }

        //108020、108025中row不为null，而没有数据，且getCell(0)不为null
        if (null == row.getCell(1)) {
            return -1;
        }

        //部分文件分类代码(cell = 1)存在且为空
        if ("".equalsIgnoreCase(row.getCell(1).toString())) {
            return 0;
        }

        //判断行是否需要处理,为NO不处理
        if ("NO".equalsIgnoreCase(row.getCell(0).toString())) {
            return 0;
        }

        //判断数据是否正确（文件名和数据中的id是否一致）
        if (!row.getCell(1).toString().contains(id)) {
            //不相等
            System.out.println(id + "数据错误");
            return 0;
        }
        return 1;
    }

    /**
     * 提取excel表中一行的值
     *
     * @param row 行数据
     * @return
     */
    private static News extractRowValue(Row row) {
        News news = new News();
        //设置news属性
        //获取id为xxxx.0形式，剔除 .0
        news.setId(row.getCell(1).toString().split("\\.")[0]);
        news.setTitle(row.getCell(3).toString());
        news.setContent(row.getCell(4).toString());
        return news;
    }

    @Override
    public List<String> extractedValue(String excelPath) {

        //从路径中获取id
        String id = excelPath.split("-")[1].split("\\\\")[1];
        News news = new News();
        List<String> newsList = new ArrayList<>();
        /**
         * 计数器
         */
        int count = 0;

        //新闻文件工作铺
        Workbook newsWorkbook = FileUtil.getWorkbook(excelPath);
        if (null == newsWorkbook) {
            //工作铺创建创建失败
            return null;
        }
        //读取文件第二个工作表
        Sheet sheet = newsWorkbook.getSheetAt(1);
        //最后一行
        int lastRowIndex = sheet.getLastRowNum();

        //遍历处理行，从第二行开始（第一行为title）
        for (int rowIndex = 1; rowIndex <= lastRowIndex; rowIndex++) {
            //获得该行数据
            Row row = sheet.getRow(rowIndex);
            //获取处理方式
            int dealWay = dealWay(row, id);
            if (-1 == dealWay) {
                break;
            } else if (0 == dealWay) {
                continue;
            }

            //提取一行值
            news = extractRowValue(row);
            newsList.add(news.toString());

            count++;
            //只取前3000条
            if (count >= 3000) {
                break;
            }
        }
        return newsList;
    }
}
