package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsInvoiceGeneratorSchedule;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Normurod on 9/8/15.
 */
@Transactional
public class CourseScheduleCustomEventListenerImpl extends CustomBusinessEventListenerAdapter implements Constants {

    public static WfmType<EdsCourseSchedule> TYPE = new WfmType<>(EventTypes.courseScheduleEventListener);
    public static WfmType<EdsInvoiceGeneratorSchedule> TYPE_INVOICE_GENERATOR_SCHEDULE = new WfmType<>("invoiceGeneratorScheduleEventListenerString");

    public static final String EVENT_GENERATE_INVOICE = "GENERATE_INVOICE";
    public static final String EVENT_REGENERATE_INVOICE = "REGENERATE_INVOICE";

    @Autowired
    private TCService tcService;

    @Autowired
    private ScheduledCourseManager scheduledCourseManager;
    @Autowired
    private UserManager userManager;


    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        EdsUser user = userManager.get(event.getSourceID());

        ServerSecurityContext.getInstance().setStaticUserID(user.getObjectID());

        if (EVENT_GENERATE_INVOICE.equals(event.getEventType())) {
            onGenerateInvoice(event);
        } else if (EVENT_REGENERATE_INVOICE.equals(event.getEventType())) {
            onRegenerateInvoice(event);
        }

        ServerSecurityContext.getInstance().setStaticUserID(null);
    }

    private void onGenerateInvoice(EdsBusinessEvent event) {
        EdsInvoiceGeneratorSchedule invoiceGeneratorSchedule = scheduledCourseManager.getInvoiceGeneratorSchedule(event.getEntityID());
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(new Date(invoiceGeneratorSchedule.getStartDate().getTime()));
        fp.setEndDate(new Date(invoiceGeneratorSchedule.getEndDate().getTime()));
        fp.setCaseID(invoiceGeneratorSchedule.getObjectID());
        tcService.generateInvoices(fp);
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void onRegenerateInvoice(EdsBusinessEvent event) {
        EdsInvoiceGeneratorSchedule invoiceGeneratorSchedule = scheduledCourseManager.getInvoiceGeneratorSchedule(event.getEntityID());
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(new Date(invoiceGeneratorSchedule.getStartDate().getTime()));
        fp.setEndDate(new Date(invoiceGeneratorSchedule.getEndDate().getTime()));
        fp.setCaseID(invoiceGeneratorSchedule.getObjectID());
        tcService.reGenerateInvoices(fp);
        event.setStatus(EventStatus.COMPLETED.name());
    }
}
