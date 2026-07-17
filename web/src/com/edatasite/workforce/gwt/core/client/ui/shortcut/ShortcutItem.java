package com.edatasite.workforce.gwt.core.client.ui.shortcut;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by Sherali Pirnafasov.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 9/24/11
 * Time: 4:28 PM
 */
public class ShortcutItem {

    private String name;
    private String descriptioin;
    private boolean active = false;
    private Command cmd;
//    private EdsShortcuts parent;
    private String[] params;
    private int nonSavedFieldsCount;
    private boolean colapse;
    private Span statistics = new Span();
    private Integer viewId;
    private Object addNew;
    private String formId;
    private boolean customForm;

    private Command statisticCommand;

    public void setName(String name) {
        this.name = name;
    }

//    public void setParent(EdsShortcuts parent) {
//        this.parent = parent;
//    }
//
//    public EdsShortcuts getParent() {
//        return parent;
//    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return descriptioin;
    }

    private HTMLPanel item = new HTMLPanel("li", "");

    private Anchor rootElement = new Anchor();

    public ShortcutItem(ImageResource icon, String styleIcon, String viewName, Object addNew, String formId, boolean isCustomForm) {
        params = new String[]{};
        if (icon != null) {
            item.add(new Image(icon));
        } else if (styleIcon != null && styleIcon.length() > 0) {
            item.addStyleName(styleIcon);
        }
        item.ensureDebugId(viewName);
        rootElement.setWordWrap(false);
        item.add(rootElement);
        getRootElement().addClickHandler(event -> {
            if (!active) {
                activate();
            }
        });
        this.addNew = addNew;
        this.formId = formId;
        this.customForm = isCustomForm;
    }

    public void activate() {
        if (cmd != null) {
            cmd.execute();
        }
        /*if (this.getNonSavedFieldsCount() == 0) {
            parent.activate(this);
            this.setActiveStyle();
        }*/
    }

    public void setActiveStyle() {
        item.addStyleName("active");
        active = true;
    }

    public void setDefaultStyle() {
        item.removeStyleName("active");
        active = false;
    }

    public void setText(String text) {
        this.descriptioin = text;
        rootElement.setHTML(text);
    }

    public void setAccessKey(char accessKey) {
        if (' ' != accessKey) {
            getRootElement().setAccessKey(accessKey);
        }
    }

    public Anchor getRootElement() {
        return rootElement;
    }

    public void setCmd(Command cmd) {
        this.cmd = cmd;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public int getNonSavedFieldsCount() {
        return nonSavedFieldsCount;
    }

    public void setNonSavedFieldsCount(int nonSavedFieldsCount) {
        this.nonSavedFieldsCount = nonSavedFieldsCount;
    }

    public boolean isColapse() {
        return colapse;
    }

    public void setColapse(boolean colapse) {
        this.colapse = colapse;
    }

    public HTMLPanel getItem() {
        return item;
    }

    public Span getStatistics() {
        return statistics;
    }

    public void setStatistics(Span statistics) {
        this.statistics = statistics;
    }

    public Command getStatisticCommand() {
        return statisticCommand;
    }

    public void setStatisticCommand(Command statisticCommand) {
        this.statisticCommand = statisticCommand;
    }

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }

    public Object getAddNew() {
        return addNew;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public boolean isCustomForm() {
        return customForm;
    }

    public void setCustomForm(boolean customForm) {
        this.customForm = customForm;
    }
}