package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.ContactCareerItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 01.12.2010
 * Time: 21:57:05
 * To change this template use File | Settings | File Templates.
 */
public class AddContactCareerView extends KpiModal {

    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final Integer careerID;
    private final Integer contactID;
    private TextBox city;
    private TextBox company;
    private DataListBox country;
    private KpiCheckBox currentYear;
    private TextBox jobTitle;
    private DataListBox industry;
    private DatePicker fromYear;
    private DatePicker toYear;
    private Button saveButton;

    public AddContactCareerView(Integer careerID, Integer contactID, boolean isAdd) {
        super();
        this.careerID = careerID;
        this.contactID = contactID;
        setTitle(isAdd ? crmStrings.addCareer() : crmStrings.editCareer());
        setSize(300, 250);
        initialize();

    }

    private void initialize() {
        LoadingPanel.loading(true);
        FlexTable table = new FlexTable();
        table.setStyleName("workforce");
        table.setSize("300px", "250px");
        table.setCellSpacing(10);
        table.setCellPadding(10);

        company = new TextBox();
        company.setWidth("200px");
        table.setWidget(0, 0, generateTitleMessage(wfmStrings.company(), true));
        table.setWidget(0, 1, company);

        jobTitle = new TextBox();
        jobTitle.setWidth("200px");
        table.setWidget(1, 0, generateTitleMessage(wfmStrings.jobTitle(), true));
        table.setWidget(1, 1, jobTitle);

        industry = new DataListBox();
        industry.setWidth("200px");
        table.setWidget(2, 0, generateTitleMessage(wfmStrings.industry(), true));
        table.setWidget(2, 1, industry);

        fromYear = new DatePicker((Date) null);
        fromYear.setWidth("80px");
        toYear = new DatePicker((Date) null);
        toYear.setWidth("80px");
        table.setWidget(3, 0, generateTitleMessage(wfmStrings.from(), true));
        HorizontalPanel datePanel = new HorizontalPanel();
        datePanel.setWidth("200px");
        datePanel.add(fromYear);
        datePanel.setCellVerticalAlignment(fromYear, HasVerticalAlignment.ALIGN_MIDDLE);
        HTML html = generateTitleMessage(wfmStrings.to(), true);
        datePanel.add(html);
        datePanel.setCellVerticalAlignment(html, HasVerticalAlignment.ALIGN_MIDDLE);
        datePanel.add(toYear);
        datePanel.setCellVerticalAlignment(toYear, HasVerticalAlignment.ALIGN_MIDDLE);
        datePanel.setCellHorizontalAlignment(toYear, HasHorizontalAlignment.ALIGN_RIGHT);
        table.setWidget(3, 1, datePanel);

        currentYear = new KpiCheckBox(crmStrings.currentPosition(), true);
        currentYear.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        currentYear.addValueChangeHandler(booleanValueChangeEvent -> {
            if (booleanValueChangeEvent.getValue()) {
                toYear.setEnabled(false);
                toYear.setDefaultValue();
            } else {
                toYear.setEnabled(true);
            }
        });
        table.setHTML(4, 0, "&nbsp;");
        table.setWidget(4, 1, currentYear);

        city = new TextBox();
        city.setWidth("80px");
        city.setHeight("20px");
        country = new DataListBox();
        country.setWidth("115px");
        table.setWidget(5, 0, generateTitleMessage(wfmStrings.city(), true));
        HorizontalPanel cityPanel = new HorizontalPanel();
        cityPanel.setWidth("200px");
        cityPanel.add(city);
        cityPanel.setCellVerticalAlignment(city, HasVerticalAlignment.ALIGN_MIDDLE);
        cityPanel.add(country);
        cityPanel.setCellHorizontalAlignment(country, HasHorizontalAlignment.ALIGN_RIGHT);
        cityPanel.setCellVerticalAlignment(country, HasVerticalAlignment.ALIGN_MIDDLE);
        table.setWidget(5, 1, cityPanel);

        ContactService.App.get().getContactCareer(careerID, new AbstractAsyncCallback<ContactCareerItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ContactCareerItem contactCareerItem) {
                LoadingPanel.loading(false);
                if (contactCareerItem != null) {
                    company.setText(contactCareerItem.getCompanyName());
                    jobTitle.setText(contactCareerItem.getJobTitle());
                    city.setText(contactCareerItem.getCity());
                    country.setItems(contactCareerItem.getCountries());
                    if (contactCareerItem.getCountryID() != null) {
                        country.setSelected(contactCareerItem.getCountryID());
                    }
                    industry.setItems(contactCareerItem.getIndustries());
                    if (contactCareerItem.getIndustryID() != null) {
                        industry.setSelected(contactCareerItem.getIndustryID());
                    }
                    if (contactCareerItem.getFromYear() != null) {
                        fromYear.setDate(contactCareerItem.getFromYear());
                    }
                    if (contactCareerItem.getToYear() != null && !contactCareerItem.isCurrentYear()) {
                        toYear.setDate(contactCareerItem.getToYear());
                    }
                    currentYear.setValue(contactCareerItem.isCurrentYear());
                    if (currentYear.getValue()) {
                        toYear.setEnabled(false);
                        toYear.setDefaultValue();
                    } else {
                        toYear.setEnabled(true);
                    }
                }
            }
        });

        saveButton = new Button(wfmStrings.save());
        saveButton.addClickHandler(clickEvent -> save());
        table.setWidget(6, 0, saveButton);
        table.getFlexCellFormatter().setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_CENTER);
        table.getFlexCellFormatter().setColSpan(6, 0, 2);
        add(table);
        center();
    }

    private HTML generateTitleMessage(String message, boolean isRequired) {
        HTML title = new HTML("<b class=customTitle>" + message + (isRequired ? "<font color=red>*</font>:</b>" : ":</b>"));
        title.setWordWrap(false);
        return title;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        saveButton.setEnabled(false);
        ContactCareerItem careerItem = new ContactCareerItem();
        careerItem.setCareerID(careerID);
        careerItem.setContactID(contactID);
        careerItem.setCompanyName(company.getText());
        careerItem.setJobTitle(jobTitle.getText());
        careerItem.setIndustryID(industry.getSelectedItem().getId());
        careerItem.setIndustryName(industry.getSelectedItem().getName());
        careerItem.setFromYear(fromYear.getDate());
        careerItem.setToYear(toYear.getDate());
        careerItem.setCurrentYear(currentYear.getValue());
        careerItem.setCity(city.getText());
        careerItem.setCountryID(country.getSelectedItem().getId());
        careerItem.setCountryName(country.getSelectedItem().getName());

        LoadingPanel.loading(true);
        ContactService.App.get().saveContactCareer(careerItem, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer integer) {
                close();
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.careerInformation()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_CAREER_ADD_EDIT, integer, AddContactCareerView.this);
                saveButton.setEnabled(true);
            }
        });
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(company)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(jobTitle)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(city)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(industry, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(country, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateDate(fromYear, new HTML(), true)) {
            errors++;
        }
        if (toYear.isEnabled() && !Validation.validateDate(toYear, new HTML(), true)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }
}
