package core.buffer;

import constant.enums.FileMode;
import core.value.MappedFileConfig;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.joor.Reflect;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;
import sun.nio.ch.FileChannelImpl;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.function.Consumer;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-24 10:14
 * @description : 缓存
 */
public class Buffers {

    public static MappedByteBuffer mappedByteBuffer(File file, FileMode fileMode, long offset, long length) throws IOException {
        return new RandomAccessFile(file, fileMode.getFileMode())
                .getChannel()
                .map(fileMode.getMapMode(), offset, length);
    }

    public static MappedByteBuffer mappedByteBuffer(Path path, FileMode fileMode, long offset, long length) throws IOException{
        return mappedByteBuffer(path.toFile(), fileMode, offset, length);
    }

    public static MappedByteBuffer mappedByteBuffer(final MappedFileConfig config) throws IOException{
        return mappedByteBuffer(config.getPath().toFile(), config.getFileMode(), config.getOffset(), config.getLength());
    }

    public static void clean(final MappedByteBuffer buffer) {
        ((DirectBuffer) buffer).cleaner().clean();
        AccessController.doPrivileged(new PrivilegedAction() {
            @SneakyThrows
            @Override
            public Object run() {
                Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                getCleanerMethod.setAccessible(true);
                sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                cleaner.clean();
                return null;
            }
        });
    }

    public static void unmap(final MappedByteBuffer buffer){
        // 在关闭资源时执行以下代码释放内存
        Reflect.onClass(FileChannelImpl.class).call("unmap", buffer);
    }

    public static void unmapWithException(final MappedByteBuffer buffer) throws Exception{
        Method m = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
        m.setAccessible(true);
        m.invoke(FileChannelImpl.class, buffer);
    }

    public static void withMappedByteBuffer(final MappedFileConfig config, Consumer<? super MappedByteBuffer> consumer) throws Exception {
        MappedByteBuffer mappedByteBuffer = null;
        try{
            mappedByteBuffer = mappedByteBuffer(config);
            consumer.accept(mappedByteBuffer);
        } finally {
            clean(mappedByteBuffer);
        }
    }

    /**
     * 创建MappedByteBuffer
     * @param config
     * @return
     * @throws IOException
     */
    private static InternalMappedInfo mappedInfo(final MappedFileConfig config) throws IOException {
        RandomAccessFile raf = null;
        FileChannel fs = null;
        MappedByteBuffer buffer;
        try{
            raf = new RandomAccessFile(config.getPath().toFile(), config.getFileMode().getFileMode());
            fs = raf.getChannel();
            buffer = fs.map(config.getFileMode().getMapMode(), config.getOffset(), config.getLength());
        } finally {
            if(fs != null){
                fs.close();
            }
            if(raf != null){
                raf.close();
            }
        }
        return InternalMappedInfo.of(buffer, raf, fs);
    }

    @Data(staticConstructor = "of")
    private static class InternalMappedInfo{
        private final MappedByteBuffer mappedByteBuffer;
        private final Closeable closeable;
        private final FileChannel fileChannel;
    }
}
