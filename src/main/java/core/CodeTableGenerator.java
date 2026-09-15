package core;

import java.util.HashMap;
import java.util.Map;

public class CodeTableGenerator {


    public Map<Byte,String> generate(HuffmanNode root){
        Map<Byte,String> codeTable = new HashMap<>();
        if(root == null) return codeTable;
        if(root.isLeaf()){
            codeTable.put(root.getSymbol(),"0");
            return codeTable;
        }
        dfs(root,"",codeTable);
        return codeTable;
    }
    private void dfs(HuffmanNode node,String currentCode,Map<Byte,String> codeTable){
        if(node == null) return;
        if(node.isLeaf()){
            codeTable.put(node.getSymbol(),currentCode);
            return;
        }
        dfs(node.getLeft(),currentCode + "0",codeTable);
        dfs(node.getRight(),currentCode+"1",codeTable);
    }

}
