package com.example.app_microproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.MyViewHolder> {
    Activity activity;
    private Context context;
    private ArrayList Tracker_expenseId, Tracker_amount,Tracker_note,Tracker_category,Tracker_date;

    ExpensesAdapter(Activity activity ,Context context, ArrayList Tracker_expenseId, ArrayList Tracker_amount, ArrayList Tracker_note, ArrayList Tracker_category ,ArrayList Tracker_date) {
        this.activity = activity;
        this.activity = activity;
        this.context = context;
        this.Tracker_expenseId = Tracker_expenseId;
        this.Tracker_amount = Tracker_amount;
        this.Tracker_note = Tracker_note;
        this.Tracker_category = Tracker_category;
        this.Tracker_date = Tracker_date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.expense_row,parent,false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//        this.position = position;

        holder.Tracker_amount_text.setText(String.valueOf(Tracker_amount.get(position)));
        holder.Tracker_note_text.setText(String.valueOf(Tracker_note.get(position)));
        holder.Tracker_category_text.setText(String.valueOf(Tracker_category.get(position)));
        holder.Tracker_date_text.setText(String.valueOf(Tracker_date.get(position)));

        holder.expense_row.setOnClickListener((view) -> {
            Intent intent = new Intent(context,UpdateActivity.class);
            intent.putExtra("track_expId",String.valueOf(Tracker_expenseId.get(position)));
            intent.putExtra("track_amount",String.valueOf(Tracker_amount.get(position)));
            intent.putExtra("track_note",String.valueOf(Tracker_note.get(position)));
            intent.putExtra("track_category",String.valueOf(Tracker_category.get(position)));
//            context.startActivity(intent);
            activity.startActivityForResult(intent,1);
        });
    }

    @Override
    public int getItemCount() {
        return Tracker_expenseId.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Tracker_amount_text, Tracker_note_text , Tracker_category_text , Tracker_date_text;
        LinearLayout expense_row;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Tracker_amount_text = itemView.findViewById(R.id.show_amount);
            Tracker_note_text = itemView.findViewById(R.id.show_note);
            Tracker_category_text = itemView.findViewById(R.id.show_category);
            Tracker_date_text = itemView.findViewById(R.id.show_date);

            expense_row = itemView.findViewById(R.id.exp_row);
        }
    }
}
