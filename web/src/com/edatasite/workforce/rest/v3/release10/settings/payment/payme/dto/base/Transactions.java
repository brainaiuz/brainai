package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base;

import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.GetStatementResult;

import java.util.List;

public class Transactions {
    private List<GetStatementResult> transactions;

    public Transactions(List<GetStatementResult> transactions) {
        this.transactions = transactions;
    }

    public List<GetStatementResult> getTransactions() {
        return transactions;
    }
}
