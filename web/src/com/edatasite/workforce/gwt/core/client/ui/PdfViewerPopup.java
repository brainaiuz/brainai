package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * User: Faxriddin Talimov
 * Date: 20.11.19
 */

public class PdfViewerPopup extends KpiModal {
    boolean needStyle = true;

    public PdfViewerPopup(String name, Integer id, String imageUrl, boolean isShowNewPage) {
        super(false);
        setDismissible(true);
        setTitle(name);

        if (imageUrl.contains("/common/downloadFile")) {
            needStyle = false;
        }
        CommonService.App.get().getDynamicImageUrl(id, new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                onInitialize(imageUrl);
            }

            @Override
            public void onSuccess(String result) {
                if (isShowNewPage) {
                    Window.open(result, "_blank", "");
                } else {
                    onInitialize(result);
                }
            }
        });
    }

    private void onInitialize(final String url) {

        addOpenHandler(event -> {
            RootPanel.get().addStyleName("has-guide-modal");
        });
        addCloseHandler(closeEvent -> {
            RootPanel.get().removeStyleName("has-guide-modal");
        });
        Frame iFrame = new Frame(url);
        if (needStyle) {
            iFrame.getElement().getStyle().setWidth(800, Style.Unit.PX);
            iFrame.getElement().getStyle().setHeight(535, Style.Unit.PX);
        }
        add(iFrame);
    }
}
