package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 24.02.2009
 * Time: 17:00:37
 * To change this template use File | Settings | File Templates.
 */
public class BackendAddTaxView extends View {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();

	private TextBox amount;
	private WfmForm.Field amountField;
	private DataListBox country;
	private WfmForm.Field countryField;
	private TextBox name;
	private WfmForm.Field nameField;
	private WfmForm table;

	public BackendAddTaxView() {
        super("add", wfmStrings.addTaxRate());
	}

	public String getIconStyle() {
		return "icon-backendAddTaxView";
	}

	@Override
	protected Widget onInitialize() {
		table = new WfmForm();
		country = new DataListBox();
		name = new TextBox();
		amount = new TextBox();
		table.addStyleName("file--BackendAddTaxView");
		WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

		country.setWidth("200px");
		name.setWidth("200px");
		amount.setWidth("100px");

		amount.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                    && (key != (char) KeyCodes.KEY_BACKSPACE)
                    && (key != (char) KeyCodes.KEY_DELETE) && (key != (char) KeyCodes.KEY_ENTER)
                    && (key != (char) KeyCodes.KEY_HOME) && (key != (char) KeyCodes.KEY_END)
                    && (key != (char) KeyCodes.KEY_LEFT) && (key != (char) KeyCodes.KEY_UP)
                    && (key != (char) KeyCodes.KEY_RIGHT) && (key != (char) KeyCodes.KEY_DOWN)) {
                ((TextBox) event.getSource()).cancelKey();
            }
        });

		BackendService.App.get().getCountries(new AbstractAsyncCallback<SelectItem[]>() {
			public void failure(Throwable caught) {

			}

			public void success(SelectItem[] items) {
				country.setItems(items);
			}
		});

		countryField = table.addField(wfmStrings.country(), country);
        nameField = table.addField(wfmStrings.taxName(), name);
        amountField = table.addField(wfmStrings.taxRate(), amount);
		add(table);
		saveButton.addClickHandler(sender -> {
            if (!validate()) {
                return;
            }
            TaxData data = new TaxData();
            data.setCountryId(country.getSelectedItem().getId());
            data.setTaxName(name.getText());
            data.setTaxRate(new BigDecimal(amount.getText()));
            LoadingPanel.loading(true);
            BackendService.App.get().saveTaxRate(data, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
}

                public void success(Void result) {
                    LoadingPanel.loading(false);
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.setMessage(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.taxRate()));
                    messageBox.addCloseHandler(popupPanelCloseEvent -> refresh());
                    messageBox.open();
                }
            });
        });
		table.addButton(saveButton);
		return null;
	}

	private void refresh() {
		country.setSelectedIndex(0);
		name.setText("");
		amount.setText("");
	}

	private boolean validate() {
		int errors = 0;
		table.cleanupErrors();
		if (!Validation.validateListBoxRequired(country, countryField, backendStrings.pleaseChooseCountry())) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(name, nameField)) {
			errors++;
		}
		if (!Validation.validateTextBoxRequired(amount, amountField)) {
			errors++;
		}
		if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
		}
		return true;
	}

	public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess() {
				callback.onSuccess(onInitialize());
			}
		});
	}
}