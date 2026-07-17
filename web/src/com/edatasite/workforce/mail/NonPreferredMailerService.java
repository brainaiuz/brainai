package com.edatasite.workforce.mail;

import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageTypeEnum;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("unchecked")
public class NonPreferredMailerService extends BaseMailerService {
    private static final Logger log = LoggerFactory.getLogger(NonPreferredMailerService.class);
    private static boolean running = false;

    @Override
    public List<EdsSuperMessage> getMessages() {
        log.info("===Start select all NonPrefMail pending messages for sending===");
        return messageManager.findNative("SELECT m.*, 0 as clazz_ FROM \"public\".message m " +
                "WHERE m.status='" + MessageStatusEnum.PENDING + "' AND m.toemail<>'' AND m.toemail IS NOT NULL AND " +
                "m.type = '" + MessageTypeEnum.NON_PREFERRED + "' ORDER BY m.creationdate limit 10", EdsMessage.class);
    }

    @Transactional
    public void execute() {
        if (running) return;
        running = true;
        log.info("====== N O N P R E F M A I L E R S E R V I C E START ====");
        super.execute();
        running = false;
        log.info("====== N O N P R E F M A I L E R S E R V I C E END ======");
    }
}
