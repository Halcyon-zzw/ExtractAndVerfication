package util;

import com.csvreader.CsvReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Excel操作工具类
 *
 * @Author: zhuzw
 * @Date: 2019/8/7 13:23
 * @Version: 1.0
 */
public class FileUtil {
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
                    FileInputStream fis = new FileInputStream(excelName);
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    //执行
                    //文件流对象
                    FileInputStream fis = new FileInputStream(excelName);
                    wb = new XSSFWorkbook(fis);
                } else {
                    System.out.println("文件类型错误! " + excelPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("找不到指定的文件, " + excelPath);
        }
        return wb;
    }

    public static Sheet getSheet(String excelPath, int sheetIndex) {
        Workbook workbook = getWorkbook(excelPath);
        if (null == workbook) {
            System.out.println("工作铺创建失败！");
            return null;
        }
        return workbook.getSheetAt(sheetIndex);
    }

    /**
     * 获取csv文件格式操作对象,默认使用UTF-8编码格式
     * @param csvPath
     * @return
     * @throws FileNotFoundException
     */
    public static CsvReader getCsvReader(String csvPath) {
        return getCsvReader(csvPath, Charset.forName("UTF-8"));
    }

    /**
     * 获取csv文件格式操作对象
     *
     * @param csvPath
     * @param encodingFormat 编码格式
     * @return CsvReader对象
     */
    public static CsvReader getCsvReader(String csvPath, Charset encodingFormat) {
        return getCsvReader(csvPath, ',', encodingFormat);
    }

    /**
     * 获取csv文件格式操作对象
     *
     * @param csvPath
     * @param separator 分割符
     * @return CsvReader对象
     */
    public static CsvReader getCsvReader(String csvPath, char separator) {
        return getCsvReader(csvPath, separator, Charset.forName("UTF-8"));
    }

    /**
     * 获取csv文件格式操作对象
     *
     * @param csvPath
     * @param encodingFormat 编码格式
     * @return CsvReader对象
     */
    public static CsvReader getCsvReader(String csvPath, char separator, Charset encodingFormat) {
        CsvReader csvReader = null;

        try {
            csvReader = new CsvReader(csvPath, separator, encodingFormat);

            //解决数据长度大于100000出错问题
            csvReader.setSafetySwitch(false);
        }catch (FileNotFoundException fnfe) {
            System.out.println("文件为找到！");
            System.out.println(fnfe.getMessage());
        }
        return csvReader;
    }


    /**
     * 通过自定义编码格式获取csvReader对象，
     * 一般用于修改的csv文件（gbk）
     *
     * @param path
     * @param encoding 编码格式
     * @return
     */
    public static CsvReader getCsvReader(String path, String encoding) {
        return getCsvReader(path, Charset.forName(encoding));
    }



    /**
     * 将字符串列表保存到文件中,不存在目录自动创建
     *
     * @param strings 字符串列表
     * @param path 文件路径（包含文件名）
     * @throws IOException
     */
    public static void createFile(List<String> strings, String path) throws IOException {
        createDir(path);

        Path target = Paths.get(path);
//        BufferedWriter bw = new BufferedWriter(
//                new OutputStreamWriter(
//                        new FileOutputStream(target.toFile())
//                        , StandardCharsets.UTF_8));
        BufferedWriter bw = Files.newBufferedWriter(target);

        for (String s : strings) {
            if(s == null) {
                continue;
            }
            bw.append(s);
            bw.newLine();
        }
        //逐层关闭
        bw.close();
    }

    /**
     * 将字符串列表保存到文件中,不存在目录自动创建
     *
     * @param strings 字符串列表
     * @param path 文件路径（包含文件名）
     * @throws IOException
     */
    public static void createFile2(Collection<String> strings, String path) throws IOException {
        createDir(path);

        Path target = Paths.get(path);
//        BufferedWriter bw = new BufferedWriter(
//                new OutputStreamWriter(
//                        new FileOutputStream(target.toFile())
//                        , StandardCharsets.UTF_8));
        BufferedWriter bw = Files.newBufferedWriter(target);

        for (String s : strings) {
            if(s == null) {
                continue;
            }
            bw.append(s);
            bw.newLine();
        }
        //逐层关闭
        bw.close();
    }


    /**
     * 将字符串列表保存到文件中,不存在目录自动创建
     *
     * @param strings 字符串列表
     * @param path 文件路径（包含文件名）
     * @throws IOException
     */
    public static void createFile(Collection<String[]> strings, String path) throws IOException {
        createDir(path);

        Path target = Paths.get(path);
        BufferedWriter bw = Files.newBufferedWriter(target);

        for (String[] ss : strings) {
            if(ss == null) {
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder("");
            for (String s: ss) {
                stringBuilder.append(s);
            }
            bw.append(stringBuilder.toString());
            bw.newLine();
        }
        //逐层关闭
        bw.close();
    }

    /**
     * 读取所有数据，处理方式，默认直接放回
     * @param path
     * @return
     * @throws IOException
     */
    public static List<String> readAll(String path) throws IOException {
        return readAll(path, (line) -> {return line;});
    }

    public static List<String> readAll(String path, DataProcess<String> dataProcess) throws IOException {
        List<String> resultList = new ArrayList<>();
        //加载关键字
        String keywordsPath = path;
        if (resultList.size() == 0) {
//            BufferedReader bReader  = new BufferedReader(new InputStreamReader(new FileInputStream(keywordsPath), "UTF-8"));
            BufferedReader bReader = Files.newBufferedReader(Paths.get(keywordsPath), Charset.forName("UTF-8"));

            String line = null;
            String tempLine = null;
            while ((line = bReader.readLine()) != null) {
                if (!StringUtils.isEmpty(line)) {
                    tempLine = dataProcess.process(line);
                    resultList.add(tempLine);
                }
            }
        }
        return resultList;
    }

    public static boolean createDir(String path) {
        Path target = Paths.get(path);
        File dir = target.getParent().toFile();
        if (! dir.exists()) {
            //不存在，生成目录
            dir.mkdir();
            System.out.println("创建目录成功，时间：" + LocalDate.now());
            return true;
        }
        return false;
    }
}
