package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * User: Admin
 * Date: 29.01.2010
 * Time: 14:42:38
 * This TextArea has a counter when you reach the limit, you can not type any latters anymore...
 */
public class TextArea2 extends Composite implements Clearable, CustomCellInterface {
    public static final Integer AREA_LENGTH_1 = 1000;
    public static final Integer AREA_LENGTH_2 = 5000;
    public static final Integer AREA_LENGTH_3 = 10000;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private String label;
    private int MAX_LENGTH;// min
    private KpiTextArea textArea;
    private VerticalPanel textAreaPanel;
    public HorizontalPanel localize;
    public HorizontalPanel counter;
    public HorizontalPanel textPanel;
    private int counterInt = 0;
    public Label counterLabel;
    private Widget internalLabelWidget;
    private boolean visibleInform = false;
    private boolean isSmsContent = false;
    private Integer[] entryIds;
    private MultiLanguageTextAreaWidget multiLanguageWidget;

    public TextArea2() {
        super();
        MAX_LENGTH = 255;
        init();
    }

    public TextArea2(int maxLength) {
        super();
        MAX_LENGTH = maxLength;
        init();
    }

    public TextArea2(boolean isSmsContent, int maxLength) {
        super();
        MAX_LENGTH = maxLength;
        this.isSmsContent = isSmsContent;
        init();
    }

    public TextArea2(int maxLength, boolean visibilityInform) {
        super();
        MAX_LENGTH = maxLength;
        visibleInform = visibilityInform;
        init();
    }

    public TextArea2(int maxLength, String label) {
        super();
        MAX_LENGTH = maxLength;
        this.label = label;
        init();
    }

    public TextArea2(String label) {
        super();
        MAX_LENGTH = 255;
        this.label = label;
        init();
    }

    private void init() {
        counterInt = 0;
        textAreaPanel = new VerticalPanel();
        textAreaPanel.addStyleName("textAreaPanel");
        localize = new HorizontalPanel();
        counter = new HorizontalPanel();
        textPanel = new HorizontalPanel();
        counter.setSpacing(0);
        counter.setBorderWidth(0);
        initWidget(textPanel);
        textArea = new KpiTextArea();
        if (isSmsContent) {
            addListener();
            counterLabel = new Label();
        } else {
            addListener();
            counterLabel = new Label(MAX_LENGTH > 0 ? "" + MAX_LENGTH + " " + wfmStrings.characters1() : "0");
        }

        textAreaPanel.add(localize);
        textAreaPanel.add(counter);
        textAreaPanel.add(textArea);
        textPanel.add(textAreaPanel);
        if (visibleInform) {
            new KpiToolTip(textArea, wfmStrings.helpText());
        }
        textAreaPanel.setCellHorizontalAlignment(textArea, HasHorizontalAlignment.ALIGN_LEFT);
        textAreaPanel.setCellHorizontalAlignment(counter, HasHorizontalAlignment.ALIGN_RIGHT);

        if (label != null) {
            setLabelText(label, false);
        }

        counter.setStyleName("counterGreen");
        counter.add(counterLabel);
        counter.setCellVerticalAlignment(counterLabel, HasVerticalAlignment.ALIGN_MIDDLE);
        counter.setCellHorizontalAlignment(counterLabel, HasHorizontalAlignment.ALIGN_RIGHT);
        counterLabel.setStyleName("counterLabel");
        if (getElement() != null) {
            getElement().setAttribute("autocomplete", "off");
        }
    }

    public void setMultiLanguage(boolean enable) {
        if (enable) {
            if (multiLanguageWidget == null) {
                multiLanguageWidget = new MultiLanguageTextAreaWidget(this);
            }
            alignLabelAndLocale();
        }
    }


    public void setLabelText(String rawTitle, boolean isRequired) {
        String title = (rawTitle == null || rawTitle.isEmpty())
                ? wfmStrings.label()
                : rawTitle;

        String html = title + (isRequired ? "<em class='redTitle'>*</em>" : "") + ":";

        this.label = rawTitle;
        if (internalLabelWidget == null) {
            internalLabelWidget = new HTML(html);
            internalLabelWidget.addStyleName("form-group__label");
        } else {
            ((HTML) internalLabelWidget).setHTML(html);
        }

        alignLabelAndLocale();
    }


    private void alignLabelAndLocale() {
        if (internalLabelWidget == null) return;

        // Clean up old locations
        if (counter.getWidgetIndex(internalLabelWidget) != -1) counter.remove(internalLabelWidget);
        if (localize.getWidgetIndex(internalLabelWidget) != -1) localize.remove(internalLabelWidget);

        // If MultiLanguage is active, put label in 'localize' panel (Top Row)
        if (multiLanguageWidget != null) {
            localize.insert(internalLabelWidget, 0); // Insert at Left
            localize.setWidth("100%");

            // This forces the label to take all available width, pushing "Locale" to the far right
            localize.setCellWidth(internalLabelWidget, "100%");
            localize.setCellHorizontalAlignment(internalLabelWidget, HasHorizontalAlignment.ALIGN_LEFT);
            localize.setCellVerticalAlignment(internalLabelWidget, HasVerticalAlignment.ALIGN_MIDDLE);

            localize.setVisible(true);
            multiLanguageWidget.setSideBarLabel(label);
        } else {
            // Standard behavior: put label in 'counter' panel (Middle Row)
            counter.insert(internalLabelWidget, 0);
        }
    }


    public MultiLanguageTextAreaWidget getMultiLanguageWidget() {
        return multiLanguageWidget;
    }

