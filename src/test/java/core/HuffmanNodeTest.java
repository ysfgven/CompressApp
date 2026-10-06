package core;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class HuffmanNodeTest {

    @Test
    void nodeWithNoChildren_isLeaf() {
        HuffmanNode node = new HuffmanNode(5, (byte)0x41);
        assertTrue(node.isLeaf());

    }

    @Test
    void nodeWithChildren_isNotLeaf() {
        HuffmanNode left = new HuffmanNode(3, (byte)0x41);
        HuffmanNode right = new HuffmanNode(2, (byte)0x42);
        HuffmanNode parent = new HuffmanNode((byte) 0, 5, left, right);

        assertFalse(parent.isLeaf());
    }

    @Test
    void lowerFrequency_comparesLess() {
        HuffmanNode low = new HuffmanNode(1,(byte)0x41);
        HuffmanNode high = new HuffmanNode(9,(byte)0x42);

        assertTrue(low.compareTo(high) < 0);

        assertTrue(high.compareTo(low) >0);
    }

    @Test
    void equalFrequency_comparesToZero() {
        HuffmanNode a = new HuffmanNode(5,(byte) 0x41);
        HuffmanNode b = new HuffmanNode(5, (byte) 0x42);

        assertEquals(0, a.compareTo(b));
    }
    @Test
    void getters_returnCorrectValues() {
        HuffmanNode left = new HuffmanNode(1, (byte)0x41);
        HuffmanNode right = new HuffmanNode(2,(byte)0x42);
        HuffmanNode parent = new HuffmanNode((byte)0x00,3,left,right);

        assertEquals(3, parent.getFrequency());
        assertSame(left,parent.getLeft());
        assertSame(right, parent.getRight());
    }
}