package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.components.sampleData.RemoveSDModal;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsServiceAsync;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HrmsMyFiles extends DashboardBaseWidget {

    private static final DocumentsServiceAsync documentsService = DocumentsService.App.get();
    private MaterialPanel panel = new MaterialPanel("gwt-wrapper");
    private MaterialPanel widgetContent = new MaterialPanel("widget-content widget-list");
    private MaterialPanel footer = new MaterialPanel("widget-footer");
    private Span loadingbar;
    private RemoveSDModal sdModal = new RemoveSDModal(true);

    private CRMLookUp employees;

    DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat());
    private Integer folderType = F_EMPLOYEE_PROFILE;

    public HrmsMyFiles(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
        sdModal.setWidth("690px");
    }

    @Override
    protected void initInternal() {

        title.removeFromParent();
        filterPanel.removeFromParent();
        headerRow.remove(actionPanel);

        mainPanel.addStyleName("widget--my-files");

        Div titleDiv = new Div("widget-heading__action");
        headerRow.add(titleDiv);
        titleDiv.getElement().setInnerHTML(wfmStrings.myFiles());

        if(Utils.hasRole(DR) || Utils.hasRole(ADMIN)) {
            Div employeeDiv = new Div("widget-heading__dropdown");

            employees = new CRMLookUp(CrmConstants.EMPLOYEE);
            employees.setWidth(MIN_DEFAULT_WIDTH);
            employees.getSuggestBox().addSelectionHandler(event -> {
                start = 0;
                getData();
            });

            employeeDiv.add(employees);
            headerRow.add(employeeDiv);
        }
        headerRow.add(actionPanel);
//        mainPanel.addStyleName("widget--updates widget--updates-links");
//        mainPanel.addStyleName("widget--task-due widget--row-links");
//        mainPanel.add(new DashboardFooter());
        panel.add(widgetContent);
        contentPanel.add(panel);

        WfmButton2 moreButton = new WfmButton2(null, "btn btn-lg btn-block text-center");
        moreButton.getElement().setInnerText(wfmStrings.loadMore());
        if (!enableToShowSample) {
            moreButton.addClickHandler(clickEvent -> {
                this.start = start + 10;
                getData();
            });
        }

        loadingbar = new Span();
        loadingbar.setStyleName("blue widget-loading--svg widget-loading");
        loadingbar.setVisible(false);
        footer.add(loadingbar);
        footer.add(moreButton);
        mainPanel.add(footer);

        initListeners();
    }

    private void initListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_FILES_UPLOADED, HrmsMyFiles.this, (sender, args) -> getData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COMPANY_DOC_LISTING_EDIT, HrmsMyFiles.this, (sender, args) -> getData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILE_DELETE, HrmsMyFiles.this, (sender, args) -> getData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_DOC_LISTING_EDIT, HrmsMyFiles.this, (sender, args) -> getData());
    }

    @Override
    protected void getData() {
        if (start == 0) {
            widgetContent.clear();
        }
//        getSampleData(false);
        getEmployeeDocuments();
    }

    private AbstractAsyncCallback asyncCallback = new AbstractAsyncCallback<ListResult<FileResource>>() {
        @Override
        public void failure(Throwable throwable) {
            loadingbar.setVisible(false);
            clearPanel();
        }

        public void success(ListResult<FileResource> fileResourceListResult) {

            loadingbar.setVisible(false);

            if (fileResourceListResult.getTotal() > widgetContent.getWidgetCount()) {
                footer.setVisible(true);
            } else {
                footer.setVisible(false);
            }

            if (fileResourceListResult.getList().size() > 0) {
                if (start == 0) {
                    clearAddPanel(false);
                }
                initializeTable(fileResourceListResult.getList(), false);
            } else {
                if(fileResourceListResult.getTotal()==0) {
                    noData();
                }
            }
        }
    };

    private void getEmployeeDocuments() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setModule(LayoutRPC.HRMS_SECTION);
        fp.setFolderType(folderType);
        fp.setSortField(FileResource.DATE);

