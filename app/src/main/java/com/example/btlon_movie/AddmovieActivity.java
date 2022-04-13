package com.example.btlon_movie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlon_movie.Component.DropdownCategory;
import com.example.btlon_movie.Component.DropdownCountry;
import com.example.btlon_movie.Model.Category;
import com.example.btlon_movie.Model.Country;
import com.example.btlon_movie.Model.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddmovieActivity extends AppCompatActivity {
    // Component
    TextView selectCategory, selectCountry;
    TextInputEditText Name, Description, language, Link, Year;
    ImageView imageThumbnail, imageBackground;
    CheckBox rating;
    BottomNavigationView menu;
    private DatabaseReference mDatabase;
    ArrayList<Category> ListCategory;
    ArrayList<Country> ListCountry;
    Button BtnAdd;
    // Link ảnh
    Uri thumbnailUri, backgroundUri;
    String LinkThumbnail, LinkBackground;

    // Lay Id tiep theo
    int SizeID;
    DropdownCategory Category;
    DropdownCountry Country;
    ProgressDialog progressDialog;

    ArrayList<Category> categoryEdit;
    ArrayList<Country> countryEdit;

    // check update image
    boolean checkBackground, checkThumbnail;
    private boolean checkUpdate;
    private int Id;

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
        // Lấy dữ liệu category gán cho listbox
        mDatabase.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Category category = postSnapshot.getValue(Category.class);
                    ListCategory.add(category);
                }
                Category = new DropdownCategory(selectCategory, ListCategory, AddmovieActivity.this, "Chọn thể loại", categoryEdit);
                Category.StartEvent();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddmovieActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
            }
        });
        //get List Country
        ListCountry = new ArrayList<Country>();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("country").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Country country = postSnapshot.getValue(Country.class);
                    ListCountry.add(country);
                }
                Country = new DropdownCountry(selectCountry, ListCountry, AddmovieActivity.this, "Chọn quốc qia", countryEdit);
                Country.StartEvent();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddmovieActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
            }
        });
        // chon anh chính
        imageThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery=new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, 100);
            }
        });
        // chon ảnh nền
        imageBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery=new Intent();
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, 200);
            }
        });
        //Them phim
        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Validated())
                {
                    Toast.makeText(AddmovieActivity.this, "Bạn nhập thiếu dữ liệu!", Toast.LENGTH_SHORT).show();
                    return;
                }
                upload();
                progressDialog.show();
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
    private  interface FirebaseCallBack{
        void onCallBack();
    }
    // check nhấn vào Ảnh thumbnail
    private void uploadThumbnail()
    {
        if(checkThumbnail)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            String randomKeyThumbnail= UUID.randomUUID().toString();
            StorageReference riversRef = storageRef.child("images/"+randomKeyThumbnail);
            riversRef.putFile(thumbnailUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    LinkThumbnail=uri.toString();
                                    Log.d("ThumbnailUpload",LinkThumbnail);
                                    if(checkBackground)
                                    {
                                        Log.d("checkBackground","Check");
                                        uploadBackground();
                                    }else{
                                        uploadMovie();
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddmovieActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Log.d("checkBackground","Check_False");
            uploadBackground();
        }
    }
    // check nhấn vào Ảnh background

    private void uploadBackground()
    {
        Log.d("checkBackground","Begin");

        if(checkBackground)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            String randomKeyBackground= UUID.randomUUID().toString();
            StorageReference riversRef1 = storageRef.child("images/"+randomKeyBackground);
            riversRef1.putFile(backgroundUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    LinkBackground=uri.toString();
                                    Log.d("BackgroundlUpload",LinkBackground);
                                    uploadMovie();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddmovieActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            uploadMovie();
        }
    }
    // Thêm phim
    private void uploadMovie()
    {
        mDatabase.child("movie").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(AddmovieActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("MovideUpload","movie");

                    SizeID = (int) task.getResult().getChildrenCount();
                    Movie movie = new Movie(
                            checkUpdate?Id:SizeID+1,
                            Name.getText().toString().trim(),
                            LinkBackground,
                            Description.getText().toString().trim(),
                            LinkThumbnail,
                            language.getText().toString().trim(),
                            Category.getResultSelect(),
                            Country.getResultSelect(),
                            rating.isChecked() ? "Like" : "",
                            Link.getText().toString().trim(),
                            Integer.valueOf(Year.getText().toString().trim())
                    );
                    mDatabase.child("movie").child(checkUpdate?String.valueOf(String.valueOf(Id)):String.valueOf(SizeID+1)).setValue(movie);
                    progressDialog.dismiss();
                    ResetText();
                    Toast.makeText(AddmovieActivity.this, checkUpdate?"Cập nhật thành công":"Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void upload() {
        progressDialog.show();
        uploadThumbnail();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // thumbnails
        if(requestCode==100 && resultCode==RESULT_OK
                && data !=null && data.getData() != null)
        {
            thumbnailUri=data.getData();
            imageThumbnail.setImageURI(thumbnailUri);
            checkThumbnail=true;
        }
        if(requestCode==200 && resultCode==RESULT_OK
                && data !=null && data.getData() != null)
        {
            checkBackground=true;
            backgroundUri=data.getData();
            imageBackground.setImageURI(backgroundUri);

        }
    }
    private void InitUI() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        selectCategory = findViewById(R.id.SelectCategory);
        selectCountry = findViewById(R.id.SelectCountry);
        Name = findViewById(R.id.Name);
        imageThumbnail = findViewById(R.id.imageThumbnail);
        Description = findViewById(R.id.Description);
        imageBackground = findViewById(R.id.imageBackground);
        language = findViewById(R.id.Language);
        rating = findViewById(R.id.Rating);
        Link = findViewById(R.id.Link);
        Year = findViewById(R.id.InputYear);
        BtnAdd = findViewById(R.id.BtnAdd);
        progressDialog = new ProgressDialog(this);
        //Menu bottom
        menu = findViewById(R.id.Navigation);
        menu.setSelectedItemId(R.id.Create);
        // Check update
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        if(bundle!=null)
        {
            checkUpdate=true;
            Menu menuBottomItem = menu.getMenu();
            menuBottomItem.findItem(R.id.Create).setTitle("Edit");
            menuBottomItem.findItem(R.id.Create).setIcon(R.drawable.ic_baseline_edit_24);
            Id=bundle.getInt("ID");
            Log.d("Id", String.valueOf(Id));
            progressDialog.show();
            mDatabase.child("movie").child(String.valueOf(Id)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        Movie movie=task.getResult().getValue(Movie.class);
                        if(movie!=null)
                        {
                            Name.setText(movie.getName());
                            Description.setText(movie.getDescription());
                            language.setText(movie.getLanguage());
                            if (movie.getRating().contains("Like")) {
                                rating.setChecked(true);
                            } else {
                                rating.setChecked(false);
                            }
                            Link.setText(movie.getLink());
                            Year.setText(String.valueOf(movie.getYear()));
                            categoryEdit= (ArrayList<com.example.btlon_movie.Model.Category>) movie.getCategory();
                            countryEdit= (ArrayList<com.example.btlon_movie.Model.Country>) movie.getCountry();
                            new DownloadImageTask(imageThumbnail).execute(movie.getThumbnail());
                            new DownloadImageTask(imageBackground).execute(movie.getImage());
                            thumbnailUri=Uri.parse(movie.getThumbnail());
                            backgroundUri=Uri.parse(movie.getImage());
                            LinkBackground=movie.getImage();
                            LinkThumbnail=movie.getThumbnail();
                        }else{
                            Toast.makeText(AddmovieActivity.this, "Lấy dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                        }

                        BtnAdd.setText("Cập nhật");
                    }
                }
            });
        }




    }

    private void ResetText() {
        Name.setText("");
        imageBackground.setImageResource(R.drawable.ic_baseline_camera_alt_24);
        Description.setText("");
        imageThumbnail.setImageResource(R.drawable.ic_baseline_camera_alt_24);
        language.setText("");
        Category.Reset();
        Country.Reset();
        rating.setChecked(false);
        Link.setText("");
        Year.setText("");
    }

    private boolean Validated() {
        if (Name.getText().toString().trim().length() == 0 ||
                thumbnailUri==null   ||
                Description.getText().toString().trim().length() == 0 ||
                backgroundUri==null ||
                language.getText().toString().trim().length() == 0 ||
                Link.getText().toString().trim().length() == 0 ||
                Category.getResultSelect().size() == 0 || Country.getResultSelect().size() == 0
        ) {
            return false;
        }
        try {
            int year = Integer.parseInt(Year.getText().toString().trim());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}