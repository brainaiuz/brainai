package com.edatasite.workforce.gwt.core.client.ui.wfmDropdown;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.ui.html.OptGroup;
import gwt.material.design.client.ui.html.Option;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 05.01.2009
 * Time: 11:17:17
 * To change this template use File | Settings | File Templates.
 */
public class WfmDropdown extends MaterialComboBox<SelectItem> implements CustomCellInterface {
    

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final String DEFAULT_CREATE_VALUE = wfmStrings.createNewItem();
    private String DEFAULT_VALUE;
    private String[] ADD_NEW_ITEM;

    private DropdownListener dropdownListener;
    private boolean addNew;
    private boolean withoutDefaultValue;
    private Map<SelectItem, String> itemGroup = new HashMap<>();

    public WfmDropdown() {
        this(false, "");
    }

    public WfmDropdown(boolean addNew, boolean withoutDefaultValue) {
        this(addNew, null, null, withoutDefaultValue);
    }

    public WfmDropdown(boolean addNew, String addNewText) {
        this(addNew, new String[]{addNewText}, null, false);
    }

    public WfmDropdown(boolean addNew, String[] addNewText, String defaultValue, boolean withoutDefaultValue) {
        this.addNew = addNew;
        this.DEFAULT_VALUE = defaultValue != null ? defaultValue : wfmStrings.pleaseSelect();
        if (addNewText != null && addNewText.length > 0) {
            ADD_NEW_ITEM = new String[addNewText.length];
            for (int i = 0; i < addNewText.length; i++) {
                ADD_NEW_ITEM[i] = addNewText[i] != null && !addNewText[i].equals("") ? addNewText[i] : DEFAULT_CREATE_VALUE;
            }
        }
        this.withoutDefaultValue = withoutDefaultValue;
        setStylePrimaryName("wfm-dropdown");

        init();
        clear();
    }

    public void setWithoutDefaultValue(boolean withoutDefaultValue) {
        this.withoutDefaultValue = withoutDefaultValue;
    }

    public Option addItem(final SelectItem item) {
        return super.addItem(item.getName(), item);
    }

    public void addItems(String groupname, SelectItem[] items) {
        if (items != null && items.length > 0) {
            OptGroup group = new OptGroup(groupname);
            for (SelectItem item : items) {
                super.addItem(item.getName(), item, group);
                itemGroup.put(item, groupname);
            }
            super.addGroup(group);
        }
    }

    /**
     * You can add array of SelectItem items to the WfmDropdown.
     * WfmDropdown shows on the popup item name and saves item objectID.
     * You may take objectId of the selected item.
     */
    public void addItems(SelectItem[] items) {
        clear();
        for (SelectItem item : items) {
            addItem(item.getName(), item);
        }
    }

    public void clear() {
        super.clear();
        itemGroup.clear();
        addDefaultItems();
    }

    /**
     * Sets style to the WfmDropdown widget.
     **/

    public void addEventHandler(DropdownListener dropdownListener) {
        this.dropdownListener = dropdownListener;
    }

    public boolean isSomethingSelected() {
        return getSelected() != null;
    }

    public Integer getSelectedId() {
        return getSelected() == null ? null : getSelected().getId();
    }

    public void setSelected(Integer objectID) {
        setSelected(objectID, false);
    }

    public SelectItem getSelected() {
        List<SelectItem> selectItems = super.getSelectedValue();
        return selectItems.size() > 0 ? selectItems.get(0) : null;
    }

    @Override
    public void setSingleValue(SelectItem value) {
        super.setSingleValue(value);
    }

    public void setSelected(Integer objectID, boolean withListener) {
        Integer selectedId = getSelectedId();
        setSingleValue(getById(objectID));
        if (selectedId != null && !selectedId.equals(objectID) && withListener && dropdownListener != null) {
            dropdownListener.itemSelected();
        }
    }

    public SelectItem getSelectedItem() {
        return getSelected();
    }

    public boolean isValid() {
        SelectItem item = getSelected();
        return item != null && item.getName() != null && !DEFAULT_VALUE.equals(item.getName());
    }

    public boolean contain(Integer objectId) {
        return getById(objectId) != null;
    }

    public void setData(Object data) {
        addItem(((SelectItem) data).getName(), (SelectItem) data);
    }

    public Object getData(Integer itemID) {
        return getById(itemID);
    }

    public SelectItem getById(Integer id) {
        List<SelectItem> values = getValues();
        if (values != null && id != null) {
            List<SelectItem> result = values.stream().filter(item -> id.equals(item.getId())).collect(Collectors.toList());
            return result.size() > 0 ? result.get(0) : null;
        }
        return null;
    }

    public Object getSelectedData() {
        return getSelected();
    }

    private void creativeItem() {
        if (ADD_NEW_ITEM != null && ADD_NEW_ITEM.length > 0) {
            for (final String itemText : ADD_NEW_ITEM) {
                MenuItem item = new MenuItem(itemText, (Command) () -> {
                    if (dropdownListener != null) {
                        dropdownListener.saveNewItem();
                    }
                });
                item.getElement().getStyle().setProperty("whiteSpace", "nowrap");
                item.setStyleName("create-item");
            }
        }

    }

    private void addDefaultItems() {
        if (!withoutDefaultValue) {
            addItem(DEFAULT_VALUE, new SelectItem(null, DEFAULT_VALUE));
        }
        if (addNew) {
            creativeItem();
        }
    }

    public void removeStyle() {
        //TODO styling is still to discuss
    }

    private void init() {
        addValueChangeHandler((e) -> {
            if (dropdownListener != null) {
                dropdownListener.itemSelected();
            }
        });
    }

    public Map<SelectItem, String> getItemGroup() {
        return itemGroup;
    }

    @Override
    public String getDisplayValue() {
        SelectItem item = getSelected();
        return item != null && item.getName() != null ? item.getName() : "";
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {
        super.open();
    }

    public void setAddNew(boolean addNew) {
        this.addNew = addNew;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    public int getItemCount() {
        return getValues().size();
    }

    @Override
    public void setStyleName(String style) {
        //super.setStyleName(style);
    }
}
