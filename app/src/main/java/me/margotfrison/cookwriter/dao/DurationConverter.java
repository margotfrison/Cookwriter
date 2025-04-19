package me.margotfrison.cookwriter.dao;

import androidx.room.TypeConverter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.utils.AndroidResources;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DurationConverter {
    private static final String DELIMITER = " ";

    private static String SECONDS;
    private static String MINUTES;
    private static String HOURS;
    private static String DAYS;
    private static Map<String, TemporalUnit> DURATIONS;

    private void loadResources() {
        if (SECONDS == null) SECONDS = AndroidResources.getString(R.string.seconds_short);
        if (MINUTES == null) MINUTES = AndroidResources.getString(R.string.minutes_short);
        if (HOURS == null) HOURS = AndroidResources.getString(R.string.hours_short);
        if (DAYS == null) DAYS = AndroidResources.getString(R.string.days_short);
        DURATIONS = Map.of(
                SECONDS, ChronoUnit.SECONDS,
                MINUTES, ChronoUnit.MINUTES,
                HOURS, ChronoUnit.HOURS,
                DAYS, ChronoUnit.DAYS
        );
    }

    @TypeConverter
    public Duration fromString(String str) {
        if (str == null)
            return null;
        loadResources();
        String[] durationsWithUnits = str.split(DELIMITER);
        Duration duration = Duration.ZERO;
        for (String durationWithUnit : durationsWithUnits) {
            for (Map.Entry<String, TemporalUnit> durationEntry : DURATIONS.entrySet()) {
                String durationLabel = durationEntry.getKey();
                TemporalUnit durationTemporalUnit = durationEntry.getValue();
                if (durationWithUnit.endsWith(durationLabel)) {
                    try {
                        String durationAmountStr = durationWithUnit.substring(0, durationWithUnit.length() - durationLabel.length());
                        duration = duration.plus(Integer.parseInt(durationAmountStr), durationTemporalUnit);
                        break;
                    } catch (NumberFormatException ignored) { }
                }
            }
        }
        return duration;
    }

    @TypeConverter
    public String toString(Duration duration) {
        if (duration == null)
            return null;
        loadResources();
        // LinkedHashMap is used to preserve entries' order
        LinkedHashMap<String, Long> linkedMap = new LinkedHashMap<>();
        linkedMap.put(DAYS, duration.toDays());
        linkedMap.put(HOURS, (long) duration.toHoursPart());
        linkedMap.put(MINUTES, (long) duration.toMinutesPart());
        linkedMap.put(SECONDS, (long) duration.toSecondsPart());
        String str = linkedMap.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .map(e -> e.getValue() + e.getKey())
                .collect(Collectors.joining(DELIMITER));
        if (str.isEmpty())
            return "0s";
        return str;
    }
}
