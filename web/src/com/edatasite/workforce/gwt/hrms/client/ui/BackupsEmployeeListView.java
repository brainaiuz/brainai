package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.BackupsEmployeeObject;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;


public class BackupsEmployeeListView extends BaseListView implements Constants {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingPanel<BackupsEmployeeObject> listingPanel;

    public BackupsEmployeeListView() {
        super(BACKUPS_EMPLOYEE);
        setDescription(property.getPlural(wfmStrings.backupEmployee()));
        if (Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_ADD)) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("backupsEmployee|add/add"));
        }
    }

    private CustomColumnDefinitionConfig[] getColumn() {

        int index = 0;
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[9];
        columnConfig[index] = new ColumnDefinitionConfig<BackupsEmployeeObject, Anchor>(wfmStrings.action(), "BACKUPS_EMPLOYEE", LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(BackupsEmployeeObject item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem view = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                view.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("backupsEmployee|add/summary/" + item.getId()));
                actionItemCount++;
                if (Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_SUMMARY)) {
                    menuBar.addItem(view);
                }

                view = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                view.setCommand(() -> deleteBackupEmployee(item.getId()));
                actionItemCount++;
                if (Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_DELETE)) {
                    menuBar.addItem(view);
                }

//                if (Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_SUMMARY)) {
//                    MenuPopItem pdfVersion = new MenuPopItem(wfmStrings.pdf(), "icon-pdf-profile");
//                    pdfVersion.ensureDebugId("exportToPDF");
//                    pdfVersion.setCommand(() -> new PDFTemplateSelector(BACKUPS_EMPLOYEE, new ExtendedCommand() {
//                        @Override
//                        public void execute(Integer id) {
//                            LeaveRequestObject requestObject = new LeaveRequestObject(item.getId(), item.getId(), id);
//                            String pdfUrl = CommandConstants.PDF_URL + "/backupEmployeeViewPDFHandler";
//                            HashMap<String, String> requestParams = requestObject.getRequestParams();
//                            listingPanel.callListPDF(pdfUrl, requestParams);
//                        }
//                    }));
//                    actionItemCount++;
//                    menuBar.addItem(pdfVersion);
//                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, SimpleLink>(wfmStrings.backupEmployee(), "backupEmployee", 110) {
            @Override
            public SimpleLink getCellValue(BackupsEmployeeObject item) {

                if (Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_SUMMARY)) {
                    return getLink(item.getSelectedEmployee() != null ? item.getSelectedEmployee().getName() : "N/A", "backupsEmployee|add/summary/" + item.getId());
                } else {
                    return getLink(item.getSelectedEmployee() != null ? item.getSelectedEmployee().getName() : "N/A", null);
                }
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.createdBy(), "creator", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getCreator() != null ? item.getCreator().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, SimpleLink>(wfmStrings.code(), "code", 110) {
            @Override
            public SimpleLink getCellValue(BackupsEmployeeObject item) {
                if (Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_SUMMARY)) {
                    return getLink(item.getCode() != null ? item.getCode() : "N/A", "backupsEmployee|add/summary/" + item.getId());
                } else {
                    return getLink(item.getCode() != null ? item.getCode() : "N/A", null);
                }
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.status(), "status", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getStatus() != null ? item.getStatus() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.department(), "department", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getDepartment() != null ? item.getDepartment().getName() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.position(), "position", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getPosition() != null ? item.getPosition().getName() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.description(), "description", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getDescription() != null ? item.getDescription() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.backupEmployee(), "backupEmployee", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getBackups() != null ? item.getBackups() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);


        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.reason(), "reason", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getReasons() != null ? item.getReasons().toString() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.grantSigningAuthority(), "grantSigningAuthority", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getIsNeedSignature() != null ? item.getIsNeedSignature() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.percentage(), "persentage", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getPercentage() != null ? item.getPercentage() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.approver(), "approver", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getApproverEmployee() != null ? item.getApproverEmployee().getName() : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.createdDate(), "createdDate", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getCreatedDate() != null ? DateUtils.formatInternal(item.getCreatedDate().getDate()) : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.modifiedBy(), "updatedBy", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getUpdater() != null ? item.getUpdater().getName() : "N/A";
            }
        };

        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfig[++index] = new ColumnDefinitionConfig<BackupsEmployeeObject, String>(wfmStrings.modifiedDate(), "updatedDate", 110) {
            @Override
            public String getCellValue(BackupsEmployeeObject item) {
                return item.getUpdatedDate() != null ? DateUtils.formatInternal(item.getUpdatedDate().getDate()) : "N/A";
            }
        };
        columnConfig[index].setMinimumColumnWidth(100);
        columnConfig[index].setColumnSortable(false);
        columnConfig[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        return columnConfig;
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return () -> SinksContainerFactory.entryPoint.onHistoryChanged("backupsEmployee|add/add");
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
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
                ActionButton addnew = getAddNewButton();
                if (Utils.hasPermission(PermissionConstants.HRMS_BACKUPS_EMPLOYEE_ADD)) {
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("backupsEmployee|add/add"));
                }
                return addnew;
            }
        };


    }

    private ListingRequestProvider<BackupsEmployeeObject> getListingRequestProvider() {
        return (filterParametrs, callback) -> HrmsService.App.get().getBackupsEmployeeList(filterParametrs, new AbstractAsyncCallback<ListResult<BackupsEmployeeObject>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<BackupsEmployeeObject> result) {
                callback.onSuccess(result);
            }
        });
    }

    private void deleteBackupEmployee(Integer id) {
        LoadingPanel.loading(true);
        HrmsService.App.get().deleteBackupEmployeeById(id, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }


            @Override
            public void onSuccess(Boolean deleted) {
                if (!deleted) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                    return;
                }
                LoadingPanel.loading(false);
                Info.show(wfmStrings.messSuccessfulyyDeleted());
                listingPanel.reloadPage();
            }
        });
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
        listingPanel = new GuideListingPanel(ListPanelType.BackupsEmployee, getColumn(), getListingRequestProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BACKUPS_EMPLOYEE_ADD, BackupsEmployeeListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BACKUPS_EMPLOYEE_DELETE, BackupsEmployeeListView.this, (sender, args) -> listingPanel.reloadPage());

        add(listingPanel);
        return null;
    }

    @Override
    public String getPropertyCode() {
        return BACKUPS_EMPLOYEE;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

}
