package com.daghlas.myrehab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RManagementAdapter extends RecyclerView.Adapter<RManagementAdapter.MyViewHolder> {
    Context context;

    public RManagementAdapter(Context context) {
        this.context = context;
    }



    @NonNull
    @Override
    public RManagementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the row layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.rm_row, parent,false);
        return new RManagementAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RManagementAdapter.MyViewHolder holder, int position) {
        //binding row elements
    }

    @Override
    public int getItemCount() {
        //number of rows to be displayed
        return 15;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rehab_name, director_name, contact;
        ImageView profile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rehab_name = itemView.findViewById(R.id.rehab_name);
            director_name = itemView.findViewById(R.id.rehab_director);
            contact = itemView.findViewById(R.id.contact);
            profile = itemView.findViewById(R.id.profile_pic);
        }
    }
}
