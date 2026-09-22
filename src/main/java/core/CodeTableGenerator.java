package core;

import model.CodeTable;

import java.util.HashMap;
import java.util.Map;

public class CodeTableGenerator {


    public CodeTable generate(HuffmanNode root) {
        Map<Byte, String> codeMap = new HashMap<>();
        if (root == null)
            return new CodeTable(codeMap);

        if (root.isLeaf()) {
            codeMap.put(root.getSymbol(),"0");
            return new CodeTable(codeMap);
        }

        dfs(root, "", codeMap);
        return new CodeTable(codeMap);
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
