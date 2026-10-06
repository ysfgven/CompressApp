package bitio;

import exception.DecompressionException;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class BitReaderTest {

    @Test
    void readBit_0xAA_returnsAlternatingBits() throws IOException {
        BitReader br = new BitReader(new ByteArrayInputStream(new byte[]{(byte)0xAA}));
        int[] expected = {1, 0, 1, 0, 1, 0, 1, 0};
        for (int bit : expected) {
            assertEquals(bit, br.readBit());
        }
    }

    @Test
    void readBit_0xFF_returnsAllOnes() throws IOException {
        BitReader br = new BitReader(new ByteArrayInputStream(new byte[]{(byte)0xFF}));
        for (int i = 0; i < 8; i++) {
            assertEquals(1, br.readBit());
        }
    }

    @Test
    void readBit_0x00_returnsAllZeros() throws IOException {
        BitReader br = new BitReader(new ByteArrayInputStream(new byte[]{0x00}));
        for (int i = 0; i < 8; i++) {
            assertEquals(0, br.readBit());
        }
    }

    @Test
    void readBit_MSB_First() throws IOException {
        BitReader br = new BitReader(new ByteArrayInputStream(new byte[]{(byte) 0b10000000}));
        assertEquals(1, br.readBit());
    }

    @Test
    void readBit_streamExhausted_ThrowsDecompressionException() throws IOException {
        BitReader br = new BitReader(new ByteArrayInputStream(new byte[]{0x00}));
        for (int i = 0; i < 8; i++) {
            br.readBit();
        }
        assertThrows(DecompressionException.class,br::readBit);
    }

    @Test
    void readBit_emptyStream_throwsDecompressionException() {
        BitReader br = new BitReader(new ByteArrayInputStream(new byte[0]));
        assertThrows(DecompressionException.class, br::readBit);
    }

    @Test
    void readBit_twoBytes_readsSixteenBitsInOrder() throws IOException {
        BitReader br = new BitReader(new ByteArrayInputStream(new byte[]{(byte)0xAA, (byte)0x55}));
        int[] expected = {1,0,1,0,1,0,1,0,   0,1,0,1,0,1,0,1};
        for(int bit : expected) {
            assertEquals(bit, br.readBit());
        }
    }

    @Test
    void readBit_lazyLoading_readsOnDemand() throws IOException {
        BitReader br = new BitReader(new ByteArrayInputStream(new byte[]{(byte)0xB4}));
        assertEquals(1, br.readBit());
        assertEquals(0, br.readBit());
    }


    @Test
    void roundTrip_writtenBitsAreReadBackCorrectly() throws IOException {
        String bits = "11001010110";
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits(bits);
        int padding = bw.flush();

        BitReader br = new BitReader(new ByteArrayInputStream(baos.toByteArray()));
        int totalBits = (baos.size()* 8) - padding;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < totalBits;i++) {
            result.append(br.readBit());
        }
        assertEquals(bits, result.toString());
    }

    @Test
    void roundTrip_256Bytes_allBitsCorrect() throws IOException {
        byte[] source = new byte[256];
        for (int i = 0; i< 256; i++) source[i] = (byte)i;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        for (byte b : source) {
            for (int bit = 7; bit >= 0; bit--) {
                bw.writeBit((b >> bit) & 1);
            }
        }
        bw.flush();

        BitReader br = new BitReader(new ByteArrayInputStream(baos.toByteArray()));
        for (byte b : source) {
            int reconstructed = 0;
            for (int bit = 7; bit >= 0; bit--) {
                reconstructed |= (br.readBit() << bit);
            }

            assertEquals(b & 0xFF, reconstructed);
        }
    }

    @Test
    void roundTrip_paddingBitsNotRead() throws IOException {
        String bits = "10110";
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        bw.writeBits(bits);
        int padding =bw.flush();

        assertEquals(3, padding);


        int totalBits = (baos.size() * 8) - padding;
        BitReader br = new BitReader(new ByteArrayInputStream(baos.toByteArray()));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < totalBits; i++) {
            result.append(br.readBit());
        }

        assertEquals(bits, result.toString());
    }
}
