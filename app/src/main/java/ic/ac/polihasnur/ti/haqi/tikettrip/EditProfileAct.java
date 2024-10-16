package ic.ac.polihasnur.ti.haqi.tikettrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
public class EditProfileAct extends AppCompatActivity {
    ImageView pic_photo_profile_edit;
    Button btn_simpan_edit;
    EditText bio_edit, nama_lengkap_edit;
    Uri photo_location;
    Integer photo_max = 1;
    View btn_edit_photo;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    DatabaseReference reference, reference2;
    StorageReference storage;
    String url_photo_old; // Menyimpan URL foto lama

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inisialisasi komponen UI
        pic_photo_profile_edit = findViewById(R.id.pic_photo_profile_edit);
        btn_simpan_edit = findViewById(R.id.btn_simpan_edit);
        nama_lengkap_edit = findViewById(R.id.nama_lengkap_edit);
        bio_edit = findViewById(R.id.bio_edit);
        btn_edit_photo = findViewById(R.id.btn_edit_photo);

        // Mendapatkan username lokal dari SharedPreferences
        getUsernameLocal();

        // Mendapatkan data pengguna dari Firebase dan menampilkan di UI
        reference2 = FirebaseDatabase.getInstance().getReference().child("User").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nama_lengkap_edit.setText(snapshot.child("nama_lengkap").getValue().toString());
                bio_edit.setText(snapshot.child("bio").getValue().toString());
                url_photo_old = snapshot.child("url_photo_profile").getValue().toString();
                Picasso.with(EditProfileAct.this).load(url_photo_old)
                        .centerCrop().fit().into(pic_photo_profile_edit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Tombol untuk memilih foto
        btn_edit_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });

        // Tombol untuk menyimpan perubahan profil
        btn_simpan_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("User").child(username_key_new);
                storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

                // Validasi file foto terpilih
                if (photo_location != null) {
                    // Upload foto baru ke Firebase Storage
                    StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Mendapatkan URL download foto setelah berhasil diunggah
                                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();

                                            // Menyimpan URL foto baru ke Realtime Database
                                            reference.child("url_photo_profile").setValue(uri_photo);
                                            reference.child("nama_lengkap").setValue(nama_lengkap_edit.getText().toString());
                                            reference.child("bio").setValue(bio_edit.getText().toString());

                                            // Pindah ke halaman sukses
                                            Intent gotosuccess = new Intent(EditProfileAct.this, HomeAct.class);
                                            startActivity(gotosuccess);
                                            finish(); // Menutup activity saat ini
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                    Toast.makeText(EditProfileAct.this, "Gagal mengunggah foto", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    // Jika tidak ada foto baru yang dipilih, gunakan URL foto lama
                    reference.child("url_photo_profile").setValue(url_photo_old);
                    reference.child("nama_lengkap").setValue(nama_lengkap_edit.getText().toString());
                    reference.child("bio").setValue(bio_edit.getText().toString());

                    // Pindah ke halaman sukses
                    Intent gotosuccess = new Intent(EditProfileAct.this, HomeAct.class);
                    startActivity(gotosuccess);
                    finish(); // Menutup activity saat ini
                }
            }
        });
    }

    // Mendapatkan ekstensi file dari URI
    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Memilih foto dari galeri
    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    // Menangani hasil dari pemilihan foto dari galeri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(EditProfileAct.this).load(photo_location)
                    .centerCrop().fit().into(pic_photo_profile_edit);
        }
    }

    // Mendapatkan username lokal dari SharedPreferences
    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}

