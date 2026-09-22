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
}
