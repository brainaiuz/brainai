package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.AddressNewUIWidget;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.ContactCareerView;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.ContactStatusHistoryGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.ContactUpdatesGrid;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:39:36
 */
public class ViewContactForm extends AddContactView implements NoColapse {

    protected static final CRMServiceAsync crmService = CRMService.App.get();

    protected MaterialLink callLog;
    protected MaterialLink addEvent;
    protected MaterialLink addTask;
    protected MaterialLink addOpportunity;
    protected MaterialLink convert;
    protected MaterialDropDown statusMenuBar;
    protected NoteWidget noteWidget;
    protected CrmActivityGrid activityWidget;
    protected boolean fromCalendar;
    protected ContactStatusHistoryGrid statusHistoryGrid;

    private static final String googleRootUrl = "http://www.google.com/";
    private static final String commonParamForUrl = "menubar=no,location=no,resizable=yes,scrollbars=yes,status=yes,height=600,width=800";
    private static final String linkedinRootUrl = "http://www.linkedin.com/";
    private static final String facebookRootUrl = "http://www.facebook.com/";
    private static final String twitterRootUrl = "http://twitter.com/";

    private HTML categories;
    public HTML fullName;
    private HTML birthDOBWidget;
    public HTML companyName;
    public HTML industries;
    public HTML accountType;
    public HTML jobTitle;
    private HTML jobFunction;
    private HTML department;
    private HTML assets;
    public HTML campaignSource;
    public HTML emailOpt;
    public HTML contactOwner;
    private HTML reportsTo;
    private HTML refIndNumber;
    public FlowPanel phoneNumInf;
    public FlowPanel imsAddressInf;
    public FlowPanel webSiteInf;
    private FlowPanel relations;
    public FlowPanel emailInf;
    public FlowPanel telegramInf;
    public HTML mailListTable;
    public HTML createdDate;
    public HTML updatedDate;
    private ContactCareerView contactCareer;
    private boolean isContact = true;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private HTMLPanel exportPanel;

    public ViewContactForm(String name) {
        super(name);
        isContact = false;
    }

    public ViewContactForm(String name, String description) {
        super(name, description);
        isContact = false;
    }

