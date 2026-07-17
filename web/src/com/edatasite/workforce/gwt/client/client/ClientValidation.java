package com.edatasite.workforce.gwt.client.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

public class ClientValidation {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public static boolean validateTextBoxRequired(final TextBoxBase textBox, final ErrorLabel errLabel, final String message) {
        if (textBox.getText() == null || "".equals(textBox.getText())) {
            errLabel.setText(message);
            textBox.addKeyboardListener(new KeyboardListener() {
                public void onKeyDown(Widget widget, char c, int i) {
                    validateTextBox(widget, c, i);
                }

                public void onKeyPress(Widget widget, char c, int i) {
                    validateTextBox(widget, c, i);
                }

                public void onKeyUp(Widget widget, char c, int i) {
                    validateTextBox(widget, c, i);
                }

                private void validateTextBox(Widget widget, char c, int i) {
                    if (((TextBoxBase) widget).getText().length() < 1) {
                        errLabel.setText(message);
                    } else {
                        errLabel.setText(null);
                    }
                }
            });
            return false;
        }
        return true;
    }

    public static boolean validateEmailRequired(final TextBoxBase textBox, final ErrorLabel errLabel, String message) {
        String email = textBox.getText();
        if (email == null || email.equals("")) {
            errLabel.setText(message);
            textBox.addKeyboardListener(new KeyboardListener() {
                public void onKeyDown(Widget widget, char c, int i) {
                    errLabel.setText("");
                }

                public void onKeyPress(Widget widget, char c, int i) {
                    errLabel.setText("");
                }

                public void onKeyUp(Widget widget, char c, int i) {
                    errLabel.setText("");
                }
            });
            return false;
        }
        textBox.addKeyPressHandler(event -> errLabel.setText(""));
        email = email.trim();
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == ' ') {
                errLabel.setText(wfmStrings.enterCorrectEmailAddress());
                return false;
            }
        }
        if (!email.matches(emailRegex/*".+@.+\\.[a-z]+"*/)/*("/b[A-Z0-9._%+-]+@(?:[A-Z0-9-]+/.)+[A-Z]{2,4}/b")*/) {
            errLabel.setText(wfmStrings.enterCorrectEmailAddress());
            return false;
        }
        textBox.setText(email);
        return true;
    }

    /**
     * Regex taken from: http://www.regular-expressions.info/email.html
     * by Jan Goyvaerts
     */
    private static String emailRegex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,4}";

    public class ErrorLabel extends HTML {
        public void setText(String text) {
            if (text == null || "".equals(text)) {
                setHTML("");
            } else {
                setHTML("<p><font color='red'>" + text + "</font></p>");
            }
        }
    }

    public ErrorLabel createErrorLabel() {
        return new ErrorLabel();
    }
}
