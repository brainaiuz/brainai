package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class SinglePayslipListener extends BaseAmqpListener<PayslipItemFilter> {

    private static final Logger log = LoggerFactory.getLogger(SinglePayslipListener.class);

    @Autowired
    private PayrollServiceLocal payrollServiceLocal;

    @Override
    protected void receiveMessage(PayslipItemFilter itemFilter) {
        long millis = new Date().getTime();
        log.info("SinglePayslipListener has started. employeeId = {}", itemFilter.getEmployeeID());

        payrollServiceLocal.createSinglePayrun(itemFilter);

        log.info("SinglePayslipListener has finished, duration {}ms", millis - new Date().getTime());
    }

    @Override
    protected DataMQ<PayslipItemFilter> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<PayslipItemFilter>>() {
        }.getType());
    }
}
