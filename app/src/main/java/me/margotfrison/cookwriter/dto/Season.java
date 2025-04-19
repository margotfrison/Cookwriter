package me.margotfrison.cookwriter.dto;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.utils.AndroidResources;

@AllArgsConstructor
public enum Season {
    WINTER(R.string.season_winter, 0),
    SPRING(R.string.season_spring, 1),
    SUMMER(R.string.season_summer, 2),
    AUTUMN(R.string.season_autumn, 3),
    ALL(R.string.season_all, 4);

    public final int stringRes;
    public final int spinnerIndex;

    @NonNull
    @Override
    public String toString() {
        return AndroidResources.getString(stringRes);
    }
}
