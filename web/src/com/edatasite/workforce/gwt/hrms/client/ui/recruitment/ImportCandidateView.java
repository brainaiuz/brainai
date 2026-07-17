package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.crm.client.ui.ImportContactView;

/**
 * User: Hayot
 * Date: Aug 24, 2009
 * Time: 11:42:01 AM
 */
public class ImportCandidateView extends ImportContactView implements Constants, Colapse {
    private DataListBox project;
    private DataListBox source;
    private DataListBox status;
    private DataListBox createdDate;
    private DataListBox vacancies;
    private DataListBox workExperience;
    private DataListBox workExperienceMonthYearBox;
    private DataListBox currentEmployer;
    private DataListBox expectedSalary;
    private DataListBox location;
    private DataListBox skills;

    public ImportCandidateView(Integer objectId) {
        super(objectId, "importcandidate", wfmMessages.importEntity(wfmStrings.candidate()));
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Candidate;
    }

    @Override
    protected void createAndSetWidth() {
        super.createAndSetWidth();
        project = new DataListBox();
        project.getElement().setId("project");
        project.addStyleName(Constants.DEFAULT_WIDTH);

        source = new DataListBox();
        source.getElement().setId("source");
        source.addStyleName(Constants.DEFAULT_WIDTH);

        status = new DataListBox();
        status.getElement().setId("status");
        status.addStyleName(Constants.DEFAULT_WIDTH);

        createdDate = new DataListBox();
        createdDate.getElement().setId("created_date");
        createdDate.addStyleName(Constants.DEFAULT_WIDTH);

        vacancies = new DataListBox();
        vacancies.getElement().setId("vacancies");
        vacancies.addStyleName(Constants.DEFAULT_WIDTH);

        workExperience = new DataListBox();
        workExperience.getElement().setId("work_experience");

        workExperienceMonthYearBox = new DataListBox();
        workExperienceMonthYearBox.getElement().setId("work_experience_month_year_box");

        currentEmployer = new DataListBox();
        currentEmployer.getElement().setId("current_employer");
        currentEmployer.addStyleName(Constants.DEFAULT_WIDTH);

        expectedSalary = new DataListBox();
        expectedSalary.getElement().setId("expected_salary");
        expectedSalary.addStyleName(Constants.DEFAULT_WIDTH);

        location = new DataListBox();
        location.getElement().setId("location");
        location.addStyleName(Constants.DEFAULT_WIDTH);

        skills = new DataListBox();
        skills.getElement().setId("skills");
        skills.addStyleName(Constants.DEFAULT_WIDTH);
        skills.addStyleName("file--ImportCandidateView");
    }

    @Override
    protected ContactListItem getRPC() {
        ContactListItem item = super.getRPC();
        item.setCandidateSource(source.getSelectedItem());
        item.setProjectItem(project.getSelectedItem());
        item.setCandidateStatus(status.getSelectedItem());
        item.setCreatedDateID(getSelectedItem(createdDate));
        item.setVacancyID(getSelectedItem(vacancies));
        item.setWorkExperience(getSelectedItem(workExperience));
        item.setWorkExperienceMonthOrYear(getSelectedItem(workExperienceMonthYearBox));
        item.setCurrentEmployerID(getSelectedItem(currentEmployer));
        item.setExpectedSalaryID(getSelectedItem(expectedSalary));
        item.setLocationID(getSelectedItem(location));
        item.setSkillsID(getSelectedItem(skills));
        return item;
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addTitleField(OTHER_DETAILS, wfmStrings.otherDetails());
        addField(PROJECT_, project, getTitle(wfmStrings.project()));
        addField(SOURCE, source, getTitle(wfmStrings.source()));
        addField(STATUS, status, getTitle(wfmStrings.status()));
        addField(CREATED_DATE, createdDate, getTitle(wfmStrings.createdDate()));
        addField(VACANCIES, vacancies, getTitle(wfmStrings.matchedVacancies()));

        addField(WORK_EXPERIENCE, Utils.getInHorizontalPanel(3, 100, true, workExperience, workExperienceMonthYearBox), getTitle(wfmStrings.workExperience()));
        addField(CURRENT_EMPLOYER, currentEmployer, getTitle(wfmStrings.currentEmployer()));
        addField(EXPECTED_SALARY, expectedSalary, getTitle(wfmStrings.expectedSalary()));
        addField(PREFERRED_LOCATION, location, getTitle(wfmStrings.location()));
        addField(SKILLS, skills, getTitle(wfmStrings.skills()));
    }

    @Override
    public void setItems(SelectItem[] items) {
        super.setItems(items);
        project.setItems(items, wfmStrings.project());
        source.setItems(items, wfmStrings.source());
        status.setItems(items, wfmStrings.status());
        createdDate.setItems(items, wfmStrings.createdDate());
        vacancies.setItems(items, wfmStrings.matchedVacancies());
        workExperience.setItems(items, wfmStrings.workExperience());
        workExperienceMonthYearBox.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.month()), new SelectItem(2, wfmStrings.years())});
        workExperienceMonthYearBox.setSelected(2);
        currentEmployer.setItems(items, wfmStrings.currentEmployer());
        expectedSalary.setItems(items, wfmStrings.expectedSalary());
        skills.setItems(items, wfmStrings.skills());
        location.setItems(items, wfmStrings.location());
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.CANDIDATE;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_HRMS_CANDIDATE_FORM;
    }
}