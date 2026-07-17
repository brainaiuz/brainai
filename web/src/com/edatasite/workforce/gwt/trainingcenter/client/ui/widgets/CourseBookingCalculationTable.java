package com.edatasite.workforce.gwt.trainingcenter.client.ui.widgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingCalculationItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseScheduleListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 9/13/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingCalculationTable extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();

    private KpiDataGrid<CourseBookingCalculationItem> calculationTable;
    private ListDataProvider<CourseBookingCalculationItem> calculationTableDataProvider;

    private final Map<Integer, ScheduledCourseItem> courseScheduleMap;
    private Map<Integer, CourseBookingCalculationItem> calculationItemMap;

    private VerticalPanel pnlContainer;
    private HTML pnlTotal;

    public static final ProvidesKey<CourseBookingCalculationItem> KEY_PROVIDER_CALCULATION_TABLE = item -> item == null ? null : item.getObjectID();

    public CourseBookingCalculationTable() {
        courseScheduleMap = new HashMap<>();
        initialize();
    }

    private void initialize() {
        pnlContainer = new VerticalPanel();
        pnlContainer.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        pnlContainer.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
//        pnlContainer.addStyleName("inner");
        pnlContainer.setSpacing(15);
        pnlContainer.setSize("100%", "80px");

        calculationTable = new KpiDataGrid<>(KEY_PROVIDER_CALCULATION_TABLE);
//        calculationTable.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));
        calculationTable.setSize("100%", "150px");
        initColumnConfig();
        pnlContainer.add(calculationTable);

        calculationTableDataProvider = new ListDataProvider<>();
        calculationTableDataProvider.addDataDisplay(calculationTable);

//        pnlTotal = new HTML();
//        pnlContainer.add(pnlTotal);
        initWidget(pnlContainer);
    }

    public void calculation(List<StudentItem> items) {
        calculationItemMap = new HashMap<>();

        if (items != null && items.size() > 0) {
            for (StudentItem item : items) {
                if (item.getStudentCourseBookingItems() != null && item.getStudentCourseBookingItems().size() > 0) {
                    for (CourseScheduleListItem courseScheduleListItem : item.getStudentCourseBookingItems()) {
                        if (courseScheduleListItem.getCourseScheduleId() != null) {
                            if (courseScheduleMap.containsKey(courseScheduleListItem.getCourseScheduleId())) {
                                initCalculationItem(courseScheduleMap.get(courseScheduleListItem.getCourseScheduleId()));
                            } else {
                                LoadingPanel.loading(true);
                                TCService.App.get().getCourseSchedule(courseScheduleListItem.getCourseScheduleId(), true, new AsyncCallback<ScheduledCourseItem>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                        caught.printStackTrace();
                                    }

                                    @Override
                                    public void onSuccess(ScheduledCourseItem result) {
                                        LoadingPanel.loading(false);
                                        courseScheduleMap.put(result.getObjectID(), result);
                                        initCalculationItem(result);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    private void initCalculationItem(ScheduledCourseItem scheduledCourseItem) {
        if (calculationItemMap.containsKey(scheduledCourseItem.getObjectID())) {
            CourseBookingCalculationItem courseBookingCalculationItem = calculationItemMap.get(scheduledCourseItem.getObjectID());
            courseBookingCalculationItem.setQty(courseBookingCalculationItem.getQty().add(BigDecimal.ONE));
            courseBookingCalculationItem.setTotal(courseBookingCalculationItem.getPrice().add(courseBookingCalculationItem.getStopFee()).multiply(courseBookingCalculationItem.getQty()).setScale(2, RoundingMode.HALF_UP));
            calculationItemMap.put(courseBookingCalculationItem.getObjectID(), courseBookingCalculationItem);
        } else {
            CourseBookingCalculationItem courseBookingCalculationItem = new CourseBookingCalculationItem();
            courseBookingCalculationItem.setObjectID(scheduledCourseItem.getObjectID());
            courseBookingCalculationItem.setCourse(scheduledCourseItem.getCourseName());
            courseBookingCalculationItem.setLocation(scheduledCourseItem.getLocationName());
            courseBookingCalculationItem.setQty(BigDecimal.ONE);
            courseBookingCalculationItem.setPrice(scheduledCourseItem.getPrice());
            courseBookingCalculationItem.setStopFee(scheduledCourseItem.getStopFee());
            courseBookingCalculationItem.setTotal(scheduledCourseItem.getPrice().add(scheduledCourseItem.getStopFee()));
            calculationItemMap.put(courseBookingCalculationItem.getObjectID(), courseBookingCalculationItem);
        }

        drawCalculatedItemTable();
    }

    private void drawCalculatedItemTable() {
        calculationTableDataProvider.getList().clear();
        calculationTableDataProvider.getList().addAll(calculationItemMap.values());
        calculationTableDataProvider.refresh();

//        BigDecimal total = BigDecimal.ZERO;
//        for (CourseBookingCalculationItem calculationItem : calculationItemMap.values()) {
//            total = total.add(calculationItem.getPrice());
//        }
//
//        pnlTotal.setText(Utils.getNumberFormat().format(total));
    }

    private void initColumnConfig() {
        //course
        calculationTable.addColumn(new Column<CourseBookingCalculationItem, String>(new TextCell()) {
            @Override
            public String getValue(CourseBookingCalculationItem object) {
                return object.getCourse();
            }
        }, wfmStrings.course());
        calculationTable.setColumnWidth(calculationTable.getColumn(0), "180px");

        //price per student
        calculationTable.addColumn(new Column<CourseBookingCalculationItem, String>(new TextCell()) {
            @Override
            public String getValue(CourseBookingCalculationItem object) {
                return Utils.getNumberFormat().format(object.getPrice());
            }
        }, wfmStrings.pricePerStudent());
        calculationTable.setColumnWidth(calculationTable.getColumn(1), "80px");

        //stop fee per student
        calculationTable.addColumn(new Column<CourseBookingCalculationItem, String>(new TextCell()) {
            @Override
            public String getValue(CourseBookingCalculationItem object) {
                return Utils.getNumberFormat().format(object.getStopFee());
            }
        }, tcStrings.stopFee());
        calculationTable.setColumnWidth(calculationTable.getColumn(2), "60px");

        //qty of student
        calculationTable.addColumn(new Column<CourseBookingCalculationItem, String>(new TextCell()) {
            @Override
            public String getValue(CourseBookingCalculationItem object) {
                return String.valueOf(object.getQty().intValue());
            }
        }, wfmStrings.qty());
        calculationTable.setColumnWidth(calculationTable.getColumn(3), "50px");

        //total value
        calculationTable.addColumn(new Column<CourseBookingCalculationItem, String>(new TextCell()) {
            @Override
            public String getValue(CourseBookingCalculationItem object) {
                return Utils.getNumberFormat().format(object.getTotal());
            }
        }, wfmStrings.total());
        calculationTable.setColumnWidth(calculationTable.getColumn(4), "60px");
    }
}
