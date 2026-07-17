package com.edatasite.workforce.gwt.task.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.localization.TaskMessages;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskServiceAsync;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDialogContent;

/**
 * User: Anvar Akramov
 * Date: 21.11.2008
 * Time: 19:02:53
 */
public class WorkstreamSummaryView extends CustomForm implements Constants, Colapse {

    private final Integer workstreamID;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final TaskMessages taskMessages = TaskMessages.App.get();
    private final TaskServiceAsync taskService = TaskService.App.get();
    private KpiModal shell;
    private DataListBox listBox;
    private Button cancel;
    private Button ok;
    private KpiCheckBox withAllTasksAndSUBW;
    private final Integer defaultDescriptionCharacterLimit = Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT;
    private WorkstreamSingleItem workstreamItem;

    private Integer workStreamID;
    private TextArea2 description;
    private HTML name, number, startDate, endDate;
    private FlowPanel project, parentWorkstream;


    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    public WorkstreamSummaryView(Integer workstreamID) {
        super("summary", wfmStrings.summaryView());
        this.workstreamID = workstreamID;
    }

    public HorizontalPanel getPercentCompletedPanel(String percent) {
        HorizontalPanel panelBackground = new HorizontalPanel();
        HorizontalPanel panelPercent = new HorizontalPanel();
        panelBackground.setStyleName("completed_back");
        panelBackground.setSize("94px", "17px");
        panelPercent.setStyleName("completed_percent");
        panelPercent.setSize(percent != null ? percent : 0 + "px", "14px");
        if (!"0.0".equals(percent) || !"".equals(percent)) {
            panelPercent.add(new HTML((percent != null && !"0".equals(percent) ? ("&nbsp;&nbsp;" + formatToDouble(percent)) : "0.0") + "%"));
            panelPercent.setTitle(formatToDouble(percent != null ? percent : "0.0") + "% " + wfmStrings.completed());
        } else {
            panelPercent.add(new HTML("0" + "%"));
            panelPercent.setTitle("0" + "% " + wfmStrings.completed());
        }
        panelPercent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        panelBackground.add(panelPercent);
        return panelBackground;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initInternal();
        return null;
    }

    private void initInternal() {
        number = initHTML();
        number.ensureDebugId("workstream_number");

        name = initHTML();
        name.addStyleName(DEFAULT_WIDTH);

        description = new TextArea2(DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        description.hideCharacterLimitPanel();
        description.ensureDebugId("description");

        project = new FlowPanel();
        project.addStyleName(DEFAULT_WIDTH);

        startDate = initHTML();
        startDate.addStyleName(DEFAULT_WIDTH);
        endDate = initHTML();
        endDate.addStyleName(DEFAULT_WIDTH);

        parentWorkstream = new FlowPanel();
        parentWorkstream.ensureDebugId("Workstream");

        addTitleField(CustomFormConstants.WORKSTREAM.WORKSTREAM_DETAILS, projectStrings.workstreamDetails());
        addField(CustomFormConstants.WORKSTREAM.NUMBER, number, getTitle(projectStrings.workstreamNumber()));
        //name.addStyleName(DEFAULT_WIDTH);
        addField(CustomFormConstants.WORKSTREAM.NAME, name, getTitle(wfmStrings.name()));
        //area.addStyleName(DEFAULT_WIDTH);
        addField(CustomFormConstants.WORKSTREAM.DESCRIPTION, description, getTitle(wfmStrings.description()));

        addField(CustomFormConstants.WORKSTREAM.PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        addField(CustomFormConstants.WORKSTREAM.START_DATE, startDate, getTitle(wfmStrings.startDate()));
        addField(CustomFormConstants.WORKSTREAM.END_DATE, endDate, getTitle(wfmStrings.endDate()));

        addField(CustomFormConstants.WORKSTREAM.PARENT_WORKSTREAM, parentWorkstream, getTitle(wfmStrings.parent()));
        show();
    }

    @Override
    protected void addButtons() {
        addEditButton().addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("workstream|edit/" + workstreamID));
        addRemoveButton().addClickHandler(event -> onShellPopup());
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        taskService.getWorkstreamSummary(workstreamID, true, new AbstractAsyncCallback<WorkstreamSingleItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(WorkstreamSingleItem workstreamSingleItem) {
                LoadingPanel.loading(false);
                workstreamItem = new WorkstreamSingleItem();
                workstreamItem = workstreamSingleItem;
                drawWorkstreamSummary(workstreamSingleItem);
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WORKSTREAM_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void drawWorkstreamSummary(WorkstreamSingleItem result) {
        if (result.getNumberData() != null) {
            number.setHTML(result.getNumberData().getNumberString());
        }
        name.setHTML(result.getName());

        description.setWidth("21em");
        description.setReadOnly(true);
        if (result.getDescription() != null) {
            description.setText(result.getDescription().replace("\r\n", "\\r\\n"));
        }

        Widget projectLink = new SimpleLink(result.getProjectName(), "project|summary/" + result.getProjectID(), result.getProjectName(), result.getNumberData().getLastNumberString());
        if (result.getProjectID() != null && !Integer.valueOf(0).equals(result.getProjectID())) {
            project.add(projectLink);
        } else {
            project.add(new HTML(result.getProjectName()));
        }

        if (result.getStartDate() != null) {
            startDate.setHTML(DateUtils.format(result.getStartDate()));
        }
        if (result.getEndDate() != null) {
            endDate.setHTML(DateUtils.format(result.getEndDate()));
        }

        Widget parentWorkstreamName = new SimpleLink(result.getParentWSName(), "workstream|summary/" + result.getParentWSID(), result.getParentWSName(), result.getNumberData().getFirstNumberString());
        if (result.getParentWSName() != null && !"".equals(result.getParentWSName()) && !wfmStrings.notAvailable().equals(result.getParentWSName())) {
            parentWorkstream.add(parentWorkstreamName);
        } else {
            parentWorkstream.add(new HTML(result.getParentWSName()));
        }
    }

    private String formatToDouble(String d) {
        return numberFormat.format(parseToDouble(d));
    }

    private double parseToDouble(String d) {
        return Double.parseDouble(d.replace(",", ""));
    }

    private void onShellPopup() {
        shell = new KpiModal();
        shell.setTitle(projectStrings.deleteWorkstream());
//        shell.setSize(350, 250);
        shell.setWidth(350);

        HTML message = new HTML(taskMessages.areYouSureYouWantToDeleteWorkstream(workstreamItem.getName()));
        HTML icon = new HTML("<span class='my-mbox-icon my-mbox-question' style='height:50px;width:40px;display:block'></span>");
        HTML listBoxLabel = new HTML(projectStrings.moveSubworkstreamsAndTasksTo());

        listBox = new DataListBox();
        listBox.setWidth("150px");
        taskService.getWorkstreamsSomeParent(workstreamID, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                listBox.setItems(result);
                deleteWorkstream(workstreamItem);
            }
        });

        ok = new Button(wfmStrings.ok());
        cancel = new Button(wfmStrings.no());
        withAllTasksAndSUBW = new KpiCheckBox();
        withAllTasksAndSUBW.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                listBox.setSelectedNullLabel();
                listBox.setStyleName("");
                listBox.setEnabled(false);

            } else {
                listBox.setEnabled(true);
            }
        });

