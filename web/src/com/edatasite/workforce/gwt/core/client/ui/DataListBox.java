package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gwt.material.design.jquery.client.api.JQuery.$;

@SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument"})
public class DataListBox extends MaterialListValueBox<SelectItem> implements Clearable, CustomCellInterface {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final String NOT_SET = wfmStrings.pleaseSelect();

    private SelectItem selectedItem;
    private SelectItem previousSelectedItem;
    private SelectItem[] items;
    private Set<String> itemNames;
    private Map<Integer, Integer> itemsById;
    private String nullLabel = wfmStrings.pleaseSelect();
    private boolean insideOrigin;
    private boolean allowFirstItem;
    private boolean withoutNullLabel;

    public DataListBox() {
        this(false, false);
    }

    public DataListBox(boolean isMultipleSelect) {
        this(isMultipleSelect, false);
    }

    public DataListBox(boolean isMultipleSelect, boolean insideOrigin) {
        this(isMultipleSelect, insideOrigin, false);
    }

    public DataListBox(boolean isMultipleSelect, boolean insideOrigin, boolean withoutNullLabel) {
        super();
        addStyleName("form-control");
        removeStyleName("input-field"); //😡 Discuss with stanislav
        setMultipleSelect(isMultipleSelect);
        setWithoutNullLabel(withoutNullLabel);
        this.insideOrigin = insideOrigin;
        addValueChangeHandler(event -> handleChange());
        // this is hack, Arrow UP, Arrow DOWN doesn't return the selected value right if ENTER not clicked
        addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                handleChange();
            }
        });
        if (getElement() != null) {
            getElement().setAttribute("autocomplete", "off");
        }
    }


    @Override
    public void load() {
        getListBox().getElement().setAttribute("inside-origin", insideOrigin + "");
        super.load();
        $(getSelectElement()).parent().click((event) -> {
            $(getSelectElement()).parent().find("input.select-dropdown").focus();
            event.stopPropagation();
            event.stopImmediatePropagation();
            return true;
        });
    }

    private void handleChange() {
        previousSelectedItem = selectedItem;
        int index = getSelectedIndex();
        if (allowFirstItem && index == 0) {
            selectedItem = new SelectItem(0, NOT_SET);
        } else if (wfmStrings.pleaseSelect().equalsIgnoreCase(getValue(0).getName())) {
            index--;
            if (items != null && index >= 0) {
                selectedItem = getSelectedValue();
            } else {
                selectedItem = null;
            }
        } else {
            selectedItem = getSelectedValue();
        }

        if (changeEvent != null) {
            changeEvent.execute();
        }
    }

    public void addStyleNameToInput(String style) {
        JQuery.$(this).find("input").addClass(style);
    }

    public void removeStyleNameFromInput(String style) {
        JQuery.$(this).find("input").removeClass(style);
    }

    public void setChangeEvent(Command changeEvent) {
        this.changeEvent = changeEvent;
    }

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        int index = getSelectedIndex();
        if (allowFirstItem && index == 0) {
            selectedItem = new SelectItem(0, NOT_SET);
        } else if (wfmStrings.pleaseSelect().equalsIgnoreCase(getValue(0).getName())) {
            index--;
            if (items != null && index >= 0) {
                selectedItem = getSelectedValue();
            }
        } else if (withoutNullLabel) {
            selectedItem = getSelectedValue();
        }

    }

    private Command changeEvent;

    public SelectItem getSelectedItem(boolean... ifNullReturnFirst) {
        if (selectedItem != null) {
            return selectedItem;
        } else if (ifNullReturnFirst != null && ifNullReturnFirst.length > 0 && ifNullReturnFirst[0]) {
            return getItems() != null && getItems().length > 0 ? getItems()[0] : null;
        }
        return null;
    }

    public SelectItem getPreviousSelectedItem() {
        return previousSelectedItem;
    }

    public String getNullLabel() {
        return nullLabel;
    }

    public void setNullLabel(String nullLabel) {
        this.nullLabel = nullLabel;
    }

    public void setItems(SelectItem[] items, String selectByWord) {
        setItems(items);
        setSelectedByTheBestValue(selectByWord);
    }

    public void setSelectedByTheBestValue(String selectByWord) {
        selectByWord = getTheBestMatch(itemNames, selectByWord);
        setSelectedByValue(selectByWord, true);
    }


    private Set<String> getAsSet(SelectItem[] items) {
        Set<String> result = new HashSet<>();
        if (items != null && items.length > 0) {
            for (SelectItem item : items) {
                if (item != null && item.getName() != null) {
                    result.add(item.getName().toLowerCase());
                }
            }
        }
        return result;
    }

    private String getTheBestMatch(Set<String> choices, String theBest) {
        if (theBest != null) {
            if (choices != null && choices.size() > 0) {
                String theBestLower = theBest.toLowerCase();
                if (choices.contains(theBestLower)) {//why lowercase? the choices are in lowercase
                    return theBest;
                }
                for (String choice : choices) {
                    if (choice.contains(theBest) || theBest.contains(choice)) {//return if there is no theBest in choice but have something similar to it.
                        return choice;
                    }
                }
                //if the best is not found yet we need to split theBest
                String[] theBests = theBestLower.split(" {2}");
                for (String bestPart : theBests) {
                    for (String choice : choices) {
                        if (choice.contains(bestPart) || bestPart.contains(choice)) {//return if there is no theBest in choice but have something similar to it.
                            return choice;
                        }
                    }
                }
            }
        }
        return theBest;
    }

    public void setItems(SelectItem[] items, WfmForm.Field... fieldOfTheWidget) {
        WfmForm.Field field = fieldOfTheWidget != null && fieldOfTheWidget.length > 0 ? fieldOfTheWidget[0] : null;
        boolean setIfTheNameIsSame = field != null;
        this.itemNames = new HashSet<>();
        if (items != null) {
            SelectItem id = selectedItem;
            this.items = items;
            this.itemNames = getAsSet(this.items);
            itemsById = new HashMap<>();

            for (int i = 0; i < items.length; i++) {
                if (id == null && setIfTheNameIsSame && items[i] != null && items[i].getName() != null && field.getLabel() != null && field.getLabel().toLowerCase().equals(items[i].getName().toLowerCase())) {
                    id = items[i];
                }
                if (items[i] != null) {
                    itemsById.put(items[i].getId(), i);
                }
            }

            previousSelectedItem = null;
            selectedItem = null;

            if (withoutNullLabel) {
                initListBox(this, items);
            } else {
                initListBox(this, items, nullLabel);
            }

            setSelected(id);
        }
    }

    private void initListBox(final MaterialListValueBox<SelectItem> listBox1, SelectItem[] data, String nullLabel) {
        while (listBox1.getItemCount() > 0) {
            listBox1.removeItem(0, false);
        }
        if (nullLabel == null) {
            nullLabel = NOT_SET;
        }
        listBox1.addItem(new SelectItem(-1, nullLabel), nullLabel, false);
        int index = 1;
        for (SelectItem aData : data) {
            if (aData != null) {
                listBox1.addItem(aData, aData.getName(), false/*, aData.getStyleName()*/);
                listBox1.getOptionElement(index).setInnerHTML(aData.getName());
                index++;
            }
        }
        listBox1.reload();
    }

    private void initListBox(final MaterialListValueBox<SelectItem> listBox, SelectItem[] data) {
        while (listBox.getItemCount() > 0) {
            listBox.removeItem(0, false);
        }
        for (SelectItem aData : data) {
            listBox.addItem(aData, aData.getName(), false/*, aData.getStyleName()*/);
        }
        listBox.reload();
    }

    public void setItems(TreeSelectItem[] items, WfmForm.Field... fieldOfTheWidget) {
        if (items != null) {
            List<SelectItem> selectItems = new ArrayList<>();
            for (TreeSelectItem treeSelectItem : items) {
                if (treeSelectItem.isShowInDropDown() || (treeSelectItem.getParent() == null && treeSelectItem.isSystematic())) {
                    treeSelectItem.setName("<b>" + treeSelectItem.getName() + "</b>");
                    selectItems.add(treeSelectItem);
                }
                addChildren(selectItems, treeSelectItem);
            }
            setItems(selectItems.toArray(new SelectItem[]{}), fieldOfTheWidget);
        }
    }

    private void addChildren(final List<SelectItem> selectItems, TreeSelectItem treeSelectItem) {
        if (treeSelectItem != null && treeSelectItem.hasChildren()) {
            for (TreeSelectItem child : treeSelectItem.getChildren()) {
                if (child != null) {
                    if (child.isShowInDropDown()) {
                        selectItems.add(child.asSelectItem(true));
                    }
                    addChildren(selectItems, child);
                }
            }
        }
    }

    public void removeListItem(int itemIndex) {
        ArrayList<SelectItem> tList = new ArrayList<>(Arrays.asList(items));
        if (itemIndex >= 1) {
            tList.remove(itemIndex - 1);
        }
        items = tList.toArray(new SelectItem[]{});
        if (itemIndex >= 1) {
            removeItem(itemIndex);
        }
    }

    public void removeListItems() {
        items = null;
        itemsById = null;
        clear();
    }

    public void removeListItem(SelectItem item) {
        List<SelectItem> tList = new ArrayList<>(Arrays.asList(items));
        int index = -1;
        for (int i = 0; i < items.length; i++) {
            if (item.getId().equals(items[i].getId())) {
                index = i;
            }
        }
        if (index != -1) {
            getItemsById().remove(item.getId());
            tList.remove(item);
            items = tList.toArray(new SelectItem[]{});
            reassignItemsById();
            if (wfmStrings.pleaseSelect().equals(getValue(0).getName())) {
                removeItem(index + 1);
            } else {
                removeItem(index);
            }
        }
    }

    private void reassignItemsById() {
        Map<Integer, Integer> itemsById2 = new HashMap<>();
        for (int i = 0; i < items.length; i++) {
            itemsById2.put(items[i].getId(), i);
        }
        itemsById = itemsById2;
    }

    public void removeBySelectItemId(SelectItem item) {
        if (getNullLabel().equals(item.getName())) {
            List<SelectItem> tList = new ArrayList<>(Arrays.asList(items));
            getItemsById().remove(item.getId());
            tList.remove(getSelectedIndex() - 1);
            items = tList.toArray(new SelectItem[]{});
            removeItem(getSelectedIndex());
        } else {
            getItemsById().remove(item.getId());
            List<SelectItem> tList = new ArrayList<>(Arrays.asList(items));
            tList.remove(getSelectedIndex() - 1);
            items = tList.toArray(new SelectItem[]{});
            removeItem(getSelectedIndex());
        }
    }

    public void removeByItemId(SelectItem item) {
        List<SelectItem> tList = new ArrayList<>(Arrays.asList(items));
        getItemsById().remove(item.getId());
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getId().equals(item.getId())) {
                tList.remove(i);
                items = tList.toArray(new SelectItem[]{});
                break;
            }
        }
    }

    public void addListItem(SelectItem item) {
        List<SelectItem> tList = new ArrayList<>();
        if ("Myself".equals(item.getName())) {
            tList.add(item);
            if (items != null) {
                tList.addAll(Arrays.asList(items));
            }
        } else {
            if (items != null) {
                tList.addAll(Arrays.asList(items));
            }
            tList.add(item);
        }
        items = tList.toArray(new SelectItem[]{});

        if (itemsById != null) {
            itemsById.clear();
        }

        itemsById = new HashMap<>();
        for (int i = 0; i < items.length; i++) {
            itemsById.put(items[i].getId(), i);
        }
        previousSelectedItem = null;
        selectedItem = null;
        if (withoutNullLabel) {
            initListBox(this, items);
        } else {
            initListBox(this, items, nullLabel);
        }
    }

    public boolean isSomethingSelected() {
        return getSelectedIndex() > 0;
    }

    public SelectItem[] getItems() {
        return items;
    }

    @Deprecated //use setSelected(SelectItem item)
    public void setSelected(Integer id) {
        if (id != null && items != null) {
            Integer index = itemsById.get(id);
            if (index != null) {
                if (wfmStrings.pleaseSelect().equals(getValue(0).getName())) {
                    setSelectedIndex(index + 1);
                } else {
                    setSelectedIndex(index);
                }
                selectedItem = items[index];
            }
        }
    }

    public void setSelectedNullLabel() {
        setSelectedIndex(0);
        selectedItem = null;
    }

    public void setSelectedItem(SelectItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setSelected(SelectItem item) {
        selectedItem = item;
        setSelectedValue(item);
    }

    public void setSelectedByValue(String item, boolean... ignoreCaseSensitive) {
        if (item != null && items != null && !"".equals(item)) {
            int index = 0;
            for (SelectItem item1 : items) {
                if (item.equals(item1.getName()) || (ignoreCaseSensitive != null && ignoreCaseSensitive.length > 0 && ignoreCaseSensitive[0] && item.equalsIgnoreCase(item1.getName()))) {
                    if (item1.getId() != null) {
                        setSelected(item1);
                    } else {
                        if (wfmStrings.pleaseSelect().equals(getValue(0))) {
                            setSelectedIndex(index + 1);
                        } else {
                            setSelectedIndex(index);
                        }
                    }
                    selectedItem = item1;
                    break;
                }
                index++;
            }
        } else {
            setSelectedNullLabel();
        }
    }

    public void setSelectedByDescription(String description) {
        if (description != null && items != null && description.length() > 0) {
            for (SelectItem item1 : items) {
                if (description.equals(item1.getDescription())) {
                    setSelected(item1);
                    selectedItem = item1;
                    break;
                }
            }
        }
    }

    public Map<Integer, Integer> getItemsById() {
        return itemsById;
    }

    public void setAllowFirstItem(boolean allowFirstItem) {
        this.allowFirstItem = allowFirstItem;
    }

    public void resetSelectedItem() {
        selectedItem = null;
    }

    public void setWithoutNullLabel(boolean withoutNullLabel) {
        this.withoutNullLabel = withoutNullLabel;
    }

    public void clearSelected() {
        setSelectedIndex(0);
    }

    public boolean isValid() {
        return selectedItem != null;
    }

    public Integer getSelectedId(boolean... ifNullReturnFirst) {
        if (selectedItem != null) {
            return selectedItem.getId();
        } else if (ifNullReturnFirst != null && ifNullReturnFirst.length > 0 && ifNullReturnFirst[0]) {
            if (items != null && items.length > 0 && items[0] != null) {
                return items[0].getId();
            }
        }
        return null;
    }

    public void setSelectedDefault() {
        if (items != null && items.length > 0) {
            for (SelectItem item : items) {
                if (item != null && item.isSelected()) {
                    setSelected(item);
                    break;
                }
            }
        }
    }

    public void setIdAttribute(String id) {
        if (id != null) {
            getListBox().getElement().setAttribute("id", id);
            reload();
        }
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
    }

    @Override
    public String getDisplayValue() {
        return getSelectedItem() != null ? getSelectedItem().getName() : "";
    }

    @Override
    public void setItemValue(Object value) {
        setSelected((Integer) value);
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }

    public void setSelectedByCode(String status) {
        if (status != null) {
            boolean selected = false;
            if (items != null && items.length > 0 && (items[0] instanceof ReferenceItem || items[0] != null)) {
                for (SelectItem item : items) {
                    if (item instanceof ReferenceItem) {
                        if (item.getCode() != null && status.equals(item.getCode())) {
                            setSelected(item);
                            selected = true;
                        }
                    } else if (item != null && item.getCode() != null && !"".equals(item.getCode())) {
                        if (item.getCode().equals(status)) {
                            setSelected(item);
                            selected = true;
                        }
                    } else if (item != null && item.getDescription() != null) {
                        if (item.getDescription().equals(status)) {
                            setSelected(item);
                            selected = true;
                        }
                    }
                }
            }
            if (!selected) {
                setSelectedByValue(status);
            }
        }
    }

    public void setWithoutNullLabel(String label) {
        SelectItem item = new SelectItem(0, label != null ? label : wfmStrings.pleaseSelect());
        this.setWithoutNullLabel(true);
        this.setItems(new SelectItem[]{item});
    }
}
