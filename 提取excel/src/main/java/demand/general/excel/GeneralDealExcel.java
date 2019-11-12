package demand.general.excel;

import config.ApplicationProperties;
import deal.DealFileWay;
import demand.general.CountControl;
import demand.general.ArticleProcess;
import demand.general.process.InvalidProcess;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import util.FileUtil;
import util.StringsUtilCustomize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/9/20 9:34
 * @Version: 1.0
 */
public class GeneralDealExcel implements DealFileWay {
    private final ApplicationProperties aps = new ApplicationProperties();

    /**
     * 数量控制
     */
    private CountControl countControl = new CountControl();

    /**
     * 行数据处理 - 默认删除无效数据
     */
    @Setter
    private ArticleProcess articleProcess = new ArticleProcess(new InvalidProcess());

    private int labelHeader;
    private int titleHeader;
    private int contentHeader;

    public GeneralDealExcel(int labelHeader, int titleHeader, int contentHeader) {
        this.labelHeader = labelHeader;
        this.titleHeader = titleHeader;
        this.contentHeader = contentHeader;
    }

    @Override
    public List<String> extractedValue(String path) {
        List<String> resultList = new ArrayList<>();
        HashMap<String, List<String>> tempResultMap = new HashMap<>();


        //读取文件第二个工作表
        Sheet sheet = FileUtil.getSheet(path, 1);
        //最后一行
        int lastRowIndex = sheet.getLastRowNum();


        int firstRowIndex = 0;
        if (haveHeader()) {
            firstRowIndex = 1;
        }

        //遍历处理行，从第二行开始（第一行为title）
        for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
            //获得该行数据
            Row row = sheet.getRow(rowIndex);
            if (null == row) {
                continue;
            }

            //从文件中获取值
            Cell labelCell = row.getCell(labelHeader);
            Cell titleCell = row.getCell(titleHeader);
            Cell countCell = row.getCell(contentHeader);
            String label = labelCell == null ? null : labelCell.toString().split("\\.")[0];
            String title = titleCell == null ? null : titleCell.toString();
            String content = countCell == null ? null : countCell.toString();


            //是否为空
            if (StringsUtilCustomize.isEmpty(label, title, content)) {
                continue;
            }

            //获取处理方式
            //数量控制
            int operationStatus = countControl.operationStatus(label, aps.getPrimaryProperties().getDataCount(), aps.getPrimaryProperties().getLabelNumber());
            if (-1 == operationStatus) {
                break;
            } else if (0 == operationStatus) {
                continue;
            }

            //提取一行数据
            String rowValue = articleProcess.extractRowValue(new String[]{label}, title, content);

            if (null == tempResultMap.get(label)) {
                tempResultMap.put(label, new ArrayList<String>());
            }
            tempResultMap.get(label).add(rowValue);

        }
        System.out.println("处理完毕!");
        //提取情况,输出提取数据的情况。label: number
        List<String> extractSituation = countControl.getExtract();
        System.out.println("----------数据量情况------------");
        extractSituation.forEach(System.out::println);
        System.out.println("----------过滤数据量少的情况------------");
        tempResultMap = countControl.filterLess(tempResultMap, 400);
        countControl.getExtract();
        extractSituation.forEach(System.out::println);

        //TODO 改用流操作
        for (Map.Entry<String, List<String>> temp : tempResultMap.entrySet()) {
            resultList.addAll(temp.getValue());
        }

        System.out.println("----数据量：" + resultList.size());
        return resultList;
    }

    private boolean haveHeader() {
        return aps.getPrimaryProperties().isHaveHeader();
    }
}
