package deal.impl;

import deal.DealFileWay;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import util.FileUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 从事件excel中提取所有的关键词（index=2）、剔除关键词(index=3)
 * 关键字所在字符串格式：#keyword1#keyword2
 *
 * @Author: zhuzw
 * @Date: 2019/11/5 10:12
 * @Version: 1.0
 */
public class EventKeywordExcelDeal implements DealFileWay {

    @Override
    public List<String> extractedValue(String path) {
        Sheet sheet = FileUtil.getSheet(path, 0);

        Set<String> resultSet = new HashSet<>();
        Set<String> keywordSet = new HashSet<>();
        Set<String> excludeKeywordSet = new HashSet<>();
        //从行1开始，0为title
        System.out.println(path);
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (null == row) {
                continue;
            }
            Cell keywordCell = row.getCell(2);
            String keyword = "";
            if (null != keywordCell) {
                keyword = keywordCell.toString();
            }

            Cell excludeKeywordCell = row.getCell(2);
            String excludeKeyword = "";
            if (null != excludeKeywordCell) {
                excludeKeyword = excludeKeywordCell.toString();
            }

            if(! StringUtils.isEmpty(keyword)) {
                //提取关键词     形式：#keyword1#keyword2#
                keywordSet = extractKeyword(keyword);
            }
            if(! StringUtils.isEmpty(excludeKeyword)) {
                excludeKeywordSet = extractKeyword(excludeKeyword);
            }

            resultSet.addAll(keywordSet);
            resultSet.addAll(excludeKeywordSet);
        }
        //将Set转为List
        return resultSet.stream().collect(Collectors.toList());
    }

    /**
     * 提取一行数据中的keyword and exclude keyword
     *
     * @param string 待提取的值  #keyword1#keyword2形式
     * @return 过滤""后的Set集合
     */
    private Set<String> extractKeyword(String string) {
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        String[] straArr = string.split("#");
        return Arrays.stream(straArr)
                .filter(s -> !StringUtils.isEmpty(s))   //提取为null数据
                .collect(Collectors.toSet());
    }

}
