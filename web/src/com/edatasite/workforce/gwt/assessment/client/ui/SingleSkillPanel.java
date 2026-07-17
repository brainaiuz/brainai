package com.edatasite.workforce.gwt.assessment.client.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.ArrayList;

/**
 * Class Intended for rendering UI in PA review pages.
 * It builds html component. If it Review page reviewing by Reviewer
 * it shows all rate and comments given by permitted collaborators PEERs, CLIENTs, MANAGERs, and EMPLOYEE HIMSELF.
 * it also provides rate buttons as radiobuttons if skill is rateable and TextArea for comments.
 */

@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection"})
public class SingleSkillPanel extends FlowPanel implements Constants {

    private String comment;
    private String employeeName;
    private boolean isWeightable;
    private double overalRate;
    private String managerName;
    private Double rate;
    private boolean rateable = true;
    private ArrayList<Double> rates;
    private boolean reviewOnly;
    private AssessmentRatingsComments skill;
    private String skillDescription;
    private Integer skillID;
    private String skillName;
    private Integer skillRatingID;
    private String status;
    private String team;
    private boolean turn;
    private boolean userIsEmployee;
    private boolean userIsManager;
    private Double weight;
    private EditableSkillRateCommentBuilder yourComment;

    /**
     * Creates a new SingleSkill panel.(especially for Reviewer review)
     *
     * @param allSkills AssessmentSkills list
     * @param skill     AssessmentRatingsComment comments, ratings for this skill
     */
    public SingleSkillPanel(AssessmentSkills allSkills, AssessmentRatingsComments skill) {
        this();

        this.status = allSkills.getStatus();
        this.skill = skill;
        this.skillRatingID = skill.getKeySkillRatingId();
        this.skillID = skill.getKeySkillRatingId();
        this.skillName = skill.getSkillName();
        this.skillDescription = skill.getSkillDescription();
        this.employeeName = allSkills.getEmployeeName();
        this.managerName = allSkills.getManagerName();
        this.rateable = skill.getRateable() == null ? true : skill.getRateable();
        if (rateable) {
            overalRate = skill.getCalculatedAverage();
        }
        this.turn = allSkills.isTurn();
        if (allSkills.isTurn() == MANAGER_TURN) {
            comment = skill.getManagerComment();
            rate = skill.getManagerRating();
        } else {
            comment = skill.getEmployeeComment();
            rate = skill.getEmployeeRating();
        }
        this.team = allSkills.getTeam();
        if ((status != null) && (!status.equals(INITIATED))) {
            if (skill.getManagerRating() != null && skill.getManagerRating() != 0d) {
                rates.add(skill.getManagerRating());
            }
        }

        initHeader();
        initRatingComments();
    }


    public SingleSkillPanel(AssessmentSkills allSkills, AssessmentRatingsComments skill, boolean reviewOnly) {
        this();
        this.reviewOnly = reviewOnly;
        this.status = allSkills.getStatus();
        this.skill = skill;
        this.skillRatingID = skill.getKeySkillRatingId();
        this.skillName = skill.getSkillName();
        this.skillDescription = skill.getSkillDescription();
        this.employeeName = allSkills.getEmployeeName();
        this.managerName = allSkills.getManagerName();
        this.rateable = skill.getRateable() == null ? true : skill.getRateable();
        if (rateable) {
            overalRate = skill.getCalculatedAverage();
        }
        this.turn = allSkills.isTurn();
        if (allSkills.isTurn() == MANAGER_TURN) {
            comment = skill.getManagerComment();
            rate = skill.getManagerRating();
        } else {
            comment = skill.getEmployeeComment();
            rate = skill.getEmployeeRating();
        }
        this.team = allSkills.getTeam();
        if ((status != null) && (!status.equals(INITIATED))) {
            if (skill.getManagerRating() != null && skill.getManagerRating().intValue() != 0) {
                rates.add(skill.getManagerRating());
            }
        }

        initHeader();
        initRatingComments();
    }


