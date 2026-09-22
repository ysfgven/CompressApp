package core;

import bitio.BitWriter;
import exception.CompressionException;
import model.CodeTable;
import java.io.IOException;



public class HuffmanEncoder {

    private final CodeTable codeTable;
    public HuffmanEncoder(CodeTable codeTable) {
        this.codeTable = codeTable;
    }

    public void encodeChunk(byte[] rawData, BitWriter bitWriter) throws IOException {
        if (rawData == null||rawData.length == 0) {
            return;
        }
        for (byte b : rawData) {
            String code = codeTable.getCode(b);
            if (code == null) {
                throw new CompressionException("No code for that byte: " + (b & 0xFF));
            }
            bitWriter.writeBits(code);
        }
    }
}
