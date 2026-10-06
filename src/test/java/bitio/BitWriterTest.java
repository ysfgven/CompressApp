package bitio;


import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BitWriterTest {


    @Test
    void flush_noBitsWritten_returnsZeroAndNoOutput() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        int padding = bw.flush();
        assertEquals(0,padding);
        assertEquals(0,baos.size());
    }

    @Test
    void flush_exactlyEightBits_returnsZeroPadding() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("10110100");
        int padding = bw.flush();
        assertEquals(0, padding);
        assertEquals(1, baos.size());
    }

    @Test
    void flush_oneBitWritten_returnsSeven() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBit(1);
        int padding = bw.flush();
        assertEquals(7, padding);
        assertEquals(1, baos.size());
    }

    @Test
    void flush_sevenBitsWritten_returnsOne() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("1011010");
        int padding = bw.flush();
        assertEquals(1, padding);
        assertEquals(1, baos.size());
    }

    @Test
    void flush_paddingBitsAreZero() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("10110");
        bw.flush();
        assertEquals(0xB0, baos.toByteArray()[0] & 0xFF);
    }


    @Test
    void writeBit_fullByte_autoFlushedToStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("10110100"); // 8 bit
        assertEquals(1, baos.size());
        assertEquals(0xB4, baos.toByteArray()[0] & 0xFF);
    }

    @Test
    void writeBit_singleOne_isMSB() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBit(1);
        bw.flush();
        assertEquals(0b10000000, baos.toByteArray()[0] & 0xFF);
    }

    @Test
    void writeBit_allZeros_producesZeroByte() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("00000000");
        bw.flush();
        assertEquals(0x00, baos.toByteArray()[0] & 0xFF);
    }

    @Test
    void writeBit_allOnes_producesFFByte() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("11111111");
        bw.flush();
        assertEquals(0xFF, baos.toByteArray()[0] & 0xFF);
    }


    @Test
    void writeBits_emptyString_doesNothing() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("");
        int padding = bw.flush();
        assertEquals(0,padding);
        assertEquals(0, baos.size());
    }

    @Test
    void writeBits_sixteenBits_producesTwoBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("1010101001010101");
        int padding = bw.flush();
        assertEquals(0,padding);
        assertEquals(2,baos.size());
        assertEquals(0xAA,baos.toByteArray()[0] & 0xFF); //10101010
        assertEquals(0x55,baos.toByteArray()[1] & 0xFF); //01010101
    }

    @Test
    void writeBits_multipleCallsMergeCorrectly() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits("1010");
        bw.writeBits("1010");
        bw.flush();
        //10101010=0xAA
        assertEquals(0xAA, baos.toByteArray()[0] & 0xFF);
    }

    @Test
    void writeBit_onlyLSBIsUsed() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBit(0xFF);
        bw.flush();
        assertEquals(0b10000000,baos.toByteArray()[0] & 0xFF);
    }
}