package core.file.impl;

import core.chunk.IChunkedFile;
import core.chunk.impl.ChunkedFile;
import core.file.AbstractBigFile;
import core.file.IBigFile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-26 17:30
 * @description : 本地大文件，文件容量默认大于 2^32-1 byte的文件
 */
public class LocalBigFile extends AbstractBigFile implements IBigFile {

    protected final FileChannel fc;

    private final ByteBufAllocator allocator;

    protected final long startOffset;

    protected final long endOffset;

    protected long offset;

    protected BigInteger size;


    public LocalBigFile(Path path, long startOffset, long endOffset, ByteBufAllocator allocator) throws IOException {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        checkSize(startOffset, endOffset);
        this.size = BigInteger.valueOf(startOffset - endOffset);
        this.offset = this.startOffset;
        this.file = path;
        this.fc = FileChannel.open(file, StandardOpenOption.READ);
        this.allocator = allocator;
    }

    public LocalBigFile(Path path) throws IOException {
        this(path, 0, Files.size(path), ByteBufAllocator.DEFAULT);
    }

    public LocalBigFile(Path path, ByteBufAllocator allocator) throws IOException {
        this(path, 0, Files.size(path), allocator);
    }


    @Override
    protected void internalClose() throws IOException {
        fc.close();
    }

    private IChunkedFile readChunk() throws IOException{
        // todo: ensure
        long offset = this.offset;
        if (offset >= endOffset) {
            return null;
        }
        ByteBuf buffer = allocator.buffer(chunkedSize);
        boolean release = true;
        try {
            int readBytes = 0;
            for (;;) {
                int localReadBytes = buffer.writeBytes(fc, offset + readBytes, chunkedSize - readBytes);
                if (localReadBytes < 0) {
                    break;
                }
                readBytes += localReadBytes;
                if (readBytes == chunkedSize) {
                    break;
                }
            }
            this.offset += readBytes;
            release = false;
            return new ChunkedFile(buffer);
        } finally {
            if (release) {
                buffer.release();
            }
        }
    }

    @Override
    public Iterator<IChunkedFile> iterator() {
        return new LocalBigFileIterator();
    }

    public long getStartOffset() {
        return startOffset;
    }

    public long getEndOffset() {
        return endOffset;
    }

    public long getOffset() {
        return offset;
    }

    public void reset(){
        offset = startOffset;
    }

    @Override
    public BigInteger size() {
        return size;
    }

    @Override
    protected boolean isEnd() {
        return offset >= endOffset;
    }

    private class LocalBigFileIterator implements Iterator<IChunkedFile>{

        private long _offset = 0;

        @Override
        public boolean hasNext() {
            return _offset < endOffset;
        }

        @Override
        public IChunkedFile next() {
            long offset = this._offset;
            if (offset >= endOffset) {
                return null;
            }
            ByteBuf buffer = allocator.buffer(chunkedSize);
            boolean release = true;
            try {
                int readBytes = 0;
                for (;;) {
                    int localReadBytes = buffer.writeBytes(fc, offset + readBytes, chunkedSize - readBytes);
                    if (localReadBytes < 0) {
                        break;
                    }
                    readBytes += localReadBytes;
                    if (readBytes == chunkedSize) {
                        break;
                    }
                }
                this._offset += readBytes;
                release = false;
                return new ChunkedFile(buffer);
            } catch (IOException e) {
                // ignore
                return IChunkedFile.EMPTY_CHUNKED_FILE;
            } finally {
                if (release) {
                    buffer.release();
                }
            }
        }
    }
}
