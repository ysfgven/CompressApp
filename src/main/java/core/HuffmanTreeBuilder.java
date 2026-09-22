package core;

import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTreeBuilder {
    public HuffmanNode build(Map<Byte,Integer> freqTable){
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for(Map.Entry<Byte,Integer> entry : freqTable.entrySet()){
            byte symbol = entry.getKey();
            int freq = entry.getValue();
            priorityQueue.add(new HuffmanNode(freq,symbol));
        }
        while(priorityQueue.size()>1){
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            int combinedFreq = left.getFrequency()+right.getFrequency(); //todo:will check this later it might be potential npe
            HuffmanNode parent = new HuffmanNode((byte)0,combinedFreq,left,right);
            priorityQueue.add(parent);
        }
        return priorityQueue.poll();
    }
}
