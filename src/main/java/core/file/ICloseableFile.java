package core.file;

import java.io.Closeable;

/**
 * @author gaoxiaodong
 */
public interface ICloseableFile extends Closeable {

    /**
     * 文件是否关闭
     * @return 返回是否关闭
     */
    boolean isClosed();

}
