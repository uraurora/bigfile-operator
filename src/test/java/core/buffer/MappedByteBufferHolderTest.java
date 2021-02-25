package core.buffer;

import constant.enums.FileMode;
import org.junit.Test;

import java.io.IOException;
import java.nio.MappedByteBuffer;

import static org.junit.Assert.*;

public class MappedByteBufferHolderTest {
    @Test
    public void test() throws IOException {

        try(final MappedByteBufferHolder holder = MappedByteBufferHolder.builder()
                .withPath("")
                .withFileMode(FileMode.READ_ONLY)
                .build()){
            holder.operate(b->{
                b.load();

            });
        }

    }

}