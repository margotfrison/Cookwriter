package me.margotfrison.cookwriter.dao;

import androidx.room.TypeConverter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.margotfrison.cookwriter.R;
import me.margotfrison.cookwriter.android.utils.AndroidResources;
import me.margotfrison.cookwriter.exceptions.InvalidTimeFormatException;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DurationConverter {
    @Getter
    private static final DurationConverter instance = new DurationConverter();

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

    public Duration parseString(String str) throws InvalidTimeFormatException {
        if (str == null)
            throw new InvalidTimeFormatException(R.string.err_time_invalid_format, "null");
        loadResources();
        String[] durationsWithUnits = str.split(DELIMITER);
        Duration duration = Duration.ZERO;
        for (String durationWithUnit : durationsWithUnits) {
            boolean validUnit = false;
            for (Map.Entry<String, TemporalUnit> durationEntry : DURATIONS.entrySet()) {
                String durationLabel = durationEntry.getKey();
                TemporalUnit durationTemporalUnit = durationEntry.getValue();
                if (durationWithUnit.endsWith(durationLabel)) {
                    String durationAmountStr = durationWithUnit.substring(0, durationWithUnit.length() - durationLabel.length());
                    try {
                        duration = duration.plus(Integer.parseInt(durationAmountStr), durationTemporalUnit);
                        validUnit = true;
                        break;
                    } catch (NumberFormatException e) {
                        throw new InvalidTimeFormatException(R.string.err_time_invalid_format, str, e);
                    }
                }
            }
            if (!validUnit) {
                throw new InvalidTimeFormatException(R.string.err_time_invalid_format, str);
            }
        }
        return duration;
    }

    @TypeConverter
    public Duration fromString(String str) {
        return Try.of(() -> parseString(str)).getOrNull();
    }

    public boolean checkString(String str) {
        return Try.run(() -> parseString(str)).isFailure();
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
