package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 3/19/12
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlphanumComparator<T extends Object> implements Comparator<T> {

    private final boolean isDigit(char ch) {
        return ch >= 48 && ch <= 57;
    }

    /**
     * Length of string is passed in for improved efficiency (only need to calculate it once) *
     */
    private final String getChunk(String s, int slength, int marker) {
        StringBuilder chunk = new StringBuilder();
        char c = s.charAt(marker);
        chunk.append(c);
        marker++;
        if (isDigit(c)) {
            while (marker < slength) {
                c = s.charAt(marker);
                if (!isDigit(c))
                    break;
                chunk.append(c);
                marker++;
            }
        } else {
            while (marker < slength) {
                c = s.charAt(marker);
                if (isDigit(c))
                    break;
                chunk.append(c);
                marker++;
            }
        }
        return chunk.toString();
    }

    public int compareTo(Comparable o1, Comparable o2) {
        if (!(o1 instanceof String) || !(o2 instanceof String)) {
            return 0;
        }
        String s1 = (String) o1;
        String s2 = (String) o2;

        int thisMarker = 0;
        int thatMarker = 0;
        int s1Length = s1.length();
        int s2Length = s2.length();

        while (thisMarker < s1Length && thatMarker < s2Length) {
            String thisChunk = getChunk(s1, s1Length, thisMarker);
            thisMarker += thisChunk.length();

            String thatChunk = getChunk(s2, s2Length, thatMarker);
            thatMarker += thatChunk.length();

            // If both chunks contain numeric characters, sort them numerically
            int result = 0;
            if (isDigit(thisChunk.charAt(0)) && isDigit(thatChunk.charAt(0))) {
                // Simple chunk comparison by length.
                int thisChunkLength = thisChunk.length();
                result = thisChunkLength - thatChunk.length();
                // If equal, the first different number counts
                if (result == 0) {
                    for (int i = 0; i < thisChunkLength; i++) {
                        result = thisChunk.charAt(i) - thatChunk.charAt(i);
                        if (result != 0) {
                            return result;
                        }
                    }
                }
            } else {
                result = thisChunk.compareTo(thatChunk);
            }

            if (result != 0)
                return result;
        }

        return s1Length - s2Length;
    }

    public int internalCompare(Comparable c1, Comparable c2, int sortOrder) {
        int result;
        if (c1 == c2) {
            return 0;
        }
        // Null value is the biggest value whenever it goes!
        if (c1 == null && c2 != null) {
            return Integer.MAX_VALUE;
        }
        if (c2 == null && c1 != null) {
            return Integer.MIN_VALUE;
        }
        return switch (sortOrder) {
            case Constants.ASC -> compareTo(c1, c2);
            case Constants.DESC -> compareTo(c2, c1);
            default -> 0;
        };
    }

    @Override
    public int compare(T o1, T o2) {
        return 0;
    }
}
