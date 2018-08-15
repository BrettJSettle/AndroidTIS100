package com.bsettle.tis100clone.impl;

import com.bsettle.tis100clone.state.Activatable;
import com.bsettle.tis100clone.state.CommandNodeState;
import com.bsettle.tis100clone.state.NodeState;

import java.util.HashMap;

public abstract class Node implements Activatable{
    public static final int MAX_LINES = 15;

    HashMap<PortToken, Node> neighbors;
	protected NodeState state;

	HashMap<String, Object> diff = null;

    public Node(NodeState state) {
		this.state = state;
		neighbors = new HashMap<>();
	}

	public NodeState getState(){
	    return state;
    }

	public abstract HashMap<String, Object> getDiff();

	public void push(){
        state.update(diff);
        diff = null;
	}
	public void commit(String name, Object val){
	    if (diff == null){
	        diff = new HashMap<>();
        }
	    diff.put(name, val);
	}

    private PortToken getWritingNeighbor(PortToken p){
	    if (p.equals(PortToken.ANY)){
            for (PortToken t : new PortToken[]{PortToken.LEFT, PortToken.RIGHT, PortToken.UP, PortToken.DOWN}){
                t = getWritingNeighbor(t);
                if (t != null) {
                    return t;
                }
            }
            return null;
        }

	    Node n = neighbors.get(p);
	    if (n != null){
	        PortToken t = n.getState().getWritingPort();
	        if (t != null){
	            if (t.equals(PortToken.ANY)) {
                    return p;
                }else if (t.equals(p.reverse())){
	                return p;
                }
            }
        }
        return null;
    }

	public static void connectNodes(Node nodeA, PortToken token, Node nodeB){
        nodeA.neighbors.put(token, nodeB);
        nodeB.neighbors.put(token.reverse(), nodeA);
    }

    public Integer tryRead(PortToken p){
	    PortToken p_real = getWritingNeighbor(p);
	    if (p_real == null){
	        return null;
        }
        Node neighbor = neighbors.get(p_real);

        Integer val = null;
        if (neighbor != null) {
            if (p.equals(PortToken.ANY)) {
                commit(CommandNodeState.LAST_PORT, p_real);
            }

            NodeState ns = neighbor.getState();
            val = ns.getWritingValue();
            commit(CommandNodeState.READING_PORT, null);

            neighbor.writeFinished(p_real.reverse());

		}
	    return val;
    }

    public void writeFinished(PortToken direction){
        if (state.getWritingPort().equals(PortToken.ANY)){
            commit(CommandNodeState.LAST_PORT, direction);
            push();
        }

        commit(CommandNodeState.WRITING_PORT, null);
        commit(CommandNodeState.INCREMENT_COMMAND, true);
        push();

        commit(CommandNodeState.MODE, Mode.RUN);
    }

    public void reset(){
	    deactivate();
    }
}
