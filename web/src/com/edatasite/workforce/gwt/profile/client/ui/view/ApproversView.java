package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ApproversLogHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.ui.view.leaveRequestApproversView.ApproversWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.Utils.isOk;

public class ApproversView extends FooteredView implements Constants {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileServiceAsync profileService = ProfileService.App.get();

    private ApproversWidget widget;
    private DataListBox chooseType;
    private final VerticalPanel verticalPanel = new VerticalPanel();
    private Label hierarchicalLabel;
    private KpiCheckBox hierarchicalApproval;
    private WfmButton2 saveButton;
    private ApproversLogHistoryWidget noteHistoryWidget;
    private String entityType ;

    public ApproversView() {
        super("approvalProcess", wfmStrings.approvalProcess());
    }

    @Override
    public String getIconStyle() {
        return "icon-workflow-list icon-workflow";
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId("save");
        saveButton.addClickHandler(clickEvent -> widget.save());

        Label moduleLabel = new Label(wfmStrings.apps().concat(":"));
        chooseType = new DataListBox();

        hierarchicalLabel = new Label(settingsStrings.hierarchicalApprovalWorkflow());
        hierarchicalLabel.setVisible(false);

        hierarchicalApproval = new KpiCheckBox();
        hierarchicalApproval.setVisible(false);

        getApproverModules();
        HorizontalPanel tempHorizPanel = new HorizontalPanel();
        tempHorizPanel.add(moduleLabel);
        tempHorizPanel.add(chooseType);
        tempHorizPanel.add(hierarchicalApproval);
        tempHorizPanel.setCellVerticalAlignment(hierarchicalApproval, HasVerticalAlignment.ALIGN_MIDDLE);
        tempHorizPanel.setStyleName("approvers_header");

        tempHorizPanel.add(hierarchicalLabel);
        verticalPanel.add(tempHorizPanel);
        verticalPanel.setWidth("100%");
        verticalPanel.setStyleName("approvers-box");
        chooseType.addValueChangeHandler(changeEvent -> {
            hierarchicalApproval.setValue(false);
            if (isOk(widget)) {
                verticalPanel.remove(widget);
            }
            if (isOk(chooseType.getSelectedItem()) && isOk(chooseType.getSelectedItem().getDescription())) {
                if (!RelationItem.TYPE_LEAVE_REQUEST.equals(chooseType.getSelectedItem().getDescription())) {
                    hierarchicalApproval.setVisible(false);
                    hierarchicalLabel.setVisible(false);
                } else {
                    hierarchicalApproval.setVisible(true);
                    hierarchicalLabel.setVisible(true);
                }
                widget = new ApproversWidget(chooseType.getSelectedItem().getDescription(), chooseType.getSelectedItem().getCategory(), hierarchicalApproval);
                entityType = chooseType.getSelectedItem().getDescription();
                verticalPanel.add(widget);
            }
        });
        Div tableWrapper = new Div("approvers-box__wrapper box-bg--1");
        tableWrapper.add(verticalPanel);
        add(tableWrapper);
        add(createFooter());
        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ApproversView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ApproversView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        GWT.log("entityType: " + entityType);
        informer.addClickHandler(click ->    AllInOneService.App.get().getApproversHistoryNotes(entityType, new AbstractAsyncCallback<List<SelectItem>>() {

            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(List<SelectItem> result) {
                LoadingPanel.loading(false);
                new ApproversLogHistoryWidget(result).show();
            }
        }));
        informer.setInitialClasses("informer-item history-notes-container");
        leftSideWidgets.add(informer);
        return leftSideWidgets;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();

        Div saveWrapper = new Div();
        saveWrapper.add(saveButton);
        rightSideWidgets.add(saveWrapper);

        return rightSideWidgets;
    }

    private void getApproverModules() {

        profileService.getApproverModules(new ListingFilterParameter(), new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
                LoadingPanel.loading(false);
                chooseType.setItems(result);
            }
        });
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
