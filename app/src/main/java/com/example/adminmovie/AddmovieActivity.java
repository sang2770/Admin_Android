package com.example.adminmovie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class AddmovieActivity extends AppCompatActivity {
    TextView selectCategory, selectCountry;
    String[]  categorys={"Kinh dị", "Hành động", "Viễn tưởng"};
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
    }

}