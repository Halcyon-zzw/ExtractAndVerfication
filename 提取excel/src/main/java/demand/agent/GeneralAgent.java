package demand.agent;

import config.ApplicationProperties;
import create.CreateFileWay;
import create.impl.CreateFileDirect;
import deal.DealFileWay;
import demand.general.process.child.EmotionAndGradeLabel7ChinaProcess;
import demand.general.process.ArticleProcess;
import demand.general.GeneralArticleDeal;
import demand.general.GeneralSpliceDeal;
import demand.general.process.process_way.InvalidProcess;
import demand.general.record.RowRecordExtract;
import lombok.Setter;
import pool.DealFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/18 9:32
 * @Version: 1.0
 */
public class GeneralAgent {
    @Setter
    private ApplicationProperties aps;

    private DealFileWay dealFileWay;

    RowRecordExtract rowRecordExtract;
    @Setter
    private ArticleProcess articleProcess;


    public GeneralAgent() {

    }

    public GeneralAgent(ArticleProcess articleProcess, ApplicationProperties aps) {
        this.aps = aps;
        this.articleProcess = articleProcess;
    }

    /**
     * 拼接提取
     * @param aps 使用到的属性，path 及GeneralSplice中的属性
     * @param rowRecordExtract 处理的文件类型
     * @return
     * @throws IOException
     */
    public List<String> spliceExtract(ApplicationProperties aps, RowRecordExtract rowRecordExtract) {
        this.aps = aps;
        String path = aps.getPrimaryProperties().getPath();
        aps.getPrimaryProperties().info();
        //TODO change
        DealFileWay dealFileWay = new GeneralSpliceDeal(rowRecordExtract, aps);
        DealFile dealFile = new DealFile(dealFileWay);
        List<String> resutlList = dealFile.dealFile(path);

        //去重
        if (aps.getPrimaryProperties().isRemoveRepeat()) {
            List<String> removeRepeatList = new ArrayList<>();

            for (String str : resutlList) {
                if (!removeRepeatList.contains(str)) {
                    removeRepeatList.add(str);
                }
            }
            System.out.println("重复数据：" + (resutlList.size() - removeRepeatList.size()));
            resutlList = removeRepeatList;
        }

        if (aps.getPrimaryProperties().isCreateFile()) {
            createFile(resutlList);
        }

        return resutlList;
    }


    /**
     * 提取文章
     * @param aps 属性
     * @param rowRecordExtract 处理的文件类型
     * @return
     * @throws IOException
     */
    public List<String> articleExtract(ApplicationProperties aps, RowRecordExtract rowRecordExtract) {
        this.aps = aps;
        String path = aps.getPrimaryProperties().getPath();
        aps.getPrimaryProperties().info();
        //TODO change
        ArticleProcess articleProcess = new EmotionAndGradeLabel7ChinaProcess(new InvalidProcess());
        DealFileWay dealFileWay = new GeneralArticleDeal(rowRecordExtract, articleProcess, aps);
        DealFile dealFile = new DealFile(dealFileWay);
        List<String> resutlList = dealFile.dealFile(path);

        //去重
//        List<String> tempResultList = new ArrayList<>();
//
//        for (String str : resutlList) {
//            if (!tempResultList.contains(str)) {
//                tempResultList.add(str);
//            }
//        }
//
//        System.out.println("重复数据：" + (resutlList.size() - tempResultList.size()));
//        resutlList = tempResultList;

        createFile(resutlList);
        return resutlList;
    }


    private void createFile(List<String> resutlList)  {
        System.out.println("输出文件路径：" + aps.getCreateFile().getDirectPath());
        CreateFileWay createFileWay = new CreateFileDirect(aps.getCreateFile().getDirectPath());
        try {
            createFileWay.createFile(resutlList);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("生成文件失败");
        }
    }
}
