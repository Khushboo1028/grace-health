package com.replon.www.grace_thehealthapp.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AddRecordActivity extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE_MULTIPLE = 18;
    public static final String TAG = "AddRecordActivity";
    public static final int RESULT_CAMERA = 23;
    ImageView back,add_image;
    RecyclerView recyclerView;
    Button btn_save;
    TimelineMedicineAdapter medicineAdapter;
    ArrayList<ContentsMedicine> medicineList;
    RelativeLayout time_layout;
    TextView tv_time;
    AutoCompleteTextView tv_category_spinner;

    ArrayAdapter<String> categoryAdapter;



    Calendar date;

    RecyclerView recyclerViewImages;
    UploadsAdapter uploadsAdapter;

//    ArrayList<Uri> uriArrayList;
    ArrayList<Bitmap> bitmapArrayList;

    Uri file_camera_uri;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageTask uploadTask;
    ArrayList<String> downloadUrlArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), AddRecordActivity.this);
        setContentView(R.layout.activity_add_record);

        init();


        //for camera intent
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        medicineList.add(new ContentsMedicine(""));

        medicineAdapter = new TimelineMedicineAdapter(AddRecordActivity.this, medicineList){


            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

                final ContentsMedicine contents = medicineList.get(i);
                holder.et_medicine.setText(contents.getMedcine());


                holder.img_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medicineList.add(new ContentsMedicine(""));
                        notifyDataSetChanged();
                    }
                });

               if(i!=0){
                   holder.img_delete.setVisibility(View.VISIBLE);
                   holder.img_add.setVisibility(View.GONE);
               }

               holder.img_delete.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       medicineList.remove(i);
                       notifyDataSetChanged();
                   }
               });


            }
        };
        recyclerView.setAdapter(medicineAdapter);

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateAndTimePicker();
            }
        });

        categoryAdapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, getResources().getStringArray(R.array.timeline_category));
        tv_category_spinner.setAdapter(categoryAdapter);

        tv_category_spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv_category_spinner.showDropDown();
                tv_category_spinner.requestFocus();
                return false;
            }
        });

        tv_category_spinner.setSelection(0);

        tv_category_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG,"SELECTED POS" + position);

                switch (position){
                    case 0:
                        tv_category_spinner.setBackground(getDrawable(R.drawable.timeline_sick_bg));
                        break;
                    case 1:
                        tv_category_spinner.setBackground(getDrawable(R.drawable.timeline_hospital_bg));
                        break;
                    case 2:
                        tv_category_spinner.setBackground(getDrawable(R.drawable.timeline_checkup_bg));
                        break;
                }
            }

        });


        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               dialogShowPhoto();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =0; i<bitmapArrayList.size();i++){
                    uploadImage(bitmapArrayList.get(i));
                }
            }
        });

    }


    private void init(){

        back = (ImageView) findViewById(R.id.back);
        add_image = (ImageView) findViewById(R.id.add_photo);

        tv_time = (TextView) findViewById(R.id.tv_time);

        tv_category_spinner = (AutoCompleteTextView)findViewById(R.id.tv_category_spinner);

        time_layout = (RelativeLayout) findViewById(R.id.time_layout);

        btn_save = (Button) findViewById(R.id.btn_save);

        medicineList = new ArrayList<>();
        bitmapArrayList=new ArrayList<>();
        downloadUrlArrayList=new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.medicine_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerViewImages=(RecyclerView)findViewById(R.id.recycler_view_images);
        recyclerViewImages.setHasFixedSize(true);

        uploadsAdapter=new UploadsAdapter(this,bitmapArrayList);
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));//by default manager is vertical
        recyclerViewImages.setAdapter(uploadsAdapter);

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();





    }

    private void showDateAndTimePicker(){
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddRecordActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                date.set(year, monthOfYear, dayOfMonth);

                tv_time.setText(new SimpleDateFormat("MMMM dd, yyyy").format(date.getTime()));
                Log.i(TAG,"Set date is "+date);



            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.show();
    }


    public void dialogShowPhoto() {
        final String takePhoto = "Take Photo";
        final String chooseFromLibrary = "Choose from Gallery";


        final CharSequence[] items = {takePhoto, chooseFromLibrary};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        //builder.setTitle(addPhoto);


        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(chooseFromLibrary)) {


                    if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {



                        if (ActivityCompat.shouldShowRequestPermissionRationale(AddRecordActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) == true){

                            showMessage("Uh-Oh", "Seems like you denied permission to access library! Go on app settings to grant permission");



                        }else {
                            ActivityCompat.requestPermissions(AddRecordActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RESULT_LOAD_IMAGE_MULTIPLE);
                        }



                    }else{
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE_MULTIPLE);
                    }
                }


                else if (items[item].equals(takePhoto)) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                        if (ActivityCompat.shouldShowRequestPermissionRationale(AddRecordActivity.this,
                                Manifest.permission.CAMERA) == true){


                            showMessage("Uh-Oh", "Seems like you denied permission to access camera! Go on app settings to grant permission");


                        }else {
                            ActivityCompat.requestPermissions(AddRecordActivity.this, new String[]{Manifest.permission.CAMERA},
                                    RESULT_CAMERA);


                        }


                    }else{

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        file_camera_uri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,file_camera_uri );
                        startActivityForResult(intent, RESULT_CAMERA);
                    }

                }

            }
        });
        builder.show();


    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Grace");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_LOAD_IMAGE_MULTIPLE);

                showMessage("Uh-Oh", "Please grant permission to access library");

            } else {

                Log.i(TAG,"I am here");

                if(data.getData()!=null) {

                    Uri mImageUri = data.getData();

                    try {
                        Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                        bitmapArrayList.add(bitmapImage);
                        uploadsAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i(TAG,"I am here 2");

                }else {
                    try {

                        if (data.getClipData() != null) {

                            ClipData mClipData = data.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                try {
                                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                    bitmapArrayList.add(bitmapImage);
                                    uploadsAdapter.notifyDataSetChanged();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        }


                    } catch (Exception e) {
                        Toast.makeText(this, "Something went wrong" + e.getMessage(), Toast.LENGTH_LONG)
                                .show();
                        Log.i(TAG, "Error is " + e.getMessage());
                    }
                }
            }


        }

        if (requestCode == RESULT_CAMERA && resultCode == Activity.RESULT_OK) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        RESULT_CAMERA);

            }else {

                Log.i(TAG,"Here");
                if(file_camera_uri!=null) {
                    Log.i(TAG, "Here 2");
                    try {
                        Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file_camera_uri);
                        bitmapArrayList.add(bitmapImage);
                        uploadsAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }



        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RESULT_LOAD_IMAGE_MULTIPLE:
                if (grantResults!=null && grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults!=null && grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RESULT_LOAD_IMAGE_MULTIPLE);
                    }
                }
                break;




            default:
                break;


        }
    }

    private void uploadImage(Bitmap bitmap){
        final StorageReference ref = storageReference.child("timeline_images/"+ UUID.randomUUID().toString());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream); // bmp is bitmap from user image file
        bitmap.recycle();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        uploadTask=ref.putBytes(byteArray);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){

                    String downloadUrl= task.getResult().toString();
                    Log.i(TAG,"DownloadURL is "+downloadUrl);
                    downloadUrlArrayList.add(downloadUrl);
                }else{
                    Log.i(TAG,"An Error Occurred in uploading image" +task.getException().getMessage());
                }
            }
        });
    }

    public void showMessage(String title, String message){
        final AlertDialog.Builder builder=new AlertDialog.Builder(AddRecordActivity.this,R.style.AlertDialogCustom);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
