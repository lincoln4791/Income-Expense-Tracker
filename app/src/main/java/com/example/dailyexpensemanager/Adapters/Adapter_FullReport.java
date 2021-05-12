package com.example.dailyexpensemanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.model.MC_Posts;

import java.util.List;

public class Adapter_FullReport extends RecyclerView.Adapter<Adapter_FullReport.MyViewHolder> {
    private Context context;
    private List<MC_Posts> postsList;

    public Adapter_FullReport(Context context, List<MC_Posts> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_full_report,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_dateTime.setText(postsList.get(position).getPostDateTime());
        holder.tv_category.setText(postsList.get(position).getPostCategory());
        holder.tv_type.setText(postsList.get(position).getPostType());
        holder.tv_amount.setText(postsList.get(position).getPostAmount());
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_dateTime,tv_type,tv_category,tv_amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dateTime = itemView.findViewById(R.id.tv_dateTime_SampleFullReport);
            tv_type = itemView.findViewById(R.id.tv_type_SampleFullReport);
            tv_category = itemView.findViewById(R.id.tv_category_SampleFullReport);
            tv_amount = itemView.findViewById(R.id.tv_amount_SampleFullReport);
        }
    }
}
