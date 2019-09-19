package deal.impl;

import deal.DealFileWay;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 从excel（樊姐提供的列表文件）中提取所有的事件分类列表
 * 数据格式：102001-事件分类-资质风险
 *
 * @Author: zhuzw
 * @Date: 2019/8/19 10:12
 * @Version: 1.0
 */
public class AllClassifyExcelDeal implements DealFileWay {

    @Override
    public List<String> extractedValue(String path) throws IOException {
        Workbook workbook = FileUtil.getWorkbook(path);
        Sheet sheet = workbook.getSheetAt(0);

        List<String> resultList = new ArrayList<>();

        //从行1开始，0为title
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);

            if(! isDeal(row)) {
                //不处理
                continue;
            }

            resultList.add(extractedRowValue(row));
        }


        return resultList;
    }

    /**
     * 提取一行数据
     *
     * @param row
     * @return
     */
    private String extractedRowValue(Row row) {
        //获取结果为xxxx.0形式，剔除 .0
        String classifyString = row.getCell(0).toString().split("\\.")[0];

        return classifyString + "-事件分类-" + row.getCell(1).toString();
    }

    /**
     * 是否处理行
     * 返回true位处理，false为不处理
     *
     * @param row 行数据 Row对象
     * @return 列0数据位null或等于""（无数据）返回false，否则返回true
     */
    private boolean isDeal(Row row) {
        String classifyString = row.getCell(0).toString();

        if (null == classifyString || "".equals(classifyString)) {
            return false;
        }
        return true;
    }
}
