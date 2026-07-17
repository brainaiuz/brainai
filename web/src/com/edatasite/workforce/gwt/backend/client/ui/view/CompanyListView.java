package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.notifications.Info.show;

public class CompanyListView extends BaseListView implements Constants {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final BackendServiceAsync backendService = BackendService.App.get();
    private ListingPanel<CompanyListItem> listingTable;
    private final boolean hasAcccesToAction;

    public CompanyListView(boolean changeAction) {
        super("backendview", backendStrings.backendListView());
        hasAcccesToAction = changeAction;
    }

    public String getIconStyle() {
        return "backend backendListView";
    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.BackendViewPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig column;
        if (hasAcccesToAction) {
            column = new ColumnDefinitionConfig<CompanyListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

                @Override
                public Anchor getCellValue(final CompanyListItem companyListItem) {
                    return getActionMenuItem(companyListItem);
                }
            };
            column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
            column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
            column.setColumnSortable(false);
            columns.add(column);
        }
        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.companyID(), CompanyListItem.COMPANY_ID, 55) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getCompanyID() != null ? companyListItem.getCompanyID().toString() : "";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, SimpleLink>(wfmStrings.companyName(), CompanyListItem.COMPANY_NAME, 100) {
            @Override
            public SimpleLink getCellValue(CompanyListItem companyListItem) {
                return getLink(companyListItem.getCompanyName() != null ? companyListItem.getCompanyName() : "", "backendView|summary/" + companyListItem.getCompanyID());
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(wfmStrings.organizationType(), CompanyListItem.ORGANIZATION_TYPE, 100) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getOrgType() != null ? companyListItem.getOrgType() : "";
            }
        };
        column.setShow(false);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(wfmStrings.status(), CompanyListItem.COMPANY_STATUS, 100) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                String status;
                if (companyListItem.isActive()) {
                    status = wfmStrings.active();
                } else {
                    status = wfmStrings.inactive();
                }
                return status;
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(wfmStrings.registeredDate(), CompanyListItem.REGISTRATION_DATE, 65) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return DateUtils.preiewFormat(companyListItem.getRegistrationDate());
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.expirationDate(), CompanyListItem.EXPIRATION_DATE, 68) {

            @Override
            public String getCellValue(CompanyListItem rowValue) {
                return rowValue.getUsagPlanEndDate() != null ? DateUtils.preiewFormat(rowValue.getUsagPlanEndDate()) : " ";
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.firstAccessDate(), CompanyListItem.FIRST_ACCESS_DATE, 68) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getFirstAccessDate() != null ? DateUtils.preiewFormat(companyListItem.getFirstAccessDate()) : " ";
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.lastAccessDate(), CompanyListItem.LAST_ACCESS_DATE, 68) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getLastAccessDate() != null ? DateUtils.preiewFormat(companyListItem.getLastAccessDate()) : " ";
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.countAccess(), CompanyListItem.ACCESS_COUNT, 33) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getAccessCount();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(wfmStrings.isPaid(), CompanyListItem.SUBSCRIPTION_TYPE, 33) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                String usagePlan = null;
                if (companyListItem.getUsagePlanPaymentType() != null) {
                    if ("t".equals(companyListItem.getUsagePlanPaymentType())) {
                        usagePlan = wfmStrings.yes();
                    } else {
                        usagePlan = wfmStrings.no();
                    }
                }
                return usagePlan;
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(wfmStrings.country(), CompanyListItem.COUNTRY, 33) {

            @Override
            public String getCellValue(CompanyListItem rowValue) {
                return rowValue.getCountry();
            }
        };
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.adminEmail(), CompanyListItem.ADMIN_EMAIL, 33) {

            @Override
            public String getCellValue(CompanyListItem rowValue) {
                return rowValue.getAdminEmail();
            }
        };
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.adminName(), CompanyListItem.ADMIN_NAME, 38) {

            @Override
            public String getCellValue(CompanyListItem rowValue) {
                return rowValue.getAdminName() != null ? rowValue.getAdminName() : "N/A";
            }
        };
        column.setShow(false);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.countActiveUsersSubs(), CompanyListItem.ACTIVE_USERS_COUNT_SUBS, 33) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getPlannedActiveUsers();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.countActiveUsers(), CompanyListItem.ACTIVE_USERS_COUNT, 33) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getActiveUsersCount();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.countESSUserSubs(), CompanyListItem.ESS_USERS_COUNT_SUBS, 33) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getPlannedEssUsers();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, Integer>(backendStrings.countESSUserActual(), CompanyListItem.ESS_USERS_COUNT_ACTUAL, 33) {
            @Override
            public Integer getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getEssUsersCount();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, Integer>(backendStrings.noAccessUSerCountSubs(), CompanyListItem.NO_ACCESS_USER_COUNT_SUBS, 41) {
            @Override
            public Integer getCellValue(CompanyListItem rowValue) {
                return rowValue.getPlannedNoAccessUsers();
            }
        };
        column.setColumnSortable(true);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, Integer>(backendStrings.noAccessUSerCount(), CompanyListItem.NO_ACCESS_USER_COUNT, 41) {
            @Override
            public Integer getCellValue(CompanyListItem rowValue) {
                return rowValue.getNoAccessUserCount();
            }
        };
        column.setColumnSortable(true);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.paymentStatus(), CompanyListItem.PAYMENT_STATUS, 33) {
            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getUsagePlanPaymentStatus();
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, Long>(backendStrings.periodAccess(), CompanyListItem.PERIOD_ACCESS, 33) {
            @Override
            public Long getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getPeriodAccess();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.hostName(), CompanyListItem.HOST_NAME, 33) {

            @Override
            public String getCellValue(CompanyListItem rowValue) {
                return rowValue.getHostName() != null ? rowValue.getHostName() : "N/A";
            }
        };
        column.setShow(false);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<CompanyListItem, String>(backendStrings.adminPhone(), CompanyListItem.ADMIN_PHONE, 33) {

            @Override
            public String getCellValue(CompanyListItem rowValue) {
                return rowValue.getAdminPhone() != null ? rowValue.getAdminPhone() : "N/A";
            }
        };
        column.setShow(false);
        column.setColumnSortable(false);
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[columns.size()]);
    }

    private Anchor getActionMenuItem(final CompanyListItem companyListItem) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);
        MenuPopItem backendViewSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-solution-small");
        backendViewSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("backendView|summary/" + companyListItem.getCompanyID()));
        actionItemCount++;
        menuBar.addItem(backendViewSummary);

        MenuPopItem resendUsersActivationLinks = new MenuPopItem(backendStrings.resendUsersActivationLinks(), "icon-solution-small");
        resendUsersActivationLinks.setCommand(() -> {
            try {
                backendService.resendEmployeesActivationLink(companyListItem.getCompanyID(), new AsyncCallback<Void>() {
                    public void onFailure(Throwable arg0) {
                        show(backendStrings.resendEmployeesActivationLinkHasFailed(), Info.Type.WARNING);
                    }

                    public void onSuccess(Void arg0) {
                        show(backendStrings.resendEmployeesActivationLinkHasDoneSuccessfully(), Info.Type.WARNING);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        actionItemCount++;
        menuBar.addItem(resendUsersActivationLinks);

        MenuPopItem getActivationLink = new MenuPopItem(backendStrings.getActivationLink(), "icon-solution-small");
        getActivationLink.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("activationLink|activationLink/" + companyListItem.getCompanyID()));
        actionItemCount++;
        menuBar.addItem(getActivationLink);

        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com")) {
            if (companyListItem.getActivated()) {
                MenuPopItem resendCompanyActivationLink = new MenuPopItem(backendStrings.resendCompanyActivationLink(), "icon-solution-small");
                resendCompanyActivationLink.setCommand(() -> {
                    try {
                        backendService.resendActivationLink(companyListItem.getCompanyID(), new AsyncCallback<Void>() {

                            public void onFailure(Throwable arg0) {
                                show(backendStrings.resendCompanyActivationLinkHasFailed() + "...", Info.Type.WARNING);
                            }

                            public void onSuccess(Void arg0) {
                                show(backendStrings.resendCompanyActivationLinkHasDoneSuccessfully() + "...", Info.Type.INFO);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                actionItemCount++;
                menuBar.addItem(resendCompanyActivationLink);
            }

//            MenuPopItem convertToMarketplace = new MenuPopItem(backendStrings.convertToMarketplace(), "icon-solution-small");
//            convertToMarketplace.setCommand(() -> {
//                final KpiModal db = new KpiModal();
//                db.setCloseButton(true);
//                final TextBox domainName = new TextBox();
//                domainName.setVisibleLength(55);
//                db.setSize("250px", "130px");
//                db.setTitle(backendStrings.enterGoogleAppsDomain());
//                db.setText(backendStrings.pleaseEnterGoogleAppsDomainName());
//                Button set = new Button(wfmStrings.save());
//                Button cancel = new Button(wfmStrings.cancel());
//                set.addClickHandler(clickEvent -> backendService.convertMarketplace(companyListItem.getCompanyID(), domainName.getText(), new AsyncCallback<Void>() {
//
//                    public void onFailure(Throwable arg0) {
//                        show("", wfmStrings.failed(), Info.Type.WARNING);
//                    }
//
//                    public void onSuccess(Void arg0) {
//                        show("", backendStrings.conversionSucceeded(), Info.Type.INFO);
//                        db.close();
//                    }
//                }));
//                cancel.addClickHandler(clickEvent -> db.close());
//
//                db.add(domainName);
//                db.addButton(set);
//                db.addButton(cancel);
//
//                backendService.getCompanyDomain(companyListItem.getCompanyID(), new AsyncCallback<String>() {
//
//                    public void onFailure(Throwable arg0) {
//                        db.open();
//                    }
//
//                    public void onSuccess(String arg0) {
//                        db.open();
//                        domainName.setText(arg0);
//
//                    }
//                });
//            });
//            actionItemCount++;
//            menuBar.addItem(convertToMarketplace);

//            MenuPopItem disableAccount = new MenuPopItem(backendStrings.disableAccount(), "icon-solution-small");
//            disableAccount.setCommand(() -> backendService.disableAccount(companyListItem.getCompanyID(), new AsyncCallback<Void>() {
//
//                public void onFailure(Throwable arg0) {
//                    show(wfmStrings.failed() + "...", Info.Type.WARNING);
//                }
//
//                public void onSuccess(Void arg0) {
//                    show("", backendStrings.disabledSuccessfully(), Info.Type.INFO);
//                }
//            }));
//            actionItemCount++;
//            menuBar.addItem(disableAccount);

//            MenuPopItem updateCompany = new MenuPopItem(backendStrings.updateCompany(), "icon-solution-small");
//            updateCompany.setCommand(() -> backendService.updateCompany(companyListItem.getCompanyID(), new AsyncCallback<Void>() {
//
//                public void onFailure(Throwable arg0) {
//                    show("", backendStrings.updateFailed(), Info.Type.WARNING);
//                }
//
//                public void onSuccess(Void arg0) {
//                    show(backendStrings.updatedSuccessfully(), Info.Type.INFO);
//                    listingTable.reloadPage();
//                }
//            }));
//            actionItemCount++;
//            menuBar.addItem(updateCompany);

//            MenuPopItem markAsTest = new MenuPopItem(backendStrings.markAsTest(), "icon-remove");
//            markAsTest.setCommand(() -> {
//                final WfmMessageBox messageBox = new WfmMessageBox(Icon.QUESTION, Action.YesNo, true);
//                messageBox.setText(wfmStrings.confirmation());
//                messageBox.setMessage(backendStrings.areYouSureWantToMarkAsTestCompanyThisCompany());
//                messageBox.setSize(380, 10);
//                messageBox.open();
//                messageBox.addCloseHandler(new CloseHandler() {
//                    @Override
//                    public void onSubmit() {
//                        backendService.markasTestCompany(companyListItem.getCompanyID(), new AsyncCallback<Void>() {
//                            public void onFailure(Throwable arg0) {
//                                show("", backendMessages.couldNotMarkCOMPANYNAMEAsTestCompany(companyListItem.getCompanyName()), Info.Type.WARNING);
//                            }
//
//                            public void onSuccess(Void arg0) {
//                                show("", backendMessages.COMPANYNAMEHasBeenMarkedAsTestCompany(companyListItem.getCompanyName()), Info.Type.INFO);
//                                listingTable.reloadPage();
//                            }
//                        });
//                    }
//                });
//            });
//            actionItemCount++;
//            menuBar.addItem(markAsTest);
        }

        final ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private ListingPanelDesign getListingPanelDesign() {
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
                        ArrayList<String> fields = new ArrayList<>(7);
                        fields.add(ListingChooseFilter.COUNTRY);
                        fields.add(ListingChooseFilter.COMPANY_STATUS);
                        fields.add(ListingChooseFilter.FROM_REGISTRATION_DATE);
                        fields.add(ListingChooseFilter.TO_REGISTRATION_DATE);
                        fields.add(ListingChooseFilter.FROM_EXPIRATION_DATE);
                        fields.add(ListingChooseFilter.TO_EXPIRATION_DATE);
                        fields.add(ListingChooseFilter.SUBSCRIPTION_TYPE);
                        return fields;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                String promotionalCode = Utils.userSettings.get(PROMOTIONAL_CODE);
                if (Utils.isNullOrEmpty(promotionalCode) || "null".equals(promotionalCode)) {
                    ActionButton addNew = new ActionButton(backendStrings.updateAll(), "btn btn--new", ActionButton.Type.BUTTON);
                    addNew.addClickHandler(clickEvent -> {
                        LoadingPanel.loading(true);
                        backendService.updateCompaniesStatistic(new AsyncCallback<Integer>() {
                            public void onFailure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                show(backendStrings.updationFailed(), Info.Type.WARNING);
                            }

                            public void onSuccess(Integer num) {
                                LoadingPanel.loading(false);
                                if (-1 == num) {
                                    show(backendStrings.otherUserMayBeUpdatingPleaseTryAfterSomeTime(), Info.Type.INFO);
                                } else {
                                    show(backendStrings.companiesUpdated(), Info.Type.INFO);
                                    listingTable.reloadPage();
                                }
                            }
                        });
                    });
                    return addNew;
                } else {
                    return null;
                }
            }

            @Override
            public ActionButton initTopToolBarMore() {
                ActionButton more = new ActionButton(backendStrings.optionalExport(), "", ActionButton.Type.BUTTON);
                more.ensureDebugId("company_list_action_button_id");
                more.addClickHandler(clickEvent -> showOptionalExportShell());
                return more;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                FlowPanel toolPanel = new FlowPanel();

                MaterialLink excel = new MaterialLink();
                MaterialIcon excelIcon = new MaterialIcon();
                excelIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
                excel.add(excelIcon);
                excel.setText(wfmStrings.excel());
                excel.addClickHandler(event -> {
                    String excelURL = CommandConstants.COMMON_URL + "/downloadBackendViewListExcel";
                    ListingFilterParameter listingFilterParameter = listingTable.getFilterParametrs();
                    if (listingFilterParameter == null) {
                        listingFilterParameter = new ListingFilterParameter();
                    }
                    listingFilterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(listingFilterParameter.getFacetFilter()));
                    listingFilterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(listingFilterParameter.getListPanelTool()));
                    listingFilterParameter.setCountryCode(listingFilterParameter.getCountryCode());
                    listingFilterParameter.setStatusCode(listingFilterParameter.getStatusCode());
                    listingFilterParameter.setFromRegistrationDate(listingFilterParameter.getFromRegistrationDate());
                    listingFilterParameter.setToRegistrationDate(listingFilterParameter.getToRegistrationDate());
                    listingFilterParameter.setFromExpirationDate(listingFilterParameter.getFromExpirationDate());
                    listingFilterParameter.setToExpirationDate(listingFilterParameter.getToExpirationDate());
                    listingFilterParameter.setPaid(listingFilterParameter.isPaid());
                    HashMap<String, String> parametrs = listingFilterParameter.getRequestParams();
                    Utils.sendPDFOrExcelRequest(toolPanel, excelURL, parametrs, "_blank");
                });
                menuContainer.add(excel);

                MaterialLink exportToCSV = new MaterialLink();
                MaterialIcon csvIcon = new MaterialIcon();
                csvIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
                exportToCSV.add(csvIcon);
                exportToCSV.setText("Export Contacts to Spreadsheet");
                exportToCSV.addClickHandler(event -> backendService.stealContacts("https://docs.google.com/spreadsheets/d/19IUbr-PZCvfZAltM4W69Gs4fXnyh8rc1fLAVsMWLazM/edit?usp=sharing", new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        show(wfmStrings.inProgress(), Info.Type.INFO);
                    }
                }));
                menuContainer.add(exportToCSV);

                exportOption.initExport(toolPanel, false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage emptyMessage = new DefaultNoItemsMessage(backendStrings.currentlyThereAreNoAnyItems());
                emptyDataTable.initEmptyDataTable(emptyMessage);
            }
        };
    }

    private ListingRequestProvider<CompanyListItem> getListingRequestProvider() {
        return (filterParameter, callback) -> backendService.getCompanyStatisticList(filterParameter, new AsyncCallback<ListResult<CompanyListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<CompanyListItem> result) {
                LoadingPanel.loading(false);
                callback.onSuccess(result);
            }
        });
    }

    private String getOptionalLabelWORD(String word) {
        return "&nbsp;&nbsp;" + word;
    }

    private void showOptionalExportShell() {
        FlexTable flexTable = new FlexTable();
        flexTable.setCellPadding(9);
        flexTable.setCellSpacing(9);
        int column = 0;
        int row = 0;

        final KpiCheckBox companyID = new KpiCheckBox(getOptionalLabelWORD(backendStrings.companyID()), true);
        final KpiCheckBox companyName = new KpiCheckBox(getOptionalLabelWORD(wfmStrings.companyName()), true);
        final KpiCheckBox registrationDate = new KpiCheckBox(getOptionalLabelWORD(wfmStrings.registeredDate()), true);
        final KpiCheckBox firstAccessDate = new KpiCheckBox(getOptionalLabelWORD(backendStrings.firstAccessDate()), true);
        final KpiCheckBox lastAccessDate = new KpiCheckBox(getOptionalLabelWORD(backendStrings.lastAccessDate()), true);
        final KpiCheckBox expirationDate = new KpiCheckBox(getOptionalLabelWORD(backendStrings.expirationDate()), true);

        final KpiCheckBox accessCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countAccess()), true);
        final KpiCheckBox subscriptionType = new KpiCheckBox(getOptionalLabelWORD(wfmStrings.isPaid()), true);
        final KpiCheckBox paymentStatus = new KpiCheckBox(getOptionalLabelWORD(backendStrings.paymentStatus()), true);
        final KpiCheckBox periodAccess = new KpiCheckBox(getOptionalLabelWORD(backendStrings.periodAccess()), true);
        final KpiCheckBox userCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countUser()), true);
        final KpiCheckBox activeUsersCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countActiveUsers()), true);
        final KpiCheckBox signUpFrom = new KpiCheckBox(getOptionalLabelWORD(backendStrings.signUpFrom()), true);
        final KpiCheckBox projectCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countProduct()), true);
        final KpiCheckBox taskCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countTask()), true);
        final KpiCheckBox timeSheetCount = new KpiCheckBox(getOptionalLabelWORD(Property.get(Constants.TIMESHEET, backendStrings.countTimeSheet(), wfmStrings.timesheet())), true);
        final KpiCheckBox clientCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countClient()), true);
        final KpiCheckBox supplierCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countSupplier()), true);
        final KpiCheckBox leadCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countLead()), true);
        final KpiCheckBox contactCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countContact()), true);
        final KpiCheckBox crmTaskCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countCRMTask()), true);
        final KpiCheckBox eventCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countEvent()), true);
        final KpiCheckBox caseCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countCase()), true);
        final KpiCheckBox invoiceCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countInvoice()), true);
        final KpiCheckBox expenseCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countExpense()), true);
        final KpiCheckBox productCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countProduct()), true);
        final KpiCheckBox folderCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countFolder()), true);
        final KpiCheckBox fileCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countFile()), true);
        final KpiCheckBox assessmentCount = new KpiCheckBox(getOptionalLabelWORD(backendStrings.countAssessment()), true);
        final KpiCheckBox adminEmail = new KpiCheckBox(getOptionalLabelWORD(backendStrings.adminEmail()), true);
        final KpiCheckBox country = new KpiCheckBox(getOptionalLabelWORD(wfmStrings.country()), true);
        final KpiCheckBox phone = new KpiCheckBox(getOptionalLabelWORD(wfmStrings.phone()), true);
        final KpiCheckBox hostName = new KpiCheckBox(getOptionalLabelWORD(backendStrings.hostName()), true);
        final KpiCheckBox adminName = new KpiCheckBox(getOptionalLabelWORD(backendStrings.adminName()), true);
        final KpiCheckBox affiliate = new KpiCheckBox(getOptionalLabelWORD(backendStrings.affiliate()), true);
        final KpiCheckBox compaing = new KpiCheckBox(getOptionalLabelWORD(wfmStrings.campaign()), true);
        final KpiCheckBox source = new KpiCheckBox(getOptionalLabelWORD(wfmStrings.source()), true);
        final KpiCheckBox noAccessUserCount = new KpiCheckBox(getOptionalLabelWORD("No Access User Count"), true);

        flexTable.setWidget(row, column, companyID);
        column++;
        flexTable.setWidget(row, column, companyName);
        column++;
        flexTable.setWidget(row, column, registrationDate);
        column++;
        flexTable.setWidget(row, column, firstAccessDate);

        row++;
        column = 0;

        flexTable.setWidget(row, column, lastAccessDate);
        column++;
        flexTable.setWidget(row, column, expirationDate);
        column++;
        flexTable.setWidget(row, column, accessCount);
        column++;
        flexTable.setWidget(row, column, subscriptionType);


        row++;
        column = 0;
        flexTable.setWidget(row, column, paymentStatus);
        column++;
        flexTable.setWidget(row, column, periodAccess);
        column++;
        flexTable.setWidget(row, column, userCount);
        column++;
        flexTable.setWidget(row, column, activeUsersCount);

        row++;
        column = 0;

        flexTable.setWidget(row, column, signUpFrom);
        column++;
        flexTable.setWidget(row, column, projectCount);
        column++;
        flexTable.setWidget(row, column, taskCount);
        column++;
        flexTable.setWidget(row, column, timeSheetCount);

        row++;
        column = 0;

        flexTable.setWidget(row, column, clientCount);
        column++;
        flexTable.setWidget(row, column, supplierCount);
        column++;
        flexTable.setWidget(row, column, leadCount);
        column++;
        flexTable.setWidget(row, column, contactCount);

        row++;
        column = 0;
        flexTable.setWidget(row, column, crmTaskCount);
        column++;
        flexTable.setWidget(row, column, eventCount);
        column++;
        flexTable.setWidget(row, column, caseCount);
        column++;
        flexTable.setWidget(row, column, invoiceCount);
        row++;
        column = 0;

        flexTable.setWidget(row, column, expenseCount);
        column++;
        flexTable.setWidget(row, column, productCount);
        column++;
        flexTable.setWidget(row, column, folderCount);
        column++;
        flexTable.setWidget(row, column, fileCount);
        row++;
        column = 0;

        flexTable.setWidget(row, column, assessmentCount);
        column++;
        flexTable.setWidget(row, column, adminEmail);
        column++;
        flexTable.setWidget(row, column, country);
        column++;
        flexTable.setWidget(row, column, phone);
        row++;
        column = 0;

        flexTable.setWidget(row, column, hostName);
        column++;
        flexTable.setWidget(row, column, adminName);
        column++;
        flexTable.setWidget(row, column, affiliate);
        column++;
        flexTable.setWidget(row, column, compaing);
        row++;

        column = 0;
        flexTable.setWidget(row, column, source);
        column++;
        flexTable.setWidget(row, column, noAccessUserCount);

        row++;
        flexTable.getFlexCellFormatter().setColSpan(row, column, 4);
        column = 0;

        final DatePicker fromDate = new DatePicker(new Date());
        fromDate.setWidth("100px");
        Date d = new Date();
        d.setMonth(d.getMonth() - 1);
        fromDate.setDate(d);
        final DatePicker toDate = new DatePicker(new Date());
        toDate.setWidth("100px");
        final TextBox limitBox = new TextBox();
        limitBox.setWidth("100px");
        limitBox.setText("500");
        limitBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        Validation.addNumericKeyboardListener(limitBox);

        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(5);
        panel.add(new HTML("<b class=customTitle>" + wfmStrings.registeredDate() + ":</b>"));
        panel.add(new HTML(wfmStrings.from() + ":"));
        panel.add(fromDate);
        panel.add(new HTML(wfmStrings.to() + ":"));
        panel.add(toDate);
        panel.add(new HTML(backendStrings.limitWord() + ":"));
        panel.add(limitBox);

        for (int i = 0; i < panel.getWidgetCount(); i++) {
            panel.setCellVerticalAlignment(panel.getWidget(i), HasVerticalAlignment.ALIGN_MIDDLE);
        }
        flexTable.setWidget(row, column, panel);
        flexTable.getFlexCellFormatter().setColSpan(row, column, 4);

        row++;

        companyName.setValue(true);
        companyName.setEnabled(false);
        companyID.setValue(true);

        KpiModal kpiModal = new KpiModal();
        kpiModal.setTitle(backendStrings.useFilterToExportTheReport());
        kpiModal.setWidth(700);
        kpiModal.add(flexTable);
        kpiModal.addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> kpiModal.close()));
        kpiModal.addButton(new WfmButton2(backendStrings.generateExcelReport(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            String action = GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/downloadBackendViewExcel";
            action += "?companyID=" + companyID.getValue() +
                    "&phone=" + phone.getValue() +
                    "&registrationDate=" + registrationDate.getValue() +
                    "&accessCount=" + accessCount.getValue() +
                    "&firstAccessDate=" + firstAccessDate.getValue() +
                    "&lastAccessDate=" + lastAccessDate.getValue() +
                    "&signUpFrom=" + signUpFrom.getValue() +
                    "&subscriptionType=" + subscriptionType.getValue() +
                    "&paymentStatus=" + paymentStatus.getValue() +
                    "&periodAccess=" + periodAccess.getValue() +
                    "&userCount=" + userCount.getValue() +
                    "&activeUsersCount=" + activeUsersCount.getValue() +
                    "&projectCount=" + projectCount.getValue() +
                    "&taskCount=" + taskCount.getValue() +
                    "&timeSheetCount=" + timeSheetCount.getValue() +
                    "&clientCount=" + clientCount.getValue() +
                    "&supplierCount=" + supplierCount.getValue() +
                    "&leadCount=" + leadCount.getValue() +
                    "&contactCount=" + contactCount.getValue() +
                    "&crmTaskCount=" + crmTaskCount.getValue() +
                    "&eventCount=" + eventCount.getValue() +
                    "&caseCount=" + caseCount.getValue() +
                    "&invoiceCount=" + invoiceCount.getValue() +
                    "&expenseCount=" + expenseCount.getValue() +
                    "&productCount=" + productCount.getValue() +
                    "&folderCount=" + folderCount.getValue() +
                    "&fileCount=" + fileCount.getValue() +
                    "&assessmentCount=" + assessmentCount.getValue() +
                    "&adminEmail=" + adminEmail.getValue() +
                    "&country=" + country.getValue() +
                    "&endDate=" + expirationDate.getValue() +
                    "&hostName=" + hostName.getValue() +
                    "&adminName=" + adminName.getValue() +
                    "&affiliate=" + affiliate.getValue() +
                    "&compaing=" + compaing.getValue() +
                    "&source=" + source.getValue() +
                    "&noAccessUserCount=" + noAccessUserCount.getValue() +

                    "&to=" + DateUtils.formatToParse(toDate.getDate()) +
                    "&from=" + DateUtils.formatToParse(fromDate.getDate()) +
                    "&limit=" + limitBox.getText();
            Window.open(action, "_blank", "");
        }));
        kpiModal.open();
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
}
