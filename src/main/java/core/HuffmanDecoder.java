package core;

import bitio.BitReader;
import exception.DecompressionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HuffmanDecoder {
    private final HuffmanNode root;

    public HuffmanDecoder(HuffmanNode root) {
        this.root = root;
    }

    public void decode(InputStream in, OutputStream out,int paddingBits,long compressedBytes) throws IOException {

        if (compressedBytes == 0) {
            return;
        }

        BitReader br = new BitReader(in);
        long totalBits = (compressedBytes * 8L) - paddingBits;
        if (root.isLeaf()) {
            for (long i = 0; i < totalBits; i++) {
                br.readBit();
                out.write(root.getSymbol() & 0xFF);
            }
            return;
        }
        HuffmanNode current = root;
        for (long i = 0; i < totalBits; i++) {
            int bit = br.readBit();
            current = (bit == 0) ? current.getLeft() : current.getRight();
            if (current == null) {
                throw new DecompressionException("Error: null node" + i);
            }
            if (current.isLeaf()) {
                out.write(current.getSymbol() & 0xFF);
                current = root;
            }
        }
    }
}
