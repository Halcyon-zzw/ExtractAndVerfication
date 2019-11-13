package config;

import org.apache.commons.lang3.StringUtils;

/**
 * 属性工厂类
 *
 * @Author: zhuzw
 * @Date: 2019/10/9 16:04
 * @Version: 1.0
 */
public class PropertiesFactory {
    /**
     * 获取属性
     *
     * @param propertyType 属性类型
     * @return
     */
    public static ApplicationProperties.PrimaryProperties getProperties(String propertyType) {
        if (StringUtils.isEmpty(propertyType)) {
            return null;
        }
        if (propertyType.equalsIgnoreCase("event")) {
            //事件二级分类
            return new ApplicationProperties().getEventClassifyProperties();
        } else if (propertyType.equalsIgnoreCase("emotion")) {
            //舆情情感
            return new ApplicationProperties().getEmotionProperties();
        } else if (propertyType.equalsIgnoreCase("emotionAndGrade7")) {
            //情感 and 等级
            return new ApplicationProperties().getEmotionAndGradeProperties();
        } else if (propertyType.equalsIgnoreCase("emotionAndGrade5")) {
            //情感 and 等级 5分类
            return new ApplicationProperties().getEmotionAndGradeProperties5();
        } else if (propertyType.equalsIgnoreCase("emotionAndGradeExcel")) {
            //情感 and 等级 excel
            return new ApplicationProperties().getEmotionAndGradeExcelProperties();
        } else if (propertyType.equalsIgnoreCase("column")) {
            //栏目分类
            return new ApplicationProperties().getColumnProperties();
        }else if (propertyType.equals("emotionAndGradeTsv")) {
            return new ApplicationProperties().getEmotionAndGradeTsvProperties();
        }else if (propertyType.equals("emotionAndGradeTest")) {
            return new ApplicationProperties().getEmotionAndGradeTestProperties();
        }
        return null;
    }
}
