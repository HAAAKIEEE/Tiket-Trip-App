package ic.ac.polihasnur.ti.haqi.tikettrip;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisTwoAct extends AppCompatActivity {
    ImageView pic_photo_register_user;
    Uri photo_location;
    Integer photo_max=1;
    View btn_add_photo;
    LinearLayout btn_back_toregisone;
    EditText bio,nama_lengkap;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    Button btn_continue;
    DatabaseReference reference;
    StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getUsernameLocal();
        setContentView(R.layout.activity_regis_two);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_continue=findViewById(R.id.btn_countinue);
        btn_back_toregisone=findViewById(R.id.btn_back_toregisone);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        pic_photo_register_user = findViewById(R.id.pic_photo_register_user);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading . . .");
                reference = FirebaseDatabase.getInstance().getReference().child("User").child(username_key_new);
                storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);
//                validasi file
                if (photo_location!= null){

//                    Tidak bisa karna uri ny bukan dari aces token

//                    StorageReference storageReference1 = storage.child(System.currentTimeMillis()+"."+getFileExtension(photo_location));
//                    storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            String uri_photo = taskSnapshot.getStorage().getDownloadUrl().toString();
//                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
//                            reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
//                            reference.getRef().child("bio").setValue(bio.getText().toString());
//                        }
//                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                            Intent gotosuccess = new Intent(RegisTwoAct.this,SuccessRegisterAct.class);
//                            startActivity(gotosuccess);
//                        }
//                    });


                    // Pada bagian yang melakukan unggah foto dan menyimpan URI ke database
                    StorageReference storageReference1 = storage.child(System.currentTimeMillis()+"."+getFileExtension(photo_location));
                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Mengambil URI unduhan setelah berhasil
                                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();

                                            // Menyimpan URI foto ke database Firebase
                                            reference.child("url_photo_profile").setValue(uri_photo);
                                            reference.child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                            reference.child("bio").setValue(bio.getText().toString());

                                            // Pindah ke halaman sukses
                                            Intent gotosuccess = new Intent(RegisTwoAct.this, SuccessRegisterAct.class);
                                            startActivity(gotosuccess);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                    Toast.makeText(RegisTwoAct.this, "Gagal mengunggah foto", Toast.LENGTH_SHORT).show();
                                }
                            });

                }


            }
        });
        btn_back_toregisone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisTwoAct.this,RegisterOneAct.class);
                startActivity(intent);
            }
        });
        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
    }
    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic,photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){
            photo_location = data.getData();
            Picasso.with(RegisTwoAct.this).load(photo_location)
                    .centerCrop().fit().into(pic_photo_register_user);
//            Picasso.with(HomeAct.this).load(snapshot.child("url_photo_profile")
//                    .getValue().toString()).centerCrop().fit().into(profil_pic);
        }

    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}