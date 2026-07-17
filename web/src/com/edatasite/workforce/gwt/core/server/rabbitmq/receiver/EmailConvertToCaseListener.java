package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailFilter;
import com.edatasite.workforce.gwt.core.server.db.EmailFilterManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class EmailConvertToCaseListener extends BaseAmqpListener<String> {

    private static final Logger log = LoggerFactory.getLogger(EmailConvertToCaseListener.class);

    @Autowired
    private CrmServiceLocal crmService;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailFilterManager emailFilterManager;

    @Override
    protected void receiveMessage(String emailId) {
        Optional<EdsEmail> emailOptional = emailRepository.findById(emailId);
        if (!emailOptional.isPresent()) {
            return;
        }
        EdsEmail email = emailOptional.get();
        List<EdsEmailFilter> filters = emailFilterManager.getParentsOnly();

        EdsUser user = emailSettingsManager.get(email.getEmailSettingId()).getUser();
        if (user != null) {
            SecurityContext.getInstance().setStaticUserID(user.getObjectID());
        }

        Integer caseId = crmService.convertEmailToCase(email, filters);
        if (caseId != null) {
            log.info("Company:[" + SecurityContext.getInstance().getCompanyId() + "] email[" + emailId +"] converted successfully to case: " + caseId);
        }
    }

    @Override
    protected DataMQ<String> convertMessage(String message) {
        Gson gson = new Gson();
        DataMQ<String> rawData = gson.fromJson(message, new TypeToken<DataMQ<String>>() {
        }.getType());
        Optional<EdsEmail> emailOptional = emailRepository.findById(rawData.getDataMQ());
        String clusterType = rawData.getClusterType();
        if (emailOptional.isPresent() && rawData.getClusterType() == null) {
            EdsEmail email = emailOptional.get();
            clusterType = email.getClusterType();
        }
        DataMQ<String> finalData = new DataMQ<>();
        finalData.setDataMQ(rawData.getDataMQ());
        finalData.setCompanyId(rawData.getCompanyId());
        finalData.setClusterType(clusterType);

        return finalData;
    }
}
