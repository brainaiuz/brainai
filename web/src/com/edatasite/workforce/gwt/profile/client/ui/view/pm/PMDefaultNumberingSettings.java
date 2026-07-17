package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.VerticalPanelWithSpacer;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Faxriddin
 * Date: 9/25/12
 */
public class PMDefaultNumberingSettings extends Composite implements Constants {

    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final String PM_RESTART_NUMBERING = "PM_RESTART_NUMBERING";
    protected static final String LR_RESTART_NUMBERING = "LR_RESTART_NUMBERING";
    protected PMNumberingSettings settings;

    protected AbsolutePanel boundaryPanel;
    protected VerticalPanel verticalPanel;
    protected HorizontalPanel horizontalPanel;
    protected PickupDragController columnDragController;
    protected VerticalPanel columnCompositePanelN1;
    protected VerticalPanel columnCompositePanelN2;
    protected VerticalPanel columnCompositePanelN3;
    protected VerticalPanel columnCompositePanelN4;
    protected VerticalPanel columnCompositePanelN5;
    protected VerticalPanel columnCompositePanelN6;

    protected TextBox prefixN;
    protected KpiCheckBox checkBoxDateN;
    protected KpiCheckBox checkBoxNY;
    protected KpiCheckBox checkBoxNM;
    protected KpiCheckBox checkBoxND;
    protected TextBox textBoxNYMD;
    protected KpiCheckBox clientNumber;
    protected TextBox startNumber1;
    protected TextBox startNumber2;
    protected TextBox startNumber3;
    protected TextBox startNumber4;
    protected TextBox allNumber;
    protected TextBox suffix;
    protected TextBox delimiterN;
    protected KpiCheckBox withPmNumber;
    protected TextBox preview;
    protected RadioButton reapedNumber;
    protected RadioButton uniqueNumber;
    protected HashMap<String, RestartDateWidget> restartWidgetByView = new HashMap<>();

//    protected KpiCheckBox projectRestartNumber;
//    protected DataListBox dayOfMonthP;
//    protected DataListBox monthsNameP;
//    protected InputGroup inputGroup;


    Date date = new Date();
    String y = String.valueOf(1900 + date.getYear());
    String m = String.valueOf(1 + date.getMonth());
    String d = String.valueOf(date.getDate());

    public void getBoundaryPanel(String width) {
        boundaryPanel = new AbsolutePanel();
        boundaryPanel.setWidth(width);
        boundaryPanel.add(horizontalPanel);
        verticalPanel.add(boundaryPanel);
    }

    public VerticalPanel getVerticalPanel() {
        verticalPanel = new VerticalPanel();
        return verticalPanel;
    }

    protected void getDragonDropPanel(String width) {
        getHorizontalPanel(width);
        getBoundaryPanel(width);
        getPickupController();
    }

    public HorizontalPanel getHorizontalPanel(String width) {
        horizontalPanel = new HorizontalPanel();
        horizontalPanel.setWidth(width);
        horizontalPanel.setSpacing(0);
        return horizontalPanel;
    }

    public VerticalPanel getColumnCompositePanelN1() {
        columnCompositePanelN1 = new VerticalPanel();
        return columnCompositePanelN1;
    }

    protected Date getRestartDateByViewName(String viewName) {
        if (restartWidgetByView.get(viewName) != null) {
            return restartWidgetByView.get(viewName).getSelectedDate();
        } else {
            return null;
        }
    }

    public VerticalPanel getColumnCompositePanelN2() {
        columnCompositePanelN2 = new VerticalPanel();
        return columnCompositePanelN2;
    }

    public VerticalPanel getColumnCompositePanelN3() {
        columnCompositePanelN3 = new VerticalPanel();
        return columnCompositePanelN3;
    }

    public VerticalPanel getColumnCompositePanelN4() {
        columnCompositePanelN4 = new VerticalPanel();
        return columnCompositePanelN4;
    }

    public VerticalPanel getColumnCompositePanelN5() {
        columnCompositePanelN5 = new VerticalPanel();
        return columnCompositePanelN5;
    }

    public VerticalPanel getColumnCompositePanelN6() {
        columnCompositePanelN6 = new VerticalPanel();
        return columnCompositePanelN6;
    }

