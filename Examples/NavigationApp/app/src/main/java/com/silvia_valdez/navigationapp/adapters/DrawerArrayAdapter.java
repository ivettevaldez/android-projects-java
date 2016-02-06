package com.silvia_valdez.navigationapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.silvia_valdez.navigationapp.R;

/**
 * Created by Silvia on 31/01/2016.
 * Array adapter to populate the Navigation Drawer.
 */
public class DrawerArrayAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final String[] mValues;

    private int mSelectedItem;

    public DrawerArrayAdapter(Context context, String[] values) {
        super(context, R.layout.item_drawer_view, values);
        this.mContext = context;
        this.mValues = values;
    }

    public void setSelectedItem(int selectedItem) {
        this.mSelectedItem = selectedItem;
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_drawer_view, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.item_drawer_text);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_drawer_image);

        String name = mValues[position];
        textView.setText(name);

        boolean isSelected = position == mSelectedItem;
        highlightItem(isSelected, convertView, imageView, textView);

        return convertView;
    }

    public void highlightItem(boolean isSelected, View convertView,
                              ImageView imageView, TextView textView) {
        // Highlight selected item... or not.
        if (isSelected) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
            imageView.setImageResource(R.drawable.icon_circle_primary);
            textView.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_dark));
            imageView.setImageResource(R.drawable.icon_circle_secondary);
            textView.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
        }
    }

}
