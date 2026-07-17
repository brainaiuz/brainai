package com.edatasite.workforce.gwt.core.client.ui.suggestDropdown;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class DropdownButton extends Image {

    private static DropdownBundle dropdownBundle = (DropdownBundle) GWT.create(DropdownBundle.class);

    private boolean active = false; //button clicked or not. By dafault - not.
    private PopupListener popupListener; //we call method from PopupListener interface to open and close pop up.

    private Image original; //how button looks without any events
    private Image mouseOver; //mouse over style.
    private Image opened; //clicked button style.

    /**
     * This widget incapsulate logic for button in dropdown widget.
     */
    public DropdownButton() {
        super();

        original = AbstractImagePrototype.create(dropdownBundle.original()).createImage();
        mouseOver = AbstractImagePrototype.create(dropdownBundle.mouseOver()).createImage();
        opened = AbstractImagePrototype.create(dropdownBundle.opened()).createImage();

        //we set by dfault image
        changeImage(original);
        DOM.setStyleAttribute(getStyleElement(), "cursor", "pointer");
    }

    /**
     * You can fire click logic by calling this function.
     */
    public void fireClickEvent() {
        clickEvent(null);
    }

    /**
     * You can fire click logic by calling this function.
     */
    public void fireClickEvent(Widget sender) {
        clickEvent(sender);
    }

    /**
     * Listener to follow buttons events.
     * We can know when to show and close popup.
     *
     * @param popupListener PopupListener interface implementation.
     */
    public void addPopupListener(PopupListener popupListener) {
        this.popupListener = popupListener;
    }

    /**
     * We use this method to follow events outside the button.
     * If user clicks outside of the button we should show him "original" image.(not "on_click" or "on_mouse_over" button style)
     *
     * @param event
     * @return
     */
    public boolean eventTargetButton(Event event) {
        Element target = DOM.eventGetTarget(event);
        boolean targetsButton = DOM.isOrHasChild(getElement(), target);

        /*
       If target outside of the button then change image on default.
       Else events handles standart listeners.
        */
        if (!targetsButton) {
            changeImage(original);
            active = false;
        } else {
            active = true;
            clickEvent(null);
        }

        return targetsButton;
    }

    /**
     * All button behaviour here.
     * OnClick and OnMouseOver handler.
     * Changes button style depending on event.
     * Now it's only changing image by changeImage() function and calls functions from PopupListener interface.
     * On mouse enter and mouse leave image is changing.
     * On click popup showing or hiding.
     */
    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEOVER: {
                if (!active) {
                    changeImage(mouseOver);
                }
                break;
            }
            case Event.ONMOUSEOUT: {
                if (!active) {
                    changeImage(original);
                }
                break;
            }
            case Event.ONCLICK: {
                clickEvent(null);
                break;
            }
        }
    }

    /**
     * Function that incapsulates on click logic.
     */
    private void clickEvent(Widget sender) {
        if (!active) {
            changeImage(opened);
            showPopup();
            active = true;
        } else {
            inactive(sender);
            hidePopup();
            active = false;
        }
    }

    /**
     * If clicked outside button then - "original" style.
     * Else - "mouseOver" style.
     * If sender widget not defined - then "mouseOver" style too.
     * (For compatibility with previous version)
     *
     * @param sender
     */
    private void inactive(Widget sender) {
        if (sender == null || DOM.isOrHasChild(sender.getElement(), this.getElement())) {
            changeImage(mouseOver);
        } else {
            changeImage(original);
        }
    }

    /**
     * Simply changes button image.
     * Image from DropdownBundle interface.
     * We set to main Image object different parametrs.
     *
     * @param image image we want to show.
     */
    private void changeImage(Image image) {
        String url = image.getUrl();
        int left = image.getOriginLeft();
        int top = image.getOriginTop();
        int width = image.getWidth();
        int height = image.getHeight();
        setUrlAndVisibleRect(url, left, top, width, height);
    }

    /**
     * Method calls hidePopup() function of PopupListener interface.
     */
    private void hidePopup() {
        if (popupListener != null) {
            popupListener.hidePopup();
        }
    }

    /**
     * Method calls showPopup() function of PopupListener interface.
     */
    private void showPopup() {
        if (popupListener != null) {
            popupListener.showPopup();
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
