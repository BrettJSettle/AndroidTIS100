package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;
import com.bsettle.tis100clone.state.Activatable;

public abstract class PortView extends LinearLayout {

    protected final Node nodeA, nodeB;
    protected View viewA, viewB;
    protected final int orientation;

    public PortView(Context context, int orientation, Node nodeA, Node nodeB) {
        super(context);
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.orientation = orientation;
        init();
    }

    public PortView(Context context) {
        this(context, LinearLayout.HORIZONTAL, null, null);
    }

    public PortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.nodeA = null;
        this.nodeB = null;
        orientation = LinearLayout.HORIZONTAL;
        init();
    }

    private void init(){

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        setLayoutParams(params);

        setOrientation(orientation);
        addView(getViewA(), getViewAParams());
        addView(getViewB(), getViewBParams());
    }


    LinearLayout.LayoutParams getViewAParams(){
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    LinearLayout.LayoutParams getViewBParams(){
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    protected View getViewA(){
        if (viewA == null) {
            if (orientation == LinearLayout.HORIZONTAL) {
                viewA = new UnidirectionalPortView(getContext(), PortToken.UP, nodeB, nodeA);
            }else{
                viewA = new UnidirectionalPortView(getContext(), PortToken.RIGHT, nodeA, nodeB);
            }
        }
        return viewA;
    }

    protected View getViewB(){
        if (viewB == null) {
            if (orientation == LinearLayout.HORIZONTAL) {
                viewB = new UnidirectionalPortView(getContext(), PortToken.DOWN, nodeA, nodeB);
            }else {
                viewB = new UnidirectionalPortView(getContext(), PortToken.LEFT, nodeB, nodeA);
                viewB.setLayoutDirection(LAYOUT_DIRECTION_RTL);
            }
        }
        return viewB;
    }

    public void setViewAVisibility(int visible){
        viewA.setVisibility(visible);
    }

    public void setViewBVisibility(int visible){
        viewB.setVisibility(visible);
    }

    public abstract void update();
}
