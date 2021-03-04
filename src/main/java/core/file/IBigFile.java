package core.file;

import core.chunk.IChunkedFile;
import core.common.IStreamable;

import java.math.BigInteger;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-25 20:17
 * @description :
 */
public interface IBigFile {
    /**
     * 文件大小
     * @return 文件大小
     */
    BigInteger size();

    /**
     * 获取chunkedSize分块大小
     * @return 分块大小
     */
    int getChunkedSize();

    /**
     * 设置分块chunkedSize大小
     * @param chunkedSize 分块大小
     */
    void setChunkedSize(int chunkedSize);

    /**
     * 是否是本地文件形式的大文件
     * @return 是否是本地文件的大文件
     */
    boolean isLocal();

}
