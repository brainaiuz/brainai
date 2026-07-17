package com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.command;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 19-Oct-2010
 * Time: 20:51:17
 */
public class CommandIcon extends Composite implements HasAllMouseHandlers, HasClickHandlers {
    private final Span image;

    /**
     * Creates a new <code>CommandIcon</code>.
     *
     * @param image
     * @param command
     */
    public CommandIcon(final Span image, final Command command) {
        this.image = image;
        initWidget(image);

        image.addClickHandler(new CommandClickHandler(command));

        //setStylePrimaryName("simpleGwt-CommandIcon");
    }

    /**
     * @see com.google.gwt.event.dom.client.HasMouseDownHandlers#addMouseDownHandler(com.google.gwt.event.dom.client.MouseDownHandler)
     */
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return image.addMouseDownHandler(handler);
    }

    /**
     * @see com.google.gwt.event.dom.client.HasMouseUpHandlers#addMouseUpHandler(com.google.gwt.event.dom.client.MouseUpHandler)
     */
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return image.addMouseUpHandler(handler);
    }

    /**
     * @see com.google.gwt.event.dom.client.HasMouseOutHandlers#addMouseOutHandler(com.google.gwt.event.dom.client.MouseOutHandler)
     */
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return image.addMouseOutHandler(handler);
    }

    /**
     * @see com.google.gwt.event.dom.client.HasMouseOverHandlers#addMouseOverHandler(com.google.gwt.event.dom.client.MouseOverHandler)
     */
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return image.addMouseOverHandler(handler);
    }

    /**
     * @see com.google.gwt.event.dom.client.HasMouseMoveHandlers#addMouseMoveHandler(com.google.gwt.event.dom.client.MouseMoveHandler)
     */
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return image.addMouseMoveHandler(handler);
    }

    /**
     * @see com.google.gwt.event.dom.client.HasMouseWheelHandlers#addMouseWheelHandler(com.google.gwt.event.dom.client.MouseWheelHandler)
     */
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return image.addMouseWheelHandler(handler);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler clickHandler) {
        return addDomHandler(clickHandler, ClickEvent.getType());
    }
}
