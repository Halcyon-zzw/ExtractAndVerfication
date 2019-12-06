package demand.mapping;

import demand.mapping.model.MappingResult;
import demand.mapping.service.MappingService;

import java.util.List;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/27 9:28
 * @Version: 1.0
 */
public class MappingController {
    private MappingService mappingService = new MappingService();


    /**
     * 单情感分类
     *
     * @param title 文章标题
     * @return 对应的情感分类，未匹配到返回null
     */
    public MappingResult classifyByRule(String title) {
        return mappingService.classifyByRule(title);
    }


    /**
     * 多情感分类
     *
     * @param title 文章标题
     * @return 对应的情感分类，未匹配到返回null
     */
    public List<MappingResult> multiClassifyByRule(String title, int count) {
        return mappingService.multiClassifyByRule(title, count);
    }


    /**
     * 批量分类某文件，并加结果数据添加到原数据后列
     * @param path 文件路径；文件内容格式规定，最多包含以下内容，且顺序符合如下：
     *                                      列1：标题
     *                                      列2：内容
     *                                      列3：分类名
     * @return
     */
    public int multiClassifyByRuleWithFile(String path) {
        return mappingService.multiClassifyByRuleWithFile(path);
    }


    /**
     * 自定义规则统计文章数量，并将统计到的文章内容保存在当前目录内
     * @param articlePath
     * @param rulePath
     * @return
     */
    public int classifyByCustomize(String articlePath, String rulePath) {
        return mappingService.classifyByCustomize(articlePath, rulePath);
    }
}