    private void addListener() {
        if (textArea != null) {
            textArea.addKeyUpHandler(keyUpEvent -> changeCounter());
            textArea.addKeyDownHandler(keyUpEvent -> changeCounter());
            textArea.addKeyPressHandler(changeEvent -> changeCounter());
            textArea.addChangeHandler(changeEvent -> changeCounter());
            textArea.addBlurHandler(changeEvent -> changeCounter());
        }
    }

    private void changeCounter() {
        if (textArea.getText() == null || textArea.getText().isEmpty()) return;
        boolean isCyrilic = !textArea.getText().matches("^[a-zA-Z0-9\\s\"<>!#$%&'()*+\\-,.:;=?@\\[\\]^`{|}~_/\\\\]*$");
        int lengthOfTextAreaText = textArea.getText().length();
        int firstLength = isCyrilic ? 70 : 160;
        int secondLength = isCyrilic ? 67 : 145;
        if (isSmsContent) {
            if (lengthOfTextAreaText <= firstLength || (isCyrilic && lengthOfTextAreaText <= firstLength)) {
                counterInt = firstLength - lengthOfTextAreaText;
                counterLabel.setText(counterInt >= 0 ? counterInt + "(1) " + wfmStrings.characters1() : String.valueOf(0));
            } else {
                counterInt = (lengthOfTextAreaText - firstLength) % secondLength == 0 ? 0 : secondLength - (lengthOfTextAreaText - firstLength) % secondLength;

                int smsCount = ((lengthOfTextAreaText - firstLength) / secondLength) > 0 && ((lengthOfTextAreaText - firstLength) % secondLength) == 0 ? ((lengthOfTextAreaText - firstLength) / secondLength) + 1
                        : ((lengthOfTextAreaText - firstLength) / secondLength) > 0 && ((lengthOfTextAreaText - firstLength) % secondLength) > 0 ? ((lengthOfTextAreaText - firstLength) / secondLength) + 2 : 2;
                counterLabel.setText(counterInt >= 0 ? counterInt + "(" + smsCount + ") " + wfmStrings.characters1() : String.valueOf(0));
            }

        } else {
            counterInt = MAX_LENGTH > 0 ? MAX_LENGTH - lengthOfTextAreaText : lengthOfTextAreaText;
            counterLabel.setText(counterInt > 0 ? counterInt + " " + wfmStrings.characters1() : String.valueOf(0));

            if (counterInt < 0) {
                textArea.setText(textArea.getText().substring(0, MAX_LENGTH));
            }
        }
    }

    public void addKeyPressHandler(KeyPressHandler keyPressHandler) {
        textArea.addKeyPressHandler(keyPressHandler);
    }

    public void hideCharacterLimitPanel() {
        counter.setVisible(false);
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        textArea.setWidth(width);
    }

    public void setWidth(int width) {
        textArea.setWidth(width + "px");
    }

    public void setSize(int width, int height) {
        textArea.setSize(width + "px", height + "px");
    }

    public void setHeight(String height) {
        textArea.setHeight(height);
    }

    public void setHeight(int height) {
        textArea.setHeight(height + "px");
    }

    public int getTabIndex() {
        return textArea.getTabIndex();
    }

    public void setTabIndex(int index) {
        textArea.setTabIndex(index);
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String getTitle() {
        return textArea.getTitle();
    }

    @Override
    public void setTitle(String text) {
        textArea.setTitle(text);
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        setText(text, false);
    }

    public void setText(String text, boolean removeHTML) {
        if (!Utils.isNullOrEmpty(text) && removeHTML) {
            textArea.setText(text.replaceAll("<[^>]*>", ""));
        } else {
            textArea.setText(text);
        }
        changeCounter();
    }

    public void setLocaleText(HashMap<String, String> localeText) {
        multiLanguageWidget.setValueMap(localeText);
    }

    public HashMap<String, String> getLocaleText() {
        return multiLanguageWidget.getValueMap();
    }

    public int getMAX_LENGTH() {
        return MAX_LENGTH;
    }

    public void setMAX_LENGTH(int MAX_LENGTH) {
        this.MAX_LENGTH = MAX_LENGTH;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setEnabled(boolean bool) {
        textArea.setEnabled(bool);
    }

    public boolean isEnabled() {
        return textArea.isEnabled();
    }

    public boolean isReadOnly() {
        return textArea.isReadOnly();
    }

    public void setReadOnly(boolean bool) {
        textArea.setReadOnly(bool);
    }

    public void setPlaceHolder(String placeholderMessage) {
//        if (placeholderMessage != null && !"".equals(placeholderMessage)) {
//            textArea.getElement().setAttribute("placeholder", placeholderMessage);
//        }
        textArea.setPlaceholder(placeholderMessage);
    }

    public boolean isVisible() {
        return textArea.isVisible();
    }

    public void setVisible(boolean bool) {
        super.setVisible(bool);
        textArea.setVisible(bool);
    }

    public void clearSelected() {
        textArea.setText("");
    }

    public void addBlurHandler(BlurHandler blurHandler) {
        if (blurHandler != null) {
            textArea.addBlurHandler(blurHandler);
        }
    }

    @Override
    public String getDisplayValue() {
        return getText();
    }

    @Override
    public void setItemValue(Object value) {
        if (value == null) {
            setText("");
            return;
        }
        setText((String) value);
    }

    public void setItemFocus(boolean focus) {
        textArea.setFocus(focus);
    }

    public VerticalPanel getTextAreaPanel() {
        return textAreaPanel;
    }

    public Integer[] getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(Integer[] entryIds) {
        this.entryIds = entryIds;
    }
}
