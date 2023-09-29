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

import com.daghlas.myrehab.Interface.RInterface;
import com.daghlas.myrehab.Model.KModel;
import com.daghlas.myrehab.R;
import com.daghlas.myrehab.SetKid;

import java.util.List;

public class RAdapter extends RecyclerView.Adapter<RAdapter.MyViewHolder>{

    private final RInterface rInterface;
    private final List<KModel> kModelList;

    Context context;
    public RAdapter(Context context, RInterface rInterface, List<KModel> kModelList){
        this.context = context;
        this.rInterface = rInterface;
        this.kModelList = kModelList;
    }

    @NonNull
    @Override
    public RAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.km_row, parent, false);
        return new RAdapter.MyViewHolder(view, rInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RAdapter.MyViewHolder holder, int position) {
        //binding recyclerview rows according to position
        KModel kModel = kModelList.get(position);

        holder.name.setText(kModel.getFullName());
        holder.rehab.setText(kModel.getRehab());
        holder.date.setText(kModel.getDateEnrolled());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SetKid.class);
            intent.putExtra("fullName", kModelList.get(holder.getAdapterPosition()).getFullName());
            intent.putExtra("rehab", kModelList.get(holder.getAdapterPosition()).getRehab());
            intent.putExtra("date", kModelList.get(holder.getAdapterPosition()).getDateEnrolled());
            intent.putExtra("residence", kModelList.get(holder.getAdapterPosition()).getResidence());
            intent.putExtra("guardian", kModelList.get(holder.getAdapterPosition()).getGuardian());
            intent.putExtra("gender", kModelList.get(holder.getAdapterPosition()).getGender());
            intent.putExtra("health", kModelList.get(holder.getAdapterPosition()).getHealth());
            intent.putExtra("fingerprint", kModelList.get(holder.getAdapterPosition()).getFingerprint());
            intent.putExtra("faceID", kModelList.get(holder.getAdapterPosition()).getFaceID());
            context.startActivity(intent);
            ((Activity)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        //returns number of rows in recyclerview
        return kModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, rehab, date;
        ImageView profile;

        public MyViewHolder(@NonNull View itemView, RInterface rInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.full_name);
            rehab = itemView.findViewById(R.id.allocated_rehab);
            date = itemView.findViewById(R.id.date_enrolled);
            profile = itemView.findViewById(R.id.profile_pic);

            itemView.setOnClickListener(v -> {
                if(rInterface != null){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        rInterface.onItemClick(pos);
                    }
                }
            });
        }
    }

}
