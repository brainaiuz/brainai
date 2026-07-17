package com.edatasite.workforce.gwt.core.client;

import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.UIObject;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 7/15/12
 * Time: 6:58 AM
 * To change this template use File | Settings | File Templates.
 */

public class Printer {

    /**
     * If true, use a Timer instead of DeferredCommand to print the internal fram
     */
    public static boolean USE_TIMER = false;

    /**
     * Time in seconds to wait before printing the internal frame when using Timer
     */
    public static int TIMER_DELAY = 2;


    public static native void it() /*-{
        $wnd.print();
    }-*/;

    public static void it(UIObject obj) {
        it("", obj);
    }

    public static void it(Element element) {
        it("", element);
    }

    public static void it(String style, UIObject obj) {
        it(style, obj.getElement());
    }

    public static void it(String docType, String style, UIObject obj) {
        it(docType, style, obj.getElement());
    }

    public static void it(String style, Element element) {
        it("", style, element);
    }

    public static void it(String docType, String style, Element element) {
        updateFieldsDOM(element);
        it(docType, style, DOM.toString(element));
    }

    public static void it(String docType, String style, String it) {
        it(docType
           + "<html>\n"
           + "<head>\n"
           + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
           + "<meta http-equiv=\"Content-Style-Type\"	content=\"text/css\">\n"
           + "<style type=\"text/css\">\n"
           + style
           + "\n</style>"
           + "</head>\n<body>\n"
           + "<div id = \"__printingFrame\">\n"
           + it
           + "\n</div>\n"
           + "</body>\n"
           + "</html>");
    }

    public static void it(String html) {
        try {
            buildFrame(html);

            if (USE_TIMER) {
                Timer timer = new Timer() {
                    public void run() {
                        printFrame();
                    }
                };
                timer.schedule(TIMER_DELAY * 1000);
            } else {
                DeferredCommand.addCommand(() -> printFrame());
            }

        } catch (Throwable exc) {
            Window.alert(exc.getMessage());
        }
    }

    // Open html form that exists inside the DOM
    public static native void buildFrame(String html) /*-{
        var frame = $doc.getElementById('__printingFrame');
        if (!frame) {
            $wnd.alert("Error: Can't find printing frame.");
            return;
        }
        var doc = frame.contentWindow.document;
        doc.open();
        doc.write(html);
        doc.close();

    }-*/;

    public static native void printFrame() /*-{
        var frame = $doc.getElementById('__printingFrame');
        frame = frame.contentWindow;
        frame.focus();
        frame.print();
    }-*/;

    // Open html-form that does not exist in the DOM
    public static native void openPrintWindow(String contents) /*-{
        var printWindow = window.open("", "PrintWin");
        if (printWindow && printWindow.top) {
            printWindow.document.write(contents);
            setTimeout(function() {
                printWindow.print();
                printWindow.close();
            }, 300);
        } else {
            alert("The print feature works by opening a popup window, but our popup window was blocked by your browser.");
        }
    }-*/;

    // Great contribution from mgrushinskiy to print form element
    public static void updateFieldsDOM(Element dom) {
        NodeList<com.google.gwt.dom.client.Element> textareas = dom.getElementsByTagName("textarea");
        NodeList<com.google.gwt.dom.client.Element> inputs = dom.getElementsByTagName("input");
        NodeList<com.google.gwt.dom.client.Element> options = dom.getElementsByTagName("option");


        if (textareas != null) {
            for (int cii = 0; cii < textareas.getLength(); cii++) {
                updateDOM(TextAreaElement.as(textareas.getItem(cii)));
            }
        }
        if (inputs != null) {
            for (int cii = 0; cii < inputs.getLength(); cii++) {
                updateDOM(InputElement.as(inputs.getItem(cii)));
            }
        }
        if (options != null) {
            for (int cii = 0; cii < options.getLength(); cii++) {
                updateDOM(OptionElement.as(options.getItem(cii)));
            }
        }
    }

    public static void updateDOM(InputElement item) {
        try {
            item.setDefaultValue(item.getValue());
        } finally {
        }
        try {
            item.setDefaultChecked(item.isDefaultChecked());
        } finally {
        }
    }

    public static void updateDOM(TextAreaElement item) {
        item.setDefaultValue(item.getValue());
        item.setInnerText(item.getValue());
    }

    public static void updateDOM(OptionElement item) {
        item.setDefaultSelected(item.isSelected());
    }

}
