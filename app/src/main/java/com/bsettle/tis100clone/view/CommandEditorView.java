package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
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
    private Logger logger = Logger.getLogger("CommandEditorView");
    private CharacterStyle span;
    private Vector<CharacterStyle> errorSpans = new Vector<CharacterStyle>();
    private int highlightedLine = -1;


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
        setEnabled(false);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = getText().toString();
                String[] lines = text.split("\n", -1);
                StringBuilder errors = new StringBuilder();
                for (int i = 0; i < lines.length; i++) {
                    Command c = new Command(lines[i]); //node.setCommand(i, lines[i]);
                    ParserException e = c.getError();
                    if (e != null){
                        if (errors.length() > 0){
                            errors.append("\n");
                        }

                        errors.append(String.format(Locale.getDefault(), "Line %d: ", i));
                        errors.append(e.getMessage());
                        CharacterStyle style = new UnderlineSpan();
                        errorSpans.add(style);
                        int start = 0, end = lines[i].length();

                        getText().setSpan(style, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                if (errors.length() != 0){
                    setError(errors.toString());
                }
                else{
                    setError(null);
                }
            }
        });
    }

    public void highlightLine(int line){
        highlightedLine = line;
        invalidate();
//        getText().removeSpan(span);
//        span = null;
//
//        if (line < 0){
//            return;
//        }
//
//        String s = getText().toString();
//        int start = 0;
//        for (int i = 0; i < s.length() && line > 0; i++){
//            if (s.charAt(i) == '\n'){
//                line--;
//                start = i + 1;
//            }
//        }
//        int end = s.indexOf("\n", start);
//        logger.info("HIGHLIGHT: " + line + " " + start + " " + end);
//        span = new BackgroundColorSpan(Color.BLUE);
//        if (end > start) {
//            getText().setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
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


    public void addErrors(HashMap<Integer, ParserException> errorMap){
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
    }

    public void clearErrors(){
        for (CharacterStyle span : errorSpans) {
            getEditableText().removeSpan(span);
        }
        errorSpans.clear();
    }
}
