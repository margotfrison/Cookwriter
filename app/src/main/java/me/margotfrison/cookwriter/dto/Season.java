package me.margotfrison.cookwriter.dto;

import lombok.AllArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.utils.AndroidResources;

@AllArgsConstructor
public enum Season {
    WINTER(R.string.season_winter),
    SPRING(R.string.season_spring),
    SUMMER(R.string.season_summer),
    AUTUMN(R.string.season_autumn),
    ALL(R.string.season_all);

    public final int stringRes;

    public String getString() {
        return AndroidResources.getString(stringRes);
    }
}
