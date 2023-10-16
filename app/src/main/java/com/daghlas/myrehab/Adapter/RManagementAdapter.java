package com.daghlas.myrehab.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daghlas.myrehab.Interface.RManagementInterface;
import com.daghlas.myrehab.R;
import com.daghlas.myrehab.Model.RModel;
import com.daghlas.myrehab.SetRehab;

import java.util.List;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class RManagementAdapter extends RecyclerView.Adapter<RManagementAdapter.MyViewHolder> {

    private final RManagementInterface rManagementInterface;
    Context context;
    private final List<RModel> rModelList;
    public RManagementAdapter(Context context, RManagementInterface rManagementInterface, List<RModel> rModelList) {
        this.context = context;
        this.rManagementInterface = rManagementInterface;
        this.rModelList = rModelList;
    }



    @NonNull
    @Override
    public RManagementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating the row layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.rm_row, parent,false);
        return new RManagementAdapter.MyViewHolder(view, rManagementInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RManagementAdapter.MyViewHolder holder, int position) {
        //binding row elements
        RModel rModel = rModelList.get(position);

        holder.rehab_name.setText(rModel.getRehabName());
        holder.director_name.setText(rModel.getRehabDirector());
        holder.contact.setText(rModel.getRehabPhone());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SetRehab.class);
            intent.putExtra("rehabName", rModelList.get(holder.getAdapterPosition()).getRehabName());
            intent.putExtra("rehabDirector", rModelList.get(holder.getAdapterPosition()).getRehabDirector());
            intent.putExtra("rehabLocation", rModelList.get(holder.getAdapterPosition()).getRehabLocation());
            intent.putExtra("date", rModelList.get(holder.getAdapterPosition()).getDateAdded());
            intent.putExtra("rehabEmail", rModelList.get(holder.getAdapterPosition()).getRehabEmail());
            intent.putExtra("rehabPhone", rModelList.get(holder.getAdapterPosition()).getRehabPhone());
            context.startActivity(intent);
            ((Activity)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        //number of rows to be displayed
        return rModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rehab_name, director_name, contact;
        ImageView profile;
        public MyViewHolder(@NonNull View itemView, RManagementInterface rManagementInterface) {
            super(itemView);
            rehab_name = itemView.findViewById(R.id.rehab_name);
            director_name = itemView.findViewById(R.id.rehab_director);
            contact = itemView.findViewById(R.id.contact_detail);
            profile = itemView.findViewById(R.id.profile_pic);

            itemView.setOnClickListener(v -> {
                if(rManagementInterface != null){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        rManagementInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
