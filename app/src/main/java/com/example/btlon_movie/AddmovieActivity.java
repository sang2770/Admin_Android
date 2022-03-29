package com.example.btlon_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlon_movie.Component.DropdownCategory;
import com.example.btlon_movie.Component.DropdownCountry;
import com.example.btlon_movie.Model.Category;
import com.example.btlon_movie.Model.Country;
import com.example.btlon_movie.Model.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddmovieActivity extends AppCompatActivity {
    TextView selectCategory, selectCountry;
    TextInputEditText Name, Image, Description, Thumbnail, language, Link, Year;
    CheckBox rating;
    BottomNavigationView menu;
    private DatabaseReference mDatabase;
    ArrayList<Category> ListCategory;
    ArrayList<Country> ListCountry;
    Button BtnAdd;
    int SizeID;
    DropdownCategory Category;
    DropdownCountry Country;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmovie);
        InitUI();
        InitEvent();
    }


    private void InitEvent() {
        //get List Category
        ListCategory = new ArrayList<Category>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Category category = postSnapshot.getValue(Category.class);
                    ListCategory.add(category);
                }
                Category = new DropdownCategory(selectCategory, ListCategory, AddmovieActivity.this, "Chọn thể loại");
                Category.StartEvent();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddmovieActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
            }
        });
        //get List Country
        ListCountry = new ArrayList<Country>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Country").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Country country = postSnapshot.getValue(Country.class);
                    ListCountry.add(country);
                }
                Country = new DropdownCountry(selectCountry, ListCountry, AddmovieActivity.this, "Chọn quốc qia");
                Country.StartEvent();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddmovieActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
            }
        });
        //Them phim
        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mDatabase.child("Movie").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AddmovieActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            SizeID = (int) task.getResult().getChildrenCount();
                            Movie movie = new Movie(
                                    SizeID + 1,
                                    Name.getText().toString().trim(),
                                    Image.getText().toString().trim(),
                                    Description.getText().toString().trim(),
                                    Thumbnail.getText().toString().trim(),
                                    language.getText().toString().trim(),
                                    Category.getResultSelect(),
                                    Country.getResultSelect(),
                                    rating.isChecked() ? "Like" : "",
                                    Link.getText().toString().trim(),
                                    Integer.valueOf(Year.getText().toString().trim())
                            );
                            mDatabase.child("Movie").child(String.valueOf(SizeID + 1)).setValue(movie);
                            progressDialog.dismiss();
                            ResetText();
                            Toast.makeText(AddmovieActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        //Menu bottom
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.MyHome:
                        startActivity(new Intent(AddmovieActivity.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        return true;
                    case R.id.Create:
                        return true;
                }
                return false;
            }
        });
    }

    private void InitUI() {
        selectCategory = findViewById(R.id.SelectCategory);
        selectCountry = findViewById(R.id.SelectCountry);
        Name = findViewById(R.id.Name);
        Image = findViewById(R.id.Image);
        Description = findViewById(R.id.Description);
        Thumbnail = findViewById(R.id.Thumbnail);
        language = findViewById(R.id.Language);
        rating = findViewById(R.id.Rating);
        Link = findViewById(R.id.Link);
        Year = findViewById(R.id.InputYear);
        BtnAdd = findViewById(R.id.BtnAdd);
        //Menu bottom
        menu = findViewById(R.id.Navigation);
        menu.setSelectedItemId(R.id.Create);

        progressDialog = new ProgressDialog(this);

    }

    private void ResetText() {
        Name.getText().toString();
        Image.setText("");
        Description.setText("");
        Thumbnail.setText("");
        language.setText("");
        Category.getResultSelect();
        Country.getResultSelect();
        rating.setChecked(false);
        Link.setText("");
        Year.setText("");
    }

}