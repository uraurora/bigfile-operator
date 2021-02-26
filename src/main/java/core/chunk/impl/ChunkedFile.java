package core.chunk.impl;

import core.IChunkedFile;
import core.chunk.AbstractChunkedFile;

import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-25 17:35
 * @description : 分块文件
 */
public class ChunkedFile extends AbstractChunkedFile implements IChunkedFile {

    @Override
    public long size() {
        return 0;
    }

    @Override
    public String fileName() {
        return null;
    }

    @Override
    public BasicFileAttributes basicFileAttributes() {
        return null;
    }
}
