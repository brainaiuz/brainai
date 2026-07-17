package com.edatasite.workforce.gwt.accounting.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry.ManualEntryService;
import com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry.ManualEntryServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditBankAccountForm.accountingMessages;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: May 12, 2009
 * Time: 4:43:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManualEntryListView extends BaseListView implements PermissionConstants {
    private ListingPanel<ManualJournalListItem> list;
    private final ManualEntryServiceAsync manualEntryService = ManualEntryService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final ActionButton deleteBtn = null;
    protected HashSet selectedItems = new HashSet();
    private ImportFilePopUp imp;
    private final boolean isProject_To_Head_Enabled = Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && Utils.hasGenericAccess(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED);

    public ManualEntryListView() {
        super("manualTransactions");
        setDescription(property.getPlural(accountingStrings.manualEntries()));
        if (hasPermissionToAdd()) {
            setAddNew("manual|add/add");
        }
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.ManualTransactionsListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadManualTransactionListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            setFilterValues(filterParametrs);
            list.callListExcel(excelURL, filterParametrs);
        });
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/manualTransactionListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            setFilterValues(filterParametrs);
            list.callListPDF(pdfURL, filterParametrs);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, ManualEntryListView.this, (sender, args) -> list.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_DELETED, ManualEntryListView.this, (sender, args) -> list.reloadPage());

