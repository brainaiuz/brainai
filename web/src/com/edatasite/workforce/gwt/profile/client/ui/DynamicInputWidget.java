package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.Operands;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created by Hayot on 3/17/14.
 */
public class DynamicInputWidget extends Composite {
    private final SimplePanel body;
    private ModelField field;
    private BlurHandler blurHandler;

    public DynamicInputWidget(ModelField field) {
        body = new SimplePanel();
        initWidget(body);
        this.field = field;
        initType();
    }

    private void initType() {
        String type = this.field != null ? field.getWidget() : null;
        if (type != null) {
            if (field.getSource() != null) {
                OverAllLookUp widget = new OverAllLookUp(field.getForm_ID(), field.getField_ID(), field.getSource());
                if (blurHandler != null) {
                    widget.getSuggestBox().addBlurHandler(blurHandler);
                }
                setWidget(widget);
            } else if (field.getReferenceItem() != null) {
                ReferenceLookUp widget = new ReferenceLookUp(field.getReferenceItem().getCode());
                if (blurHandler != null) {
                    widget.getSuggestBox().addBlurHandler(blurHandler);
                }
                setWidget(widget);
            } else if (Constants.UI_TYPE_TEXTBOX.equals(type)) {
                TextBox widget = new TextBox();
                if (blurHandler != null) {
                    widget.addBlurHandler(blurHandler);
                }
                if (field.getType() != null && ModelField.TYPE.INTEGER.equals(field.getType())) {
                    Validation.addNumericKeyboardListener(widget);
                }
                setWidget(widget);
            } else if (Constants.UI_TYPE_TEXTAREA.equals(type)) {
                TextArea widget = new TextArea();
                if (blurHandler != null) {
                    widget.addBlurHandler(blurHandler);
                }
                setWidget(widget);
            } else if (Constants.UI_TYPE_DATEPICKER.equals(type)) {
                DynamicInputWidget.CustomDatePicker widget = new DynamicInputWidget.CustomDatePicker();
                if (blurHandler != null) {
                    widget.addBlurHandler(blurHandler);
                }
                setWidget(widget);
            } else {
                TextBox widget = new TextBox();
                if (blurHandler != null) {
                    widget.addBlurHandler(blurHandler);
                }
                setWidget(widget);
            }
        } else {
            TextBox widget = new TextBox();
            if (blurHandler != null) {
                widget.addBlurHandler(blurHandler);
            }
            setWidget(widget);
        }
    }

    public void initCustomDateWidget(String columnCode) {
        if (columnCode != null) {
            if (Operands.DateT.TODAY.equals(columnCode)) {
                Label widget = new Label(columnCode);
                setWidget(widget);
            } else if (Operands.DateT.YESTERDAY.equals(columnCode)) {
                Label widget = new Label(columnCode);
                setWidget(widget);
            } else if (Operands.DateT.TOMORROW.equals(columnCode)) {
                Label widget = new Label(columnCode);
                setWidget(widget);
            } else if (Operands.DateT.CURRENT_DAY.equals(columnCode)) {
                Label widget = new Label(columnCode);
                setWidget(widget);
            } else if (Operands.DateT.AGE_IN_DAYS.equals(columnCode) || Operands.DateT.AGE_IN_HOURS.equals(columnCode)) {
                HorizontalPanel widget = new HorizontalPanel();
                widget.setSpacing(3);
                DataListBox rangers = new DataListBox();
                rangers.setItems(SelectItem.asItems(Operands.DateT.RANGERS));
//                rangers.setWidth("40px");
                TextBox dayRange = new TextBox();
                Validation.addNumericKeyboardListener(dayRange);
                widget.add(rangers);
                widget.add(dayRange);
                setWidget(widget);
            } else if (Operands.DateT.HAS_DAYS_LEFT.equals(columnCode)) {
                HorizontalPanel widget = new HorizontalPanel();
                widget.setSpacing(3);
                DataListBox rangers = new DataListBox();
                rangers.setItems(SelectItem.asItems(Operands.DateT.RANGERS));
//                rangers.setWidth("40px");
                TextBox dayRange = new TextBox();
                Validation.addNumericKeyboardListener(dayRange);
                widget.add(rangers);
                widget.add(dayRange);
                setWidget(widget);
            } else if (Operands.DateT.BETWEEN.equals(columnCode) || Operands.DateT.NOT_BETWEEN.equals(columnCode)) {
                if (getWidget() == null || !(getWidget() instanceof HorizontalPanel && ((HorizontalPanel) getWidget()).getWidgetCount() > 2)) {
                    HorizontalPanel widget = new HorizontalPanel();
                    widget.setSpacing(3);
                    DynamicInputWidget.CustomDatePicker datePicker1 = new DynamicInputWidget.CustomDatePicker();
                    DynamicInputWidget.CustomDatePicker datePicker2 = new DynamicInputWidget.CustomDatePicker();
                    widget.add(datePicker1);
                    HTML andLabel = new HTML("and");
                    widget.add(andLabel);
                    widget.add(datePicker2);
                    widget.setCellVerticalAlignment(andLabel, HasVerticalAlignment.ALIGN_MIDDLE);
                    setWidget(widget);
                }
            } else {
                if (getWidget() == null || !(getWidget() instanceof DynamicInputWidget.CustomDatePicker)) {
                    DynamicInputWidget.CustomDatePicker widget = new DynamicInputWidget.CustomDatePicker();
                    if (blurHandler != null) {
                        widget.addBlurHandler(blurHandler);
                    }
                    setWidget(widget);
                }
            }
        } else {
            DynamicInputWidget.CustomDatePicker widget = new DynamicInputWidget.CustomDatePicker();
            if (blurHandler != null) {
                widget.addBlurHandler(blurHandler);
            }
            setWidget(widget);
        }
    }

