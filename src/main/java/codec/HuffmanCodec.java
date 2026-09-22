package codec;

import bitio.BitWriter;
import core.HuffmanDecoder;
import core.HuffmanEncoder;
import core.HuffmanNode;
import model.CodeTable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HuffmanCodec implements Codec{


    @Override
    public void encodeChunk(byte[] rawData, CodeTable codeTable,BitWriter bitWriter) throws IOException {
        new HuffmanEncoder(codeTable).encodeChunk(rawData, bitWriter);
    }

    @Override
    public void decompress(InputStream in, OutputStream out,HuffmanNode treeRoot, int paddingBits,long compressedBytes) throws IOException {
        new HuffmanDecoder(treeRoot).decode(in, out, paddingBits, compressedBytes);
    }
}
