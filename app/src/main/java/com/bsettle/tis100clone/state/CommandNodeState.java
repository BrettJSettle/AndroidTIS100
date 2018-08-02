package com.bsettle.tis100clone.state;

import com.bsettle.tis100clone.command.Command;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.PortToken;

public class CommandNodeState extends NodeState {

    public static final String MODE = "MODE";
	public static final String ACCUMULATOR = "ACCUMULATOR";
	public static final String BACKUP = "BACKUP";
	public static final String LAST_PORT = "LAST_PORT";
	public static final String COMMAND_INDEX = "COMMAND_INDEX";
    public static final String INCREMENT_COMMAND = "INCREMENT_COMMAND";

    private Mode mode;
    private int accumulator;
    private int backup;
    private PortToken lastPort;
    private int commandIndex;
    private boolean incrementCommand;


    public CommandNodeState() {
		super();
		setProperty(MODE, Mode.IDLE);
		setProperty(ACCUMULATOR, 0);
		setProperty(BACKUP, 0);
        setProperty(COMMAND_INDEX, -1);
        setProperty(LAST_PORT, null);
        setProperty(INCREMENT_COMMAND, false);
    }

    @Override
    public void setProperty(final String property, Object value){
        switch(property){
            case MODE:
                mode = (Mode) value;
                break;
            case ACCUMULATOR:
                accumulator = (int) value;
                break;
            case BACKUP:
                backup = (int) value;
                break;
            case LAST_PORT:
                lastPort = (PortToken) value;
                break;
            case COMMAND_INDEX:
                commandIndex = (int) value;
                break;
            case INCREMENT_COMMAND:
                incrementCommand = (boolean) value;
                break;
            default:
                super.setProperty(property, value);
        }
    }


    public Mode getMode(){
        return mode;
    }
    public int getAccumulator(){
        return accumulator;
    }
    public int getBackup(){
        return backup;
    }
    public PortToken getLastPort(){
        return lastPort;
    }
    public int getCommandIndex(){
        return commandIndex;
    }
    public boolean getIncrementCommand(){
        return incrementCommand;
    }

    public String toString(){
        return String.format("%s\nMode: %s\nACC: %s\nBAK: %s\nLAST: %s\nCom:%s",
                super.toString(), mode, accumulator, backup, lastPort, commandIndex);
    }
}
