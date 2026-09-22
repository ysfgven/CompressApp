package bitio;

import exception.DecompressionException;

import java.io.IOException;
import java.io.InputStream;

public class BitReader {
    private final InputStream in;
    private int currentByte;
    private int bitIndex = 8;

    public BitReader(InputStream in) {
        this.in = in;
    }


    public int readBit() throws IOException {
        if (bitIndex == 8) {
            int read = in.read();
            if (read == -1) {
                throw new DecompressionException("Compressed stream ended unexpectedly");
            }
            currentByte = read;
            bitIndex = 0;
        }
        return (currentByte >> (7-bitIndex++))&1;
    }

}
