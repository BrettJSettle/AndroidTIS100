package com.bsettle.tis100clone.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsettle.tis100clone.R;

import java.util.Locale;
import java.util.Vector;

public class IOColumnView extends LinearLayout {
    private LinearLayout columnLayout;
    private TextView headerView;
    private Vector<TextView> lines = new Vector<>();
    private int selectedLine = -1;

    public IOColumnView(Context context) {
        super(context);
        initialize(context);
    }

    public IOColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public IOColumnView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }



    private void initialize(Context context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.io_column_view, this);

        headerView = findViewById(R.id.header);
        headerView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        columnLayout = findViewById(R.id.io_column_layout);
    }

    public void setHeader(String header){
        headerView.setText(header);
    }

    public int getLineCount(){
        return lines.size();
    }

    public TextView addRow(String i){
        TextView tv = new TextView(getContext());
        tv.setTextColor(Color.WHITE);
        tv.setText(i);
        tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        columnLayout.addView(tv);
        lines.add(tv);
        return tv;
    }

    public void setRow(int row, String val){
        if (row >= 0 && row < lines.size()){
            lines.get(row).setText(val);
        }
    }

    public void clear(){
        columnLayout.removeAllViews();
        lines.clear();
    }

    public void setData(Vector<Integer> nums){
        clear();
        for (Integer i : nums){
            addRow(String.format(Locale.getDefault(), "%d", i));
        }
    }

    public TextView getCurrentRow(){
        return selectedLine >= 0 && selectedLine < lines.size() ? lines.get(selectedLine) : null;
    }

    public void setSelectedLine(int line){
        if (selectedLine >= 0 && selectedLine < lines.size()) {
            lines.get(selectedLine).setBackgroundColor(Color.BLACK);
            lines.get(selectedLine).setTextColor(Color.WHITE);
        }
        selectedLine = line;
        if (line >= 0 && line < lines.size()) {
            lines.get(line).setBackgroundColor(Color.WHITE);
            lines.get(line).setTextColor(Color.BLACK);
        }
    }
}
