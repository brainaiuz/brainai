package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.mail.IBaseJob;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EndOfYearServiceJob implements IBaseJob {
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private CompanyManager companyManager;

    @Override
    public void execute() {
        List<Integer> companyIds = companyManager.getReallyExistingCompanyIds();
        for (Integer companyId : companyIds) {
            rabbitMQService.endOfYearProcess(companyId, companyId);
        }
    }
}
