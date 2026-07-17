package com.edatasite.workforce.gwt.dashboardwidget.client.view.documents;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DASHBOARD_WIDGET_CODE.EXPIRED_DOCUMENTS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DASHBOARD_WIDGET_CODE.EXPIRY_DOCUMENTS;

/**
 * Created by Anvar Akramov on 17/05/2018.
 */
public class DashboardExpiredDocuments extends DashboardBaseWidget {

    private static final DocumentsServiceAsync documentsService = DocumentsService.App.get();
    private MaterialPanel panel = new MaterialPanel("gwt-wrapper");
    private MaterialPanel widgetContent = new MaterialPanel("widget-content widget-list");
    private MaterialPanel footer = new MaterialPanel("widget-footer");
    private Span loadingbar;


    DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat());
    private Integer folderType = F_EMPLOYEE_PROFILE;

    public DashboardExpiredDocuments(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void initInternal() {
        if(EXPIRED_DOCUMENTS.equals(gridItemConfig.getComponentCode())) {
            setTitle(accountingStrings.expiredDocuments());
        } else if(EXPIRY_DOCUMENTS.equals(gridItemConfig.getComponentCode())) {
            setTitle(accountingStrings.expiringDocuments());
        }
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
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_FILES_UPLOADED, DashboardExpiredDocuments.this, (sender, args) -> getData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COMPANY_DOC_LISTING_EDIT, DashboardExpiredDocuments.this, (sender, args) -> getData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILE_DELETE, DashboardExpiredDocuments.this, (sender, args) -> getData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_DOC_LISTING_EDIT, DashboardExpiredDocuments.this, (sender, args) -> getData());
    }

    @Override
    protected void getData() {
        if (start == 0) {
            widgetContent.clear();
        }

//        getSampleData(false);
        if(EXPIRED_DOCUMENTS.equals(gridItemConfig.getComponentCode())) {
            getExpiredDocuments();
        } else if(EXPIRY_DOCUMENTS.equals(gridItemConfig.getComponentCode())) {
            getExpiryDocuments();
        }
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

    private void getExpiredDocuments() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setModule(LayoutRPC.HRMS_SECTION);
        fp.setFolderType(folderType);
        fp.setSortField(FileResource.EXPIRED_DATE);

        fp.setEndDate(DateUtil.addDays(new Date(), -1));
        fp.setStart(start);
        fp.setLimit(10);
        fp.setAscending(false);
        /*fp.setDepartmentId(departmentListBox.getSelectedId());
        fp.setLocationId(locationListBox.getSelectedId());*/
        fp.setDataType(getCode());

        loadingbar.setVisible(true);

        documentsService.listFile(fp, asyncCallback);
    }

    private void getExpiryDocuments() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setModule(LayoutRPC.HRMS_SECTION);
        fp.setSortField(FileResource.EXPIRE_DATE);
        fp.setStart(start);
        fp.setLimit(5);

        loadingbar.setVisible(true);

        documentsService.getCompanyAndEmployeeDocumentExpiryListNew(fp, asyncCallback);
    }

    private void initializeTable(List<FileResource> result, boolean isSample) {

        result.forEach(item -> {

//            String employeeName = F_COMPANY_DOCUMENTS == folderType ? item.getDocumentName() != null ? item.getDocumentName() : item.getFileName() : item.getEntityName();
//            String url = F_COMPANY_DOCUMENTS == folderType ? item.getDownloadUrl() : "#employeeProfile|employeeProfileView/" + item.getEntityID();

            String employeeName = item.getEntityName() != null ? item.getEntityName() : item.getOwner().getName();
            if (employeeName.length() > 22) {
                employeeName = employeeName.substring(0, 22) + "...";
            }

            Div row = new Div(EXPIRED_DOCUMENTS.equals(gridItemConfig.getComponentCode()) ? "widget-row widget-row--expired" : "widget-row widget-row--expiry");

            Div passedDays = new Div("widget-row__th");
            Element nameDl = DOM.createElement("dl");
            nameDl.setClassName("widget-row__text-dl");
            Element dt = DOM.createElement("dt");
            Element dd = DOM.createElement("dd");
            Integer days = 0;

            if(EXPIRED_DOCUMENTS.equals(gridItemConfig.getComponentCode())) {
                days = DateUtil.countDays(item.getExpireDate().getDate(), new Date());

            } if(EXPIRY_DOCUMENTS.equals(gridItemConfig.getComponentCode())) {
                days = DateUtil.countDays(new Date(), item.getExpireDate().getDate());

            }
            String m = "DAY";
            if( 1<days && days<100) {
                m = "DAYS";
            } else if(days>100) {
                m = "MONTHS";
                days = days/30;
            }
            dt.setInnerText( days + " " + m + (EXPIRED_DOCUMENTS.equals(gridItemConfig.getComponentCode()) ? " PASSED" : " LEFT") );

            dd.setInnerText(item.getExpireDate() != null ? format.format(item.getExpireDate().getNonConvertedDate()) : "");
            nameDl.appendChild(dt);
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
            empNamesDt.setInnerText(employeeName);
            empNamesDd.setInnerText(item.getType() != null ? item.getType() : "");
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
                }

            });
            Element cloudIcon = DOM.createElement("i");
            cloudIcon.setClassName("ficon--download-cloud");
            cloudLink.getElement().appendChild(cloudIcon);
            cloud.add(cloudLink);
            btnGroup.add(cloud);

            //Email link
            Div at = new Div("widget-row__button");
            MaterialLink atLink = new MaterialLink();
            new KpiToolTip(atLink, wfmStrings.compose());
            atLink.addClickHandler((event)->{
                if (item.getOwner() != null) {
                    //new ComposeView(item.getOwner().getEmail());
                    SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + item.getOwner().getEmail());
                }
            });
            Element atIcon = DOM.createElement("i");
            atIcon.setClassName("ficon--at");
            atLink.getElement().appendChild(atIcon);
            at.add(atLink);
            btnGroup.add(at);





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

        if(EXPIRED_DOCUMENTS.equals(gridItemConfig.getComponentCode())) {
            items = getExpiredSample();
        } if(EXPIRY_DOCUMENTS.equals(gridItemConfig.getComponentCode())) {
            items = getExpirySample();
        }
        loadingbar.setVisible(false);

        clearAddPanel(noData);

        initializeTable(items, true);
    }

    private ArrayList<FileResource> getExpirySample() {

        ArrayList<FileResource> items = new ArrayList<>();

        FileResource item1 = new FileResource();
        item1.setEntityName("Aaron Ruano");
        item1.setName("file1.jpg");
        item1.setType("Passport");
        item1.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), 1)));
        items.add(item1);

