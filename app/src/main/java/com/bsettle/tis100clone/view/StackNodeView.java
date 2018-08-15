package com.bsettle.tis100clone.view;

import android.content.Context;
import android.util.AttributeSet;

import com.bsettle.tis100clone.impl.StackNode;


public class StackNodeView extends NodeView {

    public StackNodeView(Context context) {
        super(context);
    }

    public StackNodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StackNodeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public StackNode getNode() {
        return (StackNode) node;
    }

    @Override
    public void update() {

    }

    @Override
    public void setActive(boolean active) {

    }
}
