package core.file;

import core.chunk.IChunkedFile;

import java.io.IOException;

/**
 * @author gaoxiaodong
 */
public interface ILocalBigFile extends IBigFile {
    /**
     * 获取当前文件readerIndex开始至(readerIndex + chunkedSize)处的分块文件
     * @return 分块文件
     * @throws IOException io异常
     */
    IChunkedFile getChunk() throws IOException;

    /**
     * 获取文件[(readerIndex+(index-1)*chunkedSize), (readerIndex + chunkedSize*index))的分块文件
     * @param index 第几个文件块，从1开始计数
     * @return 分块文件
     * @throws IOException io异常
     */
    IChunkedFile getChunk(int index) throws IOException;

    /**
     * 从文件当前readerIndex处出去分块文件，并且readerIndex会增加chunkedSize
     * @return 分块文件
     * @throws IOException io异常
     */
    IChunkedFile readChunk() throws IOException;

    /**
     * 设置文件readerIndex
     * @param readerIndex 读取索引
     */
    void readerIndex(long readerIndex);

}
