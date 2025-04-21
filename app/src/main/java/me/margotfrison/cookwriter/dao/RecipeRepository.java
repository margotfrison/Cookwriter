package me.margotfrison.cookwriter.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import me.margotfrison.cookwriter.dto.Recipe;
import me.margotfrison.cookwriter.dto.Season;

/**
 * {@link Recipe} queries for the database
 */
@Dao
public interface RecipeRepository {
    @Query("SELECT * FROM recipe WHERE author = :previewRecipeAuthor AND customName = :previewRecipeCustomName")
    Single<Recipe> get(String previewRecipeAuthor, String previewRecipeCustomName);

    @Query("SELECT author, customName, cleanName FROM recipe")
    Single<List<Recipe>> getAllAsPreview();

    @Query("SELECT * FROM recipe WHERE season LIKE :season")
    Single<List<Recipe>> getAllForSeason(Season season);

    @Query("SELECT * FROM recipe WHERE categories LIKE :category")
    Single<List<Recipe>> getAllForCategory(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Recipe... recipe);

    @Delete
    Completable delete(Recipe recipe);

    @Query("SELECT EXISTS(SELECT * FROM recipe WHERE author = :previewRecipeAuthor AND customName = :previewRecipeCustomName)")
    Single<Boolean> exists(String previewRecipeAuthor, String previewRecipeCustomName);
}
