package core;
import bitio.BitWriter;
import model.CodeTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class HuffmanRoundTripTest {

    private byte[] compress(byte[] data) throws IOException {
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : data) freq.merge(b, 1, Integer::sum);

        HuffmanNode root= new HuffmanTreeBuilder().build(freq);
        CodeTable table = new CodeTableGenerator().generate(root);
        HuffmanEncoder enc = new HuffmanEncoder(table);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        enc.encodeChunk(data, bw);
        int padding = bw.flush();

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        result.write(padding);
        result.write(baos.toByteArray());
        return result.toByteArray();
    }

    private byte[] decompress(byte[] compressed, Map<Byte, Integer> freq) throws IOException {
        int padding = compressed[0] & 0xFF;
        byte[] data = new byte[compressed.length-1];
        System.arraycopy(compressed, 1, data, 0,data.length);

        HuffmanNode root = new HuffmanTreeBuilder().build(freq);
        HuffmanDecoder dec = new HuffmanDecoder(root);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        dec.decode(new ByteArrayInputStream(data), out, padding,data.length);
        return out.toByteArray();
    }

    @ParameterizedTest
    @ValueSource(strings ={"a", "aaaa", "hello", "hello world", "tired of this project haha just kidding", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
    })
    void strings_roundTrip(String input) throws IOException {
        byte[] original = input.getBytes();
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : original) freq.merge(b, 1, Integer::sum);

        byte[] compressed = compress(original);
        byte[] decompressed = decompress(compressed, freq);

        assertArrayEquals(original, decompressed);
    }

    @Test
    void randomData_roundTrip() throws IOException {
        byte[] original = new byte[1000];
        new java.util.Random(42).nextBytes(original);
        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : original) freq.merge(b, 1, Integer::sum);

        byte[] compressed= compress(original);
        byte[] decompressed = decompress(compressed, freq);

        assertArrayEquals(original, decompressed);
    }

    @Test
    void repetitiveData_compressesSmaller() throws IOException {
        byte[] original = new byte[1000];
        java.util.Arrays.fill(original, (byte)'A');

        Map<Byte, Integer> freq = Map.of((byte)'A', 1000);
        HuffmanNode root = new HuffmanTreeBuilder().build(freq);
        CodeTable table = new CodeTableGenerator().generate(root);
        HuffmanEncoder enc = new HuffmanEncoder(table);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        enc.encodeChunk(original, bw);
        bw.flush();

        assertTrue(baos.size() < original.length);
    }
}