package ic.ac.polihasnur.ti.haqi.tikettrip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterOneAct extends AppCompatActivity {

    LinearLayout btn_back_togrtstart;
    Button btn_countinue_regis1;
    EditText username,password,email_address;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_one);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username=findViewById(R.id.username);
        password=findViewById(R.id.pasword);
        email_address=findViewById(R.id.email_address);
//===============================================================
        btn_back_togrtstart=findViewById(R.id.btn_back_togrtstart);
        btn_back_togrtstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterOneAct.this,SignInAct.class);
                startActivity(intent);
            }
        });
        btn_countinue_regis1=findViewById(R.id.btn_countinue_regis1);
        btn_countinue_regis1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//        menyimpan data ke local storage (handpone)
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key,username.getText().toString());
                editor.apply();
//        menyimpan ke database
                reference = FirebaseDatabase.getInstance().getReference().child("User").child(username.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        snapshot.getRef().child("username").setValue(username.getText().toString());
                        snapshot.getRef().child("password").setValue(password.getText().toString());
                        snapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                        snapshot.getRef().child("user_balance").setValue(800);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(getApplicationContext(),"Username"+username.getText().toString(),Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(RegisterOneAct.this,RegisTwoAct.class);
                startActivity(intent);
            }
        });
    }


}