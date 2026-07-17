/*
package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyList;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.QuickViewPanel;
import com.edatasite.workforce.gwt.core.client.ui.QuickViewStrategy;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.billboard.BillboardPanel;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionLabel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class BackendReturningView extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private ListingPanel<CompanyListItem> listingTable;
    BackendServiceAsync backendService = BackendService.App.get();
    QuickViewPanel qvPanel;

    public BackendReturningView() {
        super("Returning Users", backendStrings.accessCount());
    }

    public String getIconStyle() {
        return "icon-backend";
    }

    public void refresh() {
        listingTable.reloadPage();
    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<CompanyListItem>(ListPanelType.ReturnBackendPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        listingTable.getPdfVersion().setVisible(false);
        try {
            listingTable.setExcelListener(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    String excelURL = CommandConstants.COMMON_URL + "/downloadBackendReturningViewExcel";
                    ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
                    listingTable.callListExcel(excelURL, filterParametrs);
                }
            });
        } catch (RuntimeException e) {

            e.printStackTrace();
        }
        qvPanel = new QuickViewPanel() {

            protected boolean doPreview(Object o) {


                if (o instanceof CompanyListItem) {
                    final CompanyListItem item = (CompanyListItem) o;
                    ArrayList links = new ArrayList();
                    SimpleLink testCompany = new SimpleLink("<font class=\"whiteText\" >Mark as TestCompany</font>", "");
                    final BackendServiceAsync backendService = BackendService.App.get();

                    testCompany.addMouseListener(new MouseListener() {
                        public void onMouseDown(Widget arg0, int arg1, int arg2) {
                            backendService.markasTestCompany((Integer) item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                public void failure(Throwable arg0) {
                                    Info.show("", "Could not mark " + item.getCompanyName() + " as TestCompany", Info.Type.WARNING);
                                }

                                public void success(Void arg0) {
                                    Info.show("", item.getCompanyName() + " has been marked as TestCompany", Info.Type.WARNING);
                                    refresh();
                                }
                            });
                        }

                        public void onMouseEnter(Widget arg0) {


                        }

                        public void onMouseLeave(Widget arg0) {


                        }

                        public void onMouseMove(Widget arg0, int arg1, int arg2) {


                        }

                        public void onMouseUp(Widget arg0, int arg1, int arg2) {


                        }
                    });

                    SimpleLink email = new SimpleLink("<font class=\"whiteText\" >Send Mail</font>", "");
                    email.addMouseListener(new MouseListener() {

                        public void onMouseDown(Widget arg0, int arg1, int arg2) {

                            History.newItem("email|email/" + item.getObjectID());
                        }

                        public void onMouseEnter(Widget arg0) {


                        }

                        public void onMouseLeave(Widget arg0) {


                        }

                        public void onMouseMove(Widget arg0, int arg1, int arg2) {


                        }

                        public void onMouseUp(Widget arg0, int arg1, int arg2) {


                        }

                    });

                    SimpleLink updateCompany = new SimpleLink("<font class=\"whiteText\" >Update Company</font>", "");
                    updateCompany.addMouseListener(new MouseListener() {

                        public void onMouseDown(Widget arg0, int arg1, int arg2) {

                            //History.newItem("email|email/" + item.getObjectID());
                            backendService.updateCompany((Integer) item.getObjectID(), new AbstractAsyncCallback<Void>() {

                                public void failure(Throwable arg0) {

                                    Info.show("", "Updation failed...", Info.Type.WARNING);
                                }

                                public void success(Void arg0) {

                                    Info.show("", "Updated successfully...", Info.Type.WARNING);
                                    refresh();

                                }


                            });
                        }

                        public void onMouseEnter(Widget arg0) {


                        }

                        public void onMouseLeave(Widget arg0) {


                        }

                        public void onMouseMove(Widget arg0, int arg1, int arg2) {


                        }

                        public void onMouseUp(Widget arg0, int arg1, int arg2) {


                        }

                    });

                    links.add(testCompany);
                    links.add(email);
                    links.add(updateCompany);

                    if (item.getActivated().equals("false")) {

                        SimpleLink resendActivationLink = new SimpleLink("<font class=\"whiteText\" >Resend Activation Link</font>", "");
                        resendActivationLink.addMouseListener(new MouseListener() {

                            public void onMouseDown(Widget arg0, int arg1, int arg2) {

                                backendService.resendActivationLink((Integer) item.getObjectID(), new AbstractAsyncCallback<Void>() {

                                    public void failure(Throwable arg0) {

                                        Info.show("", "Resend Activataion Link has failed...", Info.Type.WARNING);
                                    }

                                    public void success(Void arg0) {

                                        Info.show("", "Resend Activataion Link has done Successfully...", Info.Type.WARNING);
                                    }

                                });

                            }

                            public void onMouseEnter(Widget arg0) {


                            }

                            public void onMouseLeave(Widget arg0) {


                            }

                            public void onMouseMove(Widget arg0, int arg1, int arg2) {


                            }

                            public void onMouseUp(Widget arg0, int arg1, int arg2) {


                            }
                        });
                        links.add(resendActivationLink);
                    }

                    */
