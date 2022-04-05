package com.example.btlon_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.btlon_movie.Model.Category;
import com.example.btlon_movie.Model.Country;
import com.example.btlon_movie.Model.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView BtnLogout;
    ArrayList<Movie> arrLstMovie;
    LstMovieAdapter adapterMovie;
    ListView DSMovie;
    FirebaseDatabase database;
    DatabaseReference myref;
    TabLayout Categorytab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initEvent();

        //tạo list movie
        arrLstMovie=new ArrayList<>();
        DSMovie=findViewById(R.id.lstMovie);
        getListdata("","movie");

        //tạo tab lọc category
        Categorytab=findViewById(R.id.tabLayout);
        Categorytab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        arrLstMovie.clear();
                        getListdata("","movie");
                        break;
                    case 1:
                        arrLstMovie.clear();
                        getListdata("Action","movie");

                        break;
                    case 2:

                        arrLstMovie.clear();
                        getListdata("Drama","movie");
                        break;
                    case 3:
                        arrLstMovie.clear();
                        getListdata("Horror","movie");
                        break;
                    case 4:
                        arrLstMovie.clear();
                        getListdata("Cartoon","movie");
                        break;
                    case 5:
                        arrLstMovie.clear();
                        getListdata("Adventure","movie");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    //lấy dữ liệu từ firebase
    private void getListdata(String keys,String child){
        database=FirebaseDatabase.getInstance();
        myref=database.getReference();
        myref.child(child).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("length",snapshot.getChildrenCount()+"" );
                for(DataSnapshot sanp:snapshot.getChildren()){
                    if(child=="movie"){
                        Movie movie=sanp.getValue(Movie.class);
                        if(keys==""){
                            arrLstMovie.add(movie);
                        }
                        else{
                            for(int i=0;i<sanp.child("category").getChildrenCount();i++){
                                String theloai=sanp.child("category/"+i+"/name").getValue().toString();
                                if(theloai.equals(keys))
                                {
                                    arrLstMovie.add(movie);
                                }
                            }
                        }
                    }

                }

              adapterMovie=new LstMovieAdapter(MainActivity.this,arrLstMovie);
                DSMovie.setAdapter(adapterMovie);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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