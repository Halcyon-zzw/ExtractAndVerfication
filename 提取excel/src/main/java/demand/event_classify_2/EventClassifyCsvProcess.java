package demand.event_classify_2;

import com.csvreader.CsvReader;
import deal.DealFileWay;
import deal.impl.AllClassifyExcelDeal;
import org.apache.commons.lang3.StringUtils;
import process.ColumnProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * csv文件中二级事件分类的判断处理
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 9:19
 * @Version: 1.0
 */
public class EventClassifyCsvProcess implements ColumnProcess {
    /**
     * 事件分类列表
     */
    private List<String> eventClassifyList;

    public EventClassifyCsvProcess() {
        //从文件中读出事件分类列表
        this.eventClassifyList = getClassify();
    }

    @Override
    public boolean isProcess(Object object) {
        CsvReader csvReader = (CsvReader)object;


        try {

            String eventClassify = csvReader.get("事件二级分类");
            //剔除 事件二级分类 为空情况
            if (StringUtils.isEmpty(eventClassify)) {
                return false;
            }

            //只处理classifyList中的分类，其他分类数据量少
            int i = 0;
            for (i = 0; i < eventClassifyList.size(); i++) {
                if (eventClassifyList.get(i).contains(eventClassify)) {
                    return true;
                }
            }
            if (i == eventClassifyList.size()) {
                return false;
            }
        }catch (IOException ioe) {
            System.out.println("读取二级事件分类失败！");
            System.out.println(ioe.getMessage());
        }

        return true;
    }


    /**
     * 获取事件分类列表
     * @return
     * @throws IOException
     */
    private List<String> getClassify() {
        /**
         * 事件分类文件路径
         */
        String classifyExcelPath = "E:\\下载\\钉钉文件\\工作资料\\create\\事件分类（二级）.xlsx";
        List<String> eventClassifyList = new ArrayList<>();
        DealFileWay allClassifyExcelDeal = new AllClassifyExcelDeal();
        try {
            eventClassifyList = allClassifyExcelDeal.extractedValue(classifyExcelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventClassifyList;

    }
}
