package core;

import bitio.BitWriter;
import exception.DecompressionException;
import model.CodeTable;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class HuffmanDecoderTest {
    private record Encoded(byte[] data, int paddingBits) {}

    private Encoded encode(byte[] raw, Map<Byte, Integer> freq) throws IOException {
        HuffmanNode root = new HuffmanTreeBuilder().build(freq);
        CodeTable table = new CodeTableGenerator().generate(root);
        HuffmanEncoder enc = new HuffmanEncoder(table);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        enc.encodeChunk(raw, bw);
        int padding = bw.flush();
        return new Encoded(baos.toByteArray(), padding);
    }

    private HuffmanDecoder buildDecoder(Map<Byte, Integer> freq) {
        HuffmanNode root = new HuffmanTreeBuilder().build(freq);
        return new HuffmanDecoder(root);
    }

    @Test
    void zeroBytesCompressed_writesNothing() throws IOException {
        HuffmanDecoder decoder = buildDecoder(Map.of((byte)'A', 1));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decoder.decode(new ByteArrayInputStream(new byte[0]), out, 0, 0);
        assertEquals(0, out.size());
    }

    @Test
    void singleSymbol_roundTrip() throws IOException {
        byte[] original = {(byte)'A',(byte)'A',(byte)'A'};
        Map<Byte, Integer> freq = Map.of((byte)'A', 3);

        Encoded encoded = encode(original, freq);
        HuffmanDecoder decoder = buildDecoder(freq);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decoder.decode(new ByteArrayInputStream(encoded.data()),out, encoded.paddingBits(), encoded.data().length);
        assertArrayEquals(original,out.toByteArray());
    }

    @Test
    void multiSymbol_roundTrip() throws IOException {
        byte[] original = "hello world".getBytes();
        Map<Byte, Integer> freq = new java.util.HashMap<>();
        for (byte b : original) freq.merge(b, 1, Integer::sum);

        Encoded encoded = encode(original, freq);
        HuffmanDecoder decoder = buildDecoder(freq);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decoder.decode(new ByteArrayInputStream(encoded.data()), out, encoded.paddingBits(),encoded.data().length);
        assertArrayEquals(original, out.toByteArray());
    }

    @Test
    void binaryData_roundTrip() throws IOException {
        byte[] original = new byte[256];
        for (int i = 0; i < 256; i++) original[i] = (byte) i;
        Map<Byte, Integer> freq = new java.util.HashMap<>();
        for (byte b : original) freq.merge(b, 1, Integer::sum);

        Encoded encoded = encode(original, freq);
        HuffmanDecoder decoder = buildDecoder(freq);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decoder.decode(
                new ByteArrayInputStream(encoded.data()),
                out,
                encoded.paddingBits(),
                encoded.data().length
        );
        assertArrayEquals(original, out.toByteArray());
    }

    @Test
    void corruptedStream_throwsDecompressionException() {
        Map<Byte,Integer> freq = Map.of((byte)'A',3,(byte)'B',5);
        HuffmanDecoder decoder = buildDecoder(freq);

        ByteArrayInputStream corruptedInput = new ByteArrayInputStream(new byte[]{0x00});
        ByteArrayOutputStream out =new ByteArrayOutputStream();

        assertThrows(DecompressionException.class,() ->decoder.decode(corruptedInput, out,0, 100)
        );
    }
}