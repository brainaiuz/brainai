package com.edatasite.workforce.gwt.core.client.ui.dynamicTable.validation;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 15.11.2008
 * Time: 19:40:31
 * To change this template use File | Settings | File Templates.
 */
public class InputValidator {

    private static NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    public InputValidator(TextBox textBox) {
        textBox.addKeyPressHandler(new DecimalValidator());
    }

    public static Double returnDoubleValue(TextBox textBox) {

        String value = textBox.getText();
        if (value == null || "".equals(value) || "n/a".equals(value)) {
            return 0d;
        } else {

            if (!value.replace(",", "").matches("[0-9]+\\.?[0-9]*")) {

                int cursorPosition = textBox.getCursorPos();
                String text = value.substring(0, cursorPosition - 1);
                text = text + value.substring(cursorPosition, value.length());
                textBox.setText(text);
                value = textBox.getText();
            }
            try {
                return numberFormat.parse(value);
            }
            catch (NumberFormatException ex) {
                textBox.setText(numberFormat.format(0d));
                return 0d;
            }
        }
    }

    private class DecimalValidator implements KeyPressHandler {

        @Override
        public void onKeyPress(KeyPressEvent event) {
            char keyCode = event.getCharCode();
            if (!allowedInput(keyCode)) {
                TextBox textBox = (TextBox) event.getSource();
                textBox.cancelKey();
            }
        }
    }

//    private class Formatter implements FocusListener{
//
//        public void onFocus(Widget sender) {
//
//            TextBox textBox = (TextBox)sender;
//            double value = returnDoubleValue(textBox);
//
//            if(value == 0){
//                textBox.setText("");
//            }
//        }
//
//        public void onLostFocus(Widget sender) {
//            TextBox textBox = (TextBox)sender;
//            double value = returnDoubleValue(textBox);
//            textBox.setText(numberFormat.format(value));
//        }
//    }

    private boolean allowedInput(char keyCode) {

        //if key is number (0 - 9).
        if (keyCode >= 48 && keyCode <= 57) {
            return true;
        }
        //if key is number (0 - 9) in the NumPad panel.
        else if (keyCode >= 96 && keyCode <= 105) {
            return true;
        } else if (keyCode == '-') {
            return true;
        }

        switch (keyCode) {
            case KeyboardListener.KEY_BACKSPACE:   //BackSpace
                return true;
            case KeyboardListener.KEY_TAB:   //Tab
                return true;
            case KeyboardListener.KEY_END:  //End
                return true;
            case KeyboardListener.KEY_HOME:  //Home
                return true;
            case KeyboardListener.KEY_LEFT:  //Left
                return true;
            case KeyboardListener.KEY_RIGHT:  //Right
                return true;
            case KeyboardListener.KEY_DELETE:  //Delete
                return true;
            case 110: // . (point on NumPad panel)
                return true;
            case 190: // . (point)
                return true;

            default:
                return false;
        }
    }

}
