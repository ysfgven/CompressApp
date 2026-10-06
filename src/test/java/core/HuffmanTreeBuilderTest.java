package core;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class HuffmanTreeBuilderTest {

    private final HuffmanTreeBuilder builder = new HuffmanTreeBuilder();

    @Test
    void build_emptyMap_returnsNull() {
        HuffmanNode root = builder.build(new HashMap<>());
        assertNull(root);
    }

    @Test
    void build_singleSymbol_returnsLeafWithCorrectValues() {
        Map<Byte, Integer> freqMap = new HashMap<>();
        freqMap.put((byte) 'A',7);
        HuffmanNode root = builder.build(freqMap);

        assertTrue(root.isLeaf());
        assertEquals((byte)'A',root.getSymbol());
        assertEquals(7, root.getFrequency());
    }

    @Test
    void build_multipleSymbols_rootIsNotLeaf() {
        Map<Byte, Integer> freqMap = new HashMap<>();
        freqMap.put((byte) 'A', 5);
        freqMap.put((byte) 'B', 3);

        HuffmanNode root = builder.build(freqMap);


        assertFalse(root.isLeaf());
        assertNotNull(root.getLeft());
        assertNotNull(root.getRight());
    }

    @Test
    void build_multipleSymbols_rootFrequencyEqualsTotalSum() {
        Map<Byte,Integer> freqMap = new HashMap<>();
        freqMap.put((byte)'A', 10);
        freqMap.put((byte)'B', 3);
        freqMap.put((byte)'C', 5);
        freqMap.put((byte)'D', 2);

        HuffmanNode root = builder.build(freqMap);

        assertEquals(20,root.getFrequency());//10+3+5+2
    }

    @Test
    void build_twoSymbols_lowerFrequencyIsOnLeft() {
        Map<Byte, Integer> freqMap = new HashMap<>();
        freqMap.put((byte)'A',1);
        freqMap.put((byte)'B',9);

        HuffmanNode root = builder.build(freqMap);


        assertEquals(root.getFrequency(),root.getLeft().getFrequency()+root.getRight().getFrequency());
    }
}