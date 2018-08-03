package com.bsettle.tis100clone.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.impl.Node;

public abstract class IOPortView extends PortView {
    public IOPortView(Context context, String header, Node source, Node target) {
        super(context, LinearLayout.HORIZONTAL, source, target);
        ((IOColumnView) viewA).setHeader(header);
    }

    public Node getSourceNode(){
        return nodeA;
    }

    @Override
    protected View getViewA() {
        if(viewA == null) {
            viewA = new IOColumnView(getContext());
        }
        return viewA;
    }

    IOColumnView getIOColumn(){
        return (IOColumnView) viewA;
    }

    @Override
    public void update() {
        ((PortView) viewB).update();
    }
}
