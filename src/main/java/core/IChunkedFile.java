package core;


import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author gaoxiaodong
 */
public interface IChunkedFile {

    long size();

    String fileName();

    BasicFileAttributes basicFileAttributes();

    // long index();

}
