package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.MoreMenuUpdateItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/22/11
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoreMenuUpdateView extends View implements Constants {

	private static final BackendStrings backendStrings = BackendStrings.App.get();
	private static final WfmStrings wfmStrings = WfmStrings.App.get();

	private final Integer companyID;
	private HTML companyName;
	private KpiCheckBox enabledAndroidBox;
	//private CheckBox enabledApprovalFormsBox;
	//private CheckBox enabledCatalogsDirectoriesBox;
	//private CheckBox enabledExcelBox;
	private KpiCheckBox enabledIPhoneBox;
	//private CheckBox enabledGoogleAnalyticsBox;
	private KpiCheckBox enabledLeadCaptureBox;
	private KpiCheckBox enabledMassMailingBox;
	//private CheckBox enabledOutlookBox;
	private KpiCheckBox enabledReportingDashboardBox;
	private KpiCheckBox enabledStoreFrontBox;
	//private CheckBox enabledSurveysPollsBox;
	private KpiCheckBox enabledWebFormsBox;
	private KpiCheckBox enabledWebSitesBox;
	private KpiCheckBox enableWFTMoreMenuForADMIN;
	private KpiCheckBox enableWFTMoreMenuForMEM;

	private TextBox linkNameAndroidBox;
	private TextBox linkNameApprovalFormsBox;
	private TextBox linkNameCatalogsDirectoriesBox;
	private TextBox linkNameExcelBox;
	private TextBox linkNameIPhoneBox;
	private TextBox linkNameGoogleAnalyticsBox;
	private TextBox linkNameLeadCaptureBox;
	private TextBox linkNameMassMailingBox;
	private TextBox linkNameOutlookBox;
	private TextBox linkNameReportingDashboardBox;
	private TextBox linkNameStoreFrontBox;
	private TextBox linkNameSurveysPollsBox;
	private TextBox linkNameWebFormsBox;
	private TextBox linkNameWebSitesBox;

	public MoreMenuUpdateView(Integer companyID) {
		super("updateMoreMenuItem", backendStrings.updateMoreMenu());
		this.companyID = companyID;
	}

	@Override
	public String getIconStyle() {
		return "icon=MoreMenuUpdateView";
	}

	@Override
	protected Widget onInitialize() {

		companyName = new HTML();

		enabledStoreFrontBox = new KpiCheckBox();
		linkNameStoreFrontBox = new TextBox();
		linkNameStoreFrontBox.setWidth("300px");
		linkNameStoreFrontBox.setVisible(false);
		setCheckedAndVisible(enabledStoreFrontBox, linkNameStoreFrontBox);

		enabledWebSitesBox = new KpiCheckBox();
		linkNameWebSitesBox = new TextBox();
		linkNameWebSitesBox.setWidth("300px");
		linkNameWebSitesBox.setVisible(false);
		setCheckedAndVisible(enabledWebSitesBox, linkNameWebSitesBox);

		/*enabledCatalogsDirectoriesBox = new CheckBox();
		linkNameCatalogsDirectoriesBox = new TextBox();
		linkNameCatalogsDirectoriesBox.setWidth("300px");
		linkNameCatalogsDirectoriesBox.setVisible(false);
		setCheckedAndVisible(enabledCatalogsDirectoriesBox, linkNameCatalogsDirectoriesBox);
*/
		enabledWebFormsBox = new KpiCheckBox();
		linkNameWebFormsBox = new TextBox();
		linkNameWebFormsBox.setWidth("300px");
		linkNameWebFormsBox.setVisible(false);
		setCheckedAndVisible(enabledWebFormsBox, linkNameWebFormsBox);

		/*enabledApprovalFormsBox = new CheckBox();
		linkNameApprovalFormsBox = new TextBox();
		linkNameApprovalFormsBox.setWidth("300px");
		linkNameApprovalFormsBox.setVisible(false);
		setCheckedAndVisible(enabledApprovalFormsBox, linkNameApprovalFormsBox);*/

		/*enabledSurveysPollsBox = new CheckBox();
		linkNameSurveysPollsBox = new TextBox();
		linkNameSurveysPollsBox.setWidth("300px");
		linkNameSurveysPollsBox.setVisible(false);
		setCheckedAndVisible(enabledSurveysPollsBox, linkNameSurveysPollsBox);*/

		enabledMassMailingBox = new KpiCheckBox();
		linkNameMassMailingBox = new TextBox();
		linkNameMassMailingBox.setWidth("300px");
		linkNameMassMailingBox.setVisible(false);
		setCheckedAndVisible(enabledMassMailingBox, linkNameMassMailingBox);

		enabledLeadCaptureBox = new KpiCheckBox();
		linkNameLeadCaptureBox = new TextBox();
		linkNameLeadCaptureBox.setWidth("300px");
		linkNameLeadCaptureBox.setVisible(false);
		setCheckedAndVisible(enabledLeadCaptureBox, linkNameLeadCaptureBox);

		/*enabledGoogleAnalyticsBox = new CheckBox();
		linkNameGoogleAnalyticsBox = new TextBox();
		linkNameGoogleAnalyticsBox.setWidth("300px");
		linkNameGoogleAnalyticsBox.setVisible(false);
		setCheckedAndVisible(enabledGoogleAnalyticsBox, linkNameGoogleAnalyticsBox);*/

		enabledReportingDashboardBox = new KpiCheckBox();
		linkNameReportingDashboardBox = new TextBox();
		linkNameReportingDashboardBox.setWidth("300px");
		linkNameReportingDashboardBox.setVisible(false);
		setCheckedAndVisible(enabledReportingDashboardBox, linkNameReportingDashboardBox);

		/*enabledOutlookBox = new CheckBox();
		linkNameOutlookBox = new TextBox();
		linkNameOutlookBox.setWidth("300px");
		linkNameOutlookBox.setVisible(false);
		setCheckedAndVisible(enabledOutlookBox, linkNameOutlookBox);*/

		/*enabledExcelBox = new CheckBox();
		linkNameExcelBox = new TextBox();
		linkNameExcelBox.setWidth("300px");
		linkNameExcelBox.setVisible(false);
		setCheckedAndVisible(enabledExcelBox, linkNameExcelBox);*/

		enabledAndroidBox = new KpiCheckBox();
		linkNameAndroidBox = new TextBox();
		linkNameAndroidBox.setWidth("300px");
		linkNameAndroidBox.setVisible(false);
		setCheckedAndVisible(enabledAndroidBox, linkNameAndroidBox);

		enabledIPhoneBox = new KpiCheckBox();
		linkNameIPhoneBox = new TextBox();
		linkNameIPhoneBox.setWidth("300px");
		linkNameIPhoneBox.setVisible(false);
		setCheckedAndVisible(enabledIPhoneBox, linkNameIPhoneBox);

		Button saveButton = new Button(wfmStrings.save(), (ClickHandler) clickEvent -> save());

		FlexTable generateTable = new FlexTable();
		generateTable.setCellSpacing(10);
		generateTable.setCellPadding(10);

		generateTable.setHTML(0, 0, "<b class=customTitle style='font-size:14px;'>" + backendStrings.updateMoreMenu() + "</b>");
		generateTable.getFlexCellFormatter().setColSpan(0, 0, 3);
		generateTable.setWidget(1, 0, companyName);
		generateTable.getFlexCellFormatter().setColSpan(1, 0, 3);

		//E-commerce
		generateTable.setHTML(2, 0, getBold(wfmStrings.storefront()));
		generateTable.setWidget(2, 1, enabledStoreFrontBox);
		generateTable.setWidget(2, 2, linkNameStoreFrontBox);

		generateTable.setHTML(3, 0, getBold(wfmStrings.websites()));
		generateTable.setWidget(3, 1, enabledWebSitesBox);
		generateTable.setWidget(3, 2, linkNameWebSitesBox);

		/*generateTable.setHTML(4, 0, getBold(wfmStrings.catalogsAndDirectories()));
		generateTable.setWidget(4, 1, enabledCatalogsDirectoriesBox);
		generateTable.setWidget(4, 2, linkNameCatalogsDirectoriesBox);*/

		//Custom forms
		generateTable.setHTML(4, 0, getBold(backendStrings.webForms()));
        generateTable.setWidget(4, 1, enabledWebFormsBox);
		generateTable.setWidget(4, 2, linkNameWebFormsBox);

		//Marketing
		generateTable.setHTML(5, 0, getBold(wfmStrings.workspaceMassMailing()));
		generateTable.setWidget(5, 1, enabledMassMailingBox);
		generateTable.setWidget(5, 2, linkNameMassMailingBox);

		generateTable.setHTML(6, 0, getBold(wfmStrings.leadCaptureForms()));
		generateTable.setWidget(6, 1, enabledLeadCaptureBox);
		generateTable.setWidget(6, 2, linkNameLeadCaptureBox);

		generateTable.setHTML(7, 0, getBold(wfmStrings.reportingAndDashboard()));
		generateTable.setWidget(7, 1, enabledReportingDashboardBox);
		generateTable.setWidget(7, 2, linkNameReportingDashboardBox);

		//Mobile Apps
		generateTable.setHTML(8, 0, getBold(wfmStrings.android()));
		generateTable.setWidget(8, 1, enabledAndroidBox);
		generateTable.setWidget(8, 2, linkNameAndroidBox);

		generateTable.setHTML(9, 0, getBold(wfmStrings.iPhone()));
		generateTable.setWidget(9, 1, enabledIPhoneBox);
		generateTable.setWidget(9, 2, linkNameIPhoneBox);


		generateTable.setWidget(10, 0, saveButton);
		generateTable.getFlexCellFormatter().setColSpan(10, 0, 3);
		generateTable.getFlexCellFormatter().setHorizontalAlignment(10, 0, HasHorizontalAlignment.ALIGN_CENTER);

//        add(generateTable);

		//more menu shown/hidden option - BEGIN
		enableWFTMoreMenuForMEM = new KpiCheckBox(backendStrings.showMoreMenuSettingsForMembers());
		enableWFTMoreMenuForADMIN = new KpiCheckBox(backendStrings.showMoreMenuSettingsForALLUsers());

		FlexTable moreMenuEnable = new FlexTable();
		moreMenuEnable.setCellSpacing(5);
		moreMenuEnable.setHTML(0, 0, "<div class=line></div>");
		moreMenuEnable.getFlexCellFormatter().setColSpan(0, 0, 3);
		moreMenuEnable.setWidget(1, 0, enableWFTMoreMenuForMEM);
		moreMenuEnable.setWidget(2, 0, enableWFTMoreMenuForADMIN);
		Button saveEnableWFTMoreMenuButton = new Button(wfmStrings.update(), (ClickHandler) event -> {
            LoadingPanel.loading(true);
            BackendService.App.get().saveEnableWFTMoreMenu(enableWFTMoreMenuForMEM.getValue(), enableWFTMoreMenuForADMIN.getValue(), companyID, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
}

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()), Info.Type.INFO);
                }
            });
        });
		moreMenuEnable.setWidget(3, 1, saveEnableWFTMoreMenuButton);
