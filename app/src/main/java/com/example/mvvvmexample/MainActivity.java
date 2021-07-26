package com.example.mvvvmexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        btnAdd = findViewById(R.id.btnAdd);

        noteViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Notes>>() {
            //this method will get triggered whenever our data in livedata gets changed or altered.
            @Override
            public void onChanged(List<Notes> notes) {
                //update recyclerview
                noteAdapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.ItemClicked() {
            @Override
            public void onItemClicked(Notes notes) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivty.class);
                intent.putExtra("Title",notes.getTitle());
                intent.putExtra("Description",notes.getDescription());
                intent.putExtra("Priority",notes.getPriority());
                intent.putExtra("id",notes.getId());
                startActivityForResult(intent,2);
            }
        });
    }

    public void onClickAddButton(View view) {
        Intent intent = new Intent(MainActivity.this, AddEditNoteActivty.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("Title");
            String description = data.getStringExtra("Description");
            int priority = data.getIntExtra("Priority", 1);

            Notes notes = new Notes(title, description, priority);
            noteViewModel.insert(notes);

            Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();

        }

        else if (requestCode == 2 && resultCode == RESULT_OK) {
            int id = data.getIntExtra("id",-1);
            if (id == -1) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

            String title = data.getStringExtra("Title");
            String description = data.getStringExtra("Description");
            int priority = data.getIntExtra("Priority", 1);

            Notes notes = new Notes(title, description, priority);
            notes.setId(id);
            noteViewModel.update(notes);

            Toast.makeText(this, "Your note is updated!", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this, "Note NOT saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteNodesAll) {
            noteViewModel.deleteAllNotes();
            Toast.makeText(this, "All notes successfully deleted!", Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}