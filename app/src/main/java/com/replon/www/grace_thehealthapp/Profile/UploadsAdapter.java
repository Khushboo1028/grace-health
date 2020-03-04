package com.replon.www.grace_thehealthapp.Profile;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.ViewImageActivity;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class UploadsAdapter extends RecyclerView.Adapter<UploadsAdapter.UploadsViewHolder>{


    public static final String TAG = "UploadsAdapter";
    private Activity activity;
    ArrayList<Bitmap> bitmapArrayList;



    public UploadsAdapter(Activity activity, ArrayList<Bitmap> bitmapArrayList) {
        this.activity = activity;
        this.bitmapArrayList=bitmapArrayList;
    }


    @NonNull
    @Override
    public UploadsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater= LayoutInflater.from(activity.getApplicationContext());
        View view=layoutInflater.inflate(R.layout.row_image_layout,null);
        UploadsViewHolder holder=new UploadsViewHolder(view);
        return holder;
    }



    //Will bind data to ViewHolder(UI elements)
    @Override
    public void onBindViewHolder(@NonNull UploadsViewHolder holder, final int i) {


        //so this i is position that will give you the specified item from the product list!


        //So now we bind the data using the help of this contentsComments object we created

        //if image would have been present then
       //holder.images.setImageDrawable(mContext.getResources().getDrawable(uploadsAttachmentContent.getImage(),null));



        Log.i(TAG,"Bitmap is "+bitmapArrayList.get(i));

        holder.images.setImageBitmap(bitmapArrayList.get(i));


        holder.images.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAddDeleteDialog(i,bitmapArrayList);
                return true;
            }
        });

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), ViewImageActivity.class);
                intent.putExtra("imageUri",bitmapArrayList.get(i).toString());
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                activity.getApplicationContext().startActivity(intent);

            }
        });


    }



    public void showAddDeleteDialog(final int pos,final ArrayList<Bitmap> bitmapArrayList){


        AlertDialog.Builder dialog = new AlertDialog.Builder(activity,R.style.AlertDialogCustom);
        dialog.setCancelable(false);
        dialog.setTitle("Are you sure you want to remove attachment?");
        // dialog.setMessage("Are you sure you want to delete this entry?" );
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Add".
                bitmapArrayList.remove(pos);
                notifyDataSetChanged();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    //Will return the size of the list  ie the number of elements available inside the list that is 5
    @Override
    public int getItemCount() {
        return bitmapArrayList.size();
    }

    public class UploadsViewHolder extends RecyclerView.ViewHolder {


       private ImageView images;


        public UploadsViewHolder(@NonNull View itemView) {
            super(itemView);

            images=(ImageView)itemView.findViewById(R.id.images);




        }
    }






}

