package com.replon.www.grace_thehealthapp.Nearby;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.R;

public class NearbyGridAdapter extends BaseAdapter {

    private Activity activity;

    private LayoutInflater inflater;

    private int icons[];
    private String names[];

    public NearbyGridAdapter(Activity activity, int[] icons, String[] names) {
        this.activity = activity;
        this.icons = icons;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView=convertView;

        if(convertView==null){

            inflater=(LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView=inflater.inflate(R.layout.row_nearby_grid,null);
        }

        ImageView icon=(ImageView) gridView.findViewById(R.id.image_grid);
        TextView name=(TextView)gridView.findViewById(R.id.tv_place_grid);

        icon.setImageResource(icons[position]);

        name.setText(names[position]);

        return gridView;
    }
}
