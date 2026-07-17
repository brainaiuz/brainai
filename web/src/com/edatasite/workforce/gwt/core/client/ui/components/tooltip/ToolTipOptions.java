package com.edatasite.workforce.gwt.core.client.ui.components.tooltip;

import gwt.material.design.client.constants.Position;

public class ToolTipOptions {
    private Boolean contentAsHTML = true;
    private Boolean interactive = true;
    private Integer maxWidth;
    private Integer minWidth;
    private Position side;
    private Integer openDelay;
    private Integer closeDelay;
    private Integer touchOpenDelay;
    private Integer touchCloseDelay;
    private Trigger trigger;

    public Boolean getContentAsHTML() {
        return contentAsHTML != null && contentAsHTML;
    }

    public void setContentAsHTML(Boolean contentAsHTML) {
        this.contentAsHTML = contentAsHTML;
    }

    public Boolean getInteractive() {
        return interactive != null && interactive;
    }

    public void setInteractive(Boolean interactive) {
        this.interactive = interactive;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public Position getSide() {
        return side;
    }

    public void setSide(Position side) {
        this.side = side;
    }

    public Integer getOpenDelay() {
        return openDelay;
    }

    public void setOpenDelay(Integer openDelay) {
        this.openDelay = openDelay;
    }

    public Integer getCloseDelay() {
        return closeDelay;
    }

    public void setCloseDelay(Integer closeDelay) {
        this.closeDelay = closeDelay;
    }

    public Integer getTouchOpenDelay() {
        return touchOpenDelay;
    }

    public void setTouchOpenDelay(Integer touchOpenDelay) {
        this.touchOpenDelay = touchOpenDelay;
    }

    public Integer getTouchCloseDelay() {
        return touchCloseDelay;
    }

    public void setTouchCloseDelay(Integer touchCloseDelay) {
        this.touchCloseDelay = touchCloseDelay;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

}
