package demand.mapping.model;

import lombok.Data;

/**
 * 规则关键词
 *
 * @Author: zhuzw
 * @Date: 2019/11/19 10:45
 * @Version: 1.0
 */
@Data
public class RuleKeyword {
    /**
     * 关键词
     * 格式：#注销#资格#
     */
    String includeKeyword;
    /**
     * 剔除关键词
     * 格式：职业资格#任职资格#资格审批#自查#澄清#证书资格
     */
    String excludeKeyword;

    /**
     * 剔除多个关键词
     * 格式：下调#价格|价格#下调|获#升目标|下调#费|下调#业绩|下调#房|下调#预期|下调#率|下调#价|升#评级|下调#规模|下调#估值|获#维持评级|关税#下调|售价#下调|下调#税|
     */
    String excludeMultipleKeyword;
}
