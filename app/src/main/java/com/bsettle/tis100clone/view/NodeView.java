package com.bsettle.tis100clone.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import com.bsettle.tis100clone.impl.Node;

public abstract class NodeView extends ConstraintLayout {
    protected Node node;

    public NodeView(Context context) {
        super(context);
    }

    public NodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNode(Node node){
        this.node = node;
    }

    public abstract Node getNode();

    public abstract void update();

    public abstract void setActive(boolean active);

}
