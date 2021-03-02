package core.file;

import core.chunk.IChunkedFile;

import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-25 20:17
 * @description :
 */
public abstract class AbstractBigFile extends AbstractOperableFile<Path> implements Iterable<IChunkedFile> {

    protected int chunkedSize;
    /**
     * 大文件进行分块，内部的分块索引是否到达末尾
     * @return 是否到达末尾
     */
    protected abstract boolean isEnd();

    public int getChunkedSize() {
        return chunkedSize;
    }

    public void setChunkedSize(int chunkedSize) {
        this.chunkedSize = chunkedSize;
    }
}
