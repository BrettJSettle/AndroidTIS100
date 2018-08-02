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
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.impl.PortToken;

public class UnidirectionalPortView extends RelativeLayout {

//    protected ImageView arrowImage;
    protected TextView valueLabel;
    private Drawable arrow;
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
        valueLabel = new TextView(context);
        addView(valueLabel);
        createDrawable(context, false);
    }



    public void createDrawable(Context context, boolean filled){
        RotateDrawable left = null, top = null, right = null, bottom = null;
        Drawable arrow_empty = context.getDrawable(filled ? R.drawable.arrow_occupied : R.drawable.arrow_empty);

        switch (direction) {
            case UP:
                right = new RotateDrawable();
                right.setDrawable(arrow_empty);
                right.setFromDegrees(0);
                right.setToDegrees(-90);
                arrow = right;
//                setVerticalGravity(CENTER_VERTICAL);
//                setHorizontalGravity(Gravity.END);
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                lp.addRule(ALIGN_PARENT_BOTTOM);
//                valueLabel.setLayoutParams(lp);
                break;
            case DOWN:
                left = new RotateDrawable();
                left.setDrawable(arrow_empty);
                left.setFromDegrees(0);
                left.setToDegrees(90);
                arrow = left;
//                setVerticalGravity(CENTER_VERTICAL);
//                setHorizontalGravity(Gravity.START);
                break;
            case LEFT:
                top = new RotateDrawable();
                top.setDrawable(arrow_empty);
                top.setFromDegrees(0);
                top.setToDegrees(180);
                arrow = top;
//                setVerticalGravity(Gravity.TOP);
                break;
            case RIGHT:
                bottom = new RotateDrawable();
                bottom.setDrawable(arrow_empty);
                arrow = bottom;
//                setVerticalGravity(Gravity.BOTTOM);
                break;
        }

        arrow.setBounds(0, 0, 64, 64);
        arrow.setLevel(10000);

        valueLabel.setCompoundDrawables(left, top, right, bottom);
        valueLabel.setText("999");
        setTextAlignment(TEXT_ALIGNMENT_CENTER);

//        valueLabel.setTextAlignment(TEXT_ALIGNMENT_CENTER);
//        valueLabel.setGravity(CENTER_VERTICAL);
        valueLabel.setBackgroundColor(Color.BLUE);
    }


    public boolean isValidPort(PortToken dir){
        return dir != null && (dir.equals(PortToken.ANY) || dir.equals(direction) || dir.equals(direction.reverse()));
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
        if (target != null){
            targetRead = target.getState().getReadingPort();
        }
        boolean writing = sourceWrite != null && (sourceWrite.equals(direction) || sourceWrite.equals(PortToken.ANY));
        boolean reading = targetRead != null && (targetRead.equals(direction.reverse()) || targetRead.equals(PortToken.ANY));

//        updateDrawable(getContext(), writing || reading);
//        arrowImage.setImageResource(writing || reading ? R.drawable.arrow_occupied : R.drawable.arrow_empty);

//        if (writing){
//            valueLabel.setText(String.valueOf(source.getState().getWritingValue()));
//        }else if (reading){
//            valueLabel.setText("?");
//        }else{
//            valueLabel.setText("");
//        }
    }
}
