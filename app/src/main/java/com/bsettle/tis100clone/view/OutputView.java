package com.bsettle.tis100clone.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;

public class OutputView extends IOPortView {

    public OutputView(Context context, String header, Node nodeA) {
        super(context, header, nodeA, null);
    }

    public void addOutput(int output){
        getIOColumn().getSelectedLine();
    }
}
