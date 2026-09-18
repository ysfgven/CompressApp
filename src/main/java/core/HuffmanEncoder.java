package core;

import bitio.BitWriter;
import model.CodeTable;


public class HuffmanEncoder {

    private final CodeTable codeTable;



    public HuffmanEncoder(CodeTable codeTable) {
        this.codeTable = codeTable;
    }

    public byte[] encode(byte[] rawData){

        BitWriter bitWriter = new BitWriter();
        if (rawData == null || rawData.length == 0) {
            return new byte[0];
        }


        for (int i = 0; i < rawData.length; i++) {
            String code = codeTable.getCode(rawData[i]);
            bitWriter.writeBits(code);

        }
        bitWriter.flush();
        return bitWriter.toByteArray();

    }



}
