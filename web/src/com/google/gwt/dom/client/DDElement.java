package com.google.gwt.dom.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;

/**
 * User: Ilhombek
 * Date: 3/14/12
 * Time: 4:43 PM
 */
public class DDElement extends UIObject {

	public DDElement() {
		Element dd = DOM.createElement("dd");
		setElement(dd);
	}

	public void setInnerHTML(String html) {
		getElement().setInnerHTML(html);
	}
}
