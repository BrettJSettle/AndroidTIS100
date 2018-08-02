package com.bsettle.tis100clone.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;

public class OutputView extends PortView {

    private final String header;

    public OutputView(Context context, String header, Node nodeA) {
        super(context, LinearLayout.HORIZONTAL, nodeA, null);
        this.header = header;
    }

    @Override
    protected View getViewA() {
        if(viewA == null) {
            viewA = new IOColumnView(getContext());
            ((IOColumnView) viewA).setHeader(header);
        }
        return viewA;
    }

    @Override
    public void update() {

    }
}