    public ViewContactForm(Integer objectId) {
        super("addcontact");
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.contact()));
        this.objectId = objectId;
        viewName = property.getSingular(wfmStrings.editContact(), wfmStrings.contact());
    }

    public ViewContactForm(Integer objectID, boolean fromCalendar) {
        this(objectID);
        this.fromCalendar = fromCalendar;
        filterParametrs.setObjectId(objectId);
        filterParametrs.setViewType("contact");
        filterParametrs.setHasAccessToChange(hasEditPermission());
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ContactService.App.get().getContact(objectId, false, new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ContactListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;

                    profilePicture.initialize(o.getContactImageUrl(), o.getFirstName(), o.getLastName(), true);

                    if (!Utils.hasGenericAccess(GenericSettingsEnum.CONTACT_ENABLE_ACCESS_HIDE)) {
                        addAccessButton();
                    }
                    hideButtons();
                    setContactItem();

                    if (options.getItems() == null || options.getItems().size() == 0) {
                        options.getParent().getParent().removeFromParent();
                    }
                });
            }
        });
    }

    private MaterialDropDown options;

    private void hideButtons() {
        if (item != null && item.getPermissionForEntireUser() != null) {
            boolean canEdit = item.getPermissionForEntireUser().isWrite();
            if (!canEdit) {
                if (callLog != null && !Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                    callLog.removeFromParent();
                }
                if (addEvent != null && !Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
                    addEvent.removeFromParent();
                }
                if (addTask != null && !Utils.hasPermission(PermissionConstants.CRM_TASKS_ADD)) {
                    addTask.removeFromParent();
                }
                if (convert != null && !Utils.hasPermission(PermissionConstants.CRM_LEAD_CONVERT)) {
                    convert.removeFromParent();
                }
                //statusMenuBar.removeFromParent();
            }
        }
    }

    private MaterialLink enableOrDisableAccess = null;

    private void showAccessMessage(boolean accessEnable, boolean failure) {
        WfmMessageBox messageBox = new WfmMessageBox(failure ? IconEnum.ERROR : IconEnum.INFO, Action.OK,
                accessEnable ? (failure ? crmStrings.errorOccuredWhileEnablingAccess() : wfmStrings.accessEnabledSucc()) : (failure ? crmStrings.errorOccuredWhileDisablingAccess() : crmStrings.accessDisabledSucc())) {

        };
        messageBox.setTitle(failure ? wfmStrings.failed() : wfmStrings.success());
        messageBox.open();
    }

    private void addAccessButton() {
        if (!item.isLeadContact() && item.getCrmAccount() != null && !item.getCrmAccount().isDeleted() &&
                (item.getCrmAccount().hasCustomerType() || item.getCrmAccount().hasSupplierType()) &&
                (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR))) {
            enableOrDisableAccess = new MaterialLink(item.isAccessEnabled() ? crmStrings.disableAccess() : wfmStrings.enableAccess());
            enableOrDisableAccess.addClickHandler(event -> {
                if (item.getPrimaryEmail() == null || "".equals(item.getPrimaryEmail().trim())) {
                    Window.alert(crmStrings.thereisnoEmailAddress());
                    return;
                }
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
                            enableOrDisableAccess.setText(wfmStrings.enableAccess());
                            showAccessMessage(false, false);
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
                                enableOrDisableAccess.setText(crmStrings.disableAccess());
                                showAccessMessage(true, false);
                            }
                        }
                    });
                }
            });
            options.add(enableOrDisableAccess);
        }
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);
        options = addMoreSplitButton(wfmStrings.options());

        if (Utils.hasPermission(CRM_REMOVE_CONTACT, CRM_CONTACT_LOOK_UP)) {

            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }
            if (hasDeletePermission()) {
                MaterialLink delete = new MaterialLink(wfmStrings.delete());
                delete.addClickHandler(event -> onDeleteAction());
                options.add(delete);
            }
            if (Utils.hasPermission(PermissionConstants.CRM_CONTACT_LOOK_UP)) {
                MaterialLink lookUp = new MaterialLink(crmStrings.searchIn());
                lookUp.add(createSearchMenu());
                options.add(lookUp);
            }
        }

        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return isContact ? "/viewContactFormPDFHandler" : "/viewLeadFormPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(objectId);
                HashMap<String, String> parametrs = requestObject.getRequestParams();
                return parametrs;
            }
        });
        addRightButton(pdf);

        MaterialLink addButton = new MaterialLink(wfmStrings.add());
        MaterialSplitButton addSplitButton = new MaterialSplitButton(addButton, Constants.BTN_DEFAULT_OUTLINE);
        if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
            callLog = new MaterialLink(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
            callLog.ensureDebugId("callALog");
            callLog.addClickHandler(event -> {
                if (RelationItem.TYPE_CONTACT.equals(getRelationType())) {
                    new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(getRelationType(), objectId, item.getName()));
                } else {
                    new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(getRelationType(), objectId, item.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                }
            });
            addSplitButton.addItem(callLog);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
            addEvent = new MaterialLink(Property.get(Constants.EVENT_LIST, wfmStrings.event()));
            addEvent.ensureDebugId("addMess");
            addEvent.addClickHandler(event -> {
                if (RelationItem.TYPE_CONTACT.equals(getRelationType())) {
                    new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(getRelationType(), objectId, item.getName()));
                } else {
                    new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(getRelationType(), objectId, item.getName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                }
            });
            addSplitButton.addItem(addEvent);
        }

        if (isContact && Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            addOpportunity = new MaterialLink(wfmStrings.opportunity());
            addOpportunity.ensureDebugId("addOpportunity");
            addOpportunity.addClickHandler(event -> {
                if (Utils.hasPermission(CRM_QUICK_ADD_NEW_OPPORTUNITIES)) {
                    closeTab();
                    new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()),
                            RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : null, item.getCrmAccount() != null ? item.getCrmAccount().getName() : null));

                } else if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
                    Integer crmAccountId = null;
                    String crmAccountName = null;
                    if (item.getCrmAccount() != null) {
                        crmAccountId = item.getCrmAccount().getObjectId();
                        crmAccountName = item.getCrmAccount().getName();
                    }
                    closeTab();
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + crmAccountId + "/" + crmAccountName + "/" + RelationItem.TYPE_CONTACT + "/" + item.getObjectId() + "/" + item.getName(), item.getName(), item.getName());
                }
            });
            addSplitButton.addItem(addOpportunity);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_TASKS_ADD)) {
            addTask = new MaterialLink(wfmStrings.task());
            addTask.ensureDebugId("addTask");
            addTask.addClickHandler(event -> new TaskQuickAddView(RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()),
                    RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : null,
                            item.getCrmAccount() != null ? item.getCrmAccount().getName() : null)));
            addSplitButton.addItem(addTask);
        }
        addRightButton(addSplitButton);

        if (hasEditPermission()) {
            addEditButton().addClickHandler(event -> onEditAction());
        }

        MaterialLink send = new MaterialLink(wfmStrings.send());
        MaterialSplitButton sendButton = new MaterialSplitButton(send);

        if (Utils.hasPermission(ADD_CONTACT_SMS)) {
            MaterialLink addSms = new MaterialLink(wfmStrings.sms());
            addSms.addClickHandler(event -> {
                new ActivityQuickAddForm(Appointment.SMS, item, null, RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName()));
            });
            sendButton.addItem(addSms);
        }

        if (Utils.hasPermission(CRM_SALES_QUOTE_ADD)) {
            MaterialLink squote = new MaterialLink(Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()));
            squote.addClickHandler(event -> sendInvoiceOrQuote(false));
            sendButton.addItem(squote);
        }
        if (Utils.hasPermission(CRM_SALES_INVOICE_ADD)) {
            MaterialLink sinvoice = new MaterialLink(Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()));
            sinvoice.addClickHandler(event -> sendInvoiceOrQuote(true));
            sendButton.addItem(sinvoice);
        }

        addRightButton(sendButton);
    }

    protected void onEditAction() {
        if (item.getPermissionForEntireUser() != null && item.getPermissionForEntireUser().isWrite() || Utils.hasRole(Constants.ADMIN) || Utils.hasPermission(CRM_EDIT_CONTACT)) {
            closeTab();
            SinksContainerFactory.entryPoint.onHistoryChanged("contactedit|editcontact" + "/" + item.getObjectId(), item.getName(), item.getName());
        } else {
            Info.warn(wfmMessages.youDoNotHaveEnoughPermission(wfmStrings.edit(), property.getSingular(wfmStrings.contact())));
        }
    }

    protected void onDeleteAction() {
        if (item.getPermissionForEntireUser() != null && item.getPermissionForEntireUser().isDelete()) {
            deleteContactItem(item);
        } else {
            Info.warn(wfmMessages.youDoNotHaveEnoughPermission(wfmStrings.delete(), property.getSingular(wfmStrings.contact())));
        }
    }

    protected boolean hasEditPermission() {
        return Utils.hasPermission(PermissionConstants.CRM_EDIT_CONTACT) || (Utils.isAccounting() && Utils.hasPermission(ACCOUNTING_CONTACT_EDIT));
    }

    protected boolean hasDeletePermission() {
        return Utils.hasPermission(PermissionConstants.CRM_REMOVE_CONTACT);
    }

    public void sendInvoiceOrQuote(final boolean isInvoice) {
        if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            if (isInvoice ? (Utils.hasPermission(CRM_SALES_INVOICE_ADD)) : (Utils.hasPermission(CRM_SALES_QUOTE_ADD))) {
                if (item.getCrmAccount().isNew()) {
                    LoadingPanel.loading(true);
                    CRMService.App.get().addAccountToContact(item, true, new AbstractAsyncCallback<ContactListItem>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(ContactListItem result) {
                            LoadingPanel.loading(false);
                            item = result;
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_AUTO_ACCOUNT_ADDED, item.getCrmAccount().getObjectId(), ViewContactForm.this);
                            String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + (isInvoice ? Constants.SALE_INVOICE : Constants.SALE_QUOTE) + "|add/add/account/" + item.getCrmAccount().getObjectId() + "/" + item.getObjectId() + (item.isLeadContact() ? "/lead" : "");
                            Window.open(addSalesInvoice, "_blank", "");
                        }
                    });
                } else {
                    String addSalesInvoice = GWT.getHostPageBaseURL() + "Accounting.html#" + (isInvoice ? Constants.SALE_INVOICE : Constants.SALE_QUOTE) + "|add/add/account/" + item.getCrmAccount().getObjectId() + "/" + item.getObjectId() + (item.isLeadContact() ? "/lead" : "");
                    Window.open(addSalesInvoice, "_blank", "");
                }
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        } else {
            showIsAccountingSetUpMessage();
        }
    }

    private void showIsAccountingSetUpMessage() {
        final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK,
                wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), new CloseHandler() {
            @Override
            public void onSubmit() {

            }
        });
        wfmMessageBox.setTitle(wfmStrings.information());
        wfmMessageBox.open();
    }


    protected void deleteContactItem(final ContactListItem item) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
        final KpiCheckBox googleCheckBox = new KpiCheckBox(property.getPlural(crmStrings.mesContactfromGoogle(), wfmStrings.contacts()), true);
        if (item.isHasToken() && item.getGoogleId() != null) {
            messageBox.add(googleCheckBox);
        }
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                deleteContact(item, googleCheckBox.getValue());
            }
        });
        messageBox.open();
    }

    private void deleteContact(final ContactListItem item, boolean deleteFromGoogle) {
        final ArrayList<Integer> contactIDs = new ArrayList<>();
        contactIDs.add(item.getObjectId());
        LoadingPanel.loading(true);
        ContactService.App.get().deleteContacts(contactIDs,
                item.getOwnerId(), deleteFromGoogle, new AbstractAsyncCallback<ArrayList<Integer>>() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(ArrayList<Integer> result) {
                        LoadingPanel.loading(false);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_DELETE, result, ViewContactForm.this);
                        if (item.isCandidate()) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CANDIDATE_DELETE, result, ViewContactForm.this);
                        } else if (isLead()) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEADS_DELETE, result, ViewContactForm.this);
                        }
                        showContactsUpdatedMessageBox(result, true, true, item.isCandidate(), contactIDs.size() == result.size(), 3);
                        closeTab();
                    }
                });
    }

    private void showContactsUpdatedMessageBox(ArrayList<Integer> result, boolean onlyOne, boolean deleting, boolean isCandidate, boolean noOneDeleted, Integer action) {
        String message = "";
        if (noOneDeleted || (result != null && result.size() > 0 && onlyOne)) {
            if (action == 1) {
                message = property.getSingular(wfmStrings.copiedSuccessfully(), wfmStrings.contact());
            } else if (action == 2) {
                message = property.getSingular(wfmStrings.movedSuccessfully(), wfmStrings.contact());
            } else {
                message = property.getPlural(crmStrings.cannotDeleteContactsDueToPermissions(), wfmStrings.contacts());
            }
        } else if (result != null && result.size() > 0) {
            message = crmMessages.successfullyDeletedButSomeNot(crmStrings.removed(), "" + result.size());
        } else {
            String leads = Property.getPluralWithObjectCode(Constants.LEADS, wfmStrings.leads()).toLowerCase();
            String contacts = property.getPlural(wfmStrings.contacts()).toLowerCase();
            String s = isLead() ? leads : contacts;
            message = deleting ? (isCandidate ? wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.candidate().toLowerCase()) : crmMessages.messContactsSucDeleted(s)) :
                    property.getPlural(wfmStrings.messSuccessfullyUpdated(), wfmStrings.contacts());
        }
        Info.show(message, Info.Type.INFO);
    }

    protected String getRelationType() {
        return item == null || !item.isLeadContact() ? RelationItem.TYPE_CONTACT : RelationItem.TYPE_LEAD;
    }


    protected MaterialDropDown createSearchMenu() {
        MaterialDropDown items = new MaterialDropDown();
        items.setHover(true);
        items.setBelowOrigin(true);
        //Google Search
        MaterialLink gs = new MaterialLink("Google");
        gs.ensureDebugId("google_search");
        gs.addClickHandler(event -> Window.open(googleRootUrl + "search?hl=en&site=&q=" + item.getName() + "&btnG=Search", "_blank", commonParamForUrl));
        items.add(gs);
        //Google News
        MaterialLink googleNews = new MaterialLink("Google News");
        googleNews.ensureDebugId("google_news");
        googleNews.addClickHandler(event -> Window.open(googleRootUrl + "news?q=" + item.getName() + "&btnG=Search+News", "_blank", commonParamForUrl));
        items.add(googleNews);
        //Google Maps
        MaterialLink googleMaps = new MaterialLink("Google Maps");
        googleMaps.addClickHandler(event -> {
            Address addr = null;
            String address = "";
            for (Address add : item.getAddresses()) {
                if (add.isPrimary()) {
                    addr = add;
                }
            }
            if (addr == null && item.getAddresses().size() > 0) {
                addr = item.getAddresses().get(0);
            }
            if (addr != null) {
                address += !Utils.isNullOrEmpty(addr.getAddress()) ? addr.getAddress() : "";
                address += !Utils.isNullOrEmpty(addr.getCity()) ? (address.length() > 0 ? "+" : "") + addr.getCity() : "";
                address += !Utils.isNullOrEmpty(addr.getCountry()) ? (address.length() > 0 ? "+" : "") + addr.getCountry() : "";
            }
            Window.open(googleRootUrl + "maps?f=q&hl=en&q=" + address + "&om=1", "_blank", commonParamForUrl);
        });
        googleMaps.ensureDebugId("google_maps");
        items.add(googleMaps);
        //LinkedIn
        MaterialLink linkedinProfiles = new MaterialLink("LinkedIn");
        linkedinProfiles.ensureDebugId("linkedIn_profile");
        linkedinProfiles.addClickHandler(event -> Window.open(linkedinRootUrl + "pub/dir/?first=" + (item.getFirstName() != null ? item.getFirstName() : "") + "&last=" + (item.getLastName() != null ? item.getLastName() : "") + "&search=Search", "_blank", commonParamForUrl));
        items.add(linkedinProfiles);
        //Facebook
        MaterialLink facebookProfile = new MaterialLink("Facebook");
        facebookProfile.addClickHandler(event -> Window.open(facebookRootUrl + "srch.php?nm=" + item.getName(), "_blank", commonParamForUrl));
        facebookProfile.ensureDebugId("facebook_profile");
        items.add(facebookProfile);
        //Twitter
        MaterialLink twitterProfile = new MaterialLink("Twitter");
        twitterProfile.addClickHandler(event -> Window.open(twitterRootUrl + "search?q=" + item.getName() + "&src=typd", "_blank", commonParamForUrl));
        twitterProfile.ensureDebugId("twitter_profile");
        items.add(twitterProfile);

        return items;
    }

    public String getIconStyle() {
        return "contact contact-list";
    }

    protected String getWikiCode() {
        return null;
    }

    public void initialize() {

        profilePicture = new ProfileImage(objectId, LayoutRPC.CONTACT_FORM);

        fullName = initHTML();
        categories = initHTML();
        birthDOBWidget = initHTML();
        companyName = initHTML();
        industries = initHTML();
        accountType = initHTML();
        accountType.setStyleName("crm-account-type");

        jobTitle = initHTML();
        jobFunction = initHTML();
        refIndNumber = initHTML();
        department = initHTML();

        assets = initHTML();
        /*Contact Information began*/
        emailInf = new FlowPanel();
        phoneNumInf = new FlowPanel();
        telegramInf = new FlowPanel();

        imsAddressInf = new FlowPanel();
        relations = new FlowPanel();
        webSiteInf = new FlowPanel();
        addressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> addressRow : addressInf.getWidgets()) {
                    AddressWidget addressWidget = (AddressWidget) addressRow.get(ADDRESS);
                    if (addressWidget.isNotEmpty()) {
                        return true;
                    }
                }
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, true);
        parentAddressInf = new MultiTableNewUI(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getAddressWidgets(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> addressRow : parentAddressInf.getWidgets()) {
                    AddressWidget addressWidget = (AddressWidget) addressRow.get(ADDRESS);
                    if (addressWidget.isNotEmpty()) {
                        return true;
                    }
                }
                return false;
            }
        }, true);

        reportsTo = initHTML();
        contactOwner = initHTML();
        campaignSource = initHTML();
        emailOpt = initHTML();
        createdDate = initHTML();
        updatedDate = initHTML();
        noteWidget = new NoteWidget(objectId, getEntityType());
        if (objectId != null) {
            activityWidget = new CrmActivityGrid(objectId, getEntityType());
        }
        uploadForm = new GeneralFileUpload((isLead() ? Constants.F_LEAD : isCandidate() ? Constants.F_CANDIDATE : Constants.F_CRM_CONTACT), objectId, objectId);
        showStatusHistory();
    }

    protected void showStatusHistory() {
        if (objectId != null) {
            statusHistoryGrid = new ContactStatusHistoryGrid(objectId, ContactListItem.CRM_CONTACT, true);
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAD_STATUS_CHANGED, ViewContactForm.this, (sender, args) -> statusHistoryGrid.refresher());
        }
    }

    protected String getEntityType() {
        return RelationItem.TYPE_CONTACT;
    }

    protected void drawForm() {
        contactCareer = new ContactCareerView(objectId, this);
        ContactUpdatesGrid contactUpdatesGrid = new ContactUpdatesGrid(objectId);


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(FIRST_NAME, fullName, getTitle(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(FIRST_NAME, fullName, getTitle(wfmStrings.name()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BIRTH_DAY) != null) {
            addField(BIRTH_DAY, birthDOBWidget, getTitle(formPropertyMap.get(CustomFormConstants.BIRTH_DAY).isChanged() ? formPropertyMap.get(CustomFormConstants.BIRTH_DAY).getTitle() : wfmStrings.dateOfBirth()));
        } else {
            addField(BIRTH_DAY, birthDOBWidget, getTitle(wfmStrings.dateOfBirth()));
        }

        addField(CRM_ACCOUNT_CAREER, contactCareer.getCareerPanel(), wfmStrings.careerInformation(), true);
        addField(CRM_CONTACT_UPDATES, contactUpdatesGrid, wfmStrings.updates(), true);

        addField(PROFILE_PICTURE, profilePicture, null, true);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME) != null) {
            addField(CRM_ACCOUNT_NAME, companyName, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle() : wfmStrings.company(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_NAME).isRequired()));
        } else {
            addField(CRM_ACCOUNT_NAME, companyName, getTitle(wfmStrings.company(), showRequired && isLead()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE) != null) {
            addField(CRM_ACCOUNT_TYPE, accountType, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_TYPE).getTitle() : wfmStrings.accountType()));
        } else {
            addField(CRM_ACCOUNT_TYPE, accountType, getTitle(wfmStrings.accountType()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.JOB_TITLE) != null) {
            addField(JOB_TITLE, jobTitle, getTitle(formPropertyMap.get(CustomFormConstants.JOB_TITLE).isChanged() ? formPropertyMap.get(CustomFormConstants.JOB_TITLE).getTitle() : wfmStrings.jobTitle()));
        } else {
            addField(JOB_TITLE, jobTitle, getTitle(wfmStrings.jobTitle()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null) {
            addField(DEPARTMENT, department, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        } else {
            addField(DEPARTMENT, department, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER) != null) {
            addField(REF_IND_NUMBER, refIndNumber, getTitle(formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.REF_IND_NUMBER).getTitle() : wfmStrings.refIndNumeber()));
        } else {
            addField(REF_IND_NUMBER, refIndNumber, getTitle(wfmStrings.refIndNumeber()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSETS) != null) {
            addField(ASSETS, assets, getTitle(formPropertyMap.get(CustomFormConstants.ASSETS).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSETS).getTitle() : wfmStrings.assetsUnderManagement()));
        } else {
            addField(ASSETS, assets, getTitle(wfmStrings.assetsUnderManagement()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY) != null) {
            addField(CRM_ACCOUNT_INDUSTRY, industries, getTitle(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_INDUSTRY).getTitle() : wfmStrings.industry()));
        } else {
            addField(CRM_ACCOUNT_INDUSTRY, industries, getTitle(wfmStrings.industry()));
        }

        addTitleField(CONTACT_INFORMATION, isCandidate() ? wfmStrings.candidateInformation() : property.getSingular(wfmStrings.contactInformation(), wfmStrings.contact()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL) != null) {
            addField(EMAIL, emailInf, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL).getTitle() : wfmStrings.email()));
        } else {
            addField(EMAIL, emailInf);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE) != null) {
            addField(PHONE, phoneNumInf, getTitle(formPropertyMap.get(CustomFormConstants.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone()));
        } else {
            addField(PHONE, phoneNumInf);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IM_ADDRESS) != null) {
            addField(IM_ADDRESS, imsAddressInf, getTitle(formPropertyMap.get(CustomFormConstants.IM_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.IM_ADDRESS).getTitle() : wfmStrings.imAddress()));
        } else {
            addField(IM_ADDRESS, imsAddressInf);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WEB_ADDRESS) != null) {
            addField(WEB_ADDRESS, webSiteInf, getTitle(formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.WEB_ADDRESS).getTitle() : wfmStrings.webAddress()));
        } else {
            addField(WEB_ADDRESS, webSiteInf);
        }

        addTitleField(ADDRESS_INFORMATION, wfmStrings.addressInformation());
        addField(ADDRESS, addressInf, wfmStrings.address(), true);
