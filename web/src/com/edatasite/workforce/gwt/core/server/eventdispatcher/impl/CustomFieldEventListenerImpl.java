package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Mirjalol
 * Date: 06.08.13
 * Time: 16:12
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CustomFieldEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsCompanyCustomFieldsSettings> TYPE = new WfmType<>(EventTypes.customFieldEventListener);

    @Autowired
    private UserManager userManager;

    @Autowired
    private ModelManager modelManager;

    @Autowired
    private ModelFieldManager modelFieldManager;

    @Autowired
    private MyUpdateManager myUpdateManager;

    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;

    @Qualifier("companyCFSettingsManager")
    @Autowired
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    private CustomFormSectionManager customFormSectionManager;

    @Autowired
    private PdfTemplateTableSettingsManager pdfTemplateTableSettingsManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsUser creator = userManager.get(event.getSourceID());
        EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFManager.get(event.getEntityID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCustomFieldAddUpdate(customFieldsSettings, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerCustomFieldEditUpdate(customFieldsSettings, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (customFieldsSettings != null) {
            if (!event.isMyUpdatesItemEdit()) {
                try {
                    EdsMyUpdate myUpdate = myUpdateManager.registerCustomFieldDeleteUpdate(event.getEntityID(), event.getCustomStringField(), creator, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                    event.setMyUpdatesItemEdit(true);
                    companyCFManager.deleteCustomFieldValidations(customFieldsSettings.getObjectID());
                    customFieldsSettings.setDeleted(true);
                    companyCFManager.update(customFieldsSettings);
                } catch (Exception ex) {
                    event.setMyUpdatesItemEdit(false);
                    ex.printStackTrace();
                }
            }
            if (event.isMyUpdatesItemEdit()) {
                event.setStatus(EventStatus.COMPLETED.name());
            }
        }
    }


}