        final HorizontalPanelDiv buttonPanel = new HorizontalPanelDiv();
        buttonPanel.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
        buttonPanel.setMarginRight(30);
        buttonPanel.setMarginBottom(10);
        buttonPanel.setStyleName("workforce");
        buttonPanel.add(10, ok, cancel);

        HorizontalPanelDiv checkBoxPanel = new HorizontalPanelDiv();
        HTML dltW = new HTML(projectStrings.deleteWithAllSubworkstreamsAndTasksInIt());
        dltW.getElement().getStyle().setVerticalAlign(com.google.gwt.dom.client.Style.VerticalAlign.TOP);
        checkBoxPanel.add(3, withAllTasksAndSUBW, dltW);

        final FlexTable grid = new FlexTable();
        grid.setCellSpacing(7);
        grid.setCellPadding(5);
        grid.setWidget(0, 0, icon);
        grid.setWidget(0, 1, message);
        grid.setWidget(1, 0, checkBoxPanel);
        grid.getFlexCellFormatter().setColSpan(1, 0, 2);
        grid.setHTML(2, 0, projectStrings.orAlternatively());
        grid.getFlexCellFormatter().setColSpan(2, 0, 2);
        grid.setWidget(3, 0, listBoxLabel);
        grid.getFlexCellFormatter().setWidth(3, 0, "115px");
        grid.setWidget(3, 1, listBox);
        final MaterialDialogContent cont = shell.getContent();
        cont.add(grid);
        cont.add(buttonPanel);
    }

    private void deleteWorkstream(final WorkstreamSingleItem workstream) {

        if (listBox != null && listBox.getItems() != null && listBox.getItems().length > 0) {
            shell.open();

            listBox.addValueChangeHandler(event -> {
                listBox.setStyleName("");
                if (listBox.isSomethingSelected()) {
                    withAllTasksAndSUBW.setEnabled(false);
                    withAllTasksAndSUBW.setValue(false);
                } else {
                    withAllTasksAndSUBW.setEnabled(true);
                }
            });

            ok.addClickHandler(event -> {
                if (withAllTasksAndSUBW.getValue()) {
                    ok.setEnabled(false);
                    cancel.setEnabled(false);
                    removedWorkstream(null);
                } else {
                    if (listBox.isSomethingSelected()) {
                        ok.setEnabled(false);
                        cancel.setEnabled(false);
                        removedWorkstream(listBox.getSelectedItem().getId());
                    } else {
                        listBox.setStyleName("x-form-invalid");
                    }
                }
            });

            cancel.addClickHandler(event -> {
                shell.close();
                shell.clear();
            });
        } else {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(projectStrings.deleteWorkstream());
            messageBox.setMessage(taskMessages.areYouSureYouWantToDeleteWorkstream(workstream.getName()));
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    removedWorkstream(null);
                }
            });
            messageBox.open();
        }
    }

    private void removedWorkstream(Integer defaultWorkstreamID) {
        LoadingPanel.loading(true);
        taskService.deleteWorkstream(workstreamID, defaultWorkstreamID, withAllTasksAndSUBW.getValue(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.workStream()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKSTREAM_DELETED, result, WorkstreamSummaryView.this);
                if (shell != null) {
                    shell.close();
                }
                closeTab();
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
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
//                CommonService.App.get().getDefaultDescriptionCharacterLimit(new AbstractAsyncCallback<Integer>() {
//                    @Override
//                    public void success(Integer result) {
//                        defaultDescriptionCharacterLimit = result;
//                        callback.onSuccess(onInitialize());
//                    }
//                });
            }
        });
    }
}
