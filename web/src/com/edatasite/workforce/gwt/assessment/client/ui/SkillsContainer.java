package com.edatasite.workforce.gwt.assessment.client.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentRatingsComments;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentSkills;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

public class SkillsContainer extends Composite implements Constants {

    private HorizontalPanel buttonPanel = new HorizontalPanel();
    private VerticalPanel panel = new VerticalPanel();
    private boolean reviewOnly = false;
    private AssessmentSkills skill;
    private ArrayList<SingleSkillPanel> skills = new ArrayList<>();
    private SkillAssessmentElemsStruct skillsStruct;
    private boolean turn;

    public SkillsContainer(boolean reviewOnly) {
        this();
        this.reviewOnly = reviewOnly;
    }

    public SkillsContainer() {
        buttonPanel.setSpacing(5);

        FlexTable body = new FlexTable();
        body.setWidget(0, 0, panel);
        body.setWidget(1, 0, buttonPanel);
        initWidget(body);
    }

    public void addHeader(Widget header) {
        panel.add(header);

    }

    public void clear() {
        panel.clear();
        buttonPanel.clear();
    }

    public void init(AssessmentSkills skills) {
        this.skill = skills;
        this.turn = skills.isTurn();
        if (skills.getRatingsComments() != null) {
            for (int i = 0; i < skill.getRatingsComments().length; i++) {
                initAssessmentRatingsComments(skill.getRatingsComments()[i]);
            }
        }
    }

    public void init(AssessmentSkills skills, boolean reviewOnly) {
        this.reviewOnly = reviewOnly;
        this.skill = skills;
        this.turn = skills.isTurn();
        if (skills.getRatingsComments() != null) {
            for (int i = 0; i < skill.getRatingsComments().length; i++) {
                initAssessmentRatingsComments(skill.getRatingsComments()[i]);
            }
        }
    }

    public void init(SkillAssessmentElemsStruct skills) {
        this.skillsStruct = skills;
        this.turn = skills.isTurn();
        if (skillsStruct.getElems() != null) {
            for (int i = 0; i < skillsStruct.getElems().length; i++) {
                initSkillAssessmentRatingsComments(skillsStruct.getElems()[i]);
            }
        }
    }

    public void init(SkillAssessmentElemsStruct skills, boolean reviewOnly) {
        if (reviewOnly) {
            this.reviewOnly = reviewOnly;
        }
        this.skillsStruct = skills;
        this.turn = skills.isTurn();
        if (skillsStruct.getElems() != null) {
            for (int i = 0; i < skillsStruct.getElems().length; i++) {
                initSkillAssessmentRatingsComments(skillsStruct.getElems()[i]);
            }
        }
    }

    private void initAssessmentRatingsComments(AssessmentRatingsComments skillComment) {
        SingleSkillPanel skillPanel = new SingleSkillPanel(skill, skillComment, reviewOnly);
        skills.add(skillPanel);
        panel.add(skillPanel);
    }

    private void initSkillAssessmentRatingsComments(SkillAssessmentElem skill) {
        SingleSkillPanel skillPanel = new SingleSkillPanel(skillsStruct, skill);
        skills.add(skillPanel);
        panel.add(skillPanel);
    }

   /* public void addButton(Button button) {
        buttonPanel.add(button);
    }*/
    public void addButton(WfmButton2 button) {
        buttonPanel.add(button);
    }

    public SkillAssessmentElem[] getDataToSave() {
        SkillAssessmentElem[] elems = new SkillAssessmentElem[skills.size()];
        for (int i = 0; i < skills.size(); i++) {
            elems[i] = new SkillAssessmentElem();
            SingleSkillPanel panel = skills.get(i);
            if (turn == MANAGER_TURN) {
                elems[i].setTurn(MANAGER_TURN);
                elems[i].setReviewersComment(panel.getYourComment());
                elems[i].setRaiting(panel.getYourRate());
            } else {
                elems[i].setTurn(EMPLOYEE_TURN);
                elems[i].setEmployeesComment(panel.getYourComment());
                elems[i].setEmployeeRating(panel.getYourRate());
            }

            elems[i].setSkillRatingId(panel.getSkillRatingID());
        }
        return elems;
    }
}
