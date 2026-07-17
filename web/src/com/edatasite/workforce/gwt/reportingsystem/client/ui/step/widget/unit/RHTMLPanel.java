package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Virus on 8/28/14.
 */
public class RHTMLPanel extends ComplexPanel {
    private FilterCommand refreshCommand;

    public RHTMLPanel() {
        setElement(DOM.createTBody());
    }

    public void add(Widget widget) {
        add(widget, getElement());
    }

    public void add(Element element) {
        getElement().appendChild(element);
    }

    public void refresh(boolean value) {
        refreshCommand.execute(value);
    }

    public void setRefreshCommand(FilterCommand refreshCommand) {
        this.refreshCommand = refreshCommand;
    }

    public interface FilterCommand {
        void execute(boolean force);
    }

}
