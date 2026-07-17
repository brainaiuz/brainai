package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Virus on 8/28/14.
 */
public class HtmlColumn extends ComplexPanel {
	public HtmlColumn() {
		setElement(DOM.createTD());
	}

	public void add(Widget widget) {
		add(widget, getElement());
	}
	public TableCellElement get(){
		return (TableCellElement)getElement().cast();
	}
}
