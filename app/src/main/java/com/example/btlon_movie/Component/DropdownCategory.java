package com.example.btlon_movie.Component;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.btlon_movie.Model.Category;

import java.util.ArrayList;
import java.util.Collections;

public class DropdownCategory {
    TextView TxtSelect;
    boolean[] checkSelected;
    ArrayList<Category> ResultSelect;
    ArrayList<Category> listItem;
    Context context;
    String Title;

    public DropdownCategory(TextView txtSelect,
                            ArrayList<Category> listItem,
                            Context context,
                            String Title) {
        TxtSelect = txtSelect;
        ResultSelect = new ArrayList<Category>();
        this.listItem = listItem;
        this.checkSelected = new boolean[listItem.size()];
        this.context = context;
        this.Title = Title;
    }

    public ArrayList<Category> getResultSelect() {
        return ResultSelect;
    }

    public void setResultSelect(ArrayList<Category> resultSelect) {
        ResultSelect = resultSelect;
    }

    public void StartEvent() {
        TxtSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //set Title
                builder.setTitle(Title);
                //Get List Value
                String[] Displayvalue = new String[listItem.size()];
                for (int i = 0; i < listItem.size(); i++) {
                    Displayvalue[i] = listItem.get(i).getName();
                }
                builder.setCancelable(false);

                builder.setMultiChoiceItems(Displayvalue, checkSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        //Check
                        if (b) {
                            ResultSelect.add(listItem.get(i));
                        } else {
                            ResultSelect.remove(listItem.get(i));
                        }
                    }

                    ;
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder result = new StringBuilder();
                        for (Category item : ResultSelect) {
                            result.append(item.getName() + ",");
                        }
                        TxtSelect.setText(result.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
    public void Reset()
    {
        ResultSelect.clear();
        checkSelected=new boolean[listItem.size()];
        TxtSelect.setText("");

    }
}
