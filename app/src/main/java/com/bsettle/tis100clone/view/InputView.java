package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;

public class InputView extends PortView  {
    private final String header;

    public InputView(Context context, String header, InputNode source, Node target) {
        super(context, LinearLayout.HORIZONTAL, source, target);
        this.header = header;
    }

//    @Override
//    protected View getViewA() {
//        if(viewA == null) {
//            viewA = new IOColumnView(getContext());
//            ((IOColumnView) viewA).setHeader(header);
//        }
//        return viewA;
//    }
    

    @Override
    public void update() {

    }
}
