package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.ActivationLinkList;
import com.edatasite.workforce.gwt.backend.client.rpc.ActivationLinkListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

public class ActivationLinkView extends View {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();
	private Integer companyID;
	private KpiDataGrid<ActivationLinkListItem> dataGrid;

	public ActivationLinkView(Integer companyID) {
		super("activationLink", backendStrings.getActivationLink());
		this.companyID = companyID;
	}

	public String getIconStyle() {
		return "icon-getActivationLink";
	}

	public static final ProvidesKey<ActivationLinkListItem> KEY_PROVIDER = item -> item.getActivationLink();

	@Override
	protected Widget onInitialize() {
		dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
		dataGrid.setSize("100%", "100%");
		dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(backendStrings.sorryThereIsNoInactiveUsers(), "", null));
		initInternal();
		initTableColumn();
		add(dataGrid);
		return null;
	}

	private void initTableColumn() {
		//Employee First Name
		Column<ActivationLinkListItem, String> firstName = new Column<ActivationLinkListItem, String>(new TextCell()) {
			@Override
			public String getValue(ActivationLinkListItem object) {
				return object.getFisrtName();
			}
		};
		dataGrid.addColumn(firstName, wfmStrings.firstName());
		dataGrid.setColumnWidth(firstName, 15, com.google.gwt.dom.client.Style.Unit.PCT);
		//Employee Last Name
		Column<ActivationLinkListItem, String> lastName = new Column<ActivationLinkListItem, String>(new TextCell()) {
			@Override
			public String getValue(ActivationLinkListItem object) {
				return object.getLastName();
			}
		};
		dataGrid.addColumn(lastName, wfmStrings.lastName());
		dataGrid.setColumnWidth(lastName, 15, com.google.gwt.dom.client.Style.Unit.PCT);
		//Employee Email
		Column<ActivationLinkListItem, String> email = new Column<ActivationLinkListItem, String>(new TextCell()) {
			@Override
			public String getValue(ActivationLinkListItem object) {
				return object.getEmail();
			}
		};
		dataGrid.addColumn(email, wfmStrings.email());
		dataGrid.setColumnWidth(email, 15, com.google.gwt.dom.client.Style.Unit.PCT);
		//Activation Link
		Column<ActivationLinkListItem, String> activationLink = new Column<ActivationLinkListItem, String>(new TextCell()) {
			@Override
			public String getValue(ActivationLinkListItem object) {
				return object.getActivationLink();
			}
		};
		dataGrid.addColumn(activationLink, backendStrings.activationLink());
		dataGrid.setColumnWidth(activationLink, 60, com.google.gwt.dom.client.Style.Unit.PCT);
	}

	private void initInternal() {
        LoadingPanel.loading(true);
        BackendService.App.get().getActivationLinkList(companyID, new AbstractAsyncCallback<ActivationLinkList>() {
			public void failure(Throwable arg0) {
                LoadingPanel.loading(false);
            }

			public void success(ActivationLinkList result) {
                LoadingPanel.loading(false);
                if (result != null) {
					ActivationLinkListItem activationLinkListItem[] = result.getResults();
					if (activationLinkListItem != null) {
						viewShowItems(activationLinkListItem);
					}
				}
			}
		});
	}

	private void viewShowItems(ActivationLinkListItem[] linkListItems) {
		if (linkListItems != null && linkListItems.length > 0) {
			dataGrid.supplyProvider(linkListItems);
			dataGrid.refresh();
		}
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