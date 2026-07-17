package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SecuritryType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * User: ${Dilsh0d}
 * Date: 16-Apr-2010
 * Time: 19:10:43
 */
public class ExportFormPanel extends FormPanel {

    private String name;
    private Hidden param;
    private ActionButton link;
    private String icon;

    public ExportFormPanel(String name, String icon, String uri) {
        this.name = name;
        this.icon = icon;
        setMethod(FormPanel.METHOD_POST);
        setAction(GWT.getHostPageBaseURL() + uri);
        init();
    }

    private void init() {
        param = new Hidden();
        param.setName(SecuritryType.ReportXmlString.name());
        link = new ActionButton(name, icon, ActionButton.Type.BUTTON);
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
