package core;

import bitio.BitReader;

import java.io.ByteArrayOutputStream;

public class HuffmanDecoder {
    private HuffmanNode root;

    public HuffmanDecoder(HuffmanNode root) {
        this.root = root;
    }

    public byte[] decode(byte[] compressedData, int paddingBits) {
        BitReader bitReader = new BitReader(compressedData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HuffmanNode current = root;

        int totalBits = (compressedData.length * 8) - paddingBits;

        for (int i = 0; i < totalBits; i++) {
            int bit = bitReader.readBit();
            if (bit == 0) {
                current = current.getLeft();
            }else{
                current = current.getRight();
            }

            if (current.isLeaf()) {
                out.write(current.getSymbol());
                current = root;
            }
        }
        return out.toByteArray();
    }

}
