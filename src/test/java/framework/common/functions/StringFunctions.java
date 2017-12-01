package framework.common.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringFunctions {
    /**
     * This method creates a RegExp pattern.
     *
     * @param regex     pattern in a string
     * @param matchCase should be matching case sensitive?
     */
    private static Pattern regexGetPattern(String regex, boolean matchCase) {
        int flags;
        if (matchCase) {
            flags = 0;
        } else {
            flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
        }
        return Pattern.compile(regex, flags);
    }

    /**
     * Get first match in the string
     */
    public static String regexGetMatch(String text, String regex) {
        return regexGetMatch(text, regex, false);
    }

    /**
     * Get first match in the string
     */
    public static String regexGetMatch(String text, String regex, boolean matchCase) {
        return regexGetMatchGroup(text, regex, 0, matchCase);
    }

    /**
     * validate, that string corresponds a pattern
     */
    public static boolean regexIsMatch(String text, String pattern) {
        return regexIsMatch(text, pattern, false);
    }

    /**
     * validate, that string corresponds a pattern
     */
    public static boolean regexIsMatch(String text, String regex, boolean matchCase) {
        Pattern p = regexGetPattern(regex, matchCase);
        Matcher m = p.matcher(text);
        return m.find();
    }

    /**
     * Get the N-th matching group
     *
     * @param text       - String where we are looking for
     * @param regex      - pattern for which we are looking for
     * @param groupIndex -Number of matching group we want to find
     */
    public static String regexGetMatchGroup(String text, String regex, int groupIndex) {
        return regexGetMatchGroup(text, regex, groupIndex, false);
    }

    /**
     * Get the N-th matching group
     *
     * @param text       - String where we are looking for
     * @param regex      - pattern for which we are looking for
     * @param groupIndex - Number of matching group we want to find
     * @param matchCase  - Is search case sensitive?
     */
    public static String regexGetMatchGroup(String text, String regex, int groupIndex, boolean matchCase) {
        Pattern p = regexGetPattern(regex, matchCase);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(groupIndex);
        } else {
            return null;
        }
    }

    /**
     * Get the casesCount of groups has been found
     *
     * @param text  - String where we are looking for
     * @param regex - pattern for which we are looking for * @param matchCase - Is search case sensitive?
     */
    public static int regexGetNumberMatchGroup(String text, String regex) {
        return regexGetNumberMatchGroup(text, regex, false);
    }

    /**
     * Get the casesCount of groups has been found
     *
     * @param text  - String where we are looking for
     * @param regex - pattern for which we are looking for
     */
    public static int regexGetNumberMatchGroup(String text, String regex, boolean matchCase) {
        int number = 0;
        Pattern p = regexGetPattern(regex, matchCase);
        Matcher m = p.matcher(text);
        while (m.find()) {
            m.group();
            number++;
        }
        return number;
    }

    /**
     * Escape () and backslash
     *
     * @param text - text with special symbols
     * @return escaped text
     */
    public static String escapeMetaCharacters(final String text) {
        return text.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
    }

    /**
     * Escape backslash and quote
     *
     * @param text - text with special symbols
     * @return text without backslashes
     */
    public static String escapeSeleniumCharacters(final String text) {
        return text.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"");
    }

    /**
     * Adding \s to the numbers in the string (useful in validation, if there is a space between digits ("99 678" for
     * example))
     *
     * @param text - text with numbers
     * @return escapeSpacesFromNumbers text
     */
    public static String escapeSpacesFromNumbers(final String text) {
        return text.replaceAll("(\\d)", "$1\\\\s*");
    }
}
