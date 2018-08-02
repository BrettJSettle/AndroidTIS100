package com.bsettle.tis100clone;

import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Mode;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.level.LevelInfo;
import com.bsettle.tis100clone.state.CommandNodeState;
import com.bsettle.tis100clone.state.GameState;

import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NodeGridTest {
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

    private void assertNodeInit(CommandNode node){
        CommandNodeState state = node.getState();
        assertEquals(state.getMode(), Mode.IDLE);
        assertEquals(state.getCommandIndex(), 0);
        assertEquals(state.getAccumulator(), 0);
        assertEquals(state.getBackup(), 0);
        assertNull(state.getLastPort());
        assertTrue(node.isRunning());
    }

    @Test
    public void ADD_ANY(){
        /*
        ADD ANY center node while all directions are writing in.
        Should read LEFT, RIGHT, LEFT, RIGHT, ...
         */
        up.setCommand(0, "MOV 1 DOWN");
        left.setCommand(0, "MOV 2 RIGHT");
        node.setCommand(0, "ADD ANY");
        right.setCommand(0, "MOV 3 LEFT");
        down.setCommand(0, "MOV 4 UP");
        state.activate();

        assertNodeInit(up);
        assertNodeInit(left);
        assertNodeInit(node);
        assertNodeInit(right);
        assertNodeInit(down);

        state.step();
        assertEquals(node.getState().getMode(), Mode.READ);
        assertEquals(node.getState().getReadingPort(), PortToken.ANY);
        assertEquals(node.getState().getCommandIndex(), 0);

        assertEquals(up.getState().getMode(), Mode.WRITE);
        assertEquals(up.getState().getWritingPort(), PortToken.DOWN);
        assertEquals(up.getState().getWritingValue(), 1);
        assertEquals(up.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.WRITE);
        assertEquals(left.getState().getWritingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getWritingValue(), 2);
        assertEquals(left.getState().getCommandIndex(), 0);

        assertEquals(right.getState().getMode(), Mode.WRITE);
        assertEquals(right.getState().getWritingPort(), PortToken.LEFT);
        assertEquals(right.getState().getWritingValue(), 3);
        assertEquals(right.getState().getCommandIndex(), 0);

        assertEquals(down.getState().getMode(), Mode.WRITE);
        assertEquals(down.getState().getWritingPort(), PortToken.UP);
        assertEquals(down.getState().getWritingValue(), 4);
        assertEquals(down.getState().getCommandIndex(), 0);

        state.step();
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getReadingPort(), null);
        assertEquals(node.getState().getLastPort(), PortToken.LEFT);
        assertEquals(node.getState().getCommandIndex(), 0);
        assertEquals(node.getState().getAccumulator(), 2);

        assertEquals(up.getState().getMode(), Mode.WRITE);
        assertEquals(up.getState().getWritingPort(), PortToken.DOWN);
        assertEquals(up.getState().getWritingValue(), 1);
        assertEquals(up.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.RUN);
        assertEquals(left.getState().getWritingPort(), null);
        assertEquals(left.getState().getCommandIndex(), 0);

        assertEquals(right.getState().getMode(), Mode.WRITE);
        assertEquals(right.getState().getWritingPort(), PortToken.LEFT);
        assertEquals(right.getState().getWritingValue(), 3);
        assertEquals(right.getState().getCommandIndex(), 0);

        assertEquals(down.getState().getMode(), Mode.WRITE);
        assertEquals(down.getState().getWritingPort(), PortToken.UP);
        assertEquals(down.getState().getWritingValue(), 4);
        assertEquals(down.getState().getCommandIndex(), 0);

        state.step();
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getReadingPort(), null);
        assertEquals(node.getState().getLastPort(), PortToken.RIGHT);
        assertEquals(node.getState().getCommandIndex(), 0);
        assertEquals(node.getState().getAccumulator(), 5);

        assertEquals(up.getState().getMode(), Mode.WRITE);
        assertEquals(up.getState().getWritingPort(), PortToken.DOWN);
        assertEquals(up.getState().getWritingValue(), 1);
        assertEquals(up.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.WRITE);
        assertEquals(left.getState().getWritingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getWritingValue(), 2);
        assertEquals(left.getState().getCommandIndex(), 0);

        assertEquals(right.getState().getMode(), Mode.RUN);
        assertEquals(right.getState().getWritingPort(), null);
        assertEquals(right.getState().getCommandIndex(), 0);

        assertEquals(down.getState().getMode(), Mode.WRITE);
        assertEquals(down.getState().getWritingPort(), PortToken.UP);
        assertEquals(down.getState().getWritingValue(), 4);
        assertEquals(down.getState().getCommandIndex(), 0);

    }

    @Test
    public void MOV_NUM_ANY(){
        /*
        Write in all directions. Read from all at once.
        Should read from UP always
         */
        up.setCommand(0, "ADD DOWN");
        left.setCommand(0, "ADD RIGHT");
        node.setCommand(0, "MOV 1 ANY");
        right.setCommand(0, "ADD LEFT");
        down.setCommand(0, "ADD UP");
        state.activate();

        assertNodeInit(up);
        assertNodeInit(left);
        assertNodeInit(node);
        assertNodeInit(right);
        assertNodeInit(down);

        state.step();
        assertEquals(node.getState().getMode(), Mode.WRITE);
        assertEquals(node.getState().getWritingPort(), PortToken.ANY);
        assertEquals(node.getState().getWritingValue(), 1);
        assertEquals(node.getState().getCommandIndex(), 0);

        assertEquals(up.getState().getMode(), Mode.READ);
        assertEquals(up.getState().getReadingPort(), PortToken.DOWN);
        assertEquals(up.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.READ);
        assertEquals(left.getState().getReadingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getCommandIndex(), 0);

        assertEquals(right.getState().getMode(), Mode.READ);
        assertEquals(right.getState().getReadingPort(), PortToken.LEFT);
        assertEquals(right.getState().getCommandIndex(), 0);

        assertEquals(down.getState().getMode(), Mode.READ);
        assertEquals(down.getState().getReadingPort(), PortToken.UP);
        assertEquals(down.getState().getCommandIndex(), 0);

        state.step();

        assertEquals(up.getState().getMode(), Mode.RUN);
        assertEquals(up.getState().getReadingPort(), null);
        assertEquals(up.getState().getAccumulator(), 1);
        assertEquals(up.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.READ);
        assertEquals(left.getState().getReadingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getCommandIndex(), 0);

        assertEquals(right.getState().getMode(), Mode.READ);
        assertEquals(right.getState().getReadingPort(), PortToken.LEFT);
        assertEquals(right.getState().getCommandIndex(), 0);

        assertEquals(down.getState().getMode(), Mode.READ);
        assertEquals(down.getState().getReadingPort(), PortToken.UP);
        assertEquals(down.getState().getCommandIndex(), 0);

        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getWritingPort(), null);
        assertEquals(node.getState().getLastPort(), PortToken.UP);
        assertEquals(node.getState().getCommandIndex(), 0);

        state.step();
        assertEquals(node.getState().getMode(), Mode.WRITE);
        assertEquals(node.getState().getWritingPort(), PortToken.ANY);
        assertEquals(node.getState().getWritingValue(), 1);
        assertEquals(node.getState().getCommandIndex(), 0);

        assertEquals(up.getState().getMode(), Mode.READ);
        assertEquals(up.getState().getReadingPort(), PortToken.DOWN);
        assertEquals(up.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.READ);
        assertEquals(left.getState().getReadingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getCommandIndex(), 0);

        assertEquals(right.getState().getMode(), Mode.READ);
        assertEquals(right.getState().getReadingPort(), PortToken.LEFT);
        assertEquals(right.getState().getCommandIndex(), 0);

        assertEquals(down.getState().getMode(), Mode.READ);
        assertEquals(down.getState().getReadingPort(), PortToken.UP);
        assertEquals(down.getState().getCommandIndex(), 0);


        state.step();
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getWritingPort(), null);
        assertEquals(node.getState().getLastPort(), PortToken.UP);
        assertEquals(node.getState().getCommandIndex(), 0);

        assertEquals(up.getState().getMode(), Mode.RUN);
        assertEquals(up.getState().getReadingPort(), null);
        assertEquals(up.getState().getAccumulator(), 2);
        assertEquals(up.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.READ);
        assertEquals(left.getState().getReadingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getCommandIndex(), 0);

        assertEquals(right.getState().getMode(), Mode.READ);
        assertEquals(right.getState().getReadingPort(), PortToken.LEFT);
        assertEquals(right.getState().getCommandIndex(), 0);

        assertEquals(down.getState().getMode(), Mode.READ);
        assertEquals(down.getState().getReadingPort(), PortToken.UP);
        assertEquals(down.getState().getCommandIndex(), 0);

        state.step();
        assertEquals(node.getState().getMode(), Mode.WRITE);
        assertEquals(node.getState().getWritingPort(), PortToken.ANY);
        assertEquals(node.getState().getWritingValue(), 1);
        assertEquals(node.getState().getCommandIndex(), 0);

        assertEquals(up.getState().getMode(), Mode.READ);
        assertEquals(up.getState().getReadingPort(), PortToken.DOWN);
        assertEquals(up.getState().getCommandIndex(), 0);

        assertEquals(left.getState().getMode(), Mode.READ);
        assertEquals(left.getState().getReadingPort(), PortToken.RIGHT);
        assertEquals(left.getState().getCommandIndex(), 0);

        assertEquals(right.getState().getMode(), Mode.READ);
        assertEquals(right.getState().getReadingPort(), PortToken.LEFT);
        assertEquals(right.getState().getCommandIndex(), 0);

        assertEquals(down.getState().getMode(), Mode.READ);
        assertEquals(down.getState().getReadingPort(), PortToken.UP);
        assertEquals(down.getState().getCommandIndex(), 0);
    }

}