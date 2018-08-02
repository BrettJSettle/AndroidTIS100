package com.bsettle.tis100clone.command;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.RegisterToken;
import com.bsettle.tis100clone.state.CommandNodeState;


public class RegisterExpression implements SourceExpression, DestinationExpression {
	private RegisterToken register;

	public RegisterExpression(String reg) {
		register = RegisterToken.valueOf(reg);
	}

	public RegisterToken getRegister() {
		return register;
	}

	@Override
	public String toString() {
		return register.name();
	}

	@Override
	public boolean write(CommandNode node, int val) {
		switch (register) {
		case NIL:
			return true;
		case ACC:
			node.commit(CommandNodeState.ACCUMULATOR, val);
			return true;
		case BAK: 
		default:
			return false;
		}
	}

	@Override
	public Integer read(CommandNode node) {
		switch (register) {
		case NIL:
			return 0;
		case ACC:
			return node.getState().getAccumulator();
		case BAK:
		default:
			return null;
		}
	}

}
