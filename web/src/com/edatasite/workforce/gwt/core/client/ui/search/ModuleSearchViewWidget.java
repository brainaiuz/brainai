package com.edatasite.workforce.gwt.core.client.ui.search;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.AdvancedModuleSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.AdvancedSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ModuleSectionConstants;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.OverallSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.SearchModuleType;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ShowSearchViewType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 29/11/11
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 */
public class ModuleSearchViewWidget extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private int selectSectionType = -1;

    private FlexTable content;
    private Label task;
    private Label project;
    //    private Label crmTask;
    private Label crmAccount;
    private Label crmContact;
    private Label crmLead;
    private Label crmCase;
    private Label crmOpportunity;
    private Label saleInvoice;
    private Label saleQuote;
    private Label purchaseOrder;
    private Label news;
    private Label wfmEvent;
    private Label documents;
    private Label curSelectLabel;
    private final Label selectSectionName;

    private final SearchViewWidget viewWidget;

    public ModuleSearchViewWidget(SearchViewWidget viewWidget, Label selectSectionName) {
        this.viewWidget = viewWidget;
        this.selectSectionName = selectSectionName;
        this.viewWidget.setModuleSearchView(this);
        this.init();
    }

    private void init() {
        content = new FlexTable();
        content.getElement().getStyle().setProperty("border", "1px solid #6B90DA");
        content.setWidth("100%");
        content.setCellPadding(0);
        content.setCellSpacing(0);
        this.initWidget(content);

        int row = 0;

        content.setHTML(row, 0, "&nbsp;");
        // Workspace
        if (Utils.marketplaceShowMenu(WORKSPACE_PAGE)) {
            content.setWidget(row++, 0, this.getWorkspaceAdvancedModuleSearch());
        }
        // Project Management
        if (Utils.marketplaceShowMenu(PROJECT_MANAGEMENT_PAGE)) {
            content.setWidget(row++, 0, this.getPmAdvancedModuleSearch());
        }
        // CRM
        if (Utils.marketplaceShowMenu(CRM_PAGE)
                && (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(SALESMAN) || Utils.hasRole(SALESPERSON) || Utils.hasRole(CUSTOMER_SERVICE_REPRESENTATIVE))) {
            content.setWidget(row++, 0, this.getCRMAdvancedModuleSearch());
        }
        // Accounting
        if (Utils.marketplaceShowMenu(ACCOUNTING_PAGE)) {
            content.setWidget(row++, 0, this.getAccountingAdvancedModuleSearch());
        }
        // Documents
        if (Utils.marketplaceShowMenu(DOC_MY_FOLDER) && !Utils.hasRole(CLIENT)) {
            content.setWidget(row++, 0, this.getDocumentsAdvancedModuleSearch());
        }
    }

    private Widget getDocumentsAdvancedModuleSearch() {
        FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.getElement().getStyle().setPaddingBottom(5, Style.Unit.PX);

        documents = new Label(wfmStrings.documents());
        documents.setStyleName("wft-search-link");
        documents.addClickHandler(clickEvent -> {
            setSelected(documents, ModuleSectionConstants.DOCUMENTS);
            setSelectSectionName(documents.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.DOCUMENTS);
        });

        setSelectSectionType(ModuleSectionConstants.DOCUMENTS, documents);

        int row = 0;
        table.setHTML(row, 0, wfmStrings.documents());
        table.getFlexCellFormatter().setStyleName(row++, 0, "wft-search-list");
        //Project
        table.setWidget(row++, 0, documents);

        return table;
    }

    private Widget getPmAdvancedModuleSearch() {
        FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.getElement().getStyle().setPaddingBottom(5, Style.Unit.PX);

        task = new Label(wfmStrings.tasks());
        task.setStyleName("wft-search-link");
        task.addClickHandler(clickEvent -> {
            setSelected(task, ModuleSectionConstants.TASK);
            setSelectSectionName(task.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.TASK);
        });

        project = new Label(wfmStrings.projects());
        project.setStyleName("wft-search-link");
        project.addClickHandler(clickEvent -> {
            setSelected(project, ModuleSectionConstants.PROJECT);
            setSelectSectionName(project.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.PROJECT);
        });

        setSelectSectionType(ModuleSectionConstants.TASK, task);

        int row = 0;
        table.setHTML(row, 0, wfmStrings.projects());
        table.getFlexCellFormatter().setStyleName(row++, 0, "wft-search-list");
        //Task
        table.setWidget(row++, 0, task);
        //Project
        table.setWidget(row++, 0, project);

        return table;
    }

    private Widget getCRMAdvancedModuleSearch() {
        FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.getElement().getStyle().setPaddingBottom(5, Style.Unit.PX);

//        crmTask = new Label(wfmStrings.task());
//        crmTask.setStyleName("wft-search-link");
//        crmTask.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent clickEvent) {
//                setSelected(crmTask);
//            }
//        });

        crmAccount = new Label(wfmStrings.accounts());
        crmAccount.setStyleName("wft-search-link");
        crmAccount.addClickHandler(clickEvent -> {
            setSelected(crmAccount, ModuleSectionConstants.CRM_ACCOUNT);
            setSelectSectionName(crmAccount.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.CRM_ACCOUNT);
        });

        crmContact = new Label(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.crmContact(), wfmStrings.contacts()));
        crmContact.setStyleName("wft-search-link");
        crmContact.addClickHandler(clickEvent -> {
            setSelected(crmContact, ModuleSectionConstants.CRM_CONTACT);
            setSelectSectionName(crmContact.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.CRM_CONTACT);
        });

        crmLead = new Label(wfmStrings.leads());
        crmLead.setStyleName("wft-search-link");
        crmLead.addClickHandler(clickEvent -> {
            setSelected(crmLead, ModuleSectionConstants.CRM_LEAD);
            setSelectSectionName(crmLead.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.CRM_LEAD);
        });

        crmCase = new Label(wfmStrings.cases());
        crmCase.setStyleName("wft-search-link");
        crmCase.addClickHandler(clickEvent -> {
            setSelected(crmCase, ModuleSectionConstants.CRM_CASE);
            setSelectSectionName(crmCase.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.CRM_CASE);
        });

        crmOpportunity = new Label(wfmStrings.opportunities());
        crmOpportunity.setStyleName("wft-search-link");
        crmOpportunity.addClickHandler(clickEvent -> {
            setSelected(crmOpportunity, ModuleSectionConstants.CRM_OPPORTUNITY);
            setSelectSectionName(crmOpportunity.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.CRM_OPPORTUNITY);
        });

        int row = 0;
        if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(SALESMAN) || Utils.hasRole(SALESPERSON)) {
            table.setHTML(row, 0, wfmStrings.crm());
            table.getFlexCellFormatter().setStyleName(row++, 0, "wft-search-list");
            //Lead
            table.setWidget(row++, 0, crmLead);
            // Opportunity
            table.setWidget(row++, 0, crmOpportunity);
            // Account
            table.setWidget(row++, 0, crmAccount);
            //Contact
            table.setWidget(row++, 0, crmContact);

            setSelectSectionType(ModuleSectionConstants.CRM_LEAD, crmContact);
        }
        // Case
        if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(CUSTOMER_SERVICE_REPRESENTATIVE) || Utils.hasRole(SALESPERSON)) {
            if (row == 0) {
                table.setHTML(row, 0, wfmStrings.crm());
                table.getFlexCellFormatter().setStyleName(row++, 0, "wft-search-list");
            }
            table.setWidget(row++, 0, crmCase);
            setSelectSectionType(ModuleSectionConstants.CRM_CASE, crmCase);
        }

        return table;
    }


    private Widget getAccountingAdvancedModuleSearch() {
        FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.getElement().getStyle().setPaddingBottom(5, Style.Unit.PX);

        purchaseOrder = new Label(wfmStrings.purchaseOrders());
        purchaseOrder.setStyleName("wft-search-link");
        purchaseOrder.addClickHandler(clickEvent -> {
            setSelected(purchaseOrder, ModuleSectionConstants.PURCHASE_ORDER);
            setSelectSectionName(purchaseOrder.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.PURCHASE_ORDER);
        });

        saleInvoice = new Label(wfmStrings.saleInvoices());
        saleInvoice.setStyleName("wft-search-link");
        saleInvoice.addClickHandler(clickEvent -> {
            setSelected(saleInvoice, ModuleSectionConstants.SALE_INVOICE);
            setSelectSectionName(saleInvoice.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.SALE_INVOICE);
        });

        saleQuote = new Label(wfmStrings.salesQuotes());
        saleQuote.setStyleName("wft-search-link");
        saleQuote.addClickHandler(clickEvent -> {
            setSelected(saleQuote, ModuleSectionConstants.SALE_QUOTE);
            setSelectSectionName(saleQuote.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.SALE_QUOTE);
        });

        int row = 0;
        if ("true".equals(Utils.userSettings.get(ACCOUNTING_IS_SETUP))) {

            boolean isHashAccessForPMRole = Utils.hashAccessForPMRole();
            if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT) || isHashAccessForPMRole || Utils.hasRole(CLIENT)) {
                table.setHTML(row, 0, wfmStrings.accounts());
                table.getFlexCellFormatter().setStyleName(row++, 0, "wft-search-list");

                //Sale Invoice
                table.setWidget(row++, 0, saleInvoice);
                //Sale Quote
                table.setWidget(row++, 0, saleQuote);

                setSelectSectionType(ModuleSectionConstants.SALE_INVOICE, saleInvoice);
            }
            if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT) || isHashAccessForPMRole) {
                if (row == 0) {
                    table.setHTML(row, 0, wfmStrings.accounts());
                    table.getFlexCellFormatter().setStyleName(row++, 0, "wft-search-list");
                }
                // Purchase Order
                table.setWidget(row++, 0, purchaseOrder);
                setSelectSectionType(ModuleSectionConstants.PURCHASE_ORDER, purchaseOrder);
            }
        }

        return table;
    }

    private Widget getWorkspaceAdvancedModuleSearch() {
        FlexTable table = new FlexTable();
        table.setWidth("100%");
        table.getElement().getStyle().setPaddingBottom(5, Style.Unit.PX);

        news = new Label(wfmStrings.news());
        news.setStyleName("wft-search-link");
        news.addClickHandler(clickEvent -> {
            setSelected(news, ModuleSectionConstants.NEWS);
            setSelectSectionName(news.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.NEWS);
        });

        wfmEvent = new Label(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()));
        wfmEvent.setStyleName("wft-search-link");
        wfmEvent.addClickHandler(clickEvent -> {
            setSelected(wfmEvent, ModuleSectionConstants.EVENT);
            setSelectSectionName(wfmEvent.getText());
            viewWidget.initSearchEngine(ModuleSectionConstants.EVENT);
        });

        setSelectSectionType(ModuleSectionConstants.NEWS, news);

        int row = 0;
        table.setHTML(row, 0, wfmStrings.myWorkspace());
        table.getFlexCellFormatter().setStyleName(row++, 0, "wft-search-list");
        //News
        table.setWidget(row++, 0, news);
        //Event
        table.setWidget(row++, 0, wfmEvent);

        return table;
    }

