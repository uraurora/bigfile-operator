package core.common;

import lombok.Getter;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-03-05 19:56
 * @description :
 */
public abstract class AbstractCancellable implements ICancellable{

    protected volatile boolean isCancelled;

    protected volatile int state;

    protected AbstractCancellable(){
        this.isCancelled = false;
    }

    @Override
    public boolean cancel() {
        try {
            setCancelled(true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void run(){
        while(!isCancelled){
            internalRun();
        }
    }

    protected abstract void internalRun();

    @Override
    public boolean cancelInterrupt() throws InterruptedException {
        try {
            setCancelled(true);
            return true;
        } catch (Exception e) {
            throw new InterruptedException("the task has bean interrupted! ");
        }
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public abstract boolean isDone();

    protected void setCancelled(boolean isCancelled){
        this.isCancelled = isCancelled;
    }

    @Getter
    public enum TaskState{
        /**
         * 无效状态
         */
        INVALID,
        /**
         * 新建任务
         */
        NEW,
        /**
         * 运行中
         */
        RUNNING,
        /**
         * 取消
         */
        CANCELLED,
        /**
         * 已完成
         */
        DONE
        ;
    }
}
