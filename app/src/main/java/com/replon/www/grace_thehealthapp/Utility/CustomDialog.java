package com.replon.www.grace_thehealthapp.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.BuildConfig;
import com.replon.www.grace_thehealthapp.R;

public class CustomDialog {

    public void showMessageOneOption(String title, String message, int image, int color, String btn_text, Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_one_option);

        Button btn_positive = dialog.findViewById(R.id.btn_positive);
        TextView dialog_title = dialog.findViewById(R.id.dialog_title);
        TextView dialog_message = dialog.findViewById(R.id.dialog_message);
        ImageView dialog_icon = dialog.findViewById(R.id.dialog_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog_title.setText(title);
        dialog_message.setText(message);
        btn_positive.setText(btn_text);
        dialog_icon.setImageDrawable(activity.getDrawable(image));

        dialog_icon.setColorFilter(activity.getColor(color), PorterDuff.Mode.SRC_ATOP);

        btn_positive.getBackground().setTint(activity.getColor(color));
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    public void showMessageTwoOptions(String title, String message, int image, int color, final String btn_text, final Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_dialog_two_options);

        Button btn_positive = dialog.findViewById(R.id.btn_positive);
        Button btn_negative = dialog.findViewById(R.id.btn_neagtive);
        TextView dialog_title = dialog.findViewById(R.id.dialog_title);
        TextView dialog_message = dialog.findViewById(R.id.dialog_message);
        ImageView dialog_icon = dialog.findViewById(R.id.dialog_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog_title.setText(title);
        dialog_message.setText(message);
        btn_positive.setText(btn_text);
        dialog_icon.setImageDrawable(activity.getDrawable(image));

        dialog_icon.setColorFilter(activity.getColor(color), PorterDuff.Mode.SRC_ATOP);

        btn_positive.getBackground().setTint(activity.getColor(color));

        if(btn_text.equalsIgnoreCase("permissions")){
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                    activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

                }
            });

            btn_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
        }else{
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }


        dialog.show();
    }
}
