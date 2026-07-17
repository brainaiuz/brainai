package com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.InOutPairDto;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.widget.LinkCellWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.List;

public class DetailedInOutView extends KpiSideNavBox {

    interface DetailedInOutViewUiBinder extends UiBinder<Widget, DetailedInOutView> {
    }

    private static final DetailedInOutViewUiBinder uiBinder = GWT.create(DetailedInOutViewUiBinder.class);

    private static final DateTimeFormat TIME_FORMAT = DateTimeFormat.getFormat("HH:mm");
    private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("dd/MM/yyyy");
    public static NumberFormat numberFormat = NumberFormat.getFormat("#.00");
    private EditableTable table;
    private Integer employeeId;
    private DateNonConvertable date;
    private String imageUrl;
    private String position;
    private String name;
    private String totalTime = "";

    @UiField HTMLPanel htmlPanel;

    @UiField MaterialPanel avatarWrapper;
    @UiField MaterialImage avatarImg;

    @UiField MaterialPanel textWrapper;
    @UiField MaterialLabel nameLabel;
    @UiField MaterialLabel positionLabel;
    @UiField MaterialLabel dateLabel;

    @UiField MaterialPanel statsPanel;
    @UiField MaterialPanel extraStatPanel;
    @UiField MaterialLabel percentLabel1;
    @UiField MaterialLabel percentLabel2;
    @UiField MaterialLabel percentLabel3;
    @UiField MaterialLabel lateRateLabel;
    @UiField MaterialLabel earlyLeaveLabel;
    @UiField MaterialLabel attendanceLabel;

    @UiField MaterialPanel sheet;

    @UiField MaterialPanel rowsPanel;
    @UiField MaterialButton pdfBtn;



    public DetailedInOutView(Integer employeeId, DateNonConvertable date, String imageUrl, String name,String position) {
        super(true, KpiSideNavBox.DEFAULT_WIDTH);
        setStyleName(getElement(), "trmnlAtt", true);
        this.employeeId = employeeId;
        this.date = date;
        this.imageUrl = imageUrl;
        this.position = position;
        this.name = name;
        uiBinder.createAndBindUi(this);
        initialize();
    }

private void initialize() {
    // 1. Данные
    if (imageUrl != null && !imageUrl.isEmpty()) {
        avatarImg.setUrl(imageUrl);
        avatarWrapper.getElement().getStyle()
                .setProperty("backgroundImage", "url('" + imageUrl + "')");
    }
    nameLabel.setText(name);
    positionLabel.setText(position);
    dateLabel.setText("DATE " + DateUtils.format(date.getNonConvertedDate()));

    lateRateLabel.setText(wfmStrings.lateRate());
    earlyLeaveLabel.setText(wfmStrings.earlyRate());
    attendanceLabel.setText(wfmStrings.attendance() + " (%)");

    // 2. Загрузка данных таблицы и процентов
    pdfBtn.addClickHandler(e -> exportPdf());

    table = new EditableTable(getColumns(), false, false);
    table.setWidth("100%");
    rowsPanel.add(table);
    getInOutRecords();
    getPercentages();

    // 3. РАБОТА С ХЕДЕРОМ
    // Удаляем стандартный блок заголовка (side-nav__title), он нам не нужен
    getContentHeader().removeFromParent();

    // Используем метод базового класса, который кладет всё прямо в side-nav__heading
    addHeaderContainer(avatarWrapper);
    addHeaderContainer(textWrapper);

    // Теперь получаем доступ к самому side-nav__heading, чтобы добавить ему ваш класс
    com.google.gwt.dom.client.Element heading = avatarWrapper.getElement().getParentElement();
    if (heading != null) {
        heading.addClassName("trmnlAtt-head");
    }

    // 4. Тело
    addBody(sheet);
}

