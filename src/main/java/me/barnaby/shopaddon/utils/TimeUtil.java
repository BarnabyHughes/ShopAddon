package me.barnaby.shopaddon.utils;

public class TimeUtil {

    public static long parseInterval(String intervalString) {
        int value = Integer.parseInt(intervalString.substring(0, intervalString.length() - 1));
        char unit = intervalString.charAt(intervalString.length() - 1);

        long multiplier;
        switch (unit) {
            case 's':
                multiplier = 1;
                break;
            case 'm':
                multiplier = 60;
                break;
            case 'h':
                multiplier = 60 * 60;
                break;
            default:
                throw new IllegalArgumentException("Unsupported time unit: " + unit);
        }

        return value * multiplier;
    }

}
