package com.example.adminmovie;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;

public class DropdownBox {
    TextView TxtSelect;
    boolean[] checkSelected;
    ArrayList<String> List;
    String[]  listItem;
    Context context;
    String Title;
    public DropdownBox(TextView txtSelect,
                       String[] listItem,
                       Context context,
                       String Title) {
        TxtSelect = txtSelect;
        this.checkSelected =new boolean[4];
        List =new ArrayList<String>();
        this.listItem = listItem;
        this.context = context;
        this.Title=Title;
    }

    public void StartEvent()
    {
        TxtSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Cle", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                //set Title
                builder.setTitle(Title);
                //
                builder.setCancelable(false);

                builder.setMultiChoiceItems(listItem, checkSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        //Check
                        if(b)
                        {
                            List.add(listItem[i]);
                            Collections.sort(List);
                        }else{
                            List.remove(listItem[i]);

                        }
                    };
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder result=new StringBuilder();
                        for (String item: List) {
                            result.append(item+",");

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
}