//    public void setTotalCount(int tCount, int qTime) {
//        totalCount.setHTML("Results of about <b>" + tCount + "</b> for <b>" + DocumentsAdvancedSearchView.searchKey + "</b>.  (<b>" + ((float) qTime) / 1000 + "</b> seconds)");
//    }

    public void fullSearchData(OverallSearchRpc overallSearchRpc, AdvancedSearchRpc advancedSearchRpc) {
        int pm = 0;
        int crm = 0;
        int accounting = 0;
        int workspace = 0;
        int doc = 0;
        clearAllData();
        // Set PM task total
        if (task != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.PM, ModuleSectionConstants.TASK)) {
            task.setText(wfmStrings.tasks() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.PM, ModuleSectionConstants.TASK) + ")");
            pm += getSectionTotal(overallSearchRpc, SearchModuleType.PM, ModuleSectionConstants.TASK);
        }
        // set PM project total
        if (project != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.PM, ModuleSectionConstants.PROJECT)) {
            project.setText(wfmStrings.projects() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.PM, ModuleSectionConstants.PROJECT) + ")");
            pm += getSectionTotal(overallSearchRpc, SearchModuleType.PM, ModuleSectionConstants.PROJECT);
        }
        // set Crm contact total
        if (crmContact != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_CONTACT)) {
            crmContact.setText(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.crmContact(), wfmStrings.contacts()) + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_CONTACT) + ")");
            crm += getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_CONTACT);
        }
        // set Crm lead total
        if (crmLead != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_LEAD)) {
            crmLead.setText(wfmStrings.leads() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_LEAD) + ")");
            crm += getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_LEAD);
        }
        // set Crm account total
        if (crmAccount != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_ACCOUNT)) {
            crmAccount.setText(wfmStrings.accounts() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_ACCOUNT) + ")");
            crm += getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_ACCOUNT);
        }
