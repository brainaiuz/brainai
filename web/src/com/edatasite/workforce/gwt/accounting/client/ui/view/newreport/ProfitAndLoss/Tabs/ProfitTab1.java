package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.ProfitAndLoss.Tabs;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AccountingImageBundle;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.ProfitAndLoss.NewProfitAndLoss;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by admin on 08.09.2014.
 */
public class ProfitTab1 extends Composite implements Constants {
    interface ProfitTab1UiBinder extends UiBinder<HTMLPanel, ProfitTab1> {
    }

    private NewProfitAndLoss parent;
    private Element grossProfitElement;
    private Element netProfitElement;

    private Date start;
    private Date end;
    private boolean showBudget;
    private FromToDate[] compareDates;
    private static final ProfitTab1UiBinder ourUiBinder = GWT.create(ProfitTab1UiBinder.class);
    private static final AccountingImageBundle accImageBundle = (AccountingImageBundle) GWT.create(AccountingImageBundle.class);
    private final HashSet<String> shownObjects = new HashSet<>();
    ArrayList<BudgetInDate> budgetAndVarianceGrossProfit = new ArrayList<>(2);
    ArrayList<BudgetInDate> budgetAndVarianceNetProfit = new ArrayList<>(2);

    @UiField
    HTMLPanel profitTable;

    private Element myTable;

    public ProfitTab1() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    public void onInitialize(final NewProfitAndLoss parent) {
        this.parent = parent;
    }

