package com.edatasite.workforce.gwt.hrms.client.ui.orgchart;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;


/**
 * Created by dilsh0d on 28.03.16.
 */
public class HrEmployeeOrgChart extends Composite {
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private boolean showKanbanView = false;
    private boolean levelActive = false;
    private SvgIcon listKanbanSwitch;
    private ActionButton kanbanViewSwitcher;
    private boolean isFirstInit = true;
    private MaterialLink landscape;

    interface EmployeeOrgChartUiBinder extends UiBinder<HTMLPanel, HrEmployeeOrgChart> {
    }

    private static final EmployeeOrgChartUiBinder ourUiBinder = GWT.create(EmployeeOrgChartUiBinder.class);

    @UiField
    HTMLPanel panel;
    @UiField
    MaterialButton refresh;
    @UiField
    DivElement content;
    @UiField
    DivElement wrapper;
    @UiField
    DivElement imageDiv;
    @UiField
    DivElement main;
    @UiField
    SpanElement contactButton;
    @UiField
    DivElement structure1;
    @UiField
    DivElement structure2;
    @UiField
    HeadingElement structure3;
    @UiField
    HTMLPanel switchDiv;
    @UiField
    DataListBox levelOptionList;
    @UiField
    HTMLPanel exportBtn;

    Integer scrollLeft;
    Integer scrollTop;
    Integer cursorX;
    Integer cursorY;
    Boolean isDragging;

    public HrEmployeeOrgChart() {
        onInit();
    }

    private void onInit() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
//        refresh.getElement().getStyle().setPosition(Style.Position.FIXED);
        refresh.ensureDebugId("reload_button");
        refresh.addStyleName("btn--icon");
//        refresh.getElement().getStyle().setRight(100, Style.Unit.PX);
        refresh.add(new SvgIcon(SvgEnum.rotateCw));
        new MaterialTooltip(refresh, wfmStrings.refresh()).setPosition(Position.TOP);

        panel.addStyleName("orgChartParent");
        getLevelOfEmployees(levelOptionList);


        kanbanViewSwitcher = new ActionButton("", "btn btn--icon");
        listKanbanSwitch = new SvgIcon(SvgEnum.barChart);
        setSwitchStyle(showKanbanView);
        kanbanViewSwitcher.add(listKanbanSwitch);
        kanbanViewSwitcher.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            showKanbanView = !showKanbanView;
            setSwitchStyle(showKanbanView);
            getData(showKanbanView, levelOptionList, false);
        });
        switchDiv.add(kanbanViewSwitcher);


        getData(showKanbanView, levelOptionList, levelActive);
        wrapper.getStyle().setDisplay(Style.Display.NONE);
        contactButton.getStyle().setCursor(Style.Cursor.POINTER);
        contactButton.setInnerHTML(Property.get(Constants.Contacts, hrmsStrings.contactUs(), wfmStrings.contact()));
        DOM.sinkEvents(refresh.getElement(), Event.ONCLICK);
