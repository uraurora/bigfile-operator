package core.buffer.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : gaoxiaodong04
 * @program : bigfile-operator
 * @date : 2021-03-02 20:19
 * @description :
 */
@Data
@AllArgsConstructor
public class WrapperBytes {
    /**
     * byte数组
     */
    private final byte[] array;

    /**
     * 起始数据在数组中的位置
     */
    private final int offset;

    /**
     * 长度
     */
    private final int length;

}
