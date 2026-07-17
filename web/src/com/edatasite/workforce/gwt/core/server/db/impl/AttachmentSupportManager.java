package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.domain.HasAttachments;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class AttachmentSupportManager<E extends EdsObject> extends BaseManager<E> implements BeanFactoryAware {

    private UploadManager uploadManager;

    public AttachmentSupportManager(Class objectClass) {
        super(objectClass);
    }

    @Override
    public void create(E obj) {
        super.create(obj);
        if (obj instanceof HasAttachments hasAttachments) {
            for (EdsUpload upload : hasAttachments.getAttachments()) {
                if (upload.getInputStream() == null) {
                    throw new RuntimeException("Empty attachments not permitted!");
                }
                uploadManager.create(upload);
            }
        }
    }

    @Override
    public void update(E obj) {
        super.update(obj);
        if (obj instanceof HasAttachments hasAttachments) {
            for (EdsUpload upload : hasAttachments.getAttachments()) {
                uploadManager.update(upload);
            }
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        uploadManager = (UploadManager) beanFactory.getBean(UploadManager.COMPONENT_NAME);
    }

}
