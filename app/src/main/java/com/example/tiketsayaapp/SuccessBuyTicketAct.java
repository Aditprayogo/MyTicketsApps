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

public class SuccessBuyTicketAct extends AppCompatActivity {

    Button btn_my_dashboard,btn_view_ticket;
    Animation app_splash, tttb, btt;
    ImageView succes_logo_ticket;
    TextView text_title, text_subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash);
        tttb = AnimationUtils.loadAnimation(this,R.anim.tttb);
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);


        //find content
        btn_my_dashboard = findViewById(R.id.btn_my_dashboard);
        btn_view_ticket = findViewById(R.id.btn_view_ticket);
        succes_logo_ticket = findViewById(R.id.succes_logo_ticket);
        text_title = findViewById(R.id.text_title);
        text_subtitle = findViewById(R.id.text_subtitle);

        //set animation
        succes_logo_ticket.startAnimation(app_splash);
        text_title.startAnimation(tttb);
        text_subtitle.startAnimation(tttb);

        btn_view_ticket.startAnimation(btt);
        btn_my_dashboard.startAnimation(btt);

        btn_view_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyprofile = new Intent(SuccessBuyTicketAct.this, MyProfileAct.class);
                startActivity(gotomyprofile);
            }
        });

        btn_my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomydashboard = new Intent(SuccessBuyTicketAct.this, HomeAct.class);
                startActivity(gotomydashboard);
            }
        });
    }
}
