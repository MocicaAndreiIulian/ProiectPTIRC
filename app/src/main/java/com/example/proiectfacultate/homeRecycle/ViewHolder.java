package com.example.proiectfacultate.homeRecycle;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proiectfacultate.R;

public class ViewHolder extends RecyclerView.ViewHolder{

    public TextView textView;

    public ViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.passwordItem);
    }

}
