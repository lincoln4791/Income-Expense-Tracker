package com.example.dailyexpensemanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyexpensemanager.R;
import com.example.dailyexpensemanager.common.Constants;
import com.example.dailyexpensemanager.common.NodeName;
import com.example.dailyexpensemanager.model.MC_Posts;
import com.example.dailyexpensemanager.view.Daily;
import com.example.dailyexpensemanager.view.EditDataExpense;
import com.example.dailyexpensemanager.view.EditDataIncome;
import com.example.dailyexpensemanager.view.Transactions;

import java.util.List;

public class Adapter_Daily extends RecyclerView.Adapter<Adapter_Daily.MyViewHolder> {
    private List<MC_Posts> postList;
    private Context context;
    private CardView cv_Temp=null,cv_new;
    private int i=0;

    public Adapter_Daily(List<MC_Posts> postList, Context context) {
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


        if(postList.get(position).getPostType().equals(Constants.TYPE_INCOME)){
            Log.d("tag","color green"+postList.get(position).getPostType());
            holder.cv_typeHolder.setCardBackgroundColor(context.getColor(R.color.green));
        }

        holder.tv_dateTime.setText(postList.get(position).getPostDateTime());
        holder.tv_category.setText(postList.get(position).getPostCategory());
        holder.tv_amount.setText(postList.get(position).getPostAmount());
        holder.tv_description.setText(postList.get(position).getPostDescription());


        holder.cv_mainLayout.setOnClickListener(v -> {

        });


        holder.cv_mainLayout.setOnLongClickListener(v -> {
            if(cv_Temp!=null){
                cv_Temp.setVisibility(View.GONE);
            }
            cv_Temp = holder.cv_editDltLayout;
            cv_Temp.setVisibility(View.VISIBLE);
            return false;
        });



        holder.iv_editData.setOnClickListener(v -> {
            if(postList.get(position).getPostType().equals(Constants.TYPE_INCOME)){
                Intent editDataIncomeIntent = new Intent(context, EditDataIncome.class);
                editDataIncomeIntent.putExtra(NodeName.ID,String.valueOf(postList.get(position).getID()));
                editDataIncomeIntent.putExtra(NodeName.POST_DESCRIPTION,postList.get(position).getPostDescription());
                editDataIncomeIntent.putExtra(NodeName.POST_CATEGORY,postList.get(position).getPostCategory());
                editDataIncomeIntent.putExtra(NodeName.POST_AMOUNT,postList.get(position).getPostAmount());
                editDataIncomeIntent.putExtra(NodeName.POST_YEAR,postList.get(position).getPostYear());
                editDataIncomeIntent.putExtra(NodeName.POST_MONTH,postList.get(position).getPostMonth());
                editDataIncomeIntent.putExtra(NodeName.POST_DAY,postList.get(position).getPostDay());
                editDataIncomeIntent.putExtra(NodeName.POST_TIME,postList.get(position).getPostTime());
                editDataIncomeIntent.putExtra(NodeName.POST_DATE_TIME,postList.get(position).getPostDateTime());

                context.startActivity(editDataIncomeIntent);
            }
            else if (postList.get(position).getPostType().equals(Constants.TYPE_EXPENSE)){
                Intent editDataExpenseIntent = new Intent(context, EditDataExpense.class);
                editDataExpenseIntent.putExtra(NodeName.ID,String.valueOf(postList.get(position).getID()));
                editDataExpenseIntent.putExtra(NodeName.POST_DESCRIPTION,postList.get(position).getPostDescription());
                editDataExpenseIntent.putExtra(NodeName.POST_CATEGORY,postList.get(position).getPostCategory());
                editDataExpenseIntent.putExtra(NodeName.POST_AMOUNT,postList.get(position).getPostAmount());
                editDataExpenseIntent.putExtra(NodeName.POST_YEAR,postList.get(position).getPostYear());
                editDataExpenseIntent.putExtra(NodeName.POST_MONTH,postList.get(position).getPostMonth());
                editDataExpenseIntent.putExtra(NodeName.POST_DAY,postList.get(position).getPostDay());
                editDataExpenseIntent.putExtra(NodeName.POST_TIME,postList.get(position).getPostTime());
                editDataExpenseIntent.putExtra(NodeName.POST_DATE_TIME,postList.get(position).getPostDateTime());
                context.startActivity(editDataExpenseIntent);
            }
        });


        holder.iv_deleteData.setOnClickListener(v -> {
            ((Daily)context).confirmDelete(postList.get(position).getID());
        });

        holder.iv_closeEdtDltLayout.setOnClickListener(v -> {
            holder.cv_editDltLayout.setVisibility(View.GONE);
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_dateTime,tv_category,tv_amount,tv_description;
        private CardView cv_typeHolder,cv_mainLayout,cv_editDltLayout;
        private ImageView iv_editData, iv_deleteData, iv_closeEdtDltLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_description = itemView.findViewById(R.id.tv_description_sample_transactions);
            tv_dateTime = itemView.findViewById(R.id.tv_dateTime_sample_transactions);
            tv_category = itemView.findViewById(R.id.tv_category_sample_transactions);
            tv_amount = itemView.findViewById(R.id.tv_amount_sample_transactions);
            iv_editData = itemView.findViewById(R.id.iv_edit_sampleTransactions);
            iv_deleteData = itemView.findViewById(R.id.iv_delete_sampleTransactions);
            iv_closeEdtDltLayout = itemView.findViewById(R.id.iv_closeEditDlt_sampleTransactions);
            cv_editDltLayout = itemView.findViewById(R.id.cv_editDltHolder_sampleTransactions);
            cv_typeHolder = itemView.findViewById(R.id.cv_type_sample_transactions);
            cv_mainLayout = itemView.findViewById(R.id.cv_MainLayout_sampleTransaction);

        }
    }



}
