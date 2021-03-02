package core.chunk;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import core.chunk.impl.ChunkedFile;
import io.netty.buffer.ByteBuf;

import java.time.Duration;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-25 17:14
 * @description :
 */
public class ChunkedFileFactory {

    private ChunkedFileFactory(){}

    private static class ChunkedFileFactoryHolder{
        private static final ChunkedFileFactory INSTANCE = new ChunkedFileFactory();
    }

    public static ChunkedFileFactory getInstance(){
        return ChunkedFileFactoryHolder.INSTANCE;
    }

    private static final int EXPIRE_HOURS = 2;

    private static final int CACHE_MAX_SIZE = 1024 * 8;

    private final LoadingCache<IChunkedFileIndex, IChunkedFile> cache = CacheBuilder.newBuilder()
            .maximumSize(CACHE_MAX_SIZE)
            .expireAfterWrite(Duration.ofHours(EXPIRE_HOURS))
            .build(new CacheLoader<IChunkedFileIndex, IChunkedFile>() {
                @Override
                public IChunkedFile load(IChunkedFileIndex key) throws Exception {
                    return newChunkedFile(key);
                }
            });

    public IChunkedFile getChunkedFile(IChunkedFileIndex key){
        return cache.getUnchecked(key);
    }

    private IChunkedFile newChunkedFile(IChunkedFileIndex key){
        // todo: impl
        return null;
    }

    private IChunkedFile newChunkedFile(ByteBuf buffer){
        return new ChunkedFile(buffer);
    }

}
