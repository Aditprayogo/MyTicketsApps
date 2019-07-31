package com.example.tiketsayaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class SigninAct extends AppCompatActivity {

    //Mendaftarkan btn new account
    TextView btn_new_account;
    Button btn_sign_in;
    EditText xusername,xpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //find btn new account berdasarkan id
        btn_new_account = findViewById(R.id.btn_new_account);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);


        //set event
        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(SigninAct.this, RegisterOneAct.class);

                startActivity(gotoregisterone);
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String username = xusername.getText().toString();
               final String password = xpassword.getText().toString();

                //jika btn sign in di tekan
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("Loading...");

               if (username.isEmpty()){
                   //jika username kosong
                   Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                   //jika di tekan
                   btn_sign_in.setEnabled(true);
                   btn_sign_in.setText("Sign in");

               } else {
                   //jika username tidak kosong

                   if (password.isEmpty()){
                       //jika username tidak kosong dan password kosong
                       Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                       //jika di tekan
                       btn_sign_in.setEnabled(true);
                       btn_sign_in.setText("Sign in");

                   } else {
                       //jika username tidak kosong dan password tidak kosong
                       //mengambil data dari firebase
                       reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                       reference.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               //jika datanya ada
                               if (dataSnapshot.exists()){

                                   //ambil data password dari firebase
                                   String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                   //validasi password dengan password di firebase
                                   if (password.equals(passwordFromFirebase)){
                                       //jika password benar
                                       //simpan username key kepada local
                                       //menyimpan data kepada lokal storage
                                       SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                       SharedPreferences.Editor  editor = sharedPreferences.edit();
                                       editor.putString(username_key, xusername.getText().toString());
                                       editor.apply();

                                       //pindah activity
                                       Intent gotohome = new Intent(SigninAct.this, HomeAct.class);
                                       startActivity(gotohome);
                                       finish();

                                   }else{
                                       //jika password salah
                                       Toast.makeText(getApplicationContext(), "Password salah", Toast.LENGTH_SHORT).show();
                                       //jika di tekan
                                       btn_sign_in.setEnabled(true);
                                       btn_sign_in.setText("Sign in");
                                   }

                               }else {
                                   //jika username tidak ada
                                   Toast.makeText(getApplicationContext(), "Username tidak ada", Toast.LENGTH_SHORT).show();
                                   //jika di tekan
                                   btn_sign_in.setEnabled(true);
                                   btn_sign_in.setText("Sign in");

                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                               Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();

                           }
                       });

                   }
                   //end else username & password tidak kosong

               }
               //end else




            }
        });

    }
}
