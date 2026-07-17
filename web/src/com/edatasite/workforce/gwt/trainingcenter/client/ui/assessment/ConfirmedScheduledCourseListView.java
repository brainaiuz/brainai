package com.edatasite.workforce.gwt.trainingcenter.client.ui.assessment;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse.ScheduledCourseListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 9/14/12
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfirmedScheduledCourseListView extends ScheduledCourseListView implements PermissionConstants {

    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();

    private FlowPanel pnlCRContainer;

    public ConfirmedScheduledCourseListView() {
        super("confirmedscheduledcourses",null);
        setDescription(property.getPlural(tcStrings.confirmedScheduledCourse()));
    }

    protected Widget onInitialize() {
        super.onInitialize();
        pnlCRContainer = new FlowPanel();
        add(pnlCRContainer);

        return null;
    }

    protected CustomColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[10];
        //action
        columns[0] = new ColumnDefinitionConfig<ScheduledCourseItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            public Anchor getCellValue(final ScheduledCourseItem rowValue) {
                return getActions(rowValue);
            }

        };
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<ScheduledCourseItem, SimpleLink>(wfmStrings.number(), ScheduledCourseItem.NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(ScheduledCourseItem rowValue) {
                return new SimpleLink(rowValue.getNumber(), (TC_SCHEDULED_COURSE + "|summary/" + rowValue.getObjectID() + "/" + (rowValue.getInstructorID() == null)));
            }
        };
        columns[1].setMinimumColumnWidth(100);

        columns[2] = new ColumnDefinitionConfig<ScheduledCourseItem, SimpleLink>(wfmStrings.course(), ScheduledCourseItem.COURSE, 100) {
            @Override
            public SimpleLink getCellValue(ScheduledCourseItem rowValue) {
                return getSimpleLink(rowValue);
            }
        };
        columns[2].setMinimumColumnWidth(100);

        columns[3] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.instructor(), ScheduledCourseItem.INSTRUCTOR, 100) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getInstructorName();
            }
        };
        columns[3].setMinimumColumnWidth(100);

        columns[4] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.date(), ScheduledCourseItem.START_DATE, 100) {
            @Override
            public String getCellValue(final ScheduledCourseItem rowValue) {
                return DateUtils.formatInternal(rowValue.getStartDate());
            }
        };
        columns[4].setMinimumColumnWidth(100);

        columns[5] = new ColumnDefinitionConfig<ScheduledCourseItem, HTML>(tcStrings.venue(), ScheduledCourseItem.LOCATION, 100) {
            @Override
            public HTML getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getLocationName() != null ? new HTML(rowValue.getLocationName()) : null;
            }
        };
        columns[5].setMinimumColumnWidth(100);
        columns[5].setColumnSortable(false);

        columns[6] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.courseRequirements(), ScheduledCourseItem.COURSE_REQUIREMENT, 100) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getCourseRequirementsAsString() != null ? rowValue.getCourseRequirementsAsString() : wfmStrings.notAvailable();
            }
        };
        columns[6].setMinimumColumnWidth(100);

        columns[7] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(wfmStrings.language(), ScheduledCourseItem.LANGUAGE, 100) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getLanguageName();
            }
        };
        columns[7].setMinimumColumnWidth(100);

        columns[8] = new ColumnDefinitionConfig<ScheduledCourseItem, String>(tcStrings.testOption(), ScheduledCourseItem.TEST_OPTION, 100) {
            @Override
            public String getCellValue(ScheduledCourseItem rowValue) {
                return rowValue.getTestOption();
            }

            @Override
            public void setCellValue(ScheduledCourseItem rowValue, String cellValue) {
                rowValue.setTestOption(cellValue);
                saveCellValue(rowValue);
            }
        };
        columns[8].setMinimumColumnWidth(100);

        columns[9] = new ColumnDefinitionConfig<ScheduledCourseItem, Button>(tcStrings.csvDownloadable(), ScheduledCourseItem.CSV_DOWNLOADABLE, 100) {
            @Override
            public Button getCellValue(final ScheduledCourseItem rowValue) {
                Button exportToCSV = new Button(wfmStrings.export());
                exportToCSV.addClickHandler(event -> {
                    String csvURL = CommandConstants.COMMON_URL + "/downloadCourseUserCSVHandler";
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setScheduledCourseID(rowValue.getObjectID());

                    Utils.sendCSVRequest(pnlCRContainer, csvURL, filterParameter.getRequestParams(), "_blank");
                });
                return exportToCSV;
            }
        };
        columns[9].setMinimumColumnWidth(100);

        initCellEdit(columns);
        return columns;
    }

    private void initCellEdit(ColumnDefinitionConfig[] columnConfigs) {
        if (Utils.hasPermission(TC_TEST_OPTION)) {
            final TextBoxCellEditor<String> testOption = new TextBoxCellEditor<String>() {
                @Override
                protected String getValue() {
                    return getText();
                }

                @Override
                protected void setValue(String cellValue) {
                    setText(cellValue);
                }
            };
            columnConfigs[8].setCellEditor(testOption);
            columnConfigs[8].setCellChangesSave(new CellChange<ScheduledCourseItem>() {
                @Override
                public void saveCell(ScheduledCourseItem rowValue, String columnCodeName) {
                    rowValue.setTestOption(testOption.getText());
                    saveCSCEditCellValue(rowValue, columnCodeName);
                }
            });
        }
    }

    private void saveCSCEditCellValue(ScheduledCourseItem rowValue, String columnCodeName) {
        TCService.App.get().saveCSCEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
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
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(tcStrings.courseSchedules().toLowerCase()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    protected ListingRequestProvider<ScheduledCourseItem> getProvider() {
        return (filterParametrs, callback) -> {
            TCService.App.get().getConfirmedScheduledCourseList(filterParametrs, new AsyncCallback<ListResult<ScheduledCourseItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<ScheduledCourseItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
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
    @Override
    public String getPropertyCode() {
        return "confirmedscheduledcourses";
    }
}