    /**
     * Creates a new SingleSkill panel.(especially for collaborators review)
     *
     * @param allSkills SkillAssessmentElemsStruct list
     * @param skill     SkillAssessmentElem comments, ratings for this skill
     */
    public SingleSkillPanel(SkillAssessmentElemsStruct allSkills, SkillAssessmentElem skill) {
        this();

        this.skillName = skill.getSkillName();
        this.skillDescription = skill.getSkillDescription();
        this.skillRatingID = skill.getSkillRatingId();
        this.skillID = skill.getSkillId();
        this.status = allSkills.getStatus();
        this.rateable = skill.isShowRadio() == null ? true : skill.isShowRadio();
        if (allSkills.isTurn() == MANAGER_TURN) {
            this.comment = skill.getReviewersComment();
            this.rate = skill.getRaiting();
        } else {
            this.comment = skill.getEmployeesComment();
            this.rate = skill.getEmployeeRating();
        }
        initHeader();
        initYourComment();
    }

    public SingleSkillPanel(SkillAssessmentElemsStruct allSkills, SkillAssessmentElem skill, boolean reviewOnly) {
        this();
        this.reviewOnly = reviewOnly;
        this.skillName = skill.getSkillName();
        this.skillDescription = skill.getSkillDescription();
        this.skillRatingID = skill.getSkillRatingId();
        this.skillID = skill.getSkillId();
        this.status = allSkills.getStatus();
        this.rateable = skill.isShowRadio() == null ? true : skill.isShowRadio();
        if (allSkills.isTurn() == MANAGER_TURN) {
            this.comment = skill.getReviewersComment();
            this.rate = skill.getRaiting();
        } else {
            this.comment = skill.getEmployeesComment();
            this.rate = skill.getEmployeeRating();
        }
        initHeader();
        initYourComment();
    }

    /**
     * Creates a new advancedSingleSkill panel for Simple Assessment
     *
     * @param allSkills
     * @param skill
     * @param isManager
     * @param userIsEmployee
     */
    public SingleSkillPanel(SkillAssessmentElemsStruct allSkills, SkillAssessmentElem skill,
                            boolean isManager, boolean userIsEmployee) {
        this();

        this.skillName = skill.getSkillName();
        this.skillDescription = skill.getSkillDescription();
        this.skillRatingID = skill.getSkillRatingId();
        this.skillID = skill.getSkillId();
        this.status = allSkills.getStatus();
        this.rateable = skill.isShowRadio() == null ? true : skill.isShowRadio();
        this.turn = allSkills.isTurn();
        this.managerName = allSkills.getReviewerName();
        this.employeeName = allSkills.getEmployeeName();
        this.team = allSkills.getDepartmentName();
        this.rate = skill.getRaiting();

        this.userIsEmployee = userIsEmployee;
        this.userIsManager = isManager;

        initHeader();
        initEmployeeOrManagerComment(skill);
        initEmployeeOrManagerRate(skill);
    }

    public SingleSkillPanel(SkillAssessmentElemsStruct allSkills, SkillAssessmentElem skill,
                            boolean isManager, boolean userIsEmployee, boolean reviewOnly) {
        this();

        this.reviewOnly = reviewOnly;

        this.skillName = skill.getSkillName();
        this.skillDescription = skill.getSkillDescription();
        this.skillRatingID = skill.getSkillRatingId();
        this.skillID = skill.getSkillId();
        this.status = allSkills.getStatus();
        this.rateable = skill.isShowRadio() == null ? true : skill.isShowRadio();
        this.turn = allSkills.isTurn();
        this.weight = skill.getWeight();
        this.managerName = allSkills.getReviewerName();
        this.employeeName = allSkills.getEmployeeName();
        this.team = allSkills.getDepartmentName();
        this.rate = skill.getRaiting();

        this.userIsEmployee = userIsEmployee;
        this.userIsManager = isManager;

        initHeader();
        initEmployeeOrManagerComment(skill);
        initEmployeeOrManagerRate(skill);
    }

    public SingleSkillPanel(SkillAssessmentElemsStruct allSkills, SkillAssessmentElem skill,
                            boolean isManager, boolean userIsEmployee, boolean reviewOnly, boolean isWeightable) {
        this();

        this.reviewOnly = reviewOnly;

        this.skillName = skill.getSkillName();
        this.skillDescription = skill.getSkillDescription();
        this.skillRatingID = skill.getSkillRatingId();
        this.skillID = skill.getSkillId();
        this.status = allSkills.getStatus();
        this.rateable = skill.isShowRadio() == null ? true : skill.isShowRadio();
        this.turn = allSkills.isTurn();
        if (isWeightable) {
            this.weight = skill.getWeight();
            this.isWeightable = isWeightable;
        }
        this.managerName = allSkills.getReviewerName();
        this.employeeName = allSkills.getEmployeeName();
        this.team = allSkills.getDepartmentName();
        this.rate = skill.getRaiting();

        this.userIsEmployee = userIsEmployee;
        this.userIsManager = isManager;

        initHeader();
        initEmployeeOrManagerComment(skill);
        initEmployeeOrManagerRate(skill);
    }

