package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsContractCoursePrice;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTrainingContract;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SmsSenderServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.ContractCoursePriceManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseBookingManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TrainingContractManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentAsInvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/22/12
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CourseBookingEventListenerImpl extends CustomBusinessEventListenerAdapter implements Constants {

    public static WfmType<EdsCourseBooking> TYPE = new WfmType<>(EventTypes.courseBookingEventListener);
    public static WfmType<EdsCourseScheduleStudent> TYPE_STUDENT_COURSE_SCHEDULE = new WfmType<>("studentCourseScheduleEventListenerString");

    public static final String EVENT_APPROVE_BOOKING = "EVENT_APPROVE_BOOKING";
    public static final String EVENT_REJECT_BOOKING = "EVENT_REJECT_BOOKING";
    public static final String EVENT_SUBMIT_FOR_APPROVAL = "EVENT_SUBMIT_FOR_APPROVAL";
    public static final String EVENT_STUDENT_COURSE_SCHEDULE_ATTENDED = "EVENT_STUDENT_COURSE_SCHEDULE_ATTENDED";
    public static final String EVENT_STUDENT_CANCEL_FROM_COURSE_SCHEDULE = "EVENT_STUDENT_CANCEL_FROM_COURSE_SCHEDULE";
    public static final String EVENT_UPDATE_STUDENT_AND_SCHEDULE_INVOICES = "EVENT_UPDATE_STUDENT_AND_SCHEDULE_INVOICES";

    @Autowired
    private CourseBookingManager courseBookingManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;
    @Autowired
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private SmsSenderServiceLocal smsSenderServiceLocal;
    @Autowired
    private TrainingContractManager trainingContractManager;
    @Autowired
    private ContractCoursePriceManager contractCoursePriceManager;
    @Autowired
    private ReferenceManager referenceManager;

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_APPROVE_BOOKING.equals(event.getEventType())) {
            onApproveBooking(event);
        } else if (EVENT_REJECT_BOOKING.equals(event.getEventType())) {
            onRejectBooking(event);
        } else if (EVENT_SUBMIT_FOR_APPROVAL.equals(event.getEventType())) {
            onSubmitForApproval(event);
        } else if (EVENT_STUDENT_COURSE_SCHEDULE_ATTENDED.equals(event.getEventType())) { //Student ReSchedule functional
            onCourseScheduleAttended(event);
        } else if (EVENT_STUDENT_CANCEL_FROM_COURSE_SCHEDULE.equals(event.getEventType())) {
            onCancelStudent(event);
        } else if (EVENT_UPDATE_STUDENT_AND_SCHEDULE_INVOICES.equals(event.getEventType())) {
            onUpdateStudentAndScheduleInvoices(event);
        }
    }

    private void onCourseScheduleAttended(EdsBusinessEvent event) {
        EdsCourseScheduleStudent courseScheduleStudent = courseScheduleStudentManager.get(event.getEntityID());
        if (courseScheduleStudent != null) {
            messageManager.sendToStudentsForScheduleCourseConfirm(courseScheduleStudent, event.getSourceID());
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void onCancelStudent(EdsBusinessEvent event) {
        EdsCourseScheduleStudent courseScheduleStudent = courseScheduleStudentManager.get(event.getEntityID());

        if (courseScheduleStudent != null) {
            List<EdsCourseScheduleStudent> courseScheduleStudentList = new ArrayList<>();
            courseScheduleStudentList.add(courseScheduleStudent);
            smsSenderServiceLocal.smsToQueue(emailTemplateServiceLocal.generateSmsMessagesForCourseBooking(courseScheduleStudentList, "Course rejection"));
            messageManager.sendToStudentsForCourseBookingConfirm(courseScheduleStudentList, event.getSourceID(), "KGOnline Course Rejection");
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void onApproveBooking(EdsBusinessEvent event) {
        EdsCourseBooking courseBooking = courseBookingManager.get(event.getEntityID());
        List<EdsCourseScheduleStudent> courseScheduleStudentList = courseScheduleStudentManager.getCourseScheduleStudentByStatus(courseBooking.getObjectID(), EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_ATTENDED);
        if (courseScheduleStudentList != null && !courseScheduleStudentList.isEmpty()) {
            setStudentPrices(courseScheduleStudentList);
            EdsReference type = referenceManager.findReference(COURSE_BOOKING_TYPE, BOOKING_PAY_UPON_ARRIVAL);
            if (courseBooking.getType().equals(type)) {
                List<StudentAsInvoiceItem> items = new ArrayList<>();
                for (EdsCourseScheduleStudent student : courseScheduleStudentList) {
                    items.add(student.createStudentAsInvoiceItem(student));
                }

                Integer invoiceID = invoiceServiceLocal.createDailyInvoiceForCustomerByCustomerStaff(courseBooking.getCreationDate(), courseBooking.getCustomer().getObjectID(), items, null, event.getSourceID(),true);
                courseBooking.setInvoiceID(invoiceID);

                for (EdsCourseScheduleStudent student : courseScheduleStudentList) {
                    student.setInvoiceID(invoiceID);
                }
            }
            smsSenderServiceLocal.smsToQueue(emailTemplateServiceLocal.generateSmsMessagesForCourseBooking(courseScheduleStudentList, "Course confirmation"));
            messageManager.sendToStudentsForCourseBookingConfirm(courseScheduleStudentList, event.getSourceID(), "KGOnline Course Confirmation");
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void setStudentPrices(List<EdsCourseScheduleStudent> courseScheduleStudentList) {
        for (EdsCourseScheduleStudent student : courseScheduleStudentList) {
            List<EdsTrainingContract> contracts = trainingContractManager.getKeyClientList(student.getCourseBooking().getCustomer(), student.getCourseBooking().getCreationDate());
            if (contracts != null && contracts.size() > 0) {
                for (EdsTrainingContract contract : contracts) {
                    EdsContractCoursePrice contractPrices = contractCoursePriceManager.getCoursePricesForContractByLocation(contract.getObjectID(), student.getCourseScheduleBooking().getCourse().getObjectID(), student.getCourseBooking().getLocation().getObjectID());
                    System.out.println("Contract Prices for " + student.getCourseScheduleBooking().getNumber() + ", Contract = " + contractPrices);
                    if (contractPrices != null) {
                        System.out.println("Price = " + contractPrices.getPrice() + " AND StopFee = " + contractPrices.getStopFee());
                        student.setPrice(contractPrices.getPrice());
                        student.setStopFee(contractPrices.getStopFee());
                    } else {
                        student.setPrice(student.getCourseScheduleBooking().getPrice());
                        student.setStopFee(student.getCourseScheduleBooking().getStopFee());
                    }
                }
            } else {
                student.setPrice(student.getCourseScheduleBooking().getPrice());
                student.setStopFee(student.getCourseScheduleBooking().getStopFee());
            }
        }
    }

    private void onRejectBooking(EdsBusinessEvent event) {
        EdsCourseBooking courseBooking = courseBookingManager.get(event.getEntityID());
        if (event.getCustomStringField() != null && BOOKING_APPROVED.equals(event.getCustomStringField())) {
            List<EdsCourseScheduleStudent> courseScheduleStudentList = courseScheduleStudentManager.getCourseScheduleStudentByStatus(courseBooking.getObjectID(), EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED);

            if (courseScheduleStudentList != null && !courseScheduleStudentList.isEmpty()) {
                smsSenderServiceLocal.smsToQueue(emailTemplateServiceLocal.generateSmsMessagesForCourseBooking(courseScheduleStudentList, "Course rejection"));
                messageManager.sendToStudentsForCourseBookingConfirm(courseScheduleStudentList, event.getSourceID(), "KGOnline Course Rejection");
                event.setStatus(EventStatus.COMPLETED.name());
            }
        }
    }

    private void onSubmitForApproval(EdsBusinessEvent event) {
        EdsCourseBooking courseBooking = courseBookingManager.get(event.getEntityID());
        String managerEmail = event.getCustomStringField();
        messageManager.sendToManagerForCourseBookingConfirm(courseBooking, event.getSourceID(), managerEmail);
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void onUpdateStudentAndScheduleInvoices(EdsBusinessEvent event) {
        try {
            String query = event.getCustomStringField();

            javax.persistence.EntityManager em = courseBookingManager.getJpaTemplate().createHibernateEntityManager();
            try (org.hibernate.Session session = (org.hibernate.Session) em.getDelegate()) {
                session.beginTransaction();
                Query queryObject = em.createNativeQuery(query);
                queryObject.executeUpdate();
                event.setStatus(EventStatus.COMPLETED.name());
                session.getTransaction().commit();
            } finally {
                em.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
