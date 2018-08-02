package com.bsettle.tis100clone.level;

import android.content.res.AssetManager;
import android.util.JsonReader;

import com.bsettle.tis100clone.impl.IOColumnInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

public class LevelInfo extends LevelTileInfo{
    private String name;
    private String description;
    private int rows = 3;
    private int columns = 4;
    private Vector<IOColumnInfo> inputColumns = new Vector<>();
    private Vector<IOColumnInfo> outputColumns = new Vector<>();
    private Vector<NodeInfo> nodeInfos = new Vector<>();

    enum NodeType {
        DEFAULT, STACK
    }

    public static class NodeInfo {

        private int row, column;
        private boolean disabled;
        private NodeType nodeType;
        private NodeInfo(int row, int column, boolean disabled, NodeType type){
            this.row = row;
            this.column = column;
            nodeType = type;
            this.disabled = disabled;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public NodeType getNodeType() {
            return nodeType;
        }

        private int getRow(){
            return row;
        }

        private int getColumn() {
            return column;
        }
    }

    public LevelInfo(int rows, int columns){
        super(-1, "Test", "");
        setRows(rows);
        setColumns(columns);
    }

    LevelInfo(LevelTileInfo tile){
        super(tile.getNumber(), tile.getName(), tile.getDescription());
    }

    public static LevelInfo fromFile(LevelTileInfo tile, InputStream fis){

        LevelInfo levelInfo = new LevelInfo(tile);
        levelInfo.setDescription(tile.getDescription());

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(fis));
            reader.beginObject();
            while (reader.hasNext()){
                String name = reader.nextName();
                switch(name){
                    case "rows":
                        levelInfo.setRows(reader.nextInt());
                        break;
                    case "columns":
                        levelInfo.setColumns(reader.nextInt());
                        break;
                    case "input":
                    case "output":
                        reader.beginArray();
                        while (reader.hasNext()){
                            reader.beginObject();
                            while(reader.hasNext()){
                                int column = -1;
                                Vector<Integer> values = new Vector<>();
                                switch (reader.nextName()){
                                    case "column":
                                        column = reader.nextInt();
                                        break;
                                    case "values":
                                        reader.beginArray();
                                        while(reader.hasNext()){
                                            values.add(reader.nextInt());
                                        }
                                        reader.endArray();
                                        break;
                                }
                                if (column >= 0) {
                                    if (name.equals("input")) {
                                        levelInfo.addInputColumn(column, values);
                                    }else {
                                        levelInfo.addOutputColumn(column, values);
                                    }
                                }
                            }
                            reader.endObject();
                        }
                        reader.endArray();
                        break;
                    case "nodes":
                        reader.beginArray();
                        while(reader.hasNext()){
                            int row = -1, column = -1;
                            boolean disabled = false;
                            NodeType nodeType = NodeType.DEFAULT;
                            reader.beginObject();
                            while(reader.hasNext()) {
                                String nName = reader.nextName();
                                switch (nName) {
                                    case "row":
                                        row = reader.nextInt();
                                        break;
                                    case "column":
                                        column = reader.nextInt();
                                        break;
                                    case "disabled":
                                        disabled = reader.nextBoolean();
                                        break;
                                    case "type":
                                        nodeType = NodeType.valueOf(reader.nextString());
                                    default:
                                        throw new IOException("Unknown key '" + nName + " in node");
                                }
                                if (row >= 0 && column >= 0) {
                                    levelInfo.addNodeInfo(new NodeInfo(row, column, disabled, nodeType));
                                }
                            }
                            reader.endObject();
                        }
                        reader.endArray();
                        break;
                    default:
                        throw new IOException("Unknown key '" + name + "' in level.");
                }
            }
            reader.close();
        } catch (IOException e){
            System.err.println("Failed to parse level from JSON. " + e.getMessage());
        }

        return levelInfo;
    }

    public String getName(){
        return name;
    }
    private void setName(String name) {
        this.name = name;
    }

    public String getDescription(){
        return description;
    }
    private void setDescription(String description) {
        this.description = description;
    }

    private void addNodeInfo(NodeInfo info){
        if (info != null)
            nodeInfos.add(info);
    }

    public NodeInfo getNodeInfo(int row, int column){
        for (NodeInfo ni : nodeInfos){
            if (ni.getRow() == row && ni.getColumn() == column){
                return ni;
            }
        }
        return null;
    }

    public int getRows() {
        return rows;
    }
    private void setRows(int rows){
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }
    private void setColumns(int columns){
        this.columns = columns;
    }

    public void addInputColumn(int column, Vector<Integer> values){
        inputColumns.add(new IOColumnInfo(column, values));
    }

    public void addOutputColumn(int column, Vector<Integer> values){
        outputColumns.add(new IOColumnInfo(column, values));
    }

    public Vector<IOColumnInfo> getInputColumns(){
        return inputColumns;
    }

    public Vector<IOColumnInfo> getOutputColumns() {
        return outputColumns;
    }
}
