package com.bsettle.tis100clone.command;


import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.impl.Register;
import com.bsettle.tis100clone.parse.Token;
import com.bsettle.tis100clone.state.CommandNodeState;

import java.util.HashMap;

public abstract class InstructionExpression extends Expression {
	protected Token instruction;

	public InstructionExpression(Token instruction) {
		this.instruction = instruction;
	}
	
	public Token getInstruction(){
		return instruction;
	}
	
	public abstract String toString();

    Integer resolveSource(CommandNode node, SourceExpression source){
	    if (source instanceof RegisterExpression || source instanceof NumberExpression){
	        return source.read(node);
        }else if (source instanceof PortExpression){
	        return resolveSourcePort(node, ((PortExpression) source).getPort());
        }
        return null;
    }

	private Integer resolveSourcePort(CommandNode node, PortToken port){
        node.commit(CommandNodeState.MODE, Mode.READ);

        PortToken currentPort = node.getState().getReadingPort();
        if (currentPort == null || (!port.equals(PortToken.ANY) && !port.equals(currentPort))) {
            node.commit(CommandNodeState.READING_PORT, port);
            currentPort = port;
        }

        Integer num = node.tryRead(currentPort);
        if (num != null && port.equals(PortToken.ANY)){
            node.commit(CommandNodeState.READING_PORT, null);
        }
        return num;
    }

    void resolveDestination(CommandNode node, DestinationExpression destination, int val){
        if (destination instanceof RegisterExpression || destination instanceof NumberExpression){
            destination.write(node, val);
            node.commit(CommandNodeState.MODE, Mode.RUN);
            node.commit(CommandNodeState.INCREMENT_COMMAND, true);

        }else if (destination instanceof PortExpression){
            PortToken p = ((PortExpression) destination).getPort();
            node.commit(CommandNodeState.WRITING_VALUE, val);
            node.commit(CommandNodeState.WRITING_PORT, p);
            node.commit(CommandNodeState.MODE, Mode.WRITE);
        }
    }
}
