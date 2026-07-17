package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.backend.client.rpc.ContactPrivelegiesItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 13.08.2010
 * Time: 14:34:47
 * To change this template use File | Settings | File Templates.
 */
public class ContactPrivelegiesView extends View {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();

	private static final BackendServiceAsync backendService = BackendService.App.get();

	private DataListBox yesNoBox;
	private Button saveButton;
	private final Integer companyID;
	private HTML companyName;
	private HTML helpMessage;
	private static final Integer YES_ID = 0;
	private static final Integer NO_ID = 1;

	public ContactPrivelegiesView(Integer companyID) {
		super("contactPrivelegies", backendStrings.contactPrivelegies());
		this.companyID = companyID;
	}

	@Override
	public String getIconStyle() {
		return null;
	}

	@Override
	protected Widget onInitialize() {

		yesNoBox = new DataListBox();
		yesNoBox.setWidth("150px");
		yesNoBox.setWithoutNullLabel(true);
		SelectItem[] YES_NO_LIST = new SelectItem[]{
				new SelectItem(0, wfmStrings.yes()),
				new SelectItem(1, wfmStrings.no())};

		yesNoBox.setItems(YES_NO_LIST);
		yesNoBox.setSelected(NO_ID);

		saveButton = new Button(wfmStrings.save());
		saveButton.addClickHandler(event -> save());
		companyName = new HTML("");
		final FlexTable table = new FlexTable();
		table.setCellPadding(10);
		table.setCellSpacing(10);

		table.setHTML(0, 0, "<b class=customTitle>" + backendStrings.adminHasRightToSeePrivateContacts() + "</b>");
		table.getFlexCellFormatter().setColSpan(0, 0, 2);
        table.setHTML(1, 0, wfmStrings.companyName() + ":");
		table.setWidget(1, 1, companyName);
		helpMessage = new HTML(backendStrings.thisIsOldCompanyAndHasNoARecordInCompanySystemSettingsTableHELP());
		helpMessage.setVisible(false);
		table.setWidget(2, 0, helpMessage);
		table.getFlexCellFormatter().setColSpan(2, 0, 2);
		table.setWidget(3, 1, yesNoBox);
		table.setWidget(4, 1, saveButton);
		yesNoBox.setVisible(false);
		saveButton.setVisible(false);
		backendService.getContactPrivelegiesItem(companyID, new AbstractAsyncCallback<ContactPrivelegiesItem>() {
			@Override
			public void failure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

			@Override
			public void success(ContactPrivelegiesItem result) {
				if (result.isYesOrNo() != null) {
					companyName.setHTML(result.getCompanyName());
					yesNoBox.setVisible(true);
					saveButton.setVisible(true);
					helpMessage.setVisible(false);
					if (result.isYesOrNo()) {
						yesNoBox.setSelected(YES_ID);
					} else {
						yesNoBox.setSelected(NO_ID);
					}
				} else {
					companyName.setHTML("<a target=\"_blank\" href=" + result.getCompanyLoginLink() + ">" + backendStrings.shadowLoginTo() + ":" + result.getCompanyName() + "</a>");
					yesNoBox.setVisible(false);
					saveButton.setVisible(false);
					helpMessage.setVisible(true);
				}
			}
		});

		add(table);
		return null;
	}

	private void save() {
		boolean isPrivate = false;
		if (YES_ID.equals(yesNoBox.getSelectedItem().getId())) {
			isPrivate = true;
		}
		backendService.saveContactPrivelegies(isPrivate, companyID, new AbstractAsyncCallback<Void>() {
			@Override
			public void failure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

			@Override
			public void success(Void result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.contactPrivelegies()), Info.Type.WARNING);
            }
		});
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