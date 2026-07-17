package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Virus on 9/6/14.
 */
public class HtmlThead extends ComplexPanel {
	public HtmlThead() {
		setElement(DOM.createTHead());
	}

	public void add(Widget widget) {
		add(widget, getElement());
	}
}
