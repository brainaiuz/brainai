package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.UpdateModeEnum;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Dilshod
 * Date: 10-Feb-2010
 * Time: 17:34:06
 */
public class OwnerPopup extends KpiModal {
    private ArrayList<Integer> accountIDs;
    private KpiSelect2 kpiMultiSelect;
    private KpiRadioButton overWriteRadio, addRadio;
    private WfmButton2 saveButton, cancelButton;
    private ListingFilterParameter filterParameter;
    private List<SelectItem> accountOwners;

    public OwnerPopup() {
        setTitle(wfmStrings.owners());
        this.addStyleName("file--OwnerPopup");
        init();
    }

    public OwnerPopup(List<SelectItem> accountOwners) {
        this();
        this.accountOwners = accountOwners;
    }

    private void init() {
        kpiMultiSelect = new KpiSelect2(true);
        kpiMultiSelect.addStyleName(Constants.DEFAULT_WIDTH);
        kpiMultiSelect.ensureDebugId("owners");

        overWriteRadio = new KpiRadioButton("checkbox", wfmStrings.overwrite());
        addRadio = new KpiRadioButton("checkbox", wfmStrings.add());
        addRadio.setValue(true);

        overWriteRadio.addValueChangeHandler(valueChangeEvent -> saveButton.setText(wfmStrings.overwrite()));
        addRadio.addValueChangeHandler(valueChangeEvent -> saveButton.setText(wfmStrings.add()));

        FlexTable container = new FlexTable();
        container.addStyleName("owner-panel");
        container.setWidget(0, 0, kpiMultiSelect);
        container.setWidget(1, 0, addRadio);
        container.setWidget(2, 0, overWriteRadio);
        add(container);

        setOwners();

        saveButton = new WfmButton2(addRadio.getValue()
                                    ? wfmStrings.add()
                                    : wfmStrings.overwrite(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent -> close());
        addButton(cancelButton);
        addButton(saveButton);
    }

    private void setOwners() {
        CRMService.App.get().getAccountOwnersList(new AsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> owners) {
                if (accountOwners == null) {
                    kpiMultiSelect.setItems(owners);
                } else {
                    for (SelectItem owner : owners) {
                        owner.setSelected(accountOwners.contains(owner));
                    }
                    kpiMultiSelect.setItems(owners);
                }
            }
        });
    }

    private void save() {
        kpiMultiSelect.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (kpiMultiSelect.getSelectedItem() == null) {
            kpiMultiSelect.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        LoadingPanel.loading(true);
        CRMService.App.get().changeAccountsOwners(getAccountIDs(), SelectItem.getIDs(kpiMultiSelect.getSelectedItems()),
                                                  (addRadio.getValue()
                                                   ? UpdateModeEnum.ADD
                                                   : UpdateModeEnum.OVER_WRITE),
                                                  filterParameter, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        saveButton.setEnabled(true);
                        cancelButton.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        LoadingPanel.loading(false);
                        saveButton.setEnabled(true);
                        cancelButton.setEnabled(true);
                        close();
                        Info.show(wfmStrings.ownerSuccessfullyUpdated(), Info.Type.INFO);
                        if (listRefresh != null) {
                            listRefresh.refreshList();
                        }
                    }
                });
    }

    public void addAccountID(Integer accountID) {
        getAccountIDs().add(accountID);
    }

    public ArrayList<Integer> getAccountIDs() {
        if (accountIDs == null) {
            accountIDs = new ArrayList<>();
        }
        return accountIDs;
    }


    public void setFilterParameter(ListingFilterParameter filterParameter) {
        this.filterParameter = filterParameter;
    }

    private ListRefresh listRefresh;

    public interface ListRefresh {
        void refreshList();
    }

    public void setListRefresh(ListRefresh listRefresh) {
        this.listRefresh = listRefresh;
    }

}
