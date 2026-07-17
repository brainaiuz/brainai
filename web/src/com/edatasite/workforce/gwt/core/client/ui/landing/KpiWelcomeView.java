package com.edatasite.workforce.gwt.core.client.ui.landing;

import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * User: Dilshod Madrahimov
 * Date: 3/27/13
 * Time: 7:42 PM
 */
public class KpiWelcomeView extends Composite {

    private HTMLPanel htmlPanel = new HTMLPanel("");

    public KpiWelcomeView(String style) {
        htmlPanel.setStyleName(style);
        htmlPanel.addStyleName("guideCover");
        initWidget(htmlPanel);
    }

    public void addSimpleLink(String linkName, final String url, String linkStyle,final boolean hasAccess) {
        HTMLPanel panel = new HTMLPanel("");
        panel.setStyleName("prodLink");
        panel.addStyleName(linkStyle);
        Anchor link = new Anchor(linkName);
        link.addClickHandler(clickEvent -> {
            if (hasAccess) {
                SinksContainerFactory.entryPoint.onHistoryChanged(url);
            }
        });
        panel.add(link);
        htmlPanel.add(panel);
    }

    public void addSimpleLinkWithHTMLTags(String linkName, final String url, String linkStyle,final boolean hasAccess) {
        HTMLPanel panel = new HTMLPanel("");
        panel.setStyleName("prodLink");
        panel.addStyleName(linkStyle);
        Anchor link = new Anchor(linkName , true);
        link.addClickHandler(clickEvent -> {
            if (hasAccess) {
                SinksContainerFactory.entryPoint.onHistoryChanged(url);
            }
        });
        panel.add(link);
        htmlPanel.add(panel);
    }
    public void addSimpleHTMLLink(String linkName, final String url, String linkStyle,final boolean hasAccess) {
        HTMLPanel panel = new HTMLPanel("");
        panel.setStyleName("prodLink");
        panel.addStyleName(linkStyle);
        Anchor link = new Anchor(linkName);
        link.addClickHandler(clickEvent -> {
            if (hasAccess) {
                String action = GWT.getHostPageBaseURL() + url;
                Window.open(action, "_blank", "");
            }
        });
        panel.add(link);
        htmlPanel.add(panel);
    }
    public void addSimpleLink(String linkName, String linkStyle, ClickHandler handler) {
        Anchor link = new Anchor(linkName);
        link.setStyleName(linkStyle);
        link.addStyleName("prodLink");
        link.addClickHandler(handler);
        htmlPanel.add(link);
    }

    public void addNoLink(String linkName, String linkStyle) {
        HTML link = new HTML(linkName);
        link.setStyleName(linkStyle);
        link.addStyleName("noProdLink");
        htmlPanel.add(link);
    }



}
