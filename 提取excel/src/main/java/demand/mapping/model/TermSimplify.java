package demand.mapping.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Term简化类，仅包含word 和 nature
 *
 * @Author: zhuzw
 * @Date: 2019/11/28 13:19
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
public class TermSimplify {
    private String word;

    private String nature;

    @Override
    public int hashCode() {
        return (word + "/" + nature).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof TermSimplify)) {
            return false;
        }
        TermSimplify termSimplify = (TermSimplify)obj;
        //地址相同
        if (this == termSimplify) {
            return true;
        }

        if (termSimplify.getWord().equals(this.word) && termSimplify.getNature().equalsIgnoreCase(this.nature)) {
            return true;
        }
        return false;
    }
}
