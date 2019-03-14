package com.bsettle.tis100clone.level;

import android.util.JsonReader;

import java.io.IOException;

public class NodeInfo {
    private final int row, column;
    private final LevelInfo.NodeType nodeType;
    private NodeInfo(int row, int column, LevelInfo.NodeType type){
        this.row = row;
        this.column = column;
        this.nodeType = type;
    }

    public LevelInfo.NodeType getNodeType() {
        return nodeType;
    }

    public int getRow(){
        return row;
    }

    public int getColumn() {
        return column;
    }

    public static NodeInfo fromJson(JsonReader reader) throws IOException{
        int row = -1, column = -1;
        LevelInfo.NodeType nodeType = LevelInfo.NodeType.COMMAND;
        boolean disabled = false;

        while(reader.hasNext()) {
            String nName = reader.nextName();
            switch (nName) {
                case "row":
                    row = reader.nextInt();
                    break;
                case "column":
                    column = reader.nextInt();
                    break;
                case "type":
                    nodeType = LevelInfo.NodeType.valueOf(reader.nextString());
                    break;
                case "disabled":
                    disabled = reader.nextBoolean();
                    break;
                default:
                    throw new IOException("Unknown key '" + nName + "' in node");
            }
        }

        reader.endObject();
        if (disabled){
            return null;
        }
        if (row >= 0 && column >= 0) {
            return new NodeInfo(row, column, nodeType);
        }
        return null;
    }
}
