package com.example.tiketsayaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedAct extends AppCompatActivity {

    //registrasi button
    Button btn_sign_in;
    Button btn_new_account;
    ImageView emblem_logo;
    TextView text_intro;

    Animation tttb,btt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //regis content
        emblem_logo = findViewById(R.id.emblem_logo);
        text_intro = findViewById(R.id.text_intro);

        //load aniamtion
        tttb = AnimationUtils.loadAnimation(this,R.anim.tttb);
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);

        //set animation
        emblem_logo.startAnimation(tttb);
        text_intro.startAnimation(tttb);


        //Daftarkan btn berdasarkan id
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_in.setAnimation(btt);
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosign = new Intent(GetStartedAct.this, SigninAct.class);
                startActivity(gotosign);

            }
        });


        btn_new_account = findViewById(R.id.btn_new_account);
        btn_new_account.setAnimation(btt);
        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonewaccount = new Intent(GetStartedAct.this, RegisterOneAct.class);
                startActivity(gotonewaccount);
            }
        });

    }
}
