package core.file;

import java.io.IOException;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-26 16:49
 * @description :
 */
public abstract class AbstractOperableFile<T> implements IOperable<T>, ICloseableFile{

    protected volatile boolean closed = false;

    protected T file;

    protected AbstractOperableFile(T file){
        this.file = file;
    }

    protected AbstractOperableFile(){}

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed){
        this.closed = closed;
    }

    @Override
    public void close() throws IOException{
        synchronized(this) {
            if (closed) {
                return;
            }
            internalClose();
            closed = true;
        }
    }

    @Override
    public T operable() {
        return file;
    }

    /**
     * 处理文件关闭时候的动作
     * @throws IOException io异常
     */
    protected abstract void internalClose() throws IOException;
}
