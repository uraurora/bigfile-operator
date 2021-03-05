package core.file.impl;

import core.chunk.IChunkedFile;
import core.file.IBigFile;
import core.file.ILocalBigFile;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static util.Options.listOf;
import static util.Options.rangeTo;

public class LocalBigFileTest {

    Path path = Paths.get("/Users/gaoxiaodong/data/file的副本.txt");

    @Test
    public void iterator() throws IOException {
        Path path = Paths.get("/Users/gaoxiaodong/data/file的副本.txt");
        LocalBigFile bigFile = new LocalBigFile(path, 1024);
        bigFile.forEach(k->{
            System.out.println(Arrays.toString(k.toByteArray()));
        });
    }

    @Test
    public void size() throws IOException {
        assertEquals(Files.size(path), new LocalBigFile(path).size().longValue());
    }

    @Test
    public void readChunkTest() throws IOException {
        try(ILocalBigFile bigFile = new LocalBigFile(path, 1024)) {
            List<byte[]> l = bigFile.stream()
                    .map(IChunkedFile::toByteArray)
                    .collect(Collectors.toList());
            System.out.println(l.size());
            for (int i = 1; i <= l.size(); i++) {
                assertArrayEquals(l.get(i-1), bigFile.getChunk(i).toByteArray());
            }
        }
    }

    @Test
    public void getChunk() throws IOException {
        try(ILocalBigFile bigFile = new LocalBigFile(path, 1024)) {
            List<IChunkedFile> l = bigFile.stream()
                    .collect(Collectors.toList());
            System.out.println(l.size());
            for (int i = 1; i <= l.size(); i++) {
                final IChunkedFile file = l.get(i - 1);
                final IChunkedFile chunk = bigFile.getChunk(i);
                assertArrayEquals(file.toByteArray(), chunk.toByteArray());
                assertEquals(file, chunk);
            }
        }
    }
}