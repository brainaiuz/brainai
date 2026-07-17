package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;

import static com.edatasite.workforce.gwt.contact.client.ui.AddContactView.crmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Apr 10, 2010
 * Time: 4:49:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountContactsGrid extends AbstractDataGrid<ContactListItem> implements Constants {

    private Integer crmAccountID;

    public AccountContactsGrid(Integer objectID) {
        super();
        this.crmAccountID = objectID;
        initialize();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_ADD, AccountContactsGrid.this, (sender, args) -> refresher());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_DELETE, AccountContactsGrid.this, (sender, args) -> refresher());
    }

    private void showAccessMessage(boolean accessEnable, boolean failure) {
        WfmMessageBox messageBox = new WfmMessageBox(failure ? IconEnum.ERROR : IconEnum.INFO, Action.OK,
                accessEnable ? (failure ? crmStrings.errorOccuredWhileEnablingAccess() : wfmStrings.accessEnabledSucc()) : (failure ? crmStrings.errorOccuredWhileDisablingAccess() : crmStrings.accessDisabledSucc())) {

        };
        messageBox.setTitle(failure ? wfmStrings.failed() : wfmStrings.success());
        messageBox.open();
    }
    @Override
    protected void addColums() {
        //Contact Name
        Column<ContactListItem, String> contactName = new Column<ContactListItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(ContactListItem item) {
                return item.getName();
            }
        };
        contactName.setFieldUpdater((index, item, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + item.getObjectId()));
        addColumn(contactName, Property.get(Constants.Contacts, wfmStrings.contactName(), wfmStrings.contact()));
        setColumnWidth(contactName, 240, com.google.gwt.dom.client.Style.Unit.PX);
        //Job Title
        addColumn(new Column<ContactListItem, String>(new TextCell()) {
            @Override
            public String getValue(ContactListItem item) {
                return item.getJobTitle();
            }
        }, wfmStrings.position());
        //Email
        SimpleLinkCell emailLink = new SimpleLinkCell();
        emailLink.setStyleName("txt-elem--ellipsis");
        Column<ContactListItem, String> email = new Column<ContactListItem, String>(emailLink) {
            @Override
            public String getValue(ContactListItem item) {
                return item.getPrimaryEmail();
            }
        };
        //email.setFieldUpdater((index, item, value) -> new ComposeView(item.getPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, item.getObjectId(), item.getName())));
        email.setFieldUpdater((index, item, value) -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getPrimaryEmail() + "/" + RelationItem.TYPE_CONTACT + "/" + item.getObjectId() + "/" + item.getName());
        });
        addColumn(email, wfmStrings.email());
        setColumnWidth(email, 200, com.google.gwt.dom.client.Style.Unit.PX);
        //Phone
        Column<ContactListItem, SafeHtml> phone = new Column<ContactListItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ContactListItem item) {
                return (SafeHtml) () -> Utils.getPhoneCallFormat2(item.getPrimaryPhone()).toString();
            }
        };
        addColumn(phone, wfmStrings.phone());
        setColumnWidth(phone, "250px");
        // Access
        Column<ContactListItem, String> access = new Column<ContactListItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(ContactListItem item) {
                String accessLinkName = "";
                if ("Pending".equals(item.getAccessStatus()) && item.isAccessEnabled()) {
                    accessLinkName = wfmStrings.pending();
                } else if (item.isAccessEnabled()) {
                    accessLinkName = wfmStrings.deactivate();
                } else if (!item.isAccessEnabled()) {
                    accessLinkName = wfmStrings.activate();
                }
                return accessLinkName;
            }
        };
        access.setFieldUpdater((index, item, value) -> {
            if (!item.isLeadContact() && (item.getCrmAccount().hasCustomerType() || item.getCrmAccount().hasSupplierType())) {

                if (item.getPrimaryEmail() == null || "".equals(item.getPrimaryEmail().trim())) {
                    Window.alert(crmStrings.thereisnoEmailAddress());
                    return;
                }

                WfmMessageBox activeMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                activeMessageBox.setTitle(wfmStrings.warning());
                String message = "";
                if (item.isAccessEnabled()) {
                    message = wfmStrings.doYouWantToDeactivateCustomerAccess();
                } else {
                    message = wfmStrings.doYouWantToGiveAccessToKpi();
                }
                activeMessageBox.setMessage(message);
                activeMessageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        if (item.isAccessEnabled()) {
                            ContactService.App.get().enableAccess(item.getObjectId(), false, new AbstractAsyncCallback<Integer>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    showAccessMessage(false, true);
                                }

                                public void success(Integer integer) {
                                    LoadingPanel.loading(false);
                                    item.setAccessEnabled(false);
                                    showAccessMessage(false, false);
                                    refresher();
                                }
                            });
                        } else {
                            ContactService.App.get().enableAccess(item.getObjectId(), true, new AbstractAsyncCallback<Integer>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    showAccessMessage(true, true);
                                }

                                public void success(Integer clientContactID) {
                                    LoadingPanel.loading(false);
                                    boolean userWithThisEmailExists = (clientContactID != null && clientContactID == -1);
                                    if (userWithThisEmailExists) {
                                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK,
                                                wfmStrings.userAlreadyExists()) {

                                        };
                                        messageBox.setTitle(wfmStrings.success());
                                        messageBox.open();
                                    } else {
                                        item.setAccessEnabled(true);
                                        showAccessMessage(true, false);
                                        refresher();
                                    }
                                }
                            });
                        }
                    }
                });
                activeMessageBox.open();
            } else {

                Info.show(wfmStrings.accountTypeShouldBeCustomerOrSupplier(), Info.Type.WARNING);
                return;

            }

        });
        if (Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_CONTACT_ACCESS)) {
            addColumn(access, wfmStrings.access());
            setColumnWidth(access, 130, com.google.gwt.dom.client.Style.Unit.PX);
        }
        //Primary Contact
        Column<ContactListItem, String> primary = new Column<ContactListItem, String>(new TextCell()) {
            @Override
            public String getValue(ContactListItem item) {
                return item.isPrimaryContact() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        addColumn(primary, wfmStrings.primary());
        setColumnWidth(primary, "60px");
    }

    @Override
    public void refresher() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setAccountID(crmAccountID);
        LoadingPanel.loading(true);
        fp.setLimit(GRID_LIMIT);
        ContactService.App.get().getNewContactList(fp, new AbstractAsyncCallback<ListResult<ContactListItem>>() {
            public void failure(Throwable caught) {
            }

            public void success(final ListResult<ContactListItem> list) {
                DeferredCommand.addCommand(() -> {
                    supplyProvider(list.getList().toArray(new ContactListItem[]{}));
                    reDrawItems();
                    LoadingPanel.loading(false);
                });
            }
        });
    }
}