    public Widget getWidget() {
        return body != null ? body.getWidget() : null;
    }

    public void setWidget(Widget widget) {
        if (widget != null) {
            widget.setWidth("100%");
        }
        body.setWidget(widget);
    }

    public Date getDateValue() {
        Widget widget = getWidget();
        if (widget != null && widget instanceof DynamicInputWidget.CustomDatePicker) {
            return getDatePicker().getDate();
        }
        return null;
    }

    public String getValue() {
        Widget widget = getWidget();
        if (widget != null && widget instanceof LookUp) {
            SelectItem item = getLookUp(widget instanceof ReferenceLookUp).getSelectedItem();
            if (item != null) {
                Integer id = item.getId();
                String name = item.getName();
                return id + "@" + name + "@" + item.getReferenceCode();
            }
        } else if (widget != null && widget instanceof TextBox) {
            return getTextBox().getValue();
        } else if (widget != null && widget instanceof TextArea) {
            return getTextArea().getValue();
        } else if (widget != null && widget instanceof DynamicInputWidget.CustomDatePicker) {
            return getDatePicker().getDate() != null ? DateUtils.formatToParse(getDatePicker().getDate()) : null;
        } else if (widget != null && widget instanceof Label) {
            return ((Label) widget).getText();
        } else if (widget != null && widget instanceof HorizontalPanel) {
            HorizontalPanel panel = (HorizontalPanel) widget;
            if (panel.getWidgetCount() > 2) {
                DynamicInputWidget.CustomDatePicker datePicker1 = (DynamicInputWidget.CustomDatePicker) panel.getWidget(0);
                DynamicInputWidget.CustomDatePicker datePicker2 = (DynamicInputWidget.CustomDatePicker) panel.getWidget(2);
                if (datePicker1.getDate() == null || datePicker2.getDate() == null) {
                    return null;
                }
                return DateUtils.formatToParse(datePicker1.getDate()) + "@" + DateUtils.formatToParse(datePicker2.getDate());
            } else {
                DataListBox rangers = (DataListBox) panel.getWidget(0);
                TextBox dayRange = (TextBox) panel.getWidget(1);
                if (rangers.getSelectedItem() == null || dayRange.getText() == null) {
                    return null;
                }
                return rangers.getSelectedItem().getName() + "@" + dayRange.getText();
            }
        }
        return null;
    }

    public void setValue(Date value) {
        Widget widget = getWidget();
        if (widget != null && widget instanceof DynamicInputWidget.CustomDatePicker) {
            getDatePicker().setDate(value);
        }
    }

    public void setValue(SelectItem value) {
        Widget widget = getWidget();
        if (widget != null && widget instanceof OverAllLookUp) {
            ((OverAllLookUp) widget).setSelected(value);
        } else if (widget instanceof ReferenceLookUp) {
            ((ReferenceLookUp) widget).setSelected(value);
        }
    }

