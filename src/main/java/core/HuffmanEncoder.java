package core;

import model.CodeTable;


public class HuffmanEncoder {

    private final CodeTable codeTable;



    public HuffmanEncoder(CodeTable codeTable) {
        this.codeTable = codeTable;
    }

    public byte[] encode(byte[] rawData){
        if (rawData == null || rawData.length == 0) {
            return new byte[0];
    }
        //todo:will code later
        return null;

    }



}
