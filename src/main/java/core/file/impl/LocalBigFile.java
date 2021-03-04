package core.file.impl;

import core.chunk.IChunkedFile;
import core.chunk.impl.ChunkedFile;
import core.file.AbstractBigFile;
import core.file.ILocalBigFile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

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
public class LocalBigFile extends AbstractBigFile implements ILocalBigFile {

    protected FileChannel fc;

    private final ByteBufAllocator allocator;

    protected final long startOffset;

    protected final long endOffset;

    protected long readerIndex;

    protected BigInteger size;


    public LocalBigFile(Path path, long startOffset, long endOffset, int chunkedSize, ByteBufAllocator allocator) throws IOException {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.chunkedSize = chunkedSize;
        checkSize(this.chunkedSize);
        checkSize(this.startOffset, this.endOffset);
        this.size = BigInteger.valueOf(startOffset - endOffset);
        this.readerIndex = this.startOffset;
        this.file = path;
        this.fc = FileChannel.open(file, StandardOpenOption.READ);
        this.allocator = allocator;
    }

    public LocalBigFile(Path path, int chunkedSize, ByteBufAllocator allocator) throws IOException {
        this(path, 0, Files.size(path), chunkedSize, allocator);
    }

    public LocalBigFile(Path path, ByteBufAllocator allocator) throws IOException {
        this(path, 0, Files.size(path), DEFAULT_CHUNKED_SIZE, allocator);
    }

    public LocalBigFile(Path path, int chunkedSize) throws IOException {
        this(path, chunkedSize, ByteBufAllocator.DEFAULT);
    }

    public LocalBigFile(Path path) throws IOException {
        this(path, DEFAULT_CHUNKED_SIZE);
    }


    @Override
    protected void internalClose() throws IOException {
        if(fc != null){
            fc.close();
            fc = null;
        }
    }

    @Override
    public IChunkedFile getChunk() throws IOException {
        return internalReadChunk(this.readerIndex, false);
    }

    @Override
    public IChunkedFile getChunk(int index) throws IOException {
        return internalReadChunk(startOffset + (index - 1) * chunkedSize, false);
    }

    @Override
    public IChunkedFile readChunk() throws IOException{
        return internalReadChunk(this.readerIndex, true);
    }

    @Override
    public void readerIndex(long readerIndex) {
        this.readerIndex = readerIndex;
    }

    private IChunkedFile internalReadChunk(long readerIndex, boolean isRead) throws IOException {
        long offset = readerIndex;
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
            if(isRead){
                this.readerIndex += readBytes;
            }
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

    public long getReaderIndex() {
        return readerIndex;
    }

    public void reset(){
        readerIndex = startOffset;
    }

    @Override
    public BigInteger size() {
        return size;
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    protected boolean isEnd() {
        return readerIndex >= endOffset;
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
