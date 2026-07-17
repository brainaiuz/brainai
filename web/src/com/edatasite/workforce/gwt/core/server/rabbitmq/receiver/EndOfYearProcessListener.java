package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EndOfYearProcessListener extends BaseAmqpListener<Integer> {
    private static final Logger log = LoggerFactory.getLogger(EndOfYearProcessListener.class);
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;

    @Override
    public void receiveMessage(Integer companyId) {
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        Calendar calendar = new GregorianCalendar();
        availabilityServiceLocal.copyLastYearLeaveAllowanceMinutes(calendar.get(Calendar.YEAR));
    }

    @Override
    protected DataMQ<Integer> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<Integer>>() {
        }.getType());
    }
}
