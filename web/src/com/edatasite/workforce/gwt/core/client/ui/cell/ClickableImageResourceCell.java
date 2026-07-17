package com.edatasite.workforce.gwt.core.client.ui.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 2/13/12
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * An {@link AbstractCell} used to render an {@link ImageResource}
 */

public class ClickableImageResourceCell extends AbstractCell<ImageResource> {

	private static ImageResourceRenderer renderer;
	private String appendClass;

	/**
	 * Constructor a new ClickableImageResourceCell
	 */
	public ClickableImageResourceCell() {
		super("click");
		if (renderer == null) {
			renderer = new ImageResourceRenderer();
		}
	}

	@Override
	public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.appendHtmlConstant("<span" + (appendClass != null ? " class=" + appendClass : "") + ">");
			sb.append(renderer.render(value));
			sb.appendHtmlConstant("</span>");
		}
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, ImageResource value, NativeEvent event, ValueUpdater<ImageResource> imageResourceValueUpdater) {
		super.onBrowserEvent(context, parent, value, event, imageResourceValueUpdater);
		if ("click".equals(event.getType())) {
			EventTarget eventTarget = event.getEventTarget();
			if (!Element.is(eventTarget)) {
				return;
			}
			if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
				// Ignore clicks that occur outside of the main element.
				onEnterKeyDown(context, parent, value, event, imageResourceValueUpdater);
			}
		}
	}

	@Override
	protected void onEnterKeyDown(Context context, Element parent, ImageResource value, NativeEvent event, ValueUpdater<ImageResource> imageResourceValueUpdater) {
		if (imageResourceValueUpdater != null) {
			imageResourceValueUpdater.update(value);
		}
	}

	public void setAppendClass(String appendClass) {
		this.appendClass = appendClass;
	}
}
