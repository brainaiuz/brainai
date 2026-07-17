package com.edatasite.workforce.gwt.gettingstarted.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.clickablePanel.ClickablePanel;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.gettingstarted.client.ui.GuideButtonClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 17.06.2009
 * Time: 16:08:13
 */
public abstract class GettingStartedMainView extends Composite implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    protected /*WidgetContainer*/GuideContainer container;
    protected GuideButtonClickListener listener;
    private boolean shown = false;
    protected WfmButton2 nextButton;
    protected WfmButton2 saveAndCloseButton;
    protected WfmButton2 backButton;
    protected WfmButton2 skipButton;
    protected HorizontalPanel buttonPanel;

    public GettingStartedMainView() {
        ClickablePanel panel = new ClickablePanel();
        panel.setSize("100%", "100%");

        buttonPanel = new HorizontalPanel();
        buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        buttonPanel.setSpacing(20);
        buttonPanel.setStyleName("inner");
        buttonPanel.setSize("100%", "50px");
//        exportPanel.setSpacing(25);

        container = new GuideContainer();
        container.setSize("100%", "100%");
        container.setStyleName("pm-guide-main");
//        container.setScrollEnabled(true);
//        container.addListener(Events.Add, new Listener() {
//            public void handleEvent(BaseEvent be) {
//                shown = true;
//            }
//        });


        panel.addHorizontally(container);

//        exportPanel.setWidth("900px");
        panel.addVertically(buttonPanel);
        panel.setCellHeight(buttonPanel, "50px");
        panel.setSize("970px", "100%");
//        add(panel);
        //layout(true);
        initWidget(panel);


    }

    public GettingStartedMainView(boolean showSkipButton) {
        this();
        backButton = new WfmButton2("   " + wfmStrings.back() + "   ");

        backButton.addClickHandler(sender -> listener.onBackButtonClick());
        if (showSkipButton) {
            saveAndCloseButton = new WfmButton2(accountingStrings.saveAndAddAnother());
            saveAndCloseButton.addClickHandler(sender -> saveAddAnother());
        }

        skipButton = new WfmButton2(" " + wfmStrings.skipThisStep() + " ");
        skipButton.addClickHandler(sender -> skipThisStep());

        nextButton = new WfmButton2(" " + wfmStrings.saveAndNext() + " ");
        nextButton.addClickHandler(sender -> {
            if (!saveAndNext()) {
                return;
            }
            listener.onNextButtonClick();
        });
        buttonPanel.add(backButton);
        buttonPanel.setCellHorizontalAlignment(backButton, HorizontalPanel.ALIGN_RIGHT);
        HorizontalPanel hp = new HorizontalPanel();
        if (showSkipButton) {
            hp.setWidth("330px");
            buttonPanel.add(hp);
            buttonPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
            buttonPanel.add(saveAndCloseButton);
            buttonPanel.setCellHorizontalAlignment(saveAndCloseButton, HasHorizontalAlignment.ALIGN_RIGHT);
        } else {
            hp.setWidth("500px");
            buttonPanel.add(hp);
        }
        buttonPanel.add(skipButton);
        buttonPanel.setCellHorizontalAlignment(skipButton, HasHorizontalAlignment.ALIGN_RIGHT);
        buttonPanel.add(nextButton);
        buttonPanel.setCellHorizontalAlignment(nextButton, HasHorizontalAlignment.ALIGN_LEFT);

        //initWidget(panel);

    }

    public boolean isShown() {
        return shown;
    }

    public abstract void showView();

    public abstract void refresh();

    protected abstract void saveAddAnother();

    protected abstract void skipThisStep();

    protected abstract boolean saveAndNext();

    public void addButtonClickListener(GuideButtonClickListener listener) {
        this.listener = listener;
    }

    class GuideContainer extends FlowPanel {

        public GuideContainer() {
            super();
        }


        @Override
        public void add(Widget w) {
            super.add(w);

            shown = true;
        }
    }
}
