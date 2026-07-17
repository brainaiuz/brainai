package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyQuestionTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.LookUp2;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VacancyQuestionsWidget extends Composite {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final EditableTable questionTable;
    private SelectItem[] candidateFieldItems;
    private boolean isSummary = false;

    public VacancyQuestionsWidget(boolean isSummary) {
        questionTable = new EditableTable(getColumns(), true, true);
        questionTable.setDraggable(true);
        questionTable.ensureDebugId("QuestionsWidget_questionTable");
        this.isSummary = isSummary;
        questionTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                if (!isSummary) {
                    questionTable.addRow(widgets(null, false));
                }
            }

            @Override
            public void removeRow() {
            }
        });

        fetchDataAndBuildInitialRows();

        initWidget(questionTable);
    }

    public VacancyQuestionTableItem[] getTableItems() {
        List<VacancyQuestionTableItem> questionTableItems = new ArrayList<>();
        for (int i = 0; i < questionTable.getRowCount(); i++) {
            VacancyQuestionTableItem item = new VacancyQuestionTableItem();

            ReferenceLookUp questionLookup = (ReferenceLookUp) questionTable.getColumnById(i, "question");

            LookUp2 fieldLookup = (LookUp2) questionTable.getColumnById(i, "fieldName");
            if (questionLookup != null && questionLookup.getSelectedItem() != null
               && fieldLookup != null && fieldLookup.getSelectedItem() != null) {
                item.setQuestionReferenceId(questionLookup.getSelectedItem().getId());
                item.setFieldId(fieldLookup.getSelectedItem().getId());
                questionTableItems.add(item);
            }
        }
        return questionTableItems.toArray(new VacancyQuestionTableItem[0]);
    }

    public void setData(VacancyQuestionTableItem[] questionTableItems) {
        questionTable.removeAllRows();
        if (questionTableItems != null && questionTableItems.length > 0) {
            for (VacancyQuestionTableItem item : questionTableItems) {
                questionTable.addRow(widgets(item, isSummary));
            }
        } else {
            for (int j = 0; j < 3; j++) {
                questionTable.addRow(widgets(null, isSummary));
            }
        }
    }


    private ColumnConfig[] getColumns() {
        return new ColumnConfig[]{
                new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100, true, "left-align-Cell"),
                new ColumnConfig(LookUpCell.class, "question", wfmStrings.question(), 260, true, "left-align-Cell"),
                new ColumnConfig(LookUpCell.class, "fieldName", wfmStrings.fieldName(), 220, true, "left-align-Cell"),
        };
    }


    private Widget[] widgets(VacancyQuestionTableItem question, boolean isSummary) {
        EditableTextBox typeTextBox = new EditableTextBox();
        typeTextBox.setText(wfmStrings.candidate());
        typeTextBox.setEnabled(false);

        LookUp2 fieldNameLookup = new LookUp2();
        if (candidateFieldItems == null || candidateFieldItems.length == 0) {
            fieldNameLookup.setItems(wfmStrings.entityNotFound());
        } else {
            fieldNameLookup.setItems(candidateFieldItems);
            if (question != null && question.getFieldId() != null) {
                // Can be fixed latter, I am in hurry, if you remove addItem it is not showing.
                SelectItem selectedItem = fieldNameLookup.getSelectedItem(question.getFieldId());
                if (selectedItem != null) {
                    fieldNameLookup.setSelected(selectedItem);
                    SelectItem actualItem = fieldNameLookup.getSelectedItem();
                    fieldNameLookup.addItem(actualItem);
                }
            }
        }


        final ReferenceLookUp questionLookup = new ReferenceLookUp("_CANDIDATE_QUESTIONS");
        if (question != null && question.getQuestionReference() != null && question.getQuestionReferenceId() != null) {
            questionLookup.addItem(question.getQuestionReference());

        }

        questionLookup.getSuggestBox().addSelectionHandler(
                event -> handleDuplicateCheck(questionLookup, "question", questionTable.getGrid().getCurrentRow(), wfmStrings.question()));

        fieldNameLookup.getSuggestBox().addSelectionHandler(
                event -> handleDuplicateCheck(fieldNameLookup, "fieldName", questionTable.getGrid().getCurrentRow(), wfmStrings.fieldName()));

        if (isSummary) {
            questionLookup.setEnabled(false);
            fieldNameLookup.setEnabled(false);
            questionTable.setShowAddCell(false);
            questionTable.setDisableRemoveCell(true);
        }

        return new Widget[]{
                typeTextBox,
                questionLookup,
                fieldNameLookup
        };
    }


    private void handleDuplicateCheck(LookUp lookUp, String columnId, int currentRowIndex, String friendlyFieldName) {
        SelectItem selectedItem = lookUp.getSelectedItem();
        if (selectedItem == null) return;

        for (int i = 0; i < questionTable.getRowCount(); i++) {
            if (i == currentRowIndex) {
                continue;
            }

            LookUp otherLookup = (LookUp) questionTable.getColumnById(i, columnId);
            if (otherLookup != null && otherLookup.getSelectedItem() != null) {
                if (Objects.equals(otherLookup.getSelectedItem().getId(), selectedItem.getId())) {
                    showDuplicateMessageBox(friendlyFieldName, selectedItem.getName());
                    lookUp.clear();
                    return;
                }
            }
        }
    }

    private void showDuplicateMessageBox(String fieldName, String valueName) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(fieldName + " '" + valueName + "' " + wfmStrings.isAlreadySelected());
        messageBox.open();
    }

    private void fetchDataAndBuildInitialRows() {
        CommonService.App.get().getCandidateFormCustomFieldsForQuestion(new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                if (result != null) {
                    List<CompanyCustomFieldItem> customFieldItems = result.getCompanyCustomFieldItems();

                    candidateFieldItems = new SelectItem[customFieldItems.size()];
                    int i = 0;

                    for (CompanyCustomFieldItem item : customFieldItems) {
                        candidateFieldItems[i++] = new SelectItem(item.getObjectId(), item.getFieldName());
                    }
            questionTable.addRow(widgets(null, false));

                }
            }
        });
    }


}