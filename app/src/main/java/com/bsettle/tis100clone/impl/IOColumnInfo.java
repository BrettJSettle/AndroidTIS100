package com.bsettle.tis100clone.impl;

import android.util.JsonReader;

import java.io.IOException;
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

    public static IOColumnInfo fromJson(JsonReader reader) throws IOException {
        int column = -1;
        Vector<Integer> values = new Vector<>();
        while(reader.hasNext()) {
            switch (reader.nextName()) {
                case "column":
                    column = reader.nextInt();
                    break;
                case "values":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        values.add(reader.nextInt());
                    }
                    reader.endArray();
                    break;
            }
        }
        reader.endObject();
        return new IOColumnInfo(column, values);
    }
}
