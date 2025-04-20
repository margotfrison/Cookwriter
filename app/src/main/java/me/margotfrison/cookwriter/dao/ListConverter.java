package me.margotfrison.cookwriter.dao;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import me.margotfrison.cookwriter.dto.Ingredient;
import me.margotfrison.cookwriter.dto.Step;

/**
 * Util class used by the database framework to store {@link List}s
 */
public class ListConverter {
    @TypeConverter
    public String fromStringList(List<String> list) {
        return fromList(list);
    }

    @TypeConverter
    public List<String> toStringList(String json) {
        return toList(json, String[].class);
    }

    @TypeConverter
    public String fromIngredientList(List<Ingredient> list) {
        return fromList(list);
    }

    @TypeConverter
    public List<Ingredient> toIngredientList(String json) {
        return toList(json, Ingredient[].class);
    }

    @TypeConverter
    public String fromStepList(List<Step> list) {
        return fromList(list);
    }

    @TypeConverter
    public List<Step> toStepList(String json) {
        return toList(json, Step[].class);
    }

    private static <T> String fromList(List<T> list) {
        if (list == null)
            return null;
        return new Gson().toJson(list);
    }

    private static <T> List<T> toList(String json, Class<T[]> clazz) {
        if (json == null)
            return null;
        T[] res = new Gson().fromJson(json, clazz);
        if (res == null)
            return null;
        return Arrays.asList(res);
    }
}
