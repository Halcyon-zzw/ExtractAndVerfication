package create.adapter;

import create.CreateFileWay;
import create.impl.CreateFileProportion;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生成文件适配器，数据列表 为Collection
 * TODO 下一版本使用该版本替代上一版本
 *
 * @Author: zhuzw
 * @Date: 2019/11/7 13:22
 * @Version: 1.0
 */
public class CreateFileWayAdapter implements CreateFileWayCollection {
    private CreateFileWay createFileWay;

    public CreateFileWayAdapter(CreateFileWay createFileWay) {
        this.createFileWay = createFileWay;
    }

    @Override
    public void createFile(Collection<String> collection) throws IOException {
        List<String> strList = collection.stream().map(s -> s).collect(Collectors.toList());
        createFileWay.createFile(strList);
    }
}
