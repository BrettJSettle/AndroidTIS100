package com.bsettle.tis100clone.command;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;


public class PortExpression implements SourceExpression, DestinationExpression {
	private PortToken port;

	public PortExpression(String reg) {
		port = PortToken.valueOf(reg);
	}

	public PortToken getPort() {
		return port;
	}

	@Override
	public String toString() {
		return port.name();
	}

	@Override
	public boolean write(CommandNode node, int val) {
		CommandNodeState s = node.getState();
		if (port == PortToken.LAST) {
			port = s.getLastPort();
		}
		switch (port) {
		case LEFT:
		case RIGHT:
		case UP:
		case DOWN:
		case ANY:
		default:
			return false;
		}
	}

	@Override
	public Integer read(CommandNode node) {
		CommandNodeState s = node.getState();
		if (port == PortToken.LAST) {
			port = s.getLastPort();
		}
		switch (port) {
		case LEFT:
		case RIGHT:
		case UP:
		case DOWN:
		case ANY:
            return node.tryRead(port);
		default:
			return null;
		}
	}


}