    public void setValue(String value) {
        Widget widget = getWidget();
        if (widget != null && widget instanceof LookUp) {
            Integer id = null;
            String name = null;
            String code = null;
            SelectItem item = new SelectItem();
            if (value != null) {
                if (value.contains("@")) {
                    String[] s = value.split("@");
                    if (s.length == 1) {
                        if (s[0] != null && !"".equals(s[0]) && s[0].matches(Constants.REGEX_INTEGER)) {
                            try {
                                id = Integer.parseInt(s[0]);
                                item.setId(id);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else {
                            name = s[0];
                            item.setName(name);
                        }
                    } else if (s.length > 1) {
                        if (s[0] != null && !"".equals(s[0]) && s[0].matches(Constants.REGEX_INTEGER)) {
                            try {
                                id = Integer.parseInt(s[0]);
                                item.setId(id);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        name = s[1];
                        item.setName(name);
                        if (s.length > 2) {
                            code = s[2];
                            item.setReferenceCode(code);
                        }
                    }
                }
                if (widget instanceof OverAllLookUp) {
                    ((OverAllLookUp) widget).setSelected(item);
                } else if (widget instanceof ReferenceLookUp) {
                    ((ReferenceLookUp) widget).setSelected(item);
                }
            }
        } else if (widget != null && widget instanceof DynamicInputWidget.CustomDatePicker) {
            if (value != null) {
                try {
                    Date d = DateUtils.parse(value, DateTimeFormat.getFormat(Constants.DATE_PATTERN));
                    ((DynamicInputWidget.CustomDatePicker) widget).setDate(d);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        } else if (widget != null && widget instanceof TextBox) {
            ((TextBox) widget).setValue(value);
        } else if (widget != null && widget instanceof TextArea) {
            ((TextArea) widget).setValue(value);
        } else if (widget != null && widget instanceof Label) {
            ((Label) widget).setText(value);
        } else if (widget != null && widget instanceof HorizontalPanel) {
            HorizontalPanel panel = (HorizontalPanel) widget;
            if (value != null && value.contains("@")) {
                String[] s = value.split("@");
                if (panel.getWidgetCount() > 2) {
                    DynamicInputWidget.CustomDatePicker datePicker1 = (DynamicInputWidget.CustomDatePicker) panel.getWidget(0);
                    DynamicInputWidget.CustomDatePicker datePicker2 = (DynamicInputWidget.CustomDatePicker) panel.getWidget(2);
                    try {
                        Date date1 = DateUtils.parse(s[0], DateTimeFormat.getFormat(Constants.DATE_PATTERN));
                        Date date2 = DateUtils.parse(s[1], DateTimeFormat.getFormat(Constants.DATE_PATTERN));
                        datePicker1.setDate(date1);
                        datePicker2.setDate(date2);
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    DataListBox rangers = (DataListBox) panel.getWidget(0);
                    TextBox dayRange = (TextBox) panel.getWidget(1);
                    SelectItem[] rengersItem = SelectItem.asItems(Operands.DateT.RANGERS);
                    for (SelectItem item : rengersItem) {
                        if (item != null && s[0].equals(item.getName())) {
                            rangers.setSelected(item);
                            break;
                        }
                    }
                    dayRange.setText(s[1]);
                }
            }
        }
    }

    private LookUp getLookUp(boolean isReference) {
        return (isReference ? (ReferenceLookUp) getWidget() : (OverAllLookUp) getWidget());
    }

    private TextBox getTextBox() {
        return ((TextBox) getWidget());
    }

    private TextArea getTextArea() {
        return ((TextArea) getWidget());
    }

    private DynamicInputWidget.CustomDatePicker getDatePicker() {
        return ((DynamicInputWidget.CustomDatePicker) getWidget());
    }

    public ModelField getField() {
        return field;
    }

    public void setField(ModelField field) {
        this.field = field;
        initType();
    }

    public void addBlurHandler(BlurHandler blurHandler) {
        this.blurHandler = blurHandler;
    }

    public void clear() {
        Widget widget = getWidget();
        if (widget != null) {
            if (widget instanceof LookUp) {
                ((OverAllLookUp) widget).clearAndClearItems();
            } else if (widget instanceof DynamicInputWidget.CustomDatePicker) {
                ((DynamicInputWidget.CustomDatePicker) widget).setDate(null);
            } else if (widget instanceof TextBox) {
                ((TextBox) widget).setText("");
            } else if (widget instanceof TextArea) {
                ((TextArea) widget).setValue("");
            } else if (widget instanceof Label) {
                ((Label) widget).setText("");
            } else {
                ((TextBox) widget).setText("");
            }
        }
    }

    public void setEnabled(boolean enabled) {
        Widget widget = getWidget();
        if (widget != null) {
            if (widget instanceof LookUp) {
                ((OverAllLookUp) widget).setEnabled(enabled);
            } else if (widget instanceof DynamicInputWidget.CustomDatePicker) {
                ((DynamicInputWidget.CustomDatePicker) widget).setEnabled(enabled);
            } else if (widget instanceof TextBox) {
                ((TextBox) widget).setEnabled(enabled);
            } else if (widget instanceof TextArea) {
                ((TextArea) widget).setEnabled(enabled);
            } else {
                ((TextBox) widget).setEnabled(enabled);
            }
        }
    }

    public class CustomDatePicker extends DatePicker implements CustomCellInterface {
        private DateTimeFormat dateFormatter = DateUtils.getFormat();
        private Date date;

        @Override
        public String getDisplayValue() {
            return getDate() != null ? DateUtils.format(getDate()) : "Please Select";
        }

        @Override
        public void setItemValue(Object value) {
            setDate((Date) value);
        }

        @Override
        public Date getDate() {
            if (getText() != null && !getText().isEmpty() && !"Please Select".equals(getText())) {
                //  if (date == null) {
                try {
                    if (dateFormatter == null) {
                        dateFormatter = DateTimeFormat.getFormat(Constants.DATE_PATTERN);
                    }
                    date = dateFormatter.parse(getText());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                //   }
                return date;
            }
            return null;
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }
}
