package core.file;

import core.chunk.IChunkedFile;

import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-25 20:17
 * @description :
 */
public abstract class AbstractBigFile extends AbstractOperableFile<Path> implements Iterable<IChunkedFile> {


}
