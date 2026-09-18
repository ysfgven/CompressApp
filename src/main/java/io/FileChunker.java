package io;

import exception.CompressionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class FileChunker implements Iterable<byte[]> {

    private File file;
    private int chunkSize;

    public FileChunker(File file, int chunkSize) {
        this.file = file;
        this.chunkSize = chunkSize;
    }

    @Override
    public Iterator<byte[]> iterator() {
        return new ChunkIterator();
    }

    private class ChunkIterator implements Iterator<byte[]> {
        private FileInputStream fis;
        private byte[] nextChunk;

        public ChunkIterator() {
            try {
                fis = new FileInputStream(file);
                nextChunk = readNextChunk();
            } catch (FileNotFoundException e) {
                throw new CompressionException("File not found !",e);
            }
        }

        @Override
        public boolean hasNext() {
            return nextChunk != null;
        }

        @Override
        public byte[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            byte[] current = nextChunk;
            nextChunk = readNextChunk();
            if (nextChunk == null) {
                try{
                    fis.close();
                }
                catch (IOException ignored) {

                }
            }

            return current;
        }

        private byte[] readNextChunk() {
            byte[] buffer = new byte[chunkSize];
            int totalRead = 0;
            int bytesRead;

            try {
                while (totalRead < chunkSize && (bytesRead = fis.read(buffer, totalRead, chunkSize - totalRead)) != -1) {
                    totalRead += bytesRead;
                }
            }catch (IOException e){
                throw new CompressionException("An error occurred while reading chunks! ",e);
            }

            return totalRead == 0 ? null : Arrays.copyOf(buffer, totalRead);
        }
    }
}
