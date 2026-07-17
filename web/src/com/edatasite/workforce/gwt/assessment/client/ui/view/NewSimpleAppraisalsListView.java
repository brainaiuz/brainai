package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentsListElem;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

public class NewSimpleAppraisalsListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private ListingPanel<AssessmentsListElem> list;
    private Integer employeeId;

    public NewSimpleAppraisalsListView() {
        super(PA_ARCHIVE);
        setDescription(property.getPlural(hrmsStrings.simpleAppraisals()));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_APPRAISALS)) {
            setAddNew("initiate|add/");
        }
    }

    public NewSimpleAppraisalsListView(Integer employeeId) {
        super(PA_ARCHIVE);
        this.employeeId = employeeId;
        setDescription(property.getPlural(hrmsStrings.simpleAppraisals()));
    }

    @Override
    public String getIconStyle() {
        return "assessment assessment-archive";
    }

    @Override
    protected Widget onInitialize() {

        list = new ListingPanel<>(ListPanelType.AssessmentArchiveListPanel, getColumnsConfig(), getRequestProvider(), getDesign());

        list.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/assessmentListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, filterParametrs);
        });

        list.setExcelListener(clickEvent -> {
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/assessmentListExcelHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, filterParametrs);
        });

        add(list);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SIMPLE_APPRAISAL_INITIATED, NewSimpleAppraisalsListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_360_APPRAISAL_INITIATED, NewSimpleAppraisalsListView.this, (sender, args) -> list.reloadPage());

        return null;
    }

    private Anchor getActionMenuItems(final AssessmentsListElem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);

        if (ASSESSMENT_SIMPLE.equals(item.getAssessmentTypeCode()) && item.getEmployeeAssessmentId() != null) {

            MenuPopItem viewItem = new MenuPopItem(wfmStrings.summaryView(), "removeItemStyle-profile");
            viewItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(AssessmentHelper.getReviewLinkForUI(item)));
            actionItemCount++;
            menuBar.addItem(viewItem);

            MenuPopItem pdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf-profile");
            pdf.setCommand(() -> list.callItemPDF(CommandConstants.PDF_URL + "/assessmentViewPDFHandler", new RequestObject(item.getEmployeeAssessmentId())));
            menuBar.addItem(pdf);


            MenuPopItem deleteItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
            deleteItem.setCommand(() -> {
                final WfmMessageBox wfmMessageBox = new WfmMessageBox(
                        IconEnum.QUESTION, Action.YesNo,
                        wfmStrings.areYouSureWantToDeleteThe() + " " + item.getAssessmentName() + "?"
                );

                wfmMessageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        AssessmentService.App.get().deleteAssessment(item.getEmployeeAssessmentId(), new AbstractAsyncCallback<Void>() {
                            @Override
                            public void success(Void result) {
                                wfmMessageBox.close();
                                list.reloadPage();
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_APPRAISAL_DELETE, item, NewSimpleAppraisalsListView.this);
                            }
                        });
                    }
                });
                wfmMessageBox.setWidth("300px");
                wfmMessageBox.open();
            });
            actionItemCount++;
            menuBar.addItem(deleteItem);
        }


        final ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ChooseFilter.INVOICE_FILTER;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>();
                        fields.add(ListingChooseFilter.APPRASIAL_EMPLOYEE);
                        fields.add(ListingChooseFilter.ASSESSMENT_INITIATE_BY);
                        fields.add(ListingChooseFilter.ASSESSMENT_STATUS);
                        fields.add(ListingChooseFilter.VALIDITY_PERIOD);
                        return fields;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_APPRAISALS)) {
                    ActionButton newEmployeeAppraisalItem = getAddNewButton();
                    newEmployeeAppraisalItem.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("initiate|add/"));
                    return newEmployeeAppraisalItem;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                String labelText = hrmsStrings.noAppraisals() + hrmsStrings.youCanInitiateSimpleAppraisalByClicking();
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(labelText);
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_APPRAISALS)) {
                message.setHref("initiate|add/");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ColumnDefinitionConfig[] getColumnsConfig() {
        ArrayList<ColumnDefinitionConfig> columnDefinitionConfigs = new ArrayList<>();

        // Action
        ColumnDefinitionConfig actionCol = new ColumnDefinitionConfig<AssessmentsListElem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, 100) {
            @Override
            public Anchor getCellValue(final AssessmentsListElem item) {
                return getActionMenuItems(item);
            }
        };
        actionCol.setColumnSortable(false);
        actionCol.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        actionCol.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnDefinitionConfigs.add(actionCol);

        // Employee Name
        ColumnDefinitionConfig employeeNameCol = new ColumnDefinitionConfig<AssessmentsListElem, SimpleLink>(wfmStrings.employee(), AssessmentsListElem.ASSESSMENT_NAME, 130) {
            @Override
            public SimpleLink getCellValue(AssessmentsListElem item) {
                SimpleLink link = new SimpleLink(item.getEmployeeName());
                link.addClickHandler(e -> SinksContainerFactory.entryPoint.onHistoryChanged(AssessmentHelper.getReviewLinkForUI(item)));
                return link;
            }
        };
        employeeNameCol.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(employeeNameCol);

        // Initiated Date
        ColumnDefinitionConfig initiatedDateCol = new ColumnDefinitionConfig<AssessmentsListElem, String>(hrmsStrings.initiatedDate(), AssessmentsListElem.INITIATION_DATE, 70) {
            @Override
            public String getCellValue(AssessmentsListElem item) {
                return DateUtils.format(item.getInitiationDate()) + Utils.getHijriDate(item.getInitiationDate());
            }
        };
        initiatedDateCol.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        initiatedDateCol.setMinimumColumnWidth(50);
        columnDefinitionConfigs.add(initiatedDateCol);

        // Initiated By
        ColumnDefinitionConfig initiatedByCol = new ColumnDefinitionConfig<AssessmentsListElem, String>(wfmStrings.initiatedBy(), AssessmentsListElem.INITIATOR_NAME, 120) {
            @Override
            public String getCellValue(AssessmentsListElem item) {
                return item.getInitiatorName();
            }
        };
        initiatedByCol.setMinimumColumnWidth(90);
        columnDefinitionConfigs.add(initiatedByCol);

        // Reviewers
        ColumnDefinitionConfig reviewersCol = new ColumnDefinitionConfig<AssessmentsListElem, String>(wfmStrings.reviewers(), AssessmentsListElem.REVIEWER_NAME, 130) {
            @Override
            public String getCellValue(AssessmentsListElem item) {
                return item.getReviewerName();
            }
        };
        reviewersCol.setMinimumColumnWidth(90);
        columnDefinitionConfigs.add(reviewersCol);

        // Status
        ColumnDefinitionConfig statusCol = new ColumnDefinitionConfig<AssessmentsListElem, String>(wfmStrings.status(), AssessmentsListElem.ASSESSMENT_STATUS, 120) {
            @Override
            public String getCellValue(AssessmentsListElem rowValue) {
                return rowValue.getStatus();
            }
        };
        statusCol.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        statusCol.setMinimumColumnWidth(80);
        columnDefinitionConfigs.add(statusCol);

        // Overall Score
        ColumnDefinitionConfig overallScoreCol = new ColumnDefinitionConfig<AssessmentsListElem, String>(hrmsStrings.overallScore(), AssessmentsListElem.OVERALL_SCORE, 70) {
            @Override
            public String getCellValue(AssessmentsListElem rowValue) {
                return rowValue.getOverallScore() != null ? Utils.formatDouble(rowValue.getOverallScore()) : "";
            }
        };
        overallScoreCol.setMinimumColumnWidth(80);
        overallScoreCol.setColumnSortable(false);
        columnDefinitionConfigs.add(overallScoreCol);

        // Validity Period
        ColumnDefinitionConfig validityPeriodCol = new ColumnDefinitionConfig<AssessmentsListElem, String>(wfmStrings.validityPeriod(), AssessmentsListElem.VALIDITY_PERIOD, 130) {
            @Override
            public String getCellValue(AssessmentsListElem rowValue) {
                return rowValue.getValidityPeriod() != null ? rowValue.getValidityPeriod() : "";
            }
        };
        validityPeriodCol.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        validityPeriodCol.setMinimumColumnWidth(100);
        columnDefinitionConfigs.add(validityPeriodCol);

        // Template Name
        ColumnDefinitionConfig templateNameCol = new ColumnDefinitionConfig<AssessmentsListElem, String>(wfmStrings.template(), AssessmentsListElem.TEMPLATE_NAME, 100) {
            @Override
            public String getCellValue(AssessmentsListElem item) {
                return item.getTemplateName() != null ? item.getTemplateName() : "";
            }
        };
        templateNameCol.setMinimumColumnWidth(80);
        templateNameCol.setColumnSortable(false);
        columnDefinitionConfigs.add(templateNameCol);


        ColumnDefinitionConfig departmentName = new ColumnDefinitionConfig<AssessmentsListElem, String>(wfmStrings.department(), AssessmentsListElem.DEPARTMENT_NAME, 100) {
            @Override
            public String getCellValue(AssessmentsListElem item) {
                return item.getDepartmentName() != null ? item.getDepartmentName() : "";
            }
        };
        templateNameCol.setMinimumColumnWidth(80);
        templateNameCol.setColumnSortable(false);
        columnDefinitionConfigs.add(departmentName);

        return columnDefinitionConfigs.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<AssessmentsListElem> getRequestProvider() {
        return (filterParametrs, callback) -> AssessmentService.App.get().getAssessmentsList(filterParametrs,employeeId, new AsyncCallback<ListResult<AssessmentsListElem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<AssessmentsListElem> list) {
                callback.onSuccess(list);
            }
        });
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

    public String getPropertyCode() {
        return "appraisalsArchive";
    }
}