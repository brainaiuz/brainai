package com.edatasite.workforce.gwt.profile.client.ui.view.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/17/11
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class FinancialWidgets {

    private DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMMM");
    private DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");

    private WfmDropdown currencyDropdown;
    private WfmDropdown finYearEndDay;
    private WfmDropdown finYearEndMonth;

    private WfmDropdown conversionMonth;
    private WfmDropdown conversionYear;

    private String financialWidget = "financial_widget_";

    public FinancialWidgets(boolean finYearEnd, boolean conversionDate) {
        if (finYearEnd)
            initializeFinYearEndWidgets();
        if(conversionDate)
            initializeConvesionDateWidgets();

    }

    private void initializeFinYearEndWidgets() {
        currencyDropdown = new WfmDropdown();
        currencyDropdown.ensureDebugId(financialWidget+"currencyDropdown");

        finYearEndDay = new WfmDropdown(false, true);
        finYearEndDay.ensureDebugId(financialWidget+"finYearEndDay");

        finYearEndMonth = new WfmDropdown(false, true);
        finYearEndMonth.ensureDebugId(financialWidget+"finYearEndMonth");

        finYearEndDay.setWidth("120px");
        finYearEndMonth.addEventHandler(new DropdownListener() {
            public void itemSelected() {
                setFinYearEndDayItems();
            }

            public void saveNewItem() {
            }
        });
    }

    public void setFinYearEndMonthItems() {
        finYearEndMonth.clear();
        Date date = DateUtil.getYearFirstDay(new Date());
        for (int i = 1; i <= 12; i++) {
            SelectItem item = new SelectItem(date.getMonth(), monthFormat.format(date));
            finYearEndMonth.addItem(item);
            date = DateUtil.addMonths(date, 1);
        }
        finYearEndMonth.setSelected(11/*Integer.valueOf(date.getMonth())*/);
    }

    public void setFinYearEndDayItems() {
        Integer selectedDate = finYearEndDay.getSelectedId();
        finYearEndDay.clear();
        Date date = new Date();
        if (finYearEndMonth.getSelectedId() != null) {
            date.setMonth(finYearEndMonth.getSelectedId());
        }
        Date monthEnd = DateUtil.getMonthLastDate(date);
        for (int i = 1; i <= monthEnd.getDate(); i++) {
            finYearEndDay.addItem(new SelectItem(i, String.valueOf(i)));
        }
        if (selectedDate != null && finYearEndDay.getItemCount() > selectedDate) {
            finYearEndDay.setSelected(selectedDate);
        } else {
            finYearEndDay.setSelected(monthEnd.getDate());
        }
    }

    public DateNonConvertable getFinYearEndDate() {
        return new DateNonConvertable(new Date(new Date().getYear(), finYearEndMonth.getSelectedId(), finYearEndDay.getSelectedId(), 23, 59, 59));
    }

    public void setFinYearEndDate(DateNonConvertable finYearEndDate) {
        if (finYearEndDate != null) {
            finYearEndDay.setSelected(finYearEndDate.getNonConvertedDate().getDate());
            finYearEndMonth.setSelected(finYearEndDate.getNonConvertedDate().getMonth());
        }
    }

    public InputGroup createFinYearEndWidget() {
        return new InputGroup(finYearEndDay, finYearEndMonth);
    }

    private void initializeConvesionDateWidgets() {
        conversionMonth = new WfmDropdown(false, true);
        conversionMonth.ensureDebugId(financialWidget+"conversionMonth");

        conversionYear = new WfmDropdown(false, true);
        conversionYear.ensureDebugId(financialWidget+"conversionYear");

        conversionYear.setWidth("120px");
    }

    public void setConversionDateMonthItems() {
        conversionMonth.clear();
        Date date = DateUtil.getYearFirstDay(new Date());
        for (int i = 1; i <= 12; i++) {
            SelectItem item = new SelectItem(date.getMonth(), monthFormat.format(date));
            conversionMonth.addItem(item);
            date = DateUtil.addMonths(date, 1);
        }
    }

    public void setConversionDateYearItems() {
        conversionYear.clear();
        Date date = new Date();
        date.setYear(date.getYear() - 40);
        Integer itemId = null;
        for (int i = 1; i <= 41; i++) {
            String year = yearFormat.format(date);
            itemId = date.getYear();
            conversionYear.addItem(new SelectItem(itemId, year));
            date.setYear(date.getYear() + 1);
        }
        conversionYear.setSelected(itemId);
    }

    public Date getConversionDate() {
        return new Date(conversionYear.getSelectedId(), conversionMonth.getSelectedId(), 1);
    }

    public void setConversionDate(Date conversionDate) {
        if (conversionDate != null) {
            conversionMonth.setSelected(conversionDate.getMonth());
            conversionYear.setSelected(conversionDate.getYear());
        } else {
            Date currentDate = new Date();
            conversionMonth.setSelected(currentDate.getMonth());
            conversionYear.setSelected(currentDate.getYear());
        }
    }

    public InputGroup createConversionDateWidget() {
        return new InputGroup(conversionYear, conversionMonth);
    }

    public WfmDropdown getCurrencyDropdown() {
        return currencyDropdown;
    }

    public WfmDropdown getFinYearEndDay() {
        return finYearEndDay;
    }

    public WfmDropdown getFinYearEndMonth() {
        return finYearEndMonth;
    }

    public WfmDropdown getConversionMonth() {
        return conversionMonth;
    }

    public WfmDropdown getConversionYear() {
        return conversionYear;
    }
}
