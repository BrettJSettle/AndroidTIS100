package com.bsettle.tis100clone.level;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bsettle.tis100clone.R;

import java.util.ArrayList;
import java.util.Locale;

public class LevelGridAdapter extends ArrayAdapter<LevelTileInfo>{
    private Context mContext;
    private int resourceId;

    public LevelGridAdapter(Context context, int layoutResource, ArrayList<LevelTileInfo> data){
        super(context, layoutResource, data);
        this.mContext = context;
        this.resourceId = layoutResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;

        if (itemView == null)
        {
            final LayoutInflater layoutInflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();
            holder.nameView =  itemView.findViewById(R.id.levelNameView);
            holder.descriptionView = itemView.findViewById(R.id.levelDescriptionView);
            itemView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) itemView.getTag();

        }

        LevelTileInfo item = getItem(position);
        holder.nameView.setText(String.format(Locale.getDefault(), "%d. %s", position, item.getName()));
        holder.descriptionView.setText(item.getDescription());

        return itemView;
    }



    static class ViewHolder
    {
        TextView nameView;
        TextView descriptionView;
    }
}
