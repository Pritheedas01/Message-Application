package com.pritha.www.otpverification;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.pritha.www.otpverification.Model.Upload;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 1;

    private Button chooseImage, btnUploadImage,remove,set;
    private TextView viewGallery;
    private ImageView imgPreview,btn1;
    private EditText imgDescription;
    private ProgressBar uploadProgress;
    private String image;
    private Uri imgUrl;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private static  final int CAMERA_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadProgress = findViewById(R.id.uploadProgress);
        chooseImage = findViewById(R.id.chooseImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);

        viewGallery = findViewById(R.id.viewGallery);
        remove=findViewById(R.id.remove);
        set =findViewById(R.id.setuser);
        imgDescription = findViewById(R.id.imgDescription);
        imgPreview = findViewById(R.id.imgPreview);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        /*btn1 = findViewById(R.id.btn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }
        });*/

        viewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(MainActivity.this, "Upload in progress", Toast.LENGTH_LONG).show();
                } else {
                    uploadImage();
                }
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uploadID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", uploadID);
                hashMap.put("username", imgDescription.getText().toString().trim());
                hashMap.put("imageUrl", "default");
                hashMap.put("status", "online");
                hashMap.put("typingto", "noOne ");
                mDatabaseRef.child(uploadID).setValue(hashMap);

            }
        });
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Users");
        String userid =FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query usQuery=mDatabaseRef.orderByChild("id").equalTo(userid);
        final String myid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        usQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String name=""+ds.child("username").getValue();
                    String pic=""+ds.child("imageUrl").getValue();

                    imgDescription.setText(name);
                    try{
                        Picasso.get().load(pic).placeholder(R.mipmap.ic_launcher_round).into(imgPreview);

                    }catch (Exception e){
                        Picasso.get().load(R.mipmap.ic_launcher_round).into(imgPreview);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uploadID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", uploadID);
                hashMap.put("username", imgDescription.getText().toString().trim());
                hashMap.put("imageUrl", image);
                hashMap.put("status", "online");
                hashMap.put("typingto", "noOne ");
                mDatabaseRef.child(uploadID).setValue(hashMap);
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChoose();
            }
        });
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK  && data != null) {
            Uri uri=data.getData();
            StorageReference filepath = mStorageRef.child("Photo").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this,"Uploading Finish",Toast.LENGTH_SHORT).show();

                }
            });

        }

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null) {
            imgUrl = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUrl);
                imgPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        if (imgUrl != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imgUrl));

            mUploadTask = fileReference.putFile(imgUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadProgress.setProgress(0);
                                }
                            }, 500);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Upload upload = new Upload(imgDescription.getText().toString().trim(), uri.toString());
                                    String uploadID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    image=uri.toString();
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("id", uploadID);
                                    hashMap.put("username", imgDescription.getText().toString().trim());
                                    hashMap.put("imageUrl", uri.toString());
                                    hashMap.put("status", "online");
                                    hashMap.put("typingto", "noOne ");
                                    mDatabaseRef.child(uploadID).setValue(hashMap);

                                    //  HashMap<String, Object> hashMap=new HashMap<>();
                                    //  hashMap.put("imageUrl",uri);
                                    //  String myid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    //   mDatabaseRef.child(myid).updateChildren(hashMap);
                                      Toast.makeText(MainActivity.this, "Upload successfully", Toast.LENGTH_LONG).show();
                                    //imgPreview.setImageResource(R.drawable.ic_add_image);
                                    imgDescription.setText("");
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgress.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(MainActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}