//        // set Crm task total
//        if (crmTask != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_TASK)) {
//            crmTask.setText(wfmStrings.task() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_TASK) + ")");
//            crm += getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_TASK);
//        }
        // set Crm opportunity total
        if (crmOpportunity != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_OPPORTUNITY)) {
            crmOpportunity.setText(wfmStrings.opportunities() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_OPPORTUNITY) + ")");
            crm += getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_OPPORTUNITY);
        }
        // set Crm case total
        if (crmCase != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_CASE)) {
            crmCase.setText(wfmStrings.cases() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_CASE) + ")");
            crm += getSectionTotal(overallSearchRpc, SearchModuleType.CRM, ModuleSectionConstants.CRM_CASE);
        }
        // set Accounting purchase order total
        if (purchaseOrder != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.PURCHASE_ORDER)) {
            purchaseOrder.setText(wfmStrings.purchaseOrders() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.PURCHASE_ORDER) + ")");
            accounting += getSectionTotal(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.PURCHASE_ORDER);
        }
        // set Accounting sale invoice total
        if (saleInvoice != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.SALE_INVOICE)) {
            saleInvoice.setText(wfmStrings.saleInvoices() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.SALE_INVOICE) + ")");
            accounting += getSectionTotal(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.SALE_INVOICE);
        }
        // set Accounting sale quote total
        if (saleQuote != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.SALE_QUOTE)) {
            saleQuote.setText(wfmStrings.salesQuotes() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.SALE_QUOTE) + ")");
            accounting += getSectionTotal(overallSearchRpc, SearchModuleType.Accounting, ModuleSectionConstants.SALE_QUOTE);
        }
        // set Workspace news total
        if (news != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.Workspace, ModuleSectionConstants.NEWS)) {
            news.setText(wfmStrings.news() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.Workspace, ModuleSectionConstants.NEWS) + ")");
            workspace += getSectionTotal(overallSearchRpc, SearchModuleType.Workspace, ModuleSectionConstants.NEWS);
        }
        // set Workspace news total
        if (wfmEvent != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.Workspace, ModuleSectionConstants.EVENT)) {
            wfmEvent.setText(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()) + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.Workspace, ModuleSectionConstants.EVENT) + ")");
            workspace += getSectionTotal(overallSearchRpc, SearchModuleType.Workspace, ModuleSectionConstants.EVENT);
        }
        // set Workspace news total
        if (documents != null && isCheckedModuleType(overallSearchRpc, SearchModuleType.Documents, ModuleSectionConstants.DOCUMENTS)) {
            documents.setText(wfmStrings.documents() + " (" + getSectionTotal(overallSearchRpc, SearchModuleType.Documents, ModuleSectionConstants.DOCUMENTS) + ")");
            doc += getSectionTotal(overallSearchRpc, SearchModuleType.Documents, ModuleSectionConstants.DOCUMENTS);
        }

    }

    private void clearAllData() {
        // Set PM task total
        if (task != null) {
            task.setText(wfmStrings.task() + " (0)");
        }
        // set PM project total
        if (project != null) {
            project.setText(wfmStrings.project() + " (0)");
        }
        // set Crm contact total
        if (crmContact != null) {
            crmContact.setText(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.crmContact(), wfmStrings.contacts()) + " (0)");
        }
        // set Crm lead total
        if (crmLead != null) {
            crmLead.setText(wfmStrings.lead() + " (0)");
        }
        // set Crm account total
        if (crmAccount != null) {
            crmAccount.setText(wfmStrings.account() + " (0)");
        }