//--------------------------------
        FileResource item2 = new FileResource();
        item2.setEntityName("Garry Dobson");
        item2.setName("file2.jpg");
        item2.setType("Visa");
        item2.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), 24)));
        items.add(item2);
//--------------------------------
        FileResource item3 = new FileResource();
        item3.setEntityName("Enrique Flamenco");
        item3.setName("file3.jpg");
        item3.setType("Insurance");
        item3.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), 31)));
        items.add(item3);
//--------------------------------
        FileResource item4 = new FileResource();
        item4.setEntityName("Steven Broudy");
        item4.setName("file4.jpg");
        item4.setType("Work permit");
        item4.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), 44)));
        items.add(item4);
//--------------------------------
        FileResource item5 = new FileResource();
        item5.setEntityName("William Martes");
        item5.setName("file5.jpg");
        item5.setType("Work permit");
        item5.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), 55)));
        items.add(item5);

        return items;
    }

    private ArrayList<FileResource> getExpiredSample() {
        ArrayList<FileResource> items = new ArrayList<>();

        FileResource item1 = new FileResource();
        item1.setEntityName("John Smith");
        item1.setName("file1.jpg");
        item1.setType("Passport");
        item1.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), -11)));
        items.add(item1);
//--------------------------------
        FileResource item2 = new FileResource();
        item2.setEntityName("Munir Yamal");
        item2.setName("file2.jpg");
        item2.setType("Visa");
        item2.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), -23)));
        items.add(item2);
//--------------------------------
        FileResource item3 = new FileResource();
        item3.setEntityName("Richard Bale");
        item3.setName("file3.jpg");
        item3.setType("Insurance");
        item3.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), -37)));
        items.add(item3);
//--------------------------------
        FileResource item4 = new FileResource();
        item4.setEntityName("Brian Garrison");
        item4.setName("file4.jpg");
        item4.setType("Work permit");
        item4.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), -42)));
        items.add(item4);
//--------------------------------
        FileResource item5 = new FileResource();
        item5.setEntityName("Andrea Pate");
        item5.setName("file5.jpg");
        item5.setType("Work permit");
        item5.setExpireDate(new DateNonConvertable(DateUtil.addDays(new Date(), -51)));
        items.add(item5);

        return items;
    }
}
