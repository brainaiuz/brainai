package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Div;

import java.util.HashSet;

/**
 * User: Ilhombek
 * Date: 7/18/12
 * Time: 3:05 PM
 */
public class StudentListView extends BaseListView implements TCConstants, Constants {

    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private int totalCount = 0;
    protected HashSet<StudentItem> selectedItems = new HashSet<>();
    protected ContextMenu actions;
    private ContextMenu emptyActions = null;

    public final static Images images = (Images) GWT.create(Images.class);

    ListingPanel<StudentItem> listingPanel;

    public StudentListView(String name, String description) {
        super(name, description);
    }

    public StudentListView(String name) {
        super(name);
        GWT.log("StudentListView" + property.getPlural(tcStrings.students()));
        setDescription(property.getPlural(tcStrings.students()));
    }

    @Override
    public String getIconStyle() {
        return "bgMark student-icon";
    }

    public ListPanelType getListPanelType() {
        return ListPanelType.StudentsListPanel;
    }

    @Override
    protected Widget onInitialize() {

        if ("StudentsListPanel".equals(getListPanelType().name())) {
            listingPanel = new ListingPanel<>(getListPanelType(), getColumns(), getProvider(), getDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
            listingPanel.addSelectionRowHandler(selectedRows -> {
                selectedItems = selectedRows;
                actions = null;
            });
            listingPanel.setExcelListener(clickEvent -> {
                String excelURL = CommandConstants.COMMON_URL + "/downloadStudentListExcel";
                ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs();
                listingPanel.callListExcel(excelURL, filterParametrs);
            });
            listingPanel.setPDFListener(clickEvent -> {
                if (totalCount > 1000) {
                    Window.alert(wfmStrings.CurrentlyLimitedContactExport());
                }
                String pdfURL = CommandConstants.PDF_URL + "/studentListPDFHandler";
                ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs();
                listingPanel.callListPDF(pdfURL, filterParametrs);
            });
        } else {
            listingPanel = new ListingPanel<>(getListPanelType(), getColumns(), getProvider(), getDesign());
            listingPanel.setExcelListener(clickEvent -> {
                String excelURL = CommandConstants.COMMON_URL + "/downloadRegistratedStudentListExcel";
                ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs();
                listingPanel.callListExcel(excelURL, filterParametrs);
            });
            listingPanel.setPDFListener(clickEvent -> {
                if (totalCount > 1000) {
                    Window.alert(wfmStrings.CurrentlyLimitedContactExport());
                }
                String pdfURL = CommandConstants.PDF_URL + "/studentRegistratedListPDFHandler";
                ListingFilterParameter filterParametrs = listingPanel.getFilterParametrs();
                listingPanel.callListPDF(pdfURL, filterParametrs);
            });
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_STUDENT_ADD_EDIT, StudentListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COURSE_STUDENT_ADD_EDIT, StudentListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_STUDENT_DELETE, StudentListView.this, (sender, args) -> listingPanel.reloadPage());
        add(listingPanel);
        return null;
    }

    protected CustomColumnDefinitionConfig[] getColumns() {
        CustomColumnDefinitionConfig[] columns = new CustomColumnDefinitionConfig[10];
        int index = 0;
        //action
        columns[index] = new ColumnDefinitionConfig<StudentItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final StudentItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                //student summary
                final MenuPopItem studentSummary = new MenuPopItem(wfmStrings.student() + wfmStrings.summaryView(), "icon-contact-small");
                studentSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_STUDENTS + "|summary/" + rowValue.getObjectId()));
                menuItemCount++;
                menuBar.addItem(studentSummary);
                //student edit
                final MenuPopItem studentEdit = new MenuPopItem(tcStrings.editStudentOnly(), "icon-employee-edit-profile");
                studentEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_STUDENTS + "|editStudent/" + rowValue.getObjectId()));
                menuItemCount++;
                menuBar.addItem(studentEdit);
                studentEdit.setVisible(Utils.hasPermission(PermissionConstants.TC_STUDENT_EDIT));
                //student delete
                final MenuPopItem studentDelete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                studentDelete.setCommand(() -> {
                    if (Constants.STUDENT_COURSE_SCHEDULE_ATTENDED.equals(rowValue.getStatus())) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK);
                        messageBox.setMessage("Sorry, you can not delete the student who attended to some scheduled courses.");
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                messageBox.close();
                            }
                        });
                        messageBox.open();
                    } else {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.setMessage(wfmMessages.sureYouWantToDelete(wfmStrings.student().toLowerCase(), "?"));
                        wfmMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                TCService.App.get().deleteStudent(rowValue.getObjectId(), new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Boolean result) {
                                        LoadingPanel.loading(false);
                                        if (result) {
                                            Info.show(wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.student().toLowerCase()));
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STUDENT_DELETE, result, StudentListView.this);
                                        }
                                    }
                                });
                            }
                        });
                        wfmMessageBox.open();
                    }
                });

                menuItemCount++;
                menuBar.addItem(studentDelete);
                studentDelete.setVisible(Utils.hasPermission(PermissionConstants.TC_STUDENT_DELETE));
                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                Anchor action = toolItem.getAction();
                action.addClickHandler(event -> {
                    studentSummary.setVisible(true);
                    studentEdit.setVisible(true);
                    studentDelete.setVisible(true);
                });
                return action;  //return action menu items
            }
        };
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setColumnSortable(false);
        //number
        columns[index] = new ColumnDefinitionConfig<StudentItem, SimpleLink>(wfmStrings.number(), StudentItem.STUDENT_NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(StudentItem rowValue) {
                return new SimpleLink((rowValue.getNumber() != null ? rowValue.getNumber() : ""), (TC_STUDENTS + "|summary/" + rowValue.getObjectId())); //return student number with redirect summary link
            }
        };
        columns[index++].setMinimumColumnWidth(70);

        //residence number
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(tcStrings.residenceNumber(), StudentItem.STUDENT_RESIDENCE_NUMBER, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getSafetyPPNumber() != null ? rowValue.getSafetyPPNumber() : "";
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        //company employee number
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(wfmStrings.companyEmployeeNumber(), StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getCompEmpNum() != null ? rowValue.getCompEmpNum() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(100);
        //reference indication number
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(tcStrings.refIndNumber(), StudentItem.STUDENT_REFERENCE_IND_NUMBER, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getRefIndNumber() != null ? rowValue.getRefIndNumber() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(100);
        //first name
        columns[index] = new ColumnDefinitionConfig<StudentItem, SimpleLink>(wfmStrings.firstName(), StudentItem.STUDENT_FIRST_NAME, 100) {
            @Override
            public SimpleLink getCellValue(StudentItem rowValue) {
                return new SimpleLink(rowValue.getFirstName(), (TC_STUDENTS + "|summary/" + rowValue.getObjectId())); //return student first name with redirect summary link
            }
        };
        columns[index++].setMinimumColumnWidth(100);
        //last name
        columns[index] = new ColumnDefinitionConfig<StudentItem, SimpleLink>(wfmStrings.lastName(), StudentItem.STUDENT_LAST_NAME, 100) {
            @Override
            public SimpleLink getCellValue(StudentItem rowValue) {
                return new SimpleLink(rowValue.getLastName(), (TC_STUDENTS + "|summary/" + rowValue.getObjectId())); //return student last name with redirect summary link
            }
        };
        columns[index++].setMinimumColumnWidth(100);
        //customer
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), StudentItem.STUDENT_CUSTOMER, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getCustomerName() != null ? rowValue.getCustomerName() : "";
            }
        };
        columns[index++].setMinimumColumnWidth(100);
        //phone number
        /*columns[index] = new ColumnDefinitionConfig<StudentItem, HTML>(wfmStrings.phoneNumber(), StudentItem.STUDENT_PHONE_NUMBER, 100) {
            @Override
            public HTML getCellValue(StudentItem rowValue) {
                return rowValue.getPrimaryPhone() != null && "n/a".equals(rowValue.getPrimaryPhone().toLowerCase()) ?
                        new HTML(rowValue.getPrimaryPhone()) :
                        Utils.getPhoneCallFormat2(new PhoneNumber(rowValue.getPrimaryPhone()).toString());//return student phone number
            }
        };*/
        columns[index] = new ColumnDefinitionConfig<StudentItem, Div>(wfmStrings.phone(), ContactListItem.PHONE, 115) {
            @Override
            public Div getCellValue(final StudentItem rowValue) {
                PhonePopup phonePopup = new PhonePopup(rowValue.getPrimaryPhone(), rowValue, false, true);
                return phonePopup.getPhoneWidget();
            }
        };
        columns[index++].setMinimumColumnWidth(100);
        //e-mail
        columns[index] = new ColumnDefinitionConfig<StudentItem, SimpleLink>(wfmStrings.email(), StudentItem.STUDENT_E_MAIL, 100) {
            @Override
            public SimpleLink getCellValue(final StudentItem rowValue) {
                SimpleLink sendEmailLink = new SimpleLink(!Utils.isNullOrEmpty(rowValue.getPrimaryEmail()) ? rowValue.getPrimaryEmail() : "");
                if (!Utils.isNullOrEmpty(rowValue.getPrimaryEmail())) {
                    //sendEmailLink.addClickHandler(clickEvent -> new ComposeView(rowValue.getPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_STUDENT, rowValue.getObjectId(), rowValue.getName())));
                    sendEmailLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + rowValue.getPrimaryEmail() + "/" + RelationItem.TYPE_STUDENT + "/" + rowValue.getObjectId() + "/" + rowValue.getName()));
                }
                return sendEmailLink;

            }
        };
        columns[index++].setMinimumColumnWidth(100);
        //department code
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.number(), wfmStrings.department()), StudentItem.STUDENT_DEPARTMENT_CODE, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getDepartmentCode() != null ? rowValue.getDepartmentCode() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(100);

        return columns;
    }

    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNewPlacement = getAddNewButton();
                addNewPlacement.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_STUDENTS + "|add/add"));
                return addNewPlacement;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("student_list_more");
                more.addClickHandler(clickEvent -> {
                    MenuBar menu = getActionsForSelections();
                    menu.setAutoOpen(true);
                    more.setMenu(menu);
                });
                if (Utils.hasPermission(PermissionConstants.TC_STUDENT_MORE)) {
                    return more;
                }

                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(tcStrings.students().toLowerCase()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private MenuBar getActionsForSelections() {
        if (selectedItems != null && selectedItems.size() > 0) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                actions.getMenuBar().addStyleName("my-menu");
                actions.addMenuItem(wfmStrings.merge(), AbstractImagePrototype.create(images.merge()).getHTML(), true, () -> {
                    if (selectedItems.size() >= 2) {
                        StringBuilder ids = new StringBuilder();
                        String delimitr = "/";
                        int i = 0;
                        for (StudentItem item : selectedItems) {
                            ids.append(delimitr).append(item.getObjectId().toString());
                            if (i++ == 4) {
                                break;
                            }
                        }
                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.MERGE + "|add/add/" + TCConstants.STUDENT_MERGE + ids);
                    }
                });

            }
            return actions.getMenuBar();
        } else {
            if (emptyActions == null) {
                emptyActions = new ContextMenu();
                emptyActions.getMenuBar().setAutoOpen(false);
                emptyActions.addMenuItem(wfmStrings.selectAnyItemToActivateBatchActions(), null, true, null);
            }
            return emptyActions.getMenuBar();
        }
    }

    public interface Images extends ClientBundle {
        @Source("com/edatasite/workforce/gwt/core/resource/icons/refresh.png")
        ImageResource merge();
    }

    protected ListingRequestProvider<StudentItem> getProvider() {
        return (filterParametrs, studentItemListingCallback) -> {
            TCService.App.get().getStudentList(filterParametrs, new AbstractAsyncCallback<ListResult<StudentItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    studentItemListingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<StudentItem> result) {
                    totalCount = result.getTotal();
                    studentItemListingCallback.onSuccess(result);
                }
            });
        };
    }
    @Override
    public String getPropertyCode() {
        return "students";
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