//        DOM.setEventListener(refresh.getElement(), event -> getData());
        refresh.addClickHandler(clickEvent -> {
            levelOptionList.reset();
            getData(showKanbanView, levelOptionList, false);
            LoadingPanel.loading(true);
        });

        //Level Option List
        levelOptionList.addValueChangeHandler(clickEvent -> {
//                    LoadingPanel.loading(true);
                    levelActive = true;
                    getLevelOfEmployees(levelOptionList);
                }
        );

        //Export to PDF Panel
        FlowPanel flowPanelOrgChart = new FlowPanel();
        flowPanelOrgChart.setStyleName("operPanel__actions");

        MaterialMenuBar importExportMenu = new MaterialMenuBar();
        importExportMenu.setClass("btn-group dropdown-kit--arrow--below");
        flowPanelOrgChart.add(importExportMenu);

        if (Utils.isDevhost() && Utils.getCompanyID().equals("65159")) {
            MaterialLink ieLink = new MaterialLink();//import export button link for listing top panel
            ieLink.setTooltip(wfmStrings.importExport());
            ieLink.setTooltipPosition(Position.TOP);
            ieLink.setHref("#");
            ieLink.setClass("btn btn--icon btn--white");

            Icon ieIcon = new Icon();//import export icon for listing top panel
            ieIcon.setClass("ficon--download-cloud");
            ieLink.add(ieIcon);

            importExportMenu.add(ieLink);
            exportBtn.add(flowPanelOrgChart);
            ieLink.add(HrEmployeeOrgChart.this::getLandscapeLink);
            ieLink.addClickHandler(clickEvent -> {
                LoadingPanel.loading(true);
                sendPdfRequest();
                LoadingPanel.loading(false);
            });

        }
    }

    private void setSwitchStyle(boolean active) {
        if (active) {
            new MaterialTooltip(kanbanViewSwitcher, wfmStrings.kanbanView()).setPosition(Position.TOP);
            listKanbanSwitch.addClassName("active");
        } else {
            listKanbanSwitch.removeClassName("active");
            new MaterialTooltip(kanbanViewSwitcher, wfmStrings.listView()).setPosition(Position.TOP);
        }

    }

    private void getData(boolean isShowView, DataListBox levelOptionList, boolean levelActive) {
        Integer selectedLevel = levelOptionList.getSelectedId();
        LoadingWidgets.get("getData").show();
        EmployeeService.App.get().getEmployeeGraphChart(isShowView, selectedLevel, levelActive, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingWidgets.get("getData").hide();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(String result) {
                content.setInnerHTML("");
                LoadingWidgets.get("getData").hide();
                if (result != null && !result.isEmpty()) {
                    content.setInnerHTML(result);
                    content.getStyle().setDisplay(Style.Display.BLOCK);
                    wrapper.getStyle().setDisplay(Style.Display.NONE);
                    scrollLeft = 0;
                    scrollTop = 0;
                    cursorX = 0;
                    cursorY = 0;
                    isDragging = false;
                    DOM.sinkEvents(content, Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP);
                    DOM.setEventListener(content, event -> {
                        switch (DOM.eventGetType(event)) {
                            case Event.ONMOUSEDOWN:
//                                content.getStyle().setCursor(Style.Cursor.valueOf("grabbing"));
                                scrollLeft = content.getScrollLeft();
                                scrollTop = content.getScrollTop();
                                cursorX = event.getClientX();
                                cursorY = event.getClientY();
                                isDragging = true;
                                content.addClassName("dragging");
                                break;
                            case Event.ONMOUSEMOVE:
                                if (isDragging) {
                                    content.setScrollTop(scrollTop - (event.getClientY() - cursorY));
                                    content.setScrollLeft(scrollLeft - (event.getClientX() - cursorX));
                                }
                                break;
                            case Event.ONMOUSEUP:
                                isDragging = false;
                                content.removeClassName("dragging");
                            default:
                                break;
                        }
                    });
                } else {
                    refresh.getElement().getStyle().setDisplay(Style.Display.NONE);
                    content.getStyle().setDisplay(Style.Display.NONE);
                    wrapper.getStyle().setDisplay(Style.Display.BLOCK);
                    Image image = new Image();
                    image.setUrl("/mainStyles/images/OrganizationChart.png");
                    image.getElement().getStyle().setWidth(400, Style.Unit.PX);
                    image.getElement().getStyle().setHeight(300, Style.Unit.PX);
                    imageDiv.appendChild(image.getElement());
                    structure1.setInnerHTML(hrmsStrings.structure1());
                    structure2.setInnerHTML(hrmsStrings.structure2());
                    structure3.setInnerHTML(hrmsStrings.structure3() + " ");
                    structure3.appendChild(contactButton);


                    DOM.sinkEvents(contactButton, Event.ONCLICK);
                    DOM.setEventListener(contactButton, event -> {
                        MainLayout.get().onSendfeedBack(Constants.SUPERVISOR_STRUCTURE);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FEEDBACK_BUTTON_CLICKED, "contactUs", HrEmployeeOrgChart.this);
                    });
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void getLevelOfEmployees(DataListBox levelOptionList) {
        LoadingWidgets.get("getData").show();
        EmployeeService.App.get().getLevelOfEmployees(new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                LoadingWidgets.get("getData").hide();
            }

            @Override
            public void success(Integer result) {
                ArrayList<SelectItem> items = new ArrayList<>();
                for (int i = 2; i <= result; i++) {
                    items.add(new SelectItem(i, wfmStrings.level() + " " + (i - 1)));
                }
                levelOptionList.setItems(items.toArray(new SelectItem[]{}), wfmStrings.options());
                GWT.log(String.valueOf(result));
                if (result > 4 && isFirstInit) {
                    levelOptionList.setSelected(2);
                    levelActive = true;
                    isFirstInit = false;
                }
                getData(showKanbanView, levelOptionList, levelActive);
            }
        });
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
        }
        return landscape;
    }

    private void sendPdfRequest() {
        String URL = (CommandConstants.PDF_URL + "/SupervisorStructure");
        final ListingFilterParameter filterParameter = new ListingFilterParameter();
        Integer selectedLevel = levelOptionList.getSelectedId();
        filterParameter.setLandscape(true);
        filterParameter.setLevelActive(levelActive);
        filterParameter.setShowView(showKanbanView);
        filterParameter.setLevelOptionListForSprvs(selectedLevel);
        Utils.sendPDFOrExcelRequest(exportBtn, URL, filterParameter.getRequestParams(), "_blank");
    }

    public void refreshChart() {
        if (levelOptionList.getSelectedId() != null)
            levelOptionList.setSelected(2);
        getData(showKanbanView, levelOptionList, true);
    }
}
