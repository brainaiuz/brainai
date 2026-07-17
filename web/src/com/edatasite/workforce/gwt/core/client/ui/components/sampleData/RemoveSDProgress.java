package com.edatasite.workforce.gwt.core.client.ui.components.sampleData;

import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

public class RemoveSDProgress extends KpiModal {

    Div content;
    Div loader;

    public RemoveSDProgress() {
        super();
        addStyleName("modal--sample");
        setDismissible(false);
        initialize();
    }

    private void initialize() {
        modalHeader.removeFromParent();
        modalContent.clear();

        content = new Div("progress-panel progress-panel--remove-sample-data");
        Div header = new Div("progress-panel__header");
        header.getElement().setInnerText(wfmStrings.sitTightCreatingYourAccount());

        Div steps = new Div("progress-panel__steps");
        steps.add(getStep(wfmStrings.resetData()));
        steps.add(getStep(wfmStrings.basicDetails()));
        steps.add(getStep(wfmStrings.setUp()));

        Div desc = new Div("progress-panel__current-desc");
        desc.add(getProgressDesc(wfmStrings.removingYourData()));
        desc.add(getProgressDesc(wfmStrings.copyingBasicInformation()));
        desc.add(getProgressDesc(wfmStrings.creatingYourAccount()));

        loader = new Div("cs-loader");
        loader.add(getLoaders());

        content.add(header);
        content.add(steps);
        content.add(desc);
        content.add(loader);

        modalContent.add(content);

        modalFooter.removeFromParent();
    }

    private Div getStep(String text) {
        Div step = new Div("progress-panel__step");

        Div stepCircle = new Div("progress-panel__step-circle");
        Icon stepIcon = new Icon();
        stepIcon.getElement().setClassName("progress-panel__step-icon progress-panel__step-icon--success ficon--check-thin");
        stepCircle.add(stepIcon);

        Div title = new Div();
        title.getElement().setInnerText(text);

        step.add(stepCircle);
        step.add(title);

        return step;
    }

    private Div getProgressDesc(String text) {
        Div div = new Div("progress-panel__current-desc-item");
        div.getElement().setInnerText(text);
        return div;
    }

    private Div getLoaders() {
        Div loaders = new Div("cs-loader-inner");

        for(int x = 0; x < 5; x++) {
            Icon i = new Icon();
            i.getElement().setInnerHTML("●");
            loaders.add(i);
        }
        return loaders;
    }

    private native void progressPanelRemoveData() /*-{
        $wnd.removeProgressPanel();
    }-*/;

    private native void progressPanelRemoveDataFinish() /*-{
        $wnd.progressPanelRemoveDataFinish();
    }-*/;

    @Override
    public void open() {
        super.open();
        RootPanel.getBodyElement().addClassName("has-modal--sampledata");
        progressPanelRemoveData();
    }

    @Override
    public void close() {
        progressPanelRemoveDataFinish();
        loader.removeFromParent();
//        super.close();
//        RootPanel.getBodyElement().removeClassName("has-modal--sampledata");
    }
}