//        addField(PARENT_ADDRESSES, parentAddressInf, wfmStrings.accoundAddress(), true);

        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        //LEAD VIEWni chizish uchun fieldlar  ViewLeadForm da Override qilinadi
        addTitleField(LEAD_INFORMATION, Property.get(Constants.LEADS, wfmStrings.basicDetails(), wfmStrings.lead()));
        addField(LEAD_OWNER, contactOwner, getTitle(wfmStrings.owner()));
        addField(LEAD_NAME, fullName, getTitle(wfmStrings.name()));

        if (contactType != ContactListItem.LEAD_CONTACT) {
            addField(CATEGORY, categories, getTitle(wfmStrings.category()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RELATIONSHIP) != null) {
            addField(RELATIONSHIP, relations, getTitle(formPropertyMap.get(CustomFormConstants.RELATIONSHIP).isChanged() ? formPropertyMap.get(CustomFormConstants.RELATIONSHIP).getTitle() : wfmStrings.relationship()));
        } else {
            addField(RELATIONSHIP, relations, getTitle(wfmStrings.relationship()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTS_TO) != null) {
            addField(REPORTS_TO, reportsTo, getTitle(formPropertyMap.get(CustomFormConstants.REPORTS_TO).isChanged() ? formPropertyMap.get(CustomFormConstants.REPORTS_TO).getTitle() : wfmStrings.supervisor()));
        } else {
            addField(REPORTS_TO, reportsTo, getTitle(wfmStrings.supervisor()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OWNER) != null) {
            addField(OWNER, contactOwner, getTitle(formPropertyMap.get(CustomFormConstants.OWNER).isChanged() ? formPropertyMap.get(CustomFormConstants.OWNER).getTitle() : wfmStrings.owner()));
        } else {
            addField(OWNER, contactOwner, getTitle(wfmStrings.owner()));
        }
        addField(CREATED_DATE, createdDate, getTitle(wfmStrings.createdDate()));
        addField(UPDATED_DATE, updatedDate, getTitle(wfmStrings.modifiedDate()));

        /*Additional Information - end*/

        // Crm Details
        addTitleField(CRM_DETAILS, wfmStrings.crmDetails());
        if (Utils.getPathName().contains("Crm.html")) {
            if (!isLead()) {
                if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME) != null) {
                    addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.CRM_CAMPAIGN_NAME).getTitle() : wfmStrings.campaign()));
                } else {
                    addField(CRM_CAMPAIGN_NAME, campaignSource, getTitle(wfmStrings.campaign()));
                }
            }
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT) != null) {
                addField(EMAIL_OPT_OUT, emailOpt, getTitle(formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).isChanged() ? formPropertyMap.get(CustomFormConstants.EMAIL_OPT_OUT).getTitle() : wfmStrings.emailOptOut()));
            } else {
                addField(EMAIL_OPT_OUT, emailOpt, getTitle(wfmStrings.emailOptOut()));
            }
        }
        if (Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
            mailListTable = initHTML();
            addField(SUBSCRIPTION_LIST, mailListTable, getTitle(wfmStrings.mailingLists()));
        }

        addField(CRM_ACTIVITIES, activityWidget, Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, wfmStrings.latestOpenActivities(), wfmStrings.activities()), true);
        addField(CRM_NOTE, noteWidget, wfmStrings.notes(), true);
        addField(ATTACHMENTS, uploadForm, wfmStrings.attachments(), true);
        addField(STATUS_HISTORY, statusHistoryGrid, wfmStrings.statusHistory(), true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
    }

    private WidgetsMap getAddressWidgets(Address addressData) {
        AddressNewUIWidget addressWidget = new AddressNewUIWidget(addressData, true, (objectId != null ? objectId.toString() : "add"), true, false, filterParametrs);
        final WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgetToMap(MultiTable.ADDRESS, addressWidget);
        widgetsMap.addToLeft(null, addressWidget.primaryField);
        widgetsMap.addToCenter(null, addressWidget.nameField);
        widgetsMap.addToCenter(null, addressWidget.addressViewField);
        widgetsMap.addToRight(null, addressWidget.editButton);
        return widgetsMap;
    }

    private void fillAccountFields() {
        industries.setHTML(item.getCrmAccount().getIndustry());
        accountType.setHTML(CRMUtils.getSelectItemsAsCommaDelimeted(item.getCrmAccount().getAccountTypes(), true));
        boolean isWrite = item.getPermissionForEntireUser() != null && item.getPermissionForEntireUser().isWrite();
        if (contactCareer != null) {
            contactCareer.setWrite(isWrite);
        }
    }

    public void setContactItem() {
        Utils.registrRelation(item);
        initPredefinedValues();
        LoadingPanel.loading(true);
        if (mailListTable != null) {
            MassMailService.App.get().getMailListByCrmEntityID(objectId, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(SelectItem[] result) {
                    LoadingPanel.loading(false);
                    final StringBuilder s = new StringBuilder();
                    for (SelectItem item : result) {
                        if (item.isSelected()) {
                            s.append(item.getName()).append("<br>");
                        }
                    }
                    mailListTable.setHTML(s.toString());
                }
            });
        }
        fullName.setHTML(item.getNameWithTitle());
        if (item.getBirthDate() != null) {
            birthDOBWidget.setHTML(DateUtils.format(item.getBirthDate().getNonConvertedDate()));
        } else {
            birthDOBWidget.setHTML(wfmStrings.notAvailable());
        }
        if (item.getCrmAccount().getObjectId() != null) {
            companyName.setHTML("<a href=\"javascript:\">" + item.getCrmAccount().getName());
            companyName.addDomHandler(clickEvent -> {
                if (Utils.isAccounting()) {
                    if (item.getCrmAccount().hasCustomerType()) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + item.getCrmAccount().getObjectId(), item.getCrmAccount().getNumber(), item.getCrmAccount().getName());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("suppliersummary|summary/" + item.getCrmAccount().getObjectId(), item.getCrmAccount().getNumber(), item.getCrmAccount().getName());
                    }
                } else if (Utils.hasPermission(CRM_ACCOUNTS_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("account|summary/" + item.getCrmAccount().getObjectId(), item.getCrmAccount().getNumber(), item.getCrmAccount().getName());
                }
            }, ClickEvent.getType());
        }
        fillAccountFields();
        jobTitle.setHTML(item.getJobTitle());
        jobFunction.setHTML(item.getJobFunction());
        refIndNumber.setHTML(item.getRefIndNumber());
        department.setHTML(item.getDepartment());
        //Contact Information
        setMultiTableItems(Constants.CONTACT_EMAILS);
        setMultiTableItems(Constants.CONTACT_PHONES);
        setMultiTableItems(Constants.CONTACT_WEBSITES);
        setAddressWidgets();
        setTelegramWidgets();

        setRelationWidgets();
        setContactImAddressesWidgets();
        categories.setHTML(SelectItem.asCommaDelimited(item.getSelectedCategories()));
        if (item.getReportsToId() != null) {
            reportsTo.setHTML("<a href=\"javascript:\">" + item.getReportsTo());
            reportsTo.addDomHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + item.getReportsToId(), item.getName(), item.getName()), ClickEvent.getType());
        }
        //Crm Details
        contactOwner.setHTML(item.getOwner());
        createdDate.setHTML(DateUtils.formatInternal(item.getCreatedDate()));
        updatedDate.setHTML(DateUtils.formatInternal(item.getUpdatedDate()));

        if (item.getCampaignId() != null) {
            campaignSource.setHTML("<a href=\"javascript:\">" + item.getCampaign());
            campaignSource.addDomHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("campaign|summary/" + item.getCampaignId() + "/" + item.getCampaign(), item.getCampaign()), ClickEvent.getType());
        }
        emailOpt.setHTML(item.isEmailOptOut() ? wfmStrings.yes() : wfmStrings.no());

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
        initRelatedAttachments();
    }

    private void initRelatedAttachments() {
        if (item.getTrackerIDSet() != null) {
            LoadingPanel.loading(true);
            crmService.getTrackerAttachments(item.getTrackerIDSet(), new AbstractAsyncCallback<FileResource[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(FileResource[] result) {
                    LoadingPanel.loading(false);
                    uploadForm.addAdditionalAttachments(result, false);
                }
            });
        }
    }

    private void setRelationWidgets() {
        if (item.getSelectedRelationships() != null && item.getSelectedRelationships().size() > 0) {
            for (SelectItem selectedRelation : item.getSelectedRelationships()) {
                FlowPanel f = new FlowPanel();
                f.getElement().getStyle().setDisplay(Style.Display.FLEX);
                f.addStyleName("firstChildHasPadding mb-2");
                HTML relationS = new HTML(getTitle(selectedRelation.getName()));
                HTML valueF = new HTML(selectedRelation.getDescription());
                f.add(relationS);
                f.add(valueF);
                relations.add(f);
            }
        }
    }


    private void setContactImAddressesWidgets() {
        if (item.getSelectedContactImAddress() != null && item.getSelectedContactImAddress().size() > 0) {
            for (SelectItem selectedImAddress : item.getSelectedContactImAddress()) {
                FlowPanel f = new FlowPanel();
                f.getElement().getStyle().setDisplay(Style.Display.FLEX);
                f.addStyleName("firstChildHasPadding mb-2");
                HTML relationS = new HTML(getTitle(selectedImAddress.getName()));
                HTML valueF = new HTML(selectedImAddress.getDescription());
                f.add(relationS);
                f.add(valueF);
                imsAddressInf.add(f);
            }
        }
    }

    private void setAddressWidgets() {
        parentAddressInf.removeAllRows();
        boolean hasParentAddressInf = false;
        if (item.getAddresses() != null && item.getAddresses().size() > 0) {
            addressInf.clear();
            for (int i = 0; i < item.getAddresses().size(); i++) {
                if (item.getAddresses().get(i).isLinkedAddress()) {
                    hasParentAddressInf = true;
                }
                (item.getAddresses().get(i).isLinkedAddress() ? parentAddressInf : addressInf).addWidgets(getAddressWidgets(item.getAddresses().get(i)));
            }
            if (hasParentAddressInf) {
                addField(PARENT_ADDRESSES, parentAddressInf, wfmStrings.accoundAddress(), true);
            }
        }
    }

    private void setTelegramWidgets() {
        telegramInf.clear();
        if (item.getTelegramChats() != null && !item.getTelegramChats().isEmpty()) {
            for (SelectItem telegramChat : item.getTelegramChats()) {
                FlowPanel f = new FlowPanel();
                f.getElement().getStyle().setDisplay(Style.Display.FLEX);
                f.addStyleName("firstChildHasPadding mb-2");
                HTML relationS = new HTML(getTitle(telegramChat.getName()));
                HTML valueF = new HTML(telegramChat.getDescription());
                f.add(relationS);
                f.add(valueF);
                telegramInf.add(f);
            }
        }
        addField(TELEGRAM, telegramInf, wfmStrings.telegram());
    }

    private void setMultiTableItems(int param) {
        Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(item, param);
        if (itemParamsAsMap != null && itemParamsAsMap.size() > 0) {
            for (Map.Entry<Integer, ArrayList<String>> entry : itemParamsAsMap.entrySet()) {
                int relation = entry.getKey();
                for (String value : entry.getValue()) {
                    if (value != null && !"".equals(value.trim())) {
                        value = value.replace("|", "-");
                        getKeyValuElement(value, relation, param);
                    }
                }
            }
        }
    }

    private void getKeyValuElement(final String value, int relation, int param) {
        FlowPanel f = new FlowPanel();
        f.getElement().getStyle().setDisplay(Style.Display.FLEX);
        f.addStyleName("firstChildHasPadding mb-2");
        HTML valueF = new HTML(value);
        PhonePopup popup;
        String phoneNumber = null;
        boolean mobile = false;
        HTML relationS = null;
        switch (param) {
            case Constants.CONTACT_EMAILS:
                valueF.setHTML("<a href=\"javascript:\">" + value + "</a>");
                valueF.addClickHandler(event -> {
                    if (!item.isEmailOptOut()) {
                        //new ComposeView(item.getPrimaryEmail(), RelationItem.newEventRelation(isLead() ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT, item.getObjectId(), item.getName()));
                        String relationType = isLead() ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT;
                        if (item.getCrmAccount() != null && item.getCrmAccount().getObjectId() != null && item.getCrmAccount().getName() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getPrimaryEmail() + "/" + relationType + "/" + item.getObjectId() + "/" + item.getName() + "/" + RelationItem.TYPE_CRM_ACCOUNT + "/" + item.getCrmAccount().getObjectId() + "/" + item.getCrmAccount().getName());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getPrimaryEmail() + "/" + relationType + "/" + item.getObjectId() + "/" + item.getName());
                        }
                    } else {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, crmMessages.theEmailOutIsEnabled());
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.open();
                    }
                });
