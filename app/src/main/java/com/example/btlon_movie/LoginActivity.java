package com.example.btlon_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btlon_movie.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText TxtEmail, TxtPassword;
    Button btnLogin;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        initEvent();

    }

    private void initUi() {
        TxtEmail = findViewById(R.id.Email);
        TxtPassword = findViewById(R.id.Password);
        btnLogin = findViewById(R.id.BtnLogin);
        progressDialog = new ProgressDialog(this);

    }

    private void initEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login() {
        progressDialog.show();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email, password;
        email = TxtEmail.getText().toString().trim();
        password = TxtPassword.getText().toString().trim();
        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("user").child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressDialog.dismiss();
                                    User user = dataSnapshot.getValue(User.class);
                                    if(user.getType().toString().contains("Admin"))
                                    {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();


                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                    // Getting Post failed, log a message
                                    Toast.makeText(LoginActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}