package bitio;

import java.io.IOException;
import java.io.OutputStream;

public class BitWriter {
    private final OutputStream  out;
    private int currentByte;
    private int bitCount;


    public BitWriter(OutputStream out) {
        this.out = out;
    }

    public void writeBit(int bit) throws IOException {
        currentByte = (currentByte<<1)|(bit & 1);
        bitCount++;
        if (bitCount == 8) {
            out.write(currentByte & 0xFF);
            currentByte = 0;
            bitCount = 0;
        }
    }

    public void writeBits(String bits) throws IOException {
        for (int i = 0; i < bits.length(); i++) {
            writeBit(bits.charAt(i) - '0');
        }
    }

    public int flush() throws IOException {
        if(bitCount == 0){
            return 0;
        }

        int paddingBits = 8-bitCount;
        currentByte = currentByte << paddingBits;
        out.write(currentByte & 0xFF);
        bitCount = 0;
        return paddingBits;
    }
}
