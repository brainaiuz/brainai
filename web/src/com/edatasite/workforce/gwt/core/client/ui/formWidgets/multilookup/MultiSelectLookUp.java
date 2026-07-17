package com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Tag;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.DropDownOracle;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleGwtSuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jamshid Asatillayev
 * Date: 9/5/11
 * Time: 4:06 PM
 */
public abstract class MultiSelectLookUp extends Composite {
    public static WfmStrings wfmStrings = WfmStrings.App.get();
    private ArrayList<SelectItem> selectedItems;
    private String elementId = DOM.createUniqueId();
    private DropDownOracle oracle;
    private LookUp box;
    private boolean disableBackSpaceRemove;
    private UL list;


    protected MultiSelectLookUp() {
        selectedItems = new ArrayList<>();
        oracle = new DropDownOracle();
        list = new UL();
        list.addStyleName("token-list-fb token-list--projectPosition");
        init();
    }

    public void setDisableBackSpaceRemove(boolean disableBackSpaceRemove) {
        this.disableBackSpaceRemove = disableBackSpaceRemove;
    }


    public LookUp getBox() {
        return box;
    }

    private void init() {
        final Panel panel = new Panel();
        initWidget(panel);
        final UL.LI item = new UL.LI();
        item.setStyleName("input-fb");
        item.setWidth("100%");
        final TextBox itemBox = new TextBox();

        itemBox.getElement().setAttribute("style", "outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;");
        box = new LookUp(itemBox) {
            @Override
            protected void lookUpService(ListingFilterParameter filterParametrs) {
                onLookUpService(filterParametrs);
            }

            @Override
            protected void onItemDeleteInsertUpdate(int type) {
                onActionPerformed(type);
            }
        };
        itemBox.setPlaceHolder(wfmStrings.searchTypeMessage());
        if (box.getOpenIcon() != null) {
            box.getOpenIcon().setStyleName("openIconWrapperFacebook");
        }
        box.getElement().setId(elementId);
        box.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        item.add(box);
        list.add(item);
        // this needs to be on the itemBox rather than box, or backspace will get executed twice
        itemBox.addFocusListener(new FocusListener() {
            @Override
            public void onFocus(Widget widget) {
            }

            @Override
            public void onLostFocus(Widget widget) {
                if (onCondition(itemBox.getText())) {
                    deselectItem();
                }
            }
        });
        itemBox.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                Scheduler.get().scheduleFinally(() -> {
                    getSuggestBox().hideSuggestionList();
                    getSuggestBox().getSuggestionPopup().hide();
                });
            }
        });
        itemBox.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                // only allow manual entries with @ signs (assumed email addresses)
                if (onCondition(itemBox.getText())) {
                    deselectItem();
                }
            }
            if (event.getNativeKeyCode() == 188) {
                if (onCondition(itemBox.getText())) {
                    deselectItem();
                }
            }
            // handle backspace
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE && !disableBackSpaceRemove) {
                if ("".equals(itemBox.getValue().trim())) {
                    if (list.getWidgetCount() < 2) {
                        return;
                    }
                    UL.LI li = (UL.LI) list.getWidget(list.getWidgetCount() - 2);
                    selectedItems.remove(li.getSelectItem());
                    list.remove(li);
                    itemBox.setFocus(true);
                }
            }
        });

        box.getSuggestBox().addSelectionHandler(selectionEvent -> deselectItem());
        panel.add(list);
