package me.margotfrison.cookwriter.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import me.margotfrison.cookwriter.dto.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeRepository recipeRepository();

    public static final Migration[] MIGRATIONS = new Migration[] { };
}
