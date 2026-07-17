package com.edatasite.workforce.gwt.documents.client.upload;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.profile.ActionTimesTO;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SelectItemCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.cell.client.DatePickerCellCustom;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextAreaEditCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Djuraev on 9/23/15.
 */
public class EditableAttachmentLinksComponents extends GeneralAttachmentLinksComponent {

    public EditableAttachmentLinksComponents(FileResource[] attachments, boolean removable, List<SelectItem> typeName, Integer folderType, String typeCode) {
        super(attachments, removable, typeName, folderType, typeCode);
    }

    public final ProvidesKey<FileResource> KEY_PROVIDER = fileResource -> fileResource == null ? null : fileResource.getObjectId();

    protected void initialize() {
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER, true);
        dataGrid.getElement().setId("editable_attachments");
        dataGrid.setWidth("100%");
        dataGrid.getElement().getStyle().setProperty("maxHeight", "300px");
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-attachment cellBasedWidget-mod--static-body box-radius--top");
        dataGrid.setEmptyTableWidget(getNoItemsMessage(wfmStrings.thereAreNoAttachmentsYet()));
        dataGrid.addStyleName("thereAreNoAttachmentsYet EditableAttachmentLinksComponents");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataGrid.supplyProvider(attachments);
        initWidget(dataGrid);

