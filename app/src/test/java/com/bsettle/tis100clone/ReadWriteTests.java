package com.bsettle.tis100clone;


import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.NodeGrid;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.level.LevelInfo;
import com.bsettle.tis100clone.state.CommandNodeState;
import com.bsettle.tis100clone.state.GameState;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReadWriteTests {
    private GameState state;
    private CommandNode node, left, right, up, down;
    @Before
    public void init(){
        LevelInfo levelInfo = new LevelInfo(3, 3);

        state = new GameState(levelInfo);
        node = (CommandNode) state.getNode(1, 1);
        left = (CommandNode) state.getNode(1, 0);
        right = (CommandNode) state.getNode(1, 2);
        down = (CommandNode) state.getNode(2, 1);
        up = (CommandNode) state.getNode(0, 1);
    }

    @Test
    public void ADD_PORT(){
        node.setCommand(0, "ADD LEFT");
        left.setCommand(0, "MOV 1 RIGHT");

        state.activate();

        state.step();
        assertEquals(node.getState().getMode(), Mode.READ);
        assertEquals(node.getState().getReadingPort(), PortToken.LEFT);

        assertEquals(left.getState().getMode(), Mode.WRITE);
        assertEquals(left.getState().getWritingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getWritingValue(), 1);

        state.step();
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getAccumulator(), 1);
        assertEquals(node.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.RUN);
        assertEquals(left.getState().getCommandIndex(), 0);
    }

    @Test
    public void ADD_WRITING_PORT(){
        node.setCommand(0, "NOP");
        node.setCommand(1, "ADD LEFT");
        left.setCommand(0, "MOV 1 RIGHT");

        state.activate();

        state.step();
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getCommandIndex(), 1);

        assertEquals(left.getState().getMode(), Mode.WRITE);
        assertEquals(left.getState().getWritingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getWritingValue(), 1);

        state.step();
        assertEquals(node.getState().getAccumulator(), 1);
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.RUN);
        assertEquals(left.getState().getCommandIndex(), 0);
    }

    @Test
    public void WRITE_READING_PORT(){
        node.setCommand(0, "ADD LEFT");
        left.setCommand(0, "NOP");
        left.setCommand(1, "MOV 1 RIGHT");

        state.activate();

        state.step();
        assertEquals(node.getState().getMode(), Mode.READ);
        assertEquals(node.getState().getReadingPort(), PortToken.LEFT);

        assertEquals(left.getState().getMode(), Mode.RUN);
        assertEquals(left.getState().getCommandIndex(), 1);

        state.step();
        assertEquals(node.getState().getMode(), Mode.READ);
        assertEquals(node.getState().getReadingPort(), PortToken.LEFT);

        assertEquals(left.getState().getMode(), Mode.WRITE);
        assertEquals(left.getState().getWritingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getWritingValue(), 1);

        state.step();
        assertEquals(node.getState().getAccumulator(), 1);
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.RUN);
        assertEquals(left.getState().getCommandIndex(), 0);
    }

    @Test
    public void JRO_PORT(){
        node.setCommand(0, "JRO RIGHT");
        node.setCommand(1, "ADD 1");
        node.setCommand(2, "ADD 2");
        node.setCommand(3, "ADD 3");
        node.setCommand(4, "ADD 4");
        right.setCommand(0,"MOV DOWN LEFT");
        CommandNode n = (CommandNode) state.getNode(2, 2);
        n.setCommand(0, "MOV 3 UP");

        state.activate();
        state.step();
        assertEquals(node.getState().getMode(), Mode.READ);
        assertEquals(node.getState().getReadingPort(), PortToken.RIGHT);
        assertEquals(right.getState().getMode(), Mode.READ);
        assertEquals(right.getState().getReadingPort(), PortToken.DOWN);
        assertEquals(n.getState().getMode(), Mode.WRITE);
        assertEquals(n.getState().getWritingValue(), 3);
        assertEquals(n.getState().getWritingPort(), PortToken.UP);

        state.step();
        assertEquals(node.getState().getMode(), Mode.READ);
        assertEquals(node.getState().getReadingPort(), PortToken.RIGHT);
        assertEquals(right.getState().getMode(), Mode.WRITE);
        assertEquals(right.getState().getWritingPort(), PortToken.LEFT);
        assertEquals(right.getState().getWritingValue(), 3);
        assertEquals(n.getState().getMode(), Mode.RUN);

        state.step();
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getCommandIndex(), 3);
        assertEquals(right.getState().getMode(), Mode.RUN);
        assertEquals(n.getState().getMode(), Mode.WRITE);
        assertEquals(n.getState().getWritingValue(), 3);
        assertEquals(n.getState().getWritingPort(), PortToken.UP);
    }

}