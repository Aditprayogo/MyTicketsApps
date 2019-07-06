package com.example.tiketsayaapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessRegisterAct extends AppCompatActivity {


    Animation app_splash, btt, tttb;
    Button btn_explore;
    ImageView icon_success;
    TextView app_title, app_subtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        //find content
        icon_success = findViewById(R.id.icon_success);
        app_title = findViewById(R.id.app_title);
        app_subtitle = findViewById(R.id.app_subtitle);
        btn_explore = findViewById(R.id.btn_explore);

        //regis animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        tttb = AnimationUtils.loadAnimation(this, R.anim.tttb);

        //set animation
        icon_success.startAnimation(app_splash);
        app_title.startAnimation(tttb);
        app_subtitle.startAnimation(tttb);
        btn_explore.startAnimation(btt);

        //set click
        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome= new Intent(SuccessRegisterAct.this, HomeAct.class);
                startActivity(gotohome);
            }
        });


    }
}
