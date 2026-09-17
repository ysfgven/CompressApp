package bitio;

import java.io.ByteArrayOutputStream;

public class BitWriter {
    private ByteArrayOutputStream buffer;
    private int currentByte;
    int bitCount;


    public void writeBit(int bit){
       currentByte= currentByte<<1;
       currentByte = currentByte|bit;
       bitCount++;
       if(bitCount==8){
           buffer.write(currentByte & 0xFF);
           currentByte =0;
           bitCount = 0;
       }
    }
    public void writeBits(String bits){
        for(int i =0;i<bits.length();i++){
            writeBit(bits.charAt(i)-'0');
        }

    }

    public int flush(){
        if(bitCount == 0)
            return 0;

        int emptyCount = 8- bitCount;
        currentByte = currentByte << emptyCount;
        buffer.write(currentByte & 0xFF);
        return emptyCount;

    }
    public byte[] toByteArray(){
        return buffer.toByteArray();

    }
}
