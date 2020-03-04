package com.replon.www.grace_thehealthapp.Profile;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.util.ArrayList;

public class TimelineMedicineAdapter extends RecyclerView.Adapter <TimelineMedicineAdapter.ViewHolder>  {


    Activity activity;
    ArrayList<ContentsMedicine> medicineList;

    public TimelineMedicineAdapter(Activity activity, ArrayList<ContentsMedicine> medicineList) {
        this.activity = activity;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_medicine_timeline,null);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

//        final ContentsMedicine contents = medicineList.get(i);
//        holder.et_medicine.setText(contents.getMedcine());
//


    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        EditText et_medicine;
        ImageView img_add, img_delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            et_medicine = (EditText) itemView.findViewById(R.id.et_medicine);
            img_add = (ImageView) itemView.findViewById(R.id.img_add);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);

            et_medicine.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    medicineList.get(getAdapterPosition()).setMedcine(et_medicine.getText().toString());

                }
            });


        }

    }
}
