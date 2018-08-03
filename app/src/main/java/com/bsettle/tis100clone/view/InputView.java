package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;

import java.util.Iterator;

public class InputView extends PortView  {

    private InputNode inputNode;

    public InputView(Context context, String header, InputNode source, Node target) {
        super(context, LinearLayout.HORIZONTAL, source, target);
        ((IOColumnView) viewA).setHeader(header);
        setInputNode(source);
    }

    @Override
    protected View getViewA() {
        if(viewA == null) {
            viewA = new IOColumnView(getContext());
        }
        return viewA;
    }

    public void setInputNode(InputNode inp){
        this.inputNode = inp;
        update();
    }

    private IOColumnView getInputColumn(){
        return (IOColumnView) viewA;
    }

    @Override
    LinearLayout.LayoutParams getViewBParams() {
        LinearLayout.LayoutParams params =  super.getViewBParams();
        params.gravity = Gravity.BOTTOM;
        return params;
    }

    @Override
    public void update() {
        for(Iterator<Integer> iter = inputNode.iter(); iter.hasNext();) {
            getInputColumn().addRow(iter.next());
        }
    }
}
