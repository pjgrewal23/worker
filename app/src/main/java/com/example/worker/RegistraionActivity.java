package com.example.worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistraionActivity extends AppCompatActivity {

    private Button regButton;
    private EditText emailText, passwordText, nameText;
    private RadioGroup radioType;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraion);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(RegistraionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        nameText = findViewById(R.id.name);
        regButton = findViewById(R.id.registerButton);
        emailText =  findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        radioType = findViewById(R.id.radioType);


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int selectType = radioType.getCheckedRadioButtonId();
                final RadioButton rButton = findViewById(selectType);
                if(rButton.getText() == null){
                    return;
                }
                final String name = nameText.getText().toString();
                final String email = emailText.getText().toString();
                final String password = passwordText.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                        (RegistraionActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegistraionActivity.this, "Sign Up Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child ("Users").child(userId);
                            Map userInfo = new HashMap();
                            userInfo.put("name", name);
                            userInfo.put("userType", rButton.getText().toString());
                            userInfo.put("profilepicURL", "https://randomuser.me/api/portraits/med/lego/1.jpg");
                            currentUserDb.setValue(userInfo);
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(RegistraionActivity.this, LoginRegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}
