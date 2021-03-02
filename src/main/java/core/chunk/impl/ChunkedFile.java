package core.chunk.impl;

import core.chunk.IChunkedFile;
import core.chunk.AbstractChunkedFile;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-25 17:35
 * @description : 分块文件
 */
public class ChunkedFile extends AbstractChunkedFile implements IChunkedFile {

    private ByteBuf buffer;

    public ChunkedFile(Path path, long position, int chunkedSize) throws IOException {
        super(path, chunkedSize);
        buffer.writeBytes(file, position, chunkedSize);
    }

    public ChunkedFile(FileChannel fileChannel, long position, int chunkedSize) throws IOException {
        super(fileChannel, chunkedSize);
        buffer.writeBytes(file, position, chunkedSize);
    }

    public ChunkedFile(ByteBuf buffer){
        this.buffer = buffer;
        this.chunkedSize = this.buffer.readableBytes();
    }

    @Override
    public OutputStream toOutputStream() throws IOException {
        return null;
    }

    @Override
    public ByteBuf toByteBuf() {
        return null;
    }

}
