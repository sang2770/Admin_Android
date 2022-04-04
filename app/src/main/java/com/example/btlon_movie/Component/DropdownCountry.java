package com.example.btlon_movie.Component;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.btlon_movie.Model.Category;
import com.example.btlon_movie.Model.Country;

import java.util.ArrayList;
import java.util.Collections;

public class DropdownCountry {
    TextView TxtSelect;
    boolean[] checkSelected;
    ArrayList<Country> ResultSelect;
    ArrayList<Country>  listItem;
    Context context;
    String Title;
    public DropdownCountry(TextView txtSelect,
                           ArrayList<Country> listItem,
                           Context context,
                           String Title, ArrayList<Country> listSelected) {
        TxtSelect = txtSelect;
        this.checkSelected =new boolean[listItem.size()];
        ResultSelect =new ArrayList<Country>();
        this.listItem = listItem;
        this.context = context;
        this.checkSelected = new boolean[listItem.size()];
        String selectCountry="";
        if(listSelected!=null)
        {
            for (Country item: listSelected) {
                int index=checkInArrayList(item.getID());
                if(index!=-1)
                {
                    checkSelected[index]=true;
                    selectCountry+=item.getName()+", ";
                }
            }
        }
        TxtSelect.setText(selectCountry);
        this.Title=Title;
    }
    public int checkInArrayList(int Id )
    {
        for (int i=0;i<listItem.size();i++) {
            if(listItem.get(i).getID()==Id)
            {
                return i ;
            }
        }
        return -1;
    }
    public ArrayList<Country> getResultSelect() {
        return ResultSelect;
    }

    public void StartEvent()
    {
        TxtSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                //set Title
                builder.setTitle(Title);
                //Get List Value
                String[] Displayvalue=new String[listItem.size()];
                for (int i=0;i<listItem.size();i++) {
                    Displayvalue[i]=listItem.get(i).getName();
                }
                builder.setCancelable(false);
                builder.setMultiChoiceItems(Displayvalue, checkSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        //Check
                        if(b)
                        {
                            ResultSelect.add(listItem.get(i));
                        }else{
                            ResultSelect.remove(listItem.get(i));
                        }
                    };
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder result=new StringBuilder();
                        for (Country item: ResultSelect) {
                            result.append(item.getName()+",");
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
