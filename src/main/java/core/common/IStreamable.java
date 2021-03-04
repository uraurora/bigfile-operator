package core.common;

import core.chunk.IChunkedFile;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author gaoxiaodong
 */
public interface IStreamable<T> extends Iterable<T> {

    /**
     * 返回串行流式结果
     * @return 串行流stream
     */
    default Stream<T> stream(){
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * 返回并行流式结果
     * @return 串行流stream
     */
    default Stream<T> parallelStream(){
        return StreamSupport.stream(spliterator(), true);
    }

}
