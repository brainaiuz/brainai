package com.edatasite.workforce.gwt.documents.client.upload;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkAndTextCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 04.07.2010
 * Time: 22:36:07
 */
public class GeneralAttachmentLinksComponent extends Composite implements Constants, CommandConstants {

    
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected FileResource[] attachments;
    protected boolean removable;
    protected boolean isDownload;
    protected KpiDataGrid<FileResource> dataGrid;
    protected List<SelectItem> typeNames;
    protected Integer foldeType;
    protected String typeCode;
    protected ColumnConfigs[] columnConfigs;

    protected AttachmentDeleteHandler clickRemoveLink;

    public GeneralAttachmentLinksComponent(FileResource[] attachments, boolean removable) {
        this(attachments, true, removable);
    }

    public GeneralAttachmentLinksComponent(FileResource[] attachments, boolean removable, List<SelectItem> typeNames, Integer folderType, String typeCode) {
        this.attachments = attachments;
        this.removable = removable;
        this.typeNames = typeNames;
        this.foldeType = folderType;
        this.typeCode = typeCode;
        initialize();
    }

    public GeneralAttachmentLinksComponent(FileResource[] attachments, Integer folderType, boolean isDownload, boolean removable) {
        this.attachments = attachments;
        this.isDownload = isDownload;
        this.removable = removable;
        this.foldeType = folderType;
        initialize();
    }

    public GeneralAttachmentLinksComponent(FileResource[] attachments, boolean isDownload, boolean removable) {
        this(attachments, null, isDownload, removable);
    }

    public void onRemoveAttachment(AttachmentDeleteHandler clickRemoveLink) {
        if (removable) {
            this.clickRemoveLink = clickRemoveLink;
        }
    }

    public final ProvidesKey<FileResource> KEY_PROVIDER = fileResource -> fileResource == null ? null : fileResource.getObjectId();

    protected void initialize() {
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER, true);

        dataGrid.setWidth("100%");
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-attachment cellBasedWidget-mod--static-body box-radius--top");
        dataGrid.setEmptyTableWidget(getNoItemsMessage(wfmStrings.thereAreNoAttachmentsYet()));
        dataGrid.addStyleName("thereAreNoAttachmentsYet GeneralAttachmentLinksComponint");
        dataGrid.supplyProvider(attachments);

