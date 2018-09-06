package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Constraints;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

        getViewA().setId(View.generateViewId());
        getViewB().setId(View.generateViewId());

        setOrientation(orientation);

        FrameLayout fa = new FrameLayout(getContext());
        fa.addView(getViewA());
        fa.setBackgroundColor(Color.RED);
        getViewB().setBackgroundColor(Color.YELLOW);
        addView(fa, getViewAParams());

        FrameLayout fb = new FrameLayout(getContext());
        fb.addView(getViewB());
        fb.setBackgroundColor(Color.GREEN);
        getViewB().setBackgroundColor(Color.BLUE);
        addView(fb, getViewBParams());

//        addView(getViewA(), getViewAParams());
//        addView(getViewB(), getViewBParams());

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    LinearLayout.LayoutParams getViewAParams(){
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.weight = 1;
        return p;
    }

    LinearLayout.LayoutParams getViewBParams(){
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.weight = 1;
        return p;
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
