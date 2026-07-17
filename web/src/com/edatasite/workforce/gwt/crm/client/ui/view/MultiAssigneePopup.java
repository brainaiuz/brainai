/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/4 3:18:46                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 10-Feb-2010
 * Time: 17:34:06
 * To change this template use File | Settings | File Templates.
 */
public class MultiAssigneePopup extends KpiModal {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final String WIDTH_MIN = "70px";
    public ListingFilterParameter fp = new ListingFilterParameter();
    public boolean allTableItems;
    private ArrayList<Integer> itemIDs;
    private ArrayList<SelectItem> selectItems;
    private DataListBox assignee;
    private TextBox assignCount;
    private TextBox splitPerCountText;
    private TextBox splitLimitText;
    private FlexTable table;
    private KpiRadioButton splitEqualy;
    private KpiRadioButton splitPerCount;
    private WfmButton2 save,cancel,applyButton;
    private Integer dropdownCount = 0;
    private Integer maxRowCount = 0;
    ArrayList<Widget> errorWidgets = new ArrayList<>();

    public MultiAssigneePopup() {
        setTitle(wfmStrings.assignee());
        setWidth(500);
        init();
    }

    private void init() {
        table = new FlexTable();
        table.setCellPadding(5);
        table.setCellSpacing(5);

        splitEqualy = new KpiRadioButton("Split");
        splitPerCount = new KpiRadioButton("Split");

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> saveAssignee());
        save.ensureDebugId("save_button");
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> close());
        cancel.ensureDebugId("cancel_button");

        add(table);
        addButton(cancel);
        addButton(save);
    }

    private void saveAssignee() {
        int existIndex = setItemsToData();
        if (existIndex > 0) {
            DataListBox existElement = ((DataListBox) table.getWidget(existIndex, 0));
            existElement.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(existElement.getSelectedItem().getName() + " is already selected.", Info.Type.WARNING);
            return;
        }
        save.setEnabled(false);
        cancel.setEnabled(false);
        LoadingPanel.loading(true);
        CRMService.App.get().saveLeadMultiAssignee(allTableItems, getItemIDs(), fp, selectItems, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                saved(false);
            }

            public void success(Void result) {
                saved(true);
            }
        });
    }

    private void saved(boolean success) {
        LoadingPanel.loading(false);
        save.setEnabled(true);
        cancel.setEnabled(true);
        if (success) {
            if (listRefresh != null) {
                listRefresh.refreshList();
            }
            close();
        }
    }

    private int setItemsToData() {
        clearErrorStyle();
        ArrayList<Integer> selectedIDs = new ArrayList<>();
        selectItems = new ArrayList<>();
        for (int i = 2; i < table.getRowCount(); i++) {
            SelectItem itemTO = new SelectItem();
            SelectItem selectedItem = ((DataListBox) table.getWidget(i, 0)).getSelectedItem();
            if (selectedItem == null || ((TextBox) table.getWidget(i, 1)).getText().isEmpty()) {
                continue;
            }
            if (selectedIDs.contains(selectedItem.getId())) {
                errorWidgets.add(table.getWidget(i, 0));
                return i;
            } else {
                selectedIDs.add(selectedItem.getId());
            }
            itemTO.setId(selectedItem.getId());
            itemTO.setName(selectedItem.getName());
            itemTO.setDescription(((TextBox) table.getWidget(i, 1)).getText());
            selectItems.add(itemTO);
        }
        return 0;
    }

    private void clearErrorStyle() {
        if (errorWidgets != null && errorWidgets.size() > 0) {
            for (Widget widget : errorWidgets) {
                widget.removeStyleName(Constants.ERROR_FORM_STYLE);
            }
        }
    }

    public ArrayList<Integer> getItemIDs() {
        if (itemIDs == null) {
            itemIDs = new ArrayList<>();
        }
        return itemIDs;
    }

    public void setDataItems(int contactListItemCount, HashSet<ContactListItem> selectedItems, SelectItem[] items) {
        maxRowCount = items.length > 5 ? 5 : items.length;
        dropdownCount = maxRowCount;
        final Integer itemsCount = allTableItems ? contactListItemCount : selectedItems.size();
        for (ContactListItem item : selectedItems) {
            getItemIDs().add(item.getObjectId());
        }
        HorizontalPanel hp = new HorizontalPanel();
        HTML split = new HTML(wfmStrings.split());
        hp.add(split);

        splitLimitText = new TextBox();
        splitLimitText.setWidth("100px");
        Validation.addPhoneNumberKeyboardListener(splitLimitText);
        splitLimitText.setText(String.valueOf(itemsCount));
        hp.add(splitLimitText);
        HTML count = new HTML(wfmStrings.of() + itemsCount + wfmStrings.lead());
        hp.add(count);
        hp.setCellVerticalAlignment(split, HasVerticalAlignment.ALIGN_MIDDLE);
        hp.setCellVerticalAlignment(count, HasVerticalAlignment.ALIGN_MIDDLE);
        table.setWidget(0, 0, hp);

        splitEqualy.setText(crmStrings.splitEqually());
        splitPerCount.setText(wfmStrings.splitby());
        splitEqualy.setValue(false);
        splitEqualy.setValue(true);

        HorizontalPanel rbPanel = new HorizontalPanel();
        rbPanel.add(splitEqualy);
        rbPanel.add(splitPerCount);
        table.setWidget(1, 0, rbPanel);

        splitPerCountText = new TextBox();
        splitPerCountText.setWidth(WIDTH_MIN);
        Validation.addPhoneNumberKeyboardListener(splitPerCountText);
        applyButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SECONDARY, clickEvent -> {
            clearTable();
            int allAsssignCount = Integer.parseInt(splitLimitText.getText());
            if (Integer.parseInt(splitLimitText.getText()) > itemsCount) {
                splitLimitText.setText(String.valueOf(itemsCount));
                allAsssignCount = itemsCount;
            }
            int settedValue = Integer.parseInt(splitPerCountText.getText());
            for (int i = 2; i < table.getRowCount(); i++) {
                if (allAsssignCount <= settedValue) {
                    ((TextBox) table.getWidget(i, 1)).setText(String.valueOf(allAsssignCount));
                    break;
                } else {
                    ((TextBox) table.getWidget(i, 1)).setText(String.valueOf(settedValue));
                    allAsssignCount = allAsssignCount - settedValue;
                }
            }
        });
        table.setWidget(1, 1, splitPerCountText);
        table.setWidget(1, 2, applyButton);
        splitEqualy.addClickHandler(clickEvent -> {
            if (Integer.parseInt(splitLimitText.getText()) > itemsCount) {
                splitLimitText.setText(String.valueOf(itemsCount));
            }
            spreadAssings(Integer.parseInt(splitLimitText.getText()));

        });
        splitPerCount.addClickHandler(clickEvent -> {
            applyButton.setEnabled(true);
            splitPerCountText.setEnabled(true);
            clearTable();
        });
        createRowData(items);
        spreadAssings(itemsCount);
    }

    private void spreadAssings(Integer itemsCount) {
        clearTable();
        applyButton.setEnabled(false);
        splitPerCountText.setEnabled(false);
        int butunqiymat = itemsCount / dropdownCount;
        int qoldiqiymat = itemsCount % dropdownCount;
        for (int i = 2; i < table.getRowCount(); i++) {
            int textValue = butunqiymat;
            if (qoldiqiymat > 0) {
                textValue = textValue + 1;
                qoldiqiymat--;
            }
            ((TextBox) table.getWidget(i, 1)).setText(String.valueOf(textValue));
        }
    }

    private void clearTable() {
        for (int i = 2; i < table.getRowCount(); i++) {
            ((TextBox) table.getWidget(i, 1)).setText("");
        }
    }

    private void createRowData(SelectItem[] items) {
        int t = 2;
        for (int i = 0; i < dropdownCount; i++) {
            createRowDataSingle(t, items);
            t++;
        }
    }

    private void createRowDataSingle(int t, final SelectItem[] items) {
        assignee = new DataListBox();
        assignee.addStyleName(Constants.DEFAULT_WIDTH);
        assignee.setItems(items);
        table.setWidget(t, 0, assignee);
        assignCount = new TextBox();
        assignCount.setWidth(WIDTH_MIN);
        Validation.addPhoneNumberKeyboardListener(assignCount);
        table.setWidget(t, 1, assignCount);
        Label labelAddRow = new Label();
        labelAddRow.setTextAsHtml("<b style=\"cursor:pointer; color:blue; font-size:14px;margin-left:10px;\">+</b>");
        labelAddRow.addClickHandler(clickEvent -> {
            if (table.getRowCount() < maxRowCount + 2) {
                createRowDataSingle(table.getRowCount(), items);
                dropdownCount++;
            } else {
                Info.show(crmStrings.youCannotAdd() + " " + maxRowCount + " " + wfmStrings.row(), Info.Type.INFO);
            }
        });
        Label labelRemoveRow = new Label();
        labelRemoveRow.setTextAsHtml("<b style=\"cursor:pointer; color:red; font-size:17px;\"> - </b>");
        labelRemoveRow.addClickHandler(clickEvent -> {
            if (table.getRowCount() > 3) {
                table.removeRow(table.getCellForEvent(clickEvent).getRowIndex());
                dropdownCount--;
            } else {
                Info.show(crmStrings.youCannotRemoveTheLastRow(), Info.Type.INFO);
            }
        });
        HorizontalPanel plusMinusPanel = new HorizontalPanel();
        plusMinusPanel.add(labelAddRow);
        plusMinusPanel.add(labelRemoveRow);
        table.setWidget(t, 2, plusMinusPanel);
    }

    public void setDefaultItems(boolean hasCheckedAllTableItems, ListingFilterParameter filterParam) {
        this.allTableItems = hasCheckedAllTableItems;
        this.fp = filterParam;
    }

    private LeadListRefresh listRefresh;

    public interface LeadListRefresh {
        void refreshList();
    }

    public void setListRefresh(LeadListRefresh listRefresh) {
        this.listRefresh = listRefresh;
    }
}
