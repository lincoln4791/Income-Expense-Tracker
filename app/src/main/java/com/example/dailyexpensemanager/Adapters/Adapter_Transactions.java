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

public class Adapter_Transactions extends RecyclerView.Adapter<Adapter_Transactions.MyViewHolder> {

    private List<MC_Posts> postList;
    private Context context;

    public Adapter_Transactions(List<MC_Posts> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_transactions,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_dateTime.setText(postList.get(position).getPostDateTime());
        holder.tv_category.setText(postList.get(position).getPostCategory());
        holder.tv_type.setText(postList.get(position).getPostType());
        holder.tv_amount.setText(postList.get(position).getPostAmount());


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_dateTime,tv_type,tv_category,tv_amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dateTime = itemView.findViewById(R.id.tv_dateTime_SampleTransactions);
            tv_type = itemView.findViewById(R.id.tv_type_SampleTransactions);
            tv_category = itemView.findViewById(R.id.tv_category_SampleTransactions);
            tv_amount = itemView.findViewById(R.id.tv_amount_SampleTransactions);
        }
    }
}
