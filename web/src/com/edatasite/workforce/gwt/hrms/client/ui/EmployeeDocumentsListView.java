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
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.profile.ActionTimesTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
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
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Djuraev on 9/23/15.
 */
public class EmployeeDocumentsListView extends BaseListView implements Constants {
    private static final DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat()); //"MM/dd/yyyy"
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private ListingPanel<FileResource> list;
    private Integer userID = null;
    private Integer entityId = null;
    private String typeCode = null;
    private boolean fromEmployeeProfile;


    public EmployeeDocumentsListView(Integer id) {
        super(EMPLOYEE_DOCUMENTS);
        setDescription(property.getPlural(id != null ? wfmStrings.documents() : hrmsStrings.employeeDocuments()));
        this.entityId = id;
        this.userID = Utils.getUserID();
        this.fromEmployeeProfile = false;
        if (Utils.hasPermission(PermissionConstants.UPLOAD_EMPLOYEE_DOCUMENTS)) {
            setAddNew("employeeDocument|add/add/" + this.userID);
        }
    }

    public EmployeeDocumentsListView(Integer id, boolean fromEmployeeProfile) {
        super(EMPLOYEE_DOCUMENTS);
        setDescription(property.getPlural(id != null ? wfmStrings.documents() : hrmsStrings.employeeDocuments()));
        this.userID = id;
        this.fromEmployeeProfile = fromEmployeeProfile;
        if (Utils.hasPermission(PermissionConstants.UPLOAD_EMPLOYEE_DOCUMENTS)) {
            setAddNew("employeeDocument|add/add/" + this.userID);
        }
    }

    public EmployeeDocumentsListView(Integer id, String type, String listName) {
        super(type, listName);
        this.typeCode = type;
        if (Utils.hasPermission(PermissionConstants.UPLOAD_EMPLOYEE_DOCUMENTS)) {
            String url = "employeeDocument|add/add" + "/" + userID;
            if (typeCode != null && !"".equals(typeCode)) {
                url = url + "/" + typeCode;
            }
            setAddNew(url);
        }
    }

    @Override
    public String getIconStyle() {
        return "hrms employee-profile-document";
    }

    public void reloadPage() {
        list.reloadPage();
    }

    @Override
    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.HRMS_CONTEXT, PermissionConstants.HRMS_PROFILE_DOCUMENT_LIST);
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.EmployeeDocumentListPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILE_DELETE, EmployeeDocumentsListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_FILES_UPLOADED, EmployeeDocumentsListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<FileResource, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final FileResource rowValue) {
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                int menuItemCount = 0;

                final MenuPopItem download = new MenuPopItem(wfmStrings.download(), "icon-issue-edit-small");
                download.setTitle(wfmStrings.download());
                download.setCommand(() -> {
                    LoadingPanel.loading(true);
                    DocumentsService.App.get().getFileLink(rowValue.getBodyId(), new AbstractAsyncCallback<String>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(String amazonLink) {
                            LoadingPanel.loading(false);
                            rowValue.setAmazonLink(amazonLink);
                            Utils.redirect(rowValue.getDownloadUrl());
                        }
                    });
//                        Utils.redirect(rowValue.getUrlFromSolr());
                });
                menuItemCount++;
                menuBar.addItem(download);

                final MenuPopItem fileProperties = new MenuPopItem(wfmStrings.properties());
                fileProperties.setCommand(() -> {
                    EmployeeDocFilePropertiesDialog dialog = new EmployeeDocFilePropertiesDialog(rowValue);
                    dialog.center();
                });
                menuItemCount++;
                menuBar.addItem(fileProperties);

                //Delete file
                if (Utils.hasPermission(PermissionConstants.REMOVE_EMPLOYEE_DOCUMENTS) || Utils.getUserID().equals(rowValue.getCreatedByID())) {
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
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FILE_DELETE, result, EmployeeDocumentsListView.this);
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
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        //fileName
        columnConfig = new ColumnDefinitionConfig<FileResource, SimpleLink>(wfmStrings.fileName(), FileResource.NAME, 100) {
            @Override
            public SimpleLink getCellValue(final FileResource item) {
                SimpleLink nameLink = new SimpleLink(item.getFileName() != null ? item.getFileName() : "");
                nameLink.setTitle(item.getFileName() != null ? item.getFileName() : "");
                nameLink.addClickHandler(event -> {
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
                });
                return nameLink;
            }
        };
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);

        //Document Name
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.documentName(), FileResource.DOCUMENT_NAME, 100) {
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
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);

        //created by
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.createdBy(), FileResource.CREATEBY, 120) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getCreatedBy();
            }
        };
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);
        columnConfig.setShow(false);

        //Document ID
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.documentID(), FileResource.DOCUMENT_ID, 100) {
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
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);

        //Document Description
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.description(), FileResource.DOCUMENT_DESCRIPTION, 100) {
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
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);
        columnConfig.setShow(false);

        //createdDate
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.createdDate(), FileResource.DATE, 80) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getCreationDate() != null ? format.format(rowValue.getCreationDate()) : "";
            }
        };
        columnConfig.setColumnSortable(true);
        columnConfig.setShow(false);
        columnConfigs.add(columnConfig);

        //issuedDate
        String issuedate = wfmStrings.issuedDate();
        if (INSURANCE.equals(typeCode)) {
            issuedate = wfmStrings.startDate();

        }
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(issuedate, FileResource.ISSUED_DATE, 80) {
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
        columnConfig.setShow(false);
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);

        //Document type
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.type(), FileResource.DOCUMENT_TYPE, 100) {
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
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);

        //expirationDate
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.expiryDate(), FileResource.EXPIRE_DATE, 80) {
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
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);

        //owner
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.owner(), FileResource.OWNER, 120) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getEntityName();
            }
        };
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);


        //Reminder
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.reminder(), FileResource.REMINDER_TYPE, 100) {
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
        columnConfig.setShow(false);
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);

        //employeeCode
        columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.employeeCode(), FileResource.EMPLOYEE_CODE, 80) {
            @Override
            public String getCellValue(FileResource rowValue) {
                return rowValue.getEmployeeCode() != null && !"null".equals(rowValue.getEmployeeCode()) ? rowValue.getEmployeeCode() : "";
            }

            @Override
            public void setCellValue(FileResource item, String cellValue) {
                item.setEmployeeCode(cellValue);
                saveCellValue(item);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setColumnSortable(true);
        columnConfigs.add(columnConfig);

        if (INSURANCE.equals(typeCode)) {
            //insureeName
            columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.firstName(), FileResource.INSURE_NAME, 120) {
                @Override
                public String getCellValue(FileResource rowValue) {
                    return rowValue.getInsureeName();
                }
            };
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columnConfigs.add(columnConfig);

            //insureeLastName
            columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.lastName(), FileResource.INSURE_LAST_NAME, 120) {
                @Override
                public String getCellValue(FileResource rowValue) {
                    return rowValue.getInsureeLastName();
                }
            };
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columnConfigs.add(columnConfig);

            //insuranceStatus
            columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.status(), FileResource.INSURANCE_STATUS, 120) {
                @Override
                public String getCellValue(FileResource rowValue) {
                    return rowValue.getStatusName();
                }
            };
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columnConfigs.add(columnConfig);

            //insuranceCost
            columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.insuranceCost(), FileResource.INSURANCE_COST, 120) {
                @Override
                public String getCellValue(FileResource rowValue) {
                    return rowValue.getInsuranceCost();
                }
            };
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columnConfigs.add(columnConfig);

            //insurancePlan
            columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.insurancePlan(), FileResource.INSURANCE_PLAN, 120) {
                @Override
                public String getCellValue(FileResource rowValue) {
                    return rowValue.getInsurancePlan();
                }
            };
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columnConfigs.add(columnConfig);

            //insuranceCoverage
            columnConfig = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.insuranceCoverage(), FileResource.INSURANCE_COVERAGE, 120) {
                @Override
                public String getCellValue(FileResource rowValue) {
                    return rowValue.getInsuranceCoverage();
                }
            };
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columnConfigs.add(columnConfig);
        }

        initCellEdit(columnConfigs.toArray(new ColumnDefinitionConfig[]{}));
        return columnConfigs.toArray(new ColumnDefinitionConfig[]{});
    }

    private void initCellEdit(ColumnDefinitionConfig[] columnConfigs) {
        //nameCellEditor
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
        columnConfigs[2].setCellChangesSave((item, columnCodeName) -> saveDocumentEditCellValue((FileResource) item));


        //descriptionCellEditor
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
        columnConfigs[4].setCellEditor(descriptionCellEditor);
        columnConfigs[4].setCellChangesSave((item, columnCodeName) -> saveDocumentEditCellValue((FileResource) item));

        //docIDCellEditor
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
        columnConfigs[5].setCellEditor(docIDCellEditor);
        columnConfigs[5].setCellChangesSave((item, columnCodeName) -> saveDocumentEditCellValue((FileResource) item));

        //typeCellEditor
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
        columnConfigs[8].setCellEditor(typeCellEditor);
        columnConfigs[8].setCellChangesSave(new CellChange<FileResource>() {
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
        columnConfigs[11].setCellEditor(reminderCellEditor);
        columnConfigs[11].setCellChangesSave(new CellChange<FileResource>() {
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
        columnConfigs[9].setCellEditor(expireDateCellEditor);
        columnConfigs[9].setCellChangesSave(new CellChange<FileResource>() {
            @Override
            public void saveCell(FileResource item, String columnCodeName) {
                saveDocumentEditCellValue(item);
            }
        });

        DocumentsService.App.get().getDocumentTypes(typeCode, new AbstractAsyncCallback<HashMap<Integer, ArrayList<SelectItem>>>() {
            @Override
            public void success(HashMap<Integer, ArrayList<SelectItem>> documenTypes) {
                if (documenTypes != null && documenTypes.size() > 0) {
                    typeCellEditor.getListBox().setItems(documenTypes.get(Constants.F_EMPLOYEE_PROFILE).toArray(new SelectItem[]{}));
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
        listBox.addListItem(new SelectItem(60, "" + wfmStrings.oneHour(), 60 + "", "60"));
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
        DocumentsService.App.get().updateFiles(items, item.getEntityID(), typeCode, new AbstractAsyncCallback<Void>() {
            @Override
            public void success(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_DOC_LISTING_EDIT, result, EmployeeDocumentsListView.this);
            }
        });
    }

    private ListingRequestProvider<FileResource> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            loadEmployeeDocuments(filterParametrs, callback, null);
        };
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.UPLOAD_EMPLOYEE_DOCUMENTS) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("employeeDocument|add/add/" + userID) : null;
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
                            data.setObjectID(userID);
                            data.setTypeId(Constants.F_EMPLOYEE_PROFILE);
                            RbacService.App.get().getDocumentFacetFilterData(data, typeCode, new AbstractAsyncCallback<FacetFilterRpc>() {
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
                if (Utils.hasPermission(PermissionConstants.UPLOAD_EMPLOYEE_DOCUMENTS)) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> {
                        String url = "employeeDocument|add/add" + "/" + userID;
                        if (typeCode != null && !"".equals(typeCode)) {
                            url = url + "/" + typeCode;
                        }
                        SinksContainerFactory.entryPoint.onHistoryChanged(url);

                    });
                    return addnew;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message;
                message = new DefaultNoItemsMessage(wfmStrings.thereAreNoItemsToShow());
                if (Utils.hasPermission(PermissionConstants.UPLOAD_EMPLOYEE_DOCUMENTS)) {
                    String url = "employeeDocument|add/add" + "/" + userID;
                    if (typeCode != null && !"".equals(typeCode)) {
                        url = url + "/" + typeCode;
                    }
                    message.setHref(url);
                    message.setTextBeforeLink(hrmsStrings.noDocumentLink());
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.DocumentFacetFilter.getContentCode()[0], wfmStrings.employee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrFolderRepresenter.FIELD_ENTITY_USER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrFolderRepresenter.FIELD_ENTITY_USER_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.DocumentFacetFilter.getContentCode()[1], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrFolderRepresenter.FIELD_DOCUMENT_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrFolderRepresenter.FIELD_DOCUMENT_TYPE_ID_NAME;
            }
        });
        contentConfigure.addContentConfigureDateListBox(SolrFolderRepresenter.FIELD_ISSUED_DATE, wfmStrings.issuedDate());
        contentConfigure.addContentConfigureDateListBox(SolrFolderRepresenter.FIELD_EXPIRE_DATE, wfmStrings.expiryDate());
        contentConfigure.addContentConfigureDateListBox(SolrFolderRepresenter.FIELD_REMINDER_NAME, wfmStrings.reminders());
        contentConfigure.addContentConfigureDateListBox(SolrFolderRepresenter.FIELD_DATE_CREATION, wfmStrings.createdDate());
        return contentConfigure;
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadEmployeeDocuments(new ListingFilterParameter(), null, container);
    }

    private void loadEmployeeDocuments(ListingFilterParameter filterParametrs, ListingCallback callback, Span container) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        filterParametrs.setFolderType(Constants.F_EMPLOYEE_PROFILE);
        filterParametrs.setEntityID(fromEmployeeProfile ? userID : entityId);
        if (!fromEmployeeProfile && Utils.hasPermission(PermissionConstants.VIEW_ALL_EMPLOYEE_DOCUMENTS)) {
            filterParametrs.setHasFullListAccess(true);
        } else {
            filterParametrs.setCrmEntityId(userID);
        }
        filterParametrs.setModule(LayoutRPC.HRMS_SECTION);
        filterParametrs.setViewType(typeCode);
        DocumentsService.App.get().getDocumentList(filterParametrs, new AbstractAsyncCallback<ListResult<FileResource>>() {
            @Override
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void success(ListResult<FileResource> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }

                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
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
        return EMPLOYEE_DOCUMENTS;
    }
}
