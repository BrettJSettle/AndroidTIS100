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
import java.util.Locale;
import java.util.function.Consumer;

public class InputView extends IOPortView  {

    public InputView(Context context, String header, InputNode source, Node target) {
        super(context, header, source, target);
        source.iter().forEachRemaining(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                getIOColumn().addRow(String.valueOf(integer));
            }
        });
    }

    @Override
    LinearLayout.LayoutParams getViewBParams() {
        LinearLayout.LayoutParams params =  super.getViewBParams();
        params.gravity = Gravity.BOTTOM;
        return params;
    }

    @Override
    public void update() {
        super.update();
        InputNode inp = (InputNode) getSourceNode();
        getIOColumn().setSelectedLine(inp.getInputLine());
    }
}
