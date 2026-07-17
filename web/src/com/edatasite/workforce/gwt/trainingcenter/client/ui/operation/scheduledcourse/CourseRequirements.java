package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.CourseRequirementItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseReservation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/13/12
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseRequirements extends Composite {

    private FlowPanel pnlContainer;
    private WfmForm table;
    private ScheduledCourseItem scheduledCourse;
    private CourseRequirementItem[] courseRequirementItems;

    public CourseRequirements(ScheduledCourseItem item) {
        this.scheduledCourse = item;
        initialize();
    }

    private void initialize() {
        pnlContainer = new FlowPanel();
        loadCourseRequirements();
        initWidget(pnlContainer);
    }

    private void loadCourseRequirements() {
        courseRequirementItems = scheduledCourse.getCourseRequirementItems();
        initCourseRequirements();
    }

    private void initCourseRequirements() {
        if (courseRequirementItems != null && courseRequirementItems.length > 0) {
            table = new WfmForm(new String[]{"5%", "1%", "40%"});

            for (CourseRequirementItem requirementItem : courseRequirementItems) {
                ScheduledCourseReservation reservation = scheduledCourse.getReservation(requirementItem.getObjectID());
                DataListBox dwRequirement = new DataListBox();
                dwRequirement.setLayoutData(requirementItem.getObjectID());
                dwRequirement.setItems(requirementItem.getItems());
                dwRequirement.setSelected(reservation != null ? reservation.getItemID() : null);
                if (requirementItem.getItems() == null || requirementItem.getItems().length == 0) {
                    dwRequirement.addStyleName("red-border");
                }
                table.addField(requirementItem.getName(), dwRequirement, false);
            }

            pnlContainer.clear();
            pnlContainer.add(table);
        }
    }

    public ScheduledCourseReservation[] getObjectData() {
        if (table != null) {
            List<ScheduledCourseReservation> scheduledCourseReservationList = new ArrayList<>();
            List fields = table.getFields();

            int i = 0;
            for (CourseRequirementItem requirementItem : courseRequirementItems) {
                ScheduledCourseReservation reservation = new ScheduledCourseReservation();

                WfmForm.Field field = (WfmForm.Field) fields.get(i);
                DataListBox dwRequirement = (DataListBox) field.getControl();
                reservation.setItemCategoryID((Integer)dwRequirement.getLayoutData());
                reservation.setItemID(dwRequirement.getSelectedId());
                scheduledCourseReservationList.add(reservation);

                i++;
            }

            return scheduledCourseReservationList.toArray(new ScheduledCourseReservation[]{});
        }

        return null;
    }

    public boolean validation() {
        int errors = 0;
        if (table != null) {
            List<ScheduledCourseReservation> scheduledCourseReservationList = new ArrayList<>();
            List fields = table.getFields();

            int i = 0;
            for (CourseRequirementItem requirementItem : courseRequirementItems) {
                ScheduledCourseReservation reservation = new ScheduledCourseReservation();

                WfmForm.Field field = (WfmForm.Field) fields.get(i);
                DataListBox dwRequirement = (DataListBox) field.getControl();

                if (!Validation.validateListBoxRequired(dwRequirement, field, "This field is required.")) {
                    errors++;
                }

                i++;
            }

            return errors == 0;
        }

        return true;
    }
}
