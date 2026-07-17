/*
package com.finnetlimited.reportservice.core.client.ui.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Jul 12, 2011
 * Time: 4:29:45 PM
 * To change this template use File | Settings | File Templates.
 *//*

public class DashboardChartPrintPdfPanel extends FormPanel {

    private String name;
    private Hidden param;
    private HTML link;
    private ImageResource icon;

    public DashboardChartPrintPdfPanel(ImageResource icon, String name, String uri) {
        this.name = name;
        this.icon = icon;
        setMethod(FormPanel.METHOD_POST);
        setAction(GWT.getHostPageBaseURL() + uri);
        init();
    }

    private void init() {
        param = new Hidden();
        param.setName("dashboarId");
        link = new HTML(name + "<img style='padding-left:5px;vertical-align: bottom;' src='" + icon.getURL() + "'/>");
        link.setStyleName("drs-export");
        link.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                clickHandle.clickEvent();
            }
        });

        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(link);
        hPanel.add(param);
        add(hPanel);
    }

    public void setParam(String value) {
        param.setValue(value);
    }

    public void submit() {
        super.submit();
    }

    public interface ExportClickEvent {
        void clickEvent();
    }

    private ExportClickEvent clickHandle;

    public void addClickEvent(ExportClickEvent clickHandle) {
        this.clickHandle = clickHandle;
    }

}
*/
