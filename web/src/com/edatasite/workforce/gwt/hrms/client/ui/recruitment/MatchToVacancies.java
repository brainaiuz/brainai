package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import gwt.material.design.client.ui.MaterialDialogContent;

import java.util.ArrayList;

/**
 * User: Ilhombek
 * Date: 8/24/12
 * Time: 4:07 PM
 */
public class MatchToVacancies extends KpiModal {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer candidateID;
    private WfmButton2 cancelButton;
    private WfmButton2 saveButton;
    private CustomList vacanciesList;


    public MatchToVacancies(Integer candidateID) {
        super();
        this.candidateID = candidateID;
        setTitle(hrmsStrings.matchToVacancies());
        setWidth(400);
        drawInitialize();
    }

    private void drawInitialize() {
        String matchToVacancyPopUp = "match_to_vacancy_popup_";
        //vacancies
        vacanciesList = new CustomList(Design.CHECK, true);
        vacanciesList.setSearchText(hrmsStrings.searchVacancies());
        vacanciesList.getElement().setId(matchToVacancyPopUp + "vacancies");
        vacanciesList.setHeight("200px");

        //cancel button
        cancelButton = new WfmButton2(wfmStrings.cancel(), event -> close());
        cancelButton.getElement().setId(matchToVacancyPopUp + "cancel_button");
        //save button
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> save());
        saveButton.getElement().setId(matchToVacancyPopUp + "save_button");
        //contact table
        FlexTable table = new FlexTable();
        HTML vacanciesHTML = new HTML(hrmsStrings.vacancies() + ":");
        vacanciesHTML.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        vacanciesHTML.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
        vacanciesHTML.setStyleName("customTitle form-label");
        table.setWidget(0, 0, vacanciesHTML);
        table.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        table.setWidget(1, 0, vacanciesList);

        //buttons panel
        addButton(cancelButton);
        addButton(saveButton);

        MaterialDialogContent container = getContent();
        container.add(table);

        RecruitmentService.App.get().getCandidateVacancies(candidateID, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(ArrayList<SelectItem> result) {
                if (vacanciesList.getItems() != null) {
                    vacanciesList.removeItems();
                }
                if (result != null && result.size() > 0) {
                    for (SelectItem vacancy : result) {
                        CustomListItem item = new CustomListItem(vacancy);
                        vacanciesList.add(item);

                        if (vacancy.isSelected()) {
                            item.setCheck(true);
                        }
                    }
                }
            }
        });
        //open match to vacancies popup
        this.open();
    }

    private void enabledButtons(boolean b) {
        if (saveButton != null) {
            saveButton.setEnabled(b);
        }
        if (cancelButton != null) {
            cancelButton.setEnabled(b);
        }
    }

    private void save() {
        enabledButtons(false);
        ArrayList<SelectItem> appliedVacancies = new ArrayList<>();
        if (vacanciesList.getItems() != null && vacanciesList.getItems().size() > 0) {
            for (CustomListItem vacancy : vacanciesList.getItems()) {
                SelectItem vacancyItem = vacancy.getItem();
                if (vacancy.getValue() != null) {
                    vacancyItem.setSelected(vacancy.getValue());
                }
                appliedVacancies.add(vacancyItem);
            }
        }
        LoadingPanel.loading(true);
        RecruitmentService.App.get().saveCandidateVacancies(appliedVacancies, candidateID, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enabledButtons(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                enabledButtons(true);
                Info.show(hrmsStrings.vacanciesMatched(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VACANCY_MATCHED, result, MatchToVacancies.this);
                close();
            }
        });
    }
}