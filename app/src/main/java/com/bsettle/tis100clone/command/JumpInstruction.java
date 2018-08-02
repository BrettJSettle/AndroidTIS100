package com.bsettle.tis100clone.command;

import android.util.SparseArray;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.parse.Token;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public class JumpInstruction extends InstructionExpression {
	private String label;
	public JumpInstruction(Token instruction, String label) {
		super(instruction);
		this.label = label;
	}

	@Override
	public String toString() {
	    String condition = "";
	    switch(instruction.sequence){
            case "JGZ":
                condition = " if > 0";
                break;
            case "JNZ":
                condition = " if != 0";
                break;
            case "JEZ":
                condition = " if == 0";
                break;
            case "JLZ":
                condition = " if < 0";
                break;
        }
		return "Jump to label " + label + condition;
	}

	public String getLabel(){
	    return label;
    }

	@Override
	public void getDiff(CommandNode node) {
		CommandNodeState s = node.getState();

		boolean condition = false;
		String seq = instruction.sequence;
		condition = seq.equals("JMP");
		condition |= seq.equals("JNZ") && s.getAccumulator() != 0;

		node.commit(CommandNodeState.MODE, Mode.RUN);
		if (condition){
			Integer line = node.getLabelLine(label);
			if (line != null) {
				node.commit(CommandNodeState.COMMAND_INDEX, line);
			}
		}else{
			node.commit(CommandNodeState.INCREMENT_COMMAND, true);
		}
	}

}
