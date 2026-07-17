package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.view.ObjectCommand;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;

import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 22.01.16
 * Time: 12:34:10
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyViewPopup extends KpiModal {
    private AddCurrencyView currencyView;
    private Integer objectID;
    private Date date;
    private ObjectCommand command;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmButton2 saveButton;
    private WfmButton2 closeButton;

    public CurrencyViewPopup(Date date, ObjectCommand command) {
        this(null, date, command);
    }

    public CurrencyViewPopup(Integer objectID, Date date, ObjectCommand command) {
        this.objectID = objectID;
        this.date = date;
        this.command = command;
        setTitle(objectID != null ? wfmStrings.setExchangeRate() : wfmStrings.addCurrency());
        setWidth(600);
        init();
    }

    private void init() {
        Command closeCommand = () -> {
            setButtonsEnabled(true);
            close();
        };
        currencyView = new AddCurrencyView(objectID, date, command, closeCommand);
        currencyView.onInitialize();
        add(currencyView);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId("saveButton");
        saveButton.addClickHandler(clickEvent -> {
            setButtonsEnabled(currencyView.save());
        });

        closeButton = new WfmButton2(wfmStrings.cancel(), BTN_DEFAULT_OUTLINE, clickEvent -> {
            setButtonsEnabled(false);
            close();
        });
        saveButton.ensureDebugId("closeButton");

        addButton(closeButton);
        addButton(saveButton);
        open();
    }

    private void setButtonsEnabled(boolean enable) {
        saveButton.setEnabled(enable);
        closeButton.setEnabled(enable);
    }
}
