package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: Sep 27, 2011
 * Time: 4:23:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackendSummaryView extends View {
	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();
	private final Integer companyId;

	public BackendSummaryView(Integer companyID) {
		super("summary", backendStrings.companySummary());
		this.companyId = companyID;
	}


	@Override
	public String getIconStyle() {
		return "icon-backendSummaryView";
	}

	protected Widget onInitialize() {
        LoadingPanel.loading(true);
        drawInitialize();
		return null;
	}

	private void drawInitialize() {
		BackendService.App.get().getCompany(companyId, new AbstractAsyncCallback<CompanyListItem>() {
			public void failure(Throwable caught) {
				LoadingPanel.loading(false);
			}

			public void success(CompanyListItem item) {
				if (item != null) {
					drawing(item);
				}
				LoadingPanel.loading(false);
			}
		});
	}

	private void drawing(CompanyListItem item) {

		DecoratedTabPanel yourReviewTabPanel = new DecoratedTabPanel();
		DecoratedTabPanel managerTabPanel = new DecoratedTabPanel();
		DecoratedTabPanel source = new DecoratedTabPanel();

		VerticalPanel vp = new VerticalPanel();
//		vp.setSize("99%", "99%");
		ScrollPanel scP1 = new ScrollPanel();
//		scP1.setSize("400px", "500px");
		scP1.getElement().setClassName("bknd-sum-cells__content");
		PreviewSectionField preview = new PreviewSectionField("20%", "80%");
		if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com")) {
			Anchor lnkCompany = new Anchor();
            lnkCompany.setHref(item.getCompanyLoginLink());
            lnkCompany.setTarget("_blank");
            lnkCompany.setHTML(item.getCompanyName());
            preview.addField(wfmStrings.companyName(), lnkCompany);
        } else {
            preview.addField(wfmStrings.companyName(), item.getCompanyName());
        }
        preview.addField(wfmStrings.country(), item.getCountry());
        preview.addField(wfmStrings.industry(), item.getIndustry());
        preview.addField(backendStrings.contactPerson(), item.getContactPerson());
        preview.addField(wfmStrings.email(), item.getEmail());
        preview.addField(wfmStrings.phone(), item.getPhone());
        preview.addField(backendStrings.hostName(), item.getHostName());
        preview.addField(backendStrings.singUpComputerIP(), item.getCompanySigupCompIP());
        preview.addField(wfmStrings.isPaid(), item.getUsagePlanPaymentType());
        preview.addField(backendStrings.paymentStatus(), item.getUsagePlanPaymentStatus());
        preview.addField(wfmStrings.period(), item.getPeriodStartDate() + "-" + item.getPeriodEndDate());
        preview.addField(wfmStrings.organizationType(), item.getOrgType());
        scP1.add(preview);
        vp.add(scP1);

		yourReviewTabPanel.add(vp, "<span class='icon-exclamation'></span>" + backendStrings.companySummary(), true);
		yourReviewTabPanel.selectTab(0);
//		yourReviewTabPanel.getDeckPanel().setSize("400px", "500px");
//		yourReviewTabPanel.getElement().getStyle().setMargin(20, com.google.gwt.dom.client.Style.Unit.PX);
		yourReviewTabPanel.getElement().setClassName("bknd-sum-cells__yourReviewTabPanel");

		VerticalPanel vp2 = new VerticalPanel();
//		vp2.setSize("99%", "99%");
		ScrollPanel scP = new ScrollPanel();
//		scP.setSize("400px", "500px");
		scP.getElement().setClassName("bknd-sum-cells__content");

		PreviewSectionField preview2 = new PreviewSectionField("20%", "80%");
		preview2.addField(backendStrings.companyID(), item.getCompanyID().toString());
        preview2.addField(wfmStrings.companyName(), item.getCompanyName());
		preview2.addField(wfmStrings.registeredDate(), DateUtils.preiewFormat(item.getRegistrationDate()));
		preview2.addField(backendStrings.firstAccessDate(), DateUtils.preiewFormat(item.getFirstAccessDate()));
		preview2.addField(backendStrings.lastAccessDate(), DateUtils.preiewFormat(item.getLastAccessDate()));
        preview2.addField(backendStrings.expirationDate(), DateUtils.preiewFormat(item.getUsagPlanEndDate()));
		preview2.addField(backendStrings.countAccess(), item.getAccessCount());
		preview2.addField(backendStrings.periodAccess(), String.valueOf(item.getPeriodAccess()));
		preview2.addField(backendStrings.countUser(), item.getUserCount().toString());
		preview2.addField(backendStrings.countActiveUsers(), item.getActiveUserCount() != null ? item.getActiveUserCount().toString() : "0");
        preview2.addField(backendStrings.signUpFrom(), item.getCompanySignedUpFrom());
        preview2.addField(backendStrings.promotionCode(), item.getPromoCode() != null ? item.getPromoCode() : wfmStrings.notAvailable());
        preview2.addField(backendStrings.countProduct(), item.getProjectCount());
		preview2.addField(backendStrings.countTask(), item.getTaskCount());
		preview2.addField(Property.get(Constants.TIMESHEET, backendStrings.countTimeSheet(), wfmStrings.timesheet()), item.getTimesheetCount().toString());
		preview2.addField(backendStrings.countClient(), item.getClientsCount());
		preview2.addField(backendStrings.countSupplier(), item.getSupplierCount().toString());
		preview2.addField(backendStrings.countLead(), item.getLeadCount().toString());
		preview2.addField(backendStrings.countContact(), item.getContactCount().toString());
		preview2.addField(backendStrings.countCRMTask(), item.getCrmtaskCount().toString());
		preview2.addField(backendStrings.countEvent(), item.getEventCount().toString());
		preview2.addField(backendStrings.countCase(), item.getCaseCount().toString());
		preview2.addField(backendStrings.countInvoice(), item.getInvoiceCount());
		preview2.addField(backendStrings.countExpense(), item.getExpenseCount().toString());
		preview2.addField(backendStrings.countProduct(), item.getProductCount().toString());
		preview2.addField(backendStrings.countFolder(), item.getFolderCount().toString());
		preview2.addField(backendStrings.countFile(), item.getFileCount().toString());

		scP.add(preview2);
		vp2.add(scP);

		managerTabPanel.add(vp2, "<span class='icon-exclamation'></span>" + backendStrings.companyStatistics(), true);
		managerTabPanel.selectTab(0);
//		managerTabPanel.getDeckPanel().setSize("330px", "330px");
//		managerTabPanel.getElement().getStyle().setMargin(20, com.google.gwt.dom.client.Style.Unit.PX);

        VerticalPanel vp3 = new VerticalPanel();
//        vp.setSize("99%", "99%");
        ScrollPanel scP3 = new ScrollPanel();
//        scP3.setSize("700px", "165px");
		scP3.getElement().setClassName("bknd-sum-cells__content");

        PreviewSectionField preview3 = new PreviewSectionField("8%", "92%");
        preview3.addField(wfmStrings.campaign(), item.getCompaing());
        preview3.addField(wfmStrings.source(), item.getSource());
//        preview3.addField(backendStrings.affiliate(), item.getAffiliate());
        preview3.addField("Medium", item.getMedium());
        preview3.addField("Redirected", item.getRedirected());
        preview3.addField("Referrer", item.getReferrer());
        preview3.addField("Gclid", item.getGclid());

        scP3.add(preview3);
        vp3.add(scP3);

        source.add(vp3, "<span class='icon-exclamation'></span>" + wfmStrings.source(), true);
        source.selectTab(0);
//        source.getDeckPanel().setSize("700px", "165px");
//        source.getElement().getStyle().setMargin(20, com.google.gwt.dom.client.Style.Unit.PX);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(yourReviewTabPanel);
		horizontalPanel.add(managerTabPanel);
//        horizontalPanel.setSize("315px", "250px");
		horizontalPanel.getElement().setClassName("bknd-sum-cells");
        VerticalPanel ver = new VerticalPanel();
        ver.add(horizontalPanel);
        ver.add(source);

		add(ver);
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