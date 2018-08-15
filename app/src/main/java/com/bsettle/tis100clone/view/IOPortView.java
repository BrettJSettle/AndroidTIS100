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
import java.util.Locale;

public class IOPortView extends PortView {

    protected boolean isInput = false;

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

    @Override
    protected View getViewA() {
        if(viewA == null) {
            viewA = new IOColumnView(getContext());
        }
        return viewA;
    }

    public void setData(Iterator<Integer> values){
        int row = 0;
        getColumnView().clear();
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
        if (isInput){
            iocv.setSelectedLine(((InputNode) nodeA).getInputLine());
        }else{
            OutputNode node = ((OutputNode) nodeB);
            if (node.getOutputLine() >= 0) {
                Boolean res = node.checkOutput();
                if (res != null) {
                    int exp = node.getExpectedValue();
                    int out = node.getOutputValue();
                    String s = String.format(Locale.getDefault(), "%d/%d", exp, out);
                    TextView tv = iocv.getCurrentRow();
                    tv.setText(s);
                    iocv.setSelectedLine(node.nextLine());
                    if (!res) {
                        tv.setBackgroundColor(Color.RED);
                    }
                } else {
                    iocv.setSelectedLine(node.getOutputLine());
                }
            }else{
                setData(node.iter());
            }
        }
    }
}
