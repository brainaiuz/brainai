package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.core.domain.EdsWorkflowMessage;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Azazello on 6/24/2017.
 */
@SuppressWarnings("unchecked")
public class WorkflowMailerService extends BaseMailerService {
    private static final Logger log = LoggerFactory.getLogger(WorkflowMailerService.class);
    private static boolean running = false;

    @Override
    public List<EdsSuperMessage> getMessages() {
        String limit = SpringPropertiesUtil.getProperty("email.limit");
        log.info("===Start select all WorkflowMail pending messages for sending===");
        return messageManager.findNative("SELECT m.*, 0 as clazz_ FROM \"public\".workflow_message m " +
                "WHERE m.status='" + MessageStatusEnum.PENDING + "' AND m.toemail<>'' AND m.toemail IS NOT NULL ORDER BY m.creationdate limit " + (limit != null ? limit : 50), EdsWorkflowMessage.class);
    }

    @Transactional
    public void execute() {
        if (running)
            return;
        running = true;
        log.info("====== W O R K F L O W M A I L E R S E R V I C E START ====");
        super.execute();
        running = false;
        log.info("====== W O R K F L O W M A I L E R S E R V I C E END ======");
    }
}
