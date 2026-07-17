package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 29.01.2010
 * Time: 14:42:38
 * To change this template use File | Settings | File Templates.
 * This TextArea has a counter when you reach the limit, you can not type any latters anymore...
 */
public class TextBox2 extends Composite implements Clearable {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private int MAX_LENGTH = 255;// min
    private TextBox textBox;
    private VerticalPanel vp;
    private HorizontalPanel counter;
    private int counterInt = 0;
    private Label characterLimit = new Label(wfmStrings.characterLimit());

    public TextBox2() {
        super();
        MAX_LENGTH = 255;
        init();
    }

    public TextBox2(int maxLength) {
        super();
        MAX_LENGTH = maxLength;
        init();
    }

    private void init() {
        counterInt = 0;
        vp = new VerticalPanel();
        counter = new HorizontalPanel();
        counter.setSpacing(0);
        counter.setBorderWidth(0);
        initWidget(vp);
        textBox = new TextBox();
        addListener();
        vp.add(textBox);
        vp.add(counter);
        vp.setCellHorizontalAlignment(textBox, HasHorizontalAlignment.ALIGN_LEFT);
        vp.setCellHorizontalAlignment(counter, HasHorizontalAlignment.ALIGN_RIGHT);
        counterLabel = new Label(MAX_LENGTH > 0 ? "" + MAX_LENGTH : "0");
        counterLabel.setStyleName("counterGreen");
        counter.add(characterLimit);
        counter.add(counterLabel);
    }

    private void addListener() {
        if (textBox != null) {
            textBox.addKeyUpHandler(keyUpEvent -> changeCounter());
            textBox.addChangeHandler(changeEvent -> changeCounter());
        }
    }

    Label counterLabel;

    private void changeCounter() {
        int lengthOfTextAreaText = textBox.getText().length();
        counterInt = MAX_LENGTH > 0 ? MAX_LENGTH - lengthOfTextAreaText : lengthOfTextAreaText;
        counter.remove(counterLabel);
        counterLabel.setText(counterInt + "");
        counterLabel.setTitle(counterInt + "");
        if (counterInt > 0) {
            counterLabel.setStyleName("counterGreen");
        } else {
            counterLabel.setStyleName("counterRed");
        }
        counter.add(counterLabel);
        if (counterInt < 0) {
            textBox.setText(textBox.getText().substring(0, MAX_LENGTH));
        }
    }

    public void addKeyPressHandler(KeyPressHandler keyPressHandler) {
        textBox.addKeyPressHandler(keyPressHandler);
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        textBox.setWidth("100%");
        vp.setWidth(width);
    }

    public void setWidth(int width) {
        textBox.setWidth(width + "px");
    }

    public void setSize(int width, int height) {
        textBox.setSize(width + "px", height + "px");
    }

    public void setHeight(String height) {
        textBox.setHeight(height);
    }

    public void setHeight(int height) {
        textBox.setHeight(height + "px");
    }

    public int getTabIndex() {
        return textBox.getTabIndex();
    }

    public void setTabIndex(int index) {
        textBox.setTabIndex(index);
    }

    @Override
    public String getTitle() {
        return textBox.getTitle();
    }

    @Override
    public void setTitle(String text) {
        textBox.setTitle(text);
    }

    public String getText() {
        return textBox.getText();
    }

    public void setText(String text) {
        textBox.setText(text);
        changeCounter();
    }

    public int getMAX_LENGTH() {
        return MAX_LENGTH;
    }

    public void setMAX_LENGTH(int MAX_LENGTH) {
        this.MAX_LENGTH = MAX_LENGTH;
    }

    public TextBox getTextBox() {
        return textBox;
    }

    public void setEnabled(boolean bool) {
        textBox.setEnabled(bool);
    }

    public boolean isEnabled() {
        return textBox.isEnabled();
    }

    public boolean isReadOnly() {
        return textBox.isReadOnly();
    }

    public void setReadOnly(boolean bool) {
        textBox.setReadOnly(bool);
    }

    public boolean isVisible() {
        return textBox.isVisible();
    }

    public void setVisible(boolean bool) {
        super.setVisible(bool);
        textBox.setVisible(bool);
    }

    public void clearSelected() {
        textBox.setText("");
    }

    public void addBlurHandler(BlurHandler blurHandler) {
        if (blurHandler != null) {
            textBox.addBlurHandler(blurHandler);
        }
    }
}
