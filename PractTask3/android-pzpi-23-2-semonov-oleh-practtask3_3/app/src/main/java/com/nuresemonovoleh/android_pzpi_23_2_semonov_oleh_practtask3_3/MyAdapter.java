package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask3_3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mData;
    private int[] mImageData;

    public MyAdapter(String[] data, int[] imageData){
        mData = data;
        mImageData = imageData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textView);
            imageView = v.findViewById(R.id.imageView);
        }
    }
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mData[position]);
        holder.imageView.setImageResource(mImageData[position]);
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),"Clicked: "
                    + mData[position],Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }
}