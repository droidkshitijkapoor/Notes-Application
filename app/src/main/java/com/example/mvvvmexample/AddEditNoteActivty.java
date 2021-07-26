package com.example.mvvvmexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivty extends AppCompatActivity {

    EditText etTitle, etDescription;
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        numberPicker = findViewById(R.id.pickerPriority);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(15);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);

        if (getIntent().hasExtra("id")) {
            setTitle("Edit Note");
            etTitle.setText(getIntent().getStringExtra("Title"));
            etDescription.setText(getIntent().getStringExtra("Description"));
            numberPicker.setValue(getIntent().getIntExtra("Priority",1));

        } else
            setTitle("Add note");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        if (etTitle.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "Kindly fill all the details!", Toast.LENGTH_SHORT).show();
        } else {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            int numberPickerPriority = numberPicker.getValue();

            Intent intent = new Intent();
            intent.putExtra("Title", title);
            intent.putExtra("Description", description);
            intent.putExtra("Priority", numberPickerPriority);

            int id = getIntent().getIntExtra("id",-1);
            if (id !=1) {
                intent.putExtra("id",id);
            }

            setResult(RESULT_OK, intent);
            finish();

        }
    }
}