package com.example.app_microproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.example.app_microproject.databinding.ActivityMainBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TextView add_Exp_Inc;
    MyDatabase db;
    ArrayList<String> Tracker_expenseId, Tracker_amount, Tracker_note, Tracker_category, Tracker_date;
    ExpensesAdapter expensesAdapter;
    RecyclerView recyclerView;
    int sum_amount=0,sum_expense=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = new Intent(MainActivity.this,AddExpenseActivity.class);

        recyclerView = findViewById(R.id.recycler);
        add_Exp_Inc = findViewById(R.id.add_Exp_Inc);

        add_Exp_Inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","Income");
                startActivity(intent);
            }
        });

        db = new MyDatabase(MainActivity.this);
        Tracker_expenseId = new ArrayList<>();
        Tracker_amount = new ArrayList<>();
        Tracker_note = new ArrayList<>();
        Tracker_category = new ArrayList<>();
        Tracker_date = new ArrayList<>();

        storeDataInArrays();
        sum_amount = db.total_amountData();
        sum_expense = db.total_expenseData();

        setUpGraph();

        expensesAdapter = new ExpensesAdapter(MainActivity.this,this, Tracker_expenseId, Tracker_amount, Tracker_note, Tracker_category, Tracker_date);
        recyclerView.setAdapter(expensesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    private void setUpGraph() {
        List<PieEntry> pieEntryList = new ArrayList<>();
        List<Integer> colorsList = new ArrayList<>();

        if (sum_amount!=0) {
            pieEntryList.add(new PieEntry(sum_amount,"Income"));
            colorsList.add(getResources().getColor(R.color.teal_700));
        }
        if (sum_expense!=0) {
            pieEntryList.add(new PieEntry(sum_expense,"Expense"));
            colorsList.add(getResources().getColor(R.color.orange));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntryList,"Balance : " + String.valueOf(sum_amount-sum_expense));
        pieDataSet.setColors(colorsList);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieDataSet.setValueTextSize(15);
        PieData pieData = new PieData(pieDataSet);

        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()) {
                Tracker_expenseId.add(cursor.getString(0));
                Tracker_amount.add(cursor.getString(1));
                Tracker_note.add(cursor.getString(2));
                Tracker_category.add(cursor.getString(3));
                Tracker_date.add(cursor.getString(4));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_Expense) {
            confirm_dialog();
            return true;
        }
        return false;
    }

    void confirm_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All");
        builder.setMessage("Are you sure you want to delete all ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabase db = new MyDatabase(MainActivity.this);
                db.deleteAllData();

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}