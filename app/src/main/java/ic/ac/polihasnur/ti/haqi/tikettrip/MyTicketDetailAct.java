package ic.ac.polihasnur.ti.haqi.tikettrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetailAct extends AppCompatActivity {
    DatabaseReference reference,reference2;
    TextView detailt_ketentuan_wisata,detailt_time_wisata,detailt_date_wisata,detailt_lokasi_wisata,detailt_nama_wisata;
    LinearLayout btn_back_toprofile;
    Button btn_refaund;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);
        getUsernameLocal();
        detailt_ketentuan_wisata = findViewById(R.id.detailt_ketentuan_wisata);
        detailt_time_wisata = findViewById(R.id.detailt_time_wisata);
        detailt_date_wisata = findViewById(R.id.detailt_date_wisata);
        detailt_nama_wisata = findViewById(R.id.detailt_nama_wisata);
        detailt_lokasi_wisata = findViewById(R.id.detailt_lokasi_wisata);

        btn_back_toprofile = findViewById(R.id.btn_back_toprofile);
        btn_back_toprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTicketDetailAct.this,MyProfileAct.class);
                startActivity(intent);
            }
        });

//        mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");


        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detailt_nama_wisata.setText(snapshot.child("nama_wisata").getValue().toString());
                detailt_lokasi_wisata.setText(snapshot.child("lokasi").getValue().toString());
                detailt_date_wisata.setText(snapshot.child("date_wisata").getValue().toString());
                detailt_time_wisata.setText(snapshot.child("time_wisata").getValue().toString());
                detailt_ketentuan_wisata.setText(snapshot.child("ketentuan").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_refaund = findViewById(R.id.btn_refaund);
        btn_refaund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menghapus data dari Firebase
                reference2 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new);
                reference2.orderByChild("nama_wisata").equalTo(nama_wisata_baru).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            dataSnapshot.getRef().removeValue(); // Menghapus data tiket
                        }
                        // Setelah dihapus, kembali ke halaman sebelumnya (MyProfileAct)
                        Intent intent = new Intent(MyTicketDetailAct.this, MyProfileAct.class);
                        startActivity(intent);
                        finish(); // Menutup activity ini
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });




    }
    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}