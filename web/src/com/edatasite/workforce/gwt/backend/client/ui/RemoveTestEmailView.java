package com.edatasite.workforce.gwt.backend.client.ui;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class RemoveTestEmailView extends View implements CommandConstants, Constants, Colapse {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();
	private TextArea testArea;

	public RemoveTestEmailView() {
		super("add", backendStrings.removeTestEmails());
	}

	public String getIconStyle() {
		return "icon-RemoveTestEmailView";
	}

	@Override
	protected Widget onInitialize() {
		initInternal();
		return null;
	}

	private void initInternal() {
		testArea = new TextArea();
		WfmButton2 remove = new WfmButton2(backendStrings.removeTestEmails());
		WfmForm table = new WfmForm();
		table.setStyleName("padding10");
		table.addField(backendStrings.emailIds(), testArea);

		remove.addClickHandler(sender -> BackendService.App.get().removetestmails(testArea.getText(), new AbstractAsyncCallback<String[]>() {

            public void failure(Throwable arg0) {
                Info.show(wfmStrings.failed(), Info.Type.WARNING);
            }

            public void success(String[] s) {
				if (s[0] == null) {
					WfmMessageBox messageBox;
					messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
                    messageBox.setTitle(wfmStrings.delete());
                    messageBox.setMessage(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()));
					messageBox.addCloseHandler(popupPanelCloseEvent -> closeTab());
					messageBox.open();
				} else {
					StringBuilder s1 = new StringBuilder();
					for (String value : s) {
						if (value != null) {
							s1.append(value).append(",");
						}
					}
					Info.show(s1 + backendStrings.theseMailIdsDoesntExist(), Info.Type.WARNING);
					closeTab();
				}
			}

        }));
		table.addButton(remove);
		add(table);
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