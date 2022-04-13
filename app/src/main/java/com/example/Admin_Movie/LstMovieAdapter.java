package com.example.Admin_Movie;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.Admin_Movie.Model.Movie;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LstMovieAdapter extends BaseAdapter {
    //ngữ cảnh lớp context
    private Activity activity;
    //nguôn dl
    ArrayList<Movie> data;
    //bộ phân tich layout
    private LayoutInflater inflater;

    public LstMovieAdapter(Activity activity, ArrayList<Movie> data) {
        this.activity = activity;
        this.data = data;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        Movie movie = data.get(i);
        return movie.getID();
    }

    private class ViewHolder {
        TextView etName, etDes;
        ImageView imgView;
        ImageButton deletebtn, editBtn;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ViewHolder viewHolder;
        if (v == null) {
            v = inflater.inflate(R.layout.item_movie, null);
            viewHolder = new ViewHolder();
            viewHolder.imgView = v.findViewById(R.id.imgItemMovie);
            viewHolder.etName = v.findViewById(R.id.NameMovie);
            viewHolder.etDes = v.findViewById(R.id.DesMovie);
            viewHolder.deletebtn = v.findViewById(R.id.btndelete);
            viewHolder.editBtn = v.findViewById(R.id.btnEdit);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        new DownloadImageTask(viewHolder.imgView).execute(data.get(i).getThumbnail());
        viewHolder.etName.setText(data.get(i).getName());
        viewHolder.etDes.setText(data.get(i).getDescription());
        Context context = viewHolder.imgView.getContext();
        viewHolder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref = database.getReference();
                AlertDialog Confirm = new AlertDialog.Builder(context).create();
                Confirm.setTitle("Warning");
                Confirm.setMessage("Bạn có chắc chắn muốn xóa không");
                Confirm.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        dialogInterface.dismiss();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference myref = database.getReference();
                        Log.d("delete", String.valueOf(data.get(i).getID()));
                        myref.child("movie/" + data.get(i).getID()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(context, "Bạn đã xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
                Confirm.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        dialogInterface.dismiss();
                    }
                });
                Confirm.show();

            }
        });
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie item = data.get(i);

                Intent intent = new Intent(context, AddmovieActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID", item.getID());
                intent.putExtras(bundle);
                ((Activity) context).startActivityForResult(intent, 300);
            }
        });
        return v;
    }
}
