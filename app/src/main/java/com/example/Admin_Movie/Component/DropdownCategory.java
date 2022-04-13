package com.example.Admin_Movie.Component;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.Admin_Movie.Model.Category;

import java.util.ArrayList;

public class DropdownCategory {
    //  View hiện dữ liệu
    TextView TxtSelect;
    // Mảng đánh dấu checked
    boolean[] checkSelected;
    // Kết quả chọn
    ArrayList<Category> ResultSelect;
    // Danh sách lựa chọn
    ArrayList<Category> listItem;
    // Ngữ cảnh
    Context context;
    // Tiêu đề
    String Title;
    public DropdownCategory(TextView txtSelect,
                            ArrayList<Category> listItem,
                            Context context,
                            String Title,ArrayList<Category> listSelected) {
        TxtSelect = txtSelect;
        ResultSelect = new ArrayList<Category>();
        this.listItem = listItem;
        this.checkSelected = new boolean[listItem.size()];
        //check sửa dữ liệu
        String selectCategory="";
        if(listSelected!=null)
        {
            for (Category item: listSelected) {
                int index=checkInArrayList(item.getID());
                if(index!=-1)
                {
                    checkSelected[index]=true;
                    ResultSelect.add(item);
                    selectCategory+=item.getName()+", ";
                }
            }
        }
        // Nếu chỉnh sửa
        TxtSelect.setText(selectCategory);
        // Tham chiếu ngữ cảnh
        this.context = context;
        this.Title = Title;
    }
    // Kiểm tra sự tồn tại của item
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
    // Lấy kết quả
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
                // Không thể đóng bằng phím back
                builder.setCancelable(false);
                // Cho phép lựa chọn nhiều
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
    // Reset lại các lựa chọn
    public void Reset()
    {
        ResultSelect.clear();
        checkSelected=new boolean[listItem.size()];
        TxtSelect.setText("");

    }
}
