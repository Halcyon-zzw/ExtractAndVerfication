package demand.mapping.service;

import config.ApplicationProperties;
import config.factory.ExtractFileFactory;
import demand.agent.GeneralAgent;
import demand.general.record.RowRecordExtract;
import util.FileUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/28 10:47
 * @Version: 1.0
 */
public class LoadService {
    private GeneralAgent generalAgent = new GeneralAgent();

    /**
     * 加载原始关键词
     *
     * @param path
     * @return
     */
    public List<String> loadKeyword(String path) {
        return FileUtil.readAll(path);
    }

    /**
     * 加载所有文本
     * @return
     */
    public List<String> loadText(String path) {
        return FileUtil.readAll(path);
    }

    public List<String> loadArticles(String path) {
        String fileSuffix = FileUtil.getFileSuffix(path);
        if (fileSuffix.equalsIgnoreCase("txt")) {
            return FileUtil.readAll(path);
        }
        RowRecordExtract rowRecordExtract = ExtractFileFactory.getRowRecordExtract(fileSuffix);
        ApplicationProperties aps = setAps_7000(path);

        List<String> articleList = generalAgent.spliceExtract(aps, rowRecordExtract);
        return articleList;
    }

    private ApplicationProperties setAps(String path) {
        ApplicationProperties aps = new ApplicationProperties();
        aps.getPrimaryProperties().setPath(path);
        aps.getPrimaryProperties().setExtractIndexs(new int[]{0});
        aps.getPrimaryProperties().setOptionalExtractIndexs(new int[]{1, 2});
        aps.getPrimaryProperties().setHaveHeader(false);
        aps.getPrimaryProperties().setCreateFile(false);
        return aps;
    }

    private ApplicationProperties setAps_7000(String path) {
        ApplicationProperties aps = new ApplicationProperties();
        aps.getPrimaryProperties().setPath(path);
        //标题    内容
//        aps.getPrimaryProperties().setExtractIndexs(new int[]{3, 6});
        //标题    分类Id
        aps.getPrimaryProperties().setExtractIndexs(new int[]{3, 4});
        aps.getPrimaryProperties().setHaveHeader(false);
        aps.getPrimaryProperties().setCreateFile(false);
        return aps;
    }


    /**
     * 加载事件分类的Id & name
     *
     * @return
     */
    public Map<String, String> loadEventClassify() {
        String path = "E:\\文件\\工作\\AI\\bert\\原始语料\\事件\\事件分类（二级）类别.xlsx";
        return loadEventClassify(path);
    }

    public Map<String, String> loadEventClassify(String path) {
        ApplicationProperties aps = new ApplicationProperties();
        aps.getPrimaryProperties().setPath(path);
        aps.getPrimaryProperties().setExtractIndexs(new int[]{0, 1});
        aps.getPrimaryProperties().setHaveHeader(true);
        aps.getPrimaryProperties().setCreateFile(false);

        String fileSuffix = FileUtil.getFileSuffix(path);
        RowRecordExtract rowRecordExtract = ExtractFileFactory.getRowRecordExtract(fileSuffix);
        List<String> eventList = generalAgent.spliceExtract(aps, rowRecordExtract);

        Map<String, String> eventMap = new HashMap<>();
        //list 转map
        for (String event : eventList) {
            String[] eventArr = event.split("\t");
            //转Float再转String，保证后面存在.0
            String eventId = Float.valueOf(eventArr[0]).toString();
            eventMap.put(eventId, eventArr[1]);
        }
        return eventMap;
    }

}
