package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.profile.ActionTimesTO;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Djuraev on 9/23/15.
 */
public class CompanyDocumentsListView extends BaseListView implements Constants {
    private static final DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat()); //"MM/dd/yyyy"
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<FileResource> list;


    public CompanyDocumentsListView() {
        super(COMPANY_DOCUMENTS);
        setDescription(property.getPlural(hrmsStrings.companyDocuments()));
        if (Utils.hasPermission(PermissionConstants.UPLOAD_COMPANY_DOCUMENTS)) {
            setAddNew("companyDocument|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "hrms employee-profile-document";
    }

    @Override
    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.HRMS_CONTEXT, PermissionConstants.HRMS_PROFILE_DOCUMENT_LIST);
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.CompanyDocumentListPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILE_DELETE, CompanyDocumentsListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_FILES_UPLOADED, CompanyDocumentsListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[11];
        int i = 0;

        columnConfig[i] = new ColumnDefinitionConfig<FileResource, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final FileResource rowValue) {
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                int menuItemCount = 0;

                final MenuPopItem download = new MenuPopItem(wfmStrings.download(), "icon-issue-edit-small");
                download.setTitle(wfmStrings.download());
                download.setCommand(() -> Utils.redirect(rowValue.getDownloadUrl()));
                menuItemCount++;
                menuBar.addItem(download);

                //Delete file
                if (Utils.hasPermission(PermissionConstants.REMOVE_COMPANY_DOCUMENTS) || Utils.getUserID().equals(rowValue.getCreatedByID())) {
                    final MenuPopItem deleteDocument = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteDocument.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                DocumentsService.App.get().moveFileToTrash(rowValue.getObjectId(), new AsyncCallback<Void>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void onSuccess(Void result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.attachment()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FILE_DELETE, result, CompanyDocumentsListView.this);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuItemCount++;
                    menuBar.addItem(deleteDocument);
                }

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig[i].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[i].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig[i].setColumnSortable(false);

        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, SimpleLink>(wfmStrings.fileName(), FileResource.NAME, 100) {
            @Override
            public SimpleLink getCellValue(final FileResource item) {
//                Image img = getFileIcon(item).createImage();
                SimpleLink nameLink = new SimpleLink(item.getFileName() != null ? item.getFileName() : "");
                nameLink.setTitle(item.getFileName() != null ? item.getFileName() : "");
                nameLink.addClickHandler(event -> Utils.showImageOrDownloadFile(item, false));
                return nameLink;
            }
        };
        columnConfig[i].setColumnSortable(true);

        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.documentName(), FileResource.DOCUMENT_NAME, 100) {
            @Override
            public String getCellValue(final FileResource item) {
                return item.getDocumentName();
            }

            @Override
            public void setCellValue(FileResource item, String cellValue) {
                item.setDocumentName(cellValue);
                saveCellValue(item);
            }
        };
        columnConfig[i].setColumnSortable(false);

        //createdBy
        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.createdBy(), FileResource.OWNER, 120) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getCreatedBy() != null ? rowValue.getCreatedBy() : "";
            }
        };
        columnConfig[i].setColumnSortable(true);
        columnConfig[i].setShow(false);


        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.documentID(), FileResource.DOCUMENT_ID, 100) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getDocID() != null ? rowValue.getDocID() : "";
            }

            @Override
            public void setCellValue(FileResource item, String cellValue) {
                item.setDocID(cellValue);
                saveCellValue(item);
            }
        };
        columnConfig[i].setColumnSortable(true);


        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.description(), FileResource.DOCUMENT_DESCRIPTION, 100) {
            @Override
            public String getCellValue(final FileResource item) {
                return item.getDescription();
            }

            @Override
            public void setCellValue(FileResource item, String cellValue) {
                item.setDescription(cellValue);
                saveCellValue(item);
            }
        };
        columnConfig[i].setColumnSortable(false);
        columnConfig[i].setShow(false);


        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.createdDate(), FileResource.DATE, 80) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getCreationDate() != null ? format.format(rowValue.getCreationDate()) : wfmStrings.notAvailable();
            }
        };
        columnConfig[i].setColumnSortable(true);
        columnConfig[i].setShow(false);


        //issuedDate
        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.issuedDate(), FileResource.ISSUED_DATE, 80) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getIssuedDate() != null && rowValue.getIssuedDate().getDate() != null ? format.format(rowValue.getIssuedDate().getNonConvertedDate()) : "";
            }

            @Override
            public void setCellValue(FileResource rowValue, String cellValue) {
                try {
                    rowValue.setIssuedDate(new DateNonConvertable(DateUtils.parse(cellValue)));
                    saveCellValue(rowValue);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        columnConfig[i].setColumnSortable(false);
        columnConfig[i].setShow(false);

        //expirationDate
        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.expiryDate(), FileResource.EXPIRE_DATE, 80) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getExpireDate() != null && rowValue.getExpireDate().getDate() != null ? format.format(rowValue.getExpireDate().getNonConvertedDate()) : "";
            }

            @Override
            public void setCellValue(FileResource rowValue, String cellValue) {
                try {
                    rowValue.setExpireDate(new DateNonConvertable(DateUtils.parse(cellValue)));
                    saveCellValue(rowValue);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        columnConfig[i].setColumnSortable(false);


        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.type(), FileResource.DOCUMENT_TYPE, 100) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getType() != null ? rowValue.getType() : "";
            }

            @Override
            public void setCellValue(FileResource item, String cellValue) {
                item.setType(cellValue);
                saveCellValue(item);
            }
        };
        columnConfig[i].setColumnSortable(true);
        columnConfig[i].setShow(false);

        //Reminder
        columnConfig[++i] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.reminder(), FileResource.REMINDER_TYPE, 100) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getReminderName() != null ? rowValue.getReminderName() : "N/A";
            }

            @Override
            public void setCellValue(FileResource item, String cellValue) {
                item.setReminderName(cellValue);
                saveCellValue(item);
            }
        };
        columnConfig[i].setColumnSortable(false);

        initCellEdit(columnConfig);
        return columnConfig;
    }

    private void initCellEdit(ColumnDefinitionConfig[] columnConfigs) {

        final TextBoxCellEditor<String> nameCellEditor = new TextBoxCellEditor<String>(180) {
            @Override
            protected String getValue() {
                return getText();
            }

            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        columnConfigs[2].setCellEditor(nameCellEditor);
        columnConfigs[2].setCellChangesSave(new CellChange<FileResource>() {
            @Override
            public void saveCell(FileResource item, String columnCodeName) {
                saveDocumentEditCellValue(item);
            }
        });


        final TextBoxCellEditor<String> descriptionCellEditor = new TextBoxCellEditor<String>(180) {
            @Override
            protected String getValue() {
                return getText();
            }

            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        columnConfigs[5].setCellEditor(descriptionCellEditor);
        columnConfigs[5].setCellChangesSave((item, columnCodeName) -> saveDocumentEditCellValue((FileResource) item));


        final TextBoxCellEditor<String> docIDCellEditor = new TextBoxCellEditor<String>() {
            @Override
            protected String getValue() {
                return getText();
            }

            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        columnConfigs[4].setCellEditor(docIDCellEditor);
        columnConfigs[4].setCellChangesSave((item, columnCodeName) -> saveDocumentEditCellValue((FileResource) item));


        final DropDownCellEditor<String> typeCellEditor = new DropDownCellEditor<String>() {
            @Override
            protected String getValue() {
                if (getListBox().getSelectedItem() != null) {
                    return getListBox().getSelectedItem().getName();
                }
                return null;
            }

            @Override
            protected void setValue(String cellValue) {
                if ("".equals(cellValue)) {
                    getListBox().setSelectedNullLabel();
                } else {
                    getListBox().setSelectedByValue(cellValue);
                }
            }
        };
        typeCellEditor.getListBox().setWithoutNullLabel(false);
        columnConfigs[9].setCellEditor(typeCellEditor);
        columnConfigs[9].setCellChangesSave(new CellChange<FileResource>() {
            @Override
            public void saveCell(FileResource item, String columnCodeName) {
                item.setTypeId(typeCellEditor.getSelectItem() != null ? typeCellEditor.getSelectItem().getId() : null);
                saveDocumentEditCellValue(item);
            }
        });

        //reminderCellEditor
        final DropDownCellEditor<String> reminderCellEditor = new DropDownCellEditor<String>() {
            @Override
            protected String getValue() {
                if (getListBox().getSelectedItem() != null) {
                    return getListBox().getSelectedItem().getName();
                }
                return null;
            }

            @Override
            protected void setValue(String cellValue) {
                getListBox().setSelectedByValue(cellValue);
            }
        };
        reminderCellEditor.getListBox().setWithoutNullLabel(true);
        columnConfigs[10].setCellEditor(reminderCellEditor);
        columnConfigs[10].setCellChangesSave(new CellChange<FileResource>() {
            @Override
            public void saveCell(FileResource item, String columnCodeName) {
                if (reminderCellEditor.getSelectItem() != null) {
                    item.setReminderId(reminderCellEditor.getSelectItem().getId());
                    item.setReminderName(reminderCellEditor.getSelectItem().getName());
                    item.setActionTimes(getActionTimeItems(reminderCellEditor));
                }
                saveDocumentEditCellValue(item);
            }
        });

        //issuedDateCellEditor
        DateTimePickerCellEditor<String> issuedDateCellEditor = new DateTimePickerCellEditor<String>() {
            @Override
            protected String getValue() {
                return DateUtils.format1(getDate());
            }

            @Override
            protected void setValue(String cellValue) {
                try {
                    Date date;
                    if (cellValue == null || "".equals(cellValue)) {
                        date = new Date();
                        setDefaultValue(true);
                    } else {
                        date = DateUtils.parse(cellValue);
                        setDefaultValue(false);
                    }
                    setDate(date, false);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        issuedDateCellEditor.getDateTimePicker().getAllDayCheckBox().setVisible(false);
        columnConfigs[7].setCellEditor(issuedDateCellEditor);
        columnConfigs[7].setCellChangesSave(new CellChange<FileResource>() {
            @Override
            public void saveCell(FileResource item, String columnCodeName) {
                saveDocumentEditCellValue(item);
            }
        });

        //expireDateCellEditor
        DateTimePickerCellEditor<String> expireDateCellEditor = new DateTimePickerCellEditor<String>() {
            @Override
            protected String getValue() {
                return DateUtils.format1(getDate());
            }

            @Override
            protected void setValue(String cellValue) {
                try {
                    Date date;
                    if (cellValue == null || "".equals(cellValue)) {
                        date = new Date();
                        setDefaultValue(true);
                    } else {
                        date = DateUtils.parse(cellValue);
                        setDefaultValue(false);
                    }
                    setDate(date, false);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        expireDateCellEditor.getDateTimePicker().getAllDayCheckBox().setVisible(false);
        columnConfigs[8].setCellEditor(expireDateCellEditor);
        columnConfigs[8].setCellChangesSave(new CellChange<FileResource>() {
            @Override
            public void saveCell(FileResource item, String columnCodeName) {
                saveDocumentEditCellValue(item);
            }
        });

        DocumentsService.App.get().getDocumentTypes(null, new AbstractAsyncCallback<HashMap<Integer, ArrayList<SelectItem>>>() {
            @Override
            public void success(HashMap<Integer, ArrayList<SelectItem>> documenTypes) {
                if (documenTypes != null && documenTypes.size() > 0) {
                    typeCellEditor.getListBox().setItems(documenTypes.get(F_COMPANY_DOCUMENTS).toArray(new SelectItem[]{}));
                }
            }
        });

        getDefaultReminderItems(reminderCellEditor.getListBox());

    }

    private ArrayList<ActionTimesTO> getActionTimeItems(DropDownCellEditor<String> reminderCellEditor) {
        ArrayList<ActionTimesTO> actionTimes = new ArrayList<>();
        ActionTimesTO actionTime = new ActionTimesTO();
        actionTime.setActionPeriod(reminderCellEditor.getSelectItem().getDescription());
        actionTime.setActionNumber(reminderCellEditor.getSelectItem().getCategory());
        actionTime.setActiontype("BEFORE");
        actionTimes.add(actionTime);
        return actionTimes;
    }

    private void getDefaultReminderItems(DataListBox listBox) {
        listBox.addListItem(new SelectItem(1, "1 " + wfmStrings.day(), 60 * 24 + "", "1"));
        listBox.addListItem(new SelectItem(2, "2 " + wfmStrings.days(), 60 * 24 + "", "2"));
        listBox.addListItem(new SelectItem(3, "3 " + wfmStrings.days(), 60 * 24 + "", "3"));
        listBox.addListItem(new SelectItem(7, "7 " + wfmStrings.days(), 60 * 24 + "", "7"));
        listBox.addListItem(new SelectItem(7 * 2, "2 " + wfmStrings.weeks(), 60 * 24 * 7 + "", "2"));
        listBox.addListItem(new SelectItem(30, "1 " + wfmStrings.months(), 60 * 24 * 30 + "", "1"));
        listBox.addListItem(new SelectItem(45, "45 " + wfmStrings.days(), 60 * 24 + "", "45"));
        listBox.addListItem(new SelectItem(30 * 2, "2 " + wfmStrings.months(), 60 * 24 * 30 + "", "2"));
        listBox.addListItem(new SelectItem(30 * 3, "3 " + wfmStrings.months(), 60 * 24 * 30 + "", "3"));
    }

    private void saveDocumentEditCellValue(FileResource item) {
        ArrayList<FileResource> items = new ArrayList<>();
        items.add(item);
        DocumentsService.App.get().updateFiles(items, item.getEntityID(), null, new AbstractAsyncCallback<Void>() {
            @Override
            public void success(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_COMPANY_DOC_LISTING_EDIT, result, CompanyDocumentsListView.this);
            }
        });
    }

    private ListingRequestProvider<FileResource> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs.setFolderType(Constants.F_COMPANY_DOCUMENTS);
            filterParametrs.setModule(LayoutRPC.HRMS_SECTION);
            filterParametrs.setEntityID(null);
            filterParametrs.setCrmEntityId(null);
            DocumentsService.App.get().listFile(filterParametrs, new AbstractAsyncCallback<ListResult<FileResource>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<FileResource> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.UPLOAD_COMPANY_DOCUMENTS) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("companyDocument|add/add") : null;
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
                        return (data, callback) -> {
                            data.setObjectID(null);
                            data.setTypeId(Constants.F_COMPANY_DOCUMENTS);
                            RbacService.App.get().getDocumentFacetFilterData(data, null, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callback.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc facetFilterRpc) {
                                    callback.onSuccess(facetFilterRpc);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }

                };
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.UPLOAD_COMPANY_DOCUMENTS)) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("companyDocument|add/add"));
                    return addnew;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message;
                message = new DefaultNoItemsMessage(wfmStrings.thereAreNoItemsToShow());
                if (Utils.hasPermission(PermissionConstants.UPLOAD_COMPANY_DOCUMENTS)) {
                    message.setHref("companyDocument|add/add");
                    message.setTextBeforeLink(hrmsStrings.noDocumentLink());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.CompanyDocumentFacetFilter.getContentCode()[0], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrFolderRepresenter.FIELD_DOCUMENT_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrFolderRepresenter.FIELD_DOCUMENT_TYPE_ID_NAME;
            }
        });
        return contentConfigure;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public String getPropertyCode() {
        return COMPANY_DOCUMENTS;
    }
}
