package me.margotfrison.cookwriter.android;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import lombok.Getter;
import me.margotfrison.cookwriter.dao.AppDatabase;

public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    @Getter
    private static Context context;
    @Getter
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "cookwriter-database")
                .addMigrations(AppDatabase.MIGRATIONS)
                .build();
    }
}