//        // set Crm task total
//        if (crmTask != null) {
//            crmTask.setText(wfmStrings.task() + " (0)");
//        }
        // set Crm opportunity total
        if (crmOpportunity != null) {
            crmOpportunity.setText(wfmStrings.opportunities() + " (0)");
        }
        // set Crm case total
        if (crmCase != null) {
            crmCase.setText(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()) + " (0)");
        }
        // set Accounting purchase order total
        if (purchaseOrder != null) {
            purchaseOrder.setText(wfmStrings.purchaseOrders() + " (0)");
        }
        // set Accounting sale invoice total
        if (saleInvoice != null) {
            saleInvoice.setText(wfmStrings.saleInvoices() + " (0)");
        }
        // set Accounting sale quote total
        if (saleQuote != null) {
            saleQuote.setText(wfmStrings.salesQuotes() + " (0)");
        }
        // set Workspace news total
        if (news != null) {
            news.setText(wfmStrings.news() + " (0)");
        }
        // set Workspace news total
        if (wfmEvent != null) {
            wfmEvent.setText(Property.get(Constants.EVENT_LIST, wfmStrings.event()) + " (0)");
        }
        // set Workspace news total
        if (documents != null) {
            documents.setText(wfmStrings.documents() + " (0)");
        }
    }

    public int getSectionTotal(OverallSearchRpc overallSearchRpc, SearchModuleType moduleType, int sectionName) {
        return overallSearchRpc.getOverallSearchMap().get(moduleType).getModuleOveralSearchMap().get(sectionName).getTotal();
    }

    public boolean isCheckedModuleType(OverallSearchRpc overallSearchRpc, SearchModuleType moduleType, int sectionName) {
        return overallSearchRpc.getOverallSearchMap().containsKey(moduleType) && overallSearchRpc.getOverallSearchMap().get(moduleType).getModuleOveralSearchMap().containsKey(sectionName);
    }

    public void initSearchEngine(final AdvancedSearchRpc advancedSearchRpc) {
        LoadingPanel.loading(true);
        viewWidget.showContent(ShowSearchViewType.ResultSearchView);
        CommonService.App.get().searchByAllModule(advancedSearchRpc, new AsyncCallback<OverallSearchRpc>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(OverallSearchRpc result) {
                LoadingPanel.loading(false);
                fullSearchData(result, advancedSearchRpc);
                setSelected(curSelectLabel, selectSectionType);
                setSelectSectionName(curSelectLabel != null ? curSelectLabel.getText() : "");
                viewWidget.initSearchEngine(selectSectionType);
            }
        });

    }

    private void setSelected(Label currentSelect, int selectSectionType) {
        if (curSelectLabel != null) {
            curSelectLabel.removeStyleName("wft-search-select");
            curSelectLabel.setStyleName("wft-search-link");
        }
        if (selectSectionType != -1) {
            currentSelect.removeStyleName("wft-search-link");
            currentSelect.setStyleName("wft-search-select");
            curSelectLabel = currentSelect;
        }

        this.selectSectionType = selectSectionType;
    }

    private void setSelectSectionType(int selectSectionType) {
        if (this.selectSectionType == -1) {
            this.selectSectionType = selectSectionType;
        }
    }

    private void setSelectSectionType(int selectSectionType, Label selectLabel) {
        if (curSelectLabel == null) {
            curSelectLabel = selectLabel;
        }
        setSelectSectionType(selectSectionType);
    }

    private void setSelectSectionName(String text) {
        selectSectionName.setText(text);
    }

    public void isShowModuleSection(int section, boolean showSection) {
        isShowCheckedSection(getSectionLabel(section), showSection, section);
    }

    private Label getSectionLabel(int section) {
        switch (section) {
            case ModuleSectionConstants.TASK: {
                return task;
            }
            case ModuleSectionConstants.PROJECT: {
                return project;
            }
            case ModuleSectionConstants.CRM_CONTACT: {
                return crmContact;
            }
            case ModuleSectionConstants.CRM_LEAD: {
                return crmLead;
            }
            case ModuleSectionConstants.CRM_ACCOUNT: {
                return crmAccount;
            }
            case ModuleSectionConstants.CRM_OPPORTUNITY: {
                return crmOpportunity;
            }
            case ModuleSectionConstants.CRM_CASE: {
                return crmCase;
            }
            case ModuleSectionConstants.SALE_INVOICE: {
                return saleInvoice;
            }
            case ModuleSectionConstants.SALE_QUOTE: {
                return saleQuote;
            }
            case ModuleSectionConstants.PURCHASE_ORDER: {
                return purchaseOrder;
            }
            case ModuleSectionConstants.NEWS: {
                return news;
            }
            case ModuleSectionConstants.EVENT: {
                return wfmEvent;
            }
            case ModuleSectionConstants.DOCUMENTS: {
                return documents;
            }
        }
        return null;
    }

    private void isShowCheckedSection(Label label, boolean showSection, int section) {
        label.setVisible(showSection);
        if ("wft-search-select".equals(label.getStyleName())) {
            label.removeStyleName("wft-search-select");
            label.setStyleName("wft-search-link");
        }
        if (section == selectSectionType && !showSection) {
            HashSet<Integer> sectionSet = new HashSet<>();
            HashMap<SearchModuleType, AdvancedModuleSearchRpc> advancedSectionMap = viewWidget.getAdvancedSearchRpc().getModuleSearchMap();
            for (SearchModuleType key : advancedSectionMap.keySet()) {
                sectionSet.addAll(advancedSectionMap.get(key).getModuleSectionSearch());
            }
            if (sectionSet.size() == 0) {
                selectSectionType = -1;
                setSelectSectionName("");
                curSelectLabel = null;
            } else {
                selectSectionType = sectionSet.iterator().next();
                curSelectLabel = getSectionLabel(selectSectionType);
            }
        } else if (selectSectionType == -1 && showSection) {
            selectSectionType = section;
            curSelectLabel = label;
        }
    }
}