    public void update(BudgetManagerItems profLoss, Date startDate, Date endDate, boolean excludeZero, final boolean showBudget) {
        if (myTable != null) {
            myTable.removeFromParent();
        }
        this.showBudget = showBudget;
        budgetAndVarianceGrossProfit = new ArrayList<>();
        budgetAndVarianceNetProfit = new ArrayList<>();
        myTable = DOM.createTable();
        myTable.setClassName("table table_report table_report_sections");
        profitTable.getElement().appendChild(myTable);

        start = startDate;
        end = endDate;
        createHeader();

        //REVENUE
        if (profLoss.getRevenue() != null && profLoss.getRevenue().getAccountItems() != null && profLoss.getRevenue().getAccountItems().length > 0) {
            Element revenueGroup = createTBody();
            if (showBudget) {
                collectShowBudgetTreeData(revenueGroup, profLoss.getRevenue(), false, excludeZero);
                calculateBudgetAndVariance(profLoss.getRevenue(), false);
            } else {
                collectTreeData(revenueGroup, profLoss.getRevenue(), false, excludeZero);
            }
        }
        //SALES
        if (profLoss.getSale() != null && profLoss.getSale().getAccountItems() != null && profLoss.getSale().getAccountItems().length > 0) {
            Element salesGroup = createTBody();
            if (showBudget) {
                collectShowBudgetTreeData(salesGroup, profLoss.getSale(), false, excludeZero);
                calculateBudgetAndVariance(profLoss.getSale(), false);
            } else {
                collectTreeData(salesGroup, profLoss.getSale(), false, excludeZero);
            }
        }
        //EXPENSE GROUP
        if (profLoss.getExpense() != null && profLoss.getExpense().getAccountItems() != null && profLoss.getExpense().getAccountItems().length > 0) {
            Element expenseGroup = createTBody();
            if (showBudget) {
                collectShowBudgetTreeData(expenseGroup, profLoss.getExpense(), true, excludeZero);
                calculateBudgetAndVariance(profLoss.getExpense(), true);
            } else {
                collectTreeData(expenseGroup, profLoss.getExpense(), true, excludeZero);
            }
        }
        //DIRECT EXPENSES
        if (profLoss.getDirectCosts() != null && profLoss.getDirectCosts().getAccountItems() != null && profLoss.getDirectCosts().getAccountItems().length > 0) {
            Element directExpenseGroup = createTBody();
            if (showBudget) {
                collectShowBudgetTreeData(directExpenseGroup, profLoss.getDirectCosts(), true, excludeZero);
                calculateBudgetAndVariance(profLoss.getDirectCosts(), true);
            } else {
                collectTreeData(directExpenseGroup, profLoss.getDirectCosts(), true, excludeZero);
            }
        }

        if (showBudget) {
            profLoss.getGrossProfit().addAll(budgetAndVarianceGrossProfit);
        }
        Element grossbodyElement = DOM.createTBody();
        grossbodyElement.setClassName("gross_total_row");
        myTable.appendChild(grossbodyElement);
        grossProfitElement = grossbodyElement;
        createTotals(grossProfitElement, NewProfitAndLoss.accountingStrings.grossProfit(), profLoss.getGrossProfit());


        //OTHER INCOME
        if (profLoss.getOtherIncome() != null && profLoss.getOtherIncome().getAccountItems() != null && profLoss.getOtherIncome().getAccountItems().length > 0) {
            Element otherIncomeGroup = createTBody();
            if (showBudget) {
                collectShowBudgetTreeData(otherIncomeGroup, profLoss.getOtherIncome(), false, excludeZero);
                calculateBudgetAndVarianceNetProfit(profLoss.getOtherIncome(), false);
            } else {
                collectTreeData(otherIncomeGroup, profLoss.getOtherIncome(), false, excludeZero);
            }
        }
        //DEPRECIATION
        if (profLoss.getDepreciation() != null && profLoss.getDepreciation().getAccountItems() != null && profLoss.getDepreciation().getAccountItems().length > 0) {
            Element costOfSalesGroup = createTBody();
            if (showBudget) {
                collectShowBudgetTreeData(costOfSalesGroup, profLoss.getDepreciation(), true, excludeZero);
                calculateBudgetAndVarianceNetProfit(profLoss.getDepreciation(), false);
            } else {
                collectTreeData(costOfSalesGroup, profLoss.getDepreciation(), true, excludeZero);
            }
        }
        //OVERHEAD
        if (profLoss.getOverhead() != null && profLoss.getOverhead().getAccountItems() != null && profLoss.getOverhead().getAccountItems().length > 0) {
            Element overHeadGroup = createTBody();
            if (showBudget) {
                collectShowBudgetTreeData(overHeadGroup, profLoss.getOverhead(), true, excludeZero);
                calculateBudgetAndVarianceNetProfit(profLoss.getOverhead(), true);
            } else {
                collectTreeData(overHeadGroup, profLoss.getOverhead(), true, excludeZero);
            }
        }

        if (showBudget) {
            profLoss.getNetProfit().addAll(budgetAndVarianceNetProfit);
        }
        Element netbodyElement = DOM.createTBody();
        netbodyElement.setClassName("gross_total_row");
        myTable.appendChild(netbodyElement);
        netProfitElement = netbodyElement;
        createTotals(netProfitElement, NewProfitAndLoss.accountingStrings.netProfit(), profLoss.getNetProfit());

    }

    private void calculateBudgetAndVariance(AccountItemsByAccountType accountType, boolean isNegative) {
        AccountItemWithBudgetDate[] accItems = accountType.getAccountItems();
        for (AccountItemWithBudgetDate accItem1 : accItems) {
            ActBudVar var = accItem1.getVariance();
            if (budgetAndVarianceGrossProfit != null && budgetAndVarianceGrossProfit.size() > 0 && var.getBudget() != null) {
                budgetAndVarianceGrossProfit.get(0).setValue(budgetAndVarianceGrossProfit.get(0).getValue().add(isNegative ? var.getBudget().multiply(BigDecimal.valueOf(-1)) : var.getBudget()));
            } else if (var.getBudget() != null) {
                BudgetInDate budgetInDate = new BudgetInDate();
                budgetInDate.setValue(var.getBudget());
                budgetAndVarianceGrossProfit.add(budgetInDate);
            }
            if (budgetAndVarianceGrossProfit != null && budgetAndVarianceGrossProfit.size() > 1 && var.getVariance() != null) {
                budgetAndVarianceGrossProfit.get(1).setValue(budgetAndVarianceGrossProfit.get(1).getValue().add(isNegative ? var.getVariance().multiply(BigDecimal.valueOf(-1)) : var.getVariance()));
            } else if (var.getVariance() != null) {
                BudgetInDate budgetInDate = new BudgetInDate();
                budgetInDate.setValue(var.getVariance());
                budgetAndVarianceGrossProfit.add(budgetInDate);
            }
        }
    }