        initWidget(dataGrid);
        loadData(getSection(foldeType));
    }

    public KpiDataGrid getDataGrid() {
        return dataGrid;
    }

    private void renamePopup(final String filename, final Integer fileID) {
        final KpiModal dialogBox = new KpiModal();
        dialogBox.setWidth(400);
        dialogBox.setTitle(wfmStrings.fileName());

        final TextBox fileName = new TextBox();
        fileName.addStyleName(DEFAULT_WIDTH);
        fileName.setText(filename);

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        save.addClickHandler(clickEvent -> {
            if (fileName.getText().equals("")) {
                Window.alert("Please enter file name");
            } else {
                DocumentsService.App.get().updateFile(fileID, fileName.getText(), false, null, new AbstractAsyncCallback() {
                    @Override
                    public void success(Object result) {
                        dataGrid.refresh();
                        dialogBox.close();
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        try {
                            throw throwable;
                        } catch (InsufficientPermissionsException e) {
                            DocumentsView.get().displayError(e.getMessage());
                        } catch (ObjectNotFoundException e) {
                            DocumentsView.get().displayError(wfmStrings.resursNotFoundAndNoShare());
                        } catch (DuplicateNameException e) {
                            DocumentsView.get().displayError(wfmStrings.file() + " " + wfmStrings.withTheSameNameAlreadyExist());
                        } catch (Throwable e) {
                            // last resort  a very unexpected exception
                        }
                    }
                });
                dialogBox.close();
            }
        });
        cancel.addClickHandler(clickEvent -> dialogBox.close());

        dialogBox.add(fileName);
        dialogBox.addButton(cancel);
        dialogBox.addButton(save);
        dialogBox.open();
    }

    public void supplyProvider(FileResource[] files, boolean isEditableForm) {
        ArrayList<FileResource> list = new ArrayList<>();
        if (!isEditableForm && attachments != null && attachments.length > 0) {
            list.addAll(Arrays.asList(attachments));
        }
        if (files != null && files.length > 0) {
            for (FileResource file : files) {
                if (!list.contains(file)) {
                    list.add(file);
                }
            }
        }
               if (list.size() > 0 && isEditableForm) dataGrid.setHeight((list.size()*68+35)+"px");
                dataGrid.supplyProvider(list.toArray(new FileResource[]{}));

            if (list.size() == 0){
                dataGrid.setWidth("100%");
                dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-attachment cellBasedWidget-mod--static-body box-radius--top");
                dataGrid.setEmptyTableWidget(getNoItemsMessage(wfmStrings.thereAreNoAttachmentsYet()));
                dataGrid.addStyleName("thereAreNoAttachmentsYet GeneralAttachmentLinksComponint");
                dataGrid.supplyProvider(attachments);
                dataGrid.setHeight((45+35)+"px");
        }
    }

    public static DockPanel getNoItemsMessage(String noItemText) {
        DockPanel dockPanel = new DockPanel();
        dockPanel.setSize("100%", "100%");
        dockPanel.setStyleName("leaveReqCenter");
        dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
        dockPanel.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
        VerticalPanel panel = new VerticalPanel();
        HTML text = new HTML(noItemText);
        text.addStyleName("center GeneralAttachmentLinksComponent");
        panel.add(text);
        dockPanel.add(panel, DockPanel.CENTER);
        return dockPanel;
    }

    public FileResource[] getAttachments() {
        return attachments != null ? attachments : new FileResource[]{};
    }


    public boolean validate() {
        return true;
    }

    protected ItemTableEnum getSection(Integer foldeType) {
        if (foldeType != null) {
            if (F_CRM_ACCOUNT == foldeType) {
                return ItemTableEnum.CLIENT_FORM_ATTACHMENTS;
            } else if (F_PRODUCTS_SERVICES == foldeType) {
                return ItemTableEnum.PRODUCT_ATTACHMENTS;
            } else if (F_COMPANY_DOCUMENTS == foldeType || F_EMPLOYEE_PROFILE == foldeType) {
                return ItemTableEnum.COMPANY_DOC_ATTACHMENTS;
            }else if (F_CANDIDATE == foldeType || F_PLACEMENT == foldeType){
                    return ItemTableEnum.COMPANY_DOC_ATTACHMENTS;
            }
        }
        return ItemTableEnum.GENERAL_ATTACHMENTS;
    }

    protected void loadData(ItemTableEnum section) {
        ItemTableSettingService.App.get().getColumnConfigs(section, new AsyncCallback<ColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ColumnConfigs[] result) {
                columnConfigs = result;
                initColumns();
                dataGrid.refresh();
            }
        });
    }

    protected void initColumns() {
        if (columnConfigs != null && columnConfigs.length > 0) {
            for (ColumnConfigs columnConfig : columnConfigs) {
                switch (columnConfig.getCode()) {
                    case "NAME":
                        Column<FileResource, String[]> name = new Column<FileResource, String[]>(new SimpleLinkAndTextCell()) {
                            @Override
                            public String[] getValue(FileResource item) {
                                return new String[]{item.getName(), "", "", ""};
                            }
                        };


                        dataGrid.addColumn(name, columnConfig.getTitle());
                        dataGrid.setColumnWidth(name, columnConfig.getWidth(), Style.Unit.PCT);
                        name.setFieldUpdater((index, object, value) -> Utils.showImageOrDownloadFile(object, false, this.attachments));
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        Column<FileResource, String> desc = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getDescription() != null ? item.getDescription() : "";
                            }
                        };

                        dataGrid.addColumn(desc, columnConfig.getTitle());
                        dataGrid.setColumnWidth(desc, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "DOCUMENT_ID":
                        Column<FileResource, String> docId = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getDocID() != null ? item.getDocID() : "";
                            }
                        };

                        dataGrid.addColumn(docId, columnConfig.getTitle());
                        dataGrid.setColumnWidth(docId, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "TYPE":
                        Column<FileResource, String> type = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getType() != null ? item.getType() : "";
                            }
                        };

                        dataGrid.addColumn(type, columnConfig.getTitle());
                        dataGrid.setColumnWidth(type, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "CREATED_DATE":
                        Column<FileResource, String> creationDate = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getCreationDate() != null ? DateUtils.formatInternalShort2(item.getCreationDate()) : "";
                            }
                        };

                        dataGrid.addColumn(creationDate, columnConfig.getTitle());
                        dataGrid.setColumnWidth(creationDate, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "FILE_SIZE":
                        Column<FileResource, String> fileSize = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getFileSizeAsString();
                            }
                        };
                        dataGrid.addColumn(fileSize, columnConfig.getTitle());
                        dataGrid.setColumnWidth(fileSize, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "DOWNLOAD":
                        if (isDownload) {
                            Column<FileResource, SafeHtml> downloadlink = new Column<FileResource, SafeHtml>(new SafeHtmlCell()) {
                                @Override
                                public SafeHtml getValue(final FileResource item) {
                                    return () -> {
                                        String download;
                                        if (GOOGLE.equals(item.getUploadType())) {
                                            download = wfmStrings.openInGoogleDocs();
                                        } else if (OFFICE_365.equals(item.getUploadType())) {
                                            download = wfmStrings.openInOfficeDocs();
                                        } else if (OFFICE_365_SHARE_POINT.equals(item.getUploadType())) {
                                            download = wfmStrings.openInOfficeSharePointDocs();
                                        } else {
                                            download = wfmStrings.download();
                                        }
                                        return "<a class='markDownload' href=\"" + item.getDownloadUrl() + "\" target=\"_blank\">" + download + "</a>";
                                    };
                                }
                            };

                            dataGrid.addColumn(downloadlink, columnConfig.getTitle());
                            dataGrid.setColumnWidth(downloadlink, columnConfig.getWidth(), Style.Unit.PCT);
                        }
                        break;
                    case "REMOVE":
                        if (removable) {
                            IconCell removeCell = new IconCell("ficon--trash pointer");
                            Column<FileResource, String> remove = new Column<FileResource, String>(removeCell) {
                                @Override
                                public String getValue(FileResource item) {
                                    removeCell.setClickHandler(clickEvent -> {
                                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo,
                                                (wfmStrings.sureYouWantToDelete()), new CloseHandler() {
                                            @Override
                                            public void onSubmit() {
                                                if (clickRemoveLink != null) {
                                                    clickRemoveLink.onDelete(item.getObjectId());
                                                }
                                                dataGrid.getList().remove(item);
                                                dataGrid.refresh();
                                            }


                                        });
                                        wfmMessageBox.setTitle(wfmStrings.warning());
                                        wfmMessageBox.open();
                                    });
                                    return null;
                                }
                            };
                            remove.setCellStyleNames("center");
                            dataGrid.addColumn(remove, columnConfig.getTitle());
                            dataGrid.setColumnWidth(remove, columnConfig.getWidth(), Style.Unit.PCT);
                        }
                        break;
                    case "ISSUED_DATE":
                        Column<FileResource, String> issueDate = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getIssuedDate() != null ? DateUtils.format(item.getIssuedDate()) : "";
                            }
                        };

                        dataGrid.addColumn(issueDate, columnConfig.getTitle());
                        dataGrid.setColumnWidth(issueDate, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "EXPIRY_DATE":
                        Column<FileResource, String> expiryDate = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getExpireDate() != null ? DateUtils.format(item.getExpireDate()) : "";
                            }
                        };

                        dataGrid.addColumn(expiryDate, columnConfig.getTitle());
                        dataGrid.setColumnWidth(expiryDate, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "ENABLE_REMINDER":
                        Column<FileResource, String> enableReminder = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getReminderName() != null ? item.getReminderName() : "";
                            }
                        };

                        dataGrid.addColumn(enableReminder, columnConfig.getTitle());
                        dataGrid.setColumnWidth(enableReminder, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                }
            }
        }
    }
}
