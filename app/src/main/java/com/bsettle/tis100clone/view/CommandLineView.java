package com.bsettle.tis100clone.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bsettle.tis100clone.R;

public class CommandLineView extends android.support.v7.widget.AppCompatTextView {

    public CommandLineView(Context context) {
        super(context);
        init(null, 0);
    }


    public CommandLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CommandLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setBackgroundColor(Color.BLACK);
        setTextColor(Color.WHITE);
        setIncludeFontPadding(false);
        setPadding(0, 0, 0, 0);
    }

    public boolean isEmpty(){
        return getText().toString().isEmpty();
    }
}
