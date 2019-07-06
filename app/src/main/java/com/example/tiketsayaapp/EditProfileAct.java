package com.example.tiketsayaapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileAct extends AppCompatActivity {

    LinearLayout btn_back;

    DatabaseReference reference, reference2;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    ImageView photo_edit_profile;
    Button btn_save_profile, btn_add_new_photo;
    StorageReference storage;


    Uri photo_location;
    Integer photo_max = 1;

    EditText xnama_lengkap, xbio, xusername, xemail_address, xpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getUsernameLocal();

        btn_back = findViewById(R.id.btn_back);
        photo_edit_profile = findViewById(R.id.photo_edit_profile);

        xnama_lengkap = findViewById(R.id.xnama_lengkap);
        xbio = findViewById(R.id.xbio);
        xusername = findViewById(R.id.xusername);
        xemail_address = findViewById(R.id.xemail_address);
        xpassword = findViewById(R.id.xpassword);

        btn_save_profile = findViewById(R.id.btn_save_profile);
        btn_add_new_photo = findViewById(R.id.btn_add_new_photo);


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);

        //menyimpan gambar
        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

        //harus menggunakan value agar selalu update atau kalau pengen real time
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                xnama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                xbio.setText(dataSnapshot.child("bio").getValue().toString());
                xusername.setText(dataSnapshot.child("username").getValue().toString());
                xemail_address.setText(dataSnapshot.child("email_address").getValue().toString());
                xpassword.setText(dataSnapshot.child("password").getValue().toString());
                Picasso.with(EditProfileAct.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_edit_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                        dataSnapshot.getRef().child("nama_lengkap").setValue(xnama_lengkap.getText().toString());
                        dataSnapshot.getRef().child("bio").setValue(xbio.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(xemail_address.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //validasi untuk file (apakah ada)
                if (photo_location != null){

                    final StorageReference storageReference1 =
                            storage.child(System.currentTimeMillis()+ "." + getFileExtension(photo_location));

                    //ketika berhasil di ambil fotonya , kita letakkan di database
                    storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String uri_photo = uri.toString();
                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);

                                }
                            });

                        };

                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            Intent gotomyprofile = new Intent(EditProfileAct.this, MyProfileAct.class);
                            startActivity(gotomyprofile);

                        }
                    });
                    //end complete listener
                }
                //end if

                btn_save_profile.setEnabled(false);
                btn_save_profile.setText("Loading...");



            }
        });
        //end btn-save profile

        btn_add_new_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gobacktomyprofile = new Intent(EditProfileAct.this, MyProfileAct.class);
                finish();
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

    //get file local
    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        //tipe file
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){

            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_edit_profile);
        }
    }
}
