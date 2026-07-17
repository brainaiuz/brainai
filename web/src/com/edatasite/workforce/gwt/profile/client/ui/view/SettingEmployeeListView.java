package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;

import java.util.ArrayList;
import java.util.Date;

public class SettingEmployeeListView extends EmployeeListView {

    public SettingEmployeeListView(String fromView) {
        super(fromView);
    }

    protected ColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig columnConfig;

        if (isFromHRMS() || isFromPRICING()) {
            //action
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
                @Override
                public Anchor getCellValue(EmployeeListItem item) {
                    return getEmployeeActions(item);
                }
            };
            columnConfig.setColumnSortable(false);
            columnConfig.setMinimumColumnWidth(100);
            columnConfig.setMaximumColumnWidth(100);
            columns.add(columnConfig);
        }

        if (employmentInfPermission) {
            if (isFromPM() || isFromHRMS() || isFromPAYROLL() || isFromPRICING()) {
                //employee code
                columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.employeeCode(), EmployeeListItem.EMPLOYEE_NUMBER, 110) {
                    @Override
                    public String getCellValue(EmployeeListItem item) {
                        return item.getEmployeeNumber() != null ? item.getEmployeeNumber() : "N/A";
                    }
                };
                columnConfig.setMinimumColumnWidth(85);
                columns.add(columnConfig);
            }
        }
        //first name
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SimpleLink>(wfmStrings.firstName(), EmployeeListItem.FIRST_NAME, 110) {
            @Override
            public SimpleLink getCellValue(EmployeeListItem item) {
                if (isFromHRMS()) {
                    return getLink(item.getFirstName(), "employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else if (isFromPAYROLL()) {
                    if (item.getEmployeeTemplateID() != null && !Utils.adminOrDirector() && !Utils.hasPermission(PAYROLL_EMPLOYEE_EDIT)) {
                        return getLink(item.getFirstName(), "starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    } else {
                        return getLink(item.getFirstName(), "starter|summary/" + item.getObjectID() + "/fromEmployeeList/view", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    }
                } else if (isFromPRICING()) {
                    return getLink(item.getFirstName(), "", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else {
                    return getLink(item.getFirstName(), "employee|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                }
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //last name
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SimpleLink>(wfmStrings.lastName(), EmployeeListItem.LAST_NAME, 110) {
            @Override
            public SimpleLink getCellValue(EmployeeListItem item) {
                if (isFromHRMS()) {
                    return getLink(item.getLastName(), "employeeProfile|" + EMPLOYEE_PROFILE_VIEW + "/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else if (isFromPAYROLL()) {
                    if (item.getEmployeeTemplateID() != null && !Utils.adminOrDirector() && !Utils.hasPermission(PAYROLL_EMPLOYEE_EDIT)) {
                        return getLink(item.getLastName(), "starter|summary/" + item.getEmployeeTemplateID() + "/fromTemplate/view/" + item.getStatus(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    } else {
                        return getLink(item.getLastName(), "starter|summary/" + item.getObjectID() + "/fromEmployeeList/view", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                    }
                } else if (isFromPRICING()) {
                    return getLink(item.getLastName(), "", item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                } else {
                    return getLink(item.getLastName(), "employee|summary/" + item.getObjectID(), item.getEmployeeNumber() != null && !item.getEmployeeNumber().isEmpty() ? item.getEmployeeNumber() : item.getFirstName(), item.getFirstName());
                }
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //phone number
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.phone(), EmployeeListItem.PHONE_NUMBER, 140) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return new PhoneNumber(item.getPhoneNumber()).toString();
            }

            @Override
            public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                rowValue.setPhoneNumber(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        //email
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.email(), EmployeeListItem.EMAIL, 200) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getEmail();
            }

            @Override
            public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                rowValue.setEmail(cellValue);
                saveCellValue(rowValue);
            }
        };
        columnConfig.setMinimumColumnWidth(130);
        columns.add(columnConfig);

        //employee role
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.role(), EmployeeListItem.ROLE, 80) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getRole();
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //employee status
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.accountStatus(), EmployeeListItem.STATUS, 110) {
            @Override
            public String getCellValue(EmployeeListItem item) {
                return item.getStatus();
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //employee position
        if (employmentInfPermission) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.position(), EmployeeListItem.POSITION, 90) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return item.getPositionId() != null ? new SelectItem(item.getPositionId(), item.getPosition()) : null;
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.setPositionId(cellValue.getId());
                        rowValue.setPosition(cellValue.getName());
                    } else {
                        rowValue.setPositionId(null);
                        rowValue.setPosition(null);
                    }
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setShow(false);
            columnConfig.setMinimumColumnWidth(70);
            columns.add(columnConfig);
        }

        //employee location
        if (employmentInfPermission) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), EmployeeListItem.LOCATION, 80) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return item.getLocation() != null && item.getLocationId() != null ? new SelectItem(item.getLocationId(), item.getLocation()) : null;
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.setLocationId(cellValue.getId());
                        rowValue.setLocation(cellValue.getName());
                    } else {
                        rowValue.setLocationId(null);
                        rowValue.setLocation(null);
                    }
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setColumnSortable(true);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        //gender
        columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.gender(), EmployeeListItem.GENDER_NAME, 150) {
            @Override
            public SelectItem getCellValue(EmployeeListItem item) {
                return item.getGenderName() != null ? new SelectItem((item.getGenderName().equals(wfmStrings.male()) ? 0 : 1), item.getGenderName()) : null;
            }

            @Override
            public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                rowValue.setGenderName(cellValue.getName());
                saveCellValue(rowValue);
            }
        };
        columnConfig.setMinimumColumnWidth(130);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //Birth Date
        if (birthDayPermission) {
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.dateOfBirth(), EmployeeListItem.BIRH_DATE, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return DateUtils.format(item.getBirthDate());
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        rowValue.setBirthDate(new DateNonConvertable(DateUtils.parse(cellValue)));
                        saveCellValue(rowValue);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        if (employmentInfPermission) {
            //employee supervisor
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(wfmStrings.supervisor(), EmployeeListItem.SUPERVISOR, 100) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return item.getSupervisorItem();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.getSupervisorItem().setId(cellValue.getId());
                        rowValue.getSupervisorItem().setName(cellValue.getName());
                    } else {
                        rowValue.getSupervisorItem().setId(null);
                        rowValue.getSupervisorItem().setName(null);
                    }
                    saveCellValue(rowValue);
                }
            };
            columnConfig.setMinimumColumnWidth(80);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //department
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, SelectItem>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), EmployeeListItem.DEPARTMENT, 80) {
                @Override
                public SelectItem getCellValue(EmployeeListItem item) {
                    return item.getDepartment() != null && item.getDepartmentId() != null ? new SelectItem(item.getDepartmentId(), item.getDepartment()) : null;

                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null) {
                        rowValue.setDepartmentId(cellValue.getId());
                        rowValue.setDepartment(cellValue.getName());
                    } else {
                        rowValue.setDepartmentId(null);
                        rowValue.setDepartment(null);
                    }
                    saveCellValue(rowValue);
                }
            };

            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);

            //Hire date
            if (!isFromPRICING()) {
                columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.hireDate(), EmployeeListItem.START_DATE, 100) {
                    @Override
                    public String getCellValue(EmployeeListItem item) {
                        return item.getStartDate() != null ? DateUtils.format1(item.getStartDate().getNonConvertedDate()) : wfmStrings.notAvailable();
                    }

                    @Override
                    public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                        try {
                            Date cellDate = DateUtils.parse(cellValue);
                            if (cellValue != null && rowValue.getEnddate() != null && cellDate.getDate() > rowValue.getEnddate().getDateLong()) {
                                Info.show(wfmStrings.resignationDateCannotBeEarlierThanHireDate(), Info.Type.WARNING);
                            } else {
                                rowValue.setStartDate(cellDate != null ? new DateNonConvertable(cellDate) : null);
                                saveCellValue(rowValue);
                            }
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                };
                columnConfig.setMinimumColumnWidth(70);
                columns.add(columnConfig);
            }

            //Fire date
            columnConfig = new ColumnDefinitionConfig<EmployeeListItem, String>(wfmStrings.resignationDate(), EmployeeListItem.END_DATE, 80) {
                @Override
                public String getCellValue(EmployeeListItem item) {
                    return item.getEnddate() != null ? DateUtils.format1(item.getEnddate().getNonConvertedDate()) : wfmStrings.notAvailable();
                }

                @Override
                public void setCellValue(EmployeeListItem rowValue, String cellValue) {
                    try {
                        Date cellDate = DateUtils.parse(cellValue);
                        if (cellValue != null && rowValue.getStartDate() != null && cellDate.getTime() < rowValue.getStartDate().getDateLong()) {
                            Info.show(wfmStrings.resignationDateCannotBeEarlierThanHireDate(), Info.Type.WARNING);
                        } else {
                            rowValue.setEnddate(cellDate != null ? new DateNonConvertable(cellDate) : null);
                            saveCellValue(rowValue);
                        }
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(70);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        return columns.toArray(new ColumnDefinitionConfig[0]);
    }
}
