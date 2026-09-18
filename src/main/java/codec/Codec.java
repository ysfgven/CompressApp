package codec;

import core.HuffmanNode;
import model.CodeTable;

public interface Codec {
    byte[] compress(byte[] rawData, CodeTable codeTable);
    byte[] decompress(byte[] compressedData, HuffmanNode treeRoot,int paddingBits);
}
