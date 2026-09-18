package codec;

import core.HuffmanDecoder;
import core.HuffmanEncoder;
import core.HuffmanNode;
import model.CodeTable;

public class HuffmanCodec implements Codec{


    @Override
    public byte[] compress(byte[] rawData, CodeTable codeTable) {

        HuffmanEncoder huffmanEncoder = new HuffmanEncoder(codeTable);
        return huffmanEncoder.encode(rawData);
    }

    @Override
    public byte[] decompress(byte[] compressedData, HuffmanNode treeRoot,int paddingBits) {
        HuffmanDecoder huffmanDecoder = new HuffmanDecoder(treeRoot);
        return huffmanDecoder.decode(compressedData,paddingBits);
    }
}
