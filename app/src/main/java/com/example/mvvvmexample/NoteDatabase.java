package com.example.mvvvmexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Notes.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    //making this class a singleton class so that we dont want to create multiple instances.
    // instead we'll create a single instance and use it everywhere in the app
    // instead of starting with an empty table, we can add some data initially, so that we can some something
    //in our recyclerview.
    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database").addCallback(callback)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    //Working for all APIs 29 and below; doesn't work for API 30 as async task is deprecated
    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase database) {
            noteDao = database.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Notes("Title 1","Description 1",1));
            noteDao.insert(new Notes("Title 2","Description 2",2));
            noteDao.insert(new Notes("Title 3","Description 3",3));
            return null;
        }
    }
}
