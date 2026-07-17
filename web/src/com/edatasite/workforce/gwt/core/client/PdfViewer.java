package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.NamedFrame;

public class PdfViewer {

    private static final String PDF_PRINT_FRAME = "pdfPrintFrame";

    public static ComplexPanel ensurePrintFrame(ComplexPanel host) {
        NamedFrame printFrame = new NamedFrame(PDF_PRINT_FRAME);
        printFrame.getElement().setId(PDF_PRINT_FRAME);
        printFrame.getElement().getStyle().setProperty("display", "none");
        host.add(printFrame);

        printFrame.addLoadHandler(event -> {
            LoadingPanel.loading(false);
            doPrintFrameByElement(PDF_PRINT_FRAME);
        });
        return host;
    }

    private static native void doPrintFrameByElement(String frameId) /*-{
        try {
            var el = $doc.getElementById(frameId);
            if (!el) return;
            var w = el.contentWindow;
            if (w && w.print) {
                w.focus();
                w.print();
            }
        } catch (e) {
            GWT.log(e);
        }
    }-*/;
}