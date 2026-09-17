package bitio;

public class BitReader {
    private byte[] data;
    private int byteIndex;
    private int bitIndex;

    public int readBit(){

        int bit = (data[byteIndex] >> (7 - bitIndex)) & 1;
        bitIndex++;
        if(bitIndex ==8){
            byteIndex++;
            bitIndex = 0;
        }
        return bit;

    }

    public boolean hasMore(int paddingBits){
                //totalBit                    > bits done reading
        return ((data.length * 8)-paddingBits)>((byteIndex * 8) + bitIndex);
    }

}
