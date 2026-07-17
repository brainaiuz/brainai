package com.edatasite.workforce.gwt.core.client.ui;


import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Mirjalol
 * Date: 28.08.12
 * Time: 16:14
 */
public class KpiSelectBox extends Composite {
    String text;//Current text, which selected
    private ListPanelType listPanelType;
    FlowPanel content = new FlowPanel();//Main container panel for SelectBox
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private String nullLabel = wfmStrings.pleaseSelect();
    HTMLPanel textPanel = null; //new HTMLPanel("span", "<em>" + nullLabel + "</em>");//Here you can see the selected text
    HTMLPanel itemsPanel = new HTMLPanel("ul", "");//Here are the internal elements
    ArrayList<SelectItem> items = new ArrayList<>();//All elements
    SelectItem selectedItem = new SelectItem(0, "", "");//Current selected element


    public KpiSelectBox() {
        onInitialize(null);
    }

    public KpiSelectBox(ListPanelType listPanelType) {
        this.listPanelType = listPanelType;
        onInitialize(null);
    }

    public KpiSelectBox(String customNullLabel) {
        onInitialize(customNullLabel);
        this.text = customNullLabel;
        setText(text);
    }

    private void onInitialize(String customNullLabel) {
        if (customNullLabel != null) {
            nullLabel = customNullLabel;
        }
        textPanel = new HTMLPanel("span", "<em>" + nullLabel + "</em>");
        content.addStyleName("optGroupCover pseudoSelect");

        textPanel.addDomHandler(event -> {
            content.addStyleName("expanded");
            content.getElement().setAttribute("tabindex", "1");
            content.getElement().focus();
        }, ClickEvent.getType());

        content.addDomHandler(event -> {
            content.getElement().removeAttribute("tabindex");
            content.removeStyleName("expanded");
        }, BlurEvent.getType());
        if (listPanelType != null && ListPanelType.GoodsDeliveredNoteListPanel.equals(listPanelType)) {
            if (Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                addItem(new SelectItem(0, nullLabel));
            }
        } else {
            addItem(new SelectItem(0, nullLabel));
        }

        content.add(textPanel);
        textPanel.setStyleName("curValue");
        selectedItem = null;
        content.add(itemsPanel);
        initWidget(content);
    }

    //Setting main visible text
    public void setText(String text) {
        textPanel.getElement().setInnerHTML("<em>" + text + "</em>");
    }

    public void addItem(final SelectItem item) {
        if (!item.getId().equals(0)) {
            items.add(item);
        }
        HTMLPanel itemPamel = new HTMLPanel("li", item.getName());
        itemPamel.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setText(item.getName());
                content.removeStyleName("expanded");
                if (!item.getId().equals(0)) {
                    selectedItem = item;
                } else {
                    selectedItem = null;
                }
                textPanel.fireEvent(new ChangeEvent() {
                    @Override
                    protected void dispatch(ChangeHandler handler) {
                        super.dispatch(handler);
                    }
                });
            }
        }, ClickEvent.getType());
        itemsPanel.add(itemPamel);
    }

    public SelectItem getSelectedItem() {
        return selectedItem;
    }

    public Integer getSelectedId() {
        if (selectedItem != null) {
            return selectedItem.getId();
        }
        return null;
    }

    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return textPanel.addDomHandler(handler, ChangeEvent.getType());
    }

    //Set element by index
    public void setSelected(Integer index) {
        for (SelectItem item : items) {
            if (item.getId().equals(index)) {
                selectedItem = item;
                text = selectedItem.getName();
                setText(text);
            }
        }
    }

    //Set element
    public void setSelected(SelectItem item) {
        for (SelectItem item1 : items) {
            if (item1.getId() == item.getId() && item1.getName().equals(item.getName()) && item1.getDescription().equals(item.getDescription())) {
                selectedItem = item1;
                text = selectedItem.getName();
                setText(text);
            }
        }
    }

    public ArrayList<SelectItem> getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        clear();
        for (SelectItem item : items) {
            if (item != null) {
                addItem(item);
            }
        }
    }

    private void clear() {
        itemsPanel.clear();
        items = null;
        items = new ArrayList<>();
        if (listPanelType != null && ListPanelType.GoodsDeliveredNoteListPanel.equals(listPanelType)) {
            if (Utils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                addItem(new SelectItem(0, nullLabel));
                setSelected(0);
            }
        } else {
            addItem(new SelectItem(0, nullLabel));
            setSelected(0);
        }

    }

    public void setSelectedNullLabel() {
        setText(nullLabel);
    }

    //Set element by text
    public void setSelectedText(String text) {
        for (SelectItem item : items) {
            if (item.getName().equals(text)) {
                selectedItem = item;
                this.text = selectedItem.getName();
                setText(this.text);
            }
        }
    }

    public void setEnabled(Boolean enable) {
//        textP
    }
}
