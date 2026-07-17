package com.finnetlimited.reportservice.core.server.parser;

/**
 * User: ${Dilsh0d}
 * Date: 14-Apr-2010
 * Time: 21:28:43
 */
public final class HTMLParser {

    private static final String B = "<b>";
    private static final String _B = "</b>";
    private static final String BR = "<br>";
    private static final String BR_ = "<br/>";
    private static final String _BR = "</br>";
    private static final String P = "<p>";
    private static final String _P = "</p>";

    public static String getText(String html) {
        if (html.contains(B)) {
            html = html.replaceAll(B, "");
            html = html.replaceAll(_B, "");
        }

        if (html.contains(BR)) {
            html = html.replaceAll(BR, "\n");
        }
        if (html.contains(_BR)) {
            html = html.replaceAll(_BR, "\n");
        }
        if (html.contains(BR_)) {
            html = html.replaceAll(BR_, "\n");
        }
        if (html.contains(P)) {
            html = html.replaceAll(P, "");
            html = html.replaceAll(_P, "");
        }
        return html;
    }
}
