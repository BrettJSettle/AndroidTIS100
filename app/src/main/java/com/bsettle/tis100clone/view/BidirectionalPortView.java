package com.bsettle.tis100clone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.impl.Node;

public class BidirectionalPortView extends PortView {

    public BidirectionalPortView(Context context){
        this(context, LinearLayout.HORIZONTAL, null, null);
    }
    public BidirectionalPortView(Context context, AttributeSet attrs){
        this(context, LinearLayout.HORIZONTAL, null, null);
    }
    public BidirectionalPortView(Context context, AttributeSet attrs, String s){
        this(context, LinearLayout.HORIZONTAL, null, null);
    }


    public BidirectionalPortView(Context context, int orientation, Node nodeA, Node nodeB){
        super(context, orientation, nodeA, nodeB);
    }

    @Override
    public void update(){
        ((UnidirectionalPortView) viewA).update();
        ((UnidirectionalPortView) viewB).update();
    }
}
