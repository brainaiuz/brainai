package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/16/12
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheckListView extends BaseListView {

    private ListingPanel<BankCheckData> list;

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();


    public CheckListView() {
        super("checkList");
        setDescription(property.getPlural(accountingStrings.writeChecks()));
        setAddNew("check|add/add");
    }

    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.CheckListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());


        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadBankCheckListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            setFilterValues(filterParametrs);
            list.callListExcel(excelURL, filterParametrs);
        });
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/bankCheckListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            setFilterValues(filterParametrs);
            list.callListPDF(pdfURL, filterParametrs);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_CHECK_SAVED, CheckListView.this, (sender, args) -> list.reloadPage());

//        list.setQuickViewPanel(viewPanel);
//        super.setQuickViewPanel(viewPanel);
//        super.setListingPanel(list);
//        super.display();
        add(list);
        return null;
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return CheckListView.this::addNewItem;
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
                        fields.add(ListingChooseFilter.FROM_AMOUNT);
                        fields.add(ListingChooseFilter.TO_AMOUNT);
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        fields.add(ListingChooseFilter.STATUS);
                        fields.add(ListingChooseFilter.CREATOR);
                        fields.add(ListingChooseFilter.RELATED_PROJECT);
                        fields.add(ListingChooseFilter.BANK_NAME);
                        return fields;
                    }
                    @Override
                    public ViewName getView(){
                        return ViewName.CheckList;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.ensureDebugId("checkListView" + "addNewButton");
                addNew.addClickHandler(clickEvent -> addNewItem());
                return addNew;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouDoNotHaveAnyCheck());
                message.setTextBeforeLink(accountingStrings.noCheckLick());
                message.setHref("check|add/add");
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private SinksContainer addNewItem() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("check|add/add");
    }

    private void setFilterValues(ListingFilterParameter filterParametrs) {
        if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
            DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(filterParametrs.getStartDate()));
            DateNonConvertable toDate = new DateNonConvertable(DateUtil.getDayLastTime(filterParametrs.getEndDate()));
            filterParametrs.setStartDateWithoutOffset(fromDate.getNonConvertedDate());
            filterParametrs.setEndDateWithoutOffset(toDate.getNonConvertedDate());
        }
    }

    private ListingRequestProvider<BankCheckData> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            setFilterValues(filterParametrs);
            AccountingService.App.get().getBankCheckList(filterParametrs, new AbstractAsyncCallback<ListResult<BankCheckData>>() {
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void success(ListResult<BankCheckData> list) {
                    callback.onSuccess(list);
                }
            });
        };
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[11];
        columns[0] = new ColumnDefinitionConfig<BankCheckData, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final BankCheckData item) {
                boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate()));

                MenuBar menuBar = new MenuBar(true);
                MenuPopItem viewLink = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                viewLink.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("check|summary/" + item.getObjectID(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getPayTo()));
                menuBar.addItem(viewLink);

                if (item.isEditable() && !hasAccountingBeforeBlockDate) {
                    MenuPopItem editLink = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editLink.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("check|edit/" + item.getObjectID(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getPayTo()));
                    menuBar.addItem(editLink);


                }
                if (!hasAccountingBeforeBlockDate) {
                    MenuPopItem deleteLink = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteLink.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                AccountingService.App.get().deleteBankCheckData(item.getObjectID(), new AbstractAsyncCallback<ArrayList<String>>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(ArrayList<String> result) {
                                        if (result != null) {
                                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                                            messageBox.setTitle(wfmStrings.confirmation());
                                            StringBuilder stringBuffer = new StringBuilder();
                                            stringBuffer.append("this bank check is related to the following ");
                                            for (int i = 0; i < result.size(); i++) {
                                                if (i == result.size() - 1) {
                                                    stringBuffer.append(result.get(i));
                                                } else {
                                                    stringBuffer.append(result.get(i) + ",");
                                                }
                                            }
                                            stringBuffer.append(" invoices. Please delete the first following invoices");
                                            messageBox.setMessage(stringBuffer.toString());
                                            messageBox.open();
                                        } else {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.bankCheck()), Info.Type.INFO);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_CHECK_DELETED, result, CheckListView.this);
                                            list.reloadPage();
                                        }
                                    }
                                });

                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(deleteLink);
                }
                MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                final HTMLPanel htmlPanel = new HTMLPanel("");
                generatePdf.setCommand(() -> new PDFTemplateSelector(AccountingConstants.BANK_CHECK, new ExtendedCommand() {
                    @Override
                    public void execute(Integer id) {
                        generatePDF(htmlPanel, id, item.getObjectID());
                    }
                }));
                add(htmlPanel);
                menuBar.addItem(generatePdf);

                final ToolItem toolItem = new ToolItem(3);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();

            }
        };
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<BankCheckData, Widget>(wfmStrings.number(), BankCheckData.NUMBER, 150) {

            @Override
            public Widget getCellValue(final BankCheckData item) {
                Span label = new Span(item.getNumberData() != null ? item.getNumberData().getNumberString() : "");
                if (item.getNumberData() != null) {
                    label.setStyleName("uploadLinkStyle2");
                    label.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("check|summary/" + item.getObjectID(), item.getNumberData().getNumberString()));
                }
                return label;
            }
        };
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[2] = new ColumnDefinitionConfig<BankCheckData, String>(accountingStrings.bank(), BankCheckData.BANK_ACCOUNT, 150) {

            @Override
            public String getCellValue(BankCheckData item) {
                return item.getBankAccount().getName();
            }
        };
        columns[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[3] = new ColumnDefinitionConfig<BankCheckData, String>(wfmStrings.payTo(), BankCheckData.PAY_TO, 150) {

            @Override
            public String getCellValue(BankCheckData item) {
                return item.getPayTo();
            }
        };
        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[4] = new ColumnDefinitionConfig<BankCheckData, String>(wfmStrings.date(), BankCheckData.DATE, 150) {

            @Override
            public String getCellValue(BankCheckData item) {
                return DateUtils.format(item.getDate());
            }
        };
        columns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[5] = new ColumnDefinitionConfig<BankCheckData, String>(wfmStrings.amount(), BankCheckData.AMOUNT, 150) {

            @Override
            public String getCellValue(BankCheckData item) {
                return AccountingUtils.get().formatPrice(item.getAmount());
            }
        };
        columns[5].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        columns[6] = new ColumnDefinitionConfig<BankCheckData, String>(wfmStrings.address(), BankCheckData.ADDRESS, 150) {

            @Override
            public String getCellValue(BankCheckData item) {
                return item.getAddress();
            }
        };
        columns[6].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[7] = new ColumnDefinitionConfig<BankCheckData, String>(wfmStrings.description(), BankCheckData.MEMO, 150) {

            @Override
            public String getCellValue(BankCheckData item) {
                return item.getMemo();
            }
        };
        columns[7].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[8] = new ColumnDefinitionConfig<BankCheckData, String>(wfmStrings.status(), BankCheckData.STATUS, 150) {

            @Override
            public String getCellValue(BankCheckData item) {
                return item.isPostDatedTransaction() ? wfmStrings.postDated() : wfmStrings.posted();
            }
        };
        columns[8].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[9] = new ColumnDefinitionConfig<BankCheckData, String>(wfmStrings.createdBy(), BankCheckData.CREATOR, 100) {

            @Override
            public String getCellValue(BankCheckData item) {
                return item.getCreator() != null ? item.getCreator() : "";
            }
        };
        columns[9].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columns[9].setShow(false);

        columns[10] = new ColumnDefinitionConfig<BankCheckData, String>(Property.get(Constants.PROJECT, wfmStrings.project()), BankCheckData.PROJECT, 100) {

            @Override
            public String getCellValue(BankCheckData item) {
                return item.getProject() != null ? item.getProject().getName() : "";
            }
        };
        columns[10].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        columns[10].setShow(false);


        return columns;
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectID) {
        TransactionPDFObject requestObject = new TransactionPDFObject(objectID, pdfTemplateID);
        String pdfURL = CommandConstants.PDF_URL + "/checkPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    @Override
    public String getIconStyle() {
        return "accountMark account-Check";
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
        return AccountingConstants.CHECK_LIST;
    }
}
