package constant.enums;

import lombok.Getter;

import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import static java.nio.file.StandardOpenOption.*;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-02-24 10:58
 * @description :
 */
@Getter
public enum FileMode {

    /**
     * 只读
     */
    READ_ONLY(EnumSet.of(READ), FileChannel.MapMode.READ_ONLY, "r"),

    /**
     * 读写
     */
    READ_WRITE(EnumSet.of(WRITE, READ), FileChannel.MapMode.READ_WRITE, "rwd"),
    /**
     * 仅仅限于缓存buffer的读写，不会影响文件
     */
    COPY_ON_WRITE(EnumSet.of(WRITE, READ), FileChannel.MapMode.PRIVATE, "rwd")
    ;

    private final EnumSet<StandardOpenOption> openOption;

    private final FileChannel.MapMode mapMode;

    private final String fileMode;

    FileMode(EnumSet<StandardOpenOption> openOption, FileChannel.MapMode mapMode, String fileMode) {
        this.openOption = openOption;
        this.mapMode = mapMode;
        this.fileMode = fileMode;
    }
}
