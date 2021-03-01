package core.file.impl;

import core.chunk.IChunkedFile;
import core.file.AbstractBigFile;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-26 17:30
 * @description : 本地大文件，文件容量默认大于 2^32-1 byte的文件
 */
public class LocalBigFile extends AbstractBigFile {
    @Override
    protected void internalClose() throws IOException {

    }

    @Override
    public Iterator<IChunkedFile> iterator() {
        return null;
    }
}
