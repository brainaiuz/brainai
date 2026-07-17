package com.edatasite.workforce.gwt.hrms.client.ui.quickadd;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 06.01.2018 16:38
 */
public class VacancyQuickAddForm extends Composite {
    interface VacancyQuickAddFormUiBinder extends UiBinder<Widget, VacancyQuickAddForm> {
    }

    private static final VacancyQuickAddFormUiBinder ourUiBinder = GWT.create(VacancyQuickAddFormUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @UiField
    HTMLPanel panel;
    @UiField
    Span collapsibleHeader;
    @UiField
    Label jobTitleLabel;
    @UiField
    TextBox jobTitle;
    @UiField
    Label descriptionLabel;
    @UiField
    TextArea description;
    @UiField
    Label startDateLabel;
    @UiField
    DatePicker startDate;
    @UiField
    Label endDateLabel;
    @UiField
    DatePicker endDate;
    @UiField
    Label statusLabel;
    @UiField
    DataListBox status;
    @UiField
    Span collapsibleHeader2;
    @UiField
    Label numberLabel;
    @UiField
    HTMLPanel numberDiv;
    @UiField
    Label positionLabel;
    @UiField
    DataListBox position;
    @UiField
    Label managerLabel;
    @UiField
    DataListBox manager;


    private Numbering number;

    private VacancyItem item;
    private ExtendedCommand command;

    private final String debugId = "vacancy_quick_add_";

    public VacancyQuickAddForm() {
        initWidget(ourUiBinder.createAndBindUi(this));

        initForm();
    }

    private void initForm() {
        collapsibleHeader.setText(wfmStrings.generalDetails());
        collapsibleHeader2.setText(wfmStrings.internalDetails());
        jobTitleLabel.setText(wfmStrings.name());
        descriptionLabel.setText(wfmStrings.description());
        startDateLabel.setText(wfmStrings.startDate());
        endDateLabel.setText(wfmStrings.endDate());
        statusLabel.setText(wfmStrings.status());
        numberLabel.setText(wfmStrings.number());
        positionLabel.setText(wfmStrings.position());
        managerLabel.setText(wfmStrings.manager());

        jobTitle.getElement().setId(this.debugId + "jobTitle");
        description.getElement().setId(this.debugId + "description");
        startDate.setDate(DateUtil.resetTime(new Date()));
        startDate.getElement().setId(this.debugId + "startDate");
        endDate.getElement().setId(this.debugId + "endDate");
        status.getElement().setId(this.debugId + "status");
        number = new Numbering();
        number.getElement().setId(this.debugId + "numbering");
        numberDiv.add(number);
        position.getElement().setId(this.debugId + "position");
        manager.getElement().setId(this.debugId + "manager");
    }

    public void getVacancyQuickData() {
        LoadingPanel.loading(true, panel);
        RecruitmentService.App.get().getVacancyQuickData(new AbstractAsyncCallback<VacancyItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(VacancyItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                fillFields();
            }
        });

    }

    private void fillFields() {
        number.setNumberData(item.getNumberData());
        position.setItems(item.getPositions());

        PositionItem positionItem = item.getPositionItem();
        if (positionItem != null && positionItem.getPosStatus() != null) {
            status.setItems(positionItem.getPosStatus());
            for (SelectItem st : positionItem.getPosStatus()) {
                if (VacancyItem.VS_OPEN.equals(st.getDescription())) {
                    status.setSelected(st.getId());
                    break;
                }
            }
        }
        manager.setItems(item.getManagers());
        if (item.getManager() != null) {
            manager.setSelected(item.getManager());
        }
    }

    public boolean validate() {
        int errors = 0;

        if (jobTitle.getText() == null || "".equals(jobTitle.getText())) {
            jobTitle.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!number.validate()) {
            number.getTxtNumber().addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!Validation.validateDate(startDate, new HTML(""), true)) {
            errors++;
        }
        if (!Validation.validateDate(endDate, new HTML(""), true)) {
            errors++;
        }
        if (position != null && position.getSelectedItem() == null) {
            position.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        LoadingPanel.loading(true, panel);
        setValuesToRPC();
        RecruitmentService.App.get().saveVacancy(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                clearForm();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.vacancy()), Info.Type.INFO);

                if (command != null) {
                    command.execute(null);
                }
            }
        });
    }

    private void setValuesToRPC() {
        item.setNumberData(number.getNumberData(false));
        item.setJobTitle(jobTitle.getText());
        item.setDescription(description.getText());
        item.setStartDate(startDate.getDate());
        item.setEndDate(endDate.getDate());
        if (status.getSelectedItem() != null) {
            item.setStatus(new ReferenceItem(status.getSelectedItem().getId(), status.getSelectedItem().getName()));
        }
        PositionItem positionItem = new PositionItem();
        positionItem.setObjectID(position.getSelectedId());
        item.setPositionItem(positionItem);
        item.setManager(manager.getSelectedItem());
    }

    public void clearForm() {
        jobTitle.setText("");
        description.setText("");
        startDate.setDate(DateUtil.resetTime(new Date()));
        endDate.setDefaultFormatText();
        status.setSelectedNullLabel();
        position.setSelectedNullLabel();
        manager.clear();

        jobTitle.removeStyleName(Constants.ERROR_FORM_STYLE);
        number.getTxtNumber().removeStyleName(Constants.ERROR_FORM_STYLE);
        startDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        endDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        position.removeStyleName(Constants.ERROR_FORM_STYLE);
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }
}
