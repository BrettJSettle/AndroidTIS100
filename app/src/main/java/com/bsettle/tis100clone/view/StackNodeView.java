package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.StackNode;

import java.util.Iterator;


public class StackNodeView extends NodeView {

    private LinearLayout stackLayout;

    public StackNodeView(Context context) {
        super(context);
        initialize(context);
    }

    public StackNodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public StackNodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.stack_node_view, this);
        stackLayout = findViewById(R.id.stack_layout);

    }

    @Override
    public StackNode getNode() {
        return (StackNode) node;
    }

    public TextView addRow(int n){
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.WHITE);
        tv.setText(String.valueOf(n));
        tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        tv.setMaxHeight(stackLayout.getHeight() / Node.MAX_LINES);

        stackLayout.addView(tv);
        return tv;
    }

    @Override
    public void update() {
        stackLayout.removeAllViewsInLayout();
        Iterator<Integer> stack = getNode().iter();
        TextView tv = null;
        while(stack.hasNext()){
            tv = addRow(stack.next());
        }
        if (tv != null){
            tv.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public void setActive(boolean active) {
        if (!active){
            stackLayout.removeAllViewsInLayout();
        }
    }
}
