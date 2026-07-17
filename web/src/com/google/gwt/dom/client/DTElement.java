package com.google.gwt.dom.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;

/**
 * User: Ilhombek
 * Date: 3/14/12
 * Time: 4:43 PM
 */
public class DTElement extends UIObject {

	public DTElement() {
		Element dd = DOM.createElement("dt");
		setElement(dd);
	}

	public void setInnerHTML(String html) {
		getElement().setInnerHTML(html);
	}
}
