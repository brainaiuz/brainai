package com.edatasite.workforce.gwt.core.client.ui.wfmButton;

import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.TimerImpl;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.Image;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 30.10.2008
 * Time: 12:03:04
 * To change this template use File | Settings | File Templates.
 */
public class WfmButton extends Image {

    private Image original; //how button looks without any events
    private Image mouseOver; //how button looks when user clicks on it.
    private boolean disabled = false; //true if button disabeld

    private boolean delay = false; //by default delay feature is disabled.

    private ClickListenerCollection clickListeners; //clicklistener collection. Used to fire click events.
    private String customValue;


    /**
     * We use to create button with image by ImageBundeling mechanizm.
     *
     * @param original  usual image
     * @param mouseOver image showing on mouse down event.
     */
    public WfmButton(AbstractImagePrototype original, AbstractImagePrototype mouseOver) {

        super();

        this.original = original.createImage();
        this.mouseOver = mouseOver.createImage();

        changeImage(this.original);

        DOM.setStyleAttribute(getStyleElement(), "cursor", "pointer");
    }


    /**
     * Add your own implementation of ClickListener.
     * Event will be fired on browsers ONCLICK event.
     *
     * @param listener
     */
    public void addClickListener(ClickListener listener) {
        if (clickListeners == null) {
            clickListeners = new ClickListenerCollection();
        }
        clickListeners.add(listener);
    }


    /**
     * You can switch on delay feature.
     * If true user will wait 2 sec. untill he could click button one more time.
     *
     * @param delay
     */
    public void setDelay(boolean delay) {

        this.delay = delay;
    }


    /**
     * All button behaviour here. Now it's only changing image by changeImage() function.
     * On mouse down and mouse up image is changing.
     */
    public void onBrowserEvent(Event event) {

        super.onBrowserEvent(event);

        //if buttin enabled then handle events.
        if (!disabled) {
            switch (DOM.eventGetType(event)) {
                case Event.ONMOUSEDOWN: {
                    changeImage(mouseOver);
                    break;
                }
                case Event.ONMOUSEUP: {
                    changeImage(original);
                    break;
                }
                case Event.ONCLICK: {

                    //delay for preventing user make several click without a break.
                    if (delay) {
                        delay(2000);
                        disabled = true;
                    }

                    if (clickListeners != null) {
                        clickListeners.fireClick(this);
                    }

                    break;
                }
            }
        }

    }


    /**
     * We use this to disable button for a few moments.
     *
     * @param delay
     */
    private void delay(int delay) {

        TimerImpl timer = new TimerImpl();
        timer.schedule(delay);
        timer.addTimerListener(() -> disabled = false);
    }


    /**
     * Image changing function.
     * It takes parameters of given images and sets to main Image object.
     *
     * @param image
     */
    private void changeImage(Image image) {

        String url = image.getUrl();

        int left = image.getOriginLeft();
        int top = image.getOriginTop();
        int width = image.getWidth();
        int height = image.getHeight();

        setUrlAndVisibleRect(url, left, top, width, height);
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    public String getCustomValue() {
        return customValue;
    }
}