//                f.add(relationS);
//                f.add(valueF);
                emailInf.add(valueF);
                break;
            case Constants.CONTACT_PHONES:
                switch (relation) {
                    case Constants.G_HOME:
                        relationS = new HTML(getTitle(wfmStrings.home()));
                        phoneNumber = value;
                        break;
                    case Constants.G_WORK:
                        relationS = new HTML(getTitle(wfmStrings.contactwork()));
                        phoneNumber = value;
                        break;
                    case Constants.G_OTHER:
                        relationS = new HTML(getTitle(wfmStrings.other()));
                        phoneNumber = value;
                        break;
                    case Constants.G_MOBILE:
                        relationS = new HTML(getTitle(wfmStrings.mobile()));
                        phoneNumber = value;
                        mobile = true;
                        break;
                    case Constants.G_HOME_FAX:
                        relationS = new HTML(getTitle(wfmStrings.homeFax()));
                        phoneNumber = value;
                        break;
                    case Constants.G_WORK_FAX:
                        relationS = new HTML(getTitle(wfmStrings.workFax()));
                        phoneNumber = value;
                        break;
                    case Constants.G_PAGER:
                        relationS = new HTML(getTitle(wfmStrings.pager()));
                        phoneNumber = value;
                        break;
                    case Constants.G_EXTENSION:
                        relationS = new HTML(getTitle(wfmStrings.extension()));
                        phoneNumber = value;
                        break;
                    case Constants.G_FAX:
                        relationS = new HTML(getTitle(wfmStrings.fax()));
                        phoneNumber = value;
                        break;
                    case Constants.G_WHATS_APP:
                        relationS = new HTML(getTitle(wfmStrings.whatsApp()));
                        phoneNumber = value;
                        break;
                    case Constants.G_TELEGRAM:
                        relationS = new HTML(getTitle(wfmStrings.telegram()));
                        phoneNumber = value;
                        break;
                    case Constants.G_VIBER:
                        relationS = new HTML(getTitle(wfmStrings.viber()));
                        phoneNumber = value;
                        break;
                }
                f.getElement().setAttribute("style", "display: flex; align-items: center");
                popup = new PhonePopup(phoneNumber, item, mobile);
                Div phoneWidget = popup.getPhoneWidget();
