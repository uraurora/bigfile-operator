package core.file.impl;

import core.chunk.IChunkedFile;
import core.file.IBigFile;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static util.Options.listOf;

public class LocalBigFileTest {

    @Test
    public void iterator() throws IOException {
        Path path = Paths.get("/Users/gaoxiaodong/data/file的副本.txt");
        LocalBigFile bigFile = new LocalBigFile(path);
        bigFile.setChunkedSize(1024);
        bigFile.forEach(k->{
            System.out.println(Arrays.toString(k.toByteArray()));
        });
        final IChunkedFile chunkedFile = bigFile.readChunk();
    }

    @Test
    public void size() {
    }

    @Test
    public void readChunkTest() throws IOException {
        Path path = Paths.get("/Users/gaoxiaodong/data/file的副本.txt");
        try(LocalBigFile bigFile = new LocalBigFile(path);) {
            bigFile.setChunkedSize(1024);
            List<IChunkedFile> l = bigFile.stream()
                    .collect(Collectors.toList());
            System.out.println(l.size());
            for (int i = 1; i <= l.size(); i++) {
                assertArrayEquals(l.get(i-1).toByteArray(), bigFile.readChunk(i).toByteArray());
            }
        }

    }
}