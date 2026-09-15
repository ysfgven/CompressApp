package model;

import java.util.Map;

public class CodeTable {
    private final Map<Byte,String> table;

    public CodeTable(Map<Byte, String> table) {
        this.table = table;
    }
    public String getCode(byte symbol){
        return table.get(symbol);
    }
    public double getAverageBitLength(Map<Byte,Integer> freqTable){

        long totalBits = 0;
        long totalSymbol = 0;
        if(freqTable.isEmpty() ||table.isEmpty() ) return 0.0;

        for(Map.Entry<Byte,String> entry: table.entrySet()){
            byte symbol = entry.getKey();
            String code = entry.getValue();
            int freq = freqTable.getOrDefault(symbol,0);

            totalBits += (long)code.length()*freq;
            totalSymbol += freq;


        }
        return totalBits == 0 ? 0.0 : (double) totalBits/totalSymbol;

    }
}
