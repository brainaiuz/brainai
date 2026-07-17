package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceAsync;
import com.edatasite.workforce.gwt.client.client.rpc.ClientSupplierAddressData;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.CusSupAddress;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Sherzod
 * Date: 1/26/12
 * Time: 4:53 PM
 */
public class CrmAccountWidgetsPresenter {
    public interface CrmAccountWidgetsInterface {

        boolean isEditForm();

        LookUp getCrmAccLookUp();

        LookUp getDropShipToCustomerLookUp();

        CusSupAddress billAddress();

        CusSupAddress mailAddress();

        String getType();

        String getFormType();

        String getAddActionLink();

        String getEditActionLink();

        CurrencyWidget getCurrencyWidget();
    }

    private ClientServiceAsync clientService = ClientService.App.get();
    private InvoiceServiceAsync invoiceService = InvoiceService.App.get();

    private CrmAccountWidgetsInterface viewInterface;
    Integer maillingAddressID;
    boolean isConvertInvoice = false;

    public CrmAccountWidgetsPresenter(CrmAccountWidgetsInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    public void bindUI() {
        final ExtendedCommand billingCommand = new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                if (id != null) {
                    TypeItem item = new TypeItem();
                    item.setId(viewInterface.getCrmAccLookUp().getSelectedItemID());
                    item.setBillAddressID(id);
                    initContactAddress(item, false, Address.EntityType.CrmAccount);
                }
            }

            @Override
            public void execute(Integer id1, Integer id2) {
                if (id1 != null && id2 != null) {
                    TypeItem item = new TypeItem();
                    item.setId(viewInterface.getCrmAccLookUp().getSelectedItemID());
                    item.setBillAddressID(id1);
                    item.setMailAddressID(id2);
                    initContactAddress(item, false, Address.EntityType.CrmAccount);
                }
            }
        };
        final ExtendedCommand mailingCommand = new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                if (id != null) {
                    TypeItem item = new TypeItem();
                    item.setId(viewInterface.getCrmAccLookUp().getSelectedItemID());
                    item.setMailAddressID(id);
                    initContactAddress(item, false, Address.EntityType.CrmAccount);
                }
            }

            @Override
            public void execute(Integer id1, Integer id2) {
                if (id1 != null && id2 != null) {
                    TypeItem item = new TypeItem();
                    item.setId(viewInterface.getCrmAccLookUp().getSelectedItemID());
                    item.setMailAddressID(id1);
                    item.setBillAddressID(id2);
                    initContactAddress(item, false, Address.EntityType.CrmAccount);
                }
            }
        };
        final ExtendedCommand companymailingCommand = new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                if (id != null) {
                    TypeItem item = new TypeItem();
                    item.setMailAddressID(id);
                    initContactAddress(item, false, Address.EntityType.Company);
                }
            }

            @Override
            public void execute(Integer id1, Integer id2) {
                if (id1 != null && id2 != null) {
                    TypeItem item = new TypeItem();
                    item.setMailAddressID(id1);
                    item.setBillAddressID(id2);
                    initContactAddress(item, false, Address.EntityType.Company);
                }
            }
        };

        final ExtendedCommand dropShipToMailingCommand = new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                loadDropShipToAddressData(viewInterface.getDropShipToCustomerLookUp().getSelectedItemID(), id);
            }
        };

        final boolean isClient = Constants.RECEIVABLE.equals(viewInterface.getType());

        viewInterface.billAddress().getAddAddressLink().addClickHandler(clickEvent -> new AddressAddEditView(viewInterface.getCrmAccLookUp().getSelectedItemID(), null, isClient, true, billingCommand));
        viewInterface.billAddress().getEditAddressLink().addClickHandler(clickEvent -> new AddressAddEditView(viewInterface.getCrmAccLookUp().getSelectedItemID(), viewInterface.billAddress().getAddressList().getSelectedId(), isClient, true, billingCommand));

        viewInterface.mailAddress().getAddAddressLink().addClickHandler(clickEvent -> {

            if (isClient) {
                new AddressAddEditView(viewInterface.getCrmAccLookUp().getSelectedItemID(), null, isClient, false, mailingCommand);
            } else if (viewInterface.getDropShipToCustomerLookUp() != null && viewInterface.getDropShipToCustomerLookUp().getSelectedItemID() != null) {
                new AddressAddEditView(viewInterface.getDropShipToCustomerLookUp().getSelectedItemID(), null, isClient, false, dropShipToMailingCommand);
            } else {
                new AddressAddEditView(true, null, isClient, false, companymailingCommand);
            }
        });

        viewInterface.mailAddress().getEditAddressLink().addClickHandler(clickEvent -> {

            if (isClient) {
                new AddressAddEditView(viewInterface.getCrmAccLookUp().getSelectedItemID(), viewInterface.mailAddress().getAddressList().getSelectedId(), isClient, false, mailingCommand);
            } else if (viewInterface.getDropShipToCustomerLookUp() != null && viewInterface.getDropShipToCustomerLookUp().getSelectedItemID() != null) {
                new AddressAddEditView(viewInterface.getDropShipToCustomerLookUp().getSelectedItemID(), viewInterface.mailAddress().getAddressList().getSelectedId(), isClient, false, dropShipToMailingCommand);
            } else {
                new AddressAddEditView(true, viewInterface.mailAddress().getAddressList().getSelectedId(), isClient, false, companymailingCommand);
            }
        });


        viewInterface.getCrmAccLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            final boolean setCurrency = true;

            if (Constants.PAYABLE.equals(viewInterface.getType())
                    && Constants.PURCHASE_INVOICE.equals(viewInterface.getFormType())
                    && !viewInterface.isEditForm()
                    && viewInterface.getCrmAccLookUp().getSelectedItemID() != null) {

                invoiceService.getPurchaseOrderBySupplier(viewInterface.getCrmAccLookUp().getSelectedItemID(), new AbstractAsyncCallback<NewInvoice[]>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(NewInvoice[] result) {
                        if (result != null && result.length > 0) {
                            Command listener = () -> {
                                TypeItem item = new TypeItem();
                                item.setId(viewInterface.getCrmAccLookUp().getSelectedItemID());
                                selectionHandler(item, setCurrency);
                            };
                            if (!viewInterface.getCrmAccLookUp().islink()) {
                                new SupplierPODialog().alertMessage(result, listener);
                            }
                        } else {
                            TypeItem item = new TypeItem();
                            item.setId(viewInterface.getCrmAccLookUp().getSelectedItemID());
                            selectionHandler(item, setCurrency);
                        }
                    }
                });
            } else {
                TypeItem item = new TypeItem();
                item.setId(viewInterface.getCrmAccLookUp().getSelectedItemID());
                selectionHandler(item, setCurrency);
            }
        });

        if (viewInterface.getDropShipToCustomerLookUp() != null) {
            viewInterface.getDropShipToCustomerLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                final Integer dropShipToCustomerID = viewInterface.getDropShipToCustomerLookUp().getSelectedItemID();
                loadDropShipToAddressData(dropShipToCustomerID, null);
            });
            final SelectItem[] zeroSelectItems = new SelectItem[0];
            viewInterface.getDropShipToCustomerLookUp().getSuggestBox().addKeyUpHandler(event -> {
                final Integer dropShipToCustomerID = viewInterface.getDropShipToCustomerLookUp().getSelectedItemID();
                onDropShipToCustomerChange(dropShipToCustomerID, zeroSelectItems, null);
            });
        }

        viewInterface.billAddress().getAddressList().addValueChangeHandler(changeEvent -> {
            if (viewInterface.billAddress().getAddressList().isSomethingSelected()) {
                viewInterface.billAddress().setAddressDescription(viewInterface.billAddress().getAddressList().getSelectedItem().getDescription());
            } else {
                viewInterface.billAddress().setAddressDescription("");
            }
        });
        viewInterface.mailAddress().getAddressList().addValueChangeHandler(changeEvent -> {
            if (viewInterface.mailAddress().getAddressList().isSomethingSelected()) {
                viewInterface.mailAddress().setAddressDescription(viewInterface.mailAddress().getAddressList().getSelectedItem().getDescription());
            } else {
                viewInterface.mailAddress().setAddressDescription("");
            }
        });
    }

    public void loadDropShipToAddressData(final Integer dropShipToCustomerID, final Integer dropShipToMailAddressID) {
        clientService.getAddressData(dropShipToCustomerID, true, Address.EntityType.CrmAccount, new AsyncCallback<ClientSupplierAddressData>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(ClientSupplierAddressData result) {
                onDropShipToCustomerChange(dropShipToCustomerID, result.getMailAddresses(), dropShipToMailAddressID);
            }
        });
    }

    private void onDropShipToCustomerChange(Integer dropShipToCustomerID, SelectItem[] selectItems, Integer dropShipToMailAddressID) {
        viewInterface.mailAddress().getAddressList().setSelectedNullLabel();
        viewInterface.mailAddress().getAddressList().setItems(selectItems);
        viewInterface.mailAddress().getAddAddressLink().setVisible(true);

        if (dropShipToCustomerID != null) {

            if (dropShipToMailAddressID != null && viewInterface.mailAddress().getAddressList().getItemsById().containsKey(dropShipToMailAddressID)) {
                viewInterface.mailAddress().getAddressList().setSelected(dropShipToMailAddressID);
                viewInterface.mailAddress().getEditAddressLink().setVisible(true);
                viewInterface.mailAddress().setAddressDescription(viewInterface.mailAddress().getAddressList().getSelectedItem().getDescription());
            } else {
                viewInterface.mailAddress().setAddressDescription("");
            }
        } else {
            viewInterface.mailAddress().getAddressList().setItems(companyMailingAddresses);
            defaultSelectMailAddress(companyMailingAddresses);
        }
    }

    private SelectItem[] companyMailingAddresses = null;

    public void setShippingAddress(SelectItem[] selectItems) {
        this.companyMailingAddresses = selectItems;

        viewInterface.mailAddress().setVisible(true);
        viewInterface.mailAddress().getAddAddressLink().setVisible(true);
        viewInterface.mailAddress().getEditAddressLink().setVisible(true);

        viewInterface.mailAddress().getAddressList().setSelectedNullLabel();
        viewInterface.mailAddress().getAddressList().setItems(selectItems);
        viewInterface.mailAddress().setAddressDescription("");
        defaultSelectMailAddress(selectItems);
    }

    private void defaultSelectMailAddress(SelectItem[] selectItems) {
        for (SelectItem selectItem : selectItems) {

            if (selectItem.isSelected() || selectItems.length < 2) {
                viewInterface.mailAddress().getAddressList().setSelected(selectItem.getId());
                viewInterface.mailAddress().setAddressDescription(selectItem.getDescription());
            }
        }
    }

    public void initContactAddress(final TypeItem item, final boolean setCurrency, Integer maillingAddressID) {
        this.maillingAddressID = maillingAddressID;
        isConvertInvoice = true;
        initContactAddress(item, setCurrency, Address.EntityType.CrmAccount);
    }

    public void initContactAddress(final TypeItem item, final boolean setCurrency, final Address.EntityType entityType) {
        if (item.getId() != null) {
            viewInterface.billAddress().setVisible(true);
            viewInterface.billAddress().getAddAddressLink().setVisible(true);

            if (Constants.RECEIVABLE.equals(viewInterface.getType())) {
                viewInterface.mailAddress().setVisible(true);
                viewInterface.mailAddress().getAddAddressLink().setVisible(true);
            }
        }
//        LoadingPanel.loading(true);
        final Integer billAddressID = viewInterface.billAddress().getAddressList().getSelectedId();
        final Integer mailAddressID = viewInterface.mailAddress().getAddressList().getSelectedId();

        clientService.getAddressData(item.getId(), Constants.RECEIVABLE.equals(viewInterface.getType()), entityType, new AbstractAsyncCallback<ClientSupplierAddressData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ClientSupplierAddressData data) {
                LoadingPanel.loading(false);

                viewInterface.billAddress().setAddressDescription("");

                if (Constants.RECEIVABLE.equals(viewInterface.getType())) {
                    viewInterface.mailAddress().setAddressDescription("");
                }

                if (data.getBillAddresses() != null) {
                    viewInterface.billAddress().getAddressList().setItems(data.getBillAddresses());

                    if (item.getBillAddressID() != null) {
                        viewInterface.billAddress().getAddressList().setSelected(item.getBillAddressID());
                    } else if (billAddressID != null && viewInterface.billAddress().getAddressList().getItemsById() != null
                            && viewInterface.billAddress().getAddressList().getItemsById().containsKey(billAddressID)) {
                        viewInterface.billAddress().getAddressList().setSelected(billAddressID);
                    } else if (data.getPrimaryBillAddressID() != null) {
                        viewInterface.billAddress().getAddressList().setSelected(data.getPrimaryBillAddressID());
                    }
                    if (viewInterface.billAddress().getAddressList().isSomethingSelected()) {
                        viewInterface.billAddress().getEditAddressLink().setVisible(true);
                        viewInterface.billAddress().setAddressDescription(viewInterface.billAddress().getAddressList().getSelectedItem().getDescription());
                    }
                }

                if (Constants.RECEIVABLE.equals(viewInterface.getType()) && data.getMailAddresses() != null
                        && (!isConvertInvoice || (isConvertInvoice && maillingAddressID != null))
                        || Address.EntityType.Company.equals(entityType) && data.getMailAddresses() != null) {

                    viewInterface.mailAddress().getAddressList().setItems(data.getMailAddresses());

                    if (item.getMailAddressID() != null) {
                        viewInterface.mailAddress().getAddressList().setSelected(item.getMailAddressID());
                    } else if (mailAddressID != null && viewInterface.mailAddress().getAddressList().getItemsById() != null
                            && viewInterface.mailAddress().getAddressList().getItemsById().containsKey(mailAddressID)) {
                        viewInterface.mailAddress().getAddressList().setSelected(mailAddressID);
                    } else if (data.getPrimaryMailAddressID() != null) {
                        viewInterface.mailAddress().getAddressList().setSelected(data.getPrimaryMailAddressID());
                    }
                    if (viewInterface.mailAddress().getAddressList().isSomethingSelected()) {
                        viewInterface.mailAddress().getEditAddressLink().setVisible(true);
                        viewInterface.mailAddress().setAddressDescription(viewInterface.mailAddress().getAddressList().getSelectedItem().getDescription());
                    }
                }

                if (viewInterface.getCurrencyWidget() != null && setCurrency) {

                    if (data.getCurrencyID() != null) {
                        viewInterface.getCurrencyWidget().setCurrency(data.getCurrencyID());
                    }
                }
            }
        });
    }

    public void setCustomerMailAddressData(Integer mailAddressID, TypeItem dropShipToCustomerItem) {
        maillingAddressID = mailAddressID;
        if (dropShipToCustomerItem != null) {
            viewInterface.getDropShipToCustomerLookUp().addItem(dropShipToCustomerItem);
            loadDropShipToAddressData(dropShipToCustomerItem.getId(), dropShipToCustomerItem.getMailAddressID());
        } else {
            viewInterface.mailAddress().getAddressList().setSelected(mailAddressID);
            if (viewInterface.mailAddress().getAddressList().getSelectedItem() != null)
                viewInterface.mailAddress().setAddressDescription(viewInterface.mailAddress().getAddressList().getSelectedItem().getDescription());
        }
    }

    public void selectionHandler(TypeItem item, boolean setCurrency) {
        if (item.getId() != null) {
            initContactAddress(item, setCurrency, Address.EntityType.CrmAccount);
        } else {
            viewInterface.billAddress().getAddressList().setSelectedNullLabel();
            viewInterface.mailAddress().getAddressList().setSelectedNullLabel();

            LoadingPanel.loading(false);
        }
    }

    public void setEditValues(TypeItem item) {
        if (item.getId() != null && item.getId().equals(viewInterface.getCrmAccLookUp().getSelectedItemID())) {
            initContactAddress(item, true, Address.EntityType.CrmAccount);
        }
    }

    public void setEditValues(TypeItem item, boolean setCurrency) {
        if (item.getId() != null && item.getId().equals(viewInterface.getCrmAccLookUp().getSelectedItemID())) {
            initContactAddress(item, setCurrency, Address.EntityType.CrmAccount);
        }
    }

    public void setAddValues(TypeItem item, boolean setCurrency) {
        if (item.getId() != null) {
            viewInterface.getCrmAccLookUp().setSelected(item);
            initContactAddress(item, setCurrency, Address.EntityType.CrmAccount);
        }
    }

    public void clearPanel() {
        viewInterface.billAddress().getAddressList().clear();
        viewInterface.mailAddress().getAddressList().clear();

        viewInterface.billAddress().setAddressDescription("");
        viewInterface.mailAddress().setAddressDescription("");

    }
}