    private void getPercentages() {
        AvailabilityService.App.get().getLateAndEarlyPercentage( date, employeeId,new AbstractAsyncCallback<Double[]>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show("There is no data in Terminal. Please check!", Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Double[] result) {
                percentLabel1.setText(result[0] != null ? (result[0] != 0 ? numberFormat.format( result[0]) : "0") + " %" : "N/A");
                percentLabel2.setText(result[1] != null ? (result[1] != 0 ? numberFormat.format(result[1]) : "0") + " %" : "N/A");
                percentLabel3.setText(result[2] != null ? (result[2] != 0 ? numberFormat.format(result[2]) : "0") + " %" : "N/A");
            }
        });
    }

    private void getInOutRecords() {
        AvailabilityService.App.get().getEmployeeInOutRecords(employeeId, date, new AbstractAsyncCallback<List<InOutPairDto>>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show("There is no data in Terminal. Please check!", Info.Type.WARNING);
            }

            @Override
            public void onSuccess(List<InOutPairDto> result) {
                initRows(result);
            }
        });
    }

    private void initRows(List<InOutPairDto> result) {
        int currentRow = 1;
        int totalMinutes = 0;
        if (result != null) {
            table.removeAllRows();
            for (InOutPairDto inOutPairDto : result) {
                List<Object> widgets = new ArrayList<>();
                EditableTextBox numberBox = new EditableTextBox();
                EditableTextBox inOutBox = new EditableTextBox();
                EditableTextBox actualBox = new EditableTextBox();
                LinkCellWidget spapshotCell = new LinkCellWidget(wfmStrings.image(),null);

                spapshotCell.setClickHandler(()-> {
                    initSnapshotCell(spapshotCell, inOutPairDto);
                });


                numberBox.setText(currentRow + "");
                inOutBox.setText((inOutPairDto.getInTime() != null ? TIME_FORMAT.format(inOutPairDto.getInTime().getNonConvertedDate()) : wfmStrings.na()) + "----" + (inOutPairDto.getOutTime() != null ? TIME_FORMAT.format(inOutPairDto.getOutTime().getNonConvertedDate()) : wfmStrings.na()));
                actualBox.setText(inOutPairDto.getDurationMinutes() != null ? formatMinutesToHHmm(inOutPairDto.getDurationMinutes().intValue() / 60) : "N/A");
                totalMinutes += inOutPairDto.getDurationMinutes() != null ? inOutPairDto.getDurationMinutes().intValue() : 0;
                numberBox.setEnabled(false);
                inOutBox.setEnabled(false);
                actualBox.setEnabled(false);
                widgets.add(numberBox);
                widgets.add(inOutBox);
                widgets.add(spapshotCell);
                widgets.add(actualBox);

                table.addRow(widgets.toArray());
                currentRow++;
            }
            List<Object> widgets = new ArrayList<>();
            EditableTextBox numberBox = new EditableTextBox();
            EditableTextBox inOutBox = new EditableTextBox();
            EditableTextBox actualBox = new EditableTextBox();
            LinkCellWidget spapshotCell = new LinkCellWidget("",null);
            numberBox.setText("");
            inOutBox.setText("");
            this.totalTime = totalMinutes != 0 ? formatMinutesToHHmm(totalMinutes / 60) : wfmStrings.na();
            actualBox.setText(this.totalTime);
            pdfBtn.setText(wfmStrings.totalTime().replace("&nbsp;", "") + ": " + this.totalTime);
            numberBox.setEnabled(false);
            inOutBox.setEnabled(false);
            actualBox.setEnabled(false);
            widgets.add(numberBox);
            widgets.add(inOutBox);
            widgets.add(spapshotCell);
            widgets.add(actualBox);
            table.addRow(widgets.toArray());
        }
    }

    private void initSnapshotCell(LinkCellWidget cell, InOutPairDto inOutPairDto) {
        inOutPairDto.setEmployeeName(name);
        inOutPairDto.setPosition(position);
        SnapshotView snapshotView = new SnapshotView(inOutPairDto);
        snapshotView.open();
    }

    private String formatMinutesToHHmm(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        String h = (hours < 10 ? "0" : "") + hours;
        String m = (mins < 10 ? "0" : "") + mins;
        return h + ":" + m;
    }


    private ColumnConfig[] getColumns() {
        ArrayList<ColumnConfig> columns = new ArrayList<>();
        columns.add(new ColumnConfig(CustomCell.class, "NO", Constants.No, 100));
        columns.add(new ColumnConfig(CustomCell.class, "IN", "IN/OUT", 100));
        columns.add(new ColumnConfig(LinkableCell.class, "SNAPSHOT", "SNAPSHOT", 100));
        columns.add(new ColumnConfig(CustomCell.class, "ACTUAL", "ACTUAL", 100));
        return columns.toArray(new ColumnConfig[]{});
    }

    private void exportPdf() {


    }
}