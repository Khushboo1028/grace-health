package com.replon.www.grace_thehealthapp.Utility;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.Calories.LogMealActivity;
import com.replon.www.grace_thehealthapp.R;

import java.util.List;

public class DataDisplayAdapter extends RecyclerView.Adapter<DataDisplayAdapter.DataViewHolder> {

    private Activity activity;
    private List<ContentsDataDisplay> dataList;

    public DataDisplayAdapter(Activity activity, List<ContentsDataDisplay> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity);
        View view=layoutInflater.inflate(R.layout.row_data_display,null);
        DataViewHolder holder=new DataViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int i) {

        final ContentsDataDisplay contents = dataList.get(i);
        holder.amount.setText(String.valueOf(contents.getAmount()) + " " + contents.getUnits());
        holder.date.setText(contents.getDate());


//        To CHECK FROM WHICH ACTIVITY THE DATA IS COMING USE
//        if(activity instanceof LogMealActivity){
//
//        }

//        if (i==dataList.size()-1){
//            holder.separator.setVisibility(View.GONE);
//        }


    }






    @Override
    public int getItemCount() {
        return dataList.size();

    }


    class DataViewHolder extends RecyclerView.ViewHolder {


        TextView amount,date;
        View separator;


        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = (TextView)itemView.findViewById(R.id.amount);
            date = (TextView)itemView.findViewById(R.id.date);
            separator = (View) itemView.findViewById(R.id.separator);



        }
    }
}
