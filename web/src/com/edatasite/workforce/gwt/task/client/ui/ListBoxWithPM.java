package com.edatasite.workforce.gwt.task.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Apr 2, 2011
 * Time: 2:49:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListBoxWithPM extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DataListBox dwListBox;
    private KpiCheckBox rbUpdateForAll;
    private KpiCheckBox rbUpdateForAssignment;

    private Boolean updateForAll = false;
    private Boolean updateForAssignment = false;

    public ListBoxWithPM() {
        this(false, false);
    }

    public ListBoxWithPM(Boolean updateForAll, Boolean updateForAssignment) {
        this.updateForAll = updateForAll;
        this.updateForAssignment = updateForAssignment;

        initialize();
    }

    private void initialize() {
        rbUpdateForAssignment = new KpiCheckBox(wfmStrings.updateTaskForAssign());

        rbUpdateForAll = new KpiCheckBox(wfmStrings.updateTaskForAll());

        dwListBox = new DataListBox();

        VerticalPanel pnlWrap = new VerticalPanel();
        pnlWrap.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        pnlWrap.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        pnlWrap.add(dwListBox);

        VerticalPanel pnlRBWrap = new VerticalPanel();
        pnlRBWrap.setSpacing(5);
        pnlRBWrap.add(rbUpdateForAssignment);
        pnlRBWrap.add(rbUpdateForAll);
//        pnlWrap.add(pnlRBWrap);

        initInternal();

        initWidget(pnlWrap);
    }

    public void initInternal() {
        rbUpdateForAll.setVisible(true);
        rbUpdateForAssignment.setVisible(true);
        
        rbUpdateForAll.setEnabled(true);
        rbUpdateForAssignment.setEnabled(true);

        if (updateForAll && updateForAssignment) {
            rbUpdateForAll.setEnabled(true);
            rbUpdateForAssignment.setEnabled(true);

            rbUpdateForAll.setValue(true);
        } else if (updateForAll && !updateForAssignment) {
            rbUpdateForAll.setEnabled(true);
            rbUpdateForAssignment.setEnabled(false);

            rbUpdateForAll.setValue(true);
        } else if (!updateForAll && updateForAssignment) {
            rbUpdateForAll.setVisible(false);
            rbUpdateForAssignment.setVisible(false);

            rbUpdateForAssignment.setValue(true);
        }
    }

    public DataListBox getListBox() {
        return dwListBox;
    }

    public Boolean getUpdateForAll() {
        return rbUpdateForAll.getValue();
    }

    public void setUpdateForAll(Boolean updateForAll) {
        this.updateForAll = updateForAll;
    }

    public Boolean getUpdateForAssignment() {
        return rbUpdateForAssignment.getValue();
    }

    public void setUpdateForAssignment(Boolean updateForAssignment) {
        this.updateForAssignment = updateForAssignment;
    }

    public void addValueChangeEvent(ValueChangeHandler handler) {
        rbUpdateForAll.addValueChangeHandler(handler);
        rbUpdateForAssignment.addValueChangeHandler(handler);
    }
}
