package core.chunk;

import core.file.AbstractOperableFile;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Objects;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-23 20:45
 * @description : 抽象文件块实现类
 */
public abstract class AbstractChunkedFile extends AbstractOperableFile<FileChannel> {

    protected int chunkedSize;


    protected AbstractChunkedFile(FileChannel fc, int chunkedSize) {
        this.file = Objects.requireNonNull(fc, "fileChannel");
        this.chunkedSize = chunkedSize;
    }

    protected AbstractChunkedFile(Path path, int chunkedSize) throws IOException {
        this(FileChannel.open(path, EnumSet.of(StandardOpenOption.READ)), chunkedSize);
    }

    protected AbstractChunkedFile(){}

    @Override
    protected void internalClose() throws IOException {
        file.close();
    }

    public int size(){
        return chunkedSize;
    }

    /**
     * compare
     * @param that other chunk file
     * @return 比较结果
     */
    public abstract int compareTo(IChunkedFile that);

}
