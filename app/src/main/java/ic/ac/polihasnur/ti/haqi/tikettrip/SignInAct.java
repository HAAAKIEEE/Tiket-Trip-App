package ic.ac.polihasnur.ti.haqi.tikettrip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInAct extends AppCompatActivity {
    EditText username_login,password_login;
    Button btn_login;
    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        btn_login = findViewById(R.id.btn_login);
        username_login = findViewById(R.id.username_login);
        password_login = findViewById(R.id.pasword_login);

btn_login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        btn_login.setEnabled(false);
        btn_login.setText("Loading . . .");
        String username = username_login.getText().toString();
        String passowrd = password_login.getText().toString();

        if (username.isEmpty()){
            Toast.makeText(getApplicationContext(),"Username Tidak Boleh Kosong !",Toast.LENGTH_LONG).show();
            btn_login.setEnabled(true);
            btn_login.setText("SIGN IN");
        }
        else {
            if (passowrd.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Password Tidak Boleh Kosong !",Toast.LENGTH_LONG).show();
                btn_login.setEnabled(true);
                btn_login.setText("SIGN IN");
            }
            else {

                reference = FirebaseDatabase.getInstance().getReference().child("User").child(username);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
//                    ambil data pasword di firebasae
                            String passFromFirebase = snapshot.child("password").getValue().toString();
//                    validasi password
                            if (passowrd.equals(passFromFirebase)){

//                        simpan username (key) ke local
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key,username_login.getText().toString());
                                editor.apply();


                                Intent intent = new Intent(SignInAct.this,HomeAct.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Password Salah",Toast.LENGTH_LONG).show();
                                btn_login.setEnabled(true);
                                btn_login.setText("SIGN IN");
                            }


                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Username Tidak Ada !",Toast.LENGTH_LONG).show();
                            btn_login.setEnabled(true);
                            btn_login.setText("SIGN IN");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        }
    }
});

    }
    public void regis(View view){
        Intent intent = new Intent(SignInAct.this,RegisterOneAct.class);
        startActivity(intent);
    }
//    public void toHome(View view){
//
//
//
//    }
}