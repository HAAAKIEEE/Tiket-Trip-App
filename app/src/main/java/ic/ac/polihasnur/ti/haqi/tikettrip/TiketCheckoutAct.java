package ic.ac.polihasnur.ti.haqi.tikettrip;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Random;

public class TiketCheckoutAct extends AppCompatActivity {
LinearLayout btn_back_toTikDetail;
    TextView nama_wisata,lokasi,ketentuan,your_balance,price,jumlah_tiket;
    Integer value_jumlahtiket = 1;
    Integer myblance = 0;
    Integer sisa_balance = 0;

    Integer vlauetotalharga = 0;
    Integer valuehargatiket = 0;
    String date_wisata = "";
    String time_wisata = "";
    DatabaseReference reference,reference2,reference3,reference4;
Button btn_pay;
    ImageView btn_plus,btn_min;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
//    generate no rendom untuk tiket karna ingin membuat transaksi secara unik
    Integer no_tiket = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getUsernameLocal();
        setContentView(R.layout.activity_tiket_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        btn_back_toTikDetail = findViewById(R.id.btn_back_toTikDetail);
        btn_back_toTikDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TiketCheckoutAct.this,TiketDetailAct.class);
                startActivity(intent);
            }
        });
        nama_wisata =findViewById(R.id.nama_wisata);
        lokasi =findViewById(R.id.lokasi);
        ketentuan =findViewById(R.id.ketentuan);
        your_balance =findViewById(R.id.your_balance);
        price =findViewById(R.id.price);
        btn_min =findViewById(R.id.btn_min);
        btn_plus =findViewById(R.id.btn_plus);
        btn_pay = findViewById(R.id.btn_pay);
        jumlah_tiket =findViewById(R.id.jumlah_tiket);

        jumlah_tiket.setText(value_jumlahtiket.toString());

        btn_min.animate().alpha(0).setDuration(300).start();
        btn_min.setEnabled(false);






//        mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");




//        mengamnbil data di fire base berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_wisata.setText(snapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(snapshot.child("lokasi").getValue().toString());
                ketentuan.setText(snapshot.child("ketentuan").getValue().toString());
                date_wisata = snapshot.child("date_wisata").getValue().toString();
                time_wisata = snapshot.child("time_wisata").getValue().toString();


                valuehargatiket = Integer.valueOf(snapshot.child("harga_tiket").getValue().toString());

                vlauetotalharga = value_jumlahtiket * valuehargatiket ;
                price.setText("US$ "+vlauetotalharga+"");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        mengambil data user dari fire base
        reference2 = FirebaseDatabase.getInstance().getReference().child("User").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myblance = Integer.valueOf(snapshot.child("user_balance").getValue().toString());
                your_balance.setText("US$ "+myblance+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value_jumlahtiket+=1;
                jumlah_tiket.setText(value_jumlahtiket.toString());
                if (value_jumlahtiket > 1){
                    btn_min.animate().alpha(1).setDuration(300).start();
                    btn_min.setEnabled(true);
                }
                vlauetotalharga = value_jumlahtiket * valuehargatiket ;
                price.setText("US$ "+vlauetotalharga+"");
                if (vlauetotalharga > myblance){
                    btn_pay.animate().translationY(0).alpha(0).setDuration(300).start();
                    btn_pay.setEnabled(false);
                }
            }
        });
        btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value_jumlahtiket-=1 ;
                jumlah_tiket.setText(value_jumlahtiket.toString());
                if (value_jumlahtiket < 2){
                    btn_min.animate().alpha(0).setDuration(300).start();
                    btn_min.setEnabled(false);
                }
                vlauetotalharga = value_jumlahtiket * valuehargatiket ;
                price.setText("US$ "+vlauetotalharga+"");

                if (vlauetotalharga <= myblance) {
                    btn_pay.animate().translationY(0).alpha(1).setDuration(300).start();

                    btn_pay.setEnabled(true);
                }
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//        menyimpan data user ke firebase dan membuat tabel baru myTickets
                reference3 = FirebaseDatabase.getInstance()
                        .getReference().child("MyTickets")
                        .child(username_key_new).child(nama_wisata.getText().toString() + no_tiket);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference3.getRef().child("id_tiket").setValue(nama_wisata.getText().toString() + no_tiket);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(value_jumlahtiket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
// upadate data user
                reference4 = FirebaseDatabase.getInstance().getReference().child("User").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sisa_balance = myblance - vlauetotalharga;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(TiketCheckoutAct.this, SuccsessBuyTicketAct.class);
                startActivity(intent);
            }
        });



    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}