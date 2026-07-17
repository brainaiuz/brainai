package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Virus on 8/28/14.
 */
public class HtmlCheckBox extends ComplexPanel {
	public HtmlCheckBox() {
		setElement(DOM.createInputCheck());
	}

	public void add(Widget widget) {
		add(widget, getElement());
	}

	public void setChecked(boolean check) {
		((InputElement) getElement().cast()).setChecked(check);
	}

	public void setEventListener(EventListener eventListener) {
		DOM.sinkEvents(getElement(), Event.ONCHANGE);
		DOM.setEventListener(getElement(), eventListener);
	}

	public void setEnable(boolean b) {
		if (b) {
			DOM.removeElementAttribute(getElement(), "disabled");
		} else {
			DOM.setElementAttribute(getElement(), "disabled", "");
		}
	}

	public boolean isChecked() {
		return ((InputElement) getElement().cast()).isChecked();
	}
}
