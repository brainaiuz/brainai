package com.edatasite.workforce.gwt.workstream.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.ui.Clearable;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.treetable.*;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.workstream.client.rpc.WbsService;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * User: Anvar Akramov
 * Date: 12.11.2008
 * Time: 16:08:52
 */
public class WorkstreamChooser extends Composite implements Clearable {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final String NOT_SET_LABEL = wfmStrings.pleaseSelect();

    private WbsItem workStream;
    private Integer projectId;
    private Integer wsId;
    private String projectName = " ";
    private final TextBox workStreamNameBox = new TextBox();

    private KpiModal resultsShell;
    private boolean shellOpen = false;

    private WfmTreeTable treeTable;
    private Integer selectedWorkStreamID;
    private VerticalPanelDiv noDataPanel = new VerticalPanelDiv();

    private HTML projectNameLabel;
    private Command selectedCommand;

    public WorkstreamChooser() {
        super();
        workStreamNameBox.setText(NOT_SET_LABEL);
        workStreamNameBox.addKeyPressHandler(event -> workStreamNameBox.cancelKey());
        initWidget(workStreamNameBox);
    }

    public WorkstreamChooser(Integer projectId) {
        super();
        workStreamNameBox.setText(NOT_SET_LABEL);
        workStreamNameBox.addKeyPressHandler(event -> workStreamNameBox.cancelKey());
        initWidget(workStreamNameBox);
        if (projectId != null) {
            ProjectService.App.get().getProjectAsLookupItem(projectId, new AsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(SelectItem selectItem) {
                    projectName = selectItem.getName();
                    projectNameLabel.setHTML("<b class=\"customTitle\">" + wfmStrings.project() + ": " + "</b>" + projectName);
                }
            });
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void clearSelected() {
        workStreamNameBox.setText(NOT_SET_LABEL);
    }

    public void clearSelection() {
        workStream = null;
        if (!shellOpen) {
            refreshNameBox();
        }
    }

    public boolean getRemoteSort() {
        return false;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getSortDir() {
        return 0;
    }

    public String getSortField() {
        return null;
    }

    public WbsItem getWorkstream() {
        return workStream;
    }

    public TextBox getWorkstreamNameBox() {
        return workStreamNameBox;
    }

    public void publicShowShell() {
        showShell();
    }

    public void reInit() {
        resultsShell = null;
    }

    public void reload() {
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
        if (treeTable != null) {
            treeTable.refresh();
        }
        if (workStream != null) {
            clearSelection();
        }
    }

    public Integer getWsId() {
        return wsId;
    }

    public void setWsId(Integer wsId) {
        this.wsId = wsId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setText(String s) {
        workStreamNameBox.setText(s);
    }

    public void setSelectedWorkstreamId(Integer selectedWorkStreamID) {
        this.selectedWorkStreamID = selectedWorkStreamID;
    }

    public void setSortDir(int dir) {
    }

    public void setSortField(String field) {
    }

    public void setShellOpen(boolean shellOpen) {
        this.shellOpen = shellOpen;
    }

    public void setWorkstream(WbsItem workStream) {
        this.workStream = workStream;
    }

    public void sort(String sortField, int sortDir) {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addShellListener() {
        resultsShell.addCloseHandler(popupPanelCloseEvent -> shellOpen = false);
    }

    private void createResultsShell() {
        treeTable = new WfmTreeTable(getColumn(), getProvider(), getChildProvider(), getDesigner());
        resultsShell = new KpiModal();
        resultsShell.setCloseButton(true);
        noDataPanel = new VerticalPanelDiv();
        shellOpen = true;
        resultsShell.setWidth(650);

        treeTable.setSize("100%", "313px");
        treeTable.setContentPanelHeight("250px");
//        resultsShell.addStyleName("table-dependences__wrapper file--WorkstreamChooser");
//        resultsShell.getModalHeader().removeFromParent(); //https://prnt.sc/sqheok
//
//        treeTable.addStyleName("table-dependences");
//        treeTable.getContantPanel().addStyleName("table-dependences__body");
        resultsShell.add(treeTable);

        projectNameLabel.setHTML("<b class=\"customTitle\">" + wfmStrings.project() + ": " + "</b>" + getProjectName());

        addShellListener();
    }

    private WfmTreeTableColumn[] getColumn() {
        final WfmTreeTableColumn[] treeTableColumns = new WfmTreeTableColumn[4];
        //name
        treeTableColumns[0] = new WfmTreeTableColumn(wfmStrings.name(), 60, object -> {
            final WbsItem item = (WbsItem) object;
            String name;
            name = item.getName();
            HTML nameHTML = new HTML();
            if (name != null && name.length() > 35) {
                nameHTML.setTitle(name);
                name = name.substring(0, 35) + "...";
            }
            nameHTML.setHTML(name);
            return nameHTML;
        });
        //select
        treeTableColumns[1] = new WfmTreeTableColumn(wfmStrings.select(), 10, object -> {
            final WbsItem item = (WbsItem) object;
            final RadioButton selectorRadio = new KpiRadioButton("selectThis");
            selectorRadio.getElement().getStyle().setProperty("width", "17px");
            selectorRadio.getElement().getStyle().setProperty("margin", "0 auto");
            selectorRadio.getElement().getStyle().setProperty("display", "block");
            selectorRadio.addValueChangeHandler(event -> {
                selectorRadio.setValue(true);
                selectorRadio.getElement().setAttribute("checked", null);
                workStream = item;
                if (wsId != null ? !workStream.getId().equals(wsId) : selectedWorkStreamID == null || !workStream.getId().equals(selectedWorkStreamID)) {
                    refreshNameBox();
                    resultsShell.close();
                    if (selectedCommand != null) {
                        selectedCommand.execute();
                    }
                } else {
                    Info.show("Workstream can not be set as a parent for itself", Info.Type.WARNING);
                }
            });
            return selectorRadio;
        });
        treeTableColumns[1].setColumnStyleName("text-center");
        treeTableColumns[1].setAlignment(HorizontalPanel.ALIGN_CENTER);
        //start date
        treeTableColumns[2] = new WfmTreeTableColumn(wfmStrings.startDate(), 15, object -> {
            WbsItem item = (WbsItem) object;
            return item.getStartDate() != null ? DateUtils.format(item.getStartDate()) : "";
        });
        //end date
        treeTableColumns[3] = new WfmTreeTableColumn(wfmStrings.endDate(), 15, object -> {
            WbsItem item = (WbsItem) object;
            return item.getEndDate() != null ? DateUtils.format(item.getEndDate()) : "";
        });
        return treeTableColumns;
    }

    /**
     * Tree Table Top Bar and Bottom Bar
     *
     * @return - wfm table designer
     */
    private WfmTreeTableDesigner getDesigner() {
        return new WfmTreeTableDesigner() {
            @Override
            public void treeTableTopPanel(final WfmToolBar topPanel) {
                topPanel.add(projectNameLabel = new HTML());
            }

            @Override
            public void treeTableBottomPanel(final WfmToolBar bottomPanel) {
                bottomPanel.setHeight("0px");
            }

            @Override
            public void initDataEmptyTable(WfmTreeTableEmptyDataMessage dataMessage) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(projectStrings.currentlyWorkstreamsProject());
                message.setHref("workstream|add/add/" + (getProjectId() > 0 ? getProjectId().toString() : ""));
                message.setTextBeforeLink(projectStrings.addNewWorkstreamByClicking() + " ");
                message.setHref(clickEvent -> resultsShell.close());
                dataMessage.initEmptyMessage(message);
            }
        };
    }

    private WfmTreeTableChildProvider getChildProvider() {
        return new WfmTreeTableChildProvider() {
            @Override
            public boolean isHaveChilds(Object object) {
                WbsItem item = (WbsItem) object;
                return item.hasChildren();
            }

            @Override
            public Object[] getChilds(Object object) {
                return null;
            }
        };
    }

    private WfmTreeTableCallbackProvider getProvider() {
        return (treeItem, item, callback) -> {
            if (projectId != null) {
                noDataPanel.setVisible(false);
                final WbsItem objItem = (WbsItem) treeItem;
                if (item == null) {
                    LoadingPanel.loading(true);
                    WbsService.App.get().getFirstLevelWorkstreams(projectId, new AbstractAsyncCallback<WbsItem[]>() {
                        @Override
                        public void success(WbsItem[] result) {
                            ArrayList<Integer> treeItemIds = new ArrayList<>();
                            if (result != null && result.length > 0) {
                                noDataPanel.setVisible(false);
                                for (WbsItem wbsItem : result) {
                                    if (!treeItemIds.contains(wbsItem.getId())) {
                                        treeItemIds.add(wbsItem.getId());
                                    }
                                }
                            } else if (!noDataPanel.isVisible()) {
                                noDataPanel.setVisible(true);
                            }
                            LoadingPanel.loading(false);
                            LoadingPanel.loading(false);
                            callback.onSuccess(result, item, null, treeItemIds);
                        }
                    });
                } else {
                    LoadingPanel.loading(true);
                    if (selectedWorkStreamID == null || !objItem.getId().equals(selectedWorkStreamID)) {
                        WbsService.App.get().getSubWorkStreams(objItem.getId(), new AbstractAsyncCallback<WbsItem[]>() {
                            @Override
                            public void success(WbsItem[] result) {
                                ArrayList<Integer> treeItemIds = new ArrayList<>();
                                if (result != null) {
                                    for (WbsItem wbsItem : result) {
                                        if (!treeItemIds.contains(wbsItem.getId())) {
                                            treeItemIds.add(wbsItem.getId());
                                        }
                                    }
                                }
                                LoadingPanel.loading(false);
                                LoadingPanel.loading(false);
                                callback.onSuccess(result, item, null, treeItemIds);
                            }
                        });
                    } else {
                        Info.show("Workstream can not be set as a parent for itself", Info.Type.WARNING);
                    }
                }

            }
        };
    }

    private void refreshNameBox() {
        if (workStream == null) {
            workStreamNameBox.setText(NOT_SET_LABEL);
        } else {
            workStreamNameBox.setText(workStream.getName());
        }
    }

    private void showShell() {
        if (resultsShell == null) {
            createResultsShell();
        }
        resultsShell.open();
    }

    public void setSelectedCommand(Command command) {
        this.selectedCommand = command;
    }
}