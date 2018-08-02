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
public class GameStateTest {
    private GameState state;

    @Test
    public void input(){

        LevelInfo li = new LevelInfo(1, 1);
        Vector<Integer> inp = new Vector<Integer>();
        inp.add(1);
        inp.add(2);
        li.addInputColumn(0, inp);
        state = new GameState(li);
        CommandNode node = ((CommandNode) state.getNode(0, 0));
        node.setCommand(0, "ADD UP");

        state.activate();
        assertEquals(node.getState().getMode(), Mode.IDLE);
        state.step();
        assertEquals(node.getState().getMode(), Mode.READ);
        assertEquals(node.getState().getReadingPort(), PortToken.UP);
        state.step();
        assertEquals(node.getState().getMode(), Mode.RUN);
        assertEquals(node.getState().getReadingPort(), null);
        assertEquals(node.getState().getAccumulator(), 1);

    }

}