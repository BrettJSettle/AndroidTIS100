package com.bsettle.tis100clone.command;


import android.util.SparseArray;

import com.bsettle.tis100clone.impl.CommandNode;

import java.util.HashMap;

public class CommentExpression extends Expression {
	private String comment;
	public CommentExpression(String comment) {
		this.comment = comment;
	}

	public String toString() {
		return comment;
	}

	@Override
	public void getDiff(CommandNode node) {
	}

}
