package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.impl.CommandNode;
import com.bsettle.tis100clone.impl.Node;
import com.bsettle.tis100clone.parse.ParserException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Logger;

public class CommandEditorView extends LimitedEditText {
    private int highlightedLine = -1;
    private Vector<CharacterStyle> errorSpans = new Vector<>();


    public CommandEditorView(Context context) {
        super(context);
        init(null, 0);
    }

    public CommandEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CommandEditorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        setLongClickable(false);
        setMaxLines(Node.MAX_LINES);
        setBackgroundColor(Color.argb(0, 0, 0, 0));
        setMaxCharacters(getResources().getInteger(R.integer.max_characters));
    }

    public void highlightLine(int line){
        highlightedLine = line;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setLineHeight(getHeight() / Node.MAX_LINES);

    }

    public void setLineHeight(int lineHeight) {
        int fontHeight = getPaint().getFontMetricsInt(null);
        setLineSpacing(lineHeight - fontHeight, 1);
    }



    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint(Color.BLACK));

        float fontHeight = getLineHeight();//getPaint().getFontMetrics().bottom - getPaint().getFontMetrics().top;
//        fontHeight += getLineSpacingExtra() - 3;

        Paint p = new Paint();
        p.setColor(Color.RED);
        if (highlightedLine >= 0) {
            canvas.drawRect(0, highlightedLine * fontHeight, getWidth(), (highlightedLine + 1) * fontHeight, p);
        }
        super.draw(canvas);

    }


    public void setErrorSpans(HashMap<Integer, ParserException> errorMap){
        clearErrors();

        Editable edit = getText();
        String[] lines = edit.toString().split("\n", -1);

        StringBuilder errors = new StringBuilder();
        int start = 0;
        for (int lineNum = 0; lineNum < lines.length; lineNum++){
            String line = lines[lineNum];
            if (errorMap.get(lineNum) != null){
                ParserException e = errorMap.get(lineNum);
                if (errors.length() > 0){
                    errors.append("\n");
                }

                errors.append(String.format(Locale.getDefault(), "Line %d: %s", lineNum, e.getMessage()));
                CharacterStyle style = new BackgroundColorSpan(Color.argb(100, 255, 0, 0));
                errorSpans.add(style);
                int end = start + line.length();

                edit.setSpan(style, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            start += line.length() + 1;
        }

        if (errors.length() != 0){
            setError(errors.toString());
        }
        else{
            setError(null);
        }

    }

    public void clearErrors(){
        for (CharacterStyle span : errorSpans) {
            getEditableText().removeSpan(span);
        }
        errorSpans.clear();
    }
}
