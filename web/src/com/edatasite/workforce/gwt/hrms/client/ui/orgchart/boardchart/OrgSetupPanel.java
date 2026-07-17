package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OrgSetupPanel extends Composite {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    interface Binder extends UiBinder<Widget, OrgSetupPanel> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    public enum SetupMode {
        MANUAL,
        CLASSIC,
        SIMPLIFIED
    }

    @UiField
    FlowPanel root;

    @UiField
    Label logoCaption;
    @UiField
    Label title;

    @UiField
    FlowPanel manualOption;
    @UiField
    FlowPanel classicOption;
    @UiField
    FlowPanel simplifiedOption;

    @UiField
    WfmButton2 createButton;

    private SetupMode selectedMode = null;
    private OrgSetupModal setupModal = null;

    public OrgSetupPanel() {
        initWidget(binder.createAndBindUi(this));
        initHandlers();
        createButton.setText(wfmStrings.create());
    }

    private void initHandlers() {

        manualOption.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectMode(SetupMode.MANUAL);
            }
        }, ClickEvent.getType());

        classicOption.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectMode(SetupMode.CLASSIC);
            }
        }, ClickEvent.getType());

        simplifiedOption.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectMode(SetupMode.SIMPLIFIED);
            }
        }, ClickEvent.getType());

        createButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (createButton.getStyleName().contains("disabled")) {
                    return;
                }
                event.preventDefault();

                if (setupModal == null) {
                    setupModal = new OrgSetupModal();
                }
                setupModal.open();
            }
        });
    }

    private void selectMode(SetupMode mode) {
        selectedMode = mode;

        manualOption.removeStyleName("active");
        classicOption.removeStyleName("active");
        simplifiedOption.removeStyleName("active");

        switch (mode) {
            case MANUAL:
                manualOption.addStyleName("active");
                break;
            case CLASSIC:
                classicOption.addStyleName("active");
                break;
            case SIMPLIFIED:
                simplifiedOption.addStyleName("active");
                break;
        }

        createButton.removeStyleName("disabled");
    }

    public SetupMode getSelectedMode() {
        return selectedMode;
    }
}