//        super.setListingPanel(list);
//        super.display();

        list.addSelectionRowHandler(selectedRows -> {
            if (selectedRows.size() > 0) {
                selectedItems = selectedRows;
                if (deleteBtn != null) {
                    deleteBtn.setVisible(true);
                }
            } else {
                if (deleteBtn != null) {
                    deleteBtn.setVisible(false);
                }
            }
        });

        add(list);
        return null;
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? ManualEntryListView.this::addNewItem : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return ManualEntryListView.this::openUploadModal;
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
                        fields.add(ListingChooseFilter.STATUS);
                        fields.add(ListingChooseFilter.TYPE);
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        fields.add(ListingChooseFilter.CREATOR);
                        if (isProject_To_Head_Enabled) {
                            fields.add(ListingChooseFilter.RELATED_PROJECT);
                        }
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.ManualTransaction;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton addNew = getAddNewButton();
                    String shortCut = "Alt + Shift + M";
                    if (Utils.isChrome()) {
                        shortCut = "Alt + m";
                    }
                    addNew.setTitle(shortCut);
                    addNew.addClickHandler(clickEvent -> addNewItem());
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(ACCOUNTING_MANUAL_JOURNAL_DELETE)) {
                    return getRemoveMoreButton(clickEvent -> {
                        deleteSelection();
                    });
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp = new ImportFilePopUp(ImportTypeEnum.MANUAL_TRANSACTION, null);
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importmanualtransaction|add/add/" + imp.getObjectId());
                    }
                });

                ImportFileActionLink link = new ImportFileActionLink();
                link.addClickHandler(ch -> openUploadModal());
                menuContainer.add(link);

                //Tally
                /*if (Utils.isSuperUser()) {
                    final ImportFilePopUp tallyImportPopup = new ImportFilePopUp(accountingStrings.importString(), ActionButton.Type.BUTTON, "/CreateAttachment", Constants.MANUAL_TRANSACTION_TALLY);
                    tallyImportPopup.setSubmitCompleted(() -> {
                        if (tallyImportPopup.getObjectId() != null) {
                            goTo("importtallymanualtransaction|add/add/" + tallyImportPopup.getObjectId());
                        }
                    });

                    final MenuPopItem importTallyCsvItem = new MenuPopItem("Tally");
                    importTallyCsvItem.setCommand(() -> tallyImportPopup.init());
                    importMenu.addItem(importTallyCsvItem);
                }*/


                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouDoNotHaveAnyManualEntry());
                if (hasPermissionToAdd()) {
                    message.setTextBeforeLink(accountingStrings.noManualEntryLick());
                    message.setHref("manual|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void openUploadModal() {
        imp.open();
    }

    private void addNewItem() {
        SinksContainerFactory.entryPoint.onHistoryChanged("manual|add/add");
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(ACCOUNTING_MANUAL_JOURNAL_ADD);
    }


    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.manualEntry()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        ArrayList<Integer> dontDelete = validateIsUsedManualEntry(selectedItems);
        if (dontDelete != null && dontDelete.size() == selectedItems.size()) {
            Info.show(accountingStrings.cannotDeleteAlreadyApplied(), Info.Type.WARNING);
        } else {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());

            String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
            messageBox.setMessage(message);
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    Integer countSelectSize = selectedItems.size();
                    ArrayList<Integer> ids = getIDsOnly(selectedItems);
                    if (ids.size() > 0) {
                        LoadingPanel.loading(true);
                        manualEntryService.deleteSelectedManualEntryServices(ids, new AbstractAsyncCallback<Integer>() {
                            @Override
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void success(Integer result) {
                                LoadingPanel.loading(false);
                                list.reloadPage();
                                if (result == null) {
                                    Info.show(accountingStrings.errorDeletingManualEntry(), Info.Type.WARNING);
                                } else if (countSelectSize - result > 0) {
                                    Info.show(Property.get(Constants.PRODUCTS_OR_SERVICES, wfmStrings.messSuccessfulyyDeleted(), accountingStrings.productOrService()), Info.Type.INFO);
                                    Info.show(accountingStrings.cannotDeleteAlreadyApplied(), Info.Type.WARNING);
                                } else {
                                    Info.show(Property.get(Constants.PRODUCTS_OR_SERVICES, wfmStrings.messSuccessfulyyDeleted(), accountingStrings.productOrService()), Info.Type.INFO);
                                }
                            }
                        });
                    }
                }
            });
            messageBox.open();
        }
    }

    private static ArrayList<Integer> validateIsUsedManualEntry(Set<ManualJournalListItem> selectedItems) {
        ArrayList<Integer> validate = new ArrayList<>();
        for (ManualJournalListItem item : selectedItems) {
            if (item.isUsed()) {
                validate.add(item.getObjectId());
            }
        }
        return validate;
    }

    private static ArrayList<Integer> getIDsOnly(Set<ManualJournalListItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (ManualJournalListItem item : selectedItems) {
            if (!item.isUsed()) {
                ids.add(item.getObjectId());
            }
        }
        return ids;
    }

    private void setFilterValues(ListingFilterParameter filterParametrs) {
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(filterParametrs.getStartDate()));
            DateNonConvertable toDate = new DateNonConvertable(DateUtil.getDayLastTime(filterParametrs.getEndDate()));
            filterParametrs.setStartDate(fromDate.getNonConvertedDate());
            filterParametrs.setEndDate(toDate.getNonConvertedDate());
        }
    }

    private ListingRequestProvider<ManualJournalListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            setFilterValues(filterParametrs);
            manualEntryService.getManualTransactions(filterParametrs, new AbstractAsyncCallback<ListResult<ManualJournalListItem>>() {
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void success(ListResult<ManualJournalListItem> list) {
                    callback.onSuccess(list);
                }
            });
        };
    }

    private int actionItemCount;

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[10];
        int i = 0;
        columns[i] = new ColumnDefinitionConfig<ManualJournalListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final ManualJournalListItem item) {
                actionItemCount = 0;
                boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate()));
                MenuBar menuBar = new MenuBar(true);
                if (Utils.hasPermission(ACCOUNTING_MANUAL_JOURNAL_SUMMARY)) {
                    MenuPopItem transactionView = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    transactionView.setCommand(() -> {
                        if (NewManualTransaction.DRAFT.equals(item.getStatus())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("manual|edit/" + item.getObjectId(), item.getNumber());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("manual|summary/" + item.getObjectId(), item.getNumber());
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(transactionView);
                }
                if (!item.isUsed() && !hasAccountingBeforeBlockDate) {
                    boolean canEdit = Utils.hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_EDIT) && (!NewManualTransaction.APPROVED.equals(item.getStatus()) && !NewManualTransaction.POST.equals(item.getStatus()));
                    if (item.isSetupAP()) {
                        canEdit = Utils.hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_EDIT) &&
                                ((item.isApprover() && (Constants.SUBMITTED.equals(item.getStatus()) || NewManualTransaction.APPROVED.equals(item.getStatus()) || NewManualTransaction.POST.equals(item.getStatus()))) ||
                                        item.getCreatorId() != null && item.getCreatorId().equals(Utils.getUserID()) && (Constants.SUBMITTED.equals(item.getStatus()) || Constants.REJECTED.equals(item.getStatus())));
                    }

                    if (canEdit) {
                        MenuPopItem transactionEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                        transactionEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("manual|edit/" + item.getObjectId(), item.getNumber()));
                        actionItemCount++;
                        menuBar.addItem(transactionEdit);
                    }

                    if (Utils.hasPermission(ACCOUNTING_MANUAL_JOURNAL_DELETE)) {
                        MenuPopItem transactionDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                        transactionDelete.setCommand(() -> {
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            messageBox.setTitle(wfmStrings.warning());
                            messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                            messageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    manualEntryService.deleteManualJournal(item.getObjectId(), new AbstractAsyncCallback<Boolean>() {
                                        public void failure(Throwable caught) {
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        public void success(Boolean result) {
                                            if (result) {
                                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.manualJournals()), Info.Type.INFO);
                                                list.reloadPage();
                                            } else {
                                                Info.show(wfmStrings.errorOccuredWhileDeleting(), Info.Type.WARNING);
                                            }
                                        }
                                    });
                                }
                            });
                            messageBox.open();
                        });
                        actionItemCount++;
                        menuBar.addItem(transactionDelete);
                    }

                    if (Utils.hasPermission(ACCOUNTING_MANUAL_JOURNAL_VOID) && NewManualTransaction.POST.equals(item.getStatus())) {
                        MenuPopItem voidManualJournal = new MenuPopItem(accountingStrings.voide(), "icon-remove-storefront");
                        voidManualJournal.setCommand(() -> {
                            final WfmMessageBox confirmBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
                            confirmBox.setTitle(wfmStrings.confirmation());
                            confirmBox.setMessage(accountingStrings.areYouSureYouWantToVoidThe() + item.getNarration() + " ?");
                            confirmBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    final KpiModal dialogBox = new KpiModal();
                                    dialogBox.setWidth(400);
                                    final DatePicker datePicker = new DatePicker(item.getDate().getNonConvertedDate());
                                    dialogBox.setTitle(wfmStrings.selectVoidDate());
                                    dialogBox.add(datePicker);
                                    final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide(), WfmButton2.BTN_PRIMARY);
                                    dialogBox.addButton(voidButton);
                                    voidButton.addClickHandler(clickEvent -> {
                                        if (AccountingUtils.validateVoidDate(datePicker.getDate(), item.getDate().getNonConvertedDate())) {
                                            voidButton.setEnabled(false);
                                            manualEntryService.voidManualJournal(item.getObjectId(), new DateNonConvertable(datePicker.getDate()), new AbstractAsyncCallback<Boolean>() {
                                                public void failure(Throwable caught) {
                                                    dialogBox.close();
                                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                                }

                                                public void success(Boolean result) {
                                                    if (result) {
                                                        dialogBox.close();
                                                        Info.show(accountingStrings.infoMessage51(), Info.Type.INFO);
                                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, result, ManualEntryListView.this);
                                                    } else {
                                                        Info.show(wfmStrings.errorOccurredUpdate(), Info.Type.WARNING);
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    dialogBox.open();
                                }
                            });
                            confirmBox.open();
                        });
                        actionItemCount++;
                        menuBar.addItem(voidManualJournal);
                    }
                }

                MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                final HTMLPanel htmlPanel = new HTMLPanel("");

                generatePdf.setCommand(() -> new PDFTemplateSelector(AccountingConstants.MANUAL_ENTRY, new ExtendedCommand() {
                    @Override
                    public void execute(Integer id) {
                        generatePDF(htmlPanel, id, item.getObjectId());
                    }
                }));
                add(htmlPanel);
                actionItemCount++;
                menuBar.addItem(generatePdf);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();

            }
        };
        columns[i].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[i].setColumnSortable(false);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, SimpleLink>(wfmStrings.number(), ManualJournalListItem.NUMBER, 100) {

            @Override
            public SimpleLink getCellValue(ManualJournalListItem item) {
                SimpleLink label = new SimpleLink(item.getNumber() != null ? item.getNumber() : "");
                if (item.getNumber() != null) {
                    label.setStyleName("uploadLinkStyle2");
                    label.addClickHandler(event -> {
                        if (NewManualTransaction.DRAFT.equals(item.getStatus())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("manual|edit/" + item.getObjectId(), item.getNumber());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("manual|summary/" + item.getObjectId(), item.getNumber());
                        }
                    });
                }
                return label;

            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, Widget>(wfmStrings.narration(), ManualJournalListItem.NARRATION, 300) {

            @Override
            public Widget getCellValue(final ManualJournalListItem item) {
                Label label = new Label(item.getNarration() != null ? item.getNarration() : "");
                if (item.getNarration() != null) {
                    label.setStyleName("uploadLinkStyle2");
                    label.addClickHandler(event -> {
                        if (NewManualTransaction.DRAFT.equals(item.getStatus())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("manual|edit/" + item.getObjectId(), item.getNumber());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("manual|summary/" + item.getObjectId(), item.getNumber());
                        }
                    });
                }
                return label;
            }
        };
        columns[i].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(wfmStrings.date(), ManualJournalListItem.DATE, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return item.getDate() != null ? DateUtils.format(item.getDate()) : "";
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(wfmStrings.debit(), ManualJournalListItem.DEBIT, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return AccountingUtils.get().formatPrice(item.getDebit());
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[i].addStyleAttribute("padding-right", "5px");

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(wfmStrings.credit(), ManualJournalListItem.CRETID, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return AccountingUtils.get().formatPrice(item.getCredit());
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[i].addStyleAttribute("padding-right", "5px");

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(wfmStrings.status(), ManualJournalListItem.STATUS, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return getStatusName(item.getStatus());
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(wfmStrings.approver(), ManualJournalListItem.APPROVER, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return item.getCurrentApprover();
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(wfmStrings.reference(), ManualJournalListItem.REFERENCENUMBER, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return item.getReferenceNumber();
            }
        };
        columns[i].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(Property.get(Constants.PROJECT, wfmStrings.project()), ManualJournalListItem.PROJECT, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return item.getProject() != null ? item.getProject() : "";
            }
        };
        columns[i].setShow(false);
        columns[i].setColumnSortable(false);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(wfmStrings.createdBy(), ManualJournalListItem.CREATOR, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return item.getCreator() != null ? item.getCreator() : "";
            }
        };
        columns[i].setShow(false);

        columns[++i] = new ColumnDefinitionConfig<ManualJournalListItem, String>(wfmStrings.currency(), ManualJournalListItem.CURRENCY, 100) {

            @Override
            public String getCellValue(ManualJournalListItem item) {
                return item.getCurrency() != null ? item.getCurrency() : "";
            }
        };

        return columns;
    }

    private String getStatusName(String status) {
        if (status == null) {
            return wfmStrings.notAvailable();
        } else if (NewManualTransaction.DRAFT.equals(status)) {
            return wfmStrings.draft();
        } else if (NewManualTransaction.POST.equals(status)) {
            return wfmStrings.post();
        } else if (NewManualTransaction.REVERSED.equals(status)) {
            return accountingStrings.reversed();
        } else if (Constants.SUBMITTED.equals(status)) {
            return wfmStrings.submitted();
        } else if (Constants.APPROVED.equals(status)) {
            return wfmStrings.approved();
        } else if (Constants.REJECTED.equals(status)) {
            return wfmStrings.rejected();
        }
        return status;
    }

    public String getIconStyle() {
        return "accountMark manual-journals";//return "icon-accounting-bank-accounts";
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectID) {
        TransactionPDFObject requestObject = new TransactionPDFObject(objectID, pdfTemplateID);
        String pdfURL = CommandConstants.PDF_URL + "/manualJournalViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

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

    @Override
    public String getPropertyCode() {
        return Constants.MANUAL_TRANSACTIONS;
    }
}
