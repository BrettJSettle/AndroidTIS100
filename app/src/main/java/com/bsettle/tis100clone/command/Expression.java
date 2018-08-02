package com.bsettle.tis100clone.command;

import android.util.SparseArray;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public abstract class Expression {
	public abstract String toString();
	public abstract void getDiff(CommandNode node);
}
