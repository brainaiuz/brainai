package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_CUSTOM;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_DELETE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_EDIT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_MONTH;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_WEEK;

public class ShiftListView extends BaseListView implements Constants {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingPanel<ShiftItem> listingPanel;

    public ShiftListView() {
        super(SHIFT_LIST);
        setDescription(wfmStrings.shifts());

        if (hasPermissionToAdd()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/add"));
        }
    }

    private CustomColumnDefinitionConfig[] getColumn() {
        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[3];
        columnConfig[index] = new ColumnDefinitionConfig<ShiftItem, Anchor>(wfmStrings.action(), "SHIFT", LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(ShiftItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                //Summary View
                if (hasPermissionToSummary()) {
                    MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    if (item.getOverallStatus() != null && item.getOverallStatus().getCode().equals(SHIFT_DRAFT)) {
                        summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/edit/" + item.getId() + (item.getEndDate() != null ? "/custom/true" : "week".equals(item.getPeriodType()) ? "/week/true" : ""), item.getCode()));
                    } else {
                        summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/summary/" + item.getId(), item.getCode()));
                    }
                    actionItemCount++;
                    menuBar.addItem(summary);
                }

                //Edit Action View
                if (hasPermissionToEdit()) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-task-small");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/edit/" + item.getId() + (item.getEndDate() != null ? "/custom/true" : "week".equals(item.getPeriodType()) ? "/week/true" : ""), item.getCode()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                //Delete Action View
                if (hasPermissionToDelete()) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                    delete.setCommand(() -> deleteShiftItem(item.getId()));
                    actionItemCount++;
                    menuBar.addItem(delete);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return hasPermissionToEdit() || hasPermissionToDelete() || hasPermissionToSummary() ? toolItem.getAction() : null;
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, String>(wfmStrings.type(), ShiftItem.
                TYPE, 110) {
            @Override
            public String getCellValue(ShiftItem item) {
                String name = null;
                if (item.getLookUpType() == null || item.getLookUpType().equals(LookUpConstants.BRIGADA_ID)) {
                    name = wfmStrings.team();
                } else if (item.getLookUpType().equals(LookUpConstants.EMPLOYEE_ID)) {
                    name = wfmStrings.duty();
                } else if (item.getLookUpType().equals(LookUpConstants.OVERTIME)) {
                    name = wfmStrings.overtime();
                }
                return name;
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);
        columnConfig[index].setColumnSortable(true);

        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, Widget>(wfmStrings.month(), ShiftItem.MONTH, 110) {
            @Override
            public Widget getCellValue(ShiftItem item) {
                DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MMM yyyy");
                Label label = new Label();
                String type = item.getLookUpType() == null || item.getLookUpType().equals(LookUpConstants.BRIGADA_ID) ? "B" : "E";
                String date = item.getPeriod() != null ? dateTimeFormat.format(item.getPeriod()) : null;
                if (item.getOverallStatus() != null && item.getOverallStatus().getCode() != null && item.getOverallStatus().getCode().equals(SHIFT_APPROVED)) {
                    label.setStyleName("uploadLinkStyle2");
                    label.addClickHandler(e -> {
                        goTo("attendanceReport/shift/" + item.getId() + type + "/" + date);
                    });
                }
                label.setText(date);
                return label;
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(true);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);

        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, Widget>(wfmStrings.number(), ShiftItem.NUMBER, 110) {
            @Override
            public Widget getCellValue(ShiftItem item) {
                Label label = new Label(item.getCode());
                label.setStyleName("uploadLinkStyle2");
                if (Utils.hasPermission(HRMS_SHIFT_SUMMARY)) {
                    if (item.getOverallStatus() != null && item.getOverallStatus().getCode().equals(SHIFT_DRAFT)) {
                        label.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/edit/" + item.getId() + (item.getEndDate() != null ? "/custom/true" : "week".equals(item.getPeriodType()) ? "/week/true" : ""), item.getCode()));
                    } else {
                        label.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/summary/" + item.getId(), item.getCode()));
                    }
                }
                return label;
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(true);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);


        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, String>(wfmStrings.createdBy(), ShiftItem.
                CREATOR, 110) {
            @Override
            public String getCellValue(ShiftItem item) {
                return item.getCreator() != null ? item.getCreator().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(true);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);


        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, String>(wfmStrings.modifiedBy(), ShiftItem.
                UPDATOR, 110) {
            @Override
            public String getCellValue(ShiftItem item) {
                return item.getUpdater() != null ? item.getUpdater().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(true);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);


        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, String>(wfmStrings.createdDate(), ShiftItem.CREATED_DATE, 110) {
            @Override
            public String getCellValue(ShiftItem item) {
                //  return DateUtils.formatInternal(item.getCreatedDate().getNonConvertedDate());

                return DateUtils.formatInternal(item.getCreatedDate().getDate());
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(true);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);


        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, String>(wfmStrings.modifiedDate(), ShiftItem.UPDATED_DATE, 110) {
            @Override
            public String getCellValue(ShiftItem item) {
                //  return DateUtils.formatInternal(item.getCreatedDate().getNonConvertedDate());
                return DateUtils.formatInternal(item.getUpdatedDate().getDate());
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(true);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);

        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, String>(wfmStrings.status(), ShiftItem.STATUS, 80) {
            @Override
            public String getCellValue(ShiftItem rowValue) {
                String status = " ";
                if (rowValue.getOverallStatus() != null && rowValue.getOverallStatus().getCode() != null) {
                    switch (rowValue.getOverallStatus().getCode()) {
                        case SHIFT_APPROVED:
                            status = wfmStrings.approved();
                            break;
                        case SHIFT_REJECTED:
                            status = wfmStrings.rejected();
                            break;
                        case SHIFT_SUBMITTED:
                            status = wfmStrings.waitingForApproval();
                            break;
                        case SHIFT_DRAFT:
                            status = wfmStrings.draft();
                            break;
                    }
                }
                return status;
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(true);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);


        columnConfig[++index] = new ColumnDefinitionConfig<ShiftItem, Widget>(wfmStrings.department(), ShiftItem.DEPARTMENT, 110) {
            @Override
            public Widget getCellValue(ShiftItem item) {
                Label label = new Label(item.getDepartment() != null ? item.getDepartment().getName() : wfmStrings.na());
                return label;
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(true);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig[index].setShow(true);


        return columnConfig;

    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (hasPermissionToAdd()) {
                    return () -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/add");
                }
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

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
                        fields.add(ListingChooseFilter.SHIFT_STATUS);
                        fields.add(ListingChooseFilter.SHIFT_CREATOR);
                        fields.add(ListingChooseFilter.APPROVER);
                        fields.add(ListingChooseFilter.SHIFT_TYPE);
                        fields.add(ListingChooseFilter.SHIFT_PERIOD);
                        fields.add(ListingChooseFilter.SHIFT_DEPARTMENT);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.ShiftList;
                    }
                };
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }


            @Override
            public ActionButton initTopToolBarNew() {
                if (!hasPermissionToAdd()) {
                    return null;
                }

                boolean hasMonth = Utils.hasPermission(HRMS_SHIFT_MONTH);
                boolean hasWeek = Utils.hasPermission(HRMS_SHIFT_WEEK);
                boolean hasCustom = Utils.hasPermission(HRMS_SHIFT_CUSTOM);
                int count = (hasMonth ? 1 : 0) + (hasWeek ? 1 : 0) + (hasCustom ? 1 : 0);

                if (count == 0) {
                    return null;
                }

                ActionButton more = getAddNewButton(ActionButton.Type.TOOLMENU);

                if (count == 1) {
                    if (hasMonth) {
                        more.addClickHandler(e -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/add"));
                    } else if (hasWeek) {
                        more.addClickHandler(e -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/add/week/true"));
                    } else {
                        more.addClickHandler(e -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/add/custom/true"));
                    }
                    return more;
                }

                final MenuBar contextMenu = new MenuBar(true);
                if (hasMonth) {
                    contextMenu.addItem("<span>" + wfmStrings.month() + "</span>", true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/add"));
                }
                if (hasWeek) {
                    contextMenu.addItem("<span>" + wfmStrings.week() + "</span>", true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/add/week/true"));
                }
                if (hasCustom) {
                    contextMenu.addItem("<span>" + wfmStrings.custom() + "</span>", true, (Command) () -> SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/add/custom/true"));
                }
                more.addClickHandler(clickEvent -> more.setMenu(contextMenu));
                return more;
            }
        };


    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(HRMS_SHIFT_ADD);
    }

    private boolean hasPermissionToEdit() {
        return Utils.hasPermission(HRMS_SHIFT_EDIT);
    }

    private boolean hasPermissionToDelete() {
        return Utils.hasPermission(HRMS_SHIFT_DELETE);
    }

    private boolean hasPermissionToSummary() {
        return Utils.hasPermission(HRMS_SHIFT_SUMMARY);
    }


    private ListingRequestProvider<ShiftItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> HrmsService.App.get().getShiftList(filterParametrs, new AbstractAsyncCallback<ListResult<ShiftItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<ShiftItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new GuideListingPanel(ListPanelType.ShiftList, getColumn(), getListingRequestProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SHIFT_ADD, ShiftListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SHIFT_DELETE, ShiftListView.this, (sender, args) -> listingPanel.reloadPage());

        listingPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/additionalPaymentListExcelHandler";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListExcel(excelURL, filterParameter);
        });
        listingPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/additionalPaymentItemPdfHandl";
            ListingFilterParameter filterParameter = listingPanel.getFilterParametrs();
            filterParameter.setPropertyCode(getPropertyCode());
            listingPanel.callListPDF(pdfURL, filterParameter);

        });


        add(listingPanel);
        return null;
    }

    private void deleteShiftItem(Integer id) {
        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        message.setTitle(wfmStrings.warning());
        message.setMessage(wfmStrings.areSureYouWontToDelete());
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                HrmsService.App.get().deleteShiftItem(id, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.shift()));
                        listingPanel.reloadPage();
                    }
                });
            }
        });
        message.open();
    }

    @Override
    public String getPropertyCode() {
        return SHIFT_LIST;
    }
}
