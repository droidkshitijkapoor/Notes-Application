package com.example.mvvvmexample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NotesRepository notesRepository;
    private LiveData<List<Notes>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        notesRepository = new NotesRepository(application);
        allNotes = notesRepository.getAllNotes();
    }

    public void insert(Notes notes) {
        notesRepository.insert(notes);
    }

    public void update(Notes notes) {
        notesRepository.update(notes);
    }
    public void delete(Notes notes) {
        notesRepository.delete(notes);
    }
    public void deleteAllNotes() {
        notesRepository.deleteAllNotes();
    }

    public LiveData<List<Notes>> getAllNotes(){
        return allNotes;
    }






}
