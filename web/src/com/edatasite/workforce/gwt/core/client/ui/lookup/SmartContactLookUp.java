package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SmartContactLookUp extends LookUp {

    private Command linkCommand;
    private Integer supplierId;

    public SmartContactLookUp(Command linkCommand, Integer objectId) {
        this.supplierId = objectId;
        this.linkCommand = linkCommand;
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(true);
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        ClientService.App.get().getSupplierContacts(supplierId, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                clear();
                setItems(filterParametrs.getSearchKey(), selectItems);
                String searchKey = filterParametrs.getSearchKey() != null ? filterParametrs.getSearchKey() : "";
                getSuggestBox().showSuggestions(searchKey);

            }
        });
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }

    public Command getLinkCommand() {
        return linkCommand;
    }

    public void setLinkCommand(Command linkCommand) {
        this.linkCommand = linkCommand;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }
}
