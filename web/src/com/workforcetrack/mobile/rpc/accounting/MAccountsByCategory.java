package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountsByCategory;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.workforcetrack.mobile.rpc.expense.MAccountItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 23.06.2011
 * Time: 12:18:19
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "accountsByCategory")
public class MAccountsByCategory {

    private List<MAccountItem> assets;
    private List<MAccountItem> liabilities;
    private List<MAccountItem> equity;
    private List<MAccountItem> revenue;
    private List<MAccountItem> overhead;
    private List<MAccountItem> expenses;

    private Integer revenueProductSalesCode;

    private Integer expensesMaterialsPurchased;

    public MAccountsByCategory() {
    }

    public MAccountsByCategory(AccountsByCategory accountsByCategory) {
        if (accountsByCategory != null) {
            assets = new ArrayList<>();
            for (AccountItem accountItem : accountsByCategory.getAssets()) {
                this.assets.add(new MAccountItem(accountItem));
            }
            liabilities = new ArrayList<>();
            for (AccountItem accountItem : accountsByCategory.getLiabilities()) {
                this.liabilities.add(new MAccountItem(accountItem));
            }
            equity = new ArrayList<>();
            for (AccountItem accountItem : accountsByCategory.getEquity()) {
                this.equity.add(new MAccountItem(accountItem));
            }
            revenue = new ArrayList<>();
            for (AccountItem accountItem : accountsByCategory.getRevenue()) {
                this.revenue.add(new MAccountItem(accountItem));
            }
            overhead = new ArrayList<>();
            for (AccountItem accountItem : accountsByCategory.getOverhead()) {
                this.overhead.add(new MAccountItem(accountItem));
            }
            expenses = new ArrayList<>();
            for (AccountItem accountItem : accountsByCategory.getExpenses()) {
                this.expenses.add(new MAccountItem(accountItem));
            }
            this.revenueProductSalesCode = accountsByCategory.getRevenueProductSalesCode();
            this.expensesMaterialsPurchased = accountsByCategory.getExpensesMaterialsPurchased();

        }
    }

    public static AccountsByCategory convert(MAccountsByCategory accounts) {
        AccountsByCategory accountsByCategory = new AccountsByCategory();
        accountsByCategory.setRevenueProductSalesCode(accounts.getRevenueProductSalesCode());
        accountsByCategory.setExpensesMaterialsPurchased(accounts.getExpensesMaterialsPurchased());

        List<AccountItem> assets = new ArrayList<>();
        for (MAccountItem mAccountItem : accounts.getAssets()) {
            assets.add(mAccountItem.convertToAccountItem(null));
        }
        accountsByCategory.setAssets(assets.toArray(new AccountItem[0]));

        List<AccountItem> liabilities = new ArrayList<>();
        for (MAccountItem mAccountItem : accounts.getAssets()) {

            liabilities.add(mAccountItem.convertToAccountItem(null));
        }
        accountsByCategory.setLiabilities(liabilities.toArray(new AccountItem[0]));

        List<AccountItem> equity = new ArrayList<>();
        for (MAccountItem mAccountItem : accounts.getEquity()) {
            equity.add(mAccountItem.convertToAccountItem(null));
        }
        accountsByCategory.setEquity(equity.toArray(new AccountItem[0]));

        List<AccountItem> revenue = new ArrayList<>();
        for (MAccountItem mAccountItem : accounts.getRevenue()) {
            revenue.add(mAccountItem.convertToAccountItem(null));
        }
        accountsByCategory.setRevenue(revenue.toArray(new AccountItem[0]));

        List<AccountItem> overhead = new ArrayList<>();
        for (MAccountItem mAccountItem : accounts.getOverhead()) {
            overhead.add(mAccountItem.convertToAccountItem(null));
        }
        accountsByCategory.setOverhead(overhead.toArray(new AccountItem[0]));

        List<AccountItem> expenses = new ArrayList<>();
        for (MAccountItem mAccountItem : accounts.getExpenses()) {
            expenses.add(mAccountItem.convertToAccountItem(null));
        }
        accountsByCategory.setExpenses(expenses.toArray(new AccountItem[0]));

        return accountsByCategory;
    }

    public List<MAccountItem> getAssets() {
        return assets;
    }

    public void setAssets(List<MAccountItem> assets) {
        this.assets = assets;
    }

    public List<MAccountItem> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(List<MAccountItem> liabilities) {
        this.liabilities = liabilities;
    }

    public List<MAccountItem> getEquity() {
        return equity;
    }

    public void setEquity(List<MAccountItem> equity) {
        this.equity = equity;
    }

    public List<MAccountItem> getRevenue() {
        return revenue;
    }

    public void setRevenue(List<MAccountItem> revenue) {
        this.revenue = revenue;
    }

    public List<MAccountItem> getOverhead() {
        return overhead;
    }

    public void setOverhead(List<MAccountItem> overhead) {
        this.overhead = overhead;
    }

    public List<MAccountItem> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<MAccountItem> expenses) {
        this.expenses = expenses;
    }

    public Integer getRevenueProductSalesCode() {
        return revenueProductSalesCode;
    }

    public void setRevenueProductSalesCode(Integer revenueProductSalesCode) {
        this.revenueProductSalesCode = revenueProductSalesCode;
    }

    public Integer getExpensesMaterialsPurchased() {
        return expensesMaterialsPurchased;
    }

    public void setExpensesMaterialsPurchased(Integer expensesMaterialsPurchased) {
        this.expensesMaterialsPurchased = expensesMaterialsPurchased;
    }
}