package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.CheckBoxCell;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * User: Abdullo
 * Date: 08.10.12
 * Time: 15:45
 */
public class CheckboxMailingListDataGrid extends KpiDataGrid {
    private Integer entityID;
    private boolean isMessage;
    private String defaultValue;
    private Column<SelectItem, Boolean> checkBoxCell;
    private LinkedHashSet<SelectItem> selectItems = new LinkedHashSet<>();
    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId() != null ? item.getId() : item.getName();

    public CheckboxMailingListDataGrid(Integer entityID, boolean isMessage, String defaultValue) {
        super(KEY_PROVIDER);
        getElement().getStyle().setOverflow(com.google.gwt.dom.client.Style.Overflow.AUTO);
        setHeight("200px");
        this.entityID = entityID;
        this.isMessage = isMessage;
        this.defaultValue = defaultValue;
        setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), "", null));
        initData();
        refreshData();
    }

    public void refreshData() {
        if (isMessage) {
            getByMessageID();
        } else {
            getByEntityID();
        }
    }

    private void getByEntityID() {
        MassMailService.App.get().getMailListByCrmEntityID(entityID, new AbstractAsyncCallback<SelectItem[]>() {
            public void onFailure(Throwable throwable) {
            }

            public void onSuccess(final SelectItem[] lists) {
                supplyProvider(lists);
                if (defaultValue != null) {
                    setSelectedSubscriptionList(defaultValue);
                }
            }
        });
    }

    public void getByMessageID() {
        MassMailService.App.get().getMailListsByMessage(entityID, new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable throwable) {
            }

            public void success(final SelectItem[] lists) {
                supplyProvider(lists);
            }
        });
    }

    private void initData() {
        checkBoxCell = new Column<SelectItem, Boolean>(new CheckBoxCell(true, true)) {
            @Override
            public Boolean getValue(SelectItem object) {
                if (object.isSelected()) {
                    selectItems.add(object);
                }
                return object.isSelected();
            }
        };
        addColumn(checkBoxCell, " ");
        setColumnWidth(checkBoxCell, 35, com.google.gwt.dom.client.Style.Unit.PX);
        checkBoxCell.setFieldUpdater((index, object, value) -> updateChecked(object, value));
        Column<SelectItem, String> nameCell = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(SelectItem object) {
                return refactor(object.getName());
            }
        };
        addColumn(nameCell, wfmStrings.mailingList());
        setColumnWidth(nameCell, 80, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    public void updateChecked(SelectItem object, Boolean value) {
        object.setSelected(value);
        if (value) {
            selectItems.add(object);
        } else {
            selectItems.remove(object);
        }
    }

    public SelectItem[] getSelectedItems() {
        return selectItems.toArray(new SelectItem[selectItems.size()]);
    }

    public LinkedHashSet<SelectItem> getSelectItemsList() {
        return selectItems;
    }

    public ArrayList<Integer> getSelectedIdsList() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (SelectItem select : selectItems) {
            ids.add(select.getId());
        }
        return ids;
    }

    @Override
    public void supplyProvider(Object[] listItems) {
        super.supplyProvider(listItems);
        super.refresh();
    }

    private void setSelectedSubscriptionList(ArrayList<Integer> ids) {
        if (ids != null && ids.size() > 0 && getList().size() > 0) {
            for (Integer id : ids) {
                for (Object sel : getList()) {
                    SelectItem s = (SelectItem) sel;
                    if (id.equals(s.getId())) {
                        s.setSelected(true);
                        selectItems.add(s);
                        break;
                    }
                }
            }
        }
    }

    private void setSelectedSubscriptionList(String defaultValue) {
        ArrayList<Integer> ids = new ArrayList<>();
        if (defaultValue != null && !"".equals(defaultValue)) {
            for (String id : defaultValue.split(",")) {
                if (id != null && !"".equals(id) && id.matches(Constants.REGEX_INTEGER)) {
                    ids.add(Integer.valueOf(id));
                }
            }
            if (ids.size() > 0) {
                setSelectedSubscriptionList(ids);
            }
        }
    }

    public void setCheckBoxCellFieldUpdater(FieldUpdater<SelectItem, Boolean> fieldUpdater) {
        checkBoxCell.setFieldUpdater(fieldUpdater);
    }
}