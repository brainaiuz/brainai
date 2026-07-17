package com.edatasite.workforce.gwt.core.client;

public class WordUtils {
    public WordUtils() {
    }

    public static String capitalize(String str) {
        return capitalize(str, (char[])null);
    }

    public static String capitalize(String str, char... delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (!isEmpty(str) && delimLen != 0) {
            char[] buffer = str.toCharArray();
            boolean capitalizeNext = true;

            for(int i = 0; i < buffer.length; ++i) {
                char ch = buffer[i];
                if (isDelimiter(ch, delimiters)) {
                    capitalizeNext = true;
                } else if (capitalizeNext) {
                    buffer[i] = Character.toUpperCase(ch);
                    capitalizeNext = false;
                }
            }

            return new String(buffer);
        } else {
            return str;
        }
    }

    public static String capitalizeFirst(String str) {
        if (!isEmpty(str)) {
            char[] buffer = str.toCharArray();
            buffer[0] = Character.toUpperCase(buffer[0]);

            return new String(buffer);
        } else {
            return str;
        }
    }

    public static String capitalizeFully(String str) {
        return capitalizeFully(str, (char[])null);
    }

    public static String capitalizeFully(String str, char... delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (!isEmpty(str) && delimLen != 0) {
            str = str.toLowerCase();
            return capitalize(str, delimiters);
        } else {
            return str;
        }
    }

    public static String uncapitalize(String str) {
        return uncapitalize(str, (char[])null);
    }

    public static String uncapitalize(String str, char... delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (!isEmpty(str) && delimLen != 0) {
            char[] buffer = str.toCharArray();
            boolean uncapitalizeNext = true;

            for(int i = 0; i < buffer.length; ++i) {
                char ch = buffer[i];
                if (isDelimiter(ch, delimiters)) {
                    uncapitalizeNext = true;
                } else if (uncapitalizeNext) {
                    buffer[i] = Character.toLowerCase(ch);
                    uncapitalizeNext = false;
                }
            }

            return new String(buffer);
        } else {
            return str;
        }
    }

    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        } else {
            char[] arr$ = delimiters;
            int len$ = delimiters.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                char delimiter = arr$[i$];
                if (ch == delimiter) {
                    return true;
                }
            }

            return false;
        }
    }
    /**
     * str string which only first letter has to be capitalized
     * str length has to be more than 2
     * @param str
     */

    public static String uppercaseFirstLetterOnly(String str){
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}

