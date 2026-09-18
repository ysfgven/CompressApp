package format;

import java.util.Map;

public class FileHeader {

    public static final byte[] MAGIC_NUMBER = {0x48, 0x55, 0x46, 0x46};
    public static final short CURRENT_VERSION = 1;

    private byte[] magicNum = MAGIC_NUMBER;//huff
    private short version = CURRENT_VERSION;
    private long originalFileSize;
    private String originalFileName;
    private Map<Byte,Integer> frequencyTable;
    private int paddingBits;
    private long compressedDataSize;

    public short getVersion() {
        return version;
    }

    public byte[] getMagicNum() {
        return magicNum;
    }

    public long getOriginalFileSize() {
        return originalFileSize;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public Map<Byte, Integer> getFrequencyTable() {
        return frequencyTable;
    }

    public int getPaddingBits() {
        return paddingBits;
    }

    public long getCompressedDataSize() {
        return compressedDataSize;
    }
    public int getOriginalFileNameLenght(){
        return originalFileName.length();
    }

    public void setMagicNum(byte[] magicNum) {
        this.magicNum = magicNum;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public void setOriginalFileSize(long originalFileSize) {
        this.originalFileSize = originalFileSize;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setFrequencyTable(Map<Byte, Integer> frequencyTable) {
        this.frequencyTable = frequencyTable;
    }

    public void setPaddingBits(int paddingBits) {
        this.paddingBits = paddingBits;
    }

    public void setCompressedDataSize(long compressedDataSize) {
        this.compressedDataSize = compressedDataSize;
    }
}
