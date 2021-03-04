package core.common;

import java.io.Closeable;

/**
 * @author gaoxiaodong
 */
public interface ICloseable extends Closeable {

    /**
     * 文件是否关闭
     * @return 返回是否关闭
     */
    boolean isClosed();

}
