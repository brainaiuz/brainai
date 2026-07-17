package com.edatasite.workforce.gwt.core.client.ui.notifications;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.dom.client.Element;
import gwt.material.design.client.ui.MaterialToast;

public class Info {

    public static final int DURATION = 4000;
    public static Info instance;

    public static Info get() {
        if (instance == null) {
            instance = new Info();
        }
        return instance;
    }

    public static void show(String message) {
        show(message, Type.INFO);
    }

    public static void show(String message, Position position) {
        show(message, Type.INFO, position);
    }

    public static void warn(String message) {
        show(message, Type.WARNING);
    }

    public static void warn(String message, Position position) {
        show(message, Type.WARNING, position);
    }

    public static void warn(String message, int duration) {
        show(message, Type.WARNING, Position.BOTTOM_RIGHT, duration);
    }

    public static void warn(String message, Position position, int duration) {
        show(message, Type.WARNING, position, duration);
    }

    public static void show(String message, Type type) {
        show(message, type, Position.BOTTOM_RIGHT);
    }

    public static void show(String message, Type type, Position position) {
        show(message, type, position, DURATION);
    }

    public static void show(String message, Type type, Position position, int duration) {

        getBody().setAttribute("toast", position.getClassName());

        String firstName = Utils.getFirstName() != null && !"null".equals(Utils.getFirstName()) ? Utils.getFirstName() + ", " : "";
        if (type.equals(Info.Type.WARNING)) {
            MaterialToast.fireToast("<i class='tick-toast__icon ficon--exclamation'></i>" + firstName + message, duration, "tick-toast tick-toast--error");
        } else if (type.equals(Info.Type.INFO)) {
            MaterialToast.fireToast("<i class='tick-toast__icon ficon--check'></i>" + firstName + message, duration, "tick-toast tick-toast--success");
        }
    }

    public enum Position {
        TOP_CENTER("top-center"), BOTTOM_CENTER("bottom-center"),
        TOP_LEFT("top-left"), TOP_RIGHT("top-right"), BOTTOM_LEFT("bottom-left"), BOTTOM_RIGHT("bottom-right");

        private final String className;

        Position(String className) {
            this.className = className;
        }

        public String getClassName() {
            return className;
        }
    }

    public enum Type {
        /**
         * The Humanized notification is an implementation of
         * the transparent message-pattern, and can be used to
         * indicate non-critical events while interrupting
         * the user as little as possible.
         */
        INFO,
        /**
         * The Warning notification is an implementation of
         * the transparent message-pattern, and is meant
         * to interrupt the user as little as possible,
         * while still drawing the needed attention.
         */
        WARNING,
    }

    public static native Element getBody() /*-{
        return $doc.body;
    }-*/;
}