    private SingleSkillPanel() {
        super();
        setStyleName("cp-header-color");
//        setTitleCollapse(true);
//        setBorders(true);
        rates = new ArrayList<>();
    }

    private void initHeader() {
        String header = "<table cellpadding=\"0\" width=\"100%\" border=0><tr><td width=\"670\"><b class=customTitle>" + skillName + "</b>";
        if (skillDescription != null) {
            header += "<br>" + skillDescription;
        }
        if (isWeightable) {
            header += "</td><td width=\"80\" align=\"right\"><table><tr><td valign=\"middle\">" + "<b class=customTitle> Weight: </b></td><td><b class=customTitle><font style=\"font-size:15px; font-weight:bold;\">" + weight + "</font></b></td></tr></table>";
        }
        header += "</td></tr></table>";
        getElement().setInnerText(header);
        setWidth("750px");
    }

    private void initRatingComments() {
        if (userIsEmployee && userIsManager && reviewOnly) {
            initManagerRateComment();
        } else if (turn == MANAGER_TURN) {
            initEmployeeRateComment();
        } else {
            initManagerRateComment();
        }
        for (int i = 0; i < skill.getClients().length; i++) {
            initClientRateComment(skill.getClients()[i]);
        }
        for (int i = 0; i < skill.getManagers().length; i++) {
            initManagersRateComment(skill.getManagers()[i]);
        }

        for (int i = 0; i < skill.getPeers().length; i++) {
            initPeerRateComment(skill.getPeers()[i]);
        }

        initYourComment();
    }

    private void initClientRateComment(RatingComment comment) {
        if (!comment.getStatus().equals(INITIATED)) {
            if (comment.getRating() != null) {
                rates.add(comment.getRating());
            }
        }
        SkillRateCommentBuilder builder = new SkillRateCommentBuilder(SkillRateCommentBuilder.CLIENT, comment, reviewOnly);
        add(builder);
    }

    private void initPeerRateComment(RatingComment comment) {
        if ((!comment.getStatus().equals(INITIATED))) {
            if (comment.getRating() != null) {
                rates.add(comment.getRating());
            }
        }
        SkillRateCommentBuilder builder = new SkillRateCommentBuilder(SkillRateCommentBuilder.PEER, comment, reviewOnly);
        add(builder);
    }

    private void initManagersRateComment(RatingComment comment) {
        if ((!comment.getStatus().equals(INITIATED)) && (comment.getRating() != null)) {
            rates.add(comment.getRating());
        }
        SkillRateCommentBuilder builder = new SkillRateCommentBuilder(SkillRateCommentBuilder.MANAGER, comment, reviewOnly);
        add(builder);
    }

    private void initEmployeeRateComment() {
        String employeeComment = skill.getEmployeeComment();//==null?"":skill.getEmployeeComment();
        Double rate = skill.getEmployeeRating();//!=null?skill.getEmployeeRating().intValue():1);
        SkillRateCommentBuilder builder = new SkillRateCommentBuilder(employeeComment, employeeName, SkillRateCommentBuilder.EMPLOYEE, team, rate, weight, status, rateable, reviewOnly);
        add(builder);
    }

    private void initManagerRateComment() {
        String managerComment = skill.getManagerComment();//==null?"":skill.getEmployeeComment();
        Double rate = skill.getManagerRating();//!=null?skill.getEmployeeRating().intValue():1);
        SkillRateCommentBuilder builder = new SkillRateCommentBuilder(managerComment, managerName, SkillRateCommentBuilder.MANAGER, team, rate, weight, status, rateable, reviewOnly);
        add(builder);
    }

    public void setShowGradePicker(boolean isShow) {
        if (yourComment != null) {
            yourComment.showRatePicker(isShow);
        }
    }

