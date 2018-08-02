package com.bsettle.tis100clone.command;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.parse.Token;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public class AdditionInstruction extends InstructionExpression {
	private SourceExpression other;

	public AdditionInstruction(Token instruction, SourceExpression other) {
		super(instruction);
		this.other = other;
	}

	@Override
	public void getDiff(CommandNode node) {

        Integer num = resolveSource(node, other);
        if (num == null) {
            return;
        }

        CommandNodeState s = node.getState();
        int acc = s.getAccumulator();
        switch(instruction.sequence){
            case "ADD":
                node.commit(CommandNodeState.ACCUMULATOR, acc + num);
                break;
            case "SUB":
                node.commit(CommandNodeState.ACCUMULATOR, acc - num);
                break;
            default:
                return;
        }
        node.commit(CommandNodeState.INCREMENT_COMMAND, true);
        node.commit(CommandNodeState.MODE, Mode.RUN);
	}

	@Override
	public String toString() {
		if (instruction.sequence.equals("ADD")) {
			return "Add " + other.toString() + " to ACC";
		} else if (instruction.sequence.equals("SUB")) {
			return "Subtract " + other.toString() + " from ACC";
		}
		return "Unknown arithmetic command. This should never happen";
	}

}