//        panel.getElement().setAttribute("onclick", "document.getElementById('" + elementId + "').focus()");
        panel.addClickHandler(event -> box.getSuggestBox().setFocus(true));
    }

    public abstract boolean onCondition(String text);

    public void onLookUpService(ListingFilterParameter filterParametrs) {

    }

    public void onActionPerformed(int type) {

    }


    public void deselectItem(boolean... doNotFocus) {
        if (!Utils.isNullOrEmpty(box.getText())) {
            add(getSelectedItem(), doNotFocus);
        }
    }

    private void add(SelectItem item, boolean... doNotFocus) {
        if (item == null) {
            return;
        }
        if (selectedItems.contains(item)) {
            box.getSuggestBox().setText("");
            if (doNotFocus == null && doNotFocus.length == 0 && !doNotFocus[0]) {
                box.getSuggestBox().setFocus(true);
            }
            return;
        }
        final UL.LI displayItem = new UL.LI(item);
        displayItem.setStyleName("token-fb");
        Tag p = new Tag("p", "", item.getName());

        displayItem.addClickHandler(clickEvent -> {
            if (displayItem.getStyleName() != null && displayItem.getStyleName().contains("selected")) {
                displayItem.addStyleName("token-fb");
            } else {
                displayItem.addStyleName("selected-token-fb");
            }
        });

        Label closeButton = new Label();
        closeButton.setStyleName("close-x");
        closeButton.getElement().setInnerHTML("<svg class=\"icon--x\">\n" + "<use href=\"mainStyles/new-ui/icons/sprite__panels.svg#x\"></use>\n" + "</svg>");
        closeButton.addClickHandler(clickEvent -> removeListItem(displayItem, list));

        displayItem.add(p);
        displayItem.add(closeButton);
        // hold the original value of the item selected
        list.insert(displayItem, list.getWidgetCount() - 1);
        getSuggestBox().setValue("");
        if (doNotFocus == null && doNotFocus.length == 0 && !doNotFocus[0]) {
            getSuggestBox().setFocus(true);
        }
        selectedItems.add(displayItem.getSelectItem());
        list.fireEvent(new ChangeEvent() {
            @Override
            protected void dispatch(ChangeHandler handler) {
                super.dispatch(handler);
            }
        });
    }

    private void removeListItem(UL.LI displayItem, UL list) {
        selectedItems.remove(displayItem.getSelectItem());
        list.remove(displayItem);
        runRemoveAction();
        list.fireEvent(new ChangeEvent() {
            @Override
            protected void dispatch(ChangeHandler handler) {
                super.dispatch(handler);
            }
        });
    }

    public void addItem(SelectItem item) {
        box.addItem(item);
    }

    public void clear() {
        if (box != null) {
            box.clear();
        }
        if (selectedItems != null) {
            selectedItems.clear();
        }
        if (list != null) {
            if (list.getWidgetCount() > 1) {
                for (int i = list.getWidgetCount() - 2; i > -1; i--) {
                    UL.LI li = (UL.LI) list.getWidget(i);
                    removeListItem(li, list);
                }
            }
        }
    }

    public void clearOracleItems() {
        box.clearOracleItems();
    }


    public void addLetters(String letter) {
        box.addLetters(letter);
    }

    public void setSelectedItems(SelectItem... selectedItems) {
        for (SelectItem selectedItem : selectedItems) {
            if (selectedItem != null) {
                add(selectedItem);
            }
        }
    }

    public void setSelectedItems(List<SelectItem> list) {
        if (list.size() > 0) {
            list.forEach(n -> add(n));
        }
    }

    public void setSelectedItems(ArrayList<SelectItem> selectedItems) {
        for (SelectItem selectedItem : selectedItems) {
            add(selectedItem);
        }

    }

    public SelectItem getSelectedItem(Integer itemId) {
        return box.getSelectedItem(itemId);
    }

    public ArrayList<SelectItem> getSelectedItems() {
        ArrayList<SelectItem> items = new ArrayList<>();
        items.addAll(selectedItems);
        return items;
    }

    public ArrayList<Integer> getSelectedItemIds() {
        ArrayList<Integer> itemIds = new ArrayList<>();
        for (SelectItem item : selectedItems) {
            itemIds.add(item.getId());
        }
        return itemIds;
    }

    public Integer getLastSelectedItemId() {
        SelectItem item = null;
        if (selectedItems != null && selectedItems.size() > 0) {
            return selectedItems.get(selectedItems.size() - 1).getId();
        }
        return null;
    }

    public String getSelectedItemsAsString() {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for (SelectItem item : selectedItems) {
            if (item != null && item.getName() != null) {
                result.append(isFirst ? "" : ",").append(item.getName());
                isFirst = false;
            }
        }
        return "".equals(result.toString()) ? null : result.toString();
    }

    public String getSelectedItemsIdsAsString() {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for (SelectItem item : selectedItems) {
            if (item != null && item.getName() != null) {
                result.append(isFirst ? "" : ",").append(item.getId());
                isFirst = false;
            }
        }
        return "".equals(result.toString()) ? null : result.toString();
    }

    public SelectItem getSelectedItem() {
        if (box.getSelectedItem() == null && onCondition(box.getText())) {
            SelectItem item = new SelectItem();
            item.setName(box.getText().replace(",", ""));
            box.setSelected(item);
            return item;
        } else {
            return box.getSelectedItem();
        }
    }

    public Integer getSelectedItemID() {
        return box.getSelectedItemID();
    }

    public Integer getSelectedItemIDByValue(String value) {
        return box.getSelectedItemIDByValue(value);
    }

    public String getText() {
        return getSelectedItemsAsString();
    }

    public void setText(String value) {
        clear();
        if (value != null && !"".equals(value.trim())) {
            SelectItem item = new SelectItem(null, value);
            add(item);
        }
    }

    public TextBoxBase getTextBox() {
        return box.getTextBox();
    }

    public void refreshOracle(boolean forceToInit) {
        box.refreshOracle(forceToInit);
    }

    public void setEnabled(boolean b) {
        box.setEnabled(b);
    }

    public void setItems(String txt, SelectItem[] items) {
        box.setItems(txt, items);
    }

    public void setSelected(Integer ID, String value) {
        box.setSelected(ID, value);
    }

    public void setSelected(Integer itemId) {
        box.setSelected(itemId);
    }

    public void setSelected(SelectItem selectedItem) {
        box.setSelected(selectedItem);
    }

    public void setSelected(String txt) {
        box.setSelected(txt);
    }

    public SimpleGwtSuggestBox getSuggestBox() {
        return box.getSuggestBox();
    }

    public ListingFilterParameter getFilterParametrs() {
        return box.getFilterParametrs();
    }

    public void setValue(String value) {
        box.setValue(value);
    }

    public void setValue(String value, boolean fireEvents) {
        box.setValue(value, fireEvents);
    }

    public void setAfterSearch(Command afterSearch) {
        box.setAfterSearch(afterSearch);
    }

    public void runBeforeSearch() {
        box.runBeforeSearch();
    }

    public void runAfterSearch() {
        box.runAfterSearch();
    }

    public void setBeforeSearch(Command beforeSearch) {
        box.setBeforeSearch(beforeSearch);
    }

    public void setFilterParametrs(ListingFilterParameter filterParametrs) {
        box.setFilterParametrs(filterParametrs);
    }

    public Command getAfterSearch() {
        return box.getAfterSearch();
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return box.addValueChangeHandler(handler);
    }

    public Command getBeforeSearch() {
        return box.getBeforeSearch();
    }

    public UL getList() {
        return list;
    }

    private class Panel extends FlowPanel implements HasClickHandlers {
        @Override
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }
    }

    public void runRemoveAction() {

    }
}