    public TextBox getPrefixN() {
        prefixN = new TextBox();
        prefixN.setWidth("60px");
        prefixN.addKeyUpHandler(keyUpEvent -> moveColumnUserPutAll());
        return prefixN;
    }

    public KpiCheckBox getCheckBoxNY() {
        checkBoxNY = new KpiCheckBox();
        checkBoxNY.addClickHandler(clickEvent -> moveColumnUserPutAll());
        return checkBoxNY;
    }

    public KpiCheckBox getCheckBoxNM() {
        checkBoxNM = new KpiCheckBox();
        checkBoxNM.addClickHandler(clickEvent -> moveColumnUserPutAll());
        return checkBoxNM;
    }

    public KpiCheckBox getCheckBoxND() {
        checkBoxND = new KpiCheckBox();
        checkBoxND.addClickHandler(clickEvent -> moveColumnUserPutAll());
        return checkBoxND;
    }

    public KpiCheckBox getCheckBoxDateN() {
        checkBoxDateN = new KpiCheckBox();
        checkBoxDateN.getElement().getStyle().setMarginBottom(7, Style.Unit.PX);
        checkBoxDateN.addClickHandler(clickEvent -> {
            if (checkBoxDateN.getValue()) {
                checkBoxSetValues(checkBoxNY, checkBoxNM, checkBoxND, true);
                moveColumnUserPutAll();
            } else {
                checkBoxSetValues(checkBoxNY, checkBoxNM, checkBoxND, false);
                moveColumnUserPutAll();
            }
        });
        return checkBoxDateN;
    }

    public TextBox getTextBoxNYMD() {
        textBoxNYMD = new TextBox();
        return textBoxNYMD;
    }

    public KpiCheckBox getClientNumber() {
        clientNumber = new KpiCheckBox();
        clientNumber.addClickHandler(clickEvent -> moveColumnUserPutAll());
        return clientNumber;
    }

    public TextBox getStartNumber1() {
        startNumber1 = new TextBox();
        startNumber1 = createNumCell(0);
        startNumber1.addKeyUpHandler(keyPressEvent -> moveColumnUserPutAll());
        return startNumber1;
    }

    public TextBox getStartNumber2() {
        startNumber2 = new TextBox();
        startNumber2 = createNumCell(0);
        startNumber2.addKeyUpHandler(keyPressEvent -> moveColumnUserPutAll());
        return startNumber2;
    }

    public TextBox getStartNumber3() {
        startNumber3 = new TextBox();
        startNumber3 = createNumCell(0);
        startNumber3.addKeyUpHandler(keyPressEvent -> moveColumnUserPutAll());
        return startNumber3;
    }

    public TextBox getStartNumber4() {
        startNumber4 = new TextBox();
        startNumber4 = createNumCell(0);
        startNumber4.addKeyUpHandler(keyPressEvent -> moveColumnUserPutAll());
        return startNumber4;
    }

    public TextBox getAllNumber() {
        allNumber = new TextBox();
        return allNumber;
    }

    public TextBox getSuffix() {
        suffix = new TextBox();
        suffix.setWidth("60px");
        suffix.addKeyUpHandler(keyUpEvent -> moveColumnUserPutAll());
        return suffix;
    }

    public TextBox getDelimiterN() {
        delimiterN = new TextBox();
        delimiterN.setWidth("60px");
        delimiterN.setMaxLength(1);
        delimiterN.addKeyUpHandler(keyUpEvent -> moveColumnUserPutAll());
        return delimiterN;
    }

    public KpiCheckBox getWithPmNumber() {
        withPmNumber = new KpiCheckBox();
        withPmNumber.addClickHandler(clickEvent -> moveColumnUserPutAll());
        return withPmNumber;
    }

    public TextBox getPreview() {
        preview = new TextBox();
        preview.setEnabled(false);
        return preview;
    }

//    public KpiCheckBox getProjectRestartNumber() {
//        projectRestartNumber = new KpiCheckBox();
//        projectRestartNumber.addClickHandler(clickEvent -> {
//            if (projectRestartNumber.getValue()) {
//                setVisibleDate(1, 1);
//
//            } else {
//                inputGroup.setVisible(false);
//            }
//        });
//        return projectRestartNumber;
//    }

//    protected void setVisibleDate(int dayOfMonth, int month) {
//        inputGroup.setVisible(true);
//        dayOfMonthP.setSelected(dayOfMonth);
//        monthsNameP.setSelected(month);
//    }

    protected void getRadioButtons(String radioButtonName, String radioButtonString1, String radioButtonString2) {
        VerticalPanel verticalPanel1 = new VerticalPanel();
        reapedNumber = new KpiRadioButton(radioButtonName, radioButtonString1, true);
        uniqueNumber = new KpiRadioButton(radioButtonName, radioButtonString2, true);
        verticalPanel1.add(reapedNumber);
        verticalPanel1.add(uniqueNumber);
        verticalPanel.add(verticalPanel1);
    }

    protected Widget onInitialize() {
        LoadingPanel.loading(true);

        ProfileService.App.get().getPMNumberingSettings(new AbstractAsyncCallback<PMNumberingSettings>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                setDefaultParameters();
            }

            @Override
            public void success(PMNumberingSettings pmNumberingSettings) {
                LoadingPanel.loading(false);
                settings = pmNumberingSettings;
                setSettingsData();
            }
        });
        return null;
    }

    protected PickupDragController getPickupController() {
        columnDragController = new PickupDragController(boundaryPanel, false);
        columnDragController.setBehaviorMultipleSelection(false);
        HorizontalPanelDropController columnDropController = new HorizontalPanelDropController(horizontalPanel);
        columnDragController.registerDropController(columnDropController);
        columnDragController.addDragHandler(new DragHandlerAdapter() {
            @Override
            public void onDragEnd(DragEndEvent event) {
                moveColumnUserPutAll();

            }

        });
        return columnDragController;
    }

    protected void getPrefixPanelForDragon(PickupDragController columnDragController) {
        getColumnCompositePanelN1();
        columnCompositePanelN1.getElement().setId(WIDGET_PREFIX);
        VerticalPanel verticalPanel1 = new VerticalPanelWithSpacer(1);
        horizontalPanel.add(columnCompositePanelN1);
        HTML heading1 = new HTML("<p>&nbsp;</p>");
        columnCompositePanelN1.add(heading1);
        columnCompositePanelN1.add(verticalPanel1);
        columnDragController.makeDraggable(columnCompositePanelN1, heading1);
        VerticalPanel verticalPanelPrefix = new VerticalPanel();
        setHorizontalAlignment(verticalPanelPrefix);
        Label labelPrefix = new Label(wfmStrings.prefix());
        labelPrefix.getElement().getStyle().setMarginBottom(7, Style.Unit.PX);
        verticalPanelPrefix.add(labelPrefix);
        verticalPanelPrefix.add(getPrefixN());
        verticalPanel1.add(verticalPanelPrefix);
        //columnCompositePanelN1.addStyleName("dragon-drop-window");
    }

    protected void getDatePanelForDragon(PickupDragController columnDragController) {
        getColumnCompositePanelN2();
        columnCompositePanelN2.getElement().setId(WIDGET_ALL_DATE);
        VerticalPanel verticalPanel2 = new VerticalPanelWithSpacer(1);
        horizontalPanel.add(columnCompositePanelN2);
        HTML heading2 = new HTML("<p>&nbsp;</p>");
        columnCompositePanelN2.add(heading2);
        columnCompositePanelN2.add(verticalPanel2);
        columnDragController.makeDraggable(columnCompositePanelN2, heading2);
        VerticalPanel verDates = new VerticalPanel();
        setHorizontalAlignment(verDates);
        HorizontalPanel flexTableDate = new HorizontalPanel();
        Label labelDate = new Label(wfmStrings.date());
        labelDate.getElement().getStyle().setMarginBottom(7, Style.Unit.PX);
        flexTableDate.add(labelDate);
        flexTableDate.add(getCheckBoxDateN());
        HorizontalPanel dayMonthYear = new HorizontalPanel();
        dayMonthYear.add(getCheckBoxNY());
        Label labelY = new Label("Y");
        dayMonthYear.add(labelY);
        dayMonthYear.add(new HTML("<p>&nbsp;</p>"));
        dayMonthYear.add(getCheckBoxNM());
        Label labelM = new Label("M");
        dayMonthYear.add(labelM);
        dayMonthYear.add(new HTML("<p>&nbsp;</p>"));
        dayMonthYear.add(getCheckBoxND());
        Label labelD = new Label("D");
        dayMonthYear.add(labelD);
        verDates.add(flexTableDate);
        verDates.add(dayMonthYear);
        getTextBoxNYMD();
        verticalPanel2.add(verDates);
        //columnCompositePanelN2.addStyleName("dragon-drop-window");
    }

    protected void getClientCodePanelForDragon(PickupDragController columnDragController) {
        getColumnCompositePanelN3();
        columnCompositePanelN3.getElement().setId(WIDGET_CLIENT_CODE);
        VerticalPanel verticalPanel3 = new VerticalPanelWithSpacer(1);
        horizontalPanel.add(columnCompositePanelN3);
        HTML heading3 = new HTML("<p>&nbsp;</p>");
        columnCompositePanelN3.add(heading3);
        columnCompositePanelN3.add(verticalPanel3);
        columnDragController.makeDraggable(columnCompositePanelN3, heading3);
        VerticalPanel verticalPanelCliend = new VerticalPanel();
        setHorizontalAlignment(verticalPanelCliend);
        HTML labelCliend = new HTML(wfmStrings.customerCode());
        labelCliend.getElement().getStyle().setMarginBottom(7, Style.Unit.PX);
        verticalPanelCliend.add(labelCliend);
        verticalPanelCliend.add(getClientNumber());
        verticalPanel3.add(verticalPanelCliend);
        //columnCompositePanelN3.addStyleName("dragon-drop-window");
    }

    protected void getNumberPanelForDragon(PickupDragController columnDragController) {
        getColumnCompositePanelN4();
        columnCompositePanelN4.getElement().setId(WIDGET_NUMBERS);
        VerticalPanel verticalPanel4 = new VerticalPanelWithSpacer(1);
        horizontalPanel.add(columnCompositePanelN4);
        HTML heading4 = new HTML("<p>&nbsp;</p>");
        columnCompositePanelN4.add(heading4);
        columnCompositePanelN4.add(verticalPanel4);
        columnDragController.makeDraggable(columnCompositePanelN4, heading4);
        VerticalPanel flexTableStrNumber = new VerticalPanel();
        setHorizontalAlignment(flexTableStrNumber);
        HTML startingNumber = new HTML(settingsStrings.startNumber());
        startingNumber.getElement().getStyle().setMarginBottom(7, Style.Unit.PX);
        flexTableStrNumber.add(startingNumber);
        FlexTable flexTableStrNumber2 = new FlexTable();
        flexTableStrNumber.add(flexTableStrNumber2);
        flexTableStrNumber2.setWidget(0, 0, getStartNumber1());
        flexTableStrNumber2.setWidget(0, 1, getStartNumber2());
        flexTableStrNumber2.setWidget(0, 2, getStartNumber3());
        flexTableStrNumber2.setWidget(0, 3, getStartNumber4());
        getAllNumber();
        verticalPanel4.add(flexTableStrNumber);
        //columnCompositePanelN4.addStyleName("dragon-drop-window");
    }

    protected void getSuffixPanelForDragon(PickupDragController columnDragController) {
        getColumnCompositePanelN5();
        columnCompositePanelN5.getElement().setId(WIDGET_SUFFIX);
        VerticalPanel verticalPanel5 = new VerticalPanelWithSpacer(1);
        horizontalPanel.add(columnCompositePanelN5);
        HTML heading5 = new HTML("<p>&nbsp;</p>");
        columnCompositePanelN5.add(heading5);
        columnCompositePanelN5.add(verticalPanel5);
        columnDragController.makeDraggable(columnCompositePanelN5, heading5);
        VerticalPanel flexTableSuffix = new VerticalPanel();
        setHorizontalAlignment(flexTableSuffix);
        HTML htmlSuffix = new HTML(wfmStrings.suffix());
        htmlSuffix.getElement().getStyle().setMarginBottom(7, Style.Unit.PX);
        flexTableSuffix.add(htmlSuffix);
        flexTableSuffix.add(getSuffix());
        verticalPanel5.add(flexTableSuffix);
        //columnCompositePanelN5.addStyleName("dragon-drop-window");
    }

    protected void getWithPrNumberPanelForDrag(PickupDragController columnDragControllerT) {
        getColumnCompositePanelN6();
        columnCompositePanelN6.getElement().setId(WIDGET_PROJECT_NUMBER);
        VerticalPanel verticalPanel6 = new VerticalPanelWithSpacer(1);
        horizontalPanel.add(columnCompositePanelN6);
        HTML heading6 = new HTML("<p>&nbsp;</p>");
        columnCompositePanelN6.add(heading6);
        columnCompositePanelN6.add(verticalPanel6);
        columnDragControllerT.makeDraggable(columnCompositePanelN6, heading6);
        VerticalPanel flexTableProjectNumber = new VerticalPanel();
        setHorizontalAlignment(flexTableProjectNumber);
        HTML restartNumberingHtml = new HTML(wfmStrings.projectNumber());
        restartNumberingHtml.getElement().getStyle().setMarginBottom(7, Style.Unit.PX);
        flexTableProjectNumber.add(restartNumberingHtml);
        flexTableProjectNumber.add(getWithPmNumber());
        verticalPanel6.add(flexTableProjectNumber);
        //columnCompositePanelN6.addStyleName("dragon-drop-window");
    }

    protected void getDelimiterAndPreviewPanel() {
        FlexTable flexTableDelimiter = new FlexTable();
        HTML htmlDelimiter = new HTML(wfmStrings.delimiter() + "&nbsp;&nbsp;&nbsp;"); // nbsp for space between field and label
        flexTableDelimiter.setWidget(1, 0, htmlDelimiter);
        flexTableDelimiter.setWidget(1, 1, getDelimiterN());
        flexTableDelimiter.addStyleName("mt-3 mb-3");

        HorizontalPanel horizontalPanelPr = new HorizontalPanel();
        horizontalPanelPr.setStyleName("previewPanelDrag");
        HTML labelPreview = new HTML(wfmStrings.preview() + "<span>:&nbsp;</span>");
        horizontalPanelPr.add(labelPreview);
        horizontalPanelPr.add(getPreview());

        HorizontalPanel horizontalDelVsPrew = new HorizontalPanel();
        horizontalDelVsPrew.setWidth("100%");
        horizontalDelVsPrew.add(flexTableDelimiter);
        horizontalDelVsPrew.add(horizontalPanelPr);
        verticalPanel.add(horizontalDelVsPrew);
    }

    public void setSelectedDate(String viewName, Date selectedDate) {
        if (restartWidgetByView.get(viewName) != null) {
            restartWidgetByView.get(viewName).setSelectedDate(selectedDate);
        }
    }

    protected void getRestartNumberWithDelimiterPanel(String viewName) {
        FlexTable flexTableDelimetrP = new FlexTable();
        flexTableDelimetrP.addStyleName("mt-3 mb-3");
        HTML htmlDelimiterP = new HTML(wfmStrings.delimiter() + "&nbsp;&nbsp;&nbsp;"); // nbsp for space between field and label
        flexTableDelimetrP.setWidget(1, 0, htmlDelimiterP);
        flexTableDelimetrP.setWidget(1, 1, getDelimiterN());

//        FlexTable restartNumber = new FlexTable();
//        getProjectRestartNumber();
//        restartNumber.setWidget(0, 0, projectRestartNumber);
//        HTML restartNumberingHtmlP = new HTML(wfmStrings.restartNumeringEveryYearOn() + "&nbsp;&nbsp;");
//        restartNumber.setWidget(0, 1, restartNumberingHtmlP);
//        restartNumber.addStyleName("table-checkbox");
//        restartNumberingHtmlP.addStyleName("table-checkbox__label");
//
//        dayOfMonthP = new DataListBox();
//        monthsNameP = new DataListBox();
//
//        initializeDayOfMonth(dayOfMonthP);
//        initializeMonthName(monthsNameP);
//
//        inputGroup = new InputGroup(dayOfMonthP, monthsNameP);
//        inputGroup.setVisible(false);
//
//        restartNumber.setWidget(0, 2, inputGroup);
//        nned to addd new widget

        HorizontalPanel horizontalPanelPrP = new HorizontalPanel();
        horizontalPanelPrP.setStyleName("previewPanelDrag");
        HTML labelPreviewP = new HTML(wfmStrings.preview() + "<b>:&nbsp;</b>");

        horizontalPanelPrP.add(labelPreviewP);
        horizontalPanelPrP.add(getPreview());

        HorizontalPanel horizontalDelVsPrew = new HorizontalPanel();
        horizontalDelVsPrew.setWidth("100%");
        horizontalDelVsPrew.add(flexTableDelimetrP);
        horizontalDelVsPrew.add(horizontalPanelPrP);
        verticalPanel.add(horizontalDelVsPrew);
        RestartDateWidget dateWidget = new RestartDateWidget();
        restartWidgetByView.put(viewName, dateWidget);
        verticalPanel.add(dateWidget);

    }

    protected void moveColumnUserPutAll() {

    }

    protected void setSettingsData() {

    }

    protected void setDefaultParameters() {

    }

    protected void setSettings(PMNumberingSettings numberingSettings) {
        if (settings == null) {
            settings = new PMNumberingSettings();
        }
        numberingSettings.setObjectID(settings.getObjectID());
    }

    protected void checkBoxSetValues(KpiCheckBox checkBoxY, KpiCheckBox checkBoxM, KpiCheckBox checkBoxD, boolean b) {
        checkBoxY.setValue(b);
        checkBoxM.setValue(b);
        checkBoxD.setValue(b);
    }

    protected String getDateParameters(KpiCheckBox checkBoxY, KpiCheckBox checkBoxM, KpiCheckBox checkBoxD) {
        String sb = (checkBoxY.getValue() ? y : "") +
                (checkBoxM.getValue() ? refactor(m) : "") +
                (checkBoxD.getValue() ? refactor(d) : "");
        return sb;
    }

    protected String getAllStartNumber(String text1, String text2, String text3, String text4) {
        String sb = (!"".equals(text1) ? text1 : "") +
                (!"".equals(text2) ? text2 : "") +
                (!"".equals(text3) ? text3 : "") +
                (!"".equals(text4) ? text4 : "");
        return sb;
    }

    protected TextBox createNumCell(int number) {
        final TextBox cell = new TextBox();
        cell.setWidth("30px");
        cell.setMaxLength(1);
        Validation.addNumericKeyboardListener(cell);
        return cell;
    }

    protected void clearAndReopen(HorizontalPanel horizontalPanel2, TextBox textBoxDelimiter, String delimiter) {
        horizontalPanel2.clear();
        textBoxDelimiter.setText(delimiter);
    }

    protected void clearPanels(String formula, String formulaType, String delimiter) {
        if (formula.contains(formulaType)) {
            clearAndReopen(horizontalPanel, delimiterN, delimiter);
        }
    }

    protected void setHorizontalAlignment(VerticalPanel verticalPanel) {
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    }

    protected void parseAndSetData(String numFormat, String delimiter, String defaultPrefix, TextBox prefix, final TextBox cell1, final TextBox cell2, final TextBox cell3, final TextBox cell4, final String formType) {
        if (numFormat != null && !"".equals(numFormat)) {
            if (numFormat.contains(WIDGET_PREFIX)) {
                clearPanels(numFormat, WIDGET_PREFIX, delimiter);
                String[] firstString = numFormat.split("/");
                for (String value : firstString) {
                    String[] split = value.split(":");
                    switch (split[0]) {
                        case WIDGET_PREFIX:
                            horizontalPanel.add(columnCompositePanelN1);
                            prefixN.setText(!"false".equals(split[1]) ? split[1] : "");
                            break;
                        case WIDGET_DATE_YEAR:
                            horizontalPanel.add(columnCompositePanelN2);
                            checkBoxNY.setValue(Boolean.valueOf(split[1]));
                            break;
                        case WIDGET_DATE_MONTH:
                            checkBoxNM.setValue(Boolean.valueOf(split[1]));
                            break;
                        case WIDGET_DATE_DAY:
                            checkBoxND.setValue(Boolean.valueOf(split[1]));
                            break;
                        case WIDGET_CLIENT_CODE:
                            horizontalPanel.add(columnCompositePanelN3);
                            clientNumber.setValue(Boolean.valueOf(split[1]));
                            break;
                        case WIDGET_NUMBERS:
                            horizontalPanel.add(columnCompositePanelN4);
                            startNumber1.setText(split[1].substring(0, 1));
                            startNumber2.setText(split[1].substring(1, 2));
                            startNumber3.setText(split[1].substring(2, 3));
                            startNumber4.setText(split[1].length() > 3 ? split[1].substring(3, 4) : null);
                            break;
                        case WIDGET_SUFFIX:
                            horizontalPanel.add(columnCompositePanelN5);
                            suffix.setValue(!"false".equals(split[1]) ? split[1] : "");
                            break;
                        case WIDGET_PROJECT_NUMBER:
                            horizontalPanel.add(columnCompositePanelN6);
                            withPmNumber.setValue(Boolean.valueOf(split[1]));
                            break;
                        case WIDGET_RESTART_NUMBER_EACH_PROJECT:
                            reapedNumber.setValue(Boolean.valueOf(split[1]));
                            break;
                        case WIDGET_UNIQUE_NUMBER_ALL_PROJECT:
                            uniqueNumber.setValue(Boolean.valueOf(split[1]));
                            break;
                    }
                }
                if (checkBoxDateN != null) {
                    if (checkBoxNY.getValue() && checkBoxNM.getValue() && checkBoxND.getValue()) checkBoxDateN.setValue(true);
                }
            } else {
                int splitterIndex = numFormat.lastIndexOf("_");
                prefix.setText(numFormat.substring(0, splitterIndex));
                char[] numbers = numFormat.substring(splitterIndex + 1).toCharArray();
                cell1.setText(String.valueOf(numbers[0]));
                cell2.setText(String.valueOf(numbers[1]));
                cell3.setText(String.valueOf(numbers[2]));
                cell4.setText(String.valueOf(numbers[3]));
                if (reapedNumber != null) {
                    reapedNumber.setValue(true);
                }
            }
            moveColumnUserPutAll();
        } else {
            defaultSettingsParameters(defaultPrefix, prefix, cell1, cell2, cell3, cell4, formType);
        }
    }

    private void defaultSettingsParameters(String defaultPrefix, TextBox prefix, final TextBox cell1, final TextBox cell2, final TextBox cell3, final TextBox cell4, String formType) {
        prefix.setText(defaultPrefix);
        cell1.setText("0");
        cell2.setText("0");
        cell3.setText("0");
        cell4.setText("1");
        if ("project".equals(formType)) {
            ProfileService.App.get().getProjectLastIntNumber(new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable throwable) {
                    cell1.setText("0");
                    cell2.setText("0");
                    cell3.setText("0");
                    cell4.setText("1");
                    moveColumnUserPutAll();
                }

                @Override
                public void success(String lastProjectNumber) {
                    char[] cArray = lastProjectNumber.toCharArray();
                    cell1.setText(String.valueOf(cArray[0]));
                    cell2.setText(String.valueOf(cArray[1]));
                    cell3.setText(String.valueOf(cArray[2]));
                    cell4.setText(String.valueOf(cArray[3]));
                    moveColumnUserPutAll();
                }
            });
        }
        if (uniqueNumber != null) {
            uniqueNumber.setValue(true);
        }
    }

    protected ArrayList<String> getOtherParameters(RadioButton reapedNumber, RadioButton uniqueNumber) {
        ArrayList<String> list = new ArrayList<>();
        list.add(reapedNumber.getValue() ? WIDGET_RESTART_NUMBER_EACH_PROJECT + ":true" : WIDGET_RESTART_NUMBER_EACH_PROJECT + ":false");
        list.add(uniqueNumber.getValue() ? WIDGET_UNIQUE_NUMBER_ALL_PROJECT + ":true" : WIDGET_UNIQUE_NUMBER_ALL_PROJECT + ":false");
        return list;
    }

    private String refactor(String s) {
        if (Integer.valueOf(s) < 10) {
            s = "0" + s;
        }
        return s;
    }

}
