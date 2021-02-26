package core.buffer;

import constant.enums.FileMode;
import core.buffer.impl.MappedByteBufferConfig;
import core.buffer.impl.MappedByteBufferHolder;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MappedByteBufferHolderTest {
    @Test
    public void test() throws IOException {
        Path path = Paths.get("/Users/gaoxiaodong/data/file的副本.txt");
        final long size = Files.size(path);
        System.out.println("size=" + size);
        try(final MappedByteBufferHolder holder = MappedByteBufferHolder.of(MappedByteBufferConfig.builder()
                .withPath(path)
                .withFileMode(FileMode.READ_WRITE)
                .withSize(size)
                .build())){
            holder.operate(b->{
                System.out.println(b.isReadOnly());
                System.out.println("cap=" + b.capacity());
                System.out.println("limit=" + b.limit());
                System.out.println("pos=" + b.position());
                b.position(0);
            });
        }

    }

}