//                phoneWidget.setStyleName("field");
                f.add(relationS);
                f.add(phoneWidget);
                phoneNumInf.add(f);
                break;
            case Constants.CONTACT_WEBSITES:
                switch (relation) {
                    case Constants.G_HOME:
                        relationS = new HTML(getTitle(wfmStrings.home()));
                        break;
                    case Constants.G_WORK:
                        relationS = new HTML(getTitle(wfmStrings.contactwork()));
                        break;
                    case Constants.G_OTHER:
                        relationS = new HTML(getTitle(wfmStrings.other()));
                        break;
                    case Constants.G_HOME_PAGE:
                        relationS = new HTML(getTitle(Constants.G_HOME_PAGE_STR));
                        break;
                    case Constants.G_FTP:
                        relationS = new HTML(getTitle(Constants.G_FTP_STR));
                        break;
                    case Constants.G_BLOG:
                        relationS = new HTML(getTitle(Constants.G_BLOG_STR));
                        break;
                    case Constants.G_PROFILE:
                        relationS = new HTML(getTitle(Constants.G_PROFILE_STR));
                        break;
                    case Constants.G_LINKEDIN:
                        relationS = new HTML(getTitle(Constants.G_LINKEDIN_STR));
                        break;
                    case Constants.G_FACEBOOK:
                        relationS = new HTML(getTitle(Constants.G_FACEBOOK_STR));
                        break;
                    case Constants.G_TWITTER:
                        relationS = new HTML(getTitle(Constants.G_TWITTER_STR));
                        break;
                    case Constants.G_INSTAGRAM:
                        relationS = new HTML(getTitle(Constants.G_INSTAGRAM_STR));
                        break;
                }
                String href = value.contains("http://") ? value : "http://" + value;
                valueF.setHTML("<a target=\"_blank\" href=\"" + href + "\">" + value + "</a>");
                f.add(relationS);
                f.add(valueF);
                webSiteInf.add(f);
                break;
        }
    }


    private FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.Contacts;
    }
}