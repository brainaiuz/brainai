package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Raimov M
 * Date: 3/01/22
 * Time: 2:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocalizationCFModal extends KpiModal implements Constants {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final AllInOneServiceAsync service = AllInOneService.App.get();
    protected static final CommonServiceAsync commonService = CommonService.App.get();

    private TextBox englishName, arabicName, russianName, uzbekName;
    private Integer objectId;
    private CustomFormLocalization localization;
    private DynamicTable fieldItems;
    private LinkedList<CustomFormLocalization> items;
    private final LocalizationTypeEnum type;
    private CustomFormLocalization item;
    private Integer localizationId;

    public LocalizationCFModal(Integer objectId, LocalizationTypeEnum type) {
        this.objectId = objectId;
        this.type = type;
        init();
    }

    public LocalizationCFModal(CustomFormLocalization item, LocalizationTypeEnum type) {
        this.localization = item;
        this.type = type;
        init();
    }

    private Command fieldLocalizationSaveCommand;

    public void setFieldLocalizationSaveCommand(Command fieldLocalizationSaveCommand) {
        this.fieldLocalizationSaveCommand = fieldLocalizationSaveCommand;
    }

    private void init() {
        setTitle(wfmStrings.localization());
        if (objectId == null && localization != null && localization.getId() != null) {
            objectId = localization.getId();
        }

        if (!type.equals(LocalizationTypeEnum.FORM) && !type.equals(LocalizationTypeEnum.SYSTEM_FORM)) {
            commonService.getCFLocalization(objectId, type, new AsyncCallback<CustomFormLocalization>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(CustomFormLocalization customFormLocalization) {
                    localization = customFormLocalization;
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LOCALIZATION_ADD, localization, LocalizationCFModal.this);
                    initFields();
                }
            });
        } else {
            initFields();
        }
    }

    private void setValues() {
        items = new LinkedList<>();
        if (fieldItems != null) {
            for (int i = 0; i < fieldItems.getRowNumber(); i++) {
                DynamicTableItem tableItem = fieldItems.getItem(i);
                TextBox english = (TextBox) tableItem.getColumnById("English");
                TextBox arabic = (TextBox) tableItem.getColumnById("Arabic");
                TextBox russian = (TextBox) tableItem.getColumnById("Russian");
                TextBox uzbek = (TextBox) tableItem.getColumnById("Uzbek");

                CustomFormLocalization item = new CustomFormLocalization();
                item.setId(tableItem.getObjectId());
                item.setEnglishName(english.getText());
                item.setArabicName(arabic.getText());
                item.setRussianName(russian.getText());
                item.setUzbekName(uzbek.getText());
                items.add(item);
            }
        } else {
            if (localization == null) {
                localization = new CustomFormLocalization();
            }
            localization.setEnglishName(englishName.getValue());
            localization.setArabicName(arabicName.getValue());
            localization.setRussianName(russianName.getValue());
            localization.setUzbekName(uzbekName.getValue());
            items.add(localization);
        }


    }

    private void initFields() {
        englishName = new TextBox();
        englishName.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);
        englishName.ensureDebugId("english-name");
        arabicName = new TextBox();
        arabicName.ensureDebugId("arabic-name");
        arabicName.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);
        russianName = new TextBox();
        russianName.ensureDebugId("russian-name");
        russianName.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);
        uzbekName = new TextBox();
        uzbekName.ensureDebugId("uzbek-name");
        uzbekName.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);
        GWT.log("item Max length " + englishName.getMaxLength());

        addButton(new WfmButton2(wfmStrings.cancel(), event -> close()));
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            setValues();
            if (LocalizationTypeEnum.SYSTEM_FORM.equals(type)) {
                if (fieldLocalizationSaveCommand != null) {
                    fieldLocalizationSaveCommand.execute();
                }
            } else {
                save();
            }
            close();
        }));

        if (localization != null && !type.equals(LocalizationTypeEnum.DASHBOARD_COMPONENT)
                && !type.equals(LocalizationTypeEnum.DASHBOARD_SUFFIX_COMPONENT)
                && !type.equals(LocalizationTypeEnum.DASHBOARD_DIFFERENCE_COMPONENT)
                && !type.equals(LocalizationTypeEnum.DASHBOARD_COMPARISON_COMPONENT)) {
            englishName.setText(localization.getEnglishName());
            arabicName.setText(localization.getArabicName());
            russianName.setText(localization.getRussianName());
            uzbekName.setText(localization.getUzbekName());
            if (localization.getChildren() != null && localization.getChildren().size() > 0) {
                fieldItems = new DynamicTable(getColumn(), false, true);
                drawRows(fieldItems, Arrays.asList(localization));
                add(fieldItems);
                setWidth("1000px");
            } else {
                GRow firstRow = new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup("English", englishName)),
                        new GColumn(GColumnEnum.COL_6, new FormGroup("Arabic", arabicName)));
                GRow secondRow = new GRow(new GColumn(GColumnEnum.COL_6, new FormGroup("Russian", russianName)),
                        new GColumn(GColumnEnum.COL_6, new FormGroup("Uzbek", uzbekName)));
                add(firstRow);
                add(secondRow);
                setWidth("700px");
            }
        } else {
            if (localization != null) {
                englishName.setText(localization.getEnglishName());
                arabicName.setText(localization.getArabicName());
                russianName.setText(localization.getRussianName());
                uzbekName.setText(localization.getUzbekName());
            }

            GRow firstRow = new GRow(new GColumn(GColumnEnum.COL_12, new FormGroup("English", englishName)));
            GRow secondRow = new GRow(new GColumn(GColumnEnum.COL_12, new FormGroup("Arabic", arabicName)));
            GRow thirdRow = new GRow(new GColumn(GColumnEnum.COL_12, new FormGroup("Russian", russianName)));
            GRow fourthRow = new GRow(new GColumn(GColumnEnum.COL_12, new FormGroup("Uzbek", uzbekName)));
            add(firstRow);
            add(secondRow);
            add(thirdRow);
            add(fourthRow);
            setWidth("400px");
        }
    }

    private void drawRows(DynamicTable fieldItems, List<CustomFormLocalization> localizations) {
        if (localizations != null) {
            for (CustomFormLocalization localization : localizations) {
                if (localization.getChildren() != null && !localization.getChildren().isEmpty()) {
                    fieldItems.addRow(localization.getId(), getWidget(localization));
                    drawRows(fieldItems, localization.getChildren());
                } else {
                    fieldItems.addRow(localization.getId(), getWidget(localization));
                }
            }
        }
    }

    public void save() {
        LoadingPanel.loading(true);
        service.saveCFLItems(items, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()), Info.Type.INFO);
            }
        });
    }

    private Widget[] getWidget(CustomFormLocalization item) {
        if (item == null) {
            item = new CustomFormLocalization();
        }
        Widget[] widgets = new Widget[4];

        TextBox english = new TextBox();
        english.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);
        english.setText(item.getEnglishName());

        TextBox arabic = new TextBox();
        arabic.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);
        arabic.setText(item.getArabicName());

        TextBox russian = new TextBox();
        russian.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);
        russian.setText(item.getRussianName());

        TextBox uzbek = new TextBox();
        uzbek.setMaxLength(CustomFormLocalization.MAX_LENGTH_OF_VALUE);
        uzbek.setText(item.getUzbekName());

        Integer index = 0;
        widgets[index++] = english;
        widgets[index++] = arabic;
        widgets[index++] = russian;
        widgets[index++] = uzbek;

        return widgets;
    }

    private DynamicTableColumn[] getColumn() {
        Integer index = 0;
        DynamicTableColumn[] columns = new DynamicTableColumn[4];

        columns[index] = new DynamicTableColumn("English", "English", new ColumnStatements("", ""), 200);
        columns[index++].setColumnName("English");

        columns[index] = new DynamicTableColumn("Arabic", "Arabic", new ColumnStatements("", ""), 200);
        columns[index++].setColumnName("Arabic");

        columns[index] = new DynamicTableColumn("Russian", "Russian", new ColumnStatements("", ""), 200);
        columns[index++].setColumnName("Russian");

        columns[index] = new DynamicTableColumn("Uzbek", "Uzbek", new ColumnStatements("", ""), 200);
        columns[index].setColumnName("Uzbek");

        return columns;
    }


    public CustomFormLocalization getLocalization() {
        if (Utils.isNullOrEmpty(arabicName.getText())
                && Utils.isNullOrEmpty(russianName.getText())
                && Utils.isNullOrEmpty(uzbekName.getText())
                && Utils.isNullOrEmpty(englishName.getText())
        ) {
            return null;
        }
        return localization;
    }

    public void setLocalization(CustomFormLocalization localization) {
        this.localization = localization;
    }

    public LocalizationTypeEnum getEnumType() {
        return this.type;
    }

}
