package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.client.ui.html.Div;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 09.12.13
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */

public class ImageViewerPopup extends KpiModal {

	private Image image;

	public ImageViewerPopup(String name, String imageUrl) {
		super();
		setCloseButton(true);
		setTitle(name);
		onInitialize(imageUrl);
	}

	private void onInitialize(final String url) {

		image = new Image(url);
//		image.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
		image.getElement().getStyle().setProperty("height", "initial");
		image.getElement().getStyle().setProperty("width", "initial");
		image.getElement().getStyle().setProperty("maxWidth", "900px");
		image.getElement().getStyle().setProperty("maxHeight", "500px");
		image.addLoadHandler(event -> center());
		if (url != null && !"".equals(url.trim())){
            image.getElement().getStyle().setCursor(Style.Cursor.POINTER);
			image.addClickHandler(event -> Window.open(url, "_blank", ""));
		}

		Div div = new Div();
		div.add(image);
		div.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
		add(div);
	}
}
