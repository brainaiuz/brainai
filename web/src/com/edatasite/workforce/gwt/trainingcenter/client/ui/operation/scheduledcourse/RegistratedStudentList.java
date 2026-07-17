package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
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
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.StudentListView;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/3/12
 * Time: 1:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class RegistratedStudentList extends StudentListView implements PermissionConstants, Constants {

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer scheduledCourseID;
    private boolean instructorNotFount = false;

    public RegistratedStudentList(Integer scheduledCourseID) {
        super("courseStudents", tcStrings.courseStudents());
        this.scheduledCourseID = scheduledCourseID;
    }

    public RegistratedStudentList(Integer scheduledCourseID, boolean instructor) {
        super("courseStudents", tcStrings.courseStudents());
        this.scheduledCourseID = scheduledCourseID;
        this.instructorNotFount = instructor;
    }

    @Override
    public ListPanelType getListPanelType() {
        return ListPanelType.RegistratedStudentsListPanel;
    }

    protected ListingRequestProvider<StudentItem> getProvider() {
        return (filterParametrs, studentItemListingCallback) -> {
            filterParametrs.setScheduledCourseID(scheduledCourseID);
            TCService.App.get().getScheduledCourseStudents(filterParametrs, new AbstractAsyncCallback<ListResult<StudentItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    studentItemListingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<StudentItem> result) {
                    studentItemListingCallback.onSuccess(result);
                }
            });
        };
    }

    protected CustomColumnDefinitionConfig[] getColumns() {
        CustomColumnDefinitionConfig[] columns = new CustomColumnDefinitionConfig[17];
        int index = 0;

        // 0
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

                if (Utils.hasPermission(PermissionConstants.TC_EDIT_STUDENT)) {
                    //student edit
                    final MenuPopItem studentEdit = new MenuPopItem(tcStrings.editStudentOnly(), "icon-employee-edit-profile");
                    studentEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_STUDENTS + "|editStudent/" + rowValue.getObjectId()));
                    menuItemCount++;
                    menuBar.addItem(studentEdit);
                }

                if (rowValue.getInvoiceID() == null) {
                    //student reschedule functional
                    if (Utils.hasPermission(PermissionConstants.TC_RESCHEDULE)) {
                        final MenuPopItem reschedule = new MenuPopItem(tcStrings.reschedule(), "icon-employee-edit-profile");
                        reschedule.setCommand(() -> new StudentRescheduleView(scheduledCourseID, rowValue.getObjectId()));
                        menuItemCount++;
                        if (Constants.STUDENT_COURSE_SCHEDULE_ATTENDED.equals(rowValue.getStatusCode())) {
                            menuBar.addItem(reschedule);
                        }
                    }
                    //student remove
                    if (Utils.hasPermission(PermissionConstants.TC_CANCEL_STUDENT)) {
                        final MenuPopItem studentCancel = new MenuPopItem(wfmStrings.cancel(), "icon-remove");
                        studentCancel.setCommand(() -> {
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
                                    TCService.App.get().deleteStudentCourseScheduledStudents(scheduledCourseID, rowValue.getObjectId(), new AbstractAsyncCallback<Boolean>() {
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
                                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STUDENT_DELETE, result, RegistratedStudentList.this);
                                            }
                                        }
                                    });
                                }
                            });
                            wfmMessageBox.open();
                        });
                        menuItemCount++;
                        menuBar.addItem(studentCancel);
                    }
                }


                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                Anchor action = toolItem.getAction();
                action.addClickHandler(event -> studentSummary.setVisible(true));
                return action;  //return action menu items
            }
        };
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setColumnSortable(false);

        // 1
        //number
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(wfmStrings.number(), StudentItem.STUDENT_NUMBER, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getNumber() != null ? rowValue.getNumber() : "";
            }
        };
        columns[index++].setMinimumColumnWidth(70);

        // 2
        //course booking Number
        columns[index] = new ColumnDefinitionConfig<StudentItem, SimpleLink>(wfmStrings.booking(), StudentItem.COURSE_BOOKING, 100) {
            @Override
            public SimpleLink getCellValue(StudentItem rowValue) {
                return new SimpleLink((rowValue.getCourseBookingNumber() != null ? rowValue.getCourseBookingNumber() : ""), (TCConstants.TC_COURSE_BOOKING + "|" + TC_VIEW_COURSE_BOOKING + "/" + rowValue.getCourseBookingId()));
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(55);

        // 3
        //first name
        columns[index] = new ColumnDefinitionConfig<StudentItem, SimpleLink>(wfmStrings.firstName(), StudentItem.STUDENT_FIRST_NAME, 100) {
            @Override
            public SimpleLink getCellValue(StudentItem rowValue) {
                return new SimpleLink(rowValue.getFirstName(), (TC_STUDENTS + "|summary/" + rowValue.getObjectId())); //return student first name with redirect summary link
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        // 4
        //last name
        columns[index] = new ColumnDefinitionConfig<StudentItem, SimpleLink>(wfmStrings.lastName(), StudentItem.STUDENT_LAST_NAME, 100) {
            @Override
            public SimpleLink getCellValue(StudentItem rowValue) {
                return new SimpleLink(rowValue.getLastName(), (TC_STUDENTS + "|summary/" + rowValue.getObjectId())); //return student last name with redirect summary link
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        // 5
        //customer
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), StudentItem.STUDENT_CUSTOMER, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getCustomerName() != null ? rowValue.getCustomerName() : "";
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        // 6
        //phone number
        columns[index] = new ColumnDefinitionConfig<StudentItem, HTML>(wfmStrings.phone(), StudentItem.STUDENT_PHONE_NUMBER, 100) {
            @Override
            public HTML getCellValue(StudentItem rowValue) {
                return rowValue.getPrimaryPhone() != null && "n/a".equalsIgnoreCase(rowValue.getPrimaryPhone()) ?
                        new HTML(rowValue.getPrimaryPhone()) :
                        Utils.getPhoneCallFormat2(new PhoneNumber(rowValue.getPrimaryPhone()).toString());//return student phone number
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        // 7
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

        // 8
        //company employee number
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(wfmStrings.companyEmployeeNumber(), StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getCompEmpNum() != null ? rowValue.getCompEmpNum() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(100);

        // 9
        //residence number
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(tcStrings.residenceNumber(), StudentItem.STUDENT_RESIDENCE_NUMBER, 100) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getSafetyPPNumber() != null ? rowValue.getSafetyPPNumber() : "";
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        // 10
        //status
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(wfmStrings.status(), StudentItem.STUDENT_STATUS, 100) {   //10
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getStatus();
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        // 11
        //attended status
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(tcStrings.attendance(), StudentItem.STUDENT_ATTENDED_STATUS, 100) {   //10
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getAttendedStatus();
            }

            @Override
            public void setCellValue(StudentItem rowValue, String cellValue) {
                rowValue.setAttendedStatus(cellValue);
                saveCellValue(rowValue);
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        // 12
        //exam status
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(tcStrings.examStatus(), StudentItem.STUDENT_EXAM_STATUS, 100) { //11
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getExamStatus();
            }

            @Override
            public void setCellValue(final StudentItem rowValue, final String cellValue) {
                if (!instructorNotFount) {
                    rowValue.setExamStatus(cellValue);
                    saveCellValue(rowValue);
                } else {
                    Info.show(wfmStrings.pleaseSetInstructor(), Info.Type.WARNING);
                }
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        // 13
        //grade
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(wfmStrings.grade(), StudentItem.STUDENT_GRADE_COLUMN, 70) {  //12
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getGrade();
            }

            @Override
            public void setCellValue(StudentItem rowValue, String cellValue) {
                rowValue.setGrade(cellValue);
                saveCellValue(rowValue);
            }
        };
        columns[index++].setMinimumColumnWidth(35);

        // 14
        //points
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(tcStrings.points(), StudentItem.STUDENT_POINTS, 70) {   //13
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getPoints();
            }

            @Override
            public void setCellValue(StudentItem rowValue, String cellValue) {
                rowValue.setPoints(cellValue);
                saveCellValue(rowValue);
            }
        };
        columns[index++].setMinimumColumnWidth(35);

        // 15
        //reference indication number
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(tcStrings.refIndNumber(), StudentItem.STUDENT_REFERENCE_IND_NUMBER, 70) { //14
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getRefIndNumber() != null ? rowValue.getRefIndNumber() : "";
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(35);

        // 16
        //invoice number
        columns[index] = new ColumnDefinitionConfig<StudentItem, String>(wfmStrings.invoiceNumber(), StudentItem.INVOICE_NUMBER, 70) {
            @Override
            public String getCellValue(StudentItem rowValue) {
                return rowValue.getInvoiceNumber();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(35);
        initCellEdit(columns);
        return columns;
    }

    private void initCellEdit(CustomColumnDefinitionConfig[] columns) {

        if (Utils.hasPermission(PermissionConstants.TC_RESCHEDULE)) {
            // Student Attended Status Cell Editor
            final DropDownCellEditor<String> attendedStatus = new DropDownCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getListBox().getSelectedItem().getName();
                }

                @Override
                protected void setValue(String cellValue) {
                    getListBox().setSelectedByValue(cellValue);
                }
            };
            attendedStatus.getListBox().setWithoutNullLabel(true);
            columns[11].setCellEditor(attendedStatus);

            columns[11].setCellChangesSave(new CellChange<StudentItem>() {
                @Override
                public void saveCell(StudentItem rowValue, String columnCodeName) {
                    rowValue.setAttendedStatusID(attendedStatus.getSelectItem().getId());
                    saveAttendStudentEditCellValue(rowValue, columnCodeName);
                }
            });

            // Get Status Cell List
            TCService.App.get().getAttendStatusList(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    attendedStatus.getListBox().setItems(result);
                }
            });
        }

        if (Utils.hasPermission(TC_STUDENT_EXAM_STATUS)) {
            // Student Exam Status Cell Editor
            final DropDownCellEditor<String> statusCellEditor = new DropDownCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getListBox().getSelectedItem().getName();
                }

                @Override
                protected void setValue(String cellValue) {
                    getListBox().setSelectedByValue(cellValue);
                }
            };
            statusCellEditor.getListBox().setWithoutNullLabel(true);
            columns[12].setCellEditor(statusCellEditor);

            columns[12].setCellChangesSave(new CellChange<StudentItem>() {
                @Override
                public void saveCell(StudentItem rowValue, String columnCodeName) {
                    rowValue.setExamStatusId(statusCellEditor.getSelectItem().getId());
                    saveAttendStudentEditCellValue(rowValue, columnCodeName);
                }
            });

            // Get Status Cell List
            TCService.App.get().getAttendStudentStatus(new AbstractAsyncCallback<SelectItem[]>() {
                public void success(SelectItem[] result) {
                    statusCellEditor.getListBox().setItems(result);
                }
            });
        }
        if (Utils.hasRole(ADMIN)) {

            //Student Grade Cell Editor
            final TextBoxCellEditor<String> percentCellEditor = new TextBoxCellEditor<String>(80) {
                @Override
                protected String getValue() {
                    return getText();
                }

                @Override
                protected void setValue(String cellValue) {
                    setText(cellValue);
                }
            };
            columns[13].setCellEditor(percentCellEditor);
            columns[13].setCellChangesSave(new CellChange<StudentItem>() {
                @Override
                public void saveCell(StudentItem rowValue, String columnCodeName) {
                    saveAttendStudentEditCellValue(rowValue, columnCodeName);
                }
            });

            //Student Total Point Cell Editor
            final TextBoxCellEditor<String> pointCellEditor = new TextBoxCellEditor<String>(80) {
                @Override
                protected String getValue() {
                    return getText();
                }

                @Override
                protected void setValue(String cellValue) {
                    setText(cellValue);
                }
            };
            columns[14].setCellEditor(pointCellEditor);
            columns[14].setCellChangesSave(new CellChange<StudentItem>() {
                @Override
                public void saveCell(StudentItem rowValue, String columnCodeName) {
                    saveAttendStudentEditCellValue(rowValue, columnCodeName);
                }
            });


        }
    }

    private void saveAttendStudentEditCellValue(StudentItem rowValue, String columnCodeName) {
        TCService.App.get().saveAttendStudentEditCellValue(rowValue, scheduledCourseID, columnCodeName, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                FlowPanel toolPanel = new FlowPanel();
                exportOption.initExport(toolPanel, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(tcStrings.students().toLowerCase()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

}
