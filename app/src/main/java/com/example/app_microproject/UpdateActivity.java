package com.example.app_microproject;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText amount_input, note_input;
    RadioButton rb_income2,rb_expense2;

    Button update_btn;
    String category2, str_note, str_amount, str_id, cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        rb_income2 = findViewById(R.id.rb_income2);
        rb_expense2 = findViewById(R.id.rb_expense2);
        amount_input = findViewById(R.id.amount2);
        note_input = findViewById(R.id.note2);
        update_btn = findViewById(R.id.update_btn);

        category2 = getIntent().getStringExtra("track_category");
        if (category2.equals("Income")){
            rb_income2.setChecked(true);
        }else{
            rb_expense2.setChecked(true);
        }

        getAndsetIntentData();

//        ActionBar ab = getSupportActionBar();
//        if (ab != null){
//            ab.setTitle(str_note);
//        }


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabase db = new MyDatabase(UpdateActivity.this);

                str_amount = amount_input.getText().toString().trim();
                str_note = note_input.getText().toString().trim();
                if(rb_income2.isChecked()) {
                    cat = "Income";
                }else{
                    cat = "Expense";
                }

                if (Long.parseLong(str_amount)>0) {
                    db.updateData(str_id,Long.parseLong(str_amount),str_note,cat.trim());
                }else{
                    Toast.makeText(UpdateActivity.this,"Amount 0 can't be added.",Toast.LENGTH_SHORT).show();
                }

//                amount_input.getText().clear();
//                note_input.getText().clear();
            }
        });
    }

    void getAndsetIntentData() {
        if (getIntent().hasExtra("track_expId") && getIntent().hasExtra("track_amount") && getIntent().hasExtra("track_note") && getIntent().hasExtra("track_category")){
            str_id = getIntent().getStringExtra("track_expId");
            str_amount = getIntent().getStringExtra("track_amount");
            str_note = getIntent().getStringExtra("track_note");

            amount_input.setText(str_amount);
            note_input.setText(str_note);
        }else{
            Toast.makeText(this,"No data.", Toast.LENGTH_SHORT).show();
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
        builder.setTitle("Delete " + str_note + " ?");
        builder.setMessage("Are you sure you want to delete " + str_note + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabase db = new MyDatabase(UpdateActivity.this);
                db.deleteData(str_id);
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