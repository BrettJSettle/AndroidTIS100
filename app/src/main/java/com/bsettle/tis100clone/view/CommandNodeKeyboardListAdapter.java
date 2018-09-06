package com.bsettle.tis100clone.view;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bsettle.tis100clone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

    /**
     * Created by reale on 21/11/2016.
     */

    public class CommandNodeKeyboardListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> listDataHeader;
        private HashMap<String,List<String>> listHashMap;

        public CommandNodeKeyboardListAdapter(Context context) {
            this.context = context;
            listDataHeader = new ArrayList<>();
            listHashMap = new HashMap<>();

            listDataHeader.add("Commands");
            List<String> commands = new ArrayList<>();
            commands.add("NOP");
            commands.add("NEG");
            commands.add("SAV");
            commands.add("SWP");
            commands.add("ADD");
            commands.add("SUB");
            commands.add("MOV");
            commands.add("JMP");
            commands.add("JEZ");
            commands.add("JNZ");
            commands.add("JLZ");
            commands.add("JGZ");
            commands.add("JRO");
            listHashMap.put("Commands", commands);

            listDataHeader.add("Registers");
            List<String> registers = new ArrayList<>();
            registers.add("LEFT");
            registers.add("UP");
            registers.add("RIGHT");
            registers.add("DOWN");
            registers.add("LAST");
            registers.add("ANY");
            listHashMap.put("Registers", registers);

            listDataHeader.add("Labels");
            List<String> labels = new ArrayList<>();
            labels.add("A:");
            labels.add("B:");
            labels.add("C:");
            labels.add("D:");
            labels.add("E:");
            labels.add("F:");
            labels.add("G:");
            labels.add("H:");
            labels.add("I:");
            labels.add("J:");
            labels.add("K:");
            listHashMap.put("Labels", labels);

            listDataHeader.add("Numbers");
            List<String> numbers = new ArrayList<>();
            numbers.add("0");
            numbers.add("1");
            numbers.add("2");
            numbers.add("3");
            numbers.add("4");
            numbers.add("5");
            numbers.add("6");
            numbers.add("7");
            numbers.add("8");
            numbers.add("9");
            listHashMap.put("Numbers", numbers);

        }

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return listHashMap.get(listDataHeader.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return listDataHeader.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return listHashMap.get(listDataHeader.get(i)).get(i1); // i = Group Item , i1 = ChildItem
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            String headerTitle = (String)getGroup(i);
            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_group,null);
            }
            TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            final String childText = (String)getChild(i,i1);
            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item,null);
            }

            TextView txtListChild = (TextView)view.findViewById(R.id.lblListItem);
            txtListChild.setText(childText);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

