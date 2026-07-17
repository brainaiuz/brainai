package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.ContactPrivelegiesItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 24.09.2010
 * Time: 15:58:08
 */
public class WFTFooterPdfVew extends View {

	private static final BackendStrings backendStrings = BackendStrings.App.get();

	private static final WfmStrings wfmStrings = WfmStrings.App.get();

	private final Integer companyId;
	private HTML companyName;
	private KpiCheckBox showInvoicesPDFFooter;
	private KpiCheckBox showOtherPDFFooter;
    private KpiCheckBox showEmployeePDFFooter;

	public WFTFooterPdfVew(Integer companyId) {
		super("updateWftFooter", backendStrings.updatePDFs());
		this.companyId = companyId;
	}

	@Override
	public String getIconStyle() {
		return null;
	}

	@Override
	protected Widget onInitialize() {
		showInvoicesPDFFooter = new KpiCheckBox();
		showOtherPDFFooter = new KpiCheckBox();
        showEmployeePDFFooter = new KpiCheckBox();
        companyName = new HTML();

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), event -> save());

		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(10);
		flexTable.setCellPadding(10);
		flexTable.setWidget(0, 0, new HTML(getCustomTITLE(backendStrings.updatePDFs())));
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.setHTML(1, 0, getCustomTITLE(wfmStrings.company() + ":"));
		flexTable.setWidget(1, 1, companyName);
		flexTable.setHTML(2, 0, getCustomTITLE(backendStrings.showHostInfoInInvoicePDFFooters() + ":"));
		flexTable.setWidget(2, 1, showInvoicesPDFFooter);
        flexTable.setHTML(3, 0, getCustomTITLE(backendStrings.showHostInfoInAllPDFFooters() + ":"));
        flexTable.setWidget(3, 1, showOtherPDFFooter);
        flexTable.setHTML(4, 0, getCustomTITLE("Show employee details All PDF Footers:"));
        flexTable.setWidget(4, 1, showEmployeePDFFooter);
        flexTable.setWidget(5, 0, saveButton);
        flexTable.getFlexCellFormatter().setColSpan(5, 0, 2);
        flexTable.getFlexCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_CENTER);

		BackendService.App.get().getCompanyShownWFTFooterPDFs(companyId, new AbstractAsyncCallback<ContactPrivelegiesItem>() {
			@Override
			public void failure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

			@Override
			public void success(ContactPrivelegiesItem result) {
				companyName.setHTML("<b class=customTitle>" + result.getCompanyName() + "</b> (" + backendStrings.companyID() + ":" + companyId + ")");
				showInvoicesPDFFooter.setValue(result.isShowOrHide());
				showOtherPDFFooter.setValue(result.isYesOrNo() != null ? result.isYesOrNo() : false);
                showEmployeePDFFooter.setValue(result.getShowEmployeePDFFooter() != null ? result.getShowEmployeePDFFooter() : true);
            }
		});
		add(flexTable);
		return null;
	}

	private String getCustomTITLE(String title) {
		return "<b class=customTitle>" + title + "</b>";
	}

	private void save() {
		ContactPrivelegiesItem showHidePDFFooter = new ContactPrivelegiesItem();
		showHidePDFFooter.setShowOrHide(showInvoicesPDFFooter.getValue());
		showHidePDFFooter.setYesOrNo(showOtherPDFFooter.getValue());
        showHidePDFFooter.setShowEmployeePDFFooter(showEmployeePDFFooter.getValue());
        showHidePDFFooter.setCompanyID(companyId);
		BackendService.App.get().saveCompanyIsShownWFTFooter(showHidePDFFooter, new AbstractAsyncCallback<Void>() {
			@Override
			public void failure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

			@Override
			public void success(Void result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.footer()), Info.Type.WARNING);
                closeTab();
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