package com.zhuzw.practical.mergeexcel;

import com.zhuzw.practical.mergeexcel.model.DataInformation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * 主题重复值的解决
 */

/**
 * ====================需求1.0==========================
 * 不能简单的用行号来处理，因为每个人用的文件行号可能会不同，
 * <p>
 * ==》改进：改为自动判断,读取文件行数据的形式----------->之前的判定方法都需要更改
 * <p>
 * <p>
 * 判断行是否需要处理：
 * 0 - 2 行处理不需要改变
 * 直接用cellIndex = 1 是否有数据来判断
 * <p>
 * <p>
 * 判断需要存入的行：
 * 读取到“当日工作情况”，相应的处理..........
 * 读取到“明日工作计划”，相应的处理..........
 * 读取到“存在的问题风险”，相应的处理........
 * ----------是否能用状态模式设计上述三个状态
 * <p>
 * ================完成：2018/12/26/13：25/====================
 * <p>
 * ====================需求2.0==========================
 * --提出时间：2018/12/26/上午
 * 1、复制文件，并重命名
 * 要求：
 * 提供原数据文件
 * 没有从服务器中下载
 * -->待完成
 * 提供目标地址
 * 没有需要生成
 * <p>
 * 2、判断文件夹下文件的数量
 * -->complete at 2018/12/26/19:46
 * <p>
 * 3、自动读取文件夹下所有文件
 * -->complete at 2018/12/26/19:46
 * <p>
 * -----------------------------------------------------
 * 开始：2018/12/26
 * ---------------发现问题----------------
 * 1、当日进展概况出现空行、2.1非法数据
 * 解决方案：在该位置写数据前先清空
 * --->已解决
 * <p>
 * 2、数据传输放入model中
 * -->待解决
 * <p>
 * <p>
 * modity：最后修改时间：2018/12/26
 *
 * @author zhuzw
 * @version 1.2
 */
public class GetExcel {

    private

    static List<DataInformation> dataList = new ArrayList<DataInformation>();
    /**
     * 表头进展-行号
     */
    private static int progressRowIndex = ExcelProperties.PROGRESS_ROW_INDEX_INIT;
    /**
     * 表头问题-行号
     */
    private static int problemRowIndex = ExcelProperties.PROBLEM_ROW_INDEX_INIT;
    /**
     * 为防止excel溢出，此处需要修改，增加今日工作的记录行。
     */
    /**
     * 今日工作-起始行号
     */
    private static int todayRowIndex = ExcelProperties.TODAY_ROW_INDEX_INIT;
    /**
     * 明日计划-起始行号
     */
    private static int tomorrowRowIndex = ExcelProperties.TOMORROW_ROW_INDEX_INIT;
    /**
     * 存在的风险-起始行号
     */
    private static int riskRowIndex = ExcelProperties.RISK_ROW_INDEX_INIT;
    private static boolean todayFlag = false;
    private static boolean tomorrowFlag = false;
    private static boolean riskFlag = false;


    public static void main(String[] args) {

        //待汇总文件路径
        String summariedPath = ExcelProperties.summariedPath;
        //模板文件路径
        String templatePath = ExcelProperties.templatePath;
        //汇总文件路径
        String poolExcelPath = ExcelProperties.poolExcelPath;

        //文件初始化-复制模板文件
        fileInit(templatePath, poolExcelPath);
        //统计文件夹下的文件数量
        int fileCount = countFile(summariedPath);
        //处理文件夹下的文件
//        openAllFile(prefixPath,  isCollect(fileCount, STAFF_COUNT));
        dealAllFile(summariedPath,  isCollect(fileCount, ExcelProperties.STAFF_COUNT) || true);

        //存入汇总文件
        writeData(dataList, poolExcelPath);
    }

