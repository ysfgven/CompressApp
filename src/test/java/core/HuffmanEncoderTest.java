package core;
import bitio.BitWriter;
import exception.CompressionException;
import model.CodeTable;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class HuffmanEncoderTest {

    private HuffmanEncoder buildEncoder(Map<Byte, Integer> freq) {
        HuffmanNode root = new HuffmanTreeBuilder().build(freq);
        CodeTable table = new CodeTableGenerator().generate(root);
        return new HuffmanEncoder(table);
    }

    @Test
    void nullInput_writesNothing() throws IOException {
        HuffmanEncoder encoder = buildEncoder(Map.of((byte) 'A', 1));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        encoder.encodeChunk(null, bw);
        bw.flush();
        assertEquals(0, baos.size());
    }

    @Test
    void emptyArray_writesNothing() throws IOException {
        HuffmanEncoder encoder = buildEncoder(Map.of((byte)'A', 1));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);
        encoder.encodeChunk(new byte[0], bw);
        bw.flush();
        assertEquals(0, baos.size());
    }

    @Test
    void singleSymbol_producesOutput() throws IOException {
        byte[] data = {(byte)'A', (byte)'A', (byte)'A'};
        HuffmanEncoder encoder = buildEncoder(Map.of((byte)'A', 3));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw =new BitWriter(baos);
        encoder.encodeChunk(data, bw);
        bw.flush();
        assertTrue(baos.size() > 0);
    }

    @Test
    void unknownByte_throwsCompressionException() {

        HuffmanNode root = new HuffmanTreeBuilder().build(Map.of((byte)'A', 1));
        CodeTable table = new CodeTableGenerator().generate(root);
        HuffmanEncoder encoder = new HuffmanEncoder(table);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitWriter bw = new BitWriter(baos);

        assertThrows(CompressionException.class,()->encoder.encodeChunk(new byte[]{(byte) 'B'},bw)
        );
    }
}