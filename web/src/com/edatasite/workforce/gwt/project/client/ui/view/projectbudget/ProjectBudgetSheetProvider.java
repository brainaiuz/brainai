package com.edatasite.workforce.gwt.project.client.ui.view.projectbudget;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/21/12
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectBudgetSheetProvider {
    Integer getProjectID();
    List<String> getMonthColumnKeys();
    ArrayList<DateNonConvertable[]> getMonthIntervalsList();
    void calculateProjectProfit();
    void calculateProjectColumnProfit(Integer currentColumn);
}
