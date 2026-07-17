package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * User: Faxriddin
 * Date: 9/26/12
 */
public class TaskNumberingSettingsView extends PMDefaultNumberingSettings {

    private String prPreview;

    public TaskNumberingSettingsView() {

    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        VerticalPanel v1 = getVerticalPanel();
        getDragonDropPanel("680px");

        getPrefixPanelForDragon(columnDragController);

        getDatePanelForDragon(columnDragController);

        getClientCodePanelForDragon(columnDragController);

        getNumberPanelForDragon(columnDragController);

        getSuffixPanelForDragon(columnDragController);

        getWithPrNumberPanelForDrag(columnDragController);

        getDelimiterAndPreviewPanel();

        getRadioButtons("task", settingsStrings.restartNumberingForEachProject(), settingsStrings.useUniqueNumberForTask());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_PROJECT_NUMBER, TaskNumberingSettingsView.this, (sender, args) -> {
            prPreview = args.toString();
            moveColumnUserPutTask();
        });

        return v1;
    }

    @Override
    protected void moveColumnUserPutAll() {
        moveColumnUserPutTask();
    }

    @Override
    protected void setSettings(PMNumberingSettings numberingSettings) {
        super.setSettings(numberingSettings);
        numberingSettings.setTaskNumberingFormat(moveColumnUserPutTask());
        numberingSettings.setDelimetrTask(delimiterN.getText());

    }

    @Override
    protected void setDefaultParameters() {
        parseAndSetData(null, "", "T", prefixN, startNumber1, startNumber2, startNumber3, startNumber4, "task");
    }

    @Override
    protected void setSettingsData() {
        parseAndSetData((settings != null ? settings.getTaskNumberingFormat() : null), (settings != null ? settings.getDelimetrTask() : ""), "T", prefixN, startNumber1, startNumber2, startNumber3, startNumber4, "task");
    }

    private String moveColumnUserPutTask() {
        StringBuilder bufferT = new StringBuilder();
        StringBuilder bufferFormulaT = new StringBuilder();
        textBoxNYMD.setText(getDateParameters(checkBoxNY, checkBoxNM, checkBoxND));
        allNumber.setText(getAllStartNumber(startNumber1.getText(), startNumber2.getText(), startNumber3.getText(), startNumber4.getText()));
        if (!"".equals(delimiterN.getText())) {
            for (int i = 0; i < horizontalPanel.getWidgetCount(); i++) {
                ArrayList<String> isDelimet = getPreviewTextT(horizontalPanel.getWidget(i).getElement().getId());
                if (!"".equals(isDelimet.get(0))) {
                    bufferT.append(isDelimet.get(0) + delimiterN.getText());
                }
                bufferFormulaT.append(isDelimet.get(1) + "/");
            }
            preview.setText(bufferT.toString().substring(0, bufferT.toString().length() - 1));
        } else {
            for (int i = 0; i < horizontalPanel.getWidgetCount(); i++) {
                ArrayList<String> noDelimetr = getPreviewTextT(horizontalPanel.getWidget(i).getElement().getId());
                if (!"".equals(noDelimetr.get(0))) {
                    bufferT.append(noDelimetr.get(0));
                }
                bufferFormulaT.append(noDelimetr.get(1) + "/");
            }
            preview.setText(bufferT.toString());
        }

        return bufferFormulaT.toString() + getOtherParameters(reapedNumber, uniqueNumber).get(0) + "/" + getOtherParameters(reapedNumber, uniqueNumber).get(1);
    }

    private ArrayList<String> getPreviewTextT(String wTitle2) {
        ArrayList<String> list = new ArrayList<>();
        String sms = "";
        String formulaForNuberFormatT = "";
        if (WIDGET_PREFIX.equals(wTitle2)) {
            sms = !"".equals(prefixN.getText()) ? prefixN.getText() : sms;
            formulaForNuberFormatT = !"".equals(prefixN.getText()) ? WIDGET_PREFIX + ":" + prefixN.getText() : WIDGET_PREFIX + ":false";
        } else if (WIDGET_ALL_DATE.equals(wTitle2)) {
            sms = !"".equals(textBoxNYMD.getText()) ? textBoxNYMD.getText() : sms;
            formulaForNuberFormatT = checkBoxNY.getValue() ? WIDGET_DATE_YEAR + ":true/" : WIDGET_DATE_YEAR + ":false/";
            formulaForNuberFormatT = checkBoxNM.getValue() ? formulaForNuberFormatT + WIDGET_DATE_MONTH + ":true/" : formulaForNuberFormatT + WIDGET_DATE_MONTH + ":false/";
            formulaForNuberFormatT = checkBoxND.getValue() ? formulaForNuberFormatT + WIDGET_DATE_DAY + ":true" : formulaForNuberFormatT + WIDGET_DATE_DAY + ":false";
        } else if (WIDGET_CLIENT_CODE.equals(wTitle2)) {
            sms = clientNumber.getValue() ? sms + "CUS0001" : sms;
            formulaForNuberFormatT = clientNumber.getValue() ? WIDGET_CLIENT_CODE + ":true" : WIDGET_CLIENT_CODE + ":false";
        } else if (WIDGET_NUMBERS.equals(wTitle2)) {
            sms = !"".equals(allNumber.getText()) ? allNumber.getText() : sms;
            formulaForNuberFormatT = WIDGET_NUMBERS + ":" + allNumber.getText();
            if ("".equals(allNumber.getText()) || "0000".equals(allNumber.getText())) {
                formulaForNuberFormatT = WIDGET_NUMBERS + ":0001";
            }
        } else if (WIDGET_SUFFIX.equals(wTitle2)) {
            sms = !"".equals(suffix.getText()) ? suffix.getText() : sms;
            formulaForNuberFormatT = !"".equals(suffix.getText()) ? WIDGET_SUFFIX + ":" + suffix.getText() : WIDGET_SUFFIX + ":false";
        } else if (WIDGET_PROJECT_NUMBER.equals(wTitle2)) {
            formulaForNuberFormatT = WIDGET_PROJECT_NUMBER + ":false";
            if (withPmNumber.getValue()) {
                sms = prPreview;
                formulaForNuberFormatT = WIDGET_PROJECT_NUMBER + ":true";
            }
        }
        list.add(sms);
        list.add(formulaForNuberFormatT);
        return list;
    }
}