//        fp.setEndDate(DateUtil.addDays(new Date(), -1));
        fp.setStart(start);
        fp.setLimit(10);
        fp.setAscending(false);
//        fp.setDataType(getCode());

        if(Utils.hasRole(DR) || Utils.hasRole(ADMIN)) {
            fp.setEmployeeId(employees.getSelectedItemID());
        }

        if(fp.getEmployeeId()==null) {
            fp.setEmployeeId(Utils.getUserID());

        }

        fp.setEntityID(fp.getEmployeeId());
        fp.setCrmEntityId(fp.getEmployeeId());

        loadingbar.setVisible(true);

//        documentsService.listFile(fp, asyncCallback);



        fp.setFolderType(Constants.F_EMPLOYEE_PROFILE);
        fp.setModule(LayoutRPC.HRMS_SECTION);
//        fp.setViewType(typeCode);
        DocumentsService.App.get().getDocumentList(fp, asyncCallback);
    }

    private void initializeTable(List<FileResource> result, boolean isSample) {

        result.forEach(item -> {

//            String employeeName = F_COMPANY_DOCUMENTS == folderType ? item.getDocumentName() != null ? item.getDocumentName() : item.getFileName() : item.getEntityName();
//            String url = F_COMPANY_DOCUMENTS == folderType ? item.getDownloadUrl() : "#employeeProfile|employeeProfileView/" + item.getEntityID();

            String fileName = item.getFileName() != null ? item.getFileName() : item.getOwner().getName();
            if (fileName.length() > 22) {
                fileName = fileName.substring(0, 22) + "...";
            }

            Div row = new Div("widget-row widget-row--expiry");

            Div passedDays = new Div("widget-row__th");
            Element nameDl = DOM.createElement("dl");
            nameDl.setClassName("widget-row__text-dl");
            Element dd = DOM.createElement("dd");
            Integer days = 0;

            dd.setInnerText(item.getCreationDate() != null ? format.format(item.getCreationDate()) : "");
            nameDl.appendChild(dd);
            passedDays.getElement().appendChild(nameDl);
            row.add(passedDays);

            Div docTypeIcon = new Div("widget-row__icon");
            Element i = DOM.createElement("i");
            i.setClassName("ficon--file-text2");
            docTypeIcon.getElement().appendChild(i);
            row.add(docTypeIcon);


            Div empNames = new Div("widget-row__text");
            Element empNamesDl = DOM.createElement("dl");
            empNamesDl.setClassName("widget-row__text-dl");
            Element empNamesDt = DOM.createElement("dt");
            new KpiToolTip(empNames, item.getFileName());
            Element empNamesDd = DOM.createElement("dd");
            empNamesDt.setInnerText(fileName);
            empNamesDd.setInnerText(item.getDescription() != null ? item.getDescription() : "");
            empNamesDl.appendChild(empNamesDt);
            empNamesDl.appendChild(empNamesDd);
            empNames.getElement().appendChild(empNamesDl);
            row.add(empNames);

            Div widgetRowEnd = new Div("widget-row__end");
            Div btnGroup = new Div("widget-row__button-group");

            //Preview Link
            Div btnGroupEye = new Div("widget-row__button widget-row__button--active");
            MaterialLink previewLink = new MaterialLink();
            new KpiToolTip(previewLink, wfmStrings.preview());
            previewLink.addClickHandler( (event) -> {

                if(item.getBodyId()!=null) {
                    LoadingPanel.loading(true);
                    DocumentsService.App.get().getFileLink(item.getBodyId(), new AbstractAsyncCallback<String>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(String amazonLink) {
                            LoadingPanel.loading(false);
                            item.setAmazonLink(amazonLink);
                            Utils.showImageOrDownloadFile(item, false, amazonLink);
                        }
                    });
                } else if(isSample) {
                    sdModal.open();//MaterialToast.fireToast("Preview of sample data not available.");
                }

            });
            MaterialLabel eyeSpan = new MaterialLabel();
            Element eyeIcon = DOM.createElement("i");
            eyeIcon.setClassName("ficon--eye");
            previewLink.getElement().appendChild(eyeIcon);
            eyeSpan.add(previewLink);
            btnGroupEye.add(eyeSpan);
            btnGroup.add(btnGroupEye);

            //Download Link
            Div cloud = new Div("widget-row__button");
            MaterialLink cloudLink = new MaterialLink();
            new KpiToolTip(cloudLink, wfmStrings.download());
            cloudLink.addClickHandler((event) -> {

                if(item.getBodyId()!=null) {
                    LoadingPanel.loading(true);
                    DocumentsService.App.get().getFileLink(item.getBodyId(), new AbstractAsyncCallback<String>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(String amazonLink) {
                            LoadingPanel.loading(false);
                            item.setAmazonLink(amazonLink);
                            Utils.redirect(item.getDownloadUrl());
                        }
                    });
                } else if(isSample) {
                    sdModal.open();//MaterialToast.fireToast("Download of sample data not available.");
                }

            });
            Element cloudIcon = DOM.createElement("i");
            cloudIcon.setClassName("ficon--download-cloud");
            cloudLink.getElement().appendChild(cloudIcon);
            cloud.add(cloudLink);
            btnGroup.add(cloud);

            //Email link
            /*Div at = new Div("widget-row__button");
            MaterialLink atLink = new MaterialLink();
            new KpiToolTip(atLink, wfmStrings.compose());
            atLink.addClickHandler((event)->{
                if(item.getOwner()!=null) {
                    new ComposeView(item.getOwner().getEmail());
                } else if(isSample) {
                    sdModal.open();
                }
            });
            Element atIcon = DOM.createElement("i");
            atIcon.setClassName("ficon--at");
            atLink.getElement().appendChild(atIcon);
            at.add(atLink);
            btnGroup.add(at);*/

            widgetRowEnd.add(btnGroup);
            row.add(widgetRowEnd);

            widgetContent.add(row);

        });
    }

    private void clearAddPanel(boolean noData) {
        widgetContent.clear();
        panel.clear();
        if (!noData && !fromSettings) {
            contentPanel.removeStyleName(noDataClass);
            contentPanel.clear();
        }
        panel.add(widgetContent);
        contentPanel.add(panel);
    }

    @Override
    public String getCode() {
        return gridItemConfig.getComponentCode();
    }

    @Override
    protected void getSampleData(boolean noData) {

        ArrayList<FileResource> items = new ArrayList<>();

        items = getItemsSample();

        loadingbar.setVisible(false);

        clearAddPanel(noData);

        initializeTable(items, true);
    }

    private ArrayList<FileResource> getItemsSample() {
        ArrayList<FileResource> items = new ArrayList<>();

        FileResource item1 = new FileResource();
        item1.setEntityName("John Smith");
        item1.setName("file1.jpg");
        item1.setDescription("Passport");
        item1.setCreationDate(DateUtil.addDays(new Date(), -11));
        items.add(item1);
//--------------------------------
        FileResource item2 = new FileResource();
        item2.setEntityName("Munir Yamal");
        item2.setName("file2.jpg");
        item2.setDescription("Visa");
        item2.setCreationDate(DateUtil.addDays(new Date(), -23));
        items.add(item2);
//--------------------------------
        FileResource item3 = new FileResource();
        item3.setEntityName("Richard Bale");
        item3.setName("file3.jpg");
        item3.setDescription("Insurance");
        item3.setCreationDate(DateUtil.addDays(new Date(), -37));
        items.add(item3);
//--------------------------------
        FileResource item4 = new FileResource();
        item4.setEntityName("Brian Garrison");
        item4.setName("file4.jpg");
        item4.setDescription("Work permit");
        item4.setCreationDate(DateUtil.addDays(new Date(), -42));
        items.add(item4);
//--------------------------------
        FileResource item5 = new FileResource();
        item5.setEntityName("Andrea Pate");
        item5.setName("file5.jpg");
        item5.setDescription("Work permit");
        item5.setCreationDate(DateUtil.addDays(new Date(), -51));
        items.add(item5);

        return items;
    }
}
