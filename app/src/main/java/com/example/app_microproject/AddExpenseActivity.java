package com.example.app_microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.app_microproject.databinding.ActivityAddExpenseBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    ActivityAddExpenseBinding binding;
    private String category;
    RadioButton rb_income,rb_expense;
    EditText clear_amount, clear_note;
    Button add_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rb_income = findViewById(R.id.rb_income);
        rb_expense = findViewById(R.id.rb_expense);
        category = getIntent().getStringExtra("category");
        if (category.equals("Income")){
            rb_income.setChecked(true);
        }else{
            rb_expense.setChecked(true);
        }

        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createExpense();

                clear_amount = findViewById(R.id.amount);
                clear_note = findViewById(R.id.note);

                clear_amount.getText().clear();
                clear_note.getText().clear();
            }
        });
    }

    private void createExpense() {
        String amount=binding.amount.getText().toString();
        String note=binding.note.getText().toString();
        String category;
        boolean checked = binding.rbIncome.isChecked();
        if(checked){
            category = "Income";

        }else{
            category = "Expense";
        }

        if (amount.trim().length()==0) {
            binding.amount.setError("Empty Field");
            return;
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy 'At' hh:mm:ss aa");
        String datetime = dateformat.format(c.getTime());

        if (Long.parseLong(amount)>0) {
            MyDatabase mydb = new MyDatabase(AddExpenseActivity.this);
            mydb.addAmount(Long.parseLong(amount),note,category, datetime);

        }else{
            Toast.makeText(this,"Amount 0 can't be added.",Toast.LENGTH_SHORT).show();
        }
    }
}