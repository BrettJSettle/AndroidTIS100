package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;

public abstract class PortView extends ViewGroup {

    protected final Node nodeA, nodeB;
    protected View viewA, viewB;
    private final int orientation;

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

        float height = getResources().getDimension(R.dimen.port_size);
        float width = getResources().getDimension(R.dimen.port_size);

        if (orientation == LinearLayout.HORIZONTAL){
            height = getResources().getDimension(R.dimen.node_size) / 2;
        }else if (orientation == LinearLayout.VERTICAL){
            width = getResources().getDimension(R.dimen.node_size) / 2;
        }
//        setOrientation(orientation);
        LayoutParams params = new LayoutParams((int)width, (int)height);
//        params.gravity = Gravity.END;

        setLayoutParams(params);

        addView(getViewA(), getViewAParams());
        addView(getViewB());//, getViewBParams());
    }

    protected RelativeLayout.LayoutParams getViewAParams(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_RIGHT, getViewB().getId());
        return params;
    }

    protected View getViewA(){
        if (viewA == null) {
            if (orientation == LinearLayout.HORIZONTAL) {
                viewA = new UnidirectionalPortView(getContext(), PortToken.UP, nodeA, nodeB);
            }else{
                viewA = new UnidirectionalPortView(getContext(), PortToken.RIGHT, nodeA, nodeB);
            }
        }
        return viewA;
    }

    protected View getViewB(){
        if (viewB == null) {
            if (orientation == LinearLayout.HORIZONTAL) {
                viewB = new UnidirectionalPortView(getContext(), PortToken.DOWN, nodeB, nodeA);
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        viewA.layout(0, 0, 50, 50);
        viewB.layout(viewA.getMeasuredWidth(), 0, 50, 50);

    }

    public abstract void update();
}
