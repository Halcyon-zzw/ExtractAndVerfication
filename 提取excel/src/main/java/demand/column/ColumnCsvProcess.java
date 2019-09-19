package demand.column;

import com.csvreader.CsvReader;
import config.ApplicationProperties;
import org.apache.commons.lang3.StringUtils;
import process.ColumnProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理csv文件中栏目分类相关数据列
 *
 * @Author: zhuzw
 * @Date: 2019/8/29 8:38
 * @Version: 1.0
 * @modify： 数据量控制改进
 * - 通过hashMap<String, List<String>>控制
 * - 每次放进hashMap中，控制放入的最大值
 * - 添加返回hashMap方法，少于一定数量的删除
 */
public class ColumnCsvProcess implements ColumnProcess {
    private ApplicationProperties aps = new ApplicationProperties();

    /**
     * 取的舆情情感分类数据量
     */
    private final int COLUMN_COUNT = aps.getColumnProperties().getDataCount();

    /**
     * 最少的数据量
     */
    private final int LEAST_DATA_COUNT = aps.getColumnProperties().getLeastDataCount();


    private final int COLUMN_INDEX = (int) aps.getColumnProperties().getLabel();

    private HashMap<String, List<String>> columnHashMap = new HashMap<>();

    /**
     * 1-27 分类，0 不用
     */
    private int[] columnNum = new int[28];

    /**
     * 数据量少(少于200)的分类，待剔除
     */
    private int[] lessClassify = {
            12, 17, 21, 24, 25
    };

    @Override
    public boolean isProcess(Object object) {
        CsvReader csvReader = (CsvReader) object;

        try {
//            String column = csvReader.get("栏目");
            //没有标题，使用下标获取栏目分类
            String column = csvReader.get((int) aps.getColumnProperties().getLabel());

            //剔除emotion为空情况
            if (StringUtils.isEmpty(column)) {
                return false;
            }

            //TODO 实现剔除1-27以外的数据
//            char[] chars = csvReader.get("舆情情感").toCharArray();
//            if (chars[0] < '1' || chars[0] > '') {
//                return false;
//            }


            int columnIndex = Integer.parseInt(column);

            //剔除数量少的分类
            for (int classify : lessClassify) {
                if (classify == columnIndex) {
                    return false;
                }
            }

            if (99 == columnIndex) {
                columnIndex = 27;
            }

            //判断数据量
            if (columnNum[columnIndex] >= COLUMN_COUNT) {
                return false;
            }

            //统计数量
            columnNum[columnIndex]++;

        } catch (IOException ioe) {
            System.out.println("读取栏目数据失败！");
            System.out.println(ioe.getMessage());
        }

        return true;
    }

    /**
     * 获取值
     *
     * @return
     */
    public List<String> getValues() {
        List<String> values = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : columnHashMap.entrySet()) {
            if (entry.getValue().size() > LEAST_DATA_COUNT) {
                values.addAll(entry.getValue());
            }
        }
        return values;
    }

}
