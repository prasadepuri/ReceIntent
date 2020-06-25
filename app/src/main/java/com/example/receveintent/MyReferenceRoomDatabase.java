package com.example.receveintent;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MyReference.class},version = 1,exportSchema = false)
public abstract class MyReferenceRoomDatabase extends RoomDatabase {

    public abstract MyReferenceDao myReferenceDao();
    private static volatile MyReferenceRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MyReferenceRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyReferenceRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyReferenceRoomDatabase.class, "myreference_database").addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    // Populate the database in the background.
                    // If you want to start with more words, just add them.
                    MyReferenceDao dao = INSTANCE.myReferenceDao();
                    //dao.deleteAll();

                    // MyReference word = new Word("Hello");
                    //dao.insert(word);
                    //word = new Word("World");
                    //dao.insert(word);
                }
            });
        }
    };
}
