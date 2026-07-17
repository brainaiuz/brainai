package com.edatasite.workforce.rest.v2.release10.core.to.accounting.chartofaccount;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.AccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class AccountResponseData extends ResponseData {
    private ArrayList<AccountTO> assets;
    private ArrayList<AccountTO> liabilities;
    private ArrayList<AccountTO> equity;
    private ArrayList<AccountTO> revenue;
    private ArrayList<AccountTO> overhead;
    private ArrayList<AccountTO> expenses;

    public AccountResponseData() {
    }

    public AccountResponseData(ArrayList<AccountTO> assets, ArrayList<AccountTO> liabilities, ArrayList<AccountTO> equity, ArrayList<AccountTO> revenue, ArrayList<AccountTO> overhead, ArrayList<AccountTO> expenses) {
        this.assets = assets;
        this.liabilities = liabilities;
        this.equity = equity;
        this.revenue = revenue;
        this.overhead = overhead;
        this.expenses = expenses;
    }

    public ArrayList<AccountTO> getAssets() {
        return assets;
    }

    public void setAssets(ArrayList<AccountTO> assets) {
        this.assets = assets;
    }

    public ArrayList<AccountTO> getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(ArrayList<AccountTO> liabilities) {
        this.liabilities = liabilities;
    }

    public ArrayList<AccountTO> getEquity() {
        return equity;
    }

    public void setEquity(ArrayList<AccountTO> equity) {
        this.equity = equity;
    }

    public ArrayList<AccountTO> getRevenue() {
        return revenue;
    }

    public void setRevenue(ArrayList<AccountTO> revenue) {
        this.revenue = revenue;
    }

    public ArrayList<AccountTO> getOverhead() {
        return overhead;
    }

    public void setOverhead(ArrayList<AccountTO> overhead) {
        this.overhead = overhead;
    }

    public ArrayList<AccountTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<AccountTO> expenses) {
        this.expenses = expenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountResponseData)) return false;

        AccountResponseData that = (AccountResponseData) o;

        if (assets != null ? !assets.equals(that.assets) : that.assets != null) return false;
        if (liabilities != null ? !liabilities.equals(that.liabilities) : that.liabilities != null) return false;
        if (equity != null ? !equity.equals(that.equity) : that.equity != null) return false;
        if (revenue != null ? !revenue.equals(that.revenue) : that.revenue != null) return false;
        if (overhead != null ? !overhead.equals(that.overhead) : that.overhead != null) return false;
        if (expenses != null ? !expenses.equals(that.expenses) : that.expenses != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = assets != null ? assets.hashCode() : 0;
        result = 31 * result + (liabilities != null ? liabilities.hashCode() : 0);
        result = 31 * result + (equity != null ? equity.hashCode() : 0);
        result = 31 * result + (revenue != null ? revenue.hashCode() : 0);
        result = 31 * result + (overhead != null ? overhead.hashCode() : 0);
        result = 31 * result + (expenses != null ? expenses.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountResponseData{" +
                "assets=" + assets +
                ", liabilities=" + liabilities +
                ", equity=" + equity +
                ", revenue=" + revenue +
                ", overhead=" + overhead +
                ", expenses=" + expenses +
                '}';
    }
}
