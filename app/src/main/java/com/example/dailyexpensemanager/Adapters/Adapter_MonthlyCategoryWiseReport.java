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

public class Adapter_MonthlyCategoryWiseReport extends RecyclerView.Adapter<Adapter_MonthlyCategoryWiseReport.MyViewHolder> {
    private List<MC_Posts> postList;
    private Context context;

    public Adapter_MonthlyCategoryWiseReport(List<MC_Posts> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_monthly_category_wise,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_date.setText(postList.get(position).getPostDateTime());
        holder.tv_description.setText(postList.get(position).getPostDescription());
        holder.tv_amount.setText(postList.get(position).getPostAmount());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_date,tv_time,tv_description,tv_amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_dateValue_sampleMonthlyCategoryWiseReport);
            tv_description = itemView.findViewById(R.id.tv_descriptionValue_sampleMonthlyCategoryWiseReport);
            tv_amount = itemView.findViewById(R.id.tv_amountValue_sampleMonthlyCategoryWiseReport);

        }
    }
}
