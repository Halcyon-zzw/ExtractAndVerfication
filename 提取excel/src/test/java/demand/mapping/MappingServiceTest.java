package demand.mapping;

import config.ApplicationCache;
import config.ApplicationProperties;
import config.factory.ExtractFileFactory;
import demand.agent.GeneralAgent;
import demand.general.record.ExcelRowRecordExtract;
import demand.mapping.model.MappingResult;
import demand.mapping.model.RuleInfo;
import demand.mapping.model.RuleKeyword;
import demand.mapping.service.MappingService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import util.FileUtil;
import util.ListUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MappingServiceTest {
    ApplicationProperties aps = new ApplicationProperties();
    MappingController mappingController = new MappingController();
    MappingService mappingService = new MappingService();

    GeneralAgent generalAgent = new GeneralAgent();



    @Test
    public void testLoadKeyword() {
        String path = "E:\\文件\\工作\\AI\\bert\\direct\\result.tsv";
        List<RuleInfo> ruleInfoList = mappingService.loadRuleInfo(path);
        for (RuleInfo ruleInfo : ruleInfoList) {
            System.out.println(ruleInfo.getCategoryName());
            System.out.println(ruleInfo.getRuleKeywordList().get(0));
            System.out.println(ruleInfo.getRuleKeywordList().size());
            System.out.println();
        }

    }


    @Test
    public void testAllEventClassify() throws IOException {
        String path = "E:\\文件\\工作\\AI\\bert\\原始语料\\2019舆情稽核语料（7000）.xlsx";

        aps.getPrimaryProperties().setPath(path);
        //title     事件分类Id
        aps.getPrimaryProperties().setExtractIndexs(new int[]{3, 4});
        aps.getPrimaryProperties().setHaveHeader(false);
        aps.getPrimaryProperties().setCreateFile(false);

        ExcelRowRecordExtract excelRowRecordExtract = (ExcelRowRecordExtract) ExtractFileFactory.getRowRecordExtract("excel");
        List<String> testList = generalAgent.spliceExtract(aps, excelRowRecordExtract);

        List<Event> eventList = loadEventClassify();
        List<String> resultList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (String str : testList) {
            //title     事件分类Id
            String[] strArr = str.split("\t");

            //事件分类Id转name
            for (Event event : eventList) {
                //保证存在.0
                strArr[1] = Float.valueOf(strArr[1]).toString();

                if (strArr[1].equals(event.getEventId())) {
                    strArr[1] = event.getEventName();
                    break;
                }
            }
//            MappingResult mappingResult = mappingController.classifyByRule(strArr[0]);
            List<MappingResult> mappingResultList = mappingController.multiClassifyByRule(strArr[0], 5);

            if (! ListUtil.isEmpty(mappingResultList)) {
                StringBuffer stringBuffer = new StringBuffer("");
                stringBuffer.append(strArr[0] + "\t" + strArr[1]);
                for (MappingResult mappingResult : mappingResultList) {
                    //保存
                    stringBuffer.append("\t" + mappingResult.getCategoryName()
                            + "\t" + mappingResult.getIncludeRegex()
                            + "\t" + mappingResult.getExcludeKeyword()
                            + "\t" + mappingResult.getExcludeRegex() + "\t" + "");
                }
                resultList.add(stringBuffer.toString());
            }else {
                resultList.add(strArr[0] + "\t" + strArr[1] + "\t" + null);
            }

        }
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));

        String errorPath = "E:\\文件\\工作\\AI\\资讯分类\\情感分类\\错误分类\\规则匹配_7000.tsv";
        try {
            FileUtil.createFile(resultList, errorPath);
        } catch (IOException e) {
            e.printStackTrace();
            ApplicationCache.saveCache(resultList, errorPath);
        }
    }

    @Test
    public void testEventClassify() throws IOException {
//        String path = "E:\\文件\\工作\\AI\\bert\\原始语料\\舆情语料\\事件分类\\语料\\舆情事件分类语料提供-20180910\\102001-事件分类-资质风险.xlsx";
        String path = "E:\\文件\\工作\\AI\\bert\\原始语料\\2019舆情稽核语料（7000）.xlsx";

        aps.getPrimaryProperties().setPath(path);
        // type = 1     0：是否为对应的事件分类； 2：事件分类； 3：title
//        aps.getPrimaryProperties().setExtractIndexs(new int[]{0, 2, 3});
        // type = 2     3：categoryId    4:title     6：content
        aps.getPrimaryProperties().setExtractIndexs(new int[]{3, 4});
        aps.getPrimaryProperties().setHaveHeader(true);

//        aps.getPrimaryProperties().setHaveHeader(false);
        aps.getPrimaryProperties().setCreateFile(false);

        ExcelRowRecordExtract excelRowRecordExtract = (ExcelRowRecordExtract) ExtractFileFactory.getRowRecordExtract("excel");
//        excelRowRecordExtract.setSheetIndex(1);
        //type=1
//        excelRowRecordExtract.setSheetIndex(1);
        //type = 2
        excelRowRecordExtract.setSheetIndex(0);

        //Yes   事件分类    title
        List<String> testList = generalAgent.spliceExtract(aps, excelRowRecordExtract);
        List<String> errorList = new ArrayList<>();
        int correctCount = 0;
        long startTime = System.currentTimeMillis();
        for (String str : testList) {
            String[] strArr = str.split("\t");

            MappingResult mappingResult = mappingController.classifyByRule(strArr[0]);

            float aFloat;
            if (null != mappingResult) {
                aFloat = Float.valueOf(mappingResult.getCategoryId());
            }else {
                aFloat = 0;
            }

            float bFloat = Float.valueOf(strArr[1]);
            if (aFloat == bFloat) {
                correctCount++;
            }else {
                if (null != mappingResult) {
                    //保存
                    errorList.add(strArr[0] + "\t" + strArr[1] + "\t" + aFloat
                            + "\t" + mappingResult.getIncludeRegex()
                            + "\t" + mappingResult.getExcludeKeyword()
                            + "\t" + mappingResult.getExcludeRegex());
                }else {
                    errorList.add(strArr[0] + "\t" + strArr[1] + "\t" + null);
                }

            }

            /*type = 1
            String[] strArr = str.split("\t");
            String eventClassify = mappingService.emotionClassify(strArr[2]);
            String testRestle = null;
            if (strArr[1].equals(eventClassify)) {
                testRestle = "Yes";
            }else {
                testRestle = "No";
            }

            //比较结果
            if (strArr[0].equalsIgnoreCase(testRestle)) {
                correctCount++;
            }else {
                errorList.add(strArr[2] + "\t" + strArr[0]);
            }
            */
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));

        //计算准确率
        float correctRate = ((float)correctCount) / testList.size();
        System.out.println("正确个数：" + correctCount);
        System.out.println("正确率：" + correctRate);

        String errorPath = "E:\\文件\\工作\\AI\\资讯分类\\情感分类\\错误分类\\错误分类7000.tsv";

        try {
            FileUtil.createFile(errorList, errorPath);
        } catch (IOException e) {
            e.printStackTrace();
            ApplicationCache.saveCache(errorList, errorPath);
        }

    }

    @Test
    public void testCreateRulePattern() {
        String includeKeyword = "E:\\文件\\工作\\AI\\资讯分类\\情感分类\\错误分类\\error.txt";
        RuleKeyword ruleKeyword = new RuleKeyword();

//        mappingService.createRulePattern()
    }

    @Test
    public void testChange(){
        List<String[]> stringsList = new ArrayList<>();
//        stringsList.add(new String[1]);
        String[] strings = stringsList.toArray(new String[0]);
        System.out.println(strings);
    }

    private List<Event> loadEventClassify() {
        String path = "E:\\文件\\工作\\AI\\bert\\原始语料\\事件\\事件分类（二级）类别.xlsx";
        ApplicationProperties aps = new ApplicationProperties();
        aps.getPrimaryProperties().setPath(path);
        aps.getPrimaryProperties().setExtractIndexs(new int[]{0, 1});
        aps.getPrimaryProperties().setHaveHeader(true);
        aps.getPrimaryProperties().setCreateFile(false);

        ExcelRowRecordExtract excelRowRecordExtract = (ExcelRowRecordExtract) ExtractFileFactory.getRowRecordExtract("excel");
        List<String> eventList = generalAgent.spliceExtract(aps, excelRowRecordExtract);
        return eventList.stream()
                .map(event -> {
                    String[] eventArr = event.split("\t");
                    //转Float再转String，保证后面存在.0
                    String eventId = Float.valueOf(eventArr[0]).toString();
                    return new Event(eventId, eventArr[1]);
                }).filter(event -> !StringUtils.isAnyEmpty(event.getEventId(), event.getEventName()))
                .collect(Collectors.toList());
    }
}