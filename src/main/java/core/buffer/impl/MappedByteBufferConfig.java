package core.buffer.impl;

import constant.enums.FileMode;
import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-25 17:42
 * @description :
 */
@Builder(setterPrefix = "with")
@Getter
public class MappedByteBufferConfig {

    private final Path path;

    private final FileMode fileMode;

    private final long offset;

    private final long size;

}
