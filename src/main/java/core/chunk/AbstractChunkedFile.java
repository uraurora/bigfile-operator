package core.chunk;

import core.file.AbstractOperableFile;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-23 20:45
 * @description : 抽象文件块实现类
 */
public abstract class AbstractChunkedFile extends AbstractOperableFile<Path> {



    @Override
    protected void internalClose() throws IOException {

    }

}
