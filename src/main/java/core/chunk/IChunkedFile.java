package core.chunk;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author gaoxiaodong
 */
public interface IChunkedFile {

    long size();

    String fileName();

    BasicFileAttributes basicFileAttributes();

    long index();

    OutputStream toOutputStream() throws IOException;

    default void flush(Path path) throws IOException {

    }

}
