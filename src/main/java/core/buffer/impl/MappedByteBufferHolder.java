package core.buffer.impl;

import core.file.AbstractOperableFile;
import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-24 20:12
 * @description : 创建MappedByteBuffer实例，并在使用结束有效释放资源
 */
@Getter
public class MappedByteBufferHolder extends AbstractOperableFile<MappedByteBuffer> {

    private MappedByteBufferConfig config;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock readLock = lock.readLock();

    private final Lock writeLock = lock.writeLock();

    private MappedByteBufferHolder(MappedByteBufferConfig config) throws IOException{
        this.config = config;
        checkPath(this.config.getPath());
        map();
    }

    public static MappedByteBufferHolder of(MappedByteBufferConfig config) throws IOException {
        return new MappedByteBufferHolder(config);
    }

    private void map() throws IOException {
        try(FileChannel fs = FileChannel.open(this.config.getPath(), this.config.getFileMode().getOpenOption())){
            file = fs.map(this.config.getFileMode().getMapMode(), this.config.getOffset(), this.config.getSize());
        }
    }

    @Override
    protected void internalClose() throws IOException {
        unmap();
    }

    public void unmap(){
        if(checkBuffer(file)){
            Cleaner.clean(file);
            file = null;
        }
    }

    public void operateWithReadLock(Consumer<? super MappedByteBuffer> operator){
        readLock.lock();
        try{
            operator.accept(file);
        } finally {
            readLock.unlock();
        }
    }

    public void operateWithWriteLock(Consumer<? super MappedByteBuffer> operator){
        writeLock.lock();
        try{
            operator.accept(file);
        } finally {
            writeLock.unlock();
        }
    }

    public void remap() throws IOException {
        map();
    }

    public void remap(MappedByteBufferConfig config) throws IOException {
        this.config = config;
        map();
    }

    private static class Cleaner {
        public static final boolean CLEAN_SUPPORTED;
        private static final Method DIRECT_BUFFER_CLEANER;
        private static final Method DIRECT_BUFFER_CLEANER_CLEAN;

        private Cleaner(){}

        static {
            Method directBufferCleanerX = null;
            Method directBufferCleanerCleanX = null;
            boolean v;
            try {
                directBufferCleanerX = Class.forName("java.nio.DirectByteBuffer").getMethod("cleaner");
                directBufferCleanerX.setAccessible(true);
                directBufferCleanerCleanX = Class.forName("sun.misc.Cleaner").getMethod("clean");
                directBufferCleanerCleanX.setAccessible(true);
                v = true;
            } catch (Exception e) {
                v = false;
            }
            CLEAN_SUPPORTED = v;
            DIRECT_BUFFER_CLEANER = directBufferCleanerX;
            DIRECT_BUFFER_CLEANER_CLEAN = directBufferCleanerCleanX;
        }

        public static void clean(ByteBuffer buffer) {
            if (buffer == null) {
                return;
            }
            if (CLEAN_SUPPORTED && buffer.isDirect()) {
                try {
                    Object cleaner = DIRECT_BUFFER_CLEANER.invoke(buffer);
                    DIRECT_BUFFER_CLEANER_CLEAN.invoke(cleaner);
                } catch (Exception e) {
                    // silently ignore exception
                }
            }
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
