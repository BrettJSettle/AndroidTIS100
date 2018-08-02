package com.bsettle.tis100clone.command;

import android.util.SparseArray;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.parse.Token;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public class CommandInstruction extends InstructionExpression {

	public CommandInstruction(Token instruction) {
		super(instruction);
	}

	@Override
	public String toString() {
		switch (instruction.sequence) {
		case "NEG":
			return "Negate ACC";
		case "SAV":
			return "Save ACC to BAC";
		case "SWP":
			return "Swap ACC and BAK";
		case "NOP":
		default:
			return "Unknown command " + instruction.sequence;
		}
	}

	@Override
	public void getDiff(CommandNode node) {
        CommandNodeState state = node.getState();
        switch (instruction.sequence) {
		case "NEG":
            node.commit(CommandNodeState.ACCUMULATOR, -state.getAccumulator());
			break;
		case "SWP":
			int bak = state.getBackup();
            node.commit(CommandNodeState.BACKUP, state.getAccumulator());
            node.commit(CommandNodeState.ACCUMULATOR, bak);
			break;
		case "SAV":
            node.commit(CommandNodeState.BACKUP, state.getAccumulator());
			break;
		case "NOP":
			break;
		default:
			return;
		}
        node.commit(CommandNodeState.INCREMENT_COMMAND, true);
		node.commit(CommandNodeState.MODE, Mode.RUN);
	}

}
