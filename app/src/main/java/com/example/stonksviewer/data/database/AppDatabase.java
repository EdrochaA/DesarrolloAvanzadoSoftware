package com.example.stonksviewer.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.stonksviewer.data.dao.UserDao;
import com.example.stonksviewer.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "stonksviewer_db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // Solo para pruebas, en producción usar AsyncTask o LiveData
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
