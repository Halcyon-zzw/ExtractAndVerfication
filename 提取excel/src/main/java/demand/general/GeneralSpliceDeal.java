package demand.general;

import config.ApplicationProperties;
import deal.DealFileWay;
import demand.general.record.RowRecordExtract;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 通用数据处理,直接拼接需要提取的数据
 *
 * @Author: zhuzw
 * @Date: 2019/11/17 12:16
 * @Version 1.0
 */
public class GeneralSpliceDeal implements DealFileWay {

    /**
     * 变量属性
     * 使用到的属性
     *      extractIndexs   提取Index集合
     *      optionalExtractIndexs 可选的index集合
     *      haveHeader:  是否有标题\
     *      createFile: 是否生成文件
     *      removeRepeat: 是否去重
     *
     * createFile:
     *      direc path
     */
    @Setter
    private ApplicationProperties aps;

    /**
     * 处理的数量
     */
    private int remindCount = 0;

    private RowRecordExtract rowRecordExtract;

    private int[] extractIndexs;

    private int[] optionalExtractIndexs;



    public GeneralSpliceDeal(RowRecordExtract rowRecordExtract, ApplicationProperties aps) {
        this.rowRecordExtract = rowRecordExtract;
        this.aps = aps;
        init();
    }

    private void init() {
        this.extractIndexs = aps.getPrimaryProperties().getExtractIndexs();
        this.optionalExtractIndexs = aps.getPrimaryProperties().getOptionalExtractIndexs();
    }


    /**
     * 从文件中提取数据
     *
     * @param path 文件路径
     * @return 舆情情感\t标题\t内容
     */
    @Override
    public List<String> extractedValue(String path) {
        //rowRecordExtract初始化
        rowRecordExtract.init(path);

        List<String> resultList = new ArrayList<>();
        HashMap<String, List<String>> tempResultMap = new HashMap<>();
        //待处理数据
        List<String[]> rawRecordList = new ArrayList<>();
        boolean countEnough = false;
        int i = 0;
        //第一次遍历
        boolean firstTime = true;
        System.out.println("开始处理...");

        while (true && rawRecordList != null) {
            if (rawRecordList.size() == i) {
                //获取新的数据列表，i初始化
                try {
                    rawRecordList = rowRecordExtract.getRawRecords();
                } catch (IOException e) {
                    System.out.println("获取数据出错！");
                    e.printStackTrace();
                }
                i = 0;
            }
            if (null == rawRecordList) {
                continue;
            }
            //剔除标题
            if (firstTime) {
                //剔除标题
                excludeHeader(rawRecordList, aps.getPrimaryProperties().isHaveHeader());
                firstTime = false;
                if (rawRecordList.size() == 0) {
                    //剔除后为长度为0
                    System.out.println(path + " 该文件剔除标题后没有内容...");
                    continue;
                }
            }

            String[] rawRecord = rawRecordList.get(i++);


            if (rawRecord.length <= extractIndexs[extractIndexs.length - 1]) {
                //如果原始数据长度小于等于必须提取的最大index，则该行不提取
                //存在空行数据，但部分Cell不为null
                continue;
            }
            //提取的一行数据
            String[] extractRecode = extractFromRaw(rawRecord, extractIndexs, optionalExtractIndexs);

            //判断该行是否提取(所有属性不为null)
            if (!isDealRow(extractRecode)) {
                continue;
            }
            StringBuffer rowValue = new StringBuffer("");
            //数组中的数据提成一行数据
            for (int i1 = 0; i1 < extractRecode.length; i1++) {
                rowValue.append(extractRecode[i1]);
                if (i1 != extractRecode.length - 1) {
                    rowValue.append("\t");
                }
            }
            resultList.add(rowValue.toString());

            if (aps.getPrimaryProperties().getDataCount() != -1 && resultList.size() >= aps.getPrimaryProperties().getDataCount()) {
                return resultList;
            }
            //步数提醒
//            remind(tempResultMap, 5000);
        }

       return resultList;
    }

    private String[] extractFromRaw(String[] rawRecord, int[] extractIndexs, int[] optionalExtractIndexs) {
        String[] resutltArr = null;

        //根据extractIndexs和optionalExtradctIndexs初始化数组的大小
        if (null == optionalExtractIndexs) {
            resutltArr = new String[extractIndexs.length];
        }else {
            resutltArr = new String[extractIndexs.length + optionalExtractIndexs.length];
        }
        //提取必提数据
        int resultIndex = 0;
        for (int index = 0; index < extractIndexs.length; index++) {
            String value = rawRecord[extractIndexs[index]];
            resutltArr[resultIndex++] = formatDeal(value);
        }
        //可选列不存在，直接返回
        if (null == optionalExtractIndexs) {
            return resutltArr;
        }
        //提取文章中存在可选列 且 不为null
//        if (rawRecord.length > optionalExtractIndexs[optionalExtractIndexs.length - 1]
//                && !StringUtils.isEmpty(rawRecord[optionalExtractIndexs.length - 1])) {
        //提取可选列
        for (int index = 0; index < optionalExtractIndexs.length; index++) {
            String value = rawRecord[optionalExtractIndexs[index]];
            if (null == value) {
                resutltArr[resultIndex++] = "";
            }else {
                resutltArr[resultIndex++] = formatDeal(value);
            }
        }
//        }
        return resutltArr;
    }

    /**
     * 格式处理
     * @param value
     * @return
     */
    private String formatDeal(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return value
                .replaceAll("\b", "")
                .replaceAll("\f", "")
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .replaceAll("\t", "");
    }

    /**
     * 判断是否处理该行，
     *      通过判断所有属性是否都不为null
     * TODO 是否添加属性控制某些提取数据允许为null
     * @param arr 检测的数组
     * @return
     */
    private boolean isDealRow(String[] arr) {
        for (int index = 0; index < arr.length; index++) {
            if (index >= extractIndexs.length) {
                //超过extractIndex长度为可选提取列，不需要判断是否为null
                continue;
            }

            if (StringUtils.isEmpty(arr[index])) {
                System.out.println("第" + index + "个提取的参数为null!");
                return false;
            }
        }
        return true;
    }


    /**
     * 处理提醒
     *
     * @param resultList
     * @param count      提醒步长
     */
    private void remind(HashMap<String, List<String>> resultList, int count) {
        if (0 == resultList.size() % count) {
            System.out.println("已处理：" + (remindCount += count));
        }
    }
}
