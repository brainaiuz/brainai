package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import org.apache.commons.lang3.StringUtils;

import static java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT;
import static java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
import static java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING;
import static java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE;

/**
 * User: Abror Abdukadirov
 * Date: 17.07.2018 14:59
 */
public class DirectionUtils {

    public static boolean hasRTL(String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }
        for (int i = 0, n = text.length(); i < n; ++i) {
            char c = text.charAt(i);
            if (c != ' ') {
                byte d = Character.getDirectionality(c);
                switch (d) {
                    case DIRECTIONALITY_RIGHT_TO_LEFT, DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC, DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING, DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE -> {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String dir(String text) {
        if (hasRTL(text)) {
            return "rtl";
        }
        return "ltr";
    }
}
