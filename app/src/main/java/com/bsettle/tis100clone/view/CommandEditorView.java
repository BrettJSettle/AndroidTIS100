package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import com.bsettle.tis100clone.R;
import com.bsettle.tis100clone.command.Command;
import com.bsettle.tis100clone.impl.CommandNode;
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
        setMaxLines(CommandNode.MAX_COMMANDS);
        setLineSpacing(0, 1);
        setBackgroundColor(Color.argb(0, 0, 0, 0));
        setMaxCharacters(getResources().getInteger(R.integer.max_characters));
    }

    public void highlightLine(int line){
        Logger.getLogger("CommandEditorView").info("line " + line);
        highlightedLine = line;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), new Paint(Color.BLACK));

        float fontHeight = getPaint().getFontMetrics().bottom - getPaint().getFontMetrics().top - 3;

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
        for (int k : errorMap.keySet()){
            ParserException e = errorMap.get(k);
            if (errors.length() > 0){
                errors.append("\n");
            }

            errors.append(String.format(Locale.getDefault(), "Line %d: ", k));
            errors.append(e.getMessage());
            CharacterStyle style = new UnderlineSpan();
            errorSpans.add(style);
            int start = 0, end = lines[k].length();
//                start = e.getToken().location;
//                end = start + e.getToken().sequence.length();
            edit.setSpan(style, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
