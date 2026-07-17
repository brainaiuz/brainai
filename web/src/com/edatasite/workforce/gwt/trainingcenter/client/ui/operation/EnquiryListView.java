package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.EnquiryService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.enquiry.EnquiryItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Normurod
 * Date: 7/16/12
 * Time: 9:00 PM
 */
public class EnquiryListView extends BaseListView implements TCConstants {
    public static TCStrings tcStrings = TCStrings.App.get();
    public static WfmStrings wfmStrings = WfmStrings.App.get();
    public static WfmMessages wfmMessages = WfmMessages.App.get();
	private int totalCount = 0;

    private ListingPanel<EnquiryItem> enquiryListPanel;

    public EnquiryListView() {
        super(TC_ENQUIRIES, tcStrings.enquiries());
    }

    public Widget onInitialize() {
        enquiryListPanel = new ListingPanel<>(ListPanelType.EnquiryListPanel, getColumnConfig(), getRequestProvider(), getPanelDesign());
		enquiryListPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadEnquiryListExcel";
            ListingFilterParameter filterParametrs = enquiryListPanel.getFilterParametrs();
            enquiryListPanel.callListExcel(excelURL, filterParametrs);
        });
		enquiryListPanel.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/enquiryListPDFHandler";
            ListingFilterParameter filterParametrs = enquiryListPanel.getFilterParametrs();
            enquiryListPanel.callListPDF(pdfURL, filterParametrs);
        });
        add(enquiryListPanel);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ENQUIRY_ADD_EDIT, EnquiryListView.this, (sender, args) -> enquiryListPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ENQUIRY_DELETED, EnquiryListView.this, (sender, args) -> enquiryListPanel.reloadPage());
        return null;
    }

    private ListingPanelDesign getPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addButton = getAddNewButton();
                addButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("enquires|addenquires/add"));
                return addButton;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(tcStrings.noEnquiryListMessage());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<EnquiryItem> getRequestProvider() {
        return (filterParametrs, enquiryItemCallback) -> EnquiryService.App.get().geEnquiryList(filterParametrs,new AsyncCallback<ListResult<EnquiryItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                enquiryItemCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<EnquiryItem> result) {
                totalCount = result.getTotal();
                enquiryItemCallback.onSuccess(result);
            }
        });
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[10];
        columnConfigs[0] = new ColumnDefinitionConfig<EnquiryItem,Widget>(wfmStrings.action(), EnquiryItem.ENQUIRY_ACTION, 40) {
            @Override
            public Widget getCellValue(final EnquiryItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                // Enquiry Summary View
                MenuPopItem enquirySummaryView = new MenuPopItem(tcStrings.enquirySummary(),"", () -> SinksContainerFactory.entryPoint.onHistoryChanged("enquires|viewenquires/"+rowValue.getObjectID()));
                menuBar.addItem(enquirySummaryView);

                // Enquiry Edit View
                MenuPopItem enquiryEditView = new MenuPopItem(tcStrings.enquiryEdit(), "", () -> SinksContainerFactory.entryPoint.onHistoryChanged("enquires|editenquires/"+rowValue.getObjectID()));
                menuBar.addItem(enquiryEditView);

                // Enquiry Delete Action
                MenuPopItem enquiryDeleteView = new MenuPopItem(wfmStrings.delete(), "", () -> {
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    wfmMessageBox.setTitle(wfmStrings.confirmation());
                    wfmMessageBox.setMessage(wfmMessages.sureYouWantToDelete(tcStrings.enquiryOnly().toLowerCase(), "?"));
                    wfmMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            EnquiryService.App.get().deleteEnquiry(rowValue.getObjectID(),new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmMessages.yourSomethingHasBeenDeleted(tcStrings.enquiryOnly().toLowerCase()));
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ENQUIRY_DELETED, null, EnquiryListView.this);
                                }
                            });
                        }
                    });
                    wfmMessageBox.open();
                });
                menuBar.addItem(enquiryDeleteView);


                ToolItem toolItem = new ToolItem(2);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfigs[0].setMinimumColumnWidth(40);
        columnConfigs[0].setMaximumColumnWidth(40);
        columnConfigs[0].setColumnSortable(false);
        columnConfigs[0].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfigs[1] = new ColumnDefinitionConfig<EnquiryItem, String>(wfmStrings.number(), EnquiryItem.ENQUIRY_NUMBER, 80) {
            @Override
            public String getCellValue(EnquiryItem rowValue) {
                return rowValue.getNumberData() != null ? rowValue.getNumberData().getNumberString() : "";
            }
        };
        columnConfigs[1].setMinimumColumnWidth(60);
        columnConfigs[1].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columnConfigs[2] = new ColumnDefinitionConfig<EnquiryItem, SimpleLink>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), EnquiryItem.ENQUIRY_CUSTOMER, 150) {
            @Override
            public SimpleLink getCellValue(EnquiryItem rowValue) {
                return new SimpleLink(rowValue.getCustomer().getName(), "enquires|viewenquires/" + rowValue.getObjectID());
            }
        };
        columnConfigs[2].setMinimumColumnWidth(130);


        columnConfigs[3] = new ColumnDefinitionConfig<EnquiryItem, String>(tcStrings.enquiryDate(), EnquiryItem.ENQUIRY_DATE, 100) {
            @Override
            public String getCellValue(EnquiryItem rowValue) {
                return DateUtils.format(rowValue.getEnquiryDate());
            }
        };
        columnConfigs[3].setMinimumColumnWidth(60);

        columnConfigs[4] = new ColumnDefinitionConfig<EnquiryItem, String>(tcStrings.enquiryMode(), EnquiryItem.ENQUIRY_MODE, 100) {
            @Override
            public String getCellValue(EnquiryItem rowValue) {
                return rowValue.getEnquiryMode().getName();
            }
        };
        columnConfigs[4].setMinimumColumnWidth(80);

        columnConfigs[5] = new ColumnDefinitionConfig<EnquiryItem, String>(wfmStrings.currency(), EnquiryItem.CUSTOMER_CURRENCY, 80) {
            @Override
            public String getCellValue(EnquiryItem rowValue) {
                return rowValue.getCurrency().getName();
            }
        };
        columnConfigs[5].setMinimumColumnWidth(60);
        columnConfigs[5].setColumnSortable(false);

        columnConfigs[6] = new ColumnDefinitionConfig<EnquiryItem, String>(tcStrings.refInfo(), EnquiryItem.REF_INFO, 120) {
            @Override
            public String getCellValue(EnquiryItem rowValue) {
                return rowValue.getRefInfo();
            }
        };
        columnConfigs[6].setMinimumColumnWidth(120);
        columnConfigs[6].setShow(false);

        columnConfigs[7] = new ColumnDefinitionConfig<EnquiryItem, String>(Property.get(Constants.Contacts, wfmStrings.contact()), EnquiryItem.CONTACT_NAME, 120) {
            @Override
            public String getCellValue(EnquiryItem rowValue) {
                return rowValue.getContactDetails().getName();
            }
        };
        columnConfigs[7].setMinimumColumnWidth(80);
        columnConfigs[7].setColumnSortable(false);
        columnConfigs[7].setShow(false);

        columnConfigs[8] = new ColumnDefinitionConfig<EnquiryItem, String>(Property.get(Constants.Contacts, wfmStrings.email(), wfmStrings.contact()), EnquiryItem.CONTACT_EMAIL, 120) {
            @Override
            public String getCellValue(EnquiryItem rowValue) {
                return rowValue.getContactDetails().getPrimaryEmail();
            }
        };
        columnConfigs[8].setMinimumColumnWidth(80);
        columnConfigs[8].setShow(false);

        columnConfigs[9] = new ColumnDefinitionConfig<EnquiryItem, HTML>(wfmStrings.phone(), EnquiryItem.CONTACT_PHONE, 100) {
            @Override
            public HTML getCellValue(EnquiryItem rowValue) {
                return new HTML(rowValue.getContactDetails().getPrimaryPhone());
            }
        };
        columnConfigs[9].setMinimumColumnWidth(80);
        columnConfigs[9].setColumnSortable(false);
        columnConfigs[9].setShow(false);

        return columnConfigs;
    }

    @Override
    public String getIconStyle() {
        return "bgMark enquiry-icon";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}