package com.bsettle.tis100clone.state;

import android.util.SparseArray;

import com.bsettle.tis100clone.impl.PortToken;

import java.util.HashMap;

public class NodeState {

	public static final String READING_PORT = "READING_PORT";
	public static final String WRITING_PORT = "WRITING_PORT";
	public static final String WRITING_VALUE = "WRITING_VALUE";

	private PortToken readingPort;
	private PortToken writingPort;
	private int writingValue;

	public NodeState() {
	    readingPort = null;
	    writingPort = null;
	    writingValue = 0;
	}

	public void update(HashMap<String, Object> newProps){
	    if (newProps != null) {
            for (String key : newProps.keySet()) {
                setProperty(key, newProps.get(key));
            }
        }
    }

    void setProperty(final String property, Object value){
	    switch (property){
            case READING_PORT:
                readingPort = (PortToken) value;
                break;
            case  WRITING_PORT:
                writingPort = (PortToken) value;
                break;
            case WRITING_VALUE:
                writingValue = (int) value;
                break;
        }
    }

    public PortToken getReadingPort(){
        return readingPort;
    }

    public PortToken getWritingPort(){
        return writingPort;
    }

    public int getWritingValue() { return writingValue; }

    public String toString(){
        return String.format("READ: %s\nWRITE: %s\nVAL: %s",
                readingPort, writingPort, writingValue);
    }

}
