package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.04.2009
 * Time: 12:35:25
 * To change this template use File | Settings | File Templates.
 */
public class DefaultNoItemsMessage extends NoItemsMessage {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private String text;
    private String textBeforeLink;
    private String href;
    private ClickHandler hrefClickHandler;
    private HorizontalPanel horPan;

    public DefaultNoItemsMessage() {
    }

    public DefaultNoItemsMessage(String text) {
        this.text = text;
    }

    public VerticalPanel getWholeMessage() {
        VerticalPanel vp = new VerticalPanel();
        vp.add(new HTML(text));
        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("auto");
        hp.add(new Label(textBeforeLink));
        if (href != null || hrefClickHandler != null) {
            SimpleLink simpleLink = href != null ? new SimpleLink(" " + wfmStrings.here(), href, hp.getTitle(), hp.getTitle()) : new SimpleLink(" " + wfmStrings.here(), hp.getTitle(), hp.getTitle());
            simpleLink.ensureDebugId("empty_click_here_link");
            if (hrefClickHandler != null) {
                simpleLink.addClickHandler(hrefClickHandler);
            }
            simpleLink.setStyleName("addLinkStyle");
            hp.add(simpleLink);
        }
        if (horPan != null) {
            hp = horPan;
        }
        vp.add(hp);
        return vp;
    }

    public static DockPanel getNoItemsMessage(String noItemText, String noItemBeforeLinkText, final String url) {
        DockPanel dockPanel = new DockPanel();
        dockPanel.setSize("100%", "100%");
        dockPanel.setStyleName("leaveReqCenter");
        dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
        dockPanel.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        VerticalPanel panel = new VerticalPanel();
        HTML html = new HTML(noItemText);
        html.addStyleName("center DefaultNoitemsMessage");
        panel.add(html);
        if (noItemBeforeLinkText != null) {
            HorizontalPanel horz = new HorizontalPanel();
            horz.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            horz.add(new HTML(noItemBeforeLinkText + "&nbsp;"));
            if (url != null) {
                SimpleLink link = new SimpleLink("here");
                link.addClickHandler(event -> Utils.redirect(url));
                horz.add(link);
            }
            panel.add(horz);
        }
        dockPanel.add(panel, DockPanel.CENTER);
        return dockPanel;
    }

    public MaterialPanel getDivPanelMessage() {
        MaterialPanel vp = new MaterialPanel();
        vp.addStyleName("kpiGrid-emptyTable");
        vp.add(new HTML(text));
        MaterialPanel hp = new MaterialPanel();
        hp.setWidth("auto");
        hp.add(new Span(textBeforeLink));
        if (href != null || hrefClickHandler != null) {
            Span span = new Span();
            span.ensureDebugId("empty_click_here_link");
            span.setStyleName("addLinkStyle");

            MaterialLink link = href != null ? new MaterialLink(wfmStrings.here(), href) : new MaterialLink(wfmStrings.here());
            if (hrefClickHandler != null) {
                link.addClickHandler(hrefClickHandler);
            }
            span.add(link);
            hp.add(span);
        }
        vp.add(hp);
        return vp;
    }

    public void setTextBeforeLink(String textBeforeLink) {
        this.textBeforeLink = textBeforeLink;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setHref(ClickHandler href) {
        this.hrefClickHandler = href;
    }

    public void setPanel(HorizontalPanel horPan) {
        this.horPan = horPan;
    }
}