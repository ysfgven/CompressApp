package core;

public class HuffmanNode implements Comparable<HuffmanNode>{

    private final byte symbol;
    private final int frequency;
    private HuffmanNode left;
    private HuffmanNode right;

    public HuffmanNode(byte symbol, int combinedFreq, HuffmanNode left, HuffmanNode right) {
        this.symbol = symbol;
        frequency = combinedFreq;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf(){
        return right ==null && left == null;
    }

    public HuffmanNode(int frequency, byte symbol) {
        this.frequency = frequency;
        this.symbol = symbol;
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return Integer.compare(this.frequency,o.frequency);
    }

    public byte getSymbol() {
        return symbol;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public int getFrequency() {
        return frequency;
    }
}
