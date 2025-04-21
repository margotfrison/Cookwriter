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

/**
 * Used to convert a {@link Duration} into a {@link String} and vice-versa.
 * This class is also used by the database framework to store {@link Duration}s<br>
 * <br>
 * The convention for a valid {@link String} is the following:
 * <li>The {@link String} must be a list of substrings each separated by {@link DurationConverter#DELIMITER}
 * <li>Each substring must begin with a number and end with a temporal unit from the user's locale
 * defined in the android string resources list<br>
 * <br>
 * Examples of valid {@link String}s in english locale:
 * <li>1d 1h 1m 1s (for 1 day, 1 hour, 1 minute and 1 second)
 * <li>1d (for 1 day)
 * <li>2h 30m (for 2 hours and 30 minutes)
 */
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

    /**
     * Load static resources lazily with the static context from the {@link me.margotfrison.cookwriter.android.App} class
     */
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

    /**
     * Attempt to parse a {@link String} into a {@link Duration}. See class javadoc for the convention
     * @param str the {@link String} to parse
     * @return a {@link Duration} equivalent to the {@link String} value
     * @throws InvalidTimeFormatException if the {@link String} cannot be parsed or if it's invalid
     */
    public Duration parseString(String str) throws InvalidTimeFormatException {
        if (str == null)
            throw new InvalidTimeFormatException(R.string.err_time_invalid_format, "null");
        loadResources();
        String[] durationsWithUnits = str.split(DELIMITER);
        Duration duration = Duration.ZERO;
        // For each string, we split with along spaces and we try to recognize a number and a duration unit
        for (String durationWithUnit : durationsWithUnits) {
            boolean validUnit = false;
            // Test each allowed duration unit for this substring
            for (Map.Entry<String, TemporalUnit> durationEntry : DURATIONS.entrySet()) {
                String durationLabel = durationEntry.getKey();
                TemporalUnit durationTemporalUnit = durationEntry.getValue();
                // We found a duration unit that matched
                if (durationWithUnit.endsWith(durationLabel)) {
                    String durationAmountStr = durationWithUnit.substring(0, durationWithUnit.length() - durationLabel.length());
                    try {
                        // Parse the number and add it to the current duration being parsed
                        duration = duration.plus(Integer.parseInt(durationAmountStr), durationTemporalUnit);
                        validUnit = true;
                        break;
                    } catch (NumberFormatException e) {
                        throw new InvalidTimeFormatException(R.string.err_time_invalid_format, str, e);
                    }
                }
            }
            // No duration unit allowed has been found, this string is invalid
            if (!validUnit) {
                throw new InvalidTimeFormatException(R.string.err_time_invalid_format, str);
            }
        }
        return duration;
    }

    /**
     * Similar to {@link DurationConverter#parseString(String)} but return null if the {@link String} is invalid
     * instead of throwing a {@link InvalidTimeFormatException}
     * @param str the {@link String} to parse
     * @return a {@link Duration} if the string is valid or null otherwise
     */
    @TypeConverter
    public Duration fromString(String str) {
        return Try.of(() -> parseString(str)).getOrNull();
    }

    /**
     * Test if the string is valid. This means {@link DurationConverter#parseString(String)} will not
     * throw an {@link Exception}
     * @param str the {@link String} to test
     * @return true if the {@link String} is valid, false otherwise
     */
    public boolean checkString(String str) {
        return !Try.run(() -> parseString(str)).isFailure();
    }

    /**
     * Convert a {@link Duration} into a {@link String} according to the convention that is specified in the class javadoc
     * @param duration the {@link Duration} to parse into a {@link String}
     * @return a {@link String} representing the {@link Duration}
     */
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
