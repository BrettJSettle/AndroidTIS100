package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.OutputNode;

import java.util.Iterator;

public class IOPortView extends PortView {

    private boolean isInput = false;

    public IOPortView(Context context, String header, InputNode source, Node target) {
        super(context, LinearLayout.HORIZONTAL, source, target);
        ((IOColumnView) viewA).setHeader("IN." + header);

        LinearLayout.LayoutParams params =  super.getViewBParams();
        params.gravity = Gravity.BOTTOM;
        viewB.setLayoutParams(params);

        isInput = true;
    }

    public IOPortView(Context context, String header, Node source, OutputNode target) {
        super(context, LinearLayout.HORIZONTAL, source, target);
        ((IOColumnView) viewA).setHeader("OUT." + header);
    }

    public boolean isInput() {
        return isInput;
    }

    public Node getSourceNode(){
        return nodeA;
    }
    public Node getTargetNode(){
        return nodeB;
    }

    public TextView getCurrentTextView(){
        return getColumnView().getCurrentRow();
    }

    @Override
    protected View getViewA() {
        if(viewA == null) {
            viewA = new IOColumnView(getContext());
        }
        return viewA;
    }

    public void setData(Iterator<Integer> values){
        int row = 0;
        while(values.hasNext()){
            int v = values.next();
            setRow(row++, String.valueOf(v));
        }
    }

    protected IOColumnView getColumnView(){
        return (IOColumnView) viewA;
    }

    public void setRow(int line, String val){
        IOColumnView view = getColumnView();
        String valStr = String.valueOf(val);

        if (line == view.getLineCount()){
            view.addRow(valStr);
        }else {
            view.setRow(line, valStr);
        }
    }

    @Override
    public void update() {
        ((UnidirectionalPortView) viewB).update();

        IOColumnView iocv = getColumnView();
        int selLine = -1;
        if (isInput){
            selLine = ((InputNode) nodeA).getInputLine();
        }else{
            selLine = ((OutputNode) nodeB).getOutputLine();
        }
        iocv.setSelectedLine(selLine);
    }
}
