package com.edatasite.workforce.gwt.core.client.ui.components.tooltip;


import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.documents.client.gwtupload.UUID;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Position;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jquery.client.api.JQueryElement;

import static com.edatasite.workforce.gwt.core.client.ui.components.tooltip.JsTooltipster.$;

public class KpiToolTip {
    private ToolTipOptions tOptions;
    private final JsTooltipsterOptions options;
    private final Widget widget;
    private String id;

    public KpiToolTip(Widget widget, String content) {
        this(widget, content, Position.TOP);
    }

    public KpiToolTip(Widget widget, String content, Position position) {
        this(widget, JQuery.$("<span>" + content + "</span>"), position);
    }

    public KpiToolTip(Widget widget, Widget content) {
        this(widget, content, null);
    }

    public KpiToolTip(Widget widget, String content, ToolTipOptions toptions) {
        this(widget, JQuery.$("<span>" + content + "</span>"), toptions);
    }

    private KpiToolTip(Widget widget, JQueryElement content, ToolTipOptions toptions) {
        this.widget = widget;
        tOptions = toptions;
        options = convertObjectToOptions(tOptions);
        options.content = content;
        load();
    }

    private KpiToolTip(Widget widget, JQueryElement content, Position position) {
        this.widget = widget;
        options = getDefaultOptions();
        options.content = content;
        load();
    }

    public KpiToolTip(Widget widget, Widget content, ToolTipOptions toptions) {
        this.widget = widget;
        tOptions = toptions;
        options = convertObjectToOptions(tOptions);
        initWidgetContent(content);
        load();
    }


    private JsTooltipsterOptions convertObjectToOptions(ToolTipOptions toptions) {
        JsTooltipsterOptions result = getDefaultOptions();
        if (toptions == null) {
            return result;
        }
        result.contentAsHTML = toptions.getContentAsHTML();
        result.interactive = toptions.getInteractive();
        if (toptions.getMinWidth() != null) {
            result.minWidth = toptions.getMinWidth();
        }
        if (toptions.getMaxWidth() != null) {
            result.maxWidth = toptions.getMaxWidth();
        }
        if (toptions.getSide() != null) {
            result.side = toptions.getSide().getCssName();
        }
        if (toptions.getOpenDelay() != null && toptions.getCloseDelay() != null) {
            result.delay = new int[]{toptions.getOpenDelay(), toptions.getCloseDelay()};
        }
        if (toptions.getTouchOpenDelay() != null && toptions.getTouchCloseDelay() != null) {
            result.delayTouch = new int[]{toptions.getTouchOpenDelay(), toptions.getTouchCloseDelay()};
        }
        if (toptions.getTrigger() != null) {
            result.trigger = toptions.getTrigger().name();
        }
        return result;
    }

    private void initWidgetContent(Widget content) {
        String contentId = content.getElement().getAttribute("id");
        if (contentId != null && !"".equals(contentId)) {
            this.id = contentId;
        } else {
            this.id = UUID.uuid();
        }
        widget.getElement().setAttribute("data-tooltip-content", "#" + id);
        content.getElement().setAttribute("id", id);
        MainLayout.get().getTooltips().add(content);
        JQuery.$(widget).on("delete", new Functions.EventFunc() {
            @Override
            public Object call(Event e) {
                content.removeFromParent();
                return null;
            }
        });
    }

    public void setSide(String side) {
        options.side = side;
        load();
    }

    public void setContent(String html) {
        if (html != null) {
            options.content = JQuery.$(html);
            load();
        }
    }

    private JsTooltipsterOptions getDefaultOptions() {
        JsTooltipsterOptions result = new JsTooltipsterOptions();
        result.contentAsHTML = true;
        result.contentCloning = false;
        result.interactive = true;
        result.delay = new int[]{0, 300};
        result.delayTouch = new int[]{0, 500};
        return result;
    }

    private void load() {
        $(widget.getElement()).tooltipster(options);
    }

    public void setMinWidth(int minWidth) {
        options.minWidth = minWidth;
        load();
    }

    public void setMaxWidth(int maxWidth) {
        options.maxWidth = maxWidth;
        load();
    }

    public void setConentAsHTML(boolean contentAsHTML) {
        options.contentAsHTML = contentAsHTML;
        load();
    }

    public void setInteractive(boolean interactive) {
        options.interactive = interactive;
        load();
    }

}
