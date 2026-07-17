/*
package com.finnetlimited.reportservice.core.client.ui.loading;

import com.finnetlimited.reportservice.core.client.bundle.LoadingBundle;
import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

*/
/**
 * User: ${Dilsh0d}
 * Date: 29-Mar-2010
 * Time: 21:55:53
 *//*

public class DRSLoading extends AbsolutePanel implements ResizeHandler {

    private static final String id = IdType.LOADING.getName();

    private final Label loadingLabel = new Label();
    private final HTMLPanel loading = new HTMLPanel("");
    private final Image loadingImg = new Image(LoadingBundle.instance.bigSnakeLoading());

    public DRSLoading() {
        this.setStyleName("drs-loading");
        add(loading, Window.getClientWidth() / 2, Window.getClientHeight() / 2);
        Window.addResizeHandler(this);
        init();
    }

    private void init() {
        loading.setStyleName("drs-loading-panel");
        DOM.setElementAttribute(loading.getElement(), "id", id);
        loading.add(loadingImg, id);
        loading.add(loadingLabel, id);
    }

    public void loading(String text) {
        if (text == null || "".equals(text.trim()))
            loadingLabel.setText("Loading ... ");
        else
            loadingLabel.setText(text);

        RootPanel.getBodyElement().appendChild(getElement());
//        this.setWidgetPosition(loading, (Window.getClientWidth()-loading.getOffsetWidth()) / 2, (Window.getClientHeight()-loading.getOffsetHeight()) / 2);
    }

    public void hideLoading() {
        RootPanel.getBodyElement().removeChild(this.getElement());
    }

    public void onResize(ResizeEvent resizeEvent) {
        this.setWidgetPosition(loading, (Window.getClientWidth() - loading.getOffsetWidth()) / 2, (Window.getClientHeight() - loading.getOffsetHeight()) / 2);
    }
}
*/
