package com.example.tiketsayaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_pay_now, btn_minus, btn_plus;
    TextView textjumlahtiket, texttotalharga, textmybalance, nama_wisata, lokasi, ketentuan;
    ImageView notice_uang;

    Integer valuejumlahticket = 1;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    Integer mybalance = 0;

    DatabaseReference reference, reference2, reference3 , reference4;


    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";

    Integer sisa_balance = 0;

    //generate nomor
    //agar membuat transaksi secara unik
    Integer nomor_transaksi = new Random().nextInt();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        //jalankan function agar mendapatkan user yang sedang login
        getUsernameLocal();

        //mengambil data dari intent yang dikirim dari home
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        //regis content
        btn_pay_now = findViewById(R.id.btn_pay_now);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        btn_back = findViewById(R.id.btn_back);

        textjumlahtiket = findViewById(R.id.textjumlahtiket);
        texttotalharga = findViewById(R.id.texttotalharga);
        textmybalance = findViewById(R.id.textmybalance);

        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);

        notice_uang = findViewById(R.id.notice_uang);


        //setting value baru
        textjumlahtiket.setText(valuejumlahticket.toString());


        //default , hide btn minus
        btn_minus.animate().alpha(0).setDuration(300).start();
        btn_minus.setEnabled(false);

        //default hide notice harga
        notice_uang.setVisibility(View.GONE);

        //mengambil data user dari firebase
        //mengambil data user yang sedang login dengan username_key_new
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //mengambil user balance dari firebase
                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                textmybalance.setText("US$ "+ mybalance+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //mengambil data dari firebase
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Wisata")
                .child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //menimpa data yang ada dengan data yang baru
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());

                //memperbarui sebuah string
                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                valuehargatiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                //meletakan harga harus setelah pengambilan data dari database
                //setting value harga
                valuetotalharga = valuehargatiket * valuejumlahticket;
                texttotalharga.setText("US$"+valuetotalharga+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //end refrence

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valuejumlahticket+=1;
                textjumlahtiket.setText(valuejumlahticket.toString());

                if (valuejumlahticket > 1){
                    btn_minus.animate().alpha(1).setDuration(300).start();
                    btn_minus.setEnabled(true);
                }

                //setting harga
                valuetotalharga = valuehargatiket * valuejumlahticket;
                texttotalharga.setText("US$"+valuetotalharga+"");

                //kondisi jika harga tiket melampaui my balance
                if (valuetotalharga > mybalance){

                    //menghilangkan btn pay now
                    btn_pay_now.animate().translationY(0).alpha(0).setDuration(350).start();
                    btn_pay_now.setEnabled(false);

                    //set color menjadi merah
                    textmybalance.setTextColor(Color.parseColor("#D81B60"));

                    //menampilkan icon notice = visible
                    notice_uang.setVisibility(View.VISIBLE);
                }
            }
        });
        //end btn plus

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valuejumlahticket-=1;
                textjumlahtiket.setText(valuejumlahticket.toString());

                if (valuejumlahticket < 2){

                    btn_minus.animate().alpha(0).setDuration(300).start();
                    btn_minus.setEnabled(false);
                }

                //setting harga
                valuetotalharga = valuehargatiket * valuejumlahticket;
                texttotalharga.setText("US$"+valuetotalharga+"");

                //kondisi jika harga tiket melampaui my balance
                if (valuetotalharga < mybalance){

                    //Alpha untuk oppacity , me-enable kan tombol paynow
                    btn_pay_now.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_pay_now.setEnabled(true);

                    //merubah text menjadi biru
                    textmybalance.setTextColor(Color.parseColor("#203DD1"));

                    //menghilangkan notice uang
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodetailmenu = new Intent(TicketCheckoutAct.this, TicketDetailAct.class);
                finish();
            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //menyimpan tiket dengan refrence3 dan membuat tabel baru
                reference3 = FirebaseDatabase.getInstance().getReference()
                        .child("MyTickets")
                        .child(username_key_new)
                        .child(nama_wisata.getText().toString() + nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(valuejumlahticket.toString());


                        //mengambil data string dari refrence
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);

                        Intent gotosuccessticket = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                        startActivity(gotosuccessticket);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //end refrence3

                //update data balance kepada user yang saat ini login
                reference4 = FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //update sisa balance
                        sisa_balance = mybalance - valuetotalharga;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //end refrence4

            }
        });
        //end btn-paynow


    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
