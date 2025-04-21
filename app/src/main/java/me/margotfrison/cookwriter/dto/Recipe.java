package me.margotfrison.cookwriter.dto;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import java.time.Duration;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import me.margotfrison.cookwriter.dao.DurationConverter;
import me.margotfrison.cookwriter.dao.ListConverter;

@Data
@Builder
@AllArgsConstructor
@FieldNameConstants
@Entity(primaryKeys = {"author", "customName"})
public class Recipe {
    @NonNull
    private String author;
    @NonNull
    private String customName; // meant for the user to give a funny name to their recipe (ex: Grandma's boeuf bourguignon)
    private String cleanName; // meant to describe the original/clean recipe name (ex: Boeuf bourguignon)
    private String description;
    private String source;
    @TypeConverters({DurationConverter.class})
    private Duration totalDuration;
    private Season season;
    private Difficulty difficulty;
    private Price price;
    @TypeConverters({ListConverter.class})
    private List<String> categories;
    @TypeConverters({ListConverter.class})
    private List<String> labels;
    @TypeConverters({ListConverter.class})
    private List<Ingredient> ingredients;
    @TypeConverters({ListConverter.class})
    private List<Step> steps;
    private Boolean done;
}