    private void initEmployeeOrManagerComment(SkillAssessmentElem skill) {
        SkillRateCommentBuilder builder = null;
        if (userIsEmployee && userIsManager && reviewOnly) {
            if (!status.equals(INITIATED)) {
                if (!(skill.getEmployeesComment() == null)) {
                    builder = new SkillRateCommentBuilder(skill.getEmployeesComment(), employeeName, "Employee's Comments", team, rate, weight, status, rateable, reviewOnly);
                }
            }
        } else if (userIsEmployee) {
            if (!status.equals(INITIATED)) {
                if (!(skill.getReviewersComment() == null && !rateable)) {
                    if (!reviewOnly) {
                        builder = new SkillRateCommentBuilder(skill.getReviewersComment(), managerName, "Manager's Comments", team, rate, weight, status, rateable);
                    }
                }
            }
        } else {
            if (!status.equals(INITIATED)) {
                if (skill.getEmployeesComment() != null) {
                    builder = new SkillRateCommentBuilder(skill.getEmployeesComment(), employeeName, "Employee's Comments", team, rate, weight, status, false, reviewOnly);
                }
            }

        }
        if (builder != null) {
            add(builder);
        }
    }

    private void initEmployeeOrManagerRate(SkillAssessmentElem assessmentElem) {
        if (userIsEmployee && userIsManager && reviewOnly) {
            if (status.equals(APPROVED_BY_MANAGER) || reviewOnly) {
                yourComment = new EditableSkillRateCommentBuilder(skillRatingID, assessmentElem.getReviewersComment(), rate, weight, true, true, skillID);
                yourComment.showRatePicker(rateable);
                yourComment.setEnabledComment(true);
            } else {
                yourComment = new EditableSkillRateCommentBuilder(skillRatingID, assessmentElem.getReviewersComment(), rate, weight, true, skillID);
                yourComment.showRatePicker(rateable);
                yourComment.setEnabledComment(reviewOnly);
            }
        } else if (userIsEmployee) {
            if (status.equals(APPROVED_BY_MANAGER) || reviewOnly) {
                yourComment = new EditableSkillRateCommentBuilder(skillRatingID, assessmentElem.getEmployeesComment(), 0d, weight, true, skillID);
                yourComment.showRatePicker(false);
                yourComment.setEnabledComment(true);
            } else {
                yourComment = new EditableSkillRateCommentBuilder(skillRatingID, assessmentElem.getEmployeesComment(), 0d, weight, true, skillID);
                yourComment.showRatePicker(false);
                yourComment.setEnabledComment(reviewOnly);
            }
        } else {
            if (status.equals(APPROVED_BY_MANAGER) || reviewOnly) {
                yourComment = new EditableSkillRateCommentBuilder(skillRatingID, assessmentElem.getReviewersComment(), rate, weight, true, true, skillID);
                yourComment.showRatePicker(rateable);
                yourComment.setEnabledComment(true);
            } else {
                yourComment = new EditableSkillRateCommentBuilder(skillRatingID, assessmentElem.getReviewersComment(), rate, weight, true, skillID);
                yourComment.showRatePicker(rateable);
                yourComment.setEnabledComment(reviewOnly);
            }
        }

        add(yourComment);
    }

    private void initYourComment() {
        boolean gradePickerStatic = (status.equals(APPROVED_BY_MANAGER) || this.reviewOnly);

        if (rates != null) {
            yourComment = new EditableSkillRateCommentBuilder(skillRatingID, comment, rate, rateable, overalRate, gradePickerStatic, skillID);
        } else {
            yourComment = new EditableSkillRateCommentBuilder(skillRatingID, comment, rate, rateable, gradePickerStatic, skillID);
        }
        add(yourComment);
    }

    /*
   * Returns comment as String for giving skill
    */

    public String getYourComment() {
        return yourComment.getComment();
    }

    /*
   * Returns rate as Integer for giving skill
    */

    public Double getYourRate() {
        return yourComment.getRate();
    }

    /*
   * Returns SkillID
    */

    public Integer getSkillRatingID() {
        return skillRatingID;
    }

    public boolean isRateable() {
        return rateable;
    }

    public void setRateable(boolean rateable) {
        this.rateable = rateable;
    }
}
