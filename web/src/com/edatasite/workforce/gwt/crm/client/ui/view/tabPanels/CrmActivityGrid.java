package com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels;

import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;

import java.util.ArrayList;

/**
 * User: Hayot
 * Date: Apr 10, 2010
 * Time: 4:49:32 PM
 */
public class CrmActivityGrid extends AbstractDataGrid<ActivityItem> implements Constants {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected Integer entityID;
    protected String entityType;
    protected Integer entityIDClone;
    protected String otherEntityType;

    public CrmActivityGrid(Integer entityID, String entityType) {
        super();
        this.entityID = entityID;
        this.entityType = entityType;
        initialize();
        initUiEventListeners();
    }

    public CrmActivityGrid(Integer entityID, String entityType, Integer entityIDClone, String otherEntityType) {
        super();
        this.entityID = entityID;
        this.entityType = entityType;
        this.entityIDClone = entityIDClone;
        this.otherEntityType = otherEntityType;
        initialize();
        initUiEventListeners();
    }

    protected void initUiEventListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, CrmActivityGrid.this, (sender, args) -> refresher());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, CrmActivityGrid.this, (sender, args) -> refresher());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_DELETE, CrmActivityGrid.this, (sender, args) -> refresher());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_ACTIVITY_DELETED, CrmActivityGrid.this, (sender, args) -> refresher());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MAIL_COMPOSE, CrmActivityGrid.this, (sender, args) -> refresher());
    }

    @Override
    protected void addColums() {
        //activity name
        Column<ActivityItem, String> name = new Column<ActivityItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(ActivityItem item) {
                String s = item.getActivityType();
                if (CrmConstants.TASK.equals(item.getActivityType())) {
                    s = wfmStrings.task();
                } else if (CrmConstants.EMAIL.equals(item.getActivityType())) {
                    s = wfmStrings.email();
                } else if (CrmConstants.CRM_EVENT.equals(item.getActivityType())) {
                    s = Property.get(Constants.EVENT_LIST, wfmStrings.event());
                } else if (CrmConstants.CRM_EVENT_CALLOG.equals(item.getActivityType())) {
                    s = wfmStrings.call();
                } else if (CrmConstants.CRM_EVENT_INTERVIEW.equals(item.getActivityType()) || item.isInterview()) {
                    s = wfmStrings.interview();
                } else if (CrmConstants.MASS_MAIL.equals(item.getActivityType())) {
                    s = wfmStrings.massMail();
                } else if (CrmConstants.SMS.equals(item.getActivityType())) {
                    s = wfmStrings.sms();
                } else if (CrmConstants.SALEQUOTE.equals(item.getActivityType())) {
                    s = wfmStrings.salesQuote();
                } else if (CrmConstants.SALEORDER.equals(item.getActivityType())) {
                    s = wfmStrings.saleQuoteOrder();
                } else if (CrmConstants.SALEINVOICE.equals(item.getActivityType())) {
                    s = wfmStrings.salesInvoice();
                }
                return s;
            }
        };
        name.setFieldUpdater((i, item, s) -> {
            if (CrmConstants.EMAIL.equals(item.getActivityType())) {
                SinksContainerFactory.entryPoint.onHistoryChanged("messagecenter|messagecenter" + item.getEmailObjectId() + "/" + item.getEmailObjectId() + "/" + item.getStatus() + "/" + "/" + "/");
            } else if (CrmConstants.MASS_MAIL.equals(item.getActivityType()) && Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("message|summary/" + item.getMassMailObjectId() + "/readonly");
            } else if (CrmConstants.TASK.equals(item.getActivityType())) {
                SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + item.getTaskObjectId());
            } else if (CrmConstants.SMS.equals(item.getActivityType())) {
            } else if (CrmConstants.SALEQUOTE.equals(item.getActivityType())) {
                if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/opportunity/" + item.getSalesID());
                } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|summary/" + item.getSalesID());
                }
            } else if (CrmConstants.SALEORDER.equals(item.getActivityType())) {
                if (Utils.hasPermission(PermissionConstants.CRM_SALES_ORDER_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|summary/" + item.getSalesID());
                } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|summary/" + item.getSalesID());
                }
            } else if (CrmConstants.SALEINVOICE.equals(item.getActivityType())) {
                if (Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|summary/" + item.getSalesID());
                } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|summary/" + item.getSalesID());
                }
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + item.getEventObjectId() + (item.isCallLog() ? "/true" : ""));
            }
        });
        addColumn(name, Property.get(Constants.EVENT_LIST, wfmStrings.type(), wfmStrings.activity()));
        setColumnWidth(name, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //activity subject
        Column<ActivityItem, String> subject = new Column<ActivityItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(ActivityItem item) {
                return item.getSubject() != null && !"".equals(item.getSubject()) ? item.getSubject() : wfmStrings.notAvailable();
            }
        };
        subject.setFieldUpdater((i, item, s) -> {
            if (CrmConstants.EMAIL.equals(item.getActivityType())) {
                SinksContainerFactory.entryPoint.onHistoryChanged("messagecenter|messagecenter" + item.getEmailObjectId() + "/" + item.getEmailObjectId() + "/" + item.getStatus() + "/" + "/" + "/");
            } else if (CrmConstants.MASS_MAIL.equals(item.getActivityType()) && Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("message|summary/" + item.getMassMailObjectId() + "/readonly");
            } else if (CrmConstants.TASK.equals(item.getActivityType())) {
                SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + item.getTaskObjectId());
            } else if (CrmConstants.SALEQUOTE.equals(item.getActivityType())) {
                if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|summary/" + item.getSalesID());
                } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|summary/" + item.getSalesID());
                }
            } else if (CrmConstants.SALEORDER.equals(item.getActivityType())) {
                if (Utils.hasPermission(PermissionConstants.CRM_SALES_ORDER_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|summary/" + item.getSalesID());
                } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|summary/" + item.getSalesID());
                }
            } else if (CrmConstants.SALEINVOICE.equals(item.getActivityType())) {
                if (Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|summary/" + item.getSalesID());
                } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_SUMMARY)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(SALE_INVOICE + "|summary/" + item.getSalesID());
                }
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + item.getEventObjectId() + (item.isCallLog() ? "/true" : ""));
            }
        });
        addColumn(subject, wfmStrings.subject());
        setColumnWidth(subject, 50, com.google.gwt.dom.client.Style.Unit.PCT);
        //activity start date
        Column<ActivityItem, SafeHtml> startDate = new Column<ActivityItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ActivityItem item) {
                return () -> "<span>" + (item.getStartDate() != null ? DateUtils.formatInternal(item.getStartDate()) : "") + "</span>";
            }
        };
        addColumn(startDate, wfmStrings.startDate());
        setColumnWidth(startDate, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //activity end date
        Column<ActivityItem, SafeHtml> endDate = new Column<ActivityItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ActivityItem item) {
                return () -> "<span>" + (item.getDueDate2() != null ? DateUtils.format(item.getDueDate2()) : item.getDueDate() != null ? DateUtils.formatInternal(item.getDueDate()) : "") + "</span>";
            }
        };
        addColumn(endDate, wfmStrings.endDate());
        setColumnWidth(endDate, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //activity status
        Column<ActivityItem, SafeHtml> status = new Column<ActivityItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ActivityItem item) {
                return () -> "<span>" + (CrmConstants.SMS.equals(item.getStatus()) ? wfmStrings.send() : item.getStatus() != null ? item.getStatus() : wfmStrings.notAvailable()) + "</span>";
            }
        };
        addColumn(status, wfmStrings.status());
        setColumnWidth(status, 20, com.google.gwt.dom.client.Style.Unit.PCT);

    }

    @Override
    public void refresher() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setRelationID(entityID);
        ArrayList<String> relationTypes = new ArrayList<>();
        if (otherEntityType != null && CrmConstants.CRM_ACCOUNT.equals(entityType)) {
            relationTypes.add(entityType);
            relationTypes.add(otherEntityType);
            relationTypes.add(CrmConstants.SUPPLIER);
        }
        fp.setRelationTypes(relationTypes);
        fp.setRelationType(entityType);
        fp.setCreatedFrom(Utils.isHRMS() ? (Appointment.FROM_HRMS) : (Appointment.FROM_CRM));
        CRMService.App.get().getNewActivityList(fp, new AbstractAsyncCallback<ListResult<ActivityItem>>() {
            public void failure(Throwable caught) {
            }

            public void success(final ListResult<ActivityItem> activityList) {
                Scheduler.get().scheduleDeferred(() -> {
                    supplyProvider(activityList.getList().toArray(new ActivityItem[]{}));
                    reDrawItems();
                });
            }
        });
    }
}