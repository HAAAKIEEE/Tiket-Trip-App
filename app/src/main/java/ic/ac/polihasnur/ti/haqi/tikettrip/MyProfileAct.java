package ic.ac.polihasnur.ti.haqi.tikettrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileAct extends AppCompatActivity {
    RecyclerView mytiket_place;
    Button btn_sign_out,btn_to_edit;
    ArrayList<MyTicket> list;
    TicketAdapter tiketAdapter;
    LinearLayout item_my_ticket;
    ImageView pic_photo_profile_user;
    TextView full_name_myprof, bio_prof;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference reference, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getUsernameLocal();
        btn_to_edit= findViewById(R.id.btn_to_edit);
        btn_to_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileAct.this,EditProfileAct.class);
                startActivity(intent);
            }
        });
        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//        menyimpan data ke local storage (handpone)
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key,"");
                editor.apply();

// Pindah ke
                Intent intent = new Intent(MyProfileAct.this,GetStartedAct.class);
                startActivity(intent);
                finish();
            }
        });
        full_name_myprof = findViewById(R.id.full_name_myprof);
        pic_photo_profile_user = findViewById(R.id.pic_photo_profile_user);
        bio_prof = findViewById(R.id.bio_prof);
        mytiket_place = findViewById(R.id.mytiket_place);
        mytiket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyTicket>();

        reference = FirebaseDatabase.getInstance().getReference().child("User").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                full_name_myprof.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio_prof.setText(snapshot.child("bio").getValue().toString());
                Picasso.with(MyProfileAct.this).load(snapshot.child("url_photo_profile")
                        .getValue().toString()).centerCrop().fit().into(pic_photo_profile_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference2 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    MyTicket p = snapshot1.getValue(MyTicket.class);
                    list.add(p);
                }
                tiketAdapter = new TicketAdapter(MyProfileAct.this, list);
                mytiket_place.setAdapter(tiketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}