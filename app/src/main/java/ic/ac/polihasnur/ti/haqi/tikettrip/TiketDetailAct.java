package ic.ac.polihasnur.ti.haqi.tikettrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class TiketDetailAct extends AppCompatActivity {
    Button btn_checkout;
    TextView title_tiket,lokasi_tiket,festival_tiket,photo_tiket,wifi_tiket,short_desc;
    ImageView header_tiket_detail;
    LinearLayout btn_back_toHome;

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tiket_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_back_toHome = findViewById(R.id.btn_back_toHome);
        btn_back_toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TiketDetailAct.this,HomeAct.class);
                startActivity(intent);
            }
        });
        title_tiket =findViewById(R.id.title_tiket);
        lokasi_tiket =findViewById(R.id.lokasi_tiket);
        festival_tiket =findViewById(R.id.festival_tiket);
        photo_tiket =findViewById(R.id.photo_tiket);
        wifi_tiket =findViewById(R.id.wifi_tiket);
        short_desc =findViewById(R.id.short_desc);
        header_tiket_detail =findViewById(R.id.header_tiket_detail);



//        mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
       final String jenis_tiket_baru = bundle.getString("jenis_tiket");
//        mengamnbil data di fire base berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title_tiket.setText(snapshot.child("nama_wisata").getValue().toString());
                 lokasi_tiket.setText(snapshot.child("lokasi").getValue().toString());
                photo_tiket.setText(snapshot.child("is_photo_spot").getValue().toString());
                wifi_tiket.setText(snapshot.child("is_wifi").getValue().toString());
                festival_tiket.setText(snapshot.child("is_festival").getValue().toString());
                short_desc.setText(snapshot.child("short_desc").getValue().toString());
                Picasso.with(TiketDetailAct.this).load(snapshot.child("url_thumbnile")
                        .getValue().toString()).centerCrop().fit().into(header_tiket_detail);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




//        Toast.makeText(getApplicationContext(),"Jenis Tiket " + jenis_tiket_baru,Toast.LENGTH_LONG).show();

        btn_checkout=findViewById(R.id.btn_checkout);
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TiketDetailAct.this,TiketCheckoutAct.class);
                intent.putExtra("jenis_tiket",jenis_tiket_baru);

                startActivity(intent);
            }
        });
    }

}