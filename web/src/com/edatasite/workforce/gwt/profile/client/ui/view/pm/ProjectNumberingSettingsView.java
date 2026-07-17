package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * User: Faxriddin
 * Date: 9/27/12
 */
public class ProjectNumberingSettingsView extends PMDefaultNumberingSettings {

    public ProjectNumberingSettingsView() {
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        VerticalPanel v1 = getVerticalPanel();
        v1.setStyleName("numberingSettings-pojectNumberic");
        getDragonDropPanel("560px");

        getPrefixPanelForDragon(columnDragController);
        getDatePanelForDragon(columnDragController);
        getClientCodePanelForDragon(columnDragController);
        getNumberPanelForDragon(columnDragController);
        getSuffixPanelForDragon(columnDragController);
        getRestartNumberWithDelimiterPanel(PM_RESTART_NUMBERING);

        return v1;
    }

    @Override
    protected void moveColumnUserPutAll() {
        moveColumnUserPut();
    }

    protected void setSettings(PMNumberingSettings numberingSettings) {
        super.setSettings(numberingSettings);
        numberingSettings.setProjectNumberingFormat(moveColumnUserPut());
        numberingSettings.setDelimetrProject(delimiterN.getText());
        numberingSettings.setProjectIntNumber(Integer.valueOf(getAllStartNumber(startNumber1.getText(), startNumber2.getText(), startNumber3.getText(), startNumber4.getText())));
        numberingSettings.setProjectNumberRestartDate(getRestartDateByViewName(PM_RESTART_NUMBERING));

    }

    protected void setDefaultParameters() {
        parseAndSetData(null, "", "P", prefixN, startNumber1, startNumber2, startNumber3, startNumber4, "project");
    }

    protected void setSettingsData() {
        parseAndSetData((settings != null ? settings.getProjectNumberingFormat() : null), (settings != null ? settings.getDelimetrProject() : ""), "P", prefixN, startNumber1, startNumber2, startNumber3, startNumber4, "project");
        setSelectedDate(PM_RESTART_NUMBERING, settings.getProjectNumberRestartDate());
    }

    protected String moveColumnUserPut() {
        StringBuilder buffer = new StringBuilder();
        StringBuilder bufferFormula = new StringBuilder();
        String previewText = "";
        textBoxNYMD.setText(getDateParameters(checkBoxNY, checkBoxNM, checkBoxND));
        allNumber.setText(getAllStartNumber(startNumber1.getText(), startNumber2.getText(), startNumber3.getText(), startNumber4.getText()));
        if (!"".equals(delimiterN.getText())) {
            for (int i = 0; i < horizontalPanel.getWidgetCount(); i++) {
                ArrayList<String> s = getPreviewTextP(horizontalPanel.getWidget(i).getElement().getId());
                if (!"".equals(s.get(0))) {
                    buffer.append(s.get(0) + delimiterN.getText());
                }
                bufferFormula.append(s.get(1) + "/");
            }
            previewText = buffer.toString().substring(0, buffer.toString().length() - 1);
        } else {
            for (int i = 0; i < horizontalPanel.getWidgetCount(); i++) {
                ArrayList<String> s = getPreviewTextP(horizontalPanel.getWidget(i).getElement().getId());
                if (!"".equals(s.get(0))) {
                    buffer.append(s.get(0));
                }
                bufferFormula.append(s.get(1) + "/");
            }
            previewText = buffer.toString();
        }
        preview.setText(previewText);
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_PROJECT_NUMBER, previewText, ProjectNumberingSettingsView.this);
        return bufferFormula.toString();
    }

    private ArrayList<String> getPreviewTextP(String vTitle) {
        ArrayList<String> list = new ArrayList<>();
        String sms = "";
        String formulaForNuberFormatP = "";
        if (WIDGET_PREFIX.equals(vTitle)) {
            sms = !"".equals(prefixN.getText()) ? prefixN.getText() : sms;
            formulaForNuberFormatP = !"".equals(prefixN.getText()) ? WIDGET_PREFIX + ":" + prefixN.getText() : WIDGET_PREFIX + ":false";
        } else if (WIDGET_ALL_DATE.equals(vTitle)) {
            sms = !"".equals(textBoxNYMD.getText()) ? textBoxNYMD.getText() : sms;
            formulaForNuberFormatP = checkBoxNY.getValue() ? WIDGET_DATE_YEAR + ":true/" : WIDGET_DATE_YEAR + ":false/";
            formulaForNuberFormatP = checkBoxNM.getValue() ? formulaForNuberFormatP + WIDGET_DATE_MONTH + ":true/" : formulaForNuberFormatP + WIDGET_DATE_MONTH + ":false/";
            formulaForNuberFormatP = checkBoxND.getValue() ? formulaForNuberFormatP + WIDGET_DATE_DAY + ":true" : formulaForNuberFormatP + WIDGET_DATE_DAY + ":false";
        } else if (WIDGET_CLIENT_CODE.equals(vTitle)) {
            sms = clientNumber.getValue() ? "CUS0001" : sms;
            formulaForNuberFormatP = clientNumber.getValue() ? WIDGET_CLIENT_CODE + ":true" : WIDGET_CLIENT_CODE + ":false";
        } else if (WIDGET_NUMBERS.equals(vTitle)) {
            sms = !"".equals(allNumber.getText()) ? allNumber.getText() : sms;
            formulaForNuberFormatP = WIDGET_NUMBERS + ":" + allNumber.getText();
            if ("".equals(allNumber.getText()) || "0000".equals(allNumber.getText())) {
                formulaForNuberFormatP = WIDGET_NUMBERS + ":0001";
            }
        } else if (WIDGET_SUFFIX.equals(vTitle)) {
            sms = !"".equals(suffix.getText()) ? suffix.getText() : sms;
            formulaForNuberFormatP = !"".equals(suffix.getText()) ? WIDGET_SUFFIX + ":" + suffix.getText() : WIDGET_SUFFIX + ":false";
        }
        list.add(sms);
        list.add(formulaForNuberFormatP);
        return list;
    }
}
