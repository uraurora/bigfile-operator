package core.file.impl;

import core.chunk.IChunkedFile;
import core.file.AbstractBigFile;
import core.file.IBigFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-25 20:17
 * @description :
 */
public class BigFile extends AbstractBigFile implements IBigFile {
    @Override
    public BigInteger size() {
        return null;
    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    protected void internalClose() throws IOException {

    }

    @Override
    public Iterator<IChunkedFile> iterator() {
        return null;
    }

    @Override
    protected boolean isEnd() {
        return false;
    }
}
