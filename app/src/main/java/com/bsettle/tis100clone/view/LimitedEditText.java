package com.bsettle.tis100clone.view;

import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;

import java.util.logging.Logger;

/**
 * EditText subclass created to enforce limit of the lines number in editable
 * text field
 */
public class LimitedEditText extends AppCompatEditText {


    /**
     * Max lines to be present in editable text field
     */
    private int maxLines = 1;

    /**
     * Max characters to be present in editable text field
     */
    private int maxCharacters = 50;

    /**
     * application context;
     */
    private Context context;

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public LimitedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public LimitedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LimitedEditText(Context context) {
        super(context);
        this.context = context;
    }

    public boolean isValid(String s){
        String[] lines = s.split("\n", -1);
        if (lines.length > maxLines){
            return false;
        }
        for (String line : lines){
            if (line.length() > maxCharacters){
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setVerticalScrollBarEnabled(false);
        setFilters(new InputFilter[]{
                new InputFilter.AllCaps(),
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String s = dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length());
                        while (!isValid(s) && end > start){
                            end -= 1;
                            s = dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length());
                        }
                        return source.subSequence(start, end);

                    }
                }
        });

    }

}