package com.bsettle.tis100clone.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.OutputNode;

import java.util.Iterator;
import java.util.Locale;

public class IOPortView extends PortView {

    protected boolean isInput = false;
    protected IOColumnView columnView;

    public IOPortView(Context context, AttributeSet attrs){
        super(context, LinearLayout.HORIZONTAL, null, null);
        getColumnView().setHeader("IN");

        isInput = true;
    }

    public IOPortView(Context context, String header, InputNode source, Node target) {
        super(context, LinearLayout.HORIZONTAL, source, target);
        getColumnView().setHeader("IN." + header);

        isInput = true;
    }

    @Override
    LayoutParams getViewBParams() {
        LinearLayout.LayoutParams p = super.getViewBParams();
        if (nodeA instanceof InputNode) {
            p.gravity = Gravity.BOTTOM;
        }
        return p;
    }

    public IOPortView(Context context, String header, Node source, OutputNode target) {
        super(context, LinearLayout.HORIZONTAL, source, target);
        getColumnView().setHeader("OUT." + header);
    }

    @Override
    protected View getViewA() {
        if(viewA == null) {

            viewA = new FrameLayout(this.getContext());
            columnView = new IOColumnView(getContext());

            ((FrameLayout) viewA).addView(columnView);
            viewA.setLayoutDirection(LAYOUT_DIRECTION_RTL);
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
        return columnView;
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
