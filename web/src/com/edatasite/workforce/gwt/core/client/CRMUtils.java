package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: hayot
 * Date: 4/21/11
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class CRMUtils extends Utils {
    public static Map<SelectItem, CheckBox> getSelectItemsAsCheckBoxMap(boolean asRadioButton, SelectItem... items) {
        Map<SelectItem, CheckBox> map = new LinkedHashMap<>();
        if (items != null && items.length > 0) {
            String radioButtonName = "rb" + (new Date()).getTime();
            int i = 0;
            for (final SelectItem item : items) {
                if (item != null) {
                    String className = "matched_vacancies_" + i;
                    String name = "";

                    if (asRadioButton) {
                        name = radioButtonName;
                    } else {
                        name = item.getName();
                    }
                    map.put(item, getSelectItemAsCheckBox(item, asRadioButton, name, className));
                    i++;
                }
            }
        }

        return map;
    }

    public static CheckBox getSelectItemAsCheckBox(final SelectItem selectItem, boolean asRadioButton, String name, String className) {
        if (selectItem == null) {
            return null;
        }
        CheckBox checkBox = asRadioButton ? new KpiRadioButton("<span>" + name + "</span>") : new CheckBox("<span>" + selectItem.getName() + "</span>", true);
        if (asRadioButton) {
            checkBox.setText(selectItem.getName());
            checkBox.setName(name);
            checkBox.setFormValue(selectItem.getReferenceCode());
        }
        if (selectItem.isSelected()) {
            checkBox.setValue(Boolean.TRUE);
        }
        checkBox.addValueChangeHandler(booleanValueChangeEvent -> selectItem.setSelected(booleanValueChangeEvent.getValue()));
        checkBox.getElement().setId(className);
        return checkBox;
    }

    @Deprecated //use SelectItem.getSelectItemsAsCommaDelimeted()
    public static String getSelectItemsAsCommaDelimeted(SelectItem[] selectItems, boolean onlySelected) {
        StringBuilder s = new StringBuilder();
        SelectItem[] selecteds = onlySelected ? SelectItem.getOnlySelecteds(selectItems) : selectItems;
        if (selectItems != null && selectItems.length > 0) {
            String delimitr = "";
            for (SelectItem selectItem : selecteds) {
                if (selectItem != null) {
                    s.append(delimitr).append(selectItem.getName());
                    delimitr = ", ";
                }
            }
        }
        return s.toString();
    }

    public static String getAsCommoDelimited(List collection, String returnIfNull, String... delimitrs) {
        if (collection == null || collection.size() == 0) {
            return returnIfNull;
        }
        String delimitr = ",";
        if (delimitrs != null && delimitrs.length > 0) {
            delimitr = delimitrs[0];
        }
        StringBuilder ids = new StringBuilder();
        String delim = "";
        for (Object element : collection) {
            ids.append(delim).append(element.toString());
            delim = delimitr;
        }
        return ids.toString();
    }


    public static void onEmployeeAdded(final Widget widget, final DataListBox assigneeDropDown) {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, widget, (sender, args) -> {
            List<SelectItem> assignedItems = new ArrayList<>();
            assignedItems.addAll(Arrays.asList(assigneeDropDown.getItems()));
            if (args != null && args instanceof NewEmployee[]) {
                SelectItem mustBeSelected = assigneeDropDown.getSelectedItem();
                for (NewEmployee newEmployee : (NewEmployee[]) args) {
                    if (newEmployee != null && newEmployee.getObjectID() != null) {
                        SelectItem item = new SelectItem(newEmployee.getObjectID(), (newEmployee.getLname() != null && !"".equals(newEmployee.getLname()) ? newEmployee.getLname() : "") + " " + (newEmployee.getFname() != null && !"".equals(newEmployee.getFname()) ? newEmployee.getFname() : ""));
                        if (!assignedItems.contains(item)) {
                            assignedItems.add(item);
                            mustBeSelected = item;
                        }
                    }
                }
                if (assignedItems.size() > 0) {
                    assigneeDropDown.clear();
                    assigneeDropDown.setItems(assignedItems.toArray(new SelectItem[]{}));
                    if (mustBeSelected != null) {
                        assigneeDropDown.setSelected(mustBeSelected);
                    }
                }
            }
        });
    }

    public static String formatDate(Date date, boolean withTime) {
        if (date == null) {
            return "";
        }
        if (withTime) {
            return DateUtils.formatInternal(date);
        }
        return DateUtils.format(date);
    }

    public static String refactor(Date s, boolean withTime) {
        if (s != null) {
            return formatDate(s, withTime);
        }
        return "N/A";
    }

    public static String refactor(String s) {
        int index = -1;
        if (s != null) {
            index = s.lastIndexOf(".0");
            if (index != -1) {
                if (!"".equals(s)) {
                    return s.replace(".0", "");
                }
            } else {
                return s;
            }
        }
        return "";
    }


}