/**//*



                    setLinks(links);
                    setLinksSetter(new QuickViewStrategy() {

                        public void drawLinks(List links) {

                            addLinks(links);
                        }

                    });


                    PreviewSectionContainer container = new PreviewSectionContainer();
                    PreviewSectionField field;
                    PreviewSectionLabel label;
                    label = new PreviewSectionLabel("", "");

                    StringBuffer sb = new StringBuffer();
                    sb.append("<table>");
                    sb.append("<tr><td align=\"right\"> <b>Company ID:&nbsp;</b></td><td>" + item.getCompanyID().toString() + "</td>");
                    sb.append("<tr><td align=\"right\"> <b>Company Name:&nbsp;</b></td><td><a target=\"_blank\" href=" + item.getCompanyLoginLink() + ">" + item.getCompanyName() + "</a></td>");
                    sb.append("<tr><td align=\"right\"> <b>Country:&nbsp;</b></td><td>" + item.getCountry() + "</td>");
                    sb.append("<tr><td align=\"right\"> <b>Industry:&nbsp;</b></td><td>" + item.getIndustry() + "</td>");
                    sb.append("<tr><td align=\"right\"></td><td></td>");
                    sb.append("<tr><td align=\"right\"> <b>Contact Person:&nbsp;</b></td><td>" + item.getContactPerson() + "</td>");
                    sb.append("<tr><td align=\"right\"> <b>Email:&nbsp;</b></td><td>" + item.getEmail() + "</td>");
                    sb.append("<tr><td align=\"right\"> <b>Phone:&nbsp;</b></td><td>" + item.getPhone() + "</td>");
                    //sb.append("</br>" + description);
                    sb.append("</table>");
                    label.setHTML(sb.toString());

                    field = new PreviewSectionField();
                    field.addField("Users, Overall", item.getOverallUsersCount());
                    field.addField("Active", item.getActiveUsersCount());
                    field.addField("Inactive", item.getInactiveUsersCount());
                    //field.addField("", "");
                    field.addField("Projects", item.getProjectCount());
                    field.addField("Clients", item.getClientsCount());
                    field.addField("Issues", item.getIssuesCount());
                    //field.addField("", "");
                    field.addField("Tasks", item.getTaskCount());
                    field.addField("In Progress", item.getTasksinProgressCount());
                    field.addField("Completed", item.getTasksCompletedCount());
                    //field.addField("", "");
                    if (item.getTotalTimeEntries() != null) {
                        field.addField("Time Entries", Integer.parseInt(item.getTotalTimeEntries()) == 0 ? "0 hrs." : Integer.parseInt(item.getTotalTimeEntries()) / 60 + " hours " + Integer.parseInt(item.getTotalTimeEntries()) % 60 + " minutes");
                    } else {
                        field.addField("Time Entries", "0 hrs.");
                    }
                    //field.addField("", "");
                    field.addField("Leave Requests", item.getLeaveRequestsCount());
                    field.addField("Appraisals", item.getAppraisalsCount());

                    container.addSection(label, field);
                    clear();
                    add(container);
                }
                return true;
            }
        };
        listingTable.setQuickViewPanel(qvPanel);
        super.setQuickViewPanel(qvPanel);
        super.setListingPanel(listingTable);
        super.display();
        return null;
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {

                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ListingChooseFilter.DEFAULT;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = new ActionButton("Update All", "", ActionButton.Type.BUTTON);
                addNew.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        LoadingPanel.get().show("Updating...");
                        BackendServiceAsync backendService = BackendService.App.get();
                        backendService.updateStatistic(false, new AsyncCallback<Integer>() {

                            public void onFailure(Throwable arg0) {

                                LoadingPanel.loading(false);
                                Info.show("", "Updation Failed", Info.Type.WARNING);
                            }

                            public void onSuccess(Integer arg0) {
                                LoadingPanel.loading(false);
                                Info.show("", arg0 + " companies updated...", Info.Type.WARNING);
                                refresh();

                            }
                        });
                    }
                });
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                ActionButton more = new ActionButton("Update This Month", "", ActionButton.Type.BUTTON);
                more.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        LoadingPanel.get().show("Updating...");
                        BackendServiceAsync backendService = BackendService.App.get();
                        backendService.updateStatistic(true, new AsyncCallback<Integer>() {

                            public void onFailure(Throwable arg0) {

                                LoadingPanel.loading(false);
                                Info.show("", "Updation Failed ", Info.Type.WARNING);

                            }

                            public void onSuccess(Integer arg0) {
                                LoadingPanel.loading(false);
                                Info.show("", arg0 + " companies updated...", Info.Type.WARNING);
                                refresh();

                            }
                        });
                    }
                });
                return more;
            }

            @Override
            public void initBottomToolBarWidgets(ExportImportOption exportOption) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }

    private ListingRequestProvider<CompanyListItem> getListingRequestProvider() {
        return new ListingRequestProvider<CompanyListItem>() {

            @Override
            public void getRequest(ListingFilterParameter filterParametrs, final ListingCallback<CompanyListItem> callback) {
                backendService.getCompanies(Boolean.TRUE, filterParametrs, new AsyncCallback<CompanyList>() {

                    @Override
                    public void onFailure(Throwable throwable) {
                        callback.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(CompanyList companyList) {
                        callback.onSuccess(companyList);
                    }
                });

            }
        };
    }

    private ColumnDefinitionConfig[] drawColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[12];

        columns[0] = new ColumnDefinitionConfig<CompanyListItem, String>("Company ID", CompanyListItem.COMPANY_ID, 120) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getCompanyID() != null ? companyListItem.getCompanyID().toString() : "";
            }
        };

        columns[1] = new ColumnDefinitionConfig<CompanyListItem, String>("Company Name", CompanyListItem.COMPANY_NAME, 120) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getCompanyName();
            }
        };


        columns[2] = new ColumnDefinitionConfig<CompanyListItem, String>("Phone", CompanyListItem.PHONE, 80) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return companyListItem.getPhone();
            }
        };

        columns[3] = new ColumnDefinitionConfig<CompanyListItem, String>("Last Access", CompanyListItem.LAST_ACCESS_DATE, 100) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return DateUtils.preiewFormat(companyListItem.getLastAccessDate());
            }
        };

        columns[4] = new ColumnDefinitionConfig<CompanyListItem, String>("Registration Date", CompanyListItem.REGISTRATION_DATE, 100) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return DateUtils.preiewFormat(companyListItem.getRegistrationDate());
            }
        };

        columns[5] = new ColumnDefinitionConfig<CompanyListItem, String>("Access Count", CompanyListItem.ACCESS_COUNT, 50) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return ("" + companyListItem.getAccessCount());
            }
        };

        columns[6] = new ColumnDefinitionConfig<CompanyListItem, String>("Employee", CompanyListItem.EMPLOYEES, 50) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return ("" + companyListItem.getEmployeeCount());
            }
        };

        columns[7] = new ColumnDefinitionConfig<CompanyListItem, String>("Projects", CompanyListItem.PROJECT_COUNT, 50) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return ("" + companyListItem.getProjectCount());
            }
        };

        columns[8] = new ColumnDefinitionConfig<CompanyListItem, String>("Tasks", CompanyListItem.TASK_COUNT, 50) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return ("" + companyListItem.getTaskCount());
            }
        };

        columns[9] = new ColumnDefinitionConfig<CompanyListItem, String>("Departments", CompanyListItem.TASK_COUNT, 70) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return ("" + companyListItem.getDepartmentCount());
            }
        };

        columns[10] = new ColumnDefinitionConfig<CompanyListItem, String>("Signed Page", CompanyListItem.SIGNED_UP_FROM, 70) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return ("" + companyListItem.getSignedUpPage());
            }
        };

        columns[11] = new ColumnDefinitionConfig<CompanyListItem, String>("Appraisals", CompanyListItem.APPRAISALSCOUNT, 70) {

            @Override
            public String getCellValue(CompanyListItem companyListItem) {
                return ("" + companyListItem.getAppraisalsCount());
            }
        };
        return columns;
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

*/
