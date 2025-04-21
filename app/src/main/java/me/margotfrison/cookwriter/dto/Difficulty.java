package me.margotfrison.cookwriter.dto;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.utils.AndroidResources;

@AllArgsConstructor
public enum Difficulty {
    BRAINLESS(R.string.difficulty_brainless, 0), // No cutting or prep is required and cooking is as easy as it gets
    EASY(R.string.difficulty_easy, 1), // Rough cutting and prep needed. Cooking is straight forward
    NORMAL(R.string.difficulty_normal, 2), // For some refined cutting, large amount of prep or cooking that require attention
    DELICATE(R.string.difficulty_delicate, 3), // Refined cutting and cooking that require attention and are unavoidable for the recipe to be successful
    EXPERT(R.string.difficulty_expert, 4); // Star Michelin recipe. Where all step have to be executed by the book to be successful

    public final int stringRes;
    public final int spinnerIndex;

    @NonNull
    @Override
    public String toString() {
        return AndroidResources.getString(stringRes);
    }
}