//		add(moreMenuEnable);
		//more menu shown/hidden option - END
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(generateTable);
		verticalPanel.add(moreMenuEnable);
		add(verticalPanel);

		LoadingPanel.loading(true);
		BackendService.App.get().getMoreMenuItems(companyID, new AbstractAsyncCallback<MoreMenuUpdateItem>() {
			@Override
			public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

			@Override
			public void success(MoreMenuUpdateItem moreMenuUpdateItem) {
				LoadingPanel.loading(false);
				generateMoreMenu(moreMenuUpdateItem);
			}
		});
		return null;
	}

	private String getBold(String s) {
		return "<b>" + s + "<b>";
	}

	private void generateMoreMenu(MoreMenuUpdateItem item) {
		if (item != null) {
            companyName.setHTML(wfmStrings.companyName() + ": <b class=customTitle>" + item.getCompanyName() + "</b> (" + backendStrings.companyID() + ": " + companyID + ")");
			enableWFTMoreMenuForMEM.setValue(item.isEnableWFTMoreMenuForMEM());
			enableWFTMoreMenuForADMIN.setValue(item.isEnableWFTMoreMenuForADMIN());
			if (item.getMoreMenuItems() != null) {
				item.getMoreMenuItems();
				for (SelectItem selectItem : item.getMoreMenuItems()) {
					if (MORE_MENU_STOREFRONT.equals(selectItem.getName())) {
						enabledStoreFrontBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
						linkNameStoreFrontBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
						if (enabledStoreFrontBox.getValue()) {
							linkNameStoreFrontBox.setVisible(true);
						}
					} else {
						if (MORE_MENU_WEB_SITES.equals(selectItem.getName())) {
							enabledWebSitesBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
							linkNameWebSitesBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
							if (enabledWebSitesBox.getValue()) {
								linkNameWebSitesBox.setVisible(true);
							}
						} else {
							if (MORE_MENU_CATALOG_AND_DIRECTORIES.equals(selectItem.getName())) {
							/*enabledCatalogsDirectoriesBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
							linkNameCatalogsDirectoriesBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
							if (enabledCatalogsDirectoriesBox.getValue()) {
								linkNameCatalogsDirectoriesBox.setVisible(true);
							}*/
							} else {
								if (MORE_MENU_WEB_FORMS.equals(selectItem.getName())) {
									enabledWebFormsBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
									linkNameWebFormsBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
									if (enabledWebFormsBox.getValue()) {
										linkNameWebFormsBox.setVisible(true);
									}
								} else {
									if (MORE_MENU_APPROVAL_FORMS.equals(selectItem.getName())) {
									/*enabledApprovalFormsBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
									linkNameApprovalFormsBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
									if (enabledApprovalFormsBox.getValue()) {
										linkNameApprovalFormsBox.setVisible(true);
									}*/
									} else {
										if (MORE_MENU_SURVEYS_AND_POLLS.equals(selectItem.getName())) {
										/*enabledSurveysPollsBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
										linkNameSurveysPollsBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
										if (enabledSurveysPollsBox.getValue()) {
											linkNameSurveysPollsBox.setVisible(true);
										}*/
										} else {
											if (MORE_MENU_MASS_MAILING.equals(selectItem.getName())) {
												enabledMassMailingBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
												linkNameMassMailingBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
												if (enabledMassMailingBox.getValue()) {
													linkNameMassMailingBox.setVisible(true);
												}
											} else {
												if (MORE_MENU_LEAD_CAPTURE_FORMS.equals(selectItem.getName())) {
													enabledLeadCaptureBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
													linkNameLeadCaptureBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
													if (enabledLeadCaptureBox.getValue()) {
														linkNameLeadCaptureBox.setVisible(true);
													}
												} else {
													if (MORE_MENU_GOOGLE_ANALYTICS.equals(selectItem.getName())) {
													/*enabledGoogleAnalyticsBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
													linkNameGoogleAnalyticsBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
													if (enabledGoogleAnalyticsBox.getValue()) {
														linkNameGoogleAnalyticsBox.setVisible(true);
													}*/
													} else {
														if (MORE_MENU_REPORTING_DASHBOARD.equals(selectItem.getName())) {
															enabledReportingDashboardBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
															linkNameReportingDashboardBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
															if (enabledReportingDashboardBox.getValue()) {
																linkNameReportingDashboardBox.setVisible(true);
															}
														} else {
															if (MORE_MENU_OUTLOOK_PLUGIN.equals(selectItem.getName())) {
															/*enabledOutlookBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
															linkNameOutlookBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
															if (enabledOutlookBox.getValue()) {
																linkNameOutlookBox.setVisible(true);
															}*/
															} else {
																if (MORE_MENU_EXCEL_PLUGIN.equals(selectItem.getName())) {
															/*	enabledExcelBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
																linkNameExcelBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
																if (enabledExcelBox.getValue()) {
																	linkNameExcelBox.setVisible(true);
																}*/
																} else {
																	if (MORE_MENU_ANDROID.equals(selectItem.getName())) {
																		enabledAndroidBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
																		linkNameAndroidBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
																		if (enabledAndroidBox.getValue()) {
																			linkNameAndroidBox.setVisible(true);
																		}
																	} else {
																		if (MORE_MENU_IPHONE.equals(selectItem.getName())) {
																			enabledIPhoneBox.setValue(selectItem.isNewItem() != null ? selectItem.isNewItem() : false);
																			linkNameIPhoneBox.setText(selectItem.getDescription() != null ? selectItem.getDescription() : "");
																			if (enabledIPhoneBox.getValue()) {
																				linkNameIPhoneBox.setVisible(true);
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void save() {
		if (validate()) {
			return;
		}

		ArrayList<SelectItem> enabledItems = new ArrayList<>();
//        if (enabledStoreFrontBox.getValue()) {
		enabledItems.add(setSelectItem(enabledStoreFrontBox.getValue(), MORE_MENU_STOREFRONT, linkNameStoreFrontBox.getText()));
//        }
//        if (enabledWebSitesBox.getValue()) {
		enabledItems.add(setSelectItem(enabledWebSitesBox.getValue(), MORE_MENU_WEB_SITES, linkNameWebSitesBox.getText()));
//        }
//        if (enabledCatalogsDirectoriesBox.getValue()) {
		//enabledItems.add(setSelectItem(enabledCatalogsDirectoriesBox.getValue(), MORE_MENU_CATALOG_AND_DIRECTORIES, linkNameCatalogsDirectoriesBox.getText()));
//        }
//        if (enabledWebFormsBox.getValue()) {
		enabledItems.add(setSelectItem(enabledWebFormsBox.getValue(), MORE_MENU_WEB_FORMS, linkNameWebFormsBox.getText()));
//        }
//        if (enabledApprovalFormsBox.getValue()) {
		//enabledItems.add(setSelectItem(enabledApprovalFormsBox.getValue(), MORE_MENU_APPROVAL_FORMS, linkNameApprovalFormsBox.getText()));
//        }
//        if (enabledSurveysPollsBox.getValue()) {
		//enabledItems.add(setSelectItem(enabledSurveysPollsBox.getValue(), MORE_MENU_SURVEYS_AND_POLLS, linkNameSurveysPollsBox.getText()));
//        }
//        if (enabledMassMailingBox.getValue()) {
		enabledItems.add(setSelectItem(enabledMassMailingBox.getValue(), MORE_MENU_MASS_MAILING, linkNameMassMailingBox.getText()));
//        }
//        if (enabledLeadCaptureBox.getValue()) {
		enabledItems.add(setSelectItem(enabledLeadCaptureBox.getValue(), MORE_MENU_LEAD_CAPTURE_FORMS, linkNameLeadCaptureBox.getText()));
//        }
//        if (enabledGoogleAnalyticsBox.getValue()) {
		//enabledItems.add(setSelectItem(enabledGoogleAnalyticsBox.getValue(), MORE_MENU_GOOGLE_ANALYTICS, linkNameGoogleAnalyticsBox.getText()));
//        }
//        if (enabledReportingDashboardBox.getValue()) {
		enabledItems.add(setSelectItem(enabledReportingDashboardBox.getValue(), MORE_MENU_REPORTING_DASHBOARD, linkNameReportingDashboardBox.getText()));
//        }
//        if (enabledOutlookBox.getValue()) {
		//enabledItems.add(setSelectItem(enabledOutlookBox.getValue(), MORE_MENU_OUTLOOK_PLUGIN, linkNameOutlookBox.getText()));
//        }
//        if (enabledExcelBox.getValue()) {
		//enabledItems.add(setSelectItem(enabledExcelBox.getValue(), MORE_MENU_EXCEL_PLUGIN, linkNameExcelBox.getText()));
//        }
//        if (enabledAndroidBox.getValue()) {
		enabledItems.add(setSelectItem(enabledAndroidBox.getValue(), MORE_MENU_ANDROID, linkNameAndroidBox.getText()));
//        }
//        if (enabledIPhoneBox.getValue()) {
		enabledItems.add(setSelectItem(enabledIPhoneBox.getValue(), MORE_MENU_IPHONE, linkNameIPhoneBox.getText()));
//        }

		LoadingPanel.loading(true);
		BackendService.App.get().saveMoreMenuItems(enabledItems.toArray(new SelectItem[]{}), companyID, new AbstractAsyncCallback<Void>() {
			@Override
			public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

			@Override
			public void success(Void aVoid) {
				LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()), Info.Type.INFO);
            }
		});
	}

	private void setCheckedAndVisible(KpiCheckBox checkBox, final TextBox textBox) {
		checkBox.addValueChangeHandler(booleanValueChangeEvent -> {
			textBox.setVisible(booleanValueChangeEvent.getValue());
        });
	}

	private SelectItem setSelectItem(Boolean value, String menuName, String menuUrl) {
		SelectItem s = new SelectItem();
		s.setName(menuName);
		s.setDescription(menuUrl);
		s.setNewItem(value);
		return s;
	}

	private boolean validate() {
		int errors = 0;
		if (enabledStoreFrontBox.getValue() && !Validation.validateTextBoxRequired(linkNameStoreFrontBox)) {
			errors++;
		}
		if (enabledWebSitesBox.getValue() && !Validation.validateTextBoxRequired(linkNameWebSitesBox)) {
			errors++;
		}
		/*if (enabledCatalogsDirectoriesBox.getValue() && !Validation.validateTextBoxRequired(linkNameCatalogsDirectoriesBox)) {
			errors++;
		}*/
		if (enabledWebFormsBox.getValue() && !Validation.validateTextBoxRequired(linkNameWebFormsBox)) {
			errors++;
		}
		/*if (enabledApprovalFormsBox.getValue() && !Validation.validateTextBoxRequired(linkNameApprovalFormsBox)) {
			errors++;
		}
		if (enabledSurveysPollsBox.getValue() && !Validation.validateTextBoxRequired(linkNameSurveysPollsBox)) {
			errors++;
		}*/
		if (enabledMassMailingBox.getValue() && !Validation.validateTextBoxRequired(linkNameMassMailingBox)) {
			errors++;
		}
		if (enabledLeadCaptureBox.getValue() && !Validation.validateTextBoxRequired(linkNameLeadCaptureBox)) {
			errors++;
		}
		/*if (enabledGoogleAnalyticsBox.getValue() && !Validation.validateTextBoxRequired(linkNameGoogleAnalyticsBox)) {
			errors++;
		}*/
		if (enabledReportingDashboardBox.getValue() && !Validation.validateTextBoxRequired(linkNameReportingDashboardBox)) {
			errors++;
		}
		/*if (enabledOutlookBox.getValue() && !Validation.validateTextBoxRequired(linkNameOutlookBox)) {
			errors++;
		}
		if (enabledExcelBox.getValue() && !Validation.validateTextBoxRequired(linkNameExcelBox)) {
			errors++;
		}*/
		if (enabledAndroidBox.getValue() && !Validation.validateTextBoxRequired(linkNameAndroidBox)) {
			errors++;
		}
		if (enabledIPhoneBox.getValue() && !Validation.validateTextBoxRequired(linkNameIPhoneBox)) {
			errors++;
		}
		return errors > 0;
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