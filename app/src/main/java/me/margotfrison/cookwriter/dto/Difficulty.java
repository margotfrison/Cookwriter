package me.margotfrison.cookwriter.dto;

import lombok.AllArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.utils.AndroidResources;

@AllArgsConstructor
public enum Difficulty {
    BRAINLESS(R.string.difficulty_brainless), // No cutting or prep is required and cooking is as easy as it gets
    EASY(R.string.difficulty_easy), // Rough cutting and prep needed. Cooking is straight forward
    NORMAL(R.string.difficulty_normal), // For some refined cutting, large amount of prep or cooking that require attention
    DELICATE(R.string.difficulty_delicate), // Refined cutting and cooking that require attention and are unavoidable for the recipe to be successful
    EXPERT(R.string.difficulty_expert); // Star Michelin recipe. Where all step have to be executed by the book to be successful

    public final int stringRes;

    public String getString() {
        return AndroidResources.getString(stringRes);
    }
}
