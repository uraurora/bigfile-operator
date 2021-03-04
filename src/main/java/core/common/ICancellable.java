package core.common;

/**
 * @author gaoxiaodong
 */
public interface ICancellable {
    /**
     * Attempt to cancel execution of this task.
     * @return 是否成功
     */
    boolean	cancel();

    /**
     * 尝试取消任务并且有可能被中断
     * @throws InterruptedException 中断异常
     * @return 是否成功取消
     */
    boolean cancelInterrupt() throws InterruptedException;

    /**
     * Returns true if this task was cancelled before it completed normally.
     * @return 是否处于取消状态
     */
    boolean	isCancelled();

    /**
     * Returns true if this task completed.
     * @return 是否处于完成状态
     */
    boolean	isDone();

}
