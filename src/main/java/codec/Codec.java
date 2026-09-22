package codec;

import bitio.BitWriter;
import core.HuffmanNode;
import model.CodeTable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Codec {
    void encodeChunk(byte[] rawData, CodeTable codeTable, BitWriter bitWriter) throws IOException;

    void decompress(InputStream in, OutputStream out, HuffmanNode treeRoot, int paddingBits, long compressedBytes) throws IOException;
}
