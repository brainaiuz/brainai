package com.finnetlimited.reportservice.core.client.ui.listbox;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 5/1/12
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DRSComboBox extends FlowPanel {

    private ArrayList<ComboBoxItem> items;
    private Anchor selectedItem;
    private FlowPanel itemsWrapper;
    private Integer selectedIndex;
    private Command changeHandler;
    private boolean isopen = false;


    public DRSComboBox() {
        this(new ArrayList<>());
    }

    public DRSComboBox(ArrayList<ComboBoxItem> items) {
        this(items, "");
    }

    public DRSComboBox(ArrayList<ComboBoxItem> items, String firstText) {
        this.items = items;
        init(firstText);
    }

    private void init(String firstText) {
        this.addDomHandler(event -> {
            isopen = false;
            hide();
        }, MouseOutEvent.getType());

        itemsWrapper = new FlowPanel();
        itemsWrapper.setStyleName("cmbx-wrapper");
        itemsWrapper.setVisible(false);
        itemsWrapper.getElement().setAttribute("style", "display: none;");
        selectedItem = new Anchor();
        //this.addDomHandler(createHandlerManager().getHandler(, )
        selectedItem.setHTML("<span class=\"cmbx-text\"></span><span class=\"cmbx-arrow\"></span>");
        selectedItem.setStyleName("cmbx-selecteditem");
        setTextToRoot(firstText);

        add(selectedItem);
        add(itemsWrapper);

        selectedItem.addClickHandler(event -> {
            isopen = !isopen;
            if (isopen) {
                show();
            } else {
                hide();
            }
        });

        for (ComboBoxItem item : items) {
            addItem(item.getText(), item.getValue());
        }
    }

    public void addItem(String text) {
        addItem(text, "");
    }

    public void addItem(String text, String value) {
        itemsWrapper.add(new ComboBoxItem(text, value));
    }

    public void setSelectedIndex(Integer selectedIndex) {
        this.selectedIndex = selectedIndex;

        if (itemsWrapper != null && itemsWrapper.getWidgetCount() > 0) {

            for (Integer i = 0; i < itemsWrapper.getWidgetCount(); i++) {
                if (selectedIndex != i) {
                    continue;
                }

                resetSelectedItems();
                ComboBoxItem item = (ComboBoxItem) itemsWrapper.getWidget(i);
                item.setSelected(true);
                setTextToRoot(item.getText());

            }
        }
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public void setTextToRoot(String text) {
        selectedItem.getElement().getFirstChildElement().setInnerHTML(text);
    }

    public void setCommand(Command command) {
        this.changeHandler = command;
    }

    private void resetSelectedItems() {

        for (Integer i = 0; i < itemsWrapper.getWidgetCount(); i++) {

            ComboBoxItem item = (ComboBoxItem) itemsWrapper.getWidget(i);
            item.setSelected(false);
        }
    }

    private void show() {
        selectedItem.addStyleName("cmb-expand-anchor");
        itemsWrapper.setVisible(true);
    }

    private void hide() {
        selectedItem.addStyleName("cmb-collapse-anchor");
        itemsWrapper.setVisible(false);
    }


    public class ComboBoxItem extends FlowPanel {

        public Anchor anchor;
        public String text;
        public String value;
        public Boolean selected;

        ComboBoxItem(String text) {
            this(text, "");
        }

        ComboBoxItem(String text, String value) {
            this.text = text;
            this.value = value;

            anchor = new Anchor();

            init();
        }

        public void setText(String text) {
            this.text = text;
        }

        public void init() {
            anchor.setHTML(this.text);
            anchor.getElement().setAttribute("value", this.value);
            add(anchor);
            final ComboBoxItem temp = this;
            getItem().addClickHandler(event -> {

                for (Integer i = 0; i < itemsWrapper.getWidgetCount(); i++) {
                    if (itemsWrapper.getWidget(i) == temp) {
                        selectedIndex = i;
                        setSelectedIndex(i);
                        break;
                    }
                }

                changeHandler.execute();
            });
        }

        public String getText() {
            return this.text;
        }

        private void setValue(String value) {
            this.value = value;
        }

        private String getValue() {
            return this.value;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        public Boolean getSelected() {
            return selected;
        }

        public Anchor getItem() {
            return anchor;
        }
    }
}