        if (INSURANCE.equals(typeCode)) {
            getInsuranceColumns();
        } else {
            loadData(getSection(foldeType));
        }
    }

    @Override
    protected void initColumns() {
        if (columnConfigs != null && columnConfigs.length > 0) {
            for (ColumnConfigs columnConfig : columnConfigs) {
                switch (columnConfig.getCode()) {
                    case "NAME":
                        TextInputCell filenameCell = new TextInputCell("document_name form-control");
                        Column<FileResource, String> docName = new Column<FileResource, String>(filenameCell) {
                            @Override
                            public String getValue(FileResource item) {

                                String fileName = item.getFileName();
                                String selectedName = "";
                                if (fileName != null) {
                                    int lastIndexOf = item.getFileName().lastIndexOf(".");
                                    if(lastIndexOf > 0){
                                        selectedName = item.getFileName().substring(0, item.getFileName().lastIndexOf("."));
                                    }
                                }
                                if (item.getDocumentName() == null) {
                                    item.setDocumentName(selectedName);
                                } else {
                                    selectedName = item.getDocumentName();
                                }
                                return selectedName;
                            }
                        };
                        docName.setFieldUpdater((index, item, value) -> item.setDocumentName(value));
                        docName.setCellStyleNames("");
                        dataGrid.addColumn(docName, columnConfig.getTitle());
                        dataGrid.setColumnWidth(docName, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        //document description
                        TextAreaEditCell textAreaEditCell = new TextAreaEditCell("document_description");
                        Column<FileResource, String> desc = new Column<FileResource, String>(textAreaEditCell) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getDescription() != null ? item.getDescription() : "";
                            }
                        };
                        desc.setFieldUpdater((i, item, s) -> item.setDescription(s));
                        dataGrid.addColumn(desc, columnConfig.getTitle());
                        dataGrid.setColumnWidth(desc, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "DOCUMENT_ID":
                        //document ID
                        TextInputCell docInput = new TextInputCell("document_id form-control");
                        Column<FileResource, String> docID = new Column<FileResource, String>(docInput) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getDocID() != null ? item.getDocID() : "";
                            }
                        };
                        docID.setFieldUpdater((i, item, s) -> item.setDocumentID(s));
                        dataGrid.addColumn(docID, wfmStrings.documentID());
                        dataGrid.setColumnWidth(docID, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "TYPE":
                        //Doc type
                        SelectItemCell selectionCell = new SelectItemCell(typeNames);
                        selectionCell.setStyleName("document_type");
                        Column<FileResource, SelectItem> type = new Column<FileResource, SelectItem>(selectionCell) {

                            @Override
                            public SelectItem getValue(FileResource object) {
                                return new SelectItem(object.getTypeId());
                            }
                        };
                        type.setFieldUpdater((i, item, value) -> item.setTypeId(value.getId()));
                        dataGrid.addColumn(type, columnConfig.getTitle());
                        dataGrid.setColumnWidth(type, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "CREATED_DATE":
                        Column<FileResource, String> creationDate = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getCreationDate() != null ? DateUtils.format(item.getCreationDate()) : "";
                            }
                        };

                        dataGrid.addColumn(creationDate, wfmStrings.createdDate());
                        dataGrid.setColumnWidth(creationDate, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "FILE_SIZE":
                        Column<FileResource, String> fileSize = new Column<FileResource, String>(new TextCell()) {
                            @Override
                            public String getValue(FileResource item) {
                                return item.getFileSizeAsString();
                            }
                        };
                        dataGrid.addColumn(fileSize, wfmStrings.fileSize());
                        dataGrid.setColumnWidth(fileSize, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                    case "DOWNLOAD":
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
                        break;
                    case "REMOVE":
                        if (removable) {
                            IconCell removeCell = new IconCell("ficon--trash pointer");
                            Column<FileResource, String> remove = new Column<FileResource, String>(removeCell) {
                                @Override
                                public String getValue(FileResource item) {
                                    removeCell.setClickHandler(clickEvent -> {
                                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo,
                                                (wfmStrings.sureYouWantToDelete() ), new CloseHandler() {
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
                            dataGrid.addColumn(remove, wfmStrings.delete());
                            dataGrid.setColumnWidth(remove, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                            dataGrid.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
                            dataGrid.addCellPreviewHandler(event -> {
                                        if ("keydown".equals(event.getNativeEvent().getType())) {
                                            Element tabElem = event.getNativeEvent().getEventTarget().cast();
                                            Element td;
                                            if (tabElem != null) {
                                                td = getParentTD(tabElem);
                                                if (td != null && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
                                                    if (event.getNativeEvent().getShiftKey()) {
                                                        Element prevSibling = td.getPreviousSiblingElement();
                                                        if (prevSibling == null) {
                                                            gotoPrevRow(td, event);
                                                        } else {
                                                            setFocus(prevSibling, "backward", event);

                                                            event.getNativeEvent().preventDefault();
                                                            event.getNativeEvent().stopPropagation();

                                                            if (getUserAgent().contains("msie")) {
                                                                preventDefaultBehaviour(event.getNativeEvent());
                                                            }
                                                        }
                                                    } else {//TAB
                                                        Element nextSibling = td.getNextSiblingElement();
                                                        if (nextSibling == null) {
                                                            gotoNextRow(td, event);
                                                        } else {
                                                            setFocus(nextSibling, "forward", event);

                                                            event.getNativeEvent().preventDefault();
                                                            event.getNativeEvent().stopPropagation();

                                                            if (getUserAgent().contains("msie")) {
                                                                preventDefaultBehaviour(event.getNativeEvent());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                            );
                        }
                        break;
                    case "ISSUED_DATE":
                        //Issed date
                        Column<FileResource, Date> issuedDate = new Column<FileResource, Date>(new DatePickerCellCustom(DateUtils.getFormat(), "document_issue_date form-control")) {
                            @Override
                            public Date getValue(FileResource item) {
                                return item.getIssuedDate() != null && item.getIssuedDate().getDate() != null ? item.getIssuedDate().getNonConvertedDate() : null;
                            }
                        };
                        issuedDate.setSortable(false);
                        issuedDate.setFieldUpdater((i, item, date) -> {
                            if (date != null) {
                                item.setIssuedDate(new DateNonConvertable(date));
                            }
                        });
                        dataGrid.addColumn(issuedDate, columnConfig.getTitle());
                        dataGrid.setColumnWidth(issuedDate, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                        break;
                    case "EXPIRY_DATE":
                        //expire date
                        Column<FileResource, Date> expireDate = new Column<FileResource, Date>(new DatePickerCellCustom(DateUtils.getFormat(), "document_expiry_date form-control")) {
                            @Override
                            public Date getValue(FileResource item) {
                                return item.getExpireDate() != null && item.getExpireDate().getDate() != null ? item.getExpireDate().getNonConvertedDate() : null;
                            }
                        };
                        expireDate.setSortable(false);
                        expireDate.setFieldUpdater((index, object, date) -> {
                                    if (date != null) {
                                        object.setExpireDate(new DateNonConvertable(date));
                                    }
                                }
                        );
                        dataGrid.addColumn(expireDate, wfmStrings.expiryDate());
                        dataGrid.setColumnWidth(expireDate, columnConfig.getWidth(), com.google.gwt.dom.client.Style.Unit.PCT);
                        break;
                    case "ENABLE_REMINDER":
                        SelectItemCell selectionReminder = new SelectItemCell(getReminderItems());
                        selectionReminder.setStyleName("document_reminder_type");
                        Column<FileResource, SelectItem> reminder = new Column<FileResource, SelectItem>(selectionReminder) {

                            @Override
                            public SelectItem getValue(FileResource object) {
                                return new SelectItem(object.getReminderId());
                            }
                        };
                        reminder.setFieldUpdater((i, item, value) -> {
                            item.setReminderId(value.getId());
                            item.setReminderName(value.getName());
                            item.setActionTimes(getActionTimeItems(value));
                        });
                        dataGrid.addColumn(reminder, columnConfig.getTitle());
                        dataGrid.setColumnWidth(reminder, columnConfig.getWidth(), Style.Unit.PCT);
                        break;
                }
            }
        }
    }

    private void getInsuranceColumns() {
        //Insurance firstName
        TextInputCell insureeNameCell = new TextInputCell();
        Column<FileResource, String> insureeName = new Column<FileResource, String>(insureeNameCell) {
            @Override
            public String getValue(FileResource item) {
                return item.getInsureeName();
            }
        };
        insureeName.setFieldUpdater((index, item, value) -> item.setInsureeName(value));
        insureeName.setCellStyleNames("");
        dataGrid.addColumn(insureeName, wfmStrings.firstName());
        dataGrid.setColumnWidth(insureeName, 18, Style.Unit.PCT);

        //Insurance LastName
        TextInputCell insureeLastNameCell = new TextInputCell();
        Column<FileResource, String> insureeLastName = new Column<FileResource, String>(insureeLastNameCell) {
            @Override
            public String getValue(FileResource item) {
                return item.getInsureeLastName();
            }
        };
        insureeLastName.setFieldUpdater((index, item, value) -> item.setInsureeLastName(value));
        insureeLastName.setCellStyleNames("");
        dataGrid.addColumn(insureeLastName, wfmStrings.lastName());
        dataGrid.setColumnWidth(insureeLastName, 18, Style.Unit.PCT);

        //Insurance status
        SelectItemCell statusCell = new SelectItemCell(getStatusItems());
        Column<FileResource, SelectItem> reminder = new Column<FileResource, SelectItem>(statusCell) {

            @Override
            public SelectItem getValue(FileResource object) {
                return new SelectItem(object.getStatusId());
            }
        };
        reminder.setFieldUpdater((i, item, value) -> {
            item.setStatusId(value.getId());
            item.setStatusName(value.getName());
        });
        dataGrid.addColumn(reminder, wfmStrings.status());
        dataGrid.setColumnWidth(reminder, 12, Style.Unit.PCT);

        //document description
        TextInputCell costCell = new TextInputCell();
        Column<FileResource, String> cost = new Column<FileResource, String>(costCell) {
            @Override
            public String getValue(FileResource item) {
                return item.getInsuranceCost();
            }
        };
        cost.setFieldUpdater((i, item, s) -> item.setInsuranceCost(s));
        dataGrid.addColumn(cost, wfmStrings.insuranceCost());
        dataGrid.setColumnWidth(cost, 17, Style.Unit.PCT);


        //document ID
        TextInputCell planCell = new TextInputCell();
        Column<FileResource, String> plan = new Column<FileResource, String>(planCell) {
            @Override
            public String getValue(FileResource item) {
                return item.getInsurancePlan();
            }
        };
        plan.setFieldUpdater((i, item, s) -> item.setInsurancePlan(s));
        dataGrid.addColumn(plan, wfmStrings.insurancePlan());
        dataGrid.setColumnWidth(plan, 12, Style.Unit.PCT);

        //document ID
        TextInputCell coverageCell = new TextInputCell();
        Column<FileResource, String> coverage = new Column<FileResource, String>(coverageCell) {
            @Override
            public String getValue(FileResource item) {
                return item.getInsuranceCoverage();
            }
        };
        coverage.setFieldUpdater((i, item, s) -> item.setInsuranceCoverage(s));
        dataGrid.addColumn(coverage, wfmStrings.insuranceCoverage());
        dataGrid.setColumnWidth(coverage, 12, Style.Unit.PCT);

        //Issed date
        Column<FileResource, Date> issuedDate = new Column<FileResource, Date>(new DatePickerCellCustom(DateUtils.getFormat(), "document_issue_date form-control")) {
            @Override
            public Date getValue(FileResource item) {
                return item.getIssuedDate() != null && item.getIssuedDate().getDate() != null ? item.getIssuedDate().getNonConvertedDate() : null;
            }
        };
        issuedDate.setSortable(false);
        issuedDate.setFieldUpdater((i, item, date) -> {
            if (date != null) {
                item.setIssuedDate(new DateNonConvertable(date));
            }
        });
        dataGrid.addColumn(issuedDate, wfmStrings.startDate());
        dataGrid.setColumnWidth(issuedDate, 13, com.google.gwt.dom.client.Style.Unit.PCT);


        //expire date
        Column<FileResource, Date> expireDate = new Column<FileResource, Date>(new DatePickerCellCustom(DateUtils.getFormat(), "document_expiry_date form-control")) {
            @Override
            public Date getValue(FileResource item) {
                return null;
            }
        };
        expireDate.setSortable(false);
        expireDate.setFieldUpdater((index, object, date) -> {
                    if (date != null) {
                        object.setExpireDate(new DateNonConvertable(date));
                    }
                }
        );
        dataGrid.addColumn(expireDate, wfmStrings.expiryDate());
        dataGrid.setColumnWidth(expireDate, 13, com.google.gwt.dom.client.Style.Unit.PCT);

        //Reminder type
        SelectItemCell selectionReminder = new SelectItemCell(getReminderItems());
        selectionReminder.setStyleName("document_reminder_type");
        Column<FileResource, SelectItem> reminder1 = new Column<FileResource, SelectItem>(selectionReminder) {

            @Override
            public SelectItem getValue(FileResource object) {
                return new SelectItem(object.getReminderId());
            }
        };
        reminder1.setFieldUpdater((i, item, value) -> {
            item.setReminderId(value.getId());
            item.setReminderName(value.getName());
            item.setActionTimes(getActionTimeItems(value));
        });
        dataGrid.addColumn(reminder1, wfmStrings.enableReminder());
        dataGrid.setColumnWidth(reminder1, 12, Style.Unit.PCT);

        if (isDownload) {
            Column<FileResource, SafeHtml> downloadlink = new Column<FileResource, SafeHtml>(new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(final FileResource item) {
                    return () -> {
                        String download;
                        if (GOOGLE.equals(item.getUploadType())) {
                            download = wfmStrings.openInGoogleDocs();
                        } else if (item.getUploadType().equals(OFFICE_365) || item.getUploadType().equals(OFFICE_365_SHARE_POINT)) {
                            download = wfmStrings.openInOfficeDocs();
                        } else {
                            download = wfmStrings.download();
                        }
                        return "<a class='markDownload' href=\"" + item.getDownloadUrl() + "\" target=\"_blank\">" + download + "</a>";
                    };
                }
            };

            dataGrid.addColumn(downloadlink, wfmStrings.download());
            dataGrid.setColumnWidth(downloadlink, 12, com.google.gwt.dom.client.Style.Unit.PCT);
        }


        if (removable) {
            IconCell removeCell = new IconCell("ficon--trash pointer");
            Column<FileResource, String> remove = new Column<FileResource, String>(removeCell) {
                @Override
                public String getValue(FileResource item) {
                    removeCell.setClickHandler(clickEvent -> {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo,
                                (wfmStrings.sureYouWantToDelete() ), new CloseHandler() {
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
            dataGrid.addColumn(remove, wfmStrings.delete());
            dataGrid.setColumnWidth(remove, 10, com.google.gwt.dom.client.Style.Unit.PCT);
            dataGrid.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.DISABLED);
            dataGrid.addCellPreviewHandler(event -> {
                        if ("keydown".equals(event.getNativeEvent().getType())) {
                            Element tabElem = event.getNativeEvent().getEventTarget().cast();
                            Element td;
                            if (tabElem != null) {
                                td = getParentTD(tabElem);
                                if (td != null && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
                                    if (event.getNativeEvent().getShiftKey()) {
                                        Element prevSibling = td.getPreviousSiblingElement();
                                        if (prevSibling == null) {
                                            gotoPrevRow(td, event);
                                        } else {
                                            setFocus(prevSibling, "backward", event);

                                            event.getNativeEvent().preventDefault();
                                            event.getNativeEvent().stopPropagation();

                                            if (getUserAgent().contains("msie")) {
                                                preventDefaultBehaviour(event.getNativeEvent());
                                            }
                                        }
                                    } else {//TAB
                                        Element nextSibling = td.getNextSiblingElement();
                                        if (nextSibling == null) {
                                            gotoNextRow(td, event);
                                        } else {
                                            setFocus(nextSibling, "forward", event);

                                            event.getNativeEvent().preventDefault();
                                            event.getNativeEvent().stopPropagation();

                                            if (getUserAgent().contains("msie")) {
                                                preventDefaultBehaviour(event.getNativeEvent());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

            );
        }

    }

    private ArrayList<ActionTimesTO> getActionTimeItems(SelectItem value) {
        ArrayList<ActionTimesTO> actionTimes = new ArrayList<>();
        ActionTimesTO actionTime = new ActionTimesTO();
        actionTime.setActionPeriod(value.getDescription());
        actionTime.setActionNumber(value.getCategory());
        actionTime.setActiontype("BEFORE");
        actionTimes.add(actionTime);
        return actionTimes;
    }

    private List<SelectItem> getReminderItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(0, "Please select"));
        items.add(new SelectItem(1, "1 " + wfmStrings.day(), 60 * 24 + "", "1"));
        items.add(new SelectItem(2, "2 " + wfmStrings.days(), 60 * 24 + "", "2"));
        items.add(new SelectItem(3, "3 " + wfmStrings.days(), 60 * 24 + "", "3"));
        items.add(new SelectItem(7, "7 " + wfmStrings.days(), 60 * 24 + "", "7"));
        items.add(new SelectItem(7 * 2, "2 " + wfmStrings.weeks(), 60 * 24 * 7 + "", "2"));
        items.add(new SelectItem(30, "1 " + wfmStrings.months(), 60 * 24 * 30 + "", "1"));
        items.add(new SelectItem(45, "45 " + wfmStrings.days(), 60 * 24 + "", "45"));
        items.add(new SelectItem(30 * 2, "2 " + wfmStrings.months(), 60 * 24 * 30 + "", "2"));
        items.add(new SelectItem(30 * 3, "3 " + wfmStrings.months(), 60 * 24 * 30 + "", "3"));
        return items;
    }

    private List<SelectItem> getStatusItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(0, "Please select"));
        items.add(new SelectItem(1, "Employee", "Employee", "1"));
        items.add(new SelectItem(2, "Emp Dependent", "Emp Dependent", "2"));
        return items;
    }

    public FileResource[] getAttachments() {
        return dataGrid.getList().toArray(new FileResource[]{});
    }

    @Override
    public boolean validate() {
        int error = 0;
        for (int i = 0; i < dataGrid.getRowCount(); i++) {
            for (int j = 0; j < columnConfigs.length; j++) {
                if (columnConfigs[j].isRequired()) {
                    TableCellElement tableCellElement = dataGrid.getRowElement(i).getCells().getItem(j);
                    switch (columnConfigs[j].getCode()) {
                        case "NAME":
                            error += colorizeErrorField(tableCellElement, Utils.isNullOrEmpty(attachments[i].getDocumentName()));
                            break;
                        case ItemTableConstants.DESCRIPTION:
                            error += colorizeErrorField(tableCellElement, Utils.isNullOrEmpty(attachments[i].getDescription()));
                            break;
                        case "DOCUMENT_ID":
                            error += colorizeErrorField(tableCellElement, Utils.isNullOrEmpty(attachments[i].getDocID()));
                            break;
                        case "TYPE":
                            error += colorizeErrorField(tableCellElement, attachments[i].getTypeId() == null);
                            break;
                        case "ISSUE_DATE":
                            error += colorizeErrorField(tableCellElement, attachments[i].getIssuedDate() == null);
                            break;
                        case "EXPIRY_DATE":
                            error += colorizeErrorField(tableCellElement, attachments[i].getExpireDate() == null);
                            break;
                        case "ENABLE_REMINDER":
                            error += colorizeErrorField(tableCellElement, attachments[i].getReminderId() == null);
                            break;
                    }
                }
            }
        }
        return error == 0;
    }

    private Integer colorizeErrorField(TableCellElement tableCellElement, boolean hasError) {
        if (tableCellElement != null) {
            if (tableCellElement.getFirstChildElement() != null) {
                if (hasError) {
                    tableCellElement.getFirstChildElement().addClassName(ERROR_FORM_STYLE);
                } else {
                    tableCellElement.getFirstChildElement().removeClassName(ERROR_FORM_STYLE);
                }
            } else {
                if (hasError) {
                    tableCellElement.addClassName(ERROR_FORM_STYLE);
                } else {
                    tableCellElement.removeClassName(ERROR_FORM_STYLE);
                }

            }
        }
        return hasError ? 1 : 0;
    }

    private void gotoNextRow(Element td, CellPreviewEvent event) {
        // find current row
        Element thisTr = td.getParentElement();

        // find next row
        Element nextTr = thisTr.getNextSiblingElement();

        event.getNativeEvent().preventDefault();
        event.getNativeEvent().stopPropagation();

        if (getUserAgent().contains("msie")) {
            preventDefaultBehaviour(event.getNativeEvent());
        }
        if (nextTr != null) {
            Element firstTd = nextTr.getFirstChildElement();
            // set focus in the first td's input element
            setFocus(firstTd, "forward", event);
        }
    }

    private void gotoPrevRow(Element td, CellPreviewEvent event) {
        // find current row
        Element thisTr = td.getParentElement();

        Element prevTr = thisTr.getPreviousSiblingElement();
        event.getNativeEvent().preventDefault();
        event.getNativeEvent().stopPropagation();

        if (getUserAgent().contains("msie")) {
            preventDefaultBehaviour(event.getNativeEvent());
        }

        // get last td inside tr
        if (prevTr != null) {
            Element lastTd = prevTr.getLastChild().cast();
            // set focus in the last td's input element
            setFocus(lastTd, "backward", event);
        }
    }

    private void setFocus(Element td, final String dir, CellPreviewEvent event) {
        Element input = td.getFirstChildElement().getFirstChildElement();
        if (input != null
                && (input.getTagName().equalsIgnoreCase("input")
                || input.getTagName().equalsIgnoreCase("select")
                || input.getTagName().equalsIgnoreCase("textarea")
                || input.getTagName().equalsIgnoreCase("button"))) {
            input.focus();
        } else {
            if (dir != null && "forward".equalsIgnoreCase(dir)) {
                if (td.getNextSiblingElement() == null) {
                    gotoNextRow(td, event);
                } else {
                    setFocus(td.getNextSiblingElement(), dir, event);
                }
            } else {
                if (td.getPreviousSiblingElement() == null) {
                    gotoPrevRow(td, event);
                } else {
                    setFocus(td.getPreviousSiblingElement(), dir, event);
                }
            }
        }
    }

    private Element getParentTD(Element e) {
        Element parent = e.getParentElement();
        if (parent == null) {
            return null;
        }
        if (parent.getTagName().equalsIgnoreCase("td")) {
            return parent;
        }
        return getParentTD(parent);
    }

    /**
     * Get the browser type. This is needed to check for IE.
     *
     * @return user agent
     */

    public static native String getUserAgent() /*-{
        return navigator.userAgent.toLowerCase();
    }-*/;

    /**
     * Only for IE8 fixes. IE8 doesn't seem to know about preventDefault.
     *
     * @param event the event which is to be prevented.
     */
    public static native void preventDefaultBehaviour(NativeEvent event) /*-{
        event.returnValue = false;
        $wnd.event.cancelBubble = true;
    }-*/;
}
