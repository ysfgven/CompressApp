package core;

import model.CodeTable;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CodeTableGeneratorTest {

    private final CodeTableGenerator generator = new CodeTableGenerator();
    private final HuffmanTreeBuilder treeBuilder = new HuffmanTreeBuilder();

    @Test
    void generate_nullRoot_returnsEmptyTable() {
        CodeTable table = generator.generate(null);
        assertNull(table.getCode((byte)'A'));
    }

    @Test
    void generate_singleSymbolLeafRoot_assignsCodeZero() {
        HuffmanNode leaf = new HuffmanNode(5, (byte) 'A');
        CodeTable table = generator.generate(leaf);
        assertEquals("0", table.getCode((byte)'A'));
    }

    @Test
    void generate_allSymbolsReceiveNonNullCodes() {
        Map<Byte, Integer> freqMap = new HashMap<>();
        byte[] symbols = {'A','B', 'C', 'D'};
        for (int i = 0; i < symbols.length; i++) {
            freqMap.put((byte)symbols[i], i + 1);
        }
        HuffmanNode root = treeBuilder.build(freqMap);
        CodeTable table = generator.generate(root);

        for (byte s : symbols) {
            assertNotNull(table.getCode(s),(char) s + " code didnt generated for that");
        }
    }

    @Test
    void generate_codes_arePrefixFree() {
        byte[] symbols = {'A','B','C','D','E'};
        int[] freqs = {10,7,5,3,1};
        Map<Byte, Integer> freqMap = new HashMap<>();
        for (int i = 0; i < symbols.length; i++) {
            freqMap.put((byte)symbols[i], freqs[i]);
        }
        HuffmanNode root =treeBuilder.build(freqMap);
        CodeTable table = generator.generate(root);

        String[] codes = new String[symbols.length];

        for (int i = 0; i < symbols.length; i++) {
            codes[i] = table.getCode((byte)symbols[i]);
        }

        for (int i = 0; i < codes.length; i++) {
            for (int j = 0; j < codes.length; j++) {
                if(i==j)
                    continue;
                assertFalse(codes[j].startsWith(codes[i]), "'" + " 'The code of ' "+codes[i] + "' must not be a prefix of the code of '" + codes[j]);
            }
        }
    }

    @Test
    void generate_higherFrequencySymbol_getsEqualOrShorterCode() {
        Map<Byte, Integer> freqMap = new HashMap<>();
        freqMap.put((byte)'A',100);
        freqMap.put((byte)'B',10);
        freqMap.put((byte)'C',1);
        HuffmanNode root = treeBuilder.build(freqMap);
        CodeTable table = generator.generate(root);

        int lenA = table.getCode((byte)'A').length();
        int lenC = table.getCode((byte)'C').length();

        assertTrue(lenA<= lenC,"'A' (" + lenA + " bit) must be as short as or shorter than 'C' (" + lenC + " bit)");
    }
}