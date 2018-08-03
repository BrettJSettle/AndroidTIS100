package com.bsettle.tis100clone.view;

import android.content.Context;

import com.bsettle.tis100clone.impl.Node;

public class BidirectionalPortView extends PortView {

    public BidirectionalPortView(Context context, int orientation, Node nodeA, Node nodeB){
        super(context, orientation, nodeA, nodeB);
    }

    @Override
    public void update(){
        ((UnidirectionalPortView) viewA).update();
        ((UnidirectionalPortView) viewB).update();
    }
}
