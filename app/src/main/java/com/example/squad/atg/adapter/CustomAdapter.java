package com.example.squad.atg.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.squad.atg.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {


    private List<PhotoApi> photoList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;

        MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image1);
        }
    }


    public CustomAdapter(Context context, List<PhotoApi> photoList) {
        this.photoList = photoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(photoList.get(position).getUrl()).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
