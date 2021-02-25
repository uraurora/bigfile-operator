package core.buffer;

import constant.enums.FileMode;
import lombok.Getter;
import util.Options;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-24 20:12
 * @description : 创建MappedByteBuffer实例，并在使用结束有效释放资源
 */
@Getter
public class MappedByteBufferHolder implements Closeable {

    private final Path path;

    private final FileMode fileMode;

    private final long offset;

    private final long size;

    private MappedByteBuffer buffer;

    private final Lock writeLock = new ReentrantReadWriteLock().writeLock();

    private final Lock readLock = new ReentrantReadWriteLock().readLock();

    private MappedByteBufferHolder(MappedByteBufferHolderBuilder builder) throws IOException{
        this.path = Paths.get(builder.path);
        checkPath(this.path);
        this.fileMode = builder.fileMode;
        this.offset = builder.offset;
        this.size = builder.size;

        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), fileMode.getFileMode());
             FileChannel fs = raf.getChannel()) {
            buffer = fs.map(fileMode.getMapMode(), offset, size);
        }
    }

    public static MappedByteBufferHolderBuilder builder() {
        return new MappedByteBufferHolderBuilder();
    }

    @Override
    public void close() throws IOException {
        if(checkBuffer(buffer)){
            Buffers.clean(buffer);
            buffer = null;
        }
    }

    public void operate(Consumer<? super MappedByteBuffer> operator){
        operator.accept(buffer);
    }

    public MappedByteBuffer operateWithResult(Consumer<? super MappedByteBuffer> operator){
        return Options.with(buffer, operator);
    }

    public void operateWithReadLock(Consumer<? super MappedByteBuffer> operator){
        readLock.lock();
        try{
            operator.accept(buffer);
        } finally {
            readLock.unlock();
        }
    }

    public void operateWithWriteLock(Consumer<? super MappedByteBuffer> operator){
        writeLock.lock();
        try{
            operator.accept(buffer);
        } finally {
            writeLock.unlock();
        }
    }

    public static final class MappedByteBufferHolderBuilder {
        private String path;
        private FileMode fileMode;
        private long offset = 0L;
        private long size = 0L;


        public MappedByteBufferHolderBuilder withPath(String path) {
            this.path = path;
            return this;
        }

        public MappedByteBufferHolderBuilder withFileMode(FileMode fileMode) {
            this.fileMode = fileMode;
            return this;
        }

        public MappedByteBufferHolderBuilder withOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public MappedByteBufferHolderBuilder withSize(long size) {
            this.size = size;
            return this;
        }

        public MappedByteBufferHolder build() throws IOException {
            return new MappedByteBufferHolder(this);
        }
    }

    //<editor-fold desc="static methods">

    /**
     * 判断文件是否存在
     * @param path 文件路径
     * @throws NoSuchFileException 文件不存在异常
     */
    private static void checkPath(Path path) throws NoSuchFileException {
        if(!Files.exists(path)){
            throw new NoSuchFileException(path.toString());
        }
    }

    private static boolean checkBuffer(ByteBuffer buffer){
        return buffer != null && buffer.isDirect() && buffer.capacity() != 0;
    }
    //</editor-fold>
}
