package com.daghlas.myrehab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KManagementAdapter extends RecyclerView.Adapter<KManagementAdapter.MyViewHolder> {

    private final KManagementInterface kManagementInterface;
    Context context;
    public KManagementAdapter(Context context, KManagementInterface kManagementInterface){
        this.context = context;
        this.kManagementInterface = kManagementInterface;
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
    }

    @Override
    public int getItemCount() {
        //returns number of rows in recyclerview
        return 15;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, rehab, date;
        ImageView profile;

        public MyViewHolder(@NonNull View itemView, KManagementInterface kManagementInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.full_name);
            rehab = itemView.findViewById(R.id.allocated_rehab);
            date = itemView.findViewById(R.id.date_enrolled);
            profile = itemView.findViewById(R.id.profile_pic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(kManagementInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            kManagementInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
