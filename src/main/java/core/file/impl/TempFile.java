package core.file.impl;

import core.file.AbstractOperableFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-26 16:49
 * @description : 结束使用会自己删除自己的临时文件
 */
public class TempFile extends AbstractOperableFile<Path> {

    public TempFile(Path dir, String prefix, String suffix) throws IOException {
        super(Files.createTempFile(dir, prefix, suffix));
    }

    public TempFile(String prefix, String suffix) throws IOException {
        this(null, prefix, suffix);
    }

    @Override
    protected void internalClose() throws IOException {
        if(file != null){
            Files.deleteIfExists(file);
        }
    }

}
