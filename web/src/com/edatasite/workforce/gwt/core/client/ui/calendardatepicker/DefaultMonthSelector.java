/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.edatasite.workforce.gwt.core.client.ui.calendardatepicker;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

import java.util.Date;

/**
 * A simple {@link MonthSelector} used for the default date picker. Not
 * extensible as we wish to evolve it freely over time.
 */

public final class DefaultMonthSelector extends MonthSelector {

  private PushButton backwards;
  private Label backwardsyear;
  private PushButton forwards;
  private Label forwardsyear;
  private Label textboxLabel;
  private TextBox textbox;
  private Grid grid;
  private boolean showTextBox = true;

  /**
   * Constructor.
   */
  public DefaultMonthSelector(boolean showTextBox) {
    this.showTextBox = showTextBox;
  }
 
  @Override
  protected void refresh() {
    String formattedMonth = getModel().formatCurrentMonth();
    grid.setHTML(0, 2, "<div>" + formattedMonth + "</div>");
  }

  @Override
  protected void setup() {
    // Set up backwards.
    backwards = new PushButton();
    backwards.addClickHandler(event -> addMonths(-1));

    backwards.getUpFace().setHTML(new HTML("&lsaquo;").getHTML());
    backwards.setStyleName(css().previousButton());

    // Set up year backwards.
    backwardsyear = new Label(new HTML("&laquo;").getHTML());
    backwardsyear.setStyleName(css().previousButton());
    backwardsyear.addClickHandler(event -> addMonths(-12));

    forwards = new PushButton();
    forwards.getUpFace().setHTML(new HTML("&rsaquo;").getHTML());
    forwards.setStyleName(css().nextButton());
    forwards.addClickHandler(event -> addMonths(+1));

    forwardsyear = new Label(new HTML("&raquo;").getHTML());
    forwardsyear.setStyleName(css().previousButton());
    forwardsyear.addClickHandler(event -> addMonths(+12));

    textboxLabel = new Label("#");
    textboxLabel.setTitle("Enter year");
    textboxLabel.setStyleName(css().previousButton());
    textboxLabel.addClickHandler(event -> grid.setWidget(0, 5, textbox));
    textbox = new TextBox();
    textbox.setWidth("33px");
    Validation.addPhoneNumberKeyboardListener(textbox);
    textbox.addKeyPressHandler(event -> {
      if ((event.getNativeEvent().getKeyCode() == 10 || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)) {
        if (textbox.getText() != null && !"".equals(textbox.getText().trim())) {
          grid.setWidget(0, 5, textboxLabel);
          int currentYear = Integer.parseInt(DateUtils.getYear(new Date()));
          int enteredYear = Integer.parseInt(textbox.getText());
          addMonthsWithCurrentTime(new Date(), (enteredYear - currentYear) * 12);
          textbox.setText("");
        }
      }
    });
    // Set up grid.
    grid = new Grid(1, (showTextBox ? 6 : 5));
    grid.setWidget(0, 0, backwardsyear);
    grid.setWidget(0, 1, backwards);
    grid.setWidget(0, 3, forwards);
    grid.setWidget(0, 4, forwardsyear);
    if (showTextBox) {
      grid.setWidget(0, 5, textboxLabel);
    }

    CellFormatter formatter = grid.getCellFormatter();
    formatter.setStyleName(0, 2, "div-" + css().month());
    formatter.setWidth(0, 2, "60%");
    grid.setStyleName(css().monthSelector());
    initWidget(grid);
  }

  public boolean isShowTextBox() {
    return showTextBox;
  }

  public void setShowTextBox(boolean showTextBox) {
    this.showTextBox = showTextBox;
  }
}
