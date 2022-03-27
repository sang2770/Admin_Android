package com.example.btlon_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlon_movie.Model.Category;
import com.example.btlon_movie.Model.Country;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddmovieActivity extends AppCompatActivity {
    TextView selectCategory, selectCountry;
    String[]  categorys={"Kinh dị", "Hành động", "Viễn tưởng"};
    private DatabaseReference mDatabase;
    ArrayList<Category> ListCategory;
    ArrayList<Country> ListCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmovie);
        InitUI();
        InitEvent();
    }

    private void InitEvent() {
        // Select category
        DropdownBox Category=new DropdownBox(selectCategory,categorys, this, "Chọn thể loại" );
        Category.StartEvent();
        DropdownBox Country=new DropdownBox(selectCountry,categorys, this, "Chọn quốc qia" );
        Country.StartEvent();
    }

    private void InitUI() {
        selectCategory=findViewById(R.id.SelectCategory);
        selectCountry=findViewById(R.id.SelectCountry);
        //get List Category
        ListCategory=new ArrayList<Category>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Category category=postSnapshot.getValue(Category.class);
                    ListCategory.add(category);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddmovieActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
            }
        });
        //Menu bottom
        BottomNavigationView menu=findViewById(R.id.Navigation);
        menu.setSelectedItemId(R.id.MyHome);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.MyHome:
                        startActivity(new Intent(AddmovieActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        finishAffinity();
                        return  true;
                    case R.id.Create:
                        return true;
                }
                return false;
            }
        });
    }

}