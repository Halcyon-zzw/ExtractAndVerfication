package config.factory;

import config.ApplicationProperties;
import create.CreateFileWay;
import create.impl.CreateFileDirect;
import create.impl.CreateFileProportion;
import org.apache.commons.lang3.StringUtils;

/**
 * 生成文件工厂类
 *
 * @Author: zhuzw
 * @Date: 2019/10/9 16:04
 * @Version: 1.0
 */
public class CreateFileFactory {

    static ApplicationProperties aps = new ApplicationProperties();
    /**
     * 获取生成文件方式
     *
     * @param createFileType 属性类型
     * @return
     */
    public static CreateFileWay getCreateFileWay(String createFileType) {
        if (StringUtils.isEmpty(createFileType)) {
            return null;
        }
        if (createFileType.equalsIgnoreCase("direct")) {
            //直接生成
            return new CreateFileDirect(aps.getCreateFile().getDirectPath());
        } else if (createFileType.equalsIgnoreCase("proportion")) {
            //按比率生成
//            return new CreateFileProportion(aps.getCreateFileProporttionProperties().getProportions(), aps.getCreateFileProporttionProperties().getPaths());
        }
        return null;
    }
}
