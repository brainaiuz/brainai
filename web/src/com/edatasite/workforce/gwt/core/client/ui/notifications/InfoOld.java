package com.edatasite.workforce.gwt.core.client.ui.notifications;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * User: Jamshid Asatillayev
 * Date: 9/10/11
 * Time: 9:17 AM
 */

public class InfoOld extends PopupPanel {
    private final int Z_INDEX_BASE = 20000;
    private static int startOpacity = 90;
    private static int fadeMsec = 400;
    private static int delayMsec = 2000;
    private Timer fader;
    private Timer delay;
    private static InfoOld instance;
    private HTML caption;
    private HTML message;

    public interface Bundle extends ClientBundle {
        @CssResource.NotStrict
        @Source ("com/edatasite/workforce/gwt/core/client/ui/notifications/notification.css")
        CssResource infoCss();
    }

    public InfoOld() {

    }

/*    public interface Bundles extends ClientBundle {
        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/core/client/ui/notifications/info.css")
        CssResource info();

        @ClientBundle.Source("com/edatasite/workforce/gwt/core/public/icons/error-close.png")
        ImageResource errorClose();

    }*/

    private InfoOld(int delayMsec, int fadeMsec, int startOpacity) {
        Bundle css = GWT.create(Bundle.class);
        css.infoCss().ensureInjected();
        this.fadeMsec = fadeMsec;
        this.startOpacity = startOpacity;
        this.delayMsec = delayMsec;
    }

    public static void show(String captionText, String messageText, Type type, Position position, Widget seeDetails, int delay) {
        if (instance == null) {
            instance = new InfoOld(delayMsec, fadeMsec, startOpacity);
        }
        setDelayMsec(delay > 0 ? delay : delayMsec);
        instance.setCaption(captionText);
        instance.setMessage(messageText);
        instance.setType(type);
        instance.setOpacity(90);
        instance.sinkEvents(Event.ONCLICK);
        instance.getElement().getStyle().setZIndex(instance.Z_INDEX_BASE);
        HTMLPanel htmlPanel = new HTMLPanel(instance.caption.getHTML() + instance.message.getHTML());
        if (seeDetails != null) htmlPanel.add(seeDetails);
        instance.setWidget(htmlPanel);
        instance.setPositionAndShow(position);
    }

    public static InfoOld get() {
        if (instance == null) {
            return new InfoOld();
        }
        return instance;
    }

    public static void show(String captionText, String message, Type type) {
        show(captionText, message, type, Position.CENTERED, null, 0);
    }

    public static void show(String captionText, String message, Position position) {
        show(captionText, message, Type.INFO, position, null, 0);
    }

    public static void show(String captionText, String message, Position position, Widget seeDetails, int delay) {
        show(captionText, message, Type.INFO, position, seeDetails, delay);
    }

    public static void show(String captionText, String message) {
        show(captionText, message, Type.INFO, Position.CENTERED, null, 0);
    }

    public static void warn(String captionText, String message) {
        show(captionText, message, Type.WARNING, Position.CENTERED, null, 0);
    }

    public void setType(Type type) {
        String STYLENAME = "notification";
        setStyleName(STYLENAME);
        switch (type) {
            case WARNING_AutoHide:
                removeStyleName("error notification-err");
                addStyleName("warning notification-warn");
                startDelay();
                break;
            case WARNING:
                removeStyleName("error notification-err");
                addStyleName("warning notification-warn");
                break;
            case INFO_AutoHide:
                break;
            case FEATURES:
                removeStyleName(STYLENAME);
                addStyleName("notification-feature");
                break;
            default:
                startDelay();
                break;
        }
    }

    public void setMessage(String messageText) {
        if (message == null) {
            message = new HTML("<p></p><p>" + messageText + "</p>");
        } else {
            message.setHTML("<p></p><p>" + messageText + "</p>");
        }
    }

    public void setCaption(String captionText) {
        if (caption == null) {
            caption = new HTML("<h1>" + captionText + "</h1>");
        } else {
            caption.setHTML("<h1>" + captionText + "</h1>");
        }

    }


    @Override
    public void hide() {
        cancelDelay();
        cancelFade();
        super.hide();
    }


    private void startDelay() {
        if (delayMsec > 0) {
            if (delay == null) {
                delay = new Timer() {
                    @Override
                    public void run() {
                        fade();
                    }
                };
                delay.schedule(delayMsec);
            }
        } else if (delayMsec == 0) {
            fade();
        }
    }


