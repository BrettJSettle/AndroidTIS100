package com.bsettle.tis100clone.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bsettle.tis100clone.R;

public class CommandNodeKeyboard extends LinearLayout implements View.OnClickListener {

    private Button nopButton, negButton, savButton, swpButton, addButton, subButton;

    private InputConnection inputConnection;

    public CommandNodeKeyboard(Context context) {
        this(context, null, 0);
    }

    public CommandNodeKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommandNodeKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){

    }

    @Override
    public void onClick(View view) {
        if (inputConnection == null)
            return;

        if (view.getId() == R.id.button_delete) {
            CharSequence selectedText = inputConnection.getSelectedText(0);

            if (TextUtils.isEmpty(selectedText)) {
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                inputConnection.commitText("", 1);
            }
        } else {
//            String value = keyValues.get(view.getId());
//            inputConnection.commitText(value, 1);
        }
    }

    public void setInputConnection(InputConnection ic) {
        inputConnection = ic;
    }
}
