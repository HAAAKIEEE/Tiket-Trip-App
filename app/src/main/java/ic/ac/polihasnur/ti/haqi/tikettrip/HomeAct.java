package ic.ac.polihasnur.ti.haqi.tikettrip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAct extends AppCompatActivity {
    ImageView profil_pic;
    TextView bio, full_name, user_balance;
    LinearLayout btn_tiket_candi,btn_tiket_tori,btn_tiket_pisca,btn_tiket_pagoda,btn_tiket_spinx,btn_tiket_monas;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        getUsernameLocal();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profil_pic = findViewById(R.id.profil_pic);
        bio = findViewById(R.id.bio);
        full_name = findViewById(R.id.full_name);
        user_balance = findViewById(R.id.user_balance);

        profil_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goprofile = new Intent(HomeAct.this,MyProfileAct.class);
                startActivity(goprofile);
            }
        });

//        memasukkan data firebase
        reference = FirebaseDatabase.getInstance().getReference().child("User").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                full_name.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio.setText(snapshot.child("bio").getValue().toString());
                user_balance.setText("US$ "+snapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeAct.this).load(snapshot.child("url_photo_profile")
                        .getValue().toString()).centerCrop().fit().into(profil_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        borobudur
        btn_tiket_candi = findViewById(R.id.btn_tiket_candi);
        btn_tiket_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this, TiketDetailAct.class);
                intent.putExtra("jenis_tiket","Borobudur");
                startActivity(intent);
            }
        });
//         pisca
        btn_tiket_pisca = findViewById(R.id.btn_tiket_pisca);
        btn_tiket_pisca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this, TiketDetailAct.class);
                intent.putExtra("jenis_tiket","Pisa");
                startActivity(intent);
            }
        });
//          Tori
        btn_tiket_tori = findViewById(R.id.btn_tiket_tori);
        btn_tiket_tori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this, TiketDetailAct.class);
                intent.putExtra("jenis_tiket","Tori");
                startActivity(intent);
            }
        });
//          Pagoda
        btn_tiket_pagoda = findViewById(R.id.btn_tiket_pagoda);
        btn_tiket_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this, TiketDetailAct.class);
                intent.putExtra("jenis_tiket","Pagoda");
                startActivity(intent);
            }
        });
//          Spinx
        btn_tiket_spinx = findViewById(R.id.btn_tiket_spink);
        btn_tiket_spinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this, TiketDetailAct.class);
                intent.putExtra("jenis_tiket","Spinx");
                startActivity(intent);
            }
        });
//          Monas
        btn_tiket_monas = findViewById(R.id.btn_tiket_monas);
        btn_tiket_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this, TiketDetailAct.class);
                intent.putExtra("jenis_tiket","Monas");
                startActivity(intent);
            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}