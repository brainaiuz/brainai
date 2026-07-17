package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 10-Jun-2010
 * Time: 22:08:26
 */
public class WfmContentPanel extends HTMLPanel {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final String ID = "wfm-ContenPanel";
    private static final String CAPTION_ID = "wfm-Caption";
    private static int num = 0;

    private String id;
    private String captionId;
    private HTMLPanel caption;
    private ScrollPanel content;

    public WfmContentPanel() {
        super("");
        id = (ID + num);
        getElement().setAttribute("id", id);
        getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        getElement().getStyle().setBorderWidth(1d, Style.Unit.PX);
        getElement().getStyle().setBorderColor("#99BBE8");
        getElement().getStyle().setBackgroundColor("white");
        captionId = (CAPTION_ID + num++);
        caption = new HTMLPanel("");
        caption.getElement().setAttribute("id", captionId);
        caption.setStyleName("gwt-Caption");
        caption.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);

        content = new ScrollPanel();
        content.setWidth("100%");

        this.add(caption, id);
        this.add(content, id);
    }

    /**
     * set Html to caption left position
     *
     * @param html
     */
    public void setCaptionLeftHTML(String html) {
        HTML left = new HTML(html);
        caption.add(left, captionId);
    }

    /**
     * set Widget to caption left position
     *
     * @param widget
     */
    public void setCaptionLeftWidget(Widget widget) {
        caption.add(widget, captionId);
    }

    /**
     * set Widget to caption right position
     *
     * @param widget
     */
    public void setCaptionRightWidget(Widget widget) {
        widget.getElement().getStyle().setFloat(Style.Float.RIGHT);
        caption.add(widget, captionId);
    }

    /**
     * set Html to caption right position
     *
     * @param html
     */
    public void setCaptionRightHTML(String html) {
        HTML right = new HTML(html);
        right.getElement().getStyle().setFloat(Style.Float.RIGHT);
        caption.add(right, captionId);
    }

    @Override
    public void setPixelSize(int width, int height) {
        super.setPixelSize(width, height);
        content.setSize(width + "px", (height - 20) + "px");
    }

    @Override
    public void setSize(String width, String height) {
        super.setSize(width, height);
        setContentHeight(height);
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        setContentHeight(height);
    }

    /**
     * set Content height
     *
     * @param height
     */
    private void setContentHeight(String height) {
        String[] data = height.split("px");
        if (data.length == 0) {
            data = height.split("%");
            if (data.length != 0) {
                try {
                    int height1 = Integer.parseInt(data[0]);
                    content.setHeight((height1 - 20) + "px");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    int height1 = Integer.parseInt(height);
                    content.setHeight((height1 - 20) + "px");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                int height1 = Integer.parseInt(data[0]);
                content.setHeight((height1 - 20) + "px");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void add(Widget widget) {
        content.setWidget(widget);
    }
}
