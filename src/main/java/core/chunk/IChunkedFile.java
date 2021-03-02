package core.chunk;


import core.buffer.impl.WrapperBytes;
import core.chunk.impl.ChunkedFile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import util.BufferUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author gaoxiaodong
 */
public interface IChunkedFile {

    /**
     * 返回分块文件的大小
     * @return 文件块的大小，单位为byte
     */
    int size();

    /**
     * 转换为输出流
     * @return 返回输出流
     * @throws IOException io异常
     */
    OutputStream toOutputStream() throws IOException;

    /**
     * 转换为ByteBuf，见{@link ByteBuf}
     * @return 转换为netty的ByteBuf，因为它有更好的操作和池化性能
     */
    ByteBuf toByteBuf();

    /**
     * 转换为包装byte数组，不直接转换为数组是为了避免拷贝成本
     * @return 包装数组
     */
    default WrapperBytes wrapperBytes(){
        return BufferUtils.wrapperBytes(toByteBuf());
    }

    /**
     * 转换为数组，但是尽量不要使用，这种方式会产生byte数组拷贝，影响性能
     * @return byte数组
     */
    default byte[] toByteArray(){
        return BufferUtils.array(toByteBuf());
    }

    /**
     * 返回空文件块
     * @return
     */
    IChunkedFile EMPTY_CHUNKED_FILE = new ChunkedFile(UnpooledByteBufAllocator.DEFAULT.heapBuffer(0, 0));
}
