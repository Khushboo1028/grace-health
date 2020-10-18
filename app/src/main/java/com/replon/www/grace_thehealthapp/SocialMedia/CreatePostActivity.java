package com.replon.www.grace_thehealthapp.SocialMedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.UserDataStore;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    public static final String TAG = "CreatePostActivity";
    ImageView back, create_post,post_image;

    EditText et_content;

    public static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 69;
    public static final int HOME_IMAGE = 12;
    public static final int RESULT_LOAD_IMAGE_MULTIPLE = 18;

    CustomDialog customDialog;

    Uri postImageUri;

    Spinner spinner_community;
    TextView tv_name;

    UserDataStore userDataStore;
    String[] communitiesList;

    String auth_token;

    private RequestQueue rq;

    //firebase
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageTask uploadTask;

    String downloadUrl;

    RotateLoading rotateLoading;
    Bitmap bitmapImage;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    ListenerRegistration listenerRegistration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), CreatePostActivity.this);

        setContentView(R.layout.activity_create_post);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals("")){
                    create_post.setEnabled(false);
                    create_post.setImageAlpha(80);
                }
                else {
                    create_post.setEnabled(true);
                    create_post.setImageAlpha(255);
                }

            }
        });

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePostActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)){



                    }else {
                        ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                    }



                }else{
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, HOME_IMAGE);
                }
            }
        });

        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postImageUri!=null){
                    uploadImage(bitmapImage);
                }else {
                    createPost();
                }

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.community_spinner_item, communitiesList);

//        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinner_community.setAdapter(dataAdapter);


        //get Name
        listenerRegistration = db.collection(getString(R.string.USERS)).document(firebaseUser.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.e(TAG,"Error",error);
                        }else{
                            if( snapshot!=null && snapshot.exists()){
                               if(snapshot.getString(getString(R.string.NAME))!=null){
                                   tv_name.setText(snapshot.getString(getString(R.string.NAME)) + " ➤ ");
                                }
                            }
                        }
                    }
                });
    }



    private void init(){
        back = (ImageView)findViewById(R.id.back);
        create_post = (ImageView) findViewById(R.id.image_check);
        create_post.setEnabled(false);
        create_post.setImageAlpha(80);
        post_image = (ImageView) findViewById(R.id.post_image);

        tv_name = (TextView) findViewById(R.id.tv_name);
        et_content = (EditText) findViewById(R.id.et_content);

        spinner_community = (Spinner) findViewById(R.id.spinner_community);

        communitiesList = getResources().getStringArray(R.array.communities_list);

        customDialog = new CustomDialog();

        userDataStore = new UserDataStore(CreatePostActivity.this);

        rotateLoading = (RotateLoading) findViewById(R.id.rotateLoading);
        rotateLoading.stop();

        //firebase
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        rq = Volley.newRequestQueue(CreatePostActivity.this);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HOME_IMAGE && resultCode == Activity.RESULT_OK) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

                customDialog.showMessageOneOption(
                        "Oh Snap!",
                        "Please grant permission to access library",
                        R.drawable.ic_error,
                        R.color.pink,
                        "Dismiss",
                        CreatePostActivity.this
                );

            } else {

                if(data.getData()!=null) {
                    postImageUri= data.getData();
                    try {
                      bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), postImageUri);
                      post_image.setImageBitmap(bitmapImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


        }
    }

    private void uploadImage(Bitmap bitmap){

        create_post.setEnabled(false);
        rotateLoading.start();
        final StorageReference ref = storageReference.child("post_images/"+ UUID.randomUUID().toString());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream); // bmp is bitmap from user image file
        bitmap.recycle();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        uploadTask=ref.putBytes(byteArray);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    rotateLoading.stop();
                    create_post.setEnabled(true);
                    Toast.makeText(getApplicationContext(),"Some error occurred. Please try again later!",Toast.LENGTH_SHORT).show();
                    throw task.getException();

                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    downloadUrl= task.getResult().toString();
                    Log.i(TAG,"DownloadURL is "+downloadUrl);
                    createPost();
                }else{
                    rotateLoading.stop();
                    create_post.setEnabled(true);
                    Toast.makeText(getApplicationContext(),"Some error occurred. Please try again later!",Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"An Error Occurred in uploading image" +task.getException().getMessage());
                }
            }
        });


    }
    private void createPost(){


        create_post.setEnabled(false);
        rotateLoading.start();
        String url = getString(R.string.BASE_URL) + "/post";

        DocumentReference docRef = db.collection(getString(R.string.POSTS)).document();

        HashMap<String, Object> data = new HashMap<>();
        data.put(getString(R.string.CONTENT),et_content.getText().toString().trim());
        data.put(getString(R.string.COMMUNITY_NAME),spinner_community.getSelectedItem());
        data.put(getString(R.string.DATE_CREATED), new Timestamp(new Date()));
        data.put(getString(R.string.USER_ID), firebaseUser.getUid());

        if(downloadUrl!=null){
                data.put(getString(R.string.IMAGE_URL), downloadUrl);
        }

        docRef.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        rotateLoading.stop();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        rotateLoading.stop();
                        create_post.setEnabled(true);
                        customDialog.showMessageOneOption(
                        "Oh Snap!",
                        e.getLocalizedMessage(),
                        R.color.pink,
                        R.drawable.ic_error,
                        "Dismiss",
                        CreatePostActivity.this);
                    }
                });


//        JSONObject paramJson = new JSONObject();
//
//        try {
//            paramJson.put("content", et_content.getText().toString().trim());
//            paramJson.put("community_name", spinner_community.getSelectedItem());
//            if(downloadUrl!=null){
//                paramJson.put("image_url", downloadUrl);
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        JSONObject userJSON = userDataStore.readUserData();

//        if (userJSON!=null) {
//            if (!userJSON.has("auth_token")) {
//                customDialog.showMessageOneOption(
//                        "Oh Snap!",
//                        "You need to login to see your feed",
//                        R.color.pink,
//                        R.drawable.ic_error,
//                        "Dismiss",
//                        CreatePostActivity.this);
//            } else {
//
//                try {
//                    auth_token = userJSON.getString("auth_token");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, paramJson, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        setResult(Activity.RESULT_OK);
//                        finish();
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        rotateLoading.stop();
//                        create_post.setEnabled(true);
//                        Log.i(TAG,"SOME ERROR: " + error.getMessage());
//                    }
//                })
//                {
//                    @Override
//                    public Map getHeaders() throws AuthFailureError {
//                        HashMap headers = new HashMap();
//                        headers.put("Content-Type", "application/json");
//                        headers.put("x-auth", auth_token);
//                        return headers;
//                    }
//                };
//
//                rq.add(jsonObjectRequest);



//            }
//        }


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listenerRegistration!=null){
            listenerRegistration=null;
        }
    }
}
