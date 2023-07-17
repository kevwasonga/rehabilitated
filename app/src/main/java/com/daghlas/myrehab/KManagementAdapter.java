package com.daghlas.myrehab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * DEVELOPER: daghlaskaire58@gmail.com
 * For KELVIN WASONGA
 * JULY 2023
 */

public class KManagementAdapter extends RecyclerView.Adapter<KManagementAdapter.MyViewHolder> implements Filterable {

    private final KManagementInterface kManagementInterface;
    private final List<KModel> kModelList;
    //search
    private final List<KModel> kModelListFull;
    Context context;
    public KManagementAdapter(Context context, KManagementInterface kManagementInterface, List<KModel> kModelList){
        this.context = context;
        this.kManagementInterface = kManagementInterface;
        this.kModelList = kModelList;
        //search
        kModelListFull = new ArrayList<>(kModelList);
    }

    @NonNull
    @Override
    public KManagementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflating the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.km_row, parent, false);
        return new KManagementAdapter.MyViewHolder(view, kManagementInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull KManagementAdapter.MyViewHolder holder, int position) {
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

    //search
    @Override
    public Filter getFilter() {
        return kModelFilter;
    }
    private final Filter kModelFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<KModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(kModelListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (KModel item : kModelListFull){
                    if(item.getFullName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            kModelList.clear();
            kModelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    //search end

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, rehab, date;
        ImageView profile;

        public MyViewHolder(@NonNull View itemView, KManagementInterface kManagementInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.full_name);
            rehab = itemView.findViewById(R.id.allocated_rehab);
            date = itemView.findViewById(R.id.date_enrolled);
            profile = itemView.findViewById(R.id.profile_pic);

            itemView.setOnClickListener(v -> {
                if(kManagementInterface != null){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        kManagementInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
