package com.bsettle.tis100clone.impl;

import java.util.Vector;

public class IOColumnInfo {
    private int column;
    private Vector<Integer> values;
    public IOColumnInfo(int column, Vector<Integer> values){
        this.column = column;
        this.values = values;
    }

    public int getColumn() {
        return column;
    }

    public Vector<Integer> getValues() {
        return values;
    }
}