    void setPositionAndShow(Position position) {
        final Element el = getElement();
        el.getStyle().setProperty("top", "");
        el.getStyle().setProperty("left", "");
        el.getStyle().setProperty("bottom", "");
        el.getStyle().setProperty("right", "");
        switch (position) {
            case TOP_LEFT:
                el.getStyle().setProperty("top", "0px");
                el.getStyle().setProperty("left", "0px");
                break;
            case TOP_RIGHT:
                el.getStyle().setProperty("top", "0px");
                el.getStyle().setProperty("right", "0px");
                break;
            case BOTTOM_RIGHT:
                el.getStyle().setProperty("position", "absolute");
                if (Utils.isOpera()) {
                    // tray notification on opera needs explicitly defined size
                    el.getStyle().setProperty("width", getOffsetWidth() + "px");
                    el.getStyle().setProperty("height", getOffsetHeight() + "px");
                }
                el.getStyle().setProperty("bottom", "0px");
                el.getStyle().setProperty("right", "0px");
                break;
            case BOTTOM_LEFT:
                el.getStyle().setProperty("bottom", "0px");
                el.getStyle().setProperty("left", "0px");
                break;
            case CENTERED_TOP:
                center();
                el.getStyle().setProperty("top", "0px");
                break;
            case CENTERED_BOTTOM:
                center();
                el.getStyle().setProperty("top", "");
                el.getStyle().setProperty("bottom", "0px");
                break;
            case CUSTOM:
                break;
            default:
            case CENTERED:
                center();

                if (Utils.isArabicLanguage()) {
                    int left = (Window.getClientWidth() - getOffsetWidth()) >> 1;
                    int top = (Window.getClientHeight() - getOffsetHeight()) >> 1;
                    el.getStyle().setProperty("top", top + "px");
                    el.getStyle().setProperty("left", left + "px");
                }
                break;
        }
    }


    public void fade() {
        cancelDelay();
        if (fader == null) {
            fader = new Timer() {
                private final long start = new Date().getTime();

                @Override
                public void run() {
                    /*
                    * To make animation smooth, don't count that event happens
                    * on time. Reduce opacity according to the actual time
                    * spent instead of fixed decrement.
                    */
                    long now = new Date().getTime();
                    long timeEplaced = now - start;
                    float remainingFraction = 1 - timeEplaced / (float) fadeMsec;
                    int opacity = (int) (startOpacity * remainingFraction);
                    if (opacity <= 0) {
                        cancel();
                        hide();
                        if (Utils.isOpera()) {
                            // tray notification on opera needs to explicitly
                            // define
                            // size, reset it
                            getElement().getStyle().setProperty("width", "");
                            getElement().getStyle().setProperty("height", "");
                        }
                    } else {
                        setOpacity(getElement(), opacity);
                    }
                }
            };
            fader.scheduleRepeating(50);
        }
    }


    private void cancelFade() {
        if (fader != null) {
            fader.cancel();
            fader = null;
        }
    }

    private void cancelDelay() {
        if (delay != null) {
            delay.cancel();
            delay = null;
        }
    }

    public void setOpacity(int opacity) {
        setOpacity(getElement(), opacity);
    }

    private void setOpacity(Element el, int opacity) {
        el.getStyle().setProperty("opacity", "" + (opacity / 100.0));
        if (Utils.isIE()) {
            el.getStyle().setProperty("filter", "Alpha(opacity=" + opacity + ")");
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (fader == null) {
            fade();
        }
    }

    public enum Position {
        CENTERED, CENTERED_TOP, CENTERED_BOTTOM,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CUSTOM
    }

    public enum Type {
        /**
         * The Humanized notification is an implementation of
         * the transparent message-pattern, and can be used to
         * indicate non-critical events while interrupting
         * the user as little as possible.
         */
        INFO,
        INFO_AutoHide,
        /**
         * The Warning notification is an implementation of
         * the transparent message-pattern, and is meant
         * to interrupt the user as little as possible,
         * while still drawing the needed attention.
         */
        WARNING,
        WARNING_AutoHide,

        /*
         *The Feature notification is an implementation of the
         *transparent message-pattern, and can be used to show new
         *system features, once per user
         */
        FEATURES
    }

    private static void setDelayMsec(int delayMsec) {
        InfoOld.delayMsec = delayMsec;
    }

}
