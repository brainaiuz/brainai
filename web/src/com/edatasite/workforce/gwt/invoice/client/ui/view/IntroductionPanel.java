package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/4/12
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntroductionPanel {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private Anchor introductionLink;
    private TextArea introduction;
    private String indtroductionPanel = "indtroduction_panel_";
    private static int sch = 0;

    public IntroductionPanel() {
        initialize();
    }

    private void initialize() {
        introduction = new TextArea();
//        introduction.setVisible(false);
        introduction.ensureDebugId(indtroductionPanel+"introductionText");
        introductionLink = new Anchor(accountingStrings.addIntroduction());
        introductionLink.ensureDebugId(indtroductionPanel+"introductionLink"+(sch++));
        introductionLink.addClickHandler(clickEvent -> {
            introduction.setVisible(!introduction.isVisible());
            introductionLink.setText(introduction.isVisible() ? accountingStrings.hideIntroduction() : accountingStrings.addIntroduction());
        });
        introductionLink.setStyleName(AccountingCustomFormConstants.STYLE_OPTION_LABEL);
        introduction.setStyleName(AccountingCustomFormConstants.STYLE_INTRODUCTION_TXTBOX);
    }

    public Anchor getIntroductionLink() {
        return introductionLink;
    }

    public TextArea getIntroduction() {
        return introduction;
    }
}
