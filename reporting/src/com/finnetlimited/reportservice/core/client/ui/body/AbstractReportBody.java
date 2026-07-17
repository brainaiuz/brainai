package com.finnetlimited.reportservice.core.client.ui.body;

import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.ReportContent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.LazyPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: ${Dilsh0d}
 * Date: 13-Mar-2010
 * Time: 17:52:27
 */
public abstract class AbstractReportBody extends LazyPanel {

    private static final String BODY = IdType.BODY.getName();

    private Integer id;
    private String type;
    private String param;
    private String dataType;
    private String folderType;
    private String name;
    private boolean widgetInitializing;
    private boolean widgetInitialized;

    private HTMLPanel widgetPanel;
    private ReportContent reportContent;

    protected ReportingModuleSettings reportingModuleSettings;

    public AbstractReportBody(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setReportContent(ReportContent reportContent) {
        this.reportContent = reportContent;
    }

    public ReportContent getReportContent() {
        return reportContent;
    }

    public void setParams(Integer id, String type, String param, String folderType) {
        this.id = id;
        this.type = type;
        this.param = param;
        this.folderType = folderType;
    }

    protected Widget createWidget() {
        widgetPanel = new HTMLPanel("");
        DOM.setElementAttribute(widgetPanel.getElement(), "id", BODY);
        return widgetPanel;
    }

    @Override
    public void onLoad() {
        show();
        ensureWidget();
    }

    @Override
    public void ensureWidget() {
        super.ensureWidget();
        ensureWidgetInitialized();
    }

    private void ensureWidgetInitialized() {
        if (widgetInitializing || widgetInitialized) {
            hide();
            return;
        }

        widgetInitializing = true;

        asyncOnInitialize(new AsyncCallback<Widget>() {
            public void onFailure(Throwable reason) {
                hide();
                widgetInitializing = false;
            }

            public void onSuccess(Widget result) {
                hide();
                widgetInitializing = false;
                widgetInitialized = true;

                if (result != null) {
                    widgetPanel.add(result);
                }
                onInitializeComplete();
            }
        });
    }

    public void onInitializeComplete() {
    }

    public void show() {
    }

    public void hide() {
    }

    // Write all user interface widget this is methods

    public abstract Widget onInitialize();

    protected abstract void asyncOnInitialize(final AsyncCallback<Widget> callback);

    public void addWidget(Widget widget) {
        widgetPanel.add(widget, BODY);
    }

    protected void goTo(String historyName) {
        reportContent.goToContent(historyName);
    }

    protected void refreshAndGoTo(String historyType) {
        reportContent.refreshContent(historyType);
    }

    public void goToClearAndCreateContent(String historyName) {
        String[] historyLink = historyName.split("[|]");
        if (historyLink.length < 2) {
            reportContent.setDataType(null);
        }
        reportContent.goToClearAndCreateContent(historyName);
    }

    public void refresh() {

    }

    public void refreshContent() {
        if (widgetInitialized || widgetInitializing) {
            widgetInitialized = false;
            widgetInitializing = false;
            clear();
        }
    }

    public String getFolderType() {
        return folderType;
    }

    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    public ReportingModuleSettings getReportingModuleSettings() {
        return reportingModuleSettings;
    }

    public void setReportingModuleSettings(ReportingModuleSettings reportingModuleSettings) {
        this.reportingModuleSettings = reportingModuleSettings;
    }

    protected void setName(String name) {
        this.name = name;
    }
}

