package com.example.btlon_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ImageView BtnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initEvent();
    }

    private void initEvent() {
        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void initUI() {
        BtnLogout=findViewById(R.id.BtnLogout);
        //Menu bottom
        BottomNavigationView menu=findViewById(R.id.Navigation);
        menu.setSelectedItemId(R.id.MyHome);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Create:
                        startActivity(new Intent(MainActivity.this, AddmovieActivity.class));
                        overridePendingTransition(0,0);
                        finishAffinity();
                        return  true;
                    case R.id.MyHome:
                        return true;
                }
                return false;
            }
        });
    }
}