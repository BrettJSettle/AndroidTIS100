package com.bsettle.tis100clone.command;


import android.util.SparseArray;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public class LabeledExpression extends Expression {
	private Expression expression;
	private String label;

	public LabeledExpression(String label, Expression expression) {
		this.label = label;
		this.expression = expression;
	}

	public String getLabel() {
		return label;
	}

	public Expression getExpression() {
		return expression;
	}

	public String toString() {
		String s = "Lbl[" + label + "]";
		if (expression != null) {
			s += expression.toString();
		}
		return s;
	}

	@Override
	public void getDiff(CommandNode node) {
		if (expression != null) {
            expression.getDiff(node);
        }
	}
}
