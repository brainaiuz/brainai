package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.localization.WftBackendMessages;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.TestCompanyItem;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SherzodMuratov
 * Date: 28.02.2009
 * Time: 10:53:33
 * To change this template use File | Settings | File Templates.
 */
public class SetTestCompanyView extends View {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WftBackendMessages backendMessages = WftBackendMessages.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();
	private WfmForm table;
	private WfmForm.Field titleField;
	private KpiCheckBox selectAll;
	private List<WfmForm.Field> validUsersFields = new ArrayList<>();

	public SetTestCompanyView() {
		super("settestcompany", backendStrings.setTestCompany());
	}

	public String getIconStyle() {
		return "icon-backend";
	}

	protected Widget onInitialize() {
		initInternal();
		return null;
	}

	private void initInternal() {
		table = new WfmForm(new String[]{"4%", "24%", "10%", "7%", "30%"});
		titleField = table.addField(null, new Widget[]{
				new HTML("<b class=customTitle>" + backendStrings.userNameOrEmail() + ":</b>"),
				new HTML("<b class=customTitle>" + backendStrings.markaTestCompany() + ":</b>")}, false);

		FlexTable flex = new FlexTable();
		flex.setCellPadding(15);
		flex.setCellSpacing(15);
		HTMLTable.CellFormatter cellFormatter = flex.getCellFormatter();
		cellFormatter.setWidth(0, 0, "3%");
		cellFormatter.setWidth(0, 1, "7%");
		cellFormatter.setWidth(0, 2, "17%");


        Image addMore = new Image();
        addMore.addStyleName("ficon--more");
        addMore.setStyleName("pointer");
		addMore.setTitle(wfmStrings.add());
		addMore.setSize("11px", "11px");

		selectAll = new KpiCheckBox();
		WfmButton2 button = new WfmButton2(wfmStrings.update());

		flex.setWidget(0, 0, addMore);
		HorizontalPanel select = new HorizontalPanel();
		select.add(selectAll);
		HTML selectDeselectAll = new HTML("<b class=customTitle>" + backendStrings.selectDeselectAll() + "</b>");
		selectDeselectAll.setWordWrap(false);
		select.add(selectDeselectAll);
		select.setSpacing(5);
		flex.setWidget(0, 2, select);
		flex.setWidget(1, 1, button);
		table.addOutButton(flex);
		add(table);

		for (int i = 0; i < 4; i++) {
			generateRow();
		}

		button.addClickHandler(event -> update());

		addMore.addClickHandler(sender -> generateRow());
		selectAll.addClickHandler(sender -> {

            for (Object o : table.getFields()) {
                WfmForm.Field field = (WfmForm.Field) o;
                if (field != titleField) {
                    Widget[] widgets = field.getWidgets();
                    KpiCheckBox select1 = (KpiCheckBox) widgets[1];
					select1.setValue(selectAll.getValue());
                }
            }
        });
	}

	private boolean isNull(Widget widget) {
		return (((TextBox) widget).getText() == null) || ("".equals(((TextBox) widget).getText()));
	}

	private void generateRow() {
		final WfmForm.Field field;
		TextBox username = new TextBox();
		KpiCheckBox companyTest = new KpiCheckBox();

        Image remove = new Image();
        remove.addStyleName("ficon--remove");
        remove.setStyleName("pointer");
		remove.setTitle(wfmStrings.delete());
		remove.setSize("11px", "11px");

		field = table.addField(null, new Widget[]{username, companyTest, remove}, false);

		remove.addClickHandler(sender -> table.removeField(field));
	}

	private void reInit() {
		for (WfmForm.Field validUsersField : validUsersFields) {
			Widget[] widgets = validUsersField.getWidgets();
			if (widgets != null) {
				((TextBox) widgets[0]).setText("");
				((KpiCheckBox) widgets[1]).setValue(false);
			}
		}
		selectAll.setValue(false);
	}

	private void update() {
		if (validate()) {
			TestCompanyItem[] items = new TestCompanyItem[validUsersFields.size()];
			for (int i = 0; i < validUsersFields.size(); i++) {
				Widget[] widgets = validUsersFields.get(i).getWidgets();
				if (widgets != null) {
					items[i] = new TestCompanyItem();
					if (!isNull(widgets[0])) {
						items[i].setUsername(((TextBox) widgets[0]).getText());
					}
					if (((KpiCheckBox) widgets[1]).getValue()) {
						items[i].setTestCompany(true);
					}
				}
			}
			LoadingPanel.loading(true);
			BackendService.App.get().updateCompanyAsTest(items, new AbstractAsyncCallback<String>() {
				public void failure(Throwable caught) {
					LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

				public void success(String users) {
					LoadingPanel.loading(false);
					WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
					messageBox.setTitle(wfmStrings.information());

					if (users.length() > 0) {
						messageBox.setSize("100", "100");
						messageBox.setMessage(backendMessages.userNamesAreUSERSCOUNTHasUpdatedToATestEmail(users, "test@workforcetrack.com"));
					} else {
						messageBox.setMessage(backendStrings.thereAreNoExistingUserNamesEmailsInDatabase());
					}
					messageBox.addCloseHandler(popupPanelCloseEvent -> reInit());
					messageBox.open();
				}
			});
		}
	}

	private boolean validate() {
		boolean valid = false;
		table.cleanupErrors();
		int errors = 0;
		int totalError = 0;
		int blankErrors = 0;
		validUsersFields = new ArrayList<>();
		for (Object o : table.getFields()) {
			WfmForm.Field field = (WfmForm.Field) o;
			if (field != titleField) {
				Widget[] widgets = field.getWidgets();
				if (widgets != null) {
					errors = 0;
					blankErrors = 0;
					if (!Validation.validateEmailRequired((TextBox) widgets[0], new HTML(), false)) {
						if (!isNull(widgets[0])) {
							totalError++;
							errors++;
						}
					}
					if (isNull(widgets[0])) {
						blankErrors++;
					}
				}
				if (errors == 0 && blankErrors == 0) {
					validUsersFields.add(field);
				}
			}
		}
		if (validUsersFields.size() > 0 && totalError == 0) {
			valid = true;
		} else {
			if (validUsersFields.size() == 0 && totalError == 0) {
                Info.show(backendStrings.youHaveNotEnteredAnyData(), Info.Type.WARNING);
            } else {
                Info.show(backendStrings.pleaseEnterDataByEmailOrder(), Info.Type.WARNING);
            }
		}

		return valid;
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