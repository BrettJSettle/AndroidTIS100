package com.bsettle.tis100clone.impl;


import com.bsettle.tis100clone.command.Command;
import com.bsettle.tis100clone.command.Expression;
import com.bsettle.tis100clone.command.LabeledExpression;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public class CommandNode extends Node {
	public static final int MAX_COMMANDS = 15;

	private Command[] commands;

	public CommandNode() {
		super(new CommandNodeState());

		commands = new Command[MAX_COMMANDS];
		for (int i = 0; i < MAX_COMMANDS; i++) {
			commands[i] = new Command("");
		}
	}

	/* start/stop */
	@Override
	public void activate() {
	    int line = nextAvailableCommand(0);
	    if (line >= 0) {
            commit(CommandNodeState.COMMAND_INDEX, line);
            push();
        }
	}

	@Override
    public void deactivate() {
        state = new CommandNodeState();
	}

	@Override
	public void reset() {
	    for (Command c : commands) {
			c.setCommand("");
		}
		deactivate();
	}

	/* commands */

	public Command setCommand(int i, String command) {
		if (!commands[i].getText().equals(command)) {
            commands[i] = new Command(command);
        }
		return commands[i];
	}

	private int nextAvailableCommand(){
	    int n = getState().getCommandIndex();
	    n += 1;
	    if (n >= commands.length){
	        n = 0;
        }
        return nextAvailableCommand(n);
    }

	public int nextAvailableCommand(int n) {
        int start = n;
        while (commands[n].isEmpty()) {
            n = (n + 1) % commands.length;
            if (n == start) {
                n = -1;
                break;
            }
        }
        return n;
	}

	public Integer getLabelLine(String label) {
		for (int i = 0; i < commands.length; i++) {
			Command c = commands[i];
			if (c.getExpression() instanceof LabeledExpression) {
				if (((LabeledExpression) c.getExpression()).getLabel().equals(label)) {
					return i;
				}
			}
		}
		return null;
	}

	public CommandNodeState getState(){
	    return (CommandNodeState) state;
    }

	private Command getCurrentCommand(){
	    int ind = getState().getCommandIndex();
	    if (ind < 0){
	        return null;
        }
	    return commands[ind];
    }

	@Override
	public HashMap<String, Object> getDiff() {
        if (diff == null) {
            diff = new HashMap<>();
            if (getState().getMode().equals(Mode.WRITE)){
            	return diff;
			}

			Command c = getCurrentCommand();
            if (c == null) {
                return diff;
            }

            Expression e = c.getExpression();
            if (e == null) {
                return diff;
            }

            e.getDiff(this);

            if (diff.get(CommandNodeState.MODE).equals(getState().getMode())){
                diff.remove(CommandNodeState.MODE);
            }

        }
        return diff;
	}


    @Override
    public void push() {
	    Boolean inc = (Boolean) diff.remove(CommandNodeState.INCREMENT_COMMAND);
        if (inc != null && inc.equals(true)){
            diff.put(CommandNodeState.COMMAND_INDEX, nextAvailableCommand());
        }
	    super.push();
    }
}
