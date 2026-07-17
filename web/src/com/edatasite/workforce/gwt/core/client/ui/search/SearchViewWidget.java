package com.edatasite.workforce.gwt.core.client.ui.search;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DocumentsSearchItem;
import com.edatasite.workforce.gwt.core.client.rpc.SearchResultItemList;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.AdvancedModuleSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.AdvancedSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ModuleSectionConstants;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.SearchModuleType;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ShowSearchViewType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 26/11/11
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class SearchViewWidget extends AbsolutePanel {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final DocumentsSearchItem searchItem = new DocumentsSearchItem();
    private final AdvancedSearchRpc advancedSearchRpc = new AdvancedSearchRpc();

    private final int itemsPerPage = 10;

    private Pager pager;
    private SearchResultView resultView;
    private AdvancedSearchWidget advancedSearchWidget;
    private ModuleSearchViewWidget moduleSearchView;


    public SearchViewWidget() {
        init();
    }

    private void init() {
        searchItem.setStart(0);
        searchItem.setLimit(itemsPerPage);
        initResultSearch();
        initAllModuleSearch();
    }

    private void initAllModuleSearch() {
        advancedSearchWidget = new AdvancedSearchWidget();
        advancedSearchWidget.setVisible(false);
        add(advancedSearchWidget);
    }

    public void showContent(ShowSearchViewType contentType) {
        if (ShowSearchViewType.ResultSearchView.equals(contentType)) {
            advancedSearchWidget.setVisible(false);
            pager.setVisible(true);
            resultView.setVisible(true);
        } else {
            pager.setVisible(false);
            resultView.setVisible(false);
            advancedSearchWidget.setVisible(true);
        }
    }

    private void initResultSearch() {
        pager = new Pager(10);
        pager.setWidth("100%");

        resultView = new SearchResultView();

        pager.setPagerEvents((start, limit) -> {
            searchItem.setStart(start);
            searchItem.setKeyword(OverallSearchView.searchKey);
            LoadingPanel.loading(true);
            CommonService.App.get().searchByKeyword(searchItem, new AbstractAsyncCallback<SearchResultItemList>() {
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                public void success(SearchResultItemList searchResultItemList) {
                    LoadingPanel.loading(false);
                    if (searchResultItemList != null) {
                        resultView.setSearchResult(searchItem, searchResultItemList);
                        pager.renderPager(searchResultItemList.getTotalFound(), searchItem.getLimit());
                    }
                }
            });
        });
        add(resultView);
        add(pager);

    }

    public void initSearchEngine(int sectionType) {
        searchItem.setStart(0);
        searchItem.setSectionName(sectionType);
        searchItem.setKeyword(OverallSearchView.searchKey);
        LoadingPanel.loading(true);
        CommonService.App.get().searchByKeyword(searchItem, new AbstractAsyncCallback<SearchResultItemList>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(SearchResultItemList searchResultItemList) {
                LoadingPanel.loading(false);
                if (searchResultItemList != null) {
                    resultView.setSearchResult(searchItem, searchResultItemList);
                    pager.renderPager(searchResultItemList.getTotalFound(), searchItem.getLimit(), 1);
                }
            }
        });
    }

    public class AdvancedSearchWidget extends Composite implements Constants {

        private FlexTable content;
        private KpiCheckBox task;
        private KpiCheckBox project;
        //        private CheckBox crmTask;
        private KpiCheckBox crmCase;
        private KpiCheckBox crmContact;
        private KpiCheckBox crmLead;
        private KpiCheckBox crmAccount;
        private KpiCheckBox crmOpportunity;
        private KpiCheckBox purchaseOrder;
        private KpiCheckBox saleInvoice;
        private KpiCheckBox saleQuote;
        private KpiCheckBox news;
        private KpiCheckBox wfmEvent;
        private KpiCheckBox documents;
        private Button searchBtn;
        private Button cancelBtn;

        public AdvancedSearchWidget() {
            this.init();
        }

        private void init() {
            content = new FlexTable();
            content.setCellPadding(5);
            content.setCellSpacing(5);
            this.initWidget(content);

            int row = 0;
            content.setHTML(row, 0, "<font style='font-size:25px;padding-left:100px' class='customTitle'>" + wfmStrings.advancedSearch() + "</font>");
            content.getFlexCellFormatter().setAlignment(row++, 0, HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_MIDDLE);
            content.setHTML(row, 0, "&nbsp;");

            HorizontalPanel searchPanel = new HorizontalPanel();
            searchPanel.setSpacing(10);
            searchPanel.setStyleName("wft-advancedSearchPanel");
            content.setWidget(row++, 0, searchPanel);
            // Workspace
            if (Utils.marketplaceShowMenu(WORKSPACE_PAGE)) {
                searchPanel.add(this.getWorkspaceAdvancedModuleSearch());
            }
            // Project Management
            if (Utils.marketplaceShowMenu(PROJECT_MANAGEMENT_PAGE)) {
                searchPanel.add(this.getPmAdvancedModuleSearch());
            }
            // CRM
            if (Utils.marketplaceShowMenu(CRM_PAGE)
                    && (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(SALESMAN) || Utils.hasRole(SALESPERSON) || Utils.hasRole(CUSTOMER_SERVICE_REPRESENTATIVE))) {
                searchPanel.add(this.getCRMAdvancedModuleSearch());
            }
            // Accounting
            if ( Utils.marketplaceShowMenu(ACCOUNTING_PAGE)) {
                searchPanel.add(this.getAccountingAdvancedModuleSearch());
            }
            // Documents
            if (Utils.marketplaceShowMenu(DOC_MY_FOLDER) && !Utils.hasRole(CLIENT)) {
                searchPanel.add(this.getDocumentsAdvansedModuleSearch());
            }
            searchBtn = new Button(wfmStrings.search());
            searchBtn.addClickHandler(event -> {
                showContent(ShowSearchViewType.ResultSearchView);
                moduleSearchView.initSearchEngine(getAdvancedSearchRpc());
            });

            cancelBtn = new Button(wfmStrings.cancel());
            cancelBtn.addClickHandler(event -> showContent(ShowSearchViewType.ResultSearchView));

            HorizontalPanel buttonPanel = new HorizontalPanel();
            buttonPanel.setSpacing(5);
            buttonPanel.add(searchBtn);
            buttonPanel.add(cancelBtn);

            content.setWidget(row, 0, buttonPanel);
            content.getFlexCellFormatter().setAlignment(row, 0, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
            content.getFlexCellFormatter().getElement(row, 0).getStyle().setPaddingLeft(150, Style.Unit.PX);

        }

        private Widget getDocumentsAdvansedModuleSearch() {
            FlexTable table = new FlexTable();
            table.setCellPadding(5);
            table.setCellSpacing(5);

            documents = new KpiCheckBox(wfmStrings.documents());
            documents.setValue(true);
            documents.addClickHandler(clickEvent -> changeOveralSearchData(documents.getValue(), SearchModuleType.Documents, ModuleSectionConstants.DOCUMENTS));

            int row = 0;
            table.setHTML(row++, 0, "<b>" + wfmStrings.documents() + "</b>");
            //Documents
            table.setWidget(row, 0, documents);
            table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);
            changeOveralSearchData(true, SearchModuleType.Documents, ModuleSectionConstants.DOCUMENTS);

            return table;
        }

        private Widget getWorkspaceAdvancedModuleSearch() {
            FlexTable table = new FlexTable();
            table.setCellPadding(5);
            table.setCellSpacing(5);

            news = new KpiCheckBox(wfmStrings.news());
            news.setValue(true);
            news.addClickHandler(clickEvent -> changeOveralSearchData(news.getValue(), SearchModuleType.Workspace, ModuleSectionConstants.NEWS));

            wfmEvent = new KpiCheckBox(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()));
            wfmEvent.setValue(true);
            wfmEvent.addClickHandler(clickEvent -> changeOveralSearchData(wfmEvent.getValue(), SearchModuleType.Workspace, ModuleSectionConstants.EVENT));

            int row = 0;
            table.setHTML(row++, 0, "<b>" + wfmStrings.myWorkspace() + "</b>");
            //News
            table.setWidget(row, 0, news);
            table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);
            //Event
            table.setWidget(row, 0, wfmEvent);
            table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);

            changeOveralSearchData(true, SearchModuleType.Workspace, ModuleSectionConstants.NEWS);
            changeOveralSearchData(true, SearchModuleType.Workspace, ModuleSectionConstants.EVENT);

            return table;
        }

        private Widget getAccountingAdvancedModuleSearch() {
            FlexTable table = new FlexTable();
            table.setCellPadding(5);
            table.setCellSpacing(5);

            purchaseOrder = new KpiCheckBox(wfmStrings.purchaseOrders());
            purchaseOrder.addClickHandler(clickEvent -> changeOveralSearchData(purchaseOrder.getValue(), SearchModuleType.Accounting, ModuleSectionConstants.PURCHASE_ORDER));

            saleInvoice = new KpiCheckBox(wfmStrings.saleInvoices());
            saleInvoice.addClickHandler(clickEvent -> changeOveralSearchData(saleInvoice.getValue(), SearchModuleType.Accounting, ModuleSectionConstants.SALE_INVOICE));

            saleQuote = new KpiCheckBox(wfmStrings.salesQuotes());
            saleQuote.addClickHandler(clickEvent -> changeOveralSearchData(saleQuote.getValue(), SearchModuleType.Accounting, ModuleSectionConstants.SALE_QUOTE));

            int row = 0;
            if ("true".equals(Utils.userSettings.get(ACCOUNTING_IS_SETUP))) {
                table.setHTML(row++, 0, "<b>" + wfmStrings.accounts() + "</b>");
                boolean isHashAccessForPMRole = Utils.hashAccessForPMRole();
                        /*|| Integer.valueOf(22026).equals(Utils.getCompanyID()) || Integer.valueOf(24021).equals(Utils.getCompanyID()));*/
                if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT) || isHashAccessForPMRole || Utils.hasRole(CLIENT)) {
                    saleInvoice.setValue(true);
                    saleQuote.setValue(true);
                    //Sale Invoice
                    table.setWidget(row, 0, saleInvoice);
                    table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);
                    //Sale Quote
                    table.setWidget(row, 0, saleQuote);
                    table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);

                    changeOveralSearchData(true, SearchModuleType.Accounting, ModuleSectionConstants.SALE_INVOICE);
                    changeOveralSearchData(true, SearchModuleType.Accounting, ModuleSectionConstants.SALE_QUOTE);
                }
                if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT) || isHashAccessForPMRole) {
                    purchaseOrder.setValue(true);
                    // Purchase Order
                    table.setWidget(row, 0, purchaseOrder);
                    table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);
                    changeOveralSearchData(true, SearchModuleType.Accounting, ModuleSectionConstants.PURCHASE_ORDER);
                }
            }

            return table;
        }

        private Widget getCRMAdvancedModuleSearch() {
            FlexTable table = new FlexTable();
            table.setCellPadding(5);
            table.setCellSpacing(5);

//            crmTask = new CheckBox(wfmStrings.task());
//            crmTask.addClickHandler(new ClickHandler() {
//                @Override
//                public void onClick(ClickEvent clickEvent) {
//                    changeOveralSearchData(crmTask.getValue(), SearchModuleType.CRM, ModuleSectionConstants.CRM_TASK);
//                }
//            });

            crmAccount = new KpiCheckBox(wfmStrings.accounts());
            crmAccount.addClickHandler(clickEvent -> changeOveralSearchData(crmAccount.getValue(), SearchModuleType.CRM, ModuleSectionConstants.CRM_ACCOUNT));

            crmContact = new KpiCheckBox(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.crmContact(), wfmStrings.contacts()));
            crmContact.addClickHandler(clickEvent -> changeOveralSearchData(crmContact.getValue(), SearchModuleType.CRM, ModuleSectionConstants.CRM_CONTACT));

            crmLead = new KpiCheckBox(Property.getPluralWithObjectCode(Constants.LEADS, wfmStrings.leads()));
            crmLead.addClickHandler(clickEvent -> changeOveralSearchData(crmLead.getValue(), SearchModuleType.CRM, ModuleSectionConstants.CRM_LEAD));

            crmCase = new KpiCheckBox(wfmStrings.cases());
            crmCase.addClickHandler(clickEvent -> changeOveralSearchData(crmCase.getValue(), SearchModuleType.CRM, ModuleSectionConstants.CRM_CASE));

            crmOpportunity = new KpiCheckBox(wfmStrings.opportunities());
            crmOpportunity.addClickHandler(clickEvent -> changeOveralSearchData(crmOpportunity.getValue(), SearchModuleType.CRM, ModuleSectionConstants.CRM_OPPORTUNITY));

            int row = 0;
            if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(SALESMAN) || Utils.hasRole(SALESPERSON)) {
                table.setHTML(row++, 0, "<b>" + wfmStrings.crm() + "</b>");
                crmContact.setValue(true);
                crmLead.setValue(true);
                crmAccount.setValue(true);
//                crmTask.setValue(true);
                crmOpportunity.setValue(true);

                //Lead
                table.setWidget(row, 0, crmLead);
                table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);

                // Opportunity
                table.setWidget(row, 0, crmOpportunity);
                table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);

                // Account
                table.setWidget(row, 0, crmAccount);
                table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);

                //Contact
                table.setWidget(row, 0, crmContact);
                table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);

                changeOveralSearchData(true, SearchModuleType.CRM, ModuleSectionConstants.CRM_CONTACT);
                changeOveralSearchData(true, SearchModuleType.CRM, ModuleSectionConstants.CRM_LEAD);
                changeOveralSearchData(true, SearchModuleType.CRM, ModuleSectionConstants.CRM_ACCOUNT);
                changeOveralSearchData(true, SearchModuleType.CRM, ModuleSectionConstants.CRM_OPPORTUNITY);
            }
            // Case
            if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(CUSTOMER_SERVICE_REPRESENTATIVE) || Utils.hasRole(SALESPERSON)) {
                if (row == 0) {
                    table.setHTML(row++, 0, "<b style='#E64F32'>" + wfmStrings.crm() + "</b>");
                }
                crmCase.setValue(true);
                table.setWidget(row, 0, crmCase);
                table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);

                changeOveralSearchData(true, SearchModuleType.CRM, ModuleSectionConstants.CRM_CASE);
            }

            return table;
        }

        private Widget getPmAdvancedModuleSearch() {
            FlexTable table = new FlexTable();
            table.setCellPadding(5);
            table.setCellSpacing(5);

            task = new KpiCheckBox(wfmStrings.tasks());
            task.setValue(true);
            task.addClickHandler(clickEvent -> changeOveralSearchData(task.getValue(), SearchModuleType.PM, ModuleSectionConstants.TASK));

            project = new KpiCheckBox(wfmStrings.projects());
            project.setValue(true);
            project.addClickHandler(clickEvent -> changeOveralSearchData(project.getValue(), SearchModuleType.PM, ModuleSectionConstants.PROJECT));

            int row = 0;
            table.setHTML(row++, 0, "<b>" + wfmStrings.projects() + "</b>");
            //Task
            table.setWidget(row, 0, task);
            table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);
            //Project
            table.setWidget(row, 0, project);
            table.getFlexCellFormatter().getElement(row++, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);

            changeOveralSearchData(true, SearchModuleType.PM, ModuleSectionConstants.TASK);
            changeOveralSearchData(true, SearchModuleType.PM, ModuleSectionConstants.PROJECT);

            return table;
        }

        private void changeOveralSearchData(Boolean value, SearchModuleType moduleName, int moduleSection) {
            if (!advancedSearchRpc.getModuleSearchMap().containsKey(moduleName)) {
                advancedSearchRpc.getModuleSearchMap().put(moduleName, new AdvancedModuleSearchRpc());
            }
            if (value) {
                advancedSearchRpc.getModuleSearchMap().get(moduleName).getModuleSectionSearch().add(moduleSection);
            } else {
                advancedSearchRpc.getModuleSearchMap().get(moduleName).getModuleSectionSearch().remove(moduleSection);
            }
            if (moduleSearchView != null) {
                moduleSearchView.isShowModuleSection(moduleSection, value);
            }
        }
    }

    public void setModuleSearchView(ModuleSearchViewWidget moduleSearchView) {
        this.moduleSearchView = moduleSearchView;
    }

    public AdvancedSearchRpc getAdvancedSearchRpc() {
        advancedSearchRpc.setSearchKey(OverallSearchView.searchKey);
        return advancedSearchRpc;
    }
}
