package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.InputNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.OutputNode;
import com.bsettle.tis100clone.impl.PortToken;

public class UnidirectionalPortView extends android.support.v7.widget.AppCompatTextView {

    private RotateDrawable arrow;
    protected Node source, target;
    protected PortToken direction;

    public UnidirectionalPortView(Context context, PortToken direction, Node source, Node target) {
        super(context);
        this.source = source;
        this.target = target;
        this.direction = direction;
        initialize(context);
    }
    public UnidirectionalPortView(Context context) {
        this(context, PortToken.DOWN, null, null);
    }
    public UnidirectionalPortView(Context context, AttributeSet attrs) {
        this(context, PortToken.DOWN, null, null);
    }

    protected void initialize(Context context){
        createDrawable(context);
        setTextColor(Color.WHITE);
    }

    private void updateArrow(boolean filled){
        arrow.setDrawable(getContext().getDrawable(filled ? R.drawable.arrow_occupied : R.drawable.arrow_empty));
    }


    public void createDrawable(Context context){
        RotateDrawable left = null, top = null, right = null, bottom = null;
        Drawable arrow_empty = context.getDrawable(R.drawable.arrow_empty);
        switch (direction) {
            case UP:
                right = new RotateDrawable();
                right.setDrawable(arrow_empty);
                right.setFromDegrees(0);
                right.setToDegrees(-90);
                arrow = right;
                setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                break;
            case DOWN:
                left = new RotateDrawable();
                left.setDrawable(arrow_empty);
                left.setFromDegrees(0);
                left.setToDegrees(90);
                arrow = left;
                setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                break;
            case LEFT:
                top = new RotateDrawable();
                top.setDrawable(arrow_empty);
                top.setFromDegrees(0);
                top.setToDegrees(180);
                arrow = top;
                break;
            case RIGHT:
                bottom = new RotateDrawable();
                bottom.setDrawable(arrow_empty);
                arrow = bottom;
                setGravity(Gravity.BOTTOM);
                break;
        }

        arrow.setBounds(0, 0, 64, 64);
        arrow.setLevel(10000);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }


    public void update(){
        if (direction == null) {
            return;
        }

        PortToken sourceWrite = null;
        if (source != null){
            sourceWrite = source.getState().getWritingPort();
        }
        PortToken targetRead = null;
        if (target != null && !(target instanceof OutputNode)){
            targetRead = target.getState().getReadingPort();
        }
        boolean writing = sourceWrite != null && (sourceWrite.equals(direction) || sourceWrite.equals(PortToken.ANY));
        boolean reading = targetRead != null && (targetRead.equals(direction.reverse()) || targetRead.equals(PortToken.ANY));

        updateArrow(writing || reading);


        if (writing){
            setText(String.valueOf(source.getState().getWritingValue()));
        }else if (reading){
            setText("?");
        }else{
            setText("");
        }
    }
}