    private void calculateBudgetAndVarianceNetProfit(AccountItemsByAccountType accountType, boolean isNegative) {
        AccountItemWithBudgetDate[] accItems = accountType.getAccountItems();
        for (AccountItemWithBudgetDate accItem1 : accItems) {
            ActBudVar var = accItem1.getVariance();
            if (budgetAndVarianceNetProfit != null && budgetAndVarianceNetProfit.size() > 0 && var.getBudget() != null) {
                budgetAndVarianceNetProfit.get(0).setValue(budgetAndVarianceNetProfit.get(0).getValue().add(isNegative ? var.getBudget().multiply(BigDecimal.valueOf(-1)) : var.getBudget()));
            } else if (var.getBudget() != null) {
                BudgetInDate budgetInDate = new BudgetInDate();
                if (budgetAndVarianceGrossProfit != null && !budgetAndVarianceGrossProfit.isEmpty()) {
                    budgetInDate.setValue(isNegative ? budgetAndVarianceGrossProfit.get(0).getValue().subtract(var.getBudget()) : budgetAndVarianceGrossProfit.get(0).getValue().add(var.getBudget()));
                } else {
                    budgetInDate.setValue(var.getBudget());
                }
                budgetAndVarianceNetProfit.add(budgetInDate);
            }
            if (budgetAndVarianceNetProfit != null && budgetAndVarianceNetProfit.size() > 1 && var.getVariance() != null) {
                budgetAndVarianceNetProfit.get(1).setValue(budgetAndVarianceNetProfit.get(1).getValue().add(isNegative ? var.getBudget().multiply(BigDecimal.valueOf(-1)) : var.getVariance()));
            } else if (var.getVariance() != null) {
                BudgetInDate budgetInDate = new BudgetInDate();
                budgetInDate.setValue(var.getVariance());
                budgetAndVarianceNetProfit.add(budgetInDate);
            }
        }
    }

    private void collectShowBudgetTreeData(Element elment, AccountItemsByAccountType item, boolean isNegative, boolean excludeZero) {
        AccountItemWithBudgetDate[] accItems = item.getAccountItems();
        Image upImage = new Image(accImageBundle.upGreen());
        Image downImage = new Image(accImageBundle.downRed());
        if (item != null && accItems != null && accItems.length > 0) {
            createGroupHeader(elment, item);
            for (AccountItemWithBudgetDate accItem1 : accItems) {
                boolean addRow = !excludeZero;
                Element tr = DOM.createTR();
                Element td = DOM.createTD();
                tr.appendChild(td);

                ActBudVar var = accItem1.getVariance();
                if (var != null) {
                    td.setInnerHTML(accItem1.getName().concat("<small>").concat("(").concat(accItem1.getCode()).concat(")").concat("</small>"));
                    td.setAttribute("style", "text-align:left");
                    tr.appendChild(td);

                    Element td1 = DOM.createTD();
                    td1.addClassName(TEXT_RIGHT);
                    tr.appendChild(td1);
                    if (var.getActual() != null) {
                        td1.setInnerHTML(parent.getValueAsString(var.getActual(), isNegative));
                    }
                    Element td2 = DOM.createTD();
                    td2.addClassName(TEXT_RIGHT);
                    tr.appendChild(td2);
                    if (var.getBudget() != null) {
                        td2.setInnerHTML(parent.getValueAsString(var.getBudget(), isNegative));
                    }
                    Element td3 = DOM.createTD();
                    td3.addClassName(TEXT_RIGHT);
                    tr.appendChild(td3);
                    if (var.getVariance() != null) {
                        HTMLPanel panel = new HTMLPanel(AccountingUtils.get().formatPrice(var.getVariance()) + "%");
                        if (var.getVariance().compareTo(AccountingConstants.ZERO) > 0) {
                            panel.add(upImage);
                        } else {
                            panel.add(downImage);
                        }
                        td3.appendChild(panel.getElement());
                    }
                }
                elment.appendChild(tr);
            }
            createTotalRow(elment, item.getTotalWithVariance(), null, NewProfitAndLoss.wfmStrings.total() + " " + item.getGroupName(), isNegative);
        }
    }

