package model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/13 19:39
 * @Version: 1.0
 */
@AllArgsConstructor
@Data
public class CharAndIndex {
    /**
     * 删除的字符
     */
    char deleteChar;
    /**
     * 字符在全文的位置
     */
    int index;
}
