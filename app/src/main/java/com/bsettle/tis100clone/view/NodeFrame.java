package com.bsettle.tis100clone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.bsettle.tis100clone.R;

public class NodeFrame extends FrameLayout{

    private FrameLayout nodeFrame;

    public NodeFrame(Context context) {
        super(context);
        initialize(context);
    }

    public NodeFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public NodeFrame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.node_view, this);

        nodeFrame = findViewById(R.id.nodeFrame);
    }

    private void setView(View view){
        addView(view);
    }
}
