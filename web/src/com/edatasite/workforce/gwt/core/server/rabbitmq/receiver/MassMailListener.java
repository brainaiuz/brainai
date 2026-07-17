package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.shared.mail.Upload;
import com.edatasite.shared.massmailler.MassMailerData;
import com.edatasite.shared.massmailler.MassSpamSender;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.MassMailServiceLocal;
import com.edatasite.workforce.utils.InputStreamDataSource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.activation.DataHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.edatasite.shared.massmailler.MassSpamSender.EMAIL;

public class MassMailListener extends BaseAmqpListener<MassMailerData> {
    private static final Logger log = LoggerFactory.getLogger(MassMailListener.class);
    @Autowired
    FileHeaderManager fileHeaderManager;
    @Autowired
    MassMailServiceLocal massMailServiceLocal;
    @Autowired
    UploadManager uploadManager;

    @Override
    protected void receiveMessage(MassMailerData massMailerData) {
        long start = System.currentTimeMillis();
        if (EMAIL.equals(massMailerData.getSendType())) {
            List<Upload> attachments = new ArrayList<>();
            StringBuilder ids = new StringBuilder();
            if (massMailerData.getBody().getFileHeaderIds() != null) {
                for (Integer id : massMailerData.getBody().getFileHeaderIds()) {
                    ids.append(id).append(",");
                }
            }
            if (ids.length() > 0) {
                List<EdsFileHeader> fileIdsIn = fileHeaderManager.getFileIdsIn(ids.deleteCharAt(ids.length() - 1).toString());
                for (EdsFileHeader fileHeader : fileIdsIn) {
                    try {
                        attachments.add(getFileRPCs(fileHeader));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            massMailerData.getBody().setFiles(attachments);
        }
        MassSpamSender massSpamSender = new MassSpamSender(massMailerData);
        MessageStatusEnum status = massSpamSender.sendSync();

        massMailServiceLocal.updateSentEntityMessageStatus(massMailerData.getCrmEntityBody().getCompanyID(), massMailerData.getCrmEntityBody().getMsgId(), massMailerData.getCrmEntityBody().getEntityID(), status);

        log.info("MassMailer sent cid=" + SecurityContext.getInstance().getCompanyId() + "; " + SecurityContext.getInstance().getDatabase()
                + "; msgId=" + massMailerData.getCrmEntityBody().getMsgId()
                + "; entityId=" + massMailerData.getCrmEntityBody().getEntityID()
                + "; status=" + status
                + "; time=" + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    protected DataMQ<MassMailerData> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<MassMailerData>>() {
        }.getType());
    }

    private Upload getFileRPCs(EdsFileHeader fileHeader) throws IOException {
        EdsFileBody fileBody = fileHeader.getCurrentBody();
        Upload uploadItem = new Upload();
        InputStream inputStream = uploadManager.getInputStream(fileBody);
        uploadItem.setFileName(fileBody.getOriginalName());
        uploadItem.setContentType(fileBody.getContentType());
        uploadItem.setDataHandler(new DataHandler(new InputStreamDataSource(fileBody.getOriginalName(), fileBody.getContentType(), inputStream)));
        return uploadItem;
    }
}
