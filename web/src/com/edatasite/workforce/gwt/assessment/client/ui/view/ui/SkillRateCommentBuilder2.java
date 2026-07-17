package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 3/18/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class SkillRateCommentBuilder2 extends Composite implements Constants {


    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static String CLIENT = Property.get(Constants.CLIENT_LIST, wfmStrings.customer());
    public static String EMPLOYEE = hrmsStrings.employeesSelfReview();
    public static String MANAGER = wfmStrings.manager();
    public static String PEER = hrmsStrings.peer();

    private String comment;
    private String commenterName;
    private String commenterType;
    private String commenterTeam;
    private String overallStatus;
    private boolean ratable = true;
    private Double rate;
    private String grade;
    private boolean reviewOnly = false;
    private Double weight;
    private boolean isFromShift = false;

    //UI fields
    @UiField
    HTMLPanel commentPanelField;
    @UiField
    HTMLPanel ratePanelField;
    @UiField
    HTMLPanel ui;
    private AppraisalsSettingsItem settingsItem;


    interface SkillRateCommentBuilder2UiBinder extends UiBinder<HTMLPanel, SkillRateCommentBuilder2> {
    }


    public SkillRateCommentBuilder2() {
        SkillRateCommentBuilder2UiBinder ourUiBinder = GWT.create(SkillRateCommentBuilder2UiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    /**
     * Create constructor with params
     *
     * @param comment       - comment for skill given by commenter
     * @param commenterName - commenter name
     * @param commenterType - commenter type (PEER, CLIENT, MANAGER, EMPLOYEE'S SELF REVIEW)
     * @param commenterTeam - commenter department
     * @param grade          - grade given by commenter
     * @param weight        - weight
     * @param status        - assessment current overall status
     * @param ratable       - is this skill ratable or not
     */
    public SkillRateCommentBuilder2(String comment, String commenterName, String commenterType, String commenterTeam, String grade, Double weight, String status, boolean ratable, AppraisalsSettingsItem settingsItem,boolean isFromShift) {
        this();
        this.comment = comment;
        this.commenterName = commenterName;
        this.commenterType = commenterType;
        this.commenterTeam = commenterTeam;
        this.grade = grade;
        this.weight = weight;
        this.overallStatus = status;
        this.ratable = ratable;
        this.settingsItem = settingsItem;
        this.isFromShift = isFromShift;
        drawInitialize();
    }

    /**
     * Create constructor with params
     *
     * @param comment       - comment for skill given by commenter
     * @param commenterName - commenter name
     * @param commenterType - commenter type (PEER, CLIENT, MANAGER, EMPLOYEE'S SELF REVIEW)
     * @param commenterTeam - commenter department
     * @param grade         - grade given by commenter
     * @param weight        - weight
     * @param status        - assessment current overall status
     * @param ratable       - is this skill ratable or not
     * @param reviewOnly    - review only option
     */
    public SkillRateCommentBuilder2(String comment, String commenterName, String commenterType, String commenterTeam, String grade, Double weight, String status, boolean ratable, boolean reviewOnly, AppraisalsSettingsItem settingsItem,boolean isFromShift) {
        this();
        this.comment = comment;
        this.commenterName = commenterName;
        this.commenterType = commenterType;
        this.commenterTeam = commenterTeam;
        this.grade = grade;
        this.weight = weight;
        this.overallStatus = status;
        this.ratable = ratable;
        this.reviewOnly = reviewOnly;
        this.settingsItem = settingsItem;
        this.isFromShift = isFromShift;
        drawInitialize();
    }


    private void drawInitialize() {
        if (ratable) {
            drawInitRate();
        }
        if (!isFromShift) {
            drawInitComment();
        }
    }

    /**
     * Builds comment panel
     */
    private void drawInitComment() {
        commentPanelField.add(new HTML(getCustomTITLE(commenterType)));
        SkillComment2 skillComment;
        if (!INITIATED.equals(overallStatus)) {
            skillComment = new SkillComment2(commenterName, commenterTeam, comment);
        } else {
            skillComment = new SkillComment2(commenterName, commenterTeam, "");
        }
        commentPanelField.add(skillComment);
    }

    /**
     * Builds rate gradient chart
     */
    private void drawInitRate() {
        if (overallStatus != null && !INITIATED.equals(overallStatus) && grade != null) {
            ratePanelField.add(new HTML(AssessmentHelper.getCustomTITLE(AssessmentHelper.getGradeAsString(grade))));
            TextBox gradeBox = new TextBox();
            gradeBox.setText(grade);
            gradeBox.setEnabled(false);
            ratePanelField.add(gradeBox);
        } else {
            ratePanelField.add(new HTML(getCustomTITLE(hrmsStrings.notRated())));
        }
    }

    private String getCustomTITLE(String text) {
        return "<b class=customTitle>" + text + "</b>";
    }
}