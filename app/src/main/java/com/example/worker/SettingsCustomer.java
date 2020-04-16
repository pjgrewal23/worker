package com.example.worker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SettingsCustomer extends AppCompatActivity {

    private EditText nameText, phoneText, desText;
    private Button backbtn, confirmBtn;
    private ImageView profilePicture;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDb;

    private String uid, name, phone, description, profilepicURL, userType;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_settings);

        nameText = findViewById(R.id.name);
        phoneText = findViewById(R.id.phone);
        desText = findViewById(R.id.description);

        profilePicture = findViewById(R.id.profile);

        backbtn = findViewById(R.id.back);
        confirmBtn = findViewById(R.id.confirmBtn);


        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        getUserInfo();

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsCustomer.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
    }

    private void getUserInfo() {
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0)
                {
                    Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();
                    if(map.get("name") != null){
                        name = map.get("name").toString();
                        nameText.setText(name);
                    }
                    if(map.get("userType") != null){
                        userType = map.get("userType").toString();
                        //nameText.setText(name);
                    }
                    if(map.get("phone") != null){
                        phone = map.get("phone").toString();
                        phoneText.setText(phone);
                    }
                    if(map.get("profilepicURL") != null){
                        profilepicURL = map.get("profilepicURL").toString();
                        Glide.with(getApplication()).load(profilepicURL).into(profilePicture);
                    }

                    if(map.get("description") != null){
                        description = map.get("description").toString();
                        desText.setText(description);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUserInfo() {
        name = nameText.getText().toString();
        phone = phoneText.getText().toString();
        description = desText.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        userInfo.put("description", description);
        userDb.updateChildren(userInfo);

        if(resultUri != null){
            final StorageReference image = FirebaseStorage.getInstance().getReference().child("images").child(uid);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream bimage = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,20,bimage);
            byte[] data = bimage.toByteArray();

            UploadTask uploadTask = image.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsCustomer.this, "Image Upload Failed", Toast.LENGTH_LONG).show();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri url = uri.getResult();
                    //Uri downloadUri = image.getDownloadUrl().getResult();

                    Map userInfo = new HashMap();
                    userInfo.put("profilepicURL", url.toString());
                    userDb.updateChildren(userInfo);

                    Intent intent = new Intent(SettingsCustomer.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        else{
            Intent intent = new Intent(SettingsCustomer.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 ){

            Uri imageUri = data.getData();
            resultUri = imageUri;
            profilePicture.setImageURI(resultUri);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(SettingsCustomer.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}