    private void createTotalRow(Element element, ActBudVar variance, ActBudVar ytdVariance, String groupName, boolean isNegative) {
        if (element != null) {
            Image upImage = new Image(accImageBundle.upGreen());
            Image downImage = new Image(accImageBundle.downRed());
            Element tr = DOM.createTR();
            tr.addClassName("total_row");
            element.appendChild(tr);

            Element td = DOM.createTD();
            td.setInnerHTML(groupName);
            tr.appendChild(td);
            element.appendChild(tr);
            if (variance != null) {

                Element td1 = DOM.createTD();
                td1.addClassName(TEXT_RIGHT);
                tr.appendChild(td1);
                if (variance.getActual() != null) {
                    td1.setInnerHTML(parent.getValueAsString(variance.getActual(), isNegative));
                }

                Element td2 = DOM.createTD();
                td2.addClassName(TEXT_RIGHT);
                tr.appendChild(td2);
                if (variance.getBudget() != null) {
                    td2.setInnerHTML(parent.getValueAsString(variance.getBudget(), isNegative));
                }

                Element td3 = DOM.createTD();
                td3.addClassName(TEXT_RIGHT);
                tr.appendChild(td3);
                if (variance.getVariance() != null) {
                    HTMLPanel panel = new HTMLPanel(AccountingUtils.get().formatPrice(variance.getVariance()) + "%");
                    if (variance.getVariance().compareTo(AccountingConstants.ZERO) > 0) {
                        panel.add(upImage);
                    } else {
                        panel.add(downImage);
                    }
                    td3.appendChild(panel.getElement());
                }
            }
        }
    }

    private Element createTBody() {
        Element tbodyElement = DOM.createTBody();
        tbodyElement.setClassName("category_set expanded");

        myTable.appendChild(tbodyElement);
        return tbodyElement;
    }

    private void collectTreeData(Element element, AccountItemsByAccountType item, boolean isNegative, boolean excludeZero) {
        if (item != null) {
            AccountItemWithBudgetDate[] accItems = item.getAccountItems();
            if (accItems != null && accItems.length > 0) {

                Map<String, AccountItemWithBudgetDate> map1 = new HashMap<>(accItems.length);
                ArrayList<AccountItemWithBudgetDate> map2 = new ArrayList<>();

                Arrays.stream(accItems).forEach(acc -> map1.put(acc.getCode(), acc));
                Arrays.stream(accItems).forEach(acc -> {
                    if (acc.getParentCode() != null && !acc.getParentCode().isEmpty()) {
                        if (map1.get(acc.getParentCode()) == null) {
                            AccountItemWithBudgetDate budgetDate = new AccountItemWithBudgetDate(acc.getParentId(), acc.getParentCode(), acc.getParentName());
                            budgetDate.getChilds().add(acc);
                            map1.put(acc.getParentCode(), budgetDate);
                            map2.add(budgetDate);
                        } else {
                            map1.get(acc.getParentCode()).getChilds().add(acc);
                        }
                    } else {
                        map2.add(acc);
                    }
                });

                createGroupHeader(element, item);
                map2.forEach(key -> {
                    ArrayList<BigDecimal> childTotal = new ArrayList<>(parent.getColumnCount() - 1);
                    for (int i = 0; i < parent.getColumnCount(); i++) {
                        childTotal.add(i, BigDecimal.ZERO);
                    }
                    createGroup1(element, key, isNegative, excludeZero, childTotal);
                });
                createTotalRow(element, NewProfitAndLoss.wfmStrings.total() + " " + item.getGroupName(), item.getActualTotal(), isNegative);
            }
        }
    }

    private void createGroup1(Element parentElement, AccountItemWithBudgetDate accItem1, boolean isNegative,
                              boolean excludeZero, ArrayList<BigDecimal> childTotal) {
        boolean addRow = !excludeZero;
        Element element = create(parentElement, accItem1, isNegative, excludeZero, addRow, !accItem1.getChilds().isEmpty());
        if (!accItem1.getChilds().isEmpty()) {
            for (AccountItemWithBudgetDate child : accItem1.getChilds()) {
                createGroup1(element, child, isNegative, excludeZero, childTotal);

                List<BudgetInDate> budgetInOneAccount = child.getRowCells();
                if (budgetInOneAccount != null && !child.isCalculated()) {
                    child.setCalculated(true);
                    calculate(childTotal, budgetInOneAccount);
                }
            }
            if (accItem1.getRowCells() != null) {
                List<BudgetInDate> budgetInOneAccount = accItem1.getRowCells();
                if (budgetInOneAccount != null && !accItem1.isCalculated()) {
                    accItem1.setCalculated(true);
                    calculate(childTotal, budgetInOneAccount);
                }
            }

            boolean used = false;
            if (!accItem1.getChilds().isEmpty() && accItem1.getRowCells() != null && !accItem1.getRowCells().isEmpty()) {
                List<BudgetInDate> list = new ArrayList<>(accItem1.getRowCells());
                Collections.reverse(list);
                for (BudgetInDate bItem : list) {
                    if (bItem.getValue() != null) {
                        used = true;
                    }
                }
            }
            if (accItem1.getName() != null && !used) {
                List<BigDecimal> total = new ArrayList<>(childTotal);
                Collections.reverse(total);
                createGroupTotalRow(element, NewProfitAndLoss.wfmStrings.total() + " " + accItem1.getName(), total, isNegative);
            } else {
                ArrayList<BigDecimal> accountAndChildTotal = new ArrayList<>(parent.getColumnCount() - 1);
                for (int i = 0; i < parent.getColumnCount(); i++) {
                    accountAndChildTotal.add(i, BigDecimal.ZERO);
                }
                calculate(accountAndChildTotal, accItem1.getRowCells());
                for (AccountItemWithBudgetDate child : accItem1.getChilds()) {
                    List<BudgetInDate> budgetInOneAccount = child.getRowCells();
                    if (budgetInOneAccount != null) {
                        calculate(accountAndChildTotal, budgetInOneAccount);
                    }
                }

                List<BigDecimal> total = new ArrayList<>(accountAndChildTotal);
                Collections.reverse(total);
                createGroupTotalRow(element, NewProfitAndLoss.wfmStrings.total() + " " + accItem1.getName(), total, isNegative);
            }
        }
    }

    private void calculate(ArrayList<BigDecimal> childTotal, List<BudgetInDate> budgetInOneAccount) {
        int i = 0;
        for (BigDecimal cellTotal : childTotal) {
            BigDecimal amount = budgetInOneAccount.get(i).getValue() != null
                    ? budgetInOneAccount.get(i).getValue()
                    : BigDecimal.ZERO;
            if (cellTotal == null) {
                childTotal.set(i, amount);
            } else {
                childTotal.set(i, cellTotal.add(amount));
            }
            i++;
        }
    }

    private Element create(Element element, AccountItemWithBudgetDate accItem1, boolean isNegative, boolean excludeZero, boolean addRow, boolean hasChilds) {
        Element mainTr = DOM.createTR();
        Element mainTd = DOM.createTD();
        mainTr.appendChild(mainTd);
        if (hasChilds) {
            Element childTable = DOM.createTable();
            childTable.setClassName("table table_report childTable table_report_sections");

            Element header = createTH();
            header.addClassName("childTableHeader");

            Element childTBody = DOM.createTBody();
            childTBody.setClassName("category_set");
            childTBody.addClassName("collapsed");

            childTable.appendChild(header);
            childTable.appendChild(childTBody);

            Element childTr = DOM.createTR();
            childTr.addClassName("heading_row");
            Element childTd = DOM.createTD();
            childTr.appendChild(childTd);

            Element icon = DOM.createElement("i");
            icon.addClassName("btn--circle plusMinus");
            DOM.sinkEvents(icon.cast(), Event.ONCLICK);
            DOM.setEventListener(icon.cast(), event -> {
                if (childTBody.getClassName().contains("collapsed")) {
                    childTBody.removeClassName("collapsed");
                    childTBody.addClassName("expanded");
                    shownObjects.add(accItem1.getCode());
                } else {
                    childTBody.removeClassName("expanded");
                    childTBody.addClassName("collapsed");
                    shownObjects.remove(accItem1.getCode());
                }
            });

            mainTd.addClassName("second_level");
            mainTd.setAttribute("colspan", String.valueOf(parent.getColumnCount() + 1));
            mainTd.appendChild(childTable);

            childTd.appendChild(icon);
            childTd.addClassName(LEFT_ALIGN_CELL);
            Element nameElement = DOM.createElement("span");
            nameElement.setInnerHTML(accItem1.getName().concat("<small>").concat("(").concat(accItem1.getCode()).concat(")").concat("</small>"));
            childTd.appendChild(nameElement);
            if (accItem1.getRowCells() != null) {
                List<BudgetInDate> list = new ArrayList<>();
                list.addAll(accItem1.getRowCells());
                Collections.reverse(list);
                for (int i = 0; i < list.size(); i++) {
                    BudgetInDate bItem = list.get(i);
                    Element td1 = DOM.createTD();
                    Date from;
                    Date to;
                    if (compareDates != null && compareDates.length > 0 && compareDates.length > i) {
                        from = compareDates[compareDates.length - 1 - i].getFrom().getDate();
                        to = compareDates[compareDates.length - 1 - i].getTo().getDate();
                    } else {
                        from = parent.getStartDatePicker().getDate();
                        to = parent.getEndDatePicker().getDate();
                    }
                    td1.addClassName(RIGHT_ALIGN_CELL);
                    if (bItem.getValue() != null) {
                        if (excludeZero) {
                            if (bItem.getValue().compareTo(BigDecimal.ZERO) != 0) {
                                addRow = true;
                                td1.appendChild(parent.getDOMLink(bItem.getValue(), isNegative, accItem1.getId(), from, to));
                            }
                        } else {
                            addRow = true;
                            td1.appendChild(parent.getDOMLink(bItem.getValue(), isNegative, accItem1.getId(), from, to));
                        }
                    }
                    childTr.appendChild(td1);
                }
            }
            childTBody.appendChild(childTr);
            if (addRow) {
                element.appendChild(mainTr);
            }
            return childTBody;

        } else /*if (accItem1.getRowCells() != null)*/ {
            mainTr.appendChild(mainTd);
            mainTd.addClassName(LEFT_ALIGN_CELL);
            mainTd.setAttribute("style", "padding-left:45px");
            mainTd.setInnerHTML(accItem1.getName().concat("<small>").concat("(").concat(accItem1.getCode()).concat(")").concat("</small>"));
            if (accItem1.getRowCells() != null) {
                List<BudgetInDate> list = new ArrayList<>();
                list.addAll(accItem1.getRowCells());
                Collections.reverse(list);

                for (int i = 0; i < list.size(); i++) {
                    BudgetInDate bItem = list.get(i);
                    Element td1 = DOM.createTD();
                    td1.addClassName(RIGHT_ALIGN_CELL);
                    Date from;
                    Date to;
                    if (compareDates != null && compareDates.length > 0 && compareDates.length > i) {
                        from = compareDates[compareDates.length - 1 - i].getFrom().getDate();
                        to = compareDates[compareDates.length - 1 - i].getTo().getDate();
                    } else {
                        from = parent.getStartDatePicker().getDate();
                        to = parent.getEndDatePicker().getDate();
                    }
                    if (bItem.getValue() != null) {
                        if (excludeZero) {
                            if (bItem.getValue().compareTo(BigDecimal.ZERO) != 0) {
                                addRow = true;
                                td1.appendChild(parent.getDOMLink(bItem.getValue(), isNegative, accItem1.getId(), from, to));
                            }
                        } else {
                            addRow = true;
                            td1.appendChild(parent.getDOMLink(bItem.getValue(), isNegative, accItem1.getId(), from, to));
                        }
                    }
                    mainTr.appendChild(td1);
                }
            }
            if (addRow) {
                element.appendChild(mainTr);
            }
        }
        return element;
    }

    private Element createTH() {
        DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(start));
        DateNonConvertable toDate = new DateNonConvertable(DateUtil.resetTime(end));
        final FromToDate main = new FromToDate(fromDate, toDate);
        compareDates = parent != null ? parent.getCompareDates(fromDate != null
                ? fromDate.getDate()
                : null, parent.getCompareWithValues().getSelectedItem().getId()) : null;

        Element header = DOM.createTHead();
        header.setClassName("point_affix_top text-nowrap");
        Element tr = DOM.createTR();

        Element th = DOM.createTH();
        th.addClassName("stickerCell");
        th.addClassName(TEXT_LEFT);
        Element divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.getStyle().clearWidth();
        divElement.getStyle().setProperty("minWidth", "200px");
        divElement.setInnerHTML(NewProfitAndLoss.wfmStrings.accountName());
        th.appendChild(divElement);
        tr.appendChild(th);

        Element th1 = DOM.createTH();
        th1.addClassName("stickerCell");

        Element divElement1 = DOM.createDiv();
        divElement1.setClassName("frame_affix_top");
        if (showBudget) {
            divElement1.setInnerHTML(NewProfitAndLoss.wfmStrings.actual());
            divElement1.getStyle().clearWidth();
            divElement1.getStyle().setProperty("minWidth", "65px");
            th1.addClassName("stickerCell text-right");
            th1.appendChild(divElement1);
            tr.appendChild(th1);

            Element budgetTH = DOM.createTH();
            budgetTH.addClassName("stickerCell text-right");
            Element divElement3 = DOM.createDiv();
            divElement3.getStyle().setProperty("minWidth", "65px");
            divElement3.setClassName("frame_affix_top");
            divElement3.setInnerHTML(NewProfitAndLoss.wfmStrings.budget());
            budgetTH.appendChild(divElement3);
            tr.appendChild(budgetTH);

            Element varianceTH = DOM.createTH();
            varianceTH.addClassName("stickerCell text-right");
            Element divElement4 = DOM.createDiv();
            divElement4.getStyle().setProperty("minWidth", "65px");
            divElement4.setClassName("frame_affix_top");
            divElement4.setInnerHTML(NewProfitAndLoss.accountingStrings.variance());
            varianceTH.appendChild(divElement4);
            tr.appendChild(varianceTH);
        } else {
            divElement1.setInnerHTML(DateUtils.format(main.getTo()));
            divElement1.getStyle().clearWidth();
            divElement1.getStyle().setProperty("minWidth", "65px");
            th1.addClassName("stickerCell text-right");
            th1.appendChild(divElement1);
            for (int i = compareDates.length - 1; i >= 0; i--) {
                Element comparedTh = DOM.createTH();
                comparedTh.addClassName("stickerCell text-right");
                Element divElement2 = DOM.createDiv();
                divElement2.getStyle().setProperty("minWidth", "65px");
                divElement2.setClassName("frame_affix_top");
                divElement2.setInnerHTML(DateUtils.format(compareDates[i].getTo()));
                comparedTh.appendChild(divElement2);
                tr.appendChild(comparedTh);

            }

            tr.appendChild(th1);
        }
        header.appendChild(tr);
        return header;
    }

    private void createGroupHeader(Element element, AccountItemsByAccountType item) {
        Element tr = DOM.createTR();
        tr.addClassName("heading_row");
        Element td = DOM.createTD();
        shownObjects.add(item.getGroupName());

        Element icon = DOM.createElement("i");
        icon.addClassName("btn--circle plusMinus");
        DOM.sinkEvents(icon.cast(), Event.ONCLICK);
        DOM.sinkEvents(icon.cast(), Event.ONCLICK);
        DOM.setEventListener(icon.cast(), event -> {
            if (element.getClassName().contains("collapsed")) {
                element.removeClassName("collapsed");
                element.addClassName("expanded");
                shownObjects.add(item.getGroupName());
            } else {
                element.removeClassName("expanded");
                element.addClassName("collapsed");
                shownObjects.remove(item.getGroupName());
            }
        });

        Element nameElement = DOM.createElement("span");
        nameElement.setInnerHTML(item.getGroupName());
        td.appendChild(icon);
        td.appendChild(nameElement);

        td.setAttribute("colspan", String.valueOf(parent.getColumnCount() + (showBudget ? 1 : 3)));
        tr.appendChild(td);
        element.appendChild(tr);
    }

    private void createTotals(Element element, String groupName, ArrayList<BudgetInDate> bItems) {
        if (element != null) {
            Element tr = DOM.createTR();
            Element td = DOM.createTD();
            td.setInnerHTML(groupName);
            td.setAttribute("style", "border-right: none;");
            tr.appendChild(td);

            Element tr2 = DOM.createTR();
            tr2.addClassName("row-spacing java-profitTab1");
            Element td2 = DOM.createTD();
            tr2.appendChild(td2);

            if (!showBudget) {
                Collections.reverse(bItems);
            }
            for (BudgetInDate bItem : bItems) {
                Element td1 = DOM.createTD();
                td1.addClassName(TEXT_RIGHT);
                if (bItem.getValue() != null) {
                    td1.setInnerHTML(parent.getValueAsString(bItem.getValue(), false));
                }
                td1.setAttribute("style", "border-left: none;");
                tr.appendChild(td1);
                Element td12 = DOM.createTD();
                tr2.appendChild(td12);
            }
            if (showBudget && bItems.size() == 2) {
                Element td3 = DOM.createTD();
                td3.addClassName(TEXT_RIGHT);
                tr.appendChild(td3);

                Element td13 = DOM.createTD();
                td13.addClassName(TEXT_RIGHT);
                tr2.appendChild(td13);
            }
            element.appendChild(tr);
            element.appendChild(tr2);
        }
    }

    private void createGroupTotalRow(Element element, String groupName, List<BigDecimal> bItems, boolean isNegative) {
        if (element != null) {
            Element tr = DOM.createTR();
            tr.addClassName("total_row");
            Element td = DOM.createTD();
            td.setInnerHTML(groupName);
            td.setAttribute("style", "border-right: none;");
            tr.appendChild(td);

            for (BigDecimal bItem : bItems) {
                Element td1 = DOM.createTD();
                td1.addClassName(TEXT_RIGHT);
                if (bItem != null/* && bItem.compareTo(BigDecimal.ZERO) > 0*/) {
                    td1.setInnerHTML(parent.getValueAsString(bItem, isNegative));
                }
                td1.setAttribute("style", "border-left: none;");
                tr.appendChild(td1);
            }
            element.appendChild(tr);
        }
    }

    private void createTotalRow(Element element, String groupName, List<BudgetInDate> bItems, boolean isNegative) {
        if (element != null) {
            Element tr = DOM.createTR();
            tr.addClassName("total_row");
            Element td = DOM.createTD();
            td.setInnerHTML(groupName);
            td.setAttribute("style", "border-right: none;");
            tr.appendChild(td);

            Collections.reverse(bItems);
            for (BudgetInDate bItem : bItems) {
                Element td1 = DOM.createTD();
                td1.addClassName(TEXT_RIGHT);
                if (bItem.getValue() != null) {
                    td1.setInnerHTML(parent.getValueAsString(bItem.getValue(), isNegative));
                }
                td1.setAttribute("style", "border-left: none;");
                tr.appendChild(td1);
            }
            element.appendChild(tr);
        }
    }

    private void createHeader() {
        Element header = createTH();
        myTable.appendChild(header);
    }

    public HashSet<String> getShownObjects() {
        return shownObjects;
    }
}
