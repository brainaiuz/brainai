package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

public class LeaveRequestNumberingSettingsView extends PMDefaultNumberingSettings {
    public LeaveRequestNumberingSettingsView() {
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        VerticalPanel v1 = getVerticalPanel();
        getDragonDropPanel("330px");
        getPrefixPanelForDragon(columnDragController);
        getNumberPanelForDragon(columnDragController);
        getSuffixPanelForDragon(columnDragController);

//        getDelimiterAndPreviewPanel();
        getRestartNumberWithDelimiterPanel(LR_RESTART_NUMBERING);

        return v1;
    }

    @Override
    protected void moveColumnUserPutAll() {
        moveColumnUserPutForEmpNumber();
    }

    @Override
    protected void setDefaultParameters() {
        parseAndSetData(null, "", "LVR", prefixN, startNumber1, startNumber2, startNumber3, startNumber4, "leaveRequest");
    }

    @Override
    protected void setSettingsData() {
        parseAndSetData((settings != null ? settings.getLeaveRequestNumberingFormat() : null), (settings != null ? settings.getDelimetrLeaveRequestNumbering() : ""), "LVR", prefixN, startNumber1, startNumber2, startNumber3, startNumber4, "leaveRequest");
        setSelectedDate(LR_RESTART_NUMBERING, settings.getLeaveRequestNumberRestartDate());
    }

    @Override
    protected void setSettings(PMNumberingSettings numberingSettings) {
        super.setSettings(numberingSettings);
        numberingSettings.setLeaveRequestNumberingFormat(moveColumnUserPutForEmpNumber());
        numberingSettings.setDelimetrLeaveRequestNumbering(delimiterN.getText());
        numberingSettings.setLeaveRequestIntNumber(Integer.valueOf(getAllStartNumber(startNumber1.getText(), startNumber2.getText(), startNumber3.getText(), startNumber4.getText())));
        numberingSettings.setLeaveRequestNumberRestartDate(getRestartDateByViewName(LR_RESTART_NUMBERING));
    }

    protected String moveColumnUserPutForEmpNumber() {
        StringBuilder buffer = new StringBuilder();
        StringBuilder bufferFormula = new StringBuilder();
        String previewText = "";
        allNumber.setText(getAllStartNumber(startNumber1.getText(), startNumber2.getText(), startNumber3.getText(), startNumber4.getText()));
        if (!"".equals(delimiterN.getText())) {
            for (int i = 0; i < horizontalPanel.getWidgetCount(); i++) {
                ArrayList<String> s = getPreviewTextEmployee(horizontalPanel.getWidget(i).getElement().getId());
                if (!"".equals(s.get(0))) {
                    buffer.append(s.get(0) + delimiterN.getText());
                }
                bufferFormula.append(s.get(1) + "/");
            }
            previewText = buffer.toString().substring(0, buffer.toString().length() - 1);
        } else {
            for (int i = 0; i < horizontalPanel.getWidgetCount(); i++) {
                ArrayList<String> s = getPreviewTextEmployee(horizontalPanel.getWidget(i).getElement().getId());
                if (!"".equals(s.get(0))) {
                    buffer.append(s.get(0));
                }
                bufferFormula.append(s.get(1) + "/");
            }
            previewText = buffer.toString();
        }
        preview.setText(previewText);
        return bufferFormula.toString();
    }

    private ArrayList<String> getPreviewTextEmployee(String vTitle) {
        ArrayList<String> list = new ArrayList<>();
        String sms = "";
        String formulaForNuberFormatDep = "";
        if (WIDGET_PREFIX.equals(vTitle)) {
            sms = !"".equals(prefixN.getText()) ? prefixN.getText() : sms;
            formulaForNuberFormatDep = !"".equals(prefixN.getText()) ? WIDGET_PREFIX + ":" + prefixN.getText() : WIDGET_PREFIX + ":false";
        } else if (WIDGET_NUMBERS.equals(vTitle)) {
            sms = !"".equals(allNumber.getText()) ? allNumber.getText() : sms;
            formulaForNuberFormatDep = WIDGET_NUMBERS + ":" + allNumber.getText();
            if ("".equals(allNumber.getText()) || "0000".equals(allNumber.getText())) {
                formulaForNuberFormatDep = WIDGET_NUMBERS + ":0001";
            }
        } else if (WIDGET_SUFFIX.equals(vTitle)) {
            sms = !"".equals(suffix.getText()) ? suffix.getText() : sms;
            formulaForNuberFormatDep = !"".equals(suffix.getText()) ? WIDGET_SUFFIX + ":" + suffix.getText() : WIDGET_SUFFIX + ":false";
        }
        list.add(sms);
        list.add(formulaForNuberFormatDep);
        return list;
    }

}
