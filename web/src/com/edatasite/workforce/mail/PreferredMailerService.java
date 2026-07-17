package com.edatasite.workforce.mail;

import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.core.domain.EdsSignupMessage;
import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.MessageTypeEnum;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("unchecked")
public class PreferredMailerService extends BaseMailerService {
    private static final Logger log = LoggerFactory.getLogger(PreferredMailerService.class);
    private static boolean running = false;
    private static int LIMIT = 10;

    @Override
    public List<EdsSuperMessage> getMessages() {
        log.info("===Start select all PrefMail pending messages for sending===");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sm.*, 0 as clazz_ FROM \"public\".signup_message sm ")
                .append("WHERE sm.status = '").append(MessageStatusEnum.PENDING).append("' AND sm.toemail <> '' ")
                .append("AND sm.toemail IS NOT NULL AND sm.type = '").append(MessageTypeEnum.PREFERRED).append("' ")
                .append("ORDER BY sm.creationdate LIMIT ").append(LIMIT);
        List<EdsSuperMessage> messages = messageManager.findNative(sql.toString(), EdsSignupMessage.class);

        if ((LIMIT - messages.size()) > 0) {
            sql = new StringBuilder("SELECT m.*, 0 as clazz_ FROM \"public\".message m ")
                    .append("WHERE m.status='").append(MessageStatusEnum.PENDING).append("' AND m.toemail<>'' AND m.toemail IS NOT NULL ")
                    .append("AND m.type = '").append(MessageTypeEnum.PREFERRED).append("' ")
                    .append("ORDER BY m.creationdate LIMIT ").append(LIMIT - messages.size());
            messages.addAll(messageManager.findNative(sql.toString(), EdsMessage.class));
        }

        return messages;
    }

    @Transactional
    public void execute() {
        if (running) return;
        running = true;
        log.info("====== P R E F M A I L E R S E R V I C E START ====");
        super.execute();
        running = false;
        log.info("====== P R E F M A I L E R S E R V I C E END ======");
    }
}
