package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FrequencyAnalyzer {

    public Map<Byte,Integer> analyze(byte[] data){
       Map<Byte,Integer> freqMap = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            freqMap.put(data[i],freqMap.getOrDefault(data[i],0)+1);

        }
        return freqMap;
    }

    public Map<Byte,Integer> analyze(InputStream is) throws IOException {
        Map<Byte,Integer> freqMap = new HashMap<>();
        int data;

        while((data = is.read()) != -1 ){
            byte b = (byte)data;
            freqMap.put(b, freqMap.getOrDefault(b,0)+1);
        }
        return freqMap;
    }
}
