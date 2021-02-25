package constant.enums;

import lombok.Getter;

import java.nio.channels.FileChannel;

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
    READ_ONLY("r", FileChannel.MapMode.READ_ONLY),

    /**
     * 读写
     */
    READ_WRITE("rwd", FileChannel.MapMode.READ_WRITE),
    /**
     * 仅仅限于缓存buffer的读写，不会影响文件
     */
    COPY_ON_WRITE("r", FileChannel.MapMode.PRIVATE)
    ;

    private final String fileMode;

    private final FileChannel.MapMode mapMode;

    FileMode(String fileMode, FileChannel.MapMode mapMode) {
        this.fileMode = fileMode;
        this.mapMode = mapMode;
    }
}
