package util;

import constant.enums.FileMode;
import core.value.WrapperBytes;
import core.value.MappedFileConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.SneakyThrows;
import org.joor.Reflect;
import sun.nio.ch.DirectBuffer;
import sun.nio.ch.FileChannelImpl;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-24 10:14
 * @description : 数字节组缓存工具类
 */
public abstract class BufferUtils {

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

    /**
     * 不直接返回数组是因为避免不必要的数组复制
     * @param buf ByteBuf
     * @return 包装数组
     */
    public static WrapperBytes wrapperBytes(ByteBuf buf){
        byte[] array;
        int offset, length;
        if (buf.hasArray()){
            // 支撑数组形式的话
            array = buf.array();
            // 计算第一个字节的偏移量
            offset = buf.arrayOffset() + buf.readerIndex();
            // 获取可读字节数
            length = buf.readableBytes();
        } else {
            // 直接缓冲区形式，获取可读字节数
            offset = 0;
            length = buf.readableBytes();
            // 分配一个数组保存具有该长度的字节数据
            array = new byte[length];
            // 复制数据
            buf.getBytes(buf.readerIndex(), array);
        }
        return new WrapperBytes(array, offset, length);
    }

    public static byte[] array(ByteBuf buf){
        WrapperBytes bytes = wrapperBytes(buf);
        return Arrays.copyOfRange(bytes.getArray(), bytes.getOffset(), bytes.getLength());
    }


    public static void main(String[] args) {
        ByteBuf buf = null;
        ByteBuf buf1 = null;
        try{
            buf = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
            buf.writeLong(12);
            System.out.println("array=" + Arrays.toString(array(buf)));
            System.out.println("wrapperArray=" +wrapperBytes(buf));
            buf1 = UnpooledByteBufAllocator.DEFAULT.directBuffer();
            buf1.writeLong(12);
            System.out.println("array=" +Arrays.toString(array(buf1)));
            System.out.println("wrapperArray=" + wrapperBytes(buf1));
        } finally {
            buf.release();
            buf1.release();
        }

    }
}