    /**
     * 处理文件夹下所有文件
     *
     *
     * @param prefixPath 文件夹路径
     */
    public static void dealAllFile(String prefixPath, boolean isEqual) {
        /**
         * 请求确定,是否继续操作
         */
        if (!isEqual) {
            //请求拒接
            return;
        }

        File prefixFile = new File(prefixPath);

        if (!prefixFile.exists()) {
            //目录不存在,创建目录
            prefixFile.mkdir();
        }
        //批量处理
        File[] files = prefixFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String excelName = files[i].getName();
                String excelPath = prefixPath + excelName;
                dealExcel(excelPath);
            }
        }
    }

    /**
     * 文件初始化，复制模板日报文件到指定路径文件
     * 文件夹不存在需要生成目录文件夹
     *
     * @param templatePath 模板文件
     *                     文件不存在从服务器中下载
     * @param targetPath   目标文件
     *                     文件名传入前需要先通过今日情况中起始时间的字段做处理
     *                     文件路径不存在需要创建文件夹
     */
    public static void fileInit(String templatePath, String targetPath) {

        //复制文件
        File sourceFile = new File(templatePath);
        File targetFile = new File(targetPath);

        //判断源文件是否存在
        if (!sourceFile.exists()) {
            //提示下载源文件
        }
        //判断目标文件夹是否存在
        if (!targetFile.getParentFile().exists()) {
            //创建文件夹
            targetFile.getParentFile().mkdir();
        }

        try {
            //判断目标文件是否存在
            if (targetFile.exists()) {
                //存在，删除
                /**
                 * delete和deleteOnExit()的区别
                 * delete()：直接删除
                 * deleteOnExit()：文件没有直接删除，等到程序结束的时候删除
                 */
                targetFile.delete();
            }

            //复制,targeFile文件中不能存在，否则会报已存在异常；但路径必须存在
            Files.copy(sourceFile.toPath(), targetFile.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计文件夹目录下文件的数量
     *
     * @param prefixPath 文件夹目录（前缀）
     * @return 数量相同返回true，不相同返回false
     */
    public static int countFile(String prefixPath) {
        int fileCount = 0;
        File prefixFile = new File(prefixPath);
        if (! prefixFile.exists()) {
            //目录不存在，生成文件夹
            prefixFile.mkdir();
        }
        File[] files = prefixFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //统计文件的数量
                fileCount++;
            }
        }
        return fileCount;
    }

    /**
     * 判断目录下文件的数量是否和小组成员数相同
     *
     * @param fileCount 文件数量
     * @param staffCount 成员数量
     * @return 相同返回true，不相同返回false
     */
    public static boolean isCollect(int fileCount, int staffCount) {
        return fileCount == staffCount ? true : false;
    }

    /**
     * 通过excel文件路径得到对应的Workbook对象
     *
     * @param excelPath File文件对象路径
     * @return Wodkbook对象
     */
    public static Workbook getWorkbook(String excelPath) {
        File excelName = new File(excelPath);
        Workbook wb = null;
        if (excelName.isFile() && excelName.exists()) {   //判断文件是否存在
            try {
                //.是特殊字符，需要转义！！！！！
                String[] split = excelName.getName().split("\\.");

                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    wb = new HSSFWorkbook();
                } else if ("xlsx".equals(split[1])) {
                    //执行
                    //文件流对象
                    FileInputStream fis = new FileInputStream(excelName);
                    wb = new XSSFWorkbook(fis);
                } else {
                    System.out.println("文件类型错误!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("找不到指定的文件");
        }
        return wb;
    }

    /**
     * 通过列 1 判断行是否有数据
     *
     * @param row 列信息
     * @return 没有数据返回ture, 有数据返回false
     */
    public static boolean isNoData(Row row) {
        Cell cell = null;

        //1、2行需要用cell = 2判断
        cell = row.getCell(2);
        if ("".equals(cell.toString())) {
            return true;
        }

        //其他行用cell判断
        cell = row.getCell(1);
        if ("".equals(cell.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 判断该行是否需要处理
     *
     * @param row      需要判断的row对象
     * @param rowIndex 行号
     * @return 需要处理返回true，不需要返回false
     */
    public static boolean isDeal(Row row, int rowIndex) {
        Cell cell = null;

        boolean isHead = 1 == rowIndex || 2 == rowIndex ? true : false;

        //判断是否为表头
        if (isHead) {
            //该行为表头部分

            //通过cell = 2判断是否有数据
            cell = row.getCell(2);
            if (!"".equals(cell.toString())) {
                //cell = 2 不为空
                return true;
            }
        } else {
            //剔除表的标签

            cell = row.getCell(0);

            //标签信息
            List<String> labelList = new ArrayList<String>();
            labelList.add(" 当日工作情况");
            labelList.add("明日工作计划");
            labelList.add("存在的问题风险");
            labelList.add("序号");

            if (" 当日工作情况".equals(cell.toString())) {
                todayFlag = true;
            } else if ("明日工作计划".equals(cell.toString())) {
                tomorrowFlag = true;
            } else if ("存在的问题风险".equals(cell.toString())) {
                riskFlag = true;
            }

            if (!labelList.contains(cell.toString())) {
                //不存在上述词，需要处理
                cell = row.getCell(1);
                //该行是否有数据，通过cell = 1 的数据判断
                if (!"".equals(cell.toString())) {
                    //不为空
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 得到（判断）数据存放的行号
     * 通过区域标签控制器判断数据存入的区域
     *
     * @param rowIndex 需要判断的行，只有是表头数据时才需要判断行
     * @return 数据存放的行号
     */
    public static int storageRowIndex(int rowIndex) {
        /**
         * 对应的列:
         *      表头，1-2：只需要处理2
         *      今日工作，5-23：1-9（2 除外）
         *      明日计划，25-31：1-9（2 除外）
         *      存在的风险，39-38：1-10（除2、7、8、9）
         */
        if (1 == rowIndex) {
            return progressRowIndex;
        }
        if (2 == rowIndex) {
            return problemRowIndex;
        }
        if (riskFlag) {
            return riskRowIndex++;
        }
        if (tomorrowFlag) {
            return tomorrowRowIndex++;
        }
        if (todayFlag) {
            return todayRowIndex++;
        }
        return 0;
    }

    /**
     * 处理数据
     * <p>
     * ==为解决重复打开FileOutputStream输出流的操作，
     * ==处理待合并文件时返回对象(需要存入的rowIndex,cellIndex， data)数组，
     * ==
     *
     * @param excelPath 文件路径
     */
    public static List<DataInformation> dealExcel(String excelPath) {

        DataInformation dataInformation = null;

        //个人日报文件工作铺
        Workbook personalWB = getWorkbook(excelPath);

        //-----------开始解析-------------
        //读取个人日报第一个工作表
        Sheet sheet = personalWB.getSheetAt(0);

        //第一行&最后一行
        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();

        //初始化flag
        flagInit();
        //遍历处理行
        for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
            //获得该行数据
            Row row = sheet.getRow(rowIndex);

            //判断行是否需要处理
            if (!isDeal(row, rowIndex)) {
                continue;
            }

            if (row != null) {
                //得到列号,第一列&最后一列
                int firstCellIndex = row.getFirstCellNum();
                int lastCellIndex = row.getLastCellNum();

//                System.out.println("rowIndex: " + rowIndex);      //输出行

                //得到写入文件的行号,一行得到一次，不用每次(列)判断得到
                int tempRow = storageRowIndex(rowIndex);
                //遍历列
                for (int cellIndex = firstCellIndex; cellIndex < lastCellIndex; cellIndex++) {
//                    System.out.println("列：" + cellIndex);     //输出列
                    Cell cell = row.getCell(cellIndex);
                    if (cell != null) {
                        //写入文件
                        dataInformation = new DataInformation();
                        dataInformation.setRowIndex(tempRow);
                        dataInformation.setCellIndex(cellIndex);
                        dataInformation.setData(cell.toString());
                        dataList.add(dataInformation);
                    }
                }
            }
        }
        return dataList;
    }

    /**
     * 标签初始化
     */
    public static void flagInit() {
        todayFlag = false;
        tomorrowFlag = false;
        riskFlag = false;
    }

    /**
     * 向excel中写入数据
     *
     */
    public static void writeData(List<DataInformation> dataInformationList, String path) {
        /**
         * 记录当日进展概况是否清空
         */
        boolean isClear = false;

        //汇总工作铺
        Workbook writeWB = getWorkbook(path);
        Sheet sheet = writeWB.getSheetAt(0);

        Row row = null;
        Cell cell = null;
        /**
         * TODO：修改处理List，处理好后直接遍历set进cell就行
         */
        for (int index = 0; index < dataInformationList.size(); index++) {

            //获得数据信息
            DataInformation dataInformation = dataInformationList.get(index);
            int rowIndex = dataInformation.getRowIndex();
            int cellIndex = dataInformation.getCellIndex();
            String processData = dataInformation.getData();

            row = sheet.getRow(rowIndex);
            cell = row.getCell(cellIndex);

            //是否为表头数据
            boolean isHead = (1 == rowIndex || 2 == rowIndex) ? true : false;
            boolean isHeadData = isHead && 2 == cellIndex ? true : false;

            if (isHeadData) {
                //为表头数据，需要拼接数据,
                if (!isClear) {
                    //没清空，清空
                    cell.setCellValue("");
                    isClear = true;
                }
                //处理表头数据
                processData = dealHeadData(cell.toString(), processData);
            }
            cell.setCellValue((processData));

            //设置序号
            if (0 == cellIndex && !isHead) {
                cell.setCellValue(getSerialNumber(rowIndex));
            }

            //处理完成度--空值处理
            if (7 == cellIndex && !isHead) {
                cell.setCellValue(dealDegreeOfCompletion(processData));
            }
        }


        //通过path设置主标题
        row = sheet.getRow(0);
        cell = row.getCell(0);
        cell.setCellValue(setTopic(path));

        //进展汇总去重
        row = sheet.getRow(1);
        cell = row.getCell(2);
        String tempData = removeRepeatData(cell.toString());
        cell.setCellValue(tempData);

        try {
            //创建输出流
            FileOutputStream outputStream = new FileOutputStream(path);
            writeWB.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理表头数据，
     * 第一次处理，清空；其他情况下有数据才拼接数据
     *
     * @param existenceData 原来存在的数据
     * @param processData   待处理的表头数据
     * @return 处理后的表头数据
     */
    public static String dealHeadData(String existenceData, String processData) {
        if (!processData.equals("")) {
            //非空才拼接
            existenceData = existenceData + "\n" + processData;
        }
        return existenceData;
    }

    /**
     * 去除重复内容
     *
     * @return
     */
    public static String removeRepeatData(String data) {
        String datas[] = data.split("\\n");

        HashSet<String> dataSet = new HashSet<String>();
        StringBuffer resultData = new StringBuffer();
        //数据列表判空
        if (datas != null) {
            for (int i = 0; i < datas.length; i++) {
                //以“.”分割，得到去序号的行数据
                String rowDatas[] = datas[i].split("\\.");

                //得到行数据
                String rowData = datas[i];
                //行数据组判空
                if (rowDatas.length > 1) {
                    //得到行数据：包含序号情况
                    rowData = rowDatas[1];
                }
                /**
                 * 得到行数据，序号以"、"分割
                 */
                rowDatas = null;
                rowDatas = datas[i].split("、");
                if (rowDatas.length > 1) {
                    rowData = rowDatas[1];
                }
                dataSet.add(rowData);
            }
        }

        Iterator dataIterator = dataSet.iterator();
        int i = 1;
        System.out.println("进展记录数：" + dataSet.size());
        while (dataIterator.hasNext()) {
            String tempData = (String) dataIterator.next();
            System.out.println(tempData);
            //出去空行
            if (tempData.equals("")) {
                continue;
            }
            resultData.append(i + "." + tempData + "\n");
            i++;
        }

        return resultData.toString();
    }

    /**
     * 处理完成度
     *
     * @param data 待处理数据
     * @return 处理完的数据
     */
    public static float dealDegreeOfCompletion(String data) {
        if (data.length() > 0) {
            return Float.parseFloat(data);
        }
        return 0;
    }

    /**
     * 根据excel的行号设置每天记录的序号
     *
     * @param rowIndex 待存入excel的行号
     * @return 在excel文件中的序号
     */
    public static int getSerialNumber(int rowIndex) {
        //从大往小比较，满足大的一定满足小的，所以先判断大的
        if (rowIndex - ExcelProperties.RISK_ROW_INDEX_INIT >= 0) {
            return rowIndex - ExcelProperties.RISK_ROW_INDEX_INIT + 1;
        }
        if (rowIndex - ExcelProperties.TOMORROW_ROW_INDEX_INIT >= 0) {
            return rowIndex - ExcelProperties.TOMORROW_ROW_INDEX_INIT + 1;
        }
        if (rowIndex - ExcelProperties.TODAY_ROW_INDEX_INIT >= 0) {
            return rowIndex - ExcelProperties.TODAY_ROW_INDEX_INIT + 1;
        }
        return -1;
    }

    /**
     * 设置表的主题名字
     *
     * @param path excel文件的路径
     * @return 主题名字
     */
    public static String setTopic(String path) {
        //.是特殊字符，需要转义！！！！！
        String date = (path.split("-")[2]).split("\\.")[0];
        String titleName = ExcelProperties.titleName;
        return titleName + "(" + date + ")";
    }
}
