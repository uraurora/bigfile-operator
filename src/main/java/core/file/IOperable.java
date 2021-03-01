package core.file;

import java.util.function.Consumer;

import static util.Options.*;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-26 17:09
 * @description :
 */
public interface IOperable<T> {
    /**
     * 可操作的对象实例
     * @return 对象实例
     */
    T operable();

    /**
     * 对包装对象进行操作
     * @param operator 操作器
     */
    default void operate(Consumer<? super T> operator){
        let(operable(), operator);
    }

    /**
     * 对包装对象进行操作，会抛出异常
     * @param operator 操作器
     * @throws Exception 异常
     */
    default void operateWithException(Consumer<? super T> operator) throws Exception{
        let(operable(), operator);
    }

    /**
     * 执行操作后返回可操作对象
     * @param operator 操作器
     * @return 可操作对象
     */
    default T operateWithResult(Consumer<? super T> operator){
        return with(operable(), operator);
    }
}
