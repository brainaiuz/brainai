package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Faxriddin
 * Date: 12/5/12
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeNumberingSettingsView extends PMDefaultNumberingSettings {

    public EmployeeNumberingSettingsView() {
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        VerticalPanel v1 = getVerticalPanel();
        getDragonDropPanel("330px");
        getPrefixPanelForDragon(columnDragController);
        getNumberPanelForDragon(columnDragController);
        getSuffixPanelForDragon(columnDragController);

        getDelimiterAndPreviewPanel();

        return v1;
    }

    @Override
    protected void moveColumnUserPutAll() {
        moveColumnUserPutForEmpNumber();
    }

    @Override
    protected void setDefaultParameters() {
        parseAndSetData(null, "", "EMP", prefixN, startNumber1, startNumber2, startNumber3, startNumber4, "employee");
    }

    @Override
    protected void setSettingsData() {
        parseAndSetData((settings != null ? settings.getEmployeeNumberingFormat() : null), (settings != null ? settings.getDelimetrEmployeeNumbering() : ""), "EMP", prefixN, startNumber1, startNumber2, startNumber3, startNumber4, "employee");
    }

    @Override
    protected void setSettings(PMNumberingSettings numberingSettings) {
        super.setSettings(numberingSettings);
        numberingSettings.setEmployeeNumberingFormat(moveColumnUserPutForEmpNumber());
        numberingSettings.setDelimetrEmployeeNumbering(delimiterN.getText());
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
        String formulaForNuberFormatEmp = "";
        if (WIDGET_PREFIX.equals(vTitle)) {
            sms = !"".equals(prefixN.getText()) ? prefixN.getText() : sms;
            formulaForNuberFormatEmp = !"".equals(prefixN.getText()) ? WIDGET_PREFIX + ":" + prefixN.getText() : WIDGET_PREFIX + ":false";
        } else if (WIDGET_NUMBERS.equals(vTitle)) {
            sms = !"".equals(allNumber.getText()) ? allNumber.getText() : sms;
            formulaForNuberFormatEmp = WIDGET_NUMBERS + ":" + allNumber.getText();
            if ("".equals(allNumber.getText()) || "0000".equals(allNumber.getText())) {
                formulaForNuberFormatEmp = WIDGET_NUMBERS + ":0001";
            }
        } else if (WIDGET_SUFFIX.equals(vTitle)) {
            sms = !"".equals(suffix.getText()) ? suffix.getText() : sms;
            formulaForNuberFormatEmp = !"".equals(suffix.getText()) ? WIDGET_SUFFIX + ":" + suffix.getText() : WIDGET_SUFFIX + ":false";
        }
        list.add(sms);
        list.add(formulaForNuberFormatEmp);
        return list;
    }

}
