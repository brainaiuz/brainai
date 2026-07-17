package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11/28/11
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.                                                                      `
 */
public class CustomerLookUpItem extends CustomList {

    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingFilterParameter filterParameter;
    private TextBox searchBox;
    private ArrayList<CustomListItem> defaultItems;
    private ArrayList<CustomListItem> selectedItems;
    private ArrayList<SelectItem> removeItems;
    private boolean isFirst = true;
    private Integer clientID = null;
    private boolean reload;

    public CustomerLookUpItem() {
        this(Design.CHECK, true);
    }

    public CustomerLookUpItem(Design design, boolean showSearchPanel) {
        this(design, showSearchPanel, null, null);
    }

    public CustomerLookUpItem(Design design, boolean showSearchPanel, SelectItem[] list) {
        this(design, showSearchPanel, list, null);
    }

    public CustomerLookUpItem(Design design, boolean showSearchPanel, SelectItem[] list, Integer currencyID) {
        super(design, showSearchPanel);
        this.setSearchText(wfmStrings.searchClients());
        filterParameter = new ListingFilterParameter();
        filterParameter.setCurrencyID(currencyID);
        selectedItems = new ArrayList<>();
        itemsFromEditForm(list);
        defaultItems = new ArrayList<>();
        //initialize();
    }

    public void initialize() {
        loadData(filterParameter);
        /*super.searchBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                String text = ((TextBox) event.getSource()).getText();
                if (!"".equals(text)) {
                    filterParameter.setSearchKey(text);
                    checkSelectedItems();
                    loadData(filterParameter);
                } else {
                    listPanel.clear();
                    checkSelectedItems();
                    items.clear();
                    SelectItem[] item = new SelectItem[defaultItems.size()];
                    int i = 0;
                    for (CustomListItem customListItem : defaultItems) {
                        item[i] = new SelectItem();
                        item[i] = customListItem.getProductItem();
                        i++;
                    }
                    i = 0;
                    for (CustomListItem cItem : selectedItems) {
                        add(cItem);
                        items.get(i).setCheck(true);
                        i++;
                    }
                    removeItems = removeDuplicates(selectedItems, item);
                    for (SelectItem items : removeItems) {
                        add(items);
                    }
                }
            }
        });*/
    }

    public void loadData(final ListingFilterParameter fp) {
        LoadingPanel.loading(true);
        AccountingService.App.get().getClientList(fp, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                LoadingPanel.loading(false);
                listPanel.clear();
                items.clear();
                /*CustomListItem checkAll = new CustomListItem(new SelectItem(0, "<b>" + wfmStrings.selectAll() + "</b>"));
                add(checkAll);
                checkAll.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                        setCheckAllItems(booleanValueChangeEvent.getValue());
                    }
                });*/
                if (selectedItems.size() > 0 && (fp.getSearchKey() == null || "".equals(fp.getSearchKey())) && !isReload()) {
                    int i = 0;
                    for (CustomListItem cItem : selectedItems) {
                        add(cItem);
                        CustomListItem customListItem = (CustomListItem) listPanel.getWidget(i);
                        customListItem.setCheck(true);
                        customListItem.setVisible(true);
                        i++;
                    }
                    removeItems = removeDuplicates(selectedItems, selectItems);
                    for (SelectItem item : removeItems) {
                        add(item);
                    }
                } else {
                    for (SelectItem item : selectItems) {
                        add(item);
                        if (item.getId().equals(clientID)) {
                            setCheckedItem(item, true);
                        }
                    }
                }
                if (isFirst) {
                    defaultItems = copyItems(items);
                    isFirst = false;
                }
            }
        });

    }

    public ArrayList<CustomListItem> copyItems(ArrayList<CustomListItem> src) {
        ArrayList<CustomListItem> result = new ArrayList<>();
        result.addAll(src);
        return result;
    }

    public void checkSelectedItems() {
        for (CustomListItem item : super.getItems()) {
            if (item.getValue()) {
                if (selectedItems.size() > 0) {
                    boolean oldItem = false;
                    for (CustomListItem sItem : selectedItems) {
                        if (sItem.equals(item)) {
                            oldItem = true;
                            break;
                        }
                    }
                    if (!oldItem) {
                        selectedItems.add(item);
                    }
                } else {
                    selectedItems.add(item);
                }
            }
        }
    }

    public ArrayList<SelectItem> removeDuplicates(ArrayList<CustomListItem> selectedList, SelectItem[] newList) {
        ArrayList<SelectItem> list = new ArrayList<SelectItem>(Arrays.asList(newList));
        for (CustomListItem checkedItem : selectedList) {
            int i = 0;
            SelectItem sitem = checkedItem.getItem();
            for (SelectItem item : list) {
                if (sitem.equals(item)) {
                    list.remove(i);
                    break;
                }
                i++;
            }
        }
        return list;
    }

    private void itemsFromEditForm(SelectItem[] list) {
        if (list != null)
            for (SelectItem items : list) {
                selectedItems.add(new CustomListItem(items));
            }
    }

    @Override
    public void add(SelectItem item) {
        super.add(item);
    }

    @Override
    public void add(CustomListItem item) {
        super.add(item);
    }

    public void setCurrencyID(Integer currencyID) {
        filterParameter.setCurrencyID(currencyID);
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public void setSpecialOffer(boolean isSpecialOffer) {
        filterParameter.setSpecialOffer(isSpecialOffer);
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }
}
