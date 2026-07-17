package com.edatasite.workforce.gwt.trainingcenter.server;

import au.com.bytecode.opencsv.CSVReader;
import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsBookingItem;
import com.edatasite.workforce.core.domain.EdsBookingItemReservation;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCourseBookingCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCourseCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCourseSubjectCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsScheduledCourseCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsStudentCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsTrainingContractCustomFields;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificate;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificateItem;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificateType;
import com.edatasite.workforce.core.domain.trainingcenter.EdsContractCoursePrice;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCoursePrice;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseScheduleStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSubject;
import com.edatasite.workforce.core.domain.trainingcenter.EdsInstructorScheduledCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsInvoiceGeneratorSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsPassport;
import com.edatasite.workforce.core.domain.trainingcenter.EdsPassportCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsSeatTemporaryLock;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudentAttended;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCQuestionaire;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCResponse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTask;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTaskItem;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCStudentQuestionaire;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTrainingContract;
import com.edatasite.workforce.core.domain.trainingcenter.InvoiceGeneratorStatus;
import com.edatasite.workforce.core.solr.component.CourseScheduleSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.core.solr.document.CourseScheduleSolrDoc;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ClientContact;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseBookingRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseScheduleRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.commons.MastercardPaymentHandler;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.BookingItemManager;
import com.edatasite.workforce.gwt.core.server.db.BookingItemReservationManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ContractCoursePriceManager;
import com.edatasite.workforce.gwt.core.server.db.CourseManager;
import com.edatasite.workforce.gwt.core.server.db.CoursePriceManager;
import com.edatasite.workforce.gwt.core.server.db.CourseSubjectManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InstructorScheduleCourseManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceJobManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.StudentAttendedManager;
import com.edatasite.workforce.gwt.core.server.db.StudentManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotItemManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CourseBookingCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CourseCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CourseSubjectCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ScheduledCourseCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.StudentCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.TrainingContractCFManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CertificateManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CertificateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseBookingManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.PassportCourseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.PassportManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.QuestionaireManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ResponseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.StudentQuestionaireManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TCScheduledTaskItemManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TCScheduledTaskManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TemporaryLockManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TrainingContractManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.RecurringBgCourseScheduleListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CourseBookingEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CourseScheduleCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.servlets.eml.CreateZipFile;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.AssessmentItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.ContractCoursePriceItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseSubjectItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.QuestionarieResponseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TrainingContractItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.attendencesheet.InstructorItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.attendencesheet.InstructorStudentItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateItemData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateTypeData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.AddEditCourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseLanguageListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseScheduleListItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.passport.PassportData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.CourseRequirementItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.InstructorScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseReservation;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.TimeSlotItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentAsInvoiceItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.edatasite.workforce.gwt.trainingcenter.server.jaxb.Assignment;
import com.edatasite.workforce.gwt.trainingcenter.server.jaxb.ParentClass;
import com.edatasite.workforce.gwt.trainingcenter.server.jaxb.Response;
import com.edatasite.workforce.gwt.trainingcenter.server.jaxb.Results;
import com.edatasite.workforce.gwt.trainingcenter.server.jaxb.ResultsList;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRING_COURSE_SCHEDULE;

/**
 * User: Normurod
 * Date: 7/16/12
 * Time: 9:26 PM
 */

@Transactional
@Service("tcService")
public class TCServiceImpl implements TCService, TCServiceLocal, Constants, TCConstants {

    private static final Logger log = LoggerFactory.getLogger(TCServiceImpl.class);
    @Autowired
    private CourseManager courseManager;
    @Autowired
    private TrainingContractManager trainingContractManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private StudentManager studentManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    @Qualifier("contactService")
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ScheduledCourseManager scheduledCourseManager;
    @Autowired
    private CourseScheduleSolrComponent courseScheduleSolrComponent;
    @Autowired
    private LocationManager locationManager;

    @Autowired
    private SolrManager solrManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private InstructorScheduleCourseManager instructorScheduleCourseManager;
    @Autowired
    private StudentAttendedManager studentAttendedManager;
    @Autowired
    private BookingItemReservationManager reservationManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private BookingItemManager bookingItemManager;
    @Autowired
    private CourseBookingManager courseBookingManager;
    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;
    @Autowired
    private TemporaryLockManager temporaryLockManager;
    @Autowired
    @Qualifier("googleCalendarService")
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;
    @Autowired
    private CertificateManager certificateManager;
    @Autowired
    private CertificateTypeManager certificateTypeManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private CoursePriceManager coursePriceManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private CRMService crmService;
    @Autowired
    private QuestionaireManager questionaireManager;
    @Autowired
    private StudentQuestionaireManager studentQuestionaireManager;
    @Autowired
    private ResponseManager responseManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    @Qualifier("invoiceService")
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private TCScheduledTaskManager tcScheduledTaskManager;
    @Autowired
    private TCScheduledTaskItemManager tcScheduledTaskItemManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private CourseSubjectManager courseSubjectManager;
    @Autowired
    private ContractCoursePriceManager contractCoursePriceManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private PassportCourseManager passportCourseManager;
    @Autowired
    private TimeSlotItemManager timeSlotItemManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private ScheduledCourseCFManager scheduledCourseCFManager;
    @Autowired
    private StudentCFManager studentCFManager;
    @Autowired
    private CourseSubjectCFManager courseSubjectCFManager;
    @Autowired
    private TrainingContractCFManager trainingContractCFManager;
    @Autowired
    private CourseCFManager courseCFManager;
    @Autowired
    private CourseBookingCFManager courseBookingCFManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private RecurrenceJobManager recurrenceJobManager;
    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;

    @Autowired
    @Qualifier("savedSaleInvoceViewPDFHandler")
    private IPostPDFHandler savedSaleInvoiceViewPDFHandler;
    @Autowired
    @Qualifier("tcScheduledInvoicePDFHandler")
    private IPostPDFHandler tcScheduledInvoicePDFHandler;
    @Autowired
    @Qualifier("tcConsolidatedInvoicesPDFHandler")
    private IPostPDFHandler tcConsolidatedInvoicesPDFHandler;

    private static final String INSTRUCTOR = "INSTRUCTOR";
    private static final String ASSESSOR = "ASSESSOR";
    public static final DecimalFormat decimalFormat = new DecimalFormat("0000");

    /**
     * Register new or existing student
     *
     * @param studentItem - studentItem
     */
    @Transactional
    public Integer saveStudent(StudentItem studentItem) {
        return saveStudentItem(studentItem);
    }

    private Integer saveStudentItem(StudentItem studentItem) {
        //register save method
        EdsStudent student = new EdsStudent();
        if (studentItem.getObjectId() != null) {
            student = studentManager.get(studentItem.getObjectId());
        }
        if (studentItem.getCustomerID() != null/* && studentItem.getObjectId() == null*/) {
            EdsCrmAccount customer = crmAccountManager.get(studentItem.getCustomerID());
            student.setCustomer(customer);
        }
        if (studentItem.getCardTypeID() != null) {
            student.setIDCard(referenceManager.get(studentItem.getCardTypeID()));
        }
        student.setCompEmplNumber(studentItem.getCompEmpNum());
        student.setDepartmentCode(studentItem.getDepartmentCode());
        student.setIDCardNumber(studentItem.getCardNumber());
        student.setSafetyPPNumber(studentItem.getSafetyPPNumber());
        student.setNationality(studentItem.getNationality());
        student.setActive(studentItem.isActive());
        student.setGender(studentItem.getGender());
        student.setCustomFields(createStudentCustomFields(student.getCustomFields(), studentItem.getCustomFields()));

        studentManager.createOrUpdate(student);
        student.setNumber(decimalFormat.format(student.getObjectID()));

        //create student contact logic
        studentItem.setContactType(ContactListItem.STUDENT_CONTACT);

        if (student.getContact() != null) {
            studentItem.setObjectId(student.getContact().getObjectID());
        }
        Integer contactID = contactServiceLocal.saveContact(studentItem, null, studentManager.getUser(), false, true);
        if (contactID != null) {
            student.setContact(crmContactManager.get(contactID));
        }
        return student.getObjectID();
    }

    @Transactional
    public Integer saveGymStudentItem(StudentItem studentItem) {
        EdsStudent student = new EdsStudent();
        if (studentItem.getObjectId() != null) {
            student = studentManager.get(studentItem.getObjectId());
        }
        if (studentItem.getCustomerID() != null) {
            EdsCrmAccount customer = crmAccountManager.get(studentItem.getCustomerID());
            student.setCustomer(customer);
        }
        if (studentItem.getContactID() != null) {
            EdsCrmContact contact = crmContactManager.get(studentItem.getContactID());
            student.setContact(contact);
            student.setGender(contact.getGender());
        }
        if (studentItem.getCardTypeID() != null) {
            student.setIDCard(referenceManager.get(studentItem.getCardTypeID()));
        }
        student.setCompEmplNumber(studentItem.getCompEmpNum());
        student.setDepartmentCode(studentItem.getDepartmentCode());
        student.setIDCardNumber(studentItem.getCardNumber());
        student.setNationality(studentItem.getNationality());
        student.setActive(studentItem.isActive());
        student.setCustomFields(createStudentCustomFields(student.getCustomFields(), studentItem.getCustomFields()));

        studentManager.createOrUpdate(student);
        student.setNumber(decimalFormat.format(student.getObjectID()));
        student.setSafetyPPNumber(decimalFormat.format(student.getObjectID()));

        return student.getObjectID();
    }

    private EdsStudentCustomFields createStudentCustomFields(EdsStudentCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCustomFields = new EdsStudentCustomFields();
                studentCFManager.create(edsCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCustomFields, customFieldItems);
            return edsCustomFields;
        }
        return null;
    }
    private EdsCourseSubjectCustomFields createCourseSubjectCustomFields(EdsCourseSubjectCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCustomFields = new EdsCourseSubjectCustomFields();
                courseSubjectCFManager.create(edsCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCustomFields, customFieldItems);
            return edsCustomFields;
        }
        return null;
    }

    private EdsTrainingContractCustomFields createTrainingContractCustomFields(EdsTrainingContractCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCustomFields = new EdsTrainingContractCustomFields();
                trainingContractCFManager.create(edsCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCustomFields, customFieldItems);
            return edsCustomFields;
        }
        return null;
    }

    /**
     * Generate Student Item
     *
     * @param studentID - student ID
     * @return - studentItem
     */
    public StudentItem getStudentItem(Integer studentID) {
        StudentItem studentItem = new StudentItem();

        studentItem.setCountries(commonService.getCountries());
        studentItem.setStates(commonService.getRegions());
        studentItem.setCardTypes(commonServiceLocal.convertReference2SelectItem(StudentItem._STUDENT_CARD_TYPES, true, null));

        EdsStudent student;
        if (studentID != null) {
            student = studentManager.get(studentID);
            studentItem = student.getRPC(studentItem);
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Students);
            studentItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(student.getCustomFields(), customFieldsItems));
        }

        return studentItem;
    }

    /**
     * Generate Student item list
     *
     * @param filterParameter - filterParameter
     * @return - student list items
     */
    public ListResult<StudentItem> getStudentList(ListingFilterParameter filterParameter) {
        List<EdsStudent> studentList = studentManager.getStudentList(filterParameter);
        Integer totalCount = studentManager.getStudentListTotalCount(filterParameter);
        ArrayList<StudentItem> studentItems = new ArrayList<>();
        for (EdsStudent student : studentList) {
            studentItems.add(student.getRPC());
        }
        return new ListResult<>(studentItems, totalCount);
    }

    public List<StudentItem> getStudentListForCSV(ListingFilterParameter filterParameter) {

        List<EdsStudent> studentList = studentManager.getScheduledCourseStudentsForCSV(filterParameter);

        EdsCourseSchedule scheduleStudent = scheduledCourseManager.get(filterParameter.getScheduledCourseID());

        List<StudentItem> studentItems = new ArrayList<>();
        for (EdsStudent student : studentList) {
            EdsCourseScheduleStudent courseScheduleStudent = courseScheduleStudentManager.getCourseScheduleStudent(scheduleStudent, student.getObjectID());

            StudentItem studentItem = student.getRPC();
            if (courseScheduleStudent != null) {
                EdsCourseSchedule courseSchedule = courseScheduleStudent.getCourseScheduleBooking();
                if (courseSchedule != null) {
                    studentItem.setInstructor(courseSchedule.getInstructor() != null ? courseSchedule.getInstructor().getName() : "N/A");
                    studentItem.setCourseSchedulerStartDate(courseSchedule.getStartDate());
                    studentItem.setCourseSchedulerEndDate(courseSchedule.getEndDate());
                    studentItem.setCourseSchedulerNumber(courseSchedule.getNumber());
                }

                if (courseScheduleStudent.getExamStatus() != null) {
                    studentItem.setExamStatus(courseScheduleStudent.getExamStatus().getName());
                    studentItem.setExamStatusId(courseScheduleStudent.getExamStatus().getObjectID());
                }
            }

            if (student.getCustomer() != null) {
                studentItem.setCompany(student.getCustomer().getName());
            }
            studentItems.add(studentItem);
        }
        return studentItems;
    }

    @Override
    public ListResult<StudentItem> getScheduledCourseStudents(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        EdsCourseSchedule courseSchedule = scheduledCourseManager.get(fp.getScheduledCourseID());

        List<EdsStudent> studentList = studentManager.getScheduledCourseStudents(fp.getScheduledCourseID(), fp);
        Integer totalCount = studentManager.getScheduledCourseStudentsTotalCount(fp);
        ArrayList<StudentItem> studentItems = new ArrayList<>();
        for (EdsStudent student : studentList) {
            StudentItem studentItem = student.getRPC();

            EdsCourseScheduleStudent courseScheduleStudent = courseScheduleStudentManager.getCourseScheduleStudent(courseSchedule, student.getObjectID());
            if (courseScheduleStudent.getStatus() != null) {
                studentItem.setStatus(courseScheduleStudent.getStatus().getName());
                studentItem.setStatusCode(courseScheduleStudent.getStatus().getCode());
            }
            if (courseScheduleStudent.getAttendedStatus() != null) {
                studentItem.setAttendedStatus(courseScheduleStudent.getAttendedStatus().getName());
            }
            if (courseScheduleStudent.getExamStatus() != null) {
                studentItem.setExamStatus(courseScheduleStudent.getExamStatus().getName());
            }
            if (courseScheduleStudent.getGrade() != null) {
                studentItem.setGrade(courseScheduleStudent.getGrade());
            }
            if (courseScheduleStudent.getPoints() != null) {
                studentItem.setPoints(courseScheduleStudent.getPoints());
            }
            if (courseScheduleStudent.getCourseBooking() != null) {
                studentItem.setCourseBookingId(courseScheduleStudent.getCourseBooking().getObjectID());
                studentItem.setCourseBookingNumber(courseScheduleStudent.getCourseBooking().getNumber());
            }
            if (courseScheduleStudent.getInvoiceID() != null) {
                EdsInvoice invoice = invoiceManager.get(courseScheduleStudent.getInvoiceID());
                studentItem.setInvoiceID(invoice.getObjectID());
                studentItem.setInvoiceNumber(invoice.getNumber());
            }
            studentItems.add(studentItem);
        }
        return new ListResult<>(studentItems, totalCount);
    }

    /**
     * Generate student delete option
     *
     * @param studentID - student ID
     * @return - student delete or not
     */
    public Boolean deleteStudent(Integer studentID) {
        if (studentID != null) {
            EdsStudent student = studentManager.get(studentID);
            if (student != null) {
                courseScheduleStudentManager.deleteStudentFromCourseScheduledStudent(studentID);
                EdsCrmContact contact = crmContactManager.get(student.getContact().getObjectID());
                contact.setDeleted(true);
                crmContactManager.createOrUpdate(contact);
                studentManager.createOrUpdate(student);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean deleteStudentCourseScheduledStudents(Integer scheduledCourseID, Integer studentID) {
        if (scheduledCourseID != null && studentID != null) {
            EdsCourseScheduleStudent courseScheduleStudent = courseScheduleStudentManager.getCourseScheduleStudentByStudentId(scheduledCourseID, studentID);
            if (courseScheduleStudent != null) {
                courseScheduleStudent.setStatus(referenceManager.findReference(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_PARENT_STATUS, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED));
                courseScheduleStudentManager.update(courseScheduleStudent);
                baseEventPostProcessor.registerEvent(CourseBookingEventListenerImpl.TYPE_STUDENT_COURSE_SCHEDULE, CourseBookingEventListenerImpl.EVENT_STUDENT_CANCEL_FROM_COURSE_SCHEDULE, courseScheduleStudent, userManager.getUser());
                try {
                    EdsCourseSchedule courseSchedule = scheduledCourseManager.get(scheduledCourseID);
                    solrManager.addCourseScheduleToIndex(courseSchedule);
                } catch (IOException | SolrServerException e) {
                    e.printStackTrace();
                }

                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    public void saveCourse(CourseItem courseItem) {
        EdsCourse course = new EdsCourse();
        if (courseItem.getObjectID() != null) {
            course = courseManager.get(courseItem.getObjectID());
        }
        course.setSubject(courseSubjectManager.get(courseItem.getSubject().getId()));
        course.setIntNumber(courseItem.getNumberData().getIntNumber());
        course.setNumber(courseItem.getNumberData().getNumberString());
        course.setName(courseItem.getCourseName());
        course.setDescription(courseItem.getDescription());
        course.setOtherPreRequisites(courseItem.getOtherPreRequisites());

        course.setLastUpdateTime(new Date());
        course.setDuration(courseItem.getDuration());
        course.setValidity(courseItem.getValidity());

        if (courseItem.getPreRequisite() != null) {
            course.getPreRequisite().clear();
            for (SelectItem item : courseItem.getPreRequisite()) {
                course.getPreRequisite().add(courseManager.get(item.getId()));
            }
        }

        course.setLastUpdateTime(new Date());

        //course instructor functional {clear old course instructors and add new instructors of the course}
        course.getInstructors().clear();
        if (courseItem.getInstructors() != null && !courseItem.getInstructors().isEmpty()) {
            ArrayList<EdsEmployee> instructors = new ArrayList<>();
            for (SelectItem instructor : courseItem.getInstructors()) {
                instructors.add(employeeManager.get(instructor.getId()));
            }
            course.setInstructors(instructors);
        }

        //course requirements functional {clear old course requirements and add new requirements of the course}
        course.getCourseRequirements().clear();
        if (courseItem.getCourseRequirements() != null) {
            courseItem.getCourseRequirements();
            for (SelectItem category : courseItem.getCourseRequirements()) {
                if (category.getId() != null) {
                    course.getCourseRequirements().add(referenceManager.get(category.getId()));
                }
            }
        }

        course.setCustomFields(createCourseCustomFields(course.getCustomFields(), courseItem.getCustomFieldItems()));
        courseManager.createOrUpdate(course);

        if (courseItem.getPricePerLocationStudent() != null && courseItem.getPricePerLocationStudent().size() > 0) {
            coursePriceManager.deleteCoursePrices(course.getObjectID());
            for (Integer locationID : courseItem.getPricePerLocationStudent().keySet()) {
                EdsLocation location = locationManager.get(locationID);
                BigDecimal locationPrice = courseItem.getPricePerLocationStudent().get(locationID);
                BigDecimal locationStopFee = courseItem.getStopFeePerLocationStudent().get(locationID);

                //create course price
                EdsCoursePrice coursePrice = new EdsCoursePrice();
                coursePrice.setLocation(location);
                coursePrice.setPrice(locationPrice);
                coursePrice.setStopFee(locationStopFee);
                coursePrice.setCourse(course);
                course.addCoursePrices(coursePrice);
            }
        }
    }

    private EdsCourseCustomFields createCourseCustomFields(EdsCourseCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCustomFields = new EdsCourseCustomFields();
                courseCFManager.create(edsCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCustomFields, customFieldItems);
            return edsCustomFields;
        }
        return null;
    }

    public Integer saveTrainingContract(TrainingContractItem contractItem) {
        EdsTrainingContract contract = new EdsTrainingContract();
        if (contractItem.getObjectID() != null) {
            contract = trainingContractManager.get(contractItem.getObjectID());
        }
        contract.setName(contractItem.getName());
        contract.setDescription(contractItem.getDescription());
        contract.setEndDate(contractItem.getEndDate());
        contract.setStartDate(contractItem.getStartDate());
        contract.setPrePaid(contractItem.getPrepaid());
        contract.setUpdatedDate(new Date());
        contract.setAccount(crmAccountManager.get(contractItem.getAccountID()));
        contract.getCourses().clear();
        for (Integer id : contractItem.getCourseIDs()) {
            contract.getCourses().add(courseManager.get(id));
        }
        contract.setCustomFields(createTrainingContractCustomFields(contract.getCustomFields(), contractItem.getCustomFields()));

        trainingContractManager.createOrUpdate(contract);
        saveContractCoursePrices(contract.getObjectID(), contractItem.getObjectID() == null);
        return contract.getObjectID();
    }

    public void saveContractCoursePrices(Integer contractID, boolean isNew) {
        EdsTrainingContract contract = trainingContractManager.get(contractID);
        List<EdsCourse> courses = trainingContractManager.getCourses(contractID);
        if (!isNew) {
            ArrayList<EdsCourse> tempCourses = new ArrayList<>();
            ArrayList<EdsContractCoursePrice> tempPrices = new ArrayList<>();
            List<EdsContractCoursePrice> allContractCoursePrices = contractCoursePriceManager.getContractCoursePrices(contractID);
            for (EdsContractCoursePrice coursePrice : allContractCoursePrices) {
                if (courses.contains(coursePrice.getCourse())) {
                    tempPrices.add(coursePrice);
                    if (!tempCourses.contains(coursePrice.getCourse())) {
                        tempCourses.add(coursePrice.getCourse());
                    }
                }
            }
            courses.removeAll(tempCourses);
            allContractCoursePrices.removeAll(tempPrices);
            if (allContractCoursePrices != null && allContractCoursePrices.size() > 0) {
                for (EdsContractCoursePrice item : allContractCoursePrices) {
                    contractCoursePriceManager.delete(item);
                }
            }
        }
        for (EdsCourse course : courses) {
            List<EdsCoursePrice> originalPrices = coursePriceManager.getCoursePrices(course.getObjectID());
            for (EdsCoursePrice item : originalPrices) {
                EdsContractCoursePrice coursePrice = new EdsContractCoursePrice();
                coursePrice.setContract(contract);
                coursePrice.setCourse(item.getCourse());
                coursePrice.setPrice(item.getPrice());
                coursePrice.setLocation(item.getLocation());
                coursePrice.setStopFee(item.getStopFee());
                contractCoursePriceManager.create(coursePrice);
            }
        }
    }

    public TrainingContractItem getContractItem(Integer objectID) {
        TrainingContractItem item = new TrainingContractItem();
        if (objectID != null) {
            EdsTrainingContract contract = trainingContractManager.get(objectID);
            if (contract != null) {
                item = contract.getRPC();
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.CourseSubject);
                item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(contract.getCustomFields(), customFieldsItems));
            }
        }
        ListingFilterParameter parameter = new ListingFilterParameter();
        parameter.setLimit(1000);
        List<EdsCourse> courses = courseManager.list(parameter);
        if (courses != null) {
            SelectItem[] _courses = new SelectItem[courses.size()];
            int i = 0;
            for (EdsCourse course : courses) {
                _courses[i++] = course.getAsSelectItem();
            }
            item.setCoursesList(_courses);
        }
        return item;
    }

    public CourseItem getCourseItem(Integer objectID) {
        CourseItem item = new CourseItem();
        if (objectID != null) {
            EdsCourse course = courseManager.get(objectID);

            if (course != null) {
                item = course.getRPC();
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Course);
                item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(course.getCustomFields(), customFieldsItems));

                EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();

                if (settings != null && settings.getCourseNumberingFormat() != null) {
                    item.getNumberData().setNumberFormat(settings.getCourseNumberingFormat());
                } else {
                    NumberData numberData = EdsNumberingSettings.getDefaultDataForTraining(item.getNumberData().getIntNumber(), EdsNumberingSettings.DEF_COURSE_PREFIX);
                    if (course != null) {
                        numberData.setNumberString(course.getNumber());
                    }
                    item.setNumberData(numberData);
                }

                List<EdsEmployee> instructors = course.getInstructors();
                if (instructors != null && !instructors.isEmpty()) {
                    item.setInstructorMap(getInstructors(instructors));
                    ArrayList<SelectItem> items = new ArrayList<>();
                    for (EdsEmployee employee : instructors) {
                        items.add(new SelectItem(employee.getObjectID(), employee.getFullName()));
                    }
                    item.setInstructors(items);
                } else {
                    item.setInstructorMap(getInstructors());
                }
            }
        } else {
            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            Integer intNumber = courseManager.getCourseLastIntNumber();
            if (settings != null && settings.getCourseNumberingFormat() != null) {
                item.setNumberData(settings.parseNumberData(intNumber != null ? intNumber : 0, settings.getCourseNumberingFormat()));
            } else {
                item.setNumberData(EdsNumberingSettings.getDefaultData(intNumber != null ? intNumber : 0, EdsNumberingSettings.DEF_COURSE_PREFIX));
            }
            item.setInstructorMap(getInstructors());
        }

        //Course List
        ListingFilterParameter parameter = new ListingFilterParameter();
        parameter.setLimit(1000);
        List<EdsCourse> courses = courseManager.list(parameter);
        if (courses != null && !courses.isEmpty()) {
            ArrayList<SelectItem> courseItems = new ArrayList<>();
            for (EdsCourse course : courses) {
                courseItems.add(new SelectItem(course.getObjectID(), course.getName()));
            }
            item.setCourses(courseItems.toArray(new SelectItem[]{}));
        }

        //all requirement list of the course
        List<EdsReference> rCatetories = referenceManager.listReferences(BOOKING_ITEM_CATEGORY);
        if (rCatetories != null) {
            SelectItem[] _categories = new SelectItem[rCatetories.size()];
            int i = 0;
            for (EdsReference reference : rCatetories) {
                _categories[i++] = reference.getAsSelectItem();
            }
            item.setCourseRequirementList(_categories);
        }
        return item;
    }

    public ListResult<CourseItem> getCourseList(ListingFilterParameter filterParameter) {
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        ListResult<CourseItem> result = new ListResult<>(new ArrayList<>(), 0);
        List<EdsCourse> courses = courseManager.list(filterParameter);
        Integer coursesTotal = courseManager.getCourseTotalCount(filterParameter);
        if (courses != null && !courses.isEmpty()) {
            ArrayList<CourseItem> courseItems = new ArrayList<>();
            CourseItem item;
            for (EdsCourse course : courses) {
                item = course.getRPC();
                if (course.getInstructors() != null && course.getInstructors().size() > 0) {
                    ArrayList<SelectItem> instructorsItem = new ArrayList<>();
                    for (EdsEmployee insItem : course.getInstructors()) {
                        instructorsItem.add(new SelectItem(insItem.getObjectID(), insItem.getFullName()));
                    }
                    item.setInstructors(instructorsItem);
                }
                if (filterParameter.getListPanelTool() != null) {
                    HashMap<String, Object> map = CustomFieldsUtils.getRPCCustomFields(course.getCustomFields(), panelSettings.getColumnCodeName());
                    item.setCustomFieldValuesItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
                }
                courseItems.add(item);
            }
            result = new ListResult<>(courseItems, coursesTotal);
        }
        return result;
    }

    public ListResult<TrainingContractItem> getTreningContractsList(ListingFilterParameter filterParameter) {
        ListResult<TrainingContractItem> result = new ListResult<>(new ArrayList<>(), 0);
        List<EdsTrainingContract> contracts = trainingContractManager.list(filterParameter);
        Integer contractsTotal = trainingContractManager.getContractTotalCount(filterParameter);
        if (contracts != null && !contracts.isEmpty()) {
            ArrayList<TrainingContractItem> contractItems = new ArrayList<>();
            TrainingContractItem item;
            for (EdsTrainingContract contract : contracts) {
                item = contract.getRPC();
                contractItems.add(item);
            }
            result = new ListResult<>(contractItems, contractsTotal);
        }
        return result;
    }

    public Boolean deleteCourse(Integer objectID) {
        EdsCourse course = courseManager.get(objectID);
        Integer courseIdBySchedule = scheduledCourseManager.getCountOfSchedulesByCourse(course);
        if (courseIdBySchedule == 0) {
            course.setDeleted(true);
            courseManager.update(course);
            return true;
        }

        return false;
    }

    public void deleteTreningContracts(Integer objectID) {
        EdsTrainingContract course = trainingContractManager.get(objectID);
        course.setDeleted(true);
        trainingContractManager.update(course);

    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getInstructors() {
        return getInstructors(null);
    }

    public SelectItem[] getAllInstructors() {
        ArrayList<SelectItem> instructors = new ArrayList<>();
        List<EdsEmployee> allInstructors = userManager.getUsersByROLE(userManager.getUser().getCompany().getObjectID(), roleManager.getByCode(INSTRUCTOR).getObjectID(), false);
        if (allInstructors != null && allInstructors.size() > 0) {
            for (EdsEmployee instructor : allInstructors) {
                instructors.add(new SelectItem(instructor.getObjectID(), instructor.getFullName()));
            }
        }
        return instructors.toArray(new SelectItem[]{});
    }

    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getInstructors(List<EdsEmployee> selectedEmployees) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = new LinkedHashMap<>();
        List<EdsEmployee> allEmployees = userManager.getUsersByROLE(userManager.getUser().getCompany().getObjectID(), roleManager.getByCode(INSTRUCTOR).getObjectID(), false);
        boolean team;
        for (EdsEmployee pe : allEmployees) {
            team = false;
            KpiTreeInfo sItem = new KpiTreeInfo();
            EdsEmployee resEmployee = pe.getEmployeeDepartment() != null ? pe.getEmployeeDepartment().getEmployee() : null;
            if (resEmployee != null) {
                sItem.setId(pe.getObjectID());
                sItem.setName(resEmployee.getName());
                sItem.setEmployeeId(pe.getObjectID());
                if (selectedEmployees != null && selectedEmployees.contains(resEmployee)) {
                    sItem.setSelected(true);
                }
                if (pe.getEmployeeDepartment().getTeam() != null) {
                    sItem.setDepartmentId(pe.getEmployeeDepartment().getEmployee().getTeam().getObjectID());
                    sItem.setDepartmentName(pe.getEmployeeDepartment().getEmployee().getTeam().getName());
                    for (KpiTreeInfo s : assigneeList.keySet()) {
                        if (s.getId().equals(pe.getEmployeeDepartment().getTeam().getObjectID())) {
                            team = true;
                            assigneeList.get(s).add(sItem);
                            break;
                        }
                    }

                    if (!team) {
                        KpiTreeInfo department = new KpiTreeInfo(pe.getEmployeeDepartment().getTeam().getObjectID(), pe.getEmployeeDepartment().getTeam().getName());
                        ArrayList<KpiTreeInfo> list = new ArrayList<>();
                        list.add(sItem);
                        assigneeList.put(department, list);
                    }
                }
            }
        }

        return assigneeList;
    }

    @Override
    public ListResult<ScheduledCourseItem> getCourseScheduleList(ListingFilterParameter filterParameter) {
        List<EdsCourseSchedule> courseSchedules = scheduledCourseManager.list(filterParameter);
        Integer countOfScheduledCourse = scheduledCourseManager.getCountOfScheduledCourse(filterParameter);

        ArrayList<ScheduledCourseItem> scheduledCourseList = new ArrayList<>();
        for (EdsCourseSchedule courseSchedule : courseSchedules) {
            ScheduledCourseItem scheduledCourseItem = courseSchedule.getRPC();
            scheduledCourseList.add(scheduledCourseItem);
        }

        return new ListResult<>(scheduledCourseList, countOfScheduledCourse);
    }

    @Override
    public ListResult<ScheduledCourseItem> getCourseScheduleFromSolr(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        FacetFilterRpc courseScheduleFacFilter = filterParametrs.getFacetFilter();
        if (courseScheduleFacFilter != null && !courseScheduleFacFilter.isFilterChanges()) {
            courseScheduleFacFilter = commonServiceLocal.getUserFacetFilter(courseScheduleFacFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getCourseScheduleCoreSolrQuery(filterParametrs));
        solrQuery.append(QueryBuilderForSolr.getCourseScheduleListFacetFilterQuery(courseScheduleFacFilter, edsUser.getCompany(),
                SolrCourseScheduleRepresenter.FIELD_START_DATE, null,
                FacetContentType.CourseScheduleFaceFilter.getContentCode()[0],
                FacetContentType.CourseScheduleFaceFilter.getContentCode()[1],
                FacetContentType.CourseScheduleFaceFilter.getContentCode()[2],
                FacetContentType.CourseScheduleFaceFilter.getContentCode()[3],
                FacetContentType.CourseScheduleFaceFilter.getContentCode()[4]));
        return getCourseScheduleListResponse(filterParametrs, solrQuery.toString());
    }

    private ListResult<ScheduledCourseItem> getCourseScheduleListResponse(ListingFilterParameter filterParameter, String solrQuery) {
        var courseScheduleSolrDocPage = courseScheduleSolrComponent.getList(filterParameter, solrQuery);
        return getCourseScheduleFromSolrResult(filterParameter, courseScheduleSolrDocPage);
    }

    private ListResult<ScheduledCourseItem> getCourseScheduleFromSolrResult(ListingFilterParameter fp, Page<CourseScheduleSolrDoc> resp) {
        ArrayList<ScheduledCourseItem> itemList = new ArrayList<>();
        int totalNumber = 0;
        List<CourseScheduleSolrDoc> resultList = resp != null ? resp.getContent() : null;

        if (resultList == null) {
            return new ListResult<>(itemList, totalNumber);
        }

        totalNumber = (int) resp.getTotalElements();
        if (!CollectionUtils.isEmpty(resultList)) {
            for (CourseScheduleSolrDoc doc : resultList) {
                if (doc != null) {
                    ScheduledCourseItem item = mapFromSolrToScheduledCourseItem(doc);
                    item.setHasInvoice(scheduledCourseManager.hasInvoice(item.getObjectID()));
                    item.setCountOfNotAddressedStudent(scheduledCourseManager.countOfNotAddressedStudents(item.getObjectID()));
                    if (fp.getListPanelTool() != null) {
                        item.setCustomFieldValuesItems(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, fp.getListPanelTool().getColumnCodeName()));
                    }
                    itemList.add(item);
                }
            }
        }
        return new ListResult<>(itemList, totalNumber);
    }

    private ScheduledCourseItem mapFromSolrToScheduledCourseItem(CourseScheduleSolrDoc doc) {
        ScheduledCourseItem item = new ScheduledCourseItem();
        item.setObjectID(doc.getCourseScheduleId());
        item.setNumber(doc.getCourseScheduleNumber());
        item.setCourseID(doc.getCourseId());
        item.setCourseName(doc.getCourseName());
        item.setLanguageID(doc.getLanguageId());
        item.setLanguageName(doc.getLanguageName());
        item.setEnableOvertime(doc.getEnableOvertime());
        item.setStartDate(doc.getStartDate());
        item.setLocationID(doc.getLocationId());
        item.setLocationName(doc.getLocationName());
        item.setInstructorID(doc.getInstructorId());
        item.setInstructorName(doc.getInstructorName());
        item.setDuration(doc.getDuration());
        item.setNumberOfSeats(doc.getNumberOfSeats());
        item.setAssessorID(doc.getAssessorId());
        item.setAssessorName(doc.getAssessorName());
        item.setCountOfStudent(doc.getCountOfStudent());
        item.setCountOfConfirmedStudent(doc.getCountOfConfirmedStudent());
        item.setStatusID(doc.getStatusId());
        item.setStatusName(doc.getStatusName());
        item.setStatusCode(doc.getStatusCode());
        item.setCreatedDate(doc.getCreatedAt());
        item.setModifiedDate(doc.getModifiedAt());
        return item;
    }

    private SolrQuery getCourseScheduleSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(filterParameter.getLimit()));

        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                if (ScheduledCourseItem.NUMBER.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.SORTABLE_COURSE_SCHEDULE_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.COURSE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.SORTABLE_COURSE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.LANGUAGE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.SORTABLE_LANGUAGE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.START_DATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.FIELD_START_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.INSTRUCTOR.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.SORTABLE_INSTRUCTOR_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.DURATION.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.SORTABLE_DURATION, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.AVAILABLE_SET.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.FIELD_NUMBER_OF_SEATS, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.ASSESSOR.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.SORTABLE_ASSESSOR_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.COUNT_OF_STUDENT.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.FIELD_COUNT_OF_STUDENT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.COUNT_OF_CONFIRMED_STUDENT.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.FIELD_COUNT_OF_CONFIRMED_STUDENT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (ScheduledCourseItem.COURSE_SCHEDULE_STATUS.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseScheduleRepresenter.FIELD_STATUS_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                }
            } else {
                query.setSort(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    @Override
    public ListResult<ScheduledCourseItem> getConfirmedScheduledCourseList(ListingFilterParameter fp) {
        List<EdsCourseSchedule> courseSchedules = scheduledCourseManager.getConfirmedScheduledCourseList(fp);
        Integer countOfScheduledCourse = scheduledCourseManager.getCountOfConfirmedScheduledCourse(fp);

        ArrayList<ScheduledCourseItem> scheduledCourseList = new ArrayList<>();
        for (EdsCourseSchedule courseSchedule : courseSchedules) {
            scheduledCourseList.add(courseSchedule.getRPC());
        }

        return new ListResult<>(scheduledCourseList, countOfScheduledCourse);
    }

    @Override
    public ListResult<ScheduledCourseItem> getInstructorReassignCourseList(ListingFilterParameter filterParameter) {
        List<EdsCourseSchedule> courseSchedules = scheduledCourseManager.listForInstructorReassign(filterParameter);
        Integer countOfScheduledCourse = scheduledCourseManager.getCountOfInstructorReassign(filterParameter);

        ArrayList<ScheduledCourseItem> scheduledCourseList = new ArrayList<>();
        for (EdsCourseSchedule courseSchedule : courseSchedules) {
            scheduledCourseList.add(courseSchedule.getRPC());
        }

        return new ListResult<>(scheduledCourseList, countOfScheduledCourse);
    }

    /**
     * Get Schedule course item by given
     *
     * @param objectID
     * @param isViewForm
     * @return
     */
    public ScheduledCourseItem getCourseSchedule(Integer objectID, boolean isViewForm) {
        ScheduledCourseItem scheduledCourseItem = null;

        if (objectID != null) {
            EdsCourseSchedule courseSchedule = scheduledCourseManager.get(objectID);
            scheduledCourseItem = courseSchedule.getRPC();
            if (!isViewForm) {
                scheduledCourseItem.setClonedDateList(scheduledCourseManager.getClonedDateList(courseSchedule));
                scheduledCourseItem.setStudentAttended(courseScheduleStudentManager.isStudentAttended(courseSchedule.getObjectID()));
            }
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.ScheduledCourse);
            scheduledCourseItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(courseSchedule.getCustomFields(), customFieldsItems));
        } else {
            scheduledCourseItem = new ScheduledCourseItem();
        }

        if (!isViewForm) {
            scheduledCourseItem.setLocations(locationManager.getLocationsAsSelectItems(new ListingFilterParameter()));
            scheduledCourseItem.setCourses(courseManager.getCoursesAsSelectItems(new ListingFilterParameter()));
            scheduledCourseItem.setLanguages(getLanguages());
            scheduledCourseItem.setTimeSlotItems(getTimeSlotItem(scheduledCourseItem.getLocationID()));
            scheduledCourseItem.setAssessors(getAssessorList());
        }
        return scheduledCourseItem;
    }

    @Override
    public AssessmentItem getAssessment(Integer objectID, Integer stdQuestionarieID) {
        EdsTCQuestionaire questionaire = questionaireManager.get(objectID);
        AssessmentItem item = new AssessmentItem();
        if (stdQuestionarieID != null) {
            List<EdsTCResponse> responses = responseManager.getResponseByStudentQuestionarie(stdQuestionarieID);
            ArrayList<QuestionarieResponseItem> responseItems = new ArrayList<>();
            QuestionarieResponseItem responseItem;
            for (EdsTCResponse res : responses) {
                responseItem = new QuestionarieResponseItem();
                responseItem.setAnswer(res.getAnswer());
                responseItem.setPointsEarnet(res.getPointsEarnet());
                responseItem.setId(res.getObjectID());
                responseItem.setQuestionNumber(res.getQuestionNumber());
                responseItems.add(responseItem);
            }
            item.setResponseItems(responseItems);
        }
        item.setObjectId(questionaire.getObjectID());
        item.setName(questionaire.getName());
        item.setTotalPoints(questionaire.getTotalPoints().toString());
        return item;
    }


    public void saveCourseScheduleInstance(Integer scheduleId){
        EdsCourseSchedule schedule = scheduledCourseManager.get(scheduleId);
        if (schedule.getRecurrenceID() != null) {
            EdsCourseSchedule lastSchedule = scheduledCourseManager.getFirstOrLastCourseScheduleInRecurringSeries(schedule.getRecurrenceID(), false);
            EdsRecurrence recurrence = recurrenceManager.get(lastSchedule.getRecurrenceID());
            if (recurrence != null) {
                EdsRecurrence cloneRecurrence = recurrence.cloneShallow();
                List<Date> recurringDates = recurrenceService.getRecurringDates(cloneRecurrence);
                if (recurringDates != null && recurringDates.size() > 0) {
                    List<Date> recDates = recurringDates.subList(1, recurringDates.size());
                    saveRecurringCourseSchedule(lastSchedule, recDates);
                }
            }
        }
    }
    public void saveRecurringCourseSchedule(EdsCourseSchedule schedule, List<Date> recurringDates){
        Integer recurrenceID = schedule.getRecurrenceID();
        long dateDiff = schedule.getEndDate().getTime() - schedule.getStartDate().getTime();
        if (recurrenceID != null && recurringDates != null && recurringDates.size() > 0) {
            final int flushLimit = 10;
            int flushCount = 0;
            System.err.println("for start:" + new Date());

            Set<Date> createdDates = new HashSet<>();
            outerloop:
            for (Date recurringDate : recurringDates) {
                EdsCourseSchedule checkSchedule = scheduledCourseManager.getScheduleInstance(recurrenceID, recurringDate);
                ScheduledCourseItem scheduledCourseItem = schedule.getRPC();
                scheduledCourseItem.setObjectID(null);
                scheduledCourseItem.setRecurrenceJobItem(null);
                Calendar startDate = ServerUtils.convertDateIntoCalendar(recurringDate);
//                if (rec.getType() != SchedulerConstant.RECURRENCE_TYPE_WEEKLY) {
//                        if (rec.getInterval() > 1 || rec.getType() != SchedulerConstant.RECURRENCE_TYPE_DAILY) {
//                            startDate.add(Calendar.DAY_OF_WEEK, 1);
//                        } else {
//                            if (rec.getEndType() == SchedulerConstant.END_AFTER_OCCURRENCES) {
//                                startDate.add(Calendar.DAY_OF_WEEK, 1);
//                            } else {
//                                continue outerloop;
//                            }
//                        }
//                    }

                createdDates.add(startDate.getTime());

                scheduledCourseItem.setStartDate(startDate.getTime());
                scheduledCourseItem.setFireTime(recurringDate);
                scheduledCourseItem.setEndDate(new Date(startDate.getTime().getTime() + dateDiff));



                scheduledCourseItem.setNumber(generateScheduleNumber().getNumberString());
                if (checkSchedule == null) {
                    saveCourseSchedule(scheduledCourseItem);
                    flushCount++;
                    if (flushCount == flushLimit) {
                        System.err.println("flush start: " + new Date());
                        scheduledCourseManager.flushAndClear();
                        flushCount = 0;
                        System.err.println("flush end: " + new Date());
                    }
                }
            }
            System.err.println("for end: " + new Date());
            System.err.println("commit end: " + new Date());
        }
    }

    /**
     * Save scheduled course from given UI parameters
     *
     * @param scheduledCourseItem
     */
    @Transactional
    public Integer saveCourseSchedule(ScheduledCourseItem scheduledCourseItem) {
        boolean isNew1 = scheduledCourseItem.getObjectID() == null;
        boolean isRecurring = (scheduledCourseItem.getRecurrenceId() != null || (scheduledCourseItem.getRecurrenceJobItem() != null && scheduledCourseItem.getRecurrenceJobItem().isEnabled()));
        boolean isRecurringEdited = false;
        boolean isRecurringAdded = false;
        boolean isRecurringRemoved = (scheduledCourseItem.getRecurrenceId() != null && (scheduledCourseItem.getRecurrenceJobItem() == null));
        Integer recurringDateSize = 0;

        boolean isSeries = (isNew1 && isRecurring && scheduledCourseItem.getRecurrenceJobItem() == null);

        EdsCourse course = null;
        if (scheduledCourseItem.getCourseID() != null && scheduledCourseItem.getLocationID() != null) {
            course = courseManager.get(scheduledCourseItem.getCourseID());
            EdsLocation location = locationManager.get(scheduledCourseItem.getLocationID());
            if (course != null && location != null) {
                EdsCoursePrice coursePrice = coursePriceManager.getCoursePriceByLocation(course.getObjectID(), location.getObjectID());
                if (coursePrice == null) {
                    return -1;
                }
            }
        }

        EdsCourseSchedule courseSchedule = null;
//        EdsEmployee oldInstructor = null;

        if (scheduledCourseItem.getObjectID() != null) {
            courseSchedule = scheduledCourseManager.get(scheduledCourseItem.getObjectID());
//            oldInstructor = courseSchedule.getInstructor();
        }

        if (courseSchedule == null) {
            courseSchedule = new EdsCourseSchedule();
        }
        courseSchedule.clear();

        courseSchedule.setCourse(courseManager.get(scheduledCourseItem.getCourseID()));
        courseSchedule.setEnableOvertime(scheduledCourseItem.isEnabledOvertime());
        courseSchedule.setStartDate(scheduledCourseItem.getStartDate());
        courseSchedule.setScheduleDuration(scheduledCourseItem.getScheduleDuration());
        courseSchedule.setEndDate(scheduledCourseItem.getEndDate());
        if (scheduledCourseItem.getEndDate() != null && course != null && course.getValidity() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(scheduledCourseItem.getEndDate());
            cal.add(Calendar.MONTH, course.getValidity());
            Date expireDate = cal.getTime();
            courseSchedule.setExpireDate(expireDate);
        }

        if (scheduledCourseItem.getLocationID() != null) {
            courseSchedule.setLocation(locationManager.get(scheduledCourseItem.getLocationID()));
        }
        if (scheduledCourseItem.getLanguageID() != null) {
            courseSchedule.setLanguage(referenceManager.get(scheduledCourseItem.getLanguageID()));
        }
        if (scheduledCourseItem.getInstructorID() != null) {
            courseSchedule.setInstructor(employeeManager.get(scheduledCourseItem.getInstructorID()));
        } else {
            courseSchedule.setInstructor(null);
        }

        if (scheduledCourseItem.getAssessorID() != null) {
            courseSchedule.setAssessor(employeeManager.get(scheduledCourseItem.getAssessorID()));
        } else {
            courseSchedule.setAssessor(null);
        }


        if (scheduledCourseItem.getStatusID() == null) {
            courseSchedule.setStatus(referenceManager.findReference(CS_DELIVERED_STATUS, CS_NOT_STARTED));
        } else {
            courseSchedule.setStatus(referenceManager.get(scheduledCourseItem.getStatusID()));
        }

        EdsRecurrence scheduleRecurrence = null;
        if (isNew1 && scheduledCourseItem.getRecurrenceJobItem() != null){
            //Recurrence and fire time is saved when the task is saved
            EdsRecurrence recurrence = new EdsRecurrence();
            recurrenceService.wrapRecurrenceJobItemToEdsRecurrence(scheduledCourseItem.getRecurrenceJobItem(), recurrence, recurrenceJobManager.get(RECURRING_COURSE_SCHEDULE));
            Date sd = (Date) scheduledCourseItem.getStartDate().clone();
            recurrence.setStartDate(sd);  // this need for allDay tasks
            List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
            if (recurringDates != null && recurringDates.size() > 0) {
                recurringDateSize = recurringDates.size();
                scheduledCourseItem.setStartDate(recurringDates.get(0));
                 scheduleRecurrence = recurrenceManager.get(scheduledCourseItem.getRecurrenceId());
            }
        }

        Integer recurrenceId = scheduledCourseItem.getRecurrenceId();
        if (scheduledCourseItem.getRecurrenceJobItem() != null) {
            scheduledCourseItem.getRecurrenceJobItem().setBusObjectId(scheduledCourseItem.getObjectID());
            scheduledCourseItem.getRecurrenceJobItem().setJobType(RECURRING_COURSE_SCHEDULE);
            recurrenceId = recurrenceService.saveRecurrenceJob(scheduledCourseItem.getRecurrenceJobItem());
        }
        if (recurrenceId != null) {
            EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
            courseSchedule.setRecurrenceID(recurrenceId);
            recurrence.setChanged(false);
            if (scheduledCourseItem.getFireTime() != null) {
                courseSchedule.setFireTime(scheduledCourseItem.getFireTime());
            } else {
                courseSchedule.setFireTime(recurrence.getStartDate());
            }
        }

        courseSchedule.setNumberOfSeats(scheduledCourseItem.getNumberOfSeats());
        courseSchedule.setCustomFields(createScheduledCourseCustomFields(courseSchedule.getCustomFields(), scheduledCourseItem.getCustomFieldItems()));
        boolean isNew = scheduledCourseManager.createOrUpdate(courseSchedule);

        if (isNew1 && scheduledCourseItem.getRecurrenceJobItem() != null) {
            baseEventPostProcessor.registerEvent(RecurringBgCourseScheduleListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, courseSchedule, null, scheduleRecurrence);
        }

//        if (oldInstructor == null || (oldInstructor != null && !oldInstructor.getObjectID().equals(scheduledCourseItem.getInstructorID()))) {
        //save scheduled course instructors
        updateScheduledCourseInstructors(courseSchedule);
//        }

        //save scheduled course reservations
        updateScheduledCourseReservations(courseSchedule, scheduledCourseItem);

        if ((courseSchedule.getNumber() == null || courseSchedule.getNumber().isEmpty()) && courseSchedule.getIntNumber() == null) {
            NumberData numberData = generateScheduleNumber();
            courseSchedule.setNumber(numberData.getNumberString());
            courseSchedule.setIntNumber(numberData.getIntNumber());
        }
        EdsCoursePrice coursePrice = coursePriceManager.getCoursePriceByLocation(courseSchedule.getCourse().getObjectID(), courseSchedule.getLocation().getObjectID());
        courseSchedule.setPrice(coursePrice != null ? coursePrice.getPrice() : BigDecimal.ZERO);
        courseSchedule.setStopFee(coursePrice != null ? coursePrice.getStopFee() : BigDecimal.ZERO);

        //create event for the scheduled course
        createEventByCourseSchedule(courseSchedule);
        //workflow action
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, isNew ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, courseSchedule, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_COURCE_SCHEDULE);
        try {
            solrManager.addCourseScheduleToIndex(courseSchedule);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }

        return courseSchedule.getObjectID();
    }

    private EdsScheduledCourseCustomFields createScheduledCourseCustomFields(EdsScheduledCourseCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCustomFields = new EdsScheduledCourseCustomFields();
                scheduledCourseCFManager.create(edsCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCustomFields, customFieldItems);
            return edsCustomFields;
        }
        return null;
    }

    @Override
    public void deleteCourseScheduleByIds(Integer[] courseScheduleIds) {
        if (courseScheduleIds != null) {
            for (Integer objectID : courseScheduleIds) {
                deleteCourseSchedule(objectID);
            }
        }
    }

    @Override
    public boolean deleteCourseSchedule(Integer objectID) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setScheduledCourseID(objectID);
        Integer ccsCount = scheduledCourseManager.getCountOfConfirmedScheduledCourse(fp);

        //you can't delete confirmed course schedule course(confirmed course schedule has more than 3 students)
        if (ccsCount > 0) {
            return false;
        }

        EdsCourseSchedule courseSchedule = scheduledCourseManager.get(objectID);
        if (CS_DELIVERED.equals(courseSchedule.getStatus().getCode())) {
            return false;
        }

        deleteEventByScheduledCourse(courseSchedule);
        scheduledCourseManager.deleteScheduledCourseInstructors(courseSchedule.getObjectID());
        scheduledCourseManager.deleteScheduledCourseReservations(courseSchedule.getObjectID());
        courseSchedule.getReservations().clear();

        courseSchedule.setDeleted(true);
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, courseSchedule, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_COURCE_SCHEDULE);
        try {
            solrManager.removeCourseSchedulesByIds(objectID);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void deleteEventByScheduledCourse(EdsCourseSchedule courseSchedule) {
        //EdsEvent event = eventManager.get(courseSchedule.getEventID());

        if (courseSchedule.getEventID() != null) {
            googleCalendarServiceLocal.deleteEvent(null, courseSchedule.getEventID(), Constants.DELETE_THIS_INSTANCE, false);
        }
    }


    @Override
    public SelectItem[] getCourseInstructors(Integer courseID) {
        EdsCourse course = courseManager.get(courseID);
        if (course.getInstructors() != null) {
            List<SelectItem> courseInst = new ArrayList<>();
            for (EdsEmployee instructor : course.getInstructors()) {
                courseInst.add(new SelectItem(instructor.getObjectID(), instructor.getFullName()));
            }

            return courseInst.toArray(new SelectItem[]{});
        }
        return new SelectItem[0];
    }

    @Override
    public ScheduledCourseItem[] getInstructorScheduledCoursesByDate(Date date, Integer instructorId, Integer locationId) {
        if (instructorId == null) {
            EdsUser edsUser = userManager.getUser();
            instructorId = edsUser.getObjectID();
            if (edsUser.getLocation() != null) {
                locationId = edsUser.getLocation().getObjectID();
            }
        }
        List<EdsCourseSchedule> courseScheduleList = instructorScheduleCourseManager.getInstructorScheduleCourseByDate(date, instructorId, locationId);
        if (courseScheduleList != null && courseScheduleList.size() > 0) {
            List<ScheduledCourseItem> instructorScheduledCourseList = new ArrayList<>();
            for (EdsCourseSchedule courseSchedule : courseScheduleList) {
                instructorScheduledCourseList.add(courseSchedule.getRPC());
            }

            return instructorScheduledCourseList.toArray(new ScheduledCourseItem[]{});
        }
        return null;
    }

    @Override
    public InstructorStudentItem getInstructorAndStudents(Date date, Integer courseScheduleId) {
        EdsCourseSchedule courseSchedule = scheduledCourseManager.get(courseScheduleId);
        EdsCourse edsCourse = courseSchedule.getCourse();
        EdsInstructorScheduledCourse edsInstructorScheduledCourse = instructorScheduleCourseManager.getInstructorSchedule(courseSchedule.getInstructor().getObjectID(), courseScheduleId, date);

        InstructorItem instructorItem = new InstructorItem();
        instructorItem.setInsSchCourseId(edsInstructorScheduledCourse.getObjectID());
        instructorItem.setInstructorName(edsInstructorScheduledCourse.getInstructor().getName());
        instructorItem.setCourseName(edsCourse.getName());
        instructorItem.setLanguageName(courseSchedule.getLanguage().getName());
        instructorItem.setAttended(edsInstructorScheduledCourse.isAttended());
        instructorItem.setApproved(edsInstructorScheduledCourse.isApproved());

        InstructorStudentItem instructorStudentItem = new InstructorStudentItem();
        List<EdsStudent> edsStudentList = courseBookingManager.getStudentListByCourseScheduleId(courseScheduleId);
        Map<Integer, Object[]> studentAttendedMap = studentAttendedManager.getStudentsAttended(edsInstructorScheduledCourse.getObjectID(), ServerUtils.getAsCommoDelimited(edsStudentList, "(0)"));
        int i = 0;
        for (EdsStudent edsStudent : edsStudentList) {
            StudentItem studentItem = edsStudent.getRPC();
            studentItem.setNumberOrder(++i);
            if (studentAttendedMap.containsKey(studentItem.getObjectId())) {
                Object[] objs = studentAttendedMap.get(studentItem.getObjectId());
                studentItem.setAttended(Boolean.parseBoolean(String.valueOf(objs[2])));
                studentItem.setStudentAttendedId((Integer) objs[0]);
            } else {
                studentItem.setAttended(false);
                studentItem.setStudentAttendedId(null);
            }
            EdsCourseScheduleStudent courseScheduleStudent = courseScheduleStudentManager.getCourseScheduleStudent(courseSchedule, edsStudent.getObjectID());
            studentItem.setAttendedStatusID(courseScheduleStudent.getAttendedStatus() != null ? courseScheduleStudent.getAttendedStatus().getObjectID() : null);
            instructorStudentItem.getStudentItems().add(studentItem);
        }
        instructorStudentItem.setInstructorItems(instructorItem);
        instructorStudentItem.setAttendedStatusList(commonServiceLocal.referenceSelectItemWithPleaseSelect(referenceManager.listReferences(STUDENT_ATTENDED_STATUS)));
        return instructorStudentItem;
    }

    private SelectItem[] getLanguages() {
        List<EdsReference> references = referenceManager.listReferences(LANGUAGES);

        if (references != null && references.size() > 0) {
            SelectItem[] languages = new SelectItem[references.size()];
            for (int i = 0; i < references.size(); i++) {
                languages[i] = new SelectItem(references.get(i).getObjectID(), references.get(i).getName());
            }

            return languages;
        }

        return new SelectItem[0];
    }

    /**
     * Save scheduled course instructor for each day
     *
     * @param courseSchedule
     */
    private void updateScheduledCourseInstructors(EdsCourseSchedule courseSchedule) {
        EdsUser user = employeeManager.getUser();
        int timeZoneRawOffset = user.getUserTimezone().getRawOffset() / 60000;

        //clear old instructors of the scheduled course
        scheduledCourseManager.deleteScheduledCourseInstructors(courseSchedule.getObjectID());

        if (courseSchedule.getInstructor() == null) {
            return;
        }

        //get time slot of the Schedule course Instructor/Company
        HashMap<Integer, TimeSlotItem> timeSlotItems = getTimeSlotItem(courseSchedule.getLocation().getObjectID());

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(courseSchedule.getStartDate());
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);

        //optional params
        Long sTime = (courseSchedule.getStartDate().getTime() - startTime.getTime().getTime()) / 60000;
        Integer scStartTime = sTime.intValue();  //start course schedule time
        Integer cDuration = courseSchedule.getCourse().getDuration() * 60;    //course duration time
        int duration = 0;
        int i = 0;
//        do {
//            startTime.add(Calendar.DAY_OF_MONTH, i != 0 ? 1 : i);
//            Calendar endTime = (Calendar) startTime.clone();
//            TimeSlotItem timeSlotItem = timeSlotItems.get(startTime.get(Calendar.DAY_OF_WEEK) - 1);
//
//            int tsStartTime = timeSlotItem.getStartTime() - timeZoneRawOffset; //time slot start time
//            int tsEndTime = timeSlotItem.getEndTime() - timeZoneRawOffset; //time slot end time
//            int tsLunchStart = timeSlotItem.getLunchStart() - timeZoneRawOffset;//time slot lunch start time
//            int tsCoffeeStart = timeSlotItem.getCoffeeStart() - timeZoneRawOffset;//time slot coffee start time
//
//            if (timeSlotItem.getStartTime() != 0 && timeSlotItem.getEndTime() != 0) {
//                int totalDurationInDay = 0; //total duration in day
//                Integer scTime = scStartTime; //scheduled course start time
//
//                //calculate lunch time from time slot
//                int lunchTime = timeSlotItem.getLunchEnd() - timeSlotItem.getLunchStart();
//
//                //calculate break time from time slot
//                int breakTime = timeSlotItem.getCoffeeEnd() - timeSlotItem.getCoffeeStart();
//
//                if (scTime != 0) {
//                    duration = scTime + cDuration;
//                    startTime.add(Calendar.MINUTE, scTime);
//
//                    //lunch time applying to schedule duration
//                    if (scTime <= tsLunchStart && duration > tsLunchStart) {
//                        totalDurationInDay += lunchTime;
//                    }
//
//                    //break time applying to schedule duration
//                    if (scTime <= tsCoffeeStart && (duration + lunchTime) > tsCoffeeStart) {
//                        totalDurationInDay += breakTime;
//                    }
//
//                    totalDurationInDay += duration;
//
//                    scTime = 0;
//                } else {
//                    duration = tsStartTime + cDuration;
//                    startTime.add(Calendar.MINUTE, tsStartTime);
//                    totalDurationInDay = duration + lunchTime + breakTime;
//                }
//
//                EdsInstructorScheduledCourse instructorScheduledCourse = new EdsInstructorScheduledCourse();
//                instructorScheduledCourse.setDate(startTime.getTime());
//                instructorScheduledCourse.setInstructor(courseSchedule.getInstructor());
//                instructorScheduledCourse.setCourseSchedule(courseSchedule);
//
//                if (tsEndTime < totalDurationInDay) {
//                    cDuration = totalDurationInDay - tsEndTime;
//                    endTime.add(Calendar.MINUTE, tsEndTime);
//                } else {
//                    //apply lunch time to duration in day
//                    if (scStartTime <= tsLunchStart && duration > tsLunchStart) {
//                        duration += lunchTime;
//                    }
//
//                    //apply break time to duration in day
//                    if (scStartTime <= tsCoffeeStart && (duration + lunchTime) > tsCoffeeStart) {
//                        duration += breakTime;
//                    }
//
//                    endTime.add(Calendar.MINUTE, duration);
//                    cDuration = 0;
//                }
//                instructorScheduledCourse.setEndTime(endTime.getTime());
//                courseSchedule.getInstructorScheduledCourses().add(instructorScheduledCourse);
//
//                startTime.set(Calendar.HOUR_OF_DAY, 0);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.SECOND, 0);
//                startTime.set(Calendar.MILLISECOND, 0);
//            }
//
//            i++;
//        } while (cDuration > 0);
        EdsInstructorScheduledCourse instructorScheduledCourse = new EdsInstructorScheduledCourse();
        instructorScheduledCourse.setDate(courseSchedule.getStartDate());
        instructorScheduledCourse.setInstructor(courseSchedule.getInstructor());
        instructorScheduledCourse.setCourseSchedule(courseSchedule);
        instructorScheduledCourse.setEndTime(courseSchedule.getEndDate());
        courseSchedule.getInstructorScheduledCourses().add(instructorScheduledCourse);

    }

    private void updateScheduledCourseReservations(EdsCourseSchedule courseSchedule, ScheduledCourseItem scheduledCourseItem) {
        //before clear old reservations
        scheduledCourseManager.deleteScheduledCourseReservations(courseSchedule.getObjectID());
        courseSchedule.getReservations().clear();

        if (scheduledCourseItem.getReservations() != null) {
            scheduledCourseItem.getReservations();
            for (ScheduledCourseReservation reservation : scheduledCourseItem.getReservations()) {
                EdsBookingItemReservation itemReservation = new EdsBookingItemReservation();
                itemReservation.setBookingItem(bookingItemManager.get(reservation.getItemID()));
                itemReservation.setFrom(courseSchedule.getStartDate());
                itemReservation.setTo(courseSchedule.getEndDate());
                itemReservation.setReservedBy(scheduledCourseManager.getUser());
                reservationManager.create(itemReservation);
                courseSchedule.getReservations().add(itemReservation);
            }
        }
    }

    private void createEventByCourseSchedule(EdsCourseSchedule courseSchedule) {
        Appointment appointment = new Appointment();
        appointment.setObjectID(courseSchedule.getEventID());
        appointment.setSubject(generateEventSubject(courseSchedule));
        appointment.setLocation(courseSchedule.getLocationAsString());
        appointment.setStartDate(courseSchedule.getStartDate());
        appointment.setEndDate(courseSchedule.getEndDate());
        appointment.setAction(Appointment.ADD_NEW_EVENT);
        appointment.setDescription(generateEventDescription(courseSchedule));

        if (courseSchedule.getInstructor() != null && courseSchedule.getInstructor().getObjectID() != null) {
            ArrayList<Attendee> attendees = new ArrayList<>();
            attendees.add(new Attendee(courseSchedule.getInstructor().getObjectID(), true));
            appointment.setAttendees(attendees);
        }
        if (courseSchedule.getLocation() != null) {
            appointment.setLocationId(courseSchedule.getLocation().getObjectID());
        }
        SelectItem result = googleCalendarServiceLocal.saveCalendarEvent(null, appointment, false);
        courseSchedule.setEventID(result.getId());
    }

    private String generateEventSubject(EdsCourseSchedule courseSchedule) {
        EdsCourse course = courseSchedule.getCourse();
        EdsEmployee instructor = courseSchedule.getInstructor();

        StringBuilder builder = new StringBuilder();
        builder.append(courseSchedule.getNumber());
        builder.append(" ").append(course.getNumber());
        if (courseSchedule.getClassRoom() != null) {
            builder.append(" - ").append(courseSchedule.getClassRoom());
        }

        if (instructor != null) {
            builder.append(" - ").append(instructor.getFullName());
        }
        builder.append(" - ").append(courseSchedule.getLocationAsString());
        builder.append(" - ").append(courseSchedule.getLanguage().getName());

        return builder.toString();
    }

    private String generateEventDescription(EdsCourseSchedule courseSchedule) {
        StringBuilder builder = new StringBuilder();

        if (courseSchedule.getInstructor() != null) {
            builder.append("Instructor: ").append(courseSchedule.getInstructor().getFullName()).append("; \n");
        }
        if (courseSchedule.getReservations() != null && courseSchedule.getReservations().size() > 0) {
            builder.append("Course Requirements:\n");

            for (EdsBookingItemReservation reservation : courseSchedule.getReservations()) {
                EdsBookingItem bookingItem = reservation.getBookingItem();
                builder.append(bookingItem.getCategory().getName()).append(" - ");
                builder.append(bookingItem.getName()).append("; \n");
            }
        }

        return builder.toString();
    }


    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCourseStudents(ListingFilterParameter fp) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = new LinkedHashMap<>();
        List<EdsStudent> studentList = new ArrayList<>();
        studentList = studentManager.getStudentList(fp);
        ArrayList<EdsStudent> addedStudentList = new ArrayList<>();
        addedStudentList = (ArrayList<EdsStudent>) studentManager.getScheduledCourseStudents(fp.getObjectId(), fp);
        KpiTreeInfo parent = new KpiTreeInfo(0, "Students");
        assigneeList.put(parent, new ArrayList<>());
        for (EdsStudent resEmployee : studentList) {
            KpiTreeInfo sItem = new KpiTreeInfo(resEmployee.getObjectID(), resEmployee.getFullName());
            sItem.setEmployeeId(resEmployee.getObjectID());
            sItem.setDepartmentId(0);
            sItem.setDepartmentName("Students");
            if (addedStudentList.contains(resEmployee)) {
                sItem.setSelected(true);
            }
            assigneeList.get(parent).add(sItem);
        }
        return assigneeList;
    }

    @Override
    public SelectItem[] getInstructorList() {
        List<EdsEmployee> allInstructors = userManager.getUsersByROLE(userManager.getUser().getCompany().getObjectID(), roleManager.getByCode(INSTRUCTOR).getObjectID());
        if (allInstructors != null && allInstructors.size() > 0) {
            List<SelectItem> instructorList = new ArrayList<>();
            for (EdsEmployee instructor : allInstructors) {
                instructorList.add(new SelectItem(instructor.getObjectID(), instructor.getFullName()));
            }

            return instructorList.toArray(new SelectItem[]{});
        }
        return new SelectItem[0];
    }

    @Override
    public SelectItem[] getAssessorList() {
        List<EdsEmployee> allAssessors = userManager.getUsersByROLE(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), roleManager.getByCode(ASSESSOR).getObjectID());

        List<SelectItem> assessors = new ArrayList<>();
        if (allAssessors != null && allAssessors.size() > 0) {
            for (EdsEmployee assessor : allAssessors) {
                assessors.add(new SelectItem(assessor.getObjectID(), assessor.getFullName()));
            }
        }

        List<EdsEmployee> allInstructors = userManager.getUsersByROLE(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), roleManager.getByCode(INSTRUCTOR).getObjectID());
        if (allInstructors != null && allInstructors.size() > 0) {
            for (EdsEmployee instructor : allInstructors) {
                SelectItem item = new SelectItem(instructor.getObjectID(), instructor.getFullName());
                if (!assessors.contains(item)) {
                    assessors.add(item);
                }
            }
        }

        return assessors.toArray(new SelectItem[]{});
    }

    @Override
    public ArrayList<InstructorScheduledCourseItem> getScheduledCourseInstructors(Integer scheduledCourseID) {
        List<InstructorScheduledCourseItem> ints = scheduledCourseManager.getScheduledCourseInstructors(scheduledCourseID);
        ArrayList<InstructorScheduledCourseItem> instructorList = new ArrayList<>();

        int inc = 1;
        for (InstructorScheduledCourseItem item : ints) {
            item.setOrder(inc);
            instructorList.add(item);
            inc++;
        }
        return instructorList;
    }

    @Override
    public void updateScheduledCourseInstructors(InstructorScheduledCourseItem[] instructors) {
        for (InstructorScheduledCourseItem item : instructors) {
            EdsInstructorScheduledCourse instructorScheduledCourse = (EdsInstructorScheduledCourse) scheduledCourseManager.findSingle("SELECT isch FROM EdsInstructorScheduledCourse isch WHERE isch.objectID = '" + item.getObjectID() + "' ");
            instructorScheduledCourse.setInstructor(employeeManager.get(item.getInstructorID()));
            instructorScheduleCourseManager.merge(instructorScheduledCourse);
        }
    }

    @Override
    public void saveAttendanceSheet(InstructorStudentItem instructorStudentItem) {
        InstructorItem instructorItem = instructorStudentItem.getInstructorItems();
        EdsInstructorScheduledCourse edsInstructorScheduledCourse = instructorScheduleCourseManager.get(instructorItem.getInsSchCourseId());
        edsInstructorScheduledCourse.setAttended(instructorItem.isAttended());
        edsInstructorScheduledCourse.setApproved(instructorItem.isApproved());
        EdsCourseSchedule edsCourseSchedule = edsInstructorScheduledCourse.getCourseSchedule();
        List<EdsStudent> edsStudentList = courseBookingManager.getStudentListByCourseScheduleId(edsCourseSchedule.getObjectID());
        Map<Integer, EdsStudent> edsStudentMap = new HashMap<>();
        for (EdsStudent edsStudent : edsStudentList) {
            edsStudentMap.put(edsStudent.getObjectID(), edsStudent);
        }
        List<StudentItem> oldCreateStudentAttendedItems = new ArrayList<>();
        List<StudentItem> studentItemsList = instructorStudentItem.getStudentItems();
        for (StudentItem studentItem : studentItemsList) {
            if (studentItem.getStudentAttendedId() == null) {
                createStudentAttendedForScheduleCourse(studentItem, edsInstructorScheduledCourse, edsStudentMap);
            } else {
                oldCreateStudentAttendedItems.add(studentItem);
            }

            EdsCourseScheduleStudent courseScheduleStudent = courseScheduleStudentManager.getCourseScheduleStudent(edsCourseSchedule, studentItem.getObjectId());
            courseScheduleStudent.setAttendedStatus(referenceManager.get(studentItem.getAttendedStatusID()));
        }

        studentAttendedManager.updateStudentAttended(oldCreateStudentAttendedItems, instructorItem.getInsSchCourseId());
        instructorScheduleCourseManager.update(edsInstructorScheduledCourse);
    }

    @Override
    public SelectItem[] getSheduleCourseInstructorsByDate(Integer locationId, Date date) {
        List<EdsEmployee> instructorList = instructorScheduleCourseManager.getInstructorByDate(locationId, date);
        SelectItem[] instructorItems = new SelectItem[instructorList.size()];
        int i = 0;
        for (EdsEmployee instrustor : instructorList) {
            instructorItems[i++] = new SelectItem(instrustor.getObjectID(), instrustor.getName());
        }
        return instructorItems;
    }

    @Override
    public CrmAccountItem getCustomerData(Integer customerID) {
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(customerID);
        CrmAccountItem crmAccountItem = edsCrmAccount.getRPC(new CrmAccountItem(), true);
        List<EdsTrainingContract> edsTrainingContractList = trainingContractManager.getKeyClientList(edsCrmAccount, new Date());
        if (edsTrainingContractList.size() > 0) {
            crmAccountItem.setKeyClient(true);
        }
        return crmAccountItem;
    }

    @Override
    public ClientContact getContactData(Integer contactID) {
        EdsCrmContact edsCrmContact = crmContactManager.get(contactID);
        return edsCrmContact.getAsClientContact();
    }

    @Override
    public AddEditCourseBookingItem getCourseBookingAddEditData(Integer courseBookingObjectID) {
        AddEditCourseBookingItem courseBookingItem = new AddEditCourseBookingItem();
        courseBookingItem.setLocationItems(locationManager.getLocationsAsSelectItems(new ListingFilterParameter()));
        List<EdsReference> rTypeList = referenceManager.listReferences(COURSE_BOOKING_TYPE);

        if (rTypeList != null && rTypeList.size() > 0) {
            List<SelectItem> typeList = new ArrayList<>();
            HashMap<String, String> typeMap = new HashMap<>();
            for (EdsReference type : rTypeList) {
                typeList.add(type.getAsSelectItem());
                typeMap.put(type.getCode(), type.getName());
            }
            courseBookingItem.setTypeList(typeList.toArray(new SelectItem[]{}));
            courseBookingItem.setTypeMap(typeMap);
        }
        return courseBookingItem;
    }

    public CourseBookingItem getCourseBookingItem(Integer courseBookingObjectID) {
        EdsCourseBooking edsCourseBooking = courseBookingManager.get(courseBookingObjectID);
        CourseBookingItem courseBookingItem = edsCourseBooking.getRPC();
        if (edsCourseBooking.getIntNumber() != null) {
            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            if (settings != null && settings.getEnquiryNumberingFormat() != null) {
                courseBookingItem.setNumberData(settings.parseNumberData(edsCourseBooking.getIntNumber() != null ? edsCourseBooking.getIntNumber() - 1 : null, settings.getEnquiryNumberingFormat()));
            } else {
                courseBookingItem.setNumberData(EdsNumberingSettings.getDefaultData(edsCourseBooking.getIntNumber() != null ? edsCourseBooking.getIntNumber() - 1 : null, EdsNumberingSettings.DEF_COURSE_BOOKING_PREFIX));
            }
        }
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.CourseBooking);
        courseBookingItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsCourseBooking.getCustomFields(), customFieldsItems));
        return courseBookingItem;
    }

    public BookingItemForApprove getCourseBookingForConfirmation(Integer bookingID, Integer companyID) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }

        EdsCourseBooking courseBooking = courseBookingManager.get(bookingID);

        BookingItemForApprove bookingItemForApprove = new BookingItemForApprove();
        bookingItemForApprove.setNumber(courseBooking.getNumber());
        bookingItemForApprove.setStatusCode(courseBooking.getStatus().getCode());
        if (courseBooking.getCustomer() != null) {
            EdsCrmAccount customer = courseBooking.getCustomer();
            bookingItemForApprove.setCustomer(customer.getName());
            bookingItemForApprove.setCustomerNumber(customer.getRegistrationNumber());
        }

        if (courseBooking.getContact() != null) {
            EdsCrmContact contact = courseBooking.getContact();
            bookingItemForApprove.setContact(contact.getName());
        }

        if (courseBooking.getLocation() != null) {
            bookingItemForApprove.setLocation(courseBooking.getLocation().getAsSelectItem().getName());
        }

        if (courseBooking.getStudents() != null && courseBooking.getStudents().size() > 0) {
            ArrayList<CourseScheduleStudent> scheduleStudents = new ArrayList<>();
            for (EdsCourseScheduleStudent scheduleStudent : courseBooking.getStudents()) {
                EdsStudent student = scheduleStudent.getStudent();

                CourseScheduleStudent courseScheduleStudent = new CourseScheduleStudent();
                courseScheduleStudent.setCourse(scheduleStudent.getCourseScheduleBooking().getCourse().getNumber() + " " + scheduleStudent.getCourseScheduleBooking().getCourse().getName());

                EdsCompany company = companyManager.get(companyID);
                Date startDate = new Date(scheduleStudent.getCourseScheduleBooking().getStartDate().getTime() + company.getTimeZone().getRawOffset());
                courseScheduleStudent.setStartDate(ServerUtils.dateFormat(startDate, "dd MMM yyyy HH:mm"));
                courseScheduleStudent.setFirstName(student.getContact().getFirstName());
                courseScheduleStudent.setLastName(student.getContact().getLastName());
                courseScheduleStudent.setNumber(student.getNumber());
                courseScheduleStudent.setResidenceNumber(student.getSafetyPPNumber());
                courseScheduleStudent.setCompanyEmployeeNumber(student.getCompEmplNumber());

                scheduleStudents.add(courseScheduleStudent);
            }

            bookingItemForApprove.setScheduleStudents(scheduleStudents);
        }
        return bookingItemForApprove;
    }

    @Override
    public void updateCourseBookingStatus(Integer bookingID, String status) {
        updateCourseBookingStatus(bookingID, null, status);
    }

    @Override
    public CrmAccountItem getCustomerByRegistrationNumber(String registrationNumber) {
        EdsCrmAccount edsCrmAccount = crmAccountManager.findCustomerByRegistrationNum(registrationNumber);
        if (edsCrmAccount != null) {
            CrmAccountItem crmAccountItem = edsCrmAccount.getRPC(null, true);
            List<EdsTrainingContract> edsTrainingContractList = trainingContractManager.getKeyClientList(edsCrmAccount, new Date());
            if (edsTrainingContractList.size() > 0) {
                crmAccountItem.setKeyClient(true);
            }
            return crmAccountItem;
        }
        return null;
    }

    @Override
    public StudentItem findStudentByCompanyEmployeeNumber(String companyEmpNum, Integer courseBookingID, Integer locationID) {
        EdsCourseBooking edsCourseBooking = courseBookingManager.get(courseBookingID);
        EdsStudent edsStudent = studentManager.findStudentByCompanyEmplopyeeNum(companyEmpNum, edsCourseBooking.getCustomer());
        if (edsStudent != null) {
            StudentItem studentItem = edsStudent.getRPC();
            List<Object[]> objectsList = scheduledCourseManager.getStudentAttendedCourseSchedule(edsStudent.getObjectID(), locationID, new Date());
            for (Object[] objects : objectsList) {
                Integer courseShceduleId = (Integer) objects[0];
                Date startDate = (Date) objects[1];
                Date endDate = (Date) objects[2];

                CourseScheduleListItem courseScheduleItem = new CourseScheduleListItem();
                courseScheduleItem.setStartDate(startDate);
                courseScheduleItem.setEndDate(endDate);
                courseScheduleItem.setCourseScheduleId(courseShceduleId);
                courseScheduleItem.setCourseName((String) objects[3]);
                courseScheduleItem.setCourseCode((String) objects[4]);

                studentItem.getStudentCourseBookingItems().add(courseScheduleItem);
            }
            return studentItem;
        }
        return null;
    }

    @Override
    public Boolean validateStudentEmailToExisting(String email, Integer courseBookingID) {
        EdsCourseBooking edsCourseBooking = courseBookingManager.get(courseBookingID);
        return studentManager.validateExistingEmail(email, edsCourseBooking.getCustomer());
    }

    /**
     * Student Re Schedule functional
     *
     * @param studentID
     * @param fsID      - from schedule id
     * @param tsID      - to schedule id
     * @return
     */
    public boolean studentReschedule(Integer studentID, Integer fsID, Integer tsID) {
        EdsCourseSchedule fcs = scheduledCourseManager.get(fsID);
        EdsCourseSchedule tcs = null;
        EdsCourseScheduleStudent courseScheduleStudent = courseScheduleStudentManager.getCourseScheduleStudent(fcs, studentID);

        if (courseScheduleStudent != null) {
            tcs = scheduledCourseManager.get(tsID);
            courseScheduleStudent.setCourseScheduleBooking(tcs);
            courseScheduleStudentManager.update(courseScheduleStudent);

            EdsUser user = userManager.getUser();
            if (user == null) {
                user = userManager.getAdmins(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())).get(0);
            }
            baseEventPostProcessor.registerEvent(CourseBookingEventListenerImpl.TYPE_STUDENT_COURSE_SCHEDULE, CourseBookingEventListenerImpl.EVENT_STUDENT_COURSE_SCHEDULE_ATTENDED, courseScheduleStudent, user);
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, courseScheduleStudent, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_CS_STUDENT);
            try {
                solrManager.addCourseScheduleToIndex(fcs, tcs);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean studentDropOff(Integer courseScheduleID) {
        EdsCourseSchedule courseSchedule = scheduledCourseManager.get(courseScheduleID);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setScheduledCourseID(courseSchedule.getObjectID());
        fp.setCourseID(courseSchedule.getCourse().getObjectID());
        fp.setLocationId(courseSchedule.getLocation().getObjectID());
        fp.setLanguageID(courseSchedule.getLanguage().getObjectID());

        //available course schedule list
        List<EdsCourseSchedule> acsList = scheduledCourseManager.getAvailableCourseSchedule(fp);
        if (acsList == null || acsList.size() == 0) {
            return false;
        }

        List<EdsCourseScheduleStudent> droppableStudentList = courseScheduleStudentManager.getDroppableStudentList(courseSchedule.getObjectID());
        if (droppableStudentList == null || droppableStudentList.size() == 0) {
            return false;
        }

        return studentReschedule(droppableStudentList.get(0).getStudent().getObjectID(), courseSchedule.getObjectID(), acsList.get(0).getObjectID());
    }

    @Override
    public ScheduledCourseItem[] getAvailableScheduleCourseDates(Integer objectID) {
        EdsCourseSchedule courseSchedule = scheduledCourseManager.get(objectID);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setScheduledCourseID(courseSchedule.getObjectID());
        fp.setCourseID(courseSchedule.getCourse().getObjectID());
        fp.setLocationId(courseSchedule.getLocation().getObjectID());
        //fp.setLanguageID(courseSchedule.getLanguage().getObjectID());
        List<EdsCourseSchedule> availableScheduledCourse = scheduledCourseManager.getAvailableCourseSchedule(fp);

        if (availableScheduledCourse != null && availableScheduledCourse.size() > 0) {
            ArrayList<ScheduledCourseItem> items = new ArrayList<>();
            for (EdsCourseSchedule acs : availableScheduledCourse) {
                items.add(acs.getRPC());
            }

            return items.toArray(new ScheduledCourseItem[]{});
        }
        return new ScheduledCourseItem[0];
    }

    @Override
    public void saveCSCEditCellValue(ScheduledCourseItem rowValue, String columnCodeName) {
        try {
            EdsCourseSchedule courseSchedule = scheduledCourseManager.get(rowValue.getObjectID());
            if (ScheduledCourseItem.TEST_OPTION.equals(columnCodeName)) {
                courseSchedule.setTestOption(rowValue.getTestOption());
            } else if (ScheduledCourseItem.ASSESSOR.equals(columnCodeName)) {
                courseSchedule.setAssessor(employeeManager.get(rowValue.getAssessorID()));
            } else if (ScheduledCourseItem.COURSE_SCHEDULE_STATUS.equals(columnCodeName)) {
                courseSchedule.setStatus(referenceManager.get(rowValue.getStatusID()));
                if (CS_DELIVERED.equals(courseSchedule.getStatus().getCode()) && courseSchedule.getAssessor() == null && courseSchedule.getInstructor() != null) {
                    EdsRole edsRole = roleManager.getByCode(ASSESSOR);
                    if (edsRole != null) {
                        if (!courseSchedule.getInstructor().hasRole(edsRole)) {
                            EdsEmployee instructor = courseSchedule.getInstructor();
                            instructor.addRole(edsRole);
                            try {
//                                solrManager.addEmployeeToIndex(instructor);
                                employeeSolrComponent.index(instructor);
                            } catch (SolrServerException | IOException | SolrException e) {
                                e.printStackTrace();
                            }
                        }
                        courseSchedule.setAssessor(courseSchedule.getInstructor());
                    }
                }
            }

            scheduledCourseManager.update(courseSchedule);
            solrManager.addCourseScheduleToIndex(courseSchedule);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean reGenerateScheduledCourseEvents(Integer listStart, Integer listLimit) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(listStart);
        fp.setLimit(listLimit);
        List<EdsCourseSchedule> courseScheduleList = scheduledCourseManager.list(fp);

        if (courseScheduleList != null && courseScheduleList.size() > 0) {
            for (EdsCourseSchedule courseSchedule : courseScheduleList) {
                courseSchedule = scheduledCourseManager.get(courseSchedule.getObjectID());
                createEventByCourseSchedule(courseSchedule);
                scheduledCourseManager.flushAndClear();
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * This is temporary method for schedules times fixing
     */
    @Override
    public boolean reGenerateScheduledCourseTimes(Integer listStart, Integer listLimit) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(listStart);
        fp.setLimit(listLimit);

        List<EdsCourseSchedule> courseScheduleList = scheduledCourseManager.list(fp);
        if (courseScheduleList != null && courseScheduleList.size() > 0) {
            for (EdsCourseSchedule courseSchedule : courseScheduleList) {
                courseSchedule = scheduledCourseManager.get(courseSchedule.getObjectID());

                //initialize calculation end date of schedule
                initCalculationEndDate(courseSchedule);

                //update schedule's instructor times
                if (courseSchedule.getInstructor() != null && courseSchedule.getInstructor().getObjectID() != null) {
                    updateScheduledCourseInstructors(courseSchedule);
                }

                //create event for schedule
                createEventByCourseSchedule(courseSchedule);

                scheduledCourseManager.update(courseSchedule);

                System.out.println(courseSchedule.getNumber());
                scheduledCourseManager.flushAndClear();
            }
        } else {
            return false;
        }

        return true;
    }

    @Override
    public String importStudent(Integer objectId, Integer attachmentID) {
        String result = "";
        EdsAttachment attachment = attachmentManager.get(attachmentID);
        InputStream inputStream = uploadManager.getInputStream(attachment);
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
        }
        CSVReader reader = new CSVReader(isr, ',');
        try {
            List<String[]> allCSVLine = reader.readAll();
            String className = allCSVLine.get(1)[1];
            EdsCourseSchedule courseSchedule = scheduledCourseManager.getScheduledCourseByNumber(className);
            if (courseSchedule.getObjectID() == objectId) {
                Integer notimporttant = 0;
                for (int i = 4; i < allCSVLine.size(); i++) {
                    Integer studentID = Integer.valueOf(allCSVLine.get(i)[2]);
                    EdsCourseScheduleStudent edsCourseScheduleStudent = courseScheduleStudentManager.getCourseScheduleStudent(courseSchedule, studentID);
                    if (edsCourseScheduleStudent != null) {
                        edsCourseScheduleStudent.setGrade(allCSVLine.get(i)[3]);
                        edsCourseScheduleStudent.setPoints(allCSVLine.get(i)[5]);
                        courseScheduleStudentManager.update(edsCourseScheduleStudent);

                    } else {
                        notimporttant++;
                    }
                }
                result = "You have successfully imported a csv file";
                if (notimporttant > 0) {
                    result = "Some students not updated";
                }
            } else {
                new Throwable("You file imported other Schedule Course!");
            }


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return result;
    }

    @Override
    public SelectItem[] getAttendStudentStatus() {
        List<EdsReference> examStatus = referenceManager.listReferences(EdsCourseSchedule.STUDENT_COURSE_STATUS);
        return commonServiceLocal.referenceSelectItemWithPleaseSelect(examStatus);
    }

    @Override
    public SelectItem[] getAttendStatusList() {
        List<EdsReference> attendedStatusList = referenceManager.listReferences(EdsCourseSchedule.STUDENT_ATTENDED_STATUS);
        return commonServiceLocal.referenceSelectItemWithPleaseSelect(attendedStatusList);
    }

    @Override
    public SelectItem[] getCourseScheduleStatusList() {
        List<EdsReference> courseScheduleStatusList = referenceManager.listReferences(CS_DELIVERED_STATUS);
        return commonServiceLocal.referenceSelectItemWithPleaseSelect(courseScheduleStatusList);
    }

    @Override
    @Transactional
    public void saveAttendStudentEditCellValue(StudentItem rowValue, Integer scheduledCourseID, String columnCodeName) {
        EdsCourseSchedule courseSchedule = scheduledCourseManager.get(scheduledCourseID);
        EdsCourseScheduleStudent edsCourseScheduleStudent = courseScheduleStudentManager.getCourseScheduleStudent(courseSchedule, rowValue.getObjectId());
        if (StudentItem.STUDENT_EXAM_STATUS.equals(columnCodeName)) {
            edsCourseScheduleStudent.setExamStatus(referenceManager.get(rowValue.getExamStatusId()));
        } else if (StudentItem.STUDENT_GRADE_COLUMN.equals(columnCodeName)) {
            if (rowValue.getGrade() != null && !rowValue.getGrade().isEmpty()) {
                edsCourseScheduleStudent.setGrade(rowValue.getGrade());
            }
        } else if (StudentItem.STUDENT_POINTS.equals(columnCodeName)) {
            if (rowValue.getPoints() != null && !rowValue.getPoints().isEmpty()) {
                edsCourseScheduleStudent.setPoints(rowValue.getPoints());
            }
        } else if (StudentItem.STUDENT_ATTENDED_STATUS.equals(columnCodeName)) {
            if (rowValue.getAttendedStatusID() != null) {
                edsCourseScheduleStudent.setAttendedStatus(referenceManager.get(rowValue.getAttendedStatusID()));
            } else {
                edsCourseScheduleStudent.setAttendedStatus(null);
            }


            Integer countOfNotAddressedStudent = scheduledCourseManager.countOfNotAddressedStudents(courseSchedule.getObjectID());
            if (countOfNotAddressedStudent == 0) {
                courseSchedule.setStatus(referenceManager.findReference(CS_DELIVERED_STATUS, CS_DELIVERED));

                if (courseSchedule.getAssessor() == null && courseSchedule.getInstructor() != null) {
                    EdsRole edsRole = roleManager.getByCode(ASSESSOR);
                    if (edsRole != null) {
                        if (!courseSchedule.getInstructor().hasRole(edsRole)) {
                            EdsEmployee instructor = courseSchedule.getInstructor();
                            instructor.addRole(edsRole);
                            try {
//                                solrManager.addEmployeeToIndex(instructor);
                                employeeSolrComponent.index(instructor);
                            } catch (SolrServerException | IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        courseSchedule.setAssessor(courseSchedule.getInstructor());
                    }
                }

                try {
                    solrManager.addCourseScheduleToIndex(courseSchedule);
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        courseScheduleStudentManager.update(edsCourseScheduleStudent);

    }

    @Override
    public void updateCourseBookingStatus(Integer bookingID, Integer companyID, String status) {
        boolean isFromClient = false;
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }

        EdsCourseBooking courseBooking = courseBookingManager.get(bookingID);
        String bookingOldStatus = courseBooking.getStatus().getCode();
        EdsReference bookingStatus = referenceManager.findReference(COURSE_BOOKING_STATUS, status);
        courseBooking.setStatus(bookingStatus);

        updateBookingStudents(courseBooking);
        courseBookingManager.update(courseBooking);

        EdsUser user = userManager.getUser();
        if (user == null) {
            user = userManager.getAdmins(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())).get(0);
            isFromClient = true;
        }

        EdsBusinessEvent event = null;
        if (BOOKING_SUBMITTED_TO_MANAGER.equals(courseBooking.getStatus().getCode())) {
            event = baseEventPostProcessor.registerEvent(CourseBookingEventListenerImpl.TYPE, CourseBookingEventListenerImpl.EVENT_SUBMIT_FOR_APPROVAL, courseBooking, user);
            if (courseBooking.getContact() != null) {
                event.setCustomStringField(courseBooking.getContact().getPrimaryEmail());
            }
        } else if (BOOKING_APPROVED.equals(courseBooking.getStatus().getCode())) {
            courseBooking.setUpdater(!isFromClient ? user : null);
            baseEventPostProcessor.registerEvent(CourseBookingEventListenerImpl.TYPE, CourseBookingEventListenerImpl.EVENT_APPROVE_BOOKING, courseBooking, user);
        } else if (BOOKING_REJECTED.equals(courseBooking.getStatus().getCode())) {
            courseBooking.setUpdater(!isFromClient ? user : null);
            baseEventPostProcessor.registerEvent(CourseBookingEventListenerImpl.TYPE, CourseBookingEventListenerImpl.EVENT_REJECT_BOOKING, courseBooking, user);
        } else if (BOOKING_CONFIRMED.equals(courseBooking.getStatus().getCode())) {
        }

        //course booking index to solr
        try {
            solrManager.addCourseBookingToIndex(courseBooking);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void updateCourseBooking(CourseBookingItem courseBookingItem) {
        if (courseBookingItem != null && courseBookingItem.getObjectID() != null) {
            EdsCourseBooking courseBooking = courseBookingManager.get(courseBookingItem.getObjectID());
            if (courseBookingItem.getTypeCode() != null && !courseBookingItem.getTypeCode().isEmpty()) {
                courseBooking.setType(referenceManager.findReference(COURSE_BOOKING_TYPE, courseBookingItem.getTypeCode()));
            }

            if (courseBookingItem.getStatusCode() != null && !courseBookingItem.getStatusCode().isEmpty()) {
                updateCourseBookingStatus(courseBookingItem.getObjectID(), courseBookingItem.getStatusCode());
            }
        }
    }

    private void updateBookingStudents(EdsCourseBooking courseBooking) {
        if (courseBooking.getStudents() != null && courseBooking.getStudents().size() > 0) {
            EdsReference attended = referenceManager.findReference(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_PARENT_STATUS, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_ATTENDED);
            EdsReference rejected = referenceManager.findReference(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_PARENT_STATUS, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED);

            List<EdsCourseSchedule> listOfCourseSchedule = new ArrayList<>();
            for (EdsCourseScheduleStudent scheduleStudent : courseBooking.getStudents()) {
                if (courseBooking.getStatus() != null && (BOOKING_APPROVED.equals(courseBooking.getStatus().getCode()) || BOOKING_PAID.equals(courseBooking.getStatus().getCode()))) {
                    scheduleStudent.setStatus(attended);
                } else if (courseBooking.getStatus() != null && BOOKING_REJECTED.equals(courseBooking.getStatus().getCode())) {
                    scheduleStudent.setStatus(rejected);
                }

                if (!listOfCourseSchedule.contains(scheduleStudent.getCourseScheduleBooking())) {
                    listOfCourseSchedule.add(scheduleStudent.getCourseScheduleBooking());
                }
            }

            if (listOfCourseSchedule != null && listOfCourseSchedule.size() > 0) {
                try {
                    solrManager.addCourseScheduleToIndex(listOfCourseSchedule.toArray(new EdsCourseSchedule[]{}));
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    @Override
    public ScheduledCourseItem getAvailabilityData(ScheduledCourseItem scheduledCourseItem) {
        EdsCourse course = courseManager.get(scheduledCourseItem.getCourseID());
        scheduledCourseItem.setDuration(course.getDuration());

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(scheduledCourseItem.getStartDate());
        fp.setEndDate(scheduledCourseItem.getEndDate());
        fp.setLocationId(scheduledCourseItem.getLocationID());
        fp.setCourseID(course.getObjectID());
        fp.setLanguageID(scheduledCourseItem.getLanguageID());
        fp.setScheduledCourseID(scheduledCourseItem.getObjectID());

        List<EdsEmployee> availableInstructors = scheduledCourseManager.getAvailableInstructors(fp);
        List<EdsEmployee> nonAvailableInstructors = scheduledCourseManager.getInstructors(fp);
        nonAvailableInstructors.removeAll(availableInstructors);

        if (availableInstructors != null && availableInstructors.size() > 0) {
            List<SelectItem> instructorList = new ArrayList<>();
            for (EdsEmployee instructor : availableInstructors) {
                if (instructor.getLocation() != null && fp.getLocationId() != null && !fp.getLocationId().equals(instructor.getLocation().getObjectID())) {
                    instructorList.add(new SelectItem(instructor.getObjectID(), instructor.getFullName() + "(" + instructor.getLocation().getCity() + ")"));
                } else {
                    instructorList.add(new SelectItem(instructor.getObjectID(), instructor.getFullName()));
                }
            }
            if (nonAvailableInstructors != null && nonAvailableInstructors.size() > 0) {
                for (EdsEmployee instructor : nonAvailableInstructors) {
                    if (instructor.getLocation() != null && fp.getLocationId() != null && !fp.getLocationId().equals(instructor.getLocation().getObjectID())) {
                        instructorList.add(new SelectItem(instructor.getObjectID(), instructor.getFullName() + "(" + instructor.getLocation().getCity() + ")", null, NOT_AVAILABLE));
                    } else {
                        instructorList.add(new SelectItem(instructor.getObjectID(), instructor.getFullName(), null, NOT_AVAILABLE));
                    }
                }
            }
            scheduledCourseItem.setInstructors(instructorList.toArray(new SelectItem[]{}));
        } else {
            scheduledCourseItem.setInstructors(null);
        }


        if (course.getCourseRequirements() != null && course.getCourseRequirements().size() > 0) {
            List<CourseRequirementItem> requirementItems = new ArrayList<>();
            for (EdsReference requirement : course.getCourseRequirements()) {
                CourseRequirementItem requirementItem = new CourseRequirementItem();
                requirementItem.setObjectID(requirement.getObjectID());
                requirementItem.setName(requirement.getName());

                fp.setCategoryID(requirement.getObjectID());
                fp.setReservationIds(scheduledCourseItem.getReservationAsCommoDelimited());
                List<EdsBookingItem> bookingItems = reservationManager.getAvailableBookingItems(fp);
                if (bookingItems != null && bookingItems.size() > 0) {
                    List<SelectItem> items = new ArrayList<>();
                    for (EdsBookingItem item : bookingItems) {
                        items.add(item.getAsSelectItem());
                    }

                    requirementItem.setItems(items.toArray(new SelectItem[]{}));
                }
                requirementItems.add(requirementItem);
            }

            scheduledCourseItem.setCourseRequirementItems(requirementItems.toArray(new CourseRequirementItem[]{}));
        } else {
            scheduledCourseItem.setCourseRequirementItems(null);
        }

        return scheduledCourseItem;
    }

    @Override
    public ScheduledCourseItem getAvailabilityData(Integer scheduledCourseID) {
        ScheduledCourseItem scheduleCourseItem = getCourseSchedule(scheduledCourseID, true);
        return getAvailabilityData(scheduleCourseItem);
    }

    @Override
    public Integer setCloneOfCourseSchedule(ScheduledCourseItem scheduledCourseItem) {
        EdsCourse course = courseManager.get(scheduledCourseItem.getCourseID());
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(scheduledCourseItem.getStartDate());
        fp.setEndDate(scheduledCourseItem.getEndDate());
        fp.setLocationId(scheduledCourseItem.getLocationID());
        fp.setCourseID(course.getObjectID());
        fp.setLanguageID(scheduledCourseItem.getLanguageID());
        fp.setScheduledCourseID(scheduledCourseItem.getObjectID());
        fp.setEmployeeId(scheduledCourseItem.getInstructorID());

        if (fp.getEmployeeId() != null) {
            //instructor check for available
            List<EdsEmployee> availableInstructors = scheduledCourseManager.getAvailableInstructors(fp);
            if (availableInstructors == null || availableInstructors.size() == 0) {
                return -2;
            }
        }

        //course requirements check for available
        if (scheduledCourseItem.getReservations() != null) {
            scheduledCourseItem.getReservations();
            for (ScheduledCourseReservation courseReservation : scheduledCourseItem.getReservations()) {
                fp.setCategoryID(courseReservation.getItemCategoryID());
                fp.setItemId(courseReservation.getItemID());

                List<EdsBookingItem> bookingItems = reservationManager.getAvailableBookingItems(fp);
                if (bookingItems == null || bookingItems.size() == 0) {
                    return -2;
                }
            }
        }

        scheduledCourseItem.setObjectID(null);
        return saveCourseSchedule(scheduledCourseItem);
    }

    @Override
    public CourseBookingItem getCourseListByCourseBooking(Integer courseBookingID) {
        EdsCourseBooking edsCourseBooking = courseBookingManager.get(courseBookingID);
        CourseBookingItem courseBookingItem = edsCourseBooking.getRPC();

        if (edsCourseBooking.getCustomer() == null) {
            return null;
        }
        // key client contracts
        List<EdsTrainingContract> edsTrainingContractList = trainingContractManager.getKeyClientList(edsCourseBooking.getCustomer(), new Date());
        Set<EdsCourse> edsCourseSet = new HashSet<>();
        int i = 0;
        for (EdsTrainingContract edsTrainingContract : edsTrainingContractList) {
            if (i == 0) {
                i++;
                courseBookingItem.setPrePaid(edsTrainingContract.getPrePaid() != null ? edsTrainingContract.getPrePaid() : false);
            }
            edsCourseSet.addAll(edsTrainingContract.getCourses());
        }
        ArrayList<Integer> keyClientCourseListId = new ArrayList<>();
        for (EdsCourse edsCourse : edsCourseSet) {
            keyClientCourseListId.add(edsCourse.getObjectID());
        }

        //course booking item
        if (edsTrainingContractList != null && edsTrainingContractList.size() > 0) {
            courseBookingItem.setKeyClient(true);
        }
        courseBookingItem.setMasterCardPaymentURL(getMasterCardPaymentURL(courseBookingID));

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);

        List<Object[]> courseObjectsData = scheduledCourseManager.getCourseListByLocation(courseBookingItem.getLocation().getId(), cal.getTime());
        Map<Integer, CourseListItem> courseListItemMap = new HashMap<>();
        Map<Integer, List<SelectItem>> languageMap = new HashMap<>();
        List<Integer> courseUniqNumber = new ArrayList<>();
        List<Integer> languageUniqNumber = new ArrayList<>();
        Integer courseId = null;
        for (Object[] objs : courseObjectsData) {
            if (courseId == null || !courseId.equals(objs[0])) {
                Integer id = (Integer) objs[0];
                String courseName = (String) objs[1];// course Name
                String courseNum = (String) objs[2]; // course Code

                courseId = id;
                courseListItemMap.put(courseId, new CourseListItem(id, courseNum + " " + courseName));
                courseListItemMap.get(courseId).setKeyClientCourseIds(keyClientCourseListId);
                languageMap.put(courseId, new ArrayList<>());
                courseUniqNumber.add(courseId);
            }

            Integer languageId = (Integer) objs[3];// course languageId
            String languageName = (String) objs[4];// course languageName
            if (!languageMap.get(courseId).contains(new CourseLanguageListItem(languageId, languageName))) {
                languageMap.get(courseId).add(new CourseLanguageListItem(languageId, languageName));
                if (!languageUniqNumber.contains(languageId)) {
                    languageUniqNumber.add(languageId);
                }
            }
        }

        List<CourseListItem> listItems = new LinkedList<>();

        for (Map.Entry<Integer, CourseListItem> entry : courseListItemMap.entrySet()) {
            Integer key = entry.getKey();
            listItems.add(entry.getValue());
        }

        listItems.sort(Comparator.comparing(SelectItem::getName));
        // Add Course pre Requisite course name list
        Map<Integer, String> courseOtherPrerequisiteMap = courseManager.getCourseOtherPrerequisite(ServerUtils.getAsCommoDelimited(new ArrayList<>(courseListItemMap.keySet()), "0"));
        Map<Integer, ArrayList<String>> preRequisiteCourseNameMap = courseManager.getPreRequisiteCourseNameMap(ServerUtils.getAsCommoDelimited(new ArrayList<>(courseListItemMap.keySet()), "0"));
        for (Integer preRequisteCourseId : preRequisiteCourseNameMap.keySet()) {
            if (courseListItemMap.containsKey(preRequisteCourseId)) {
                courseListItemMap.get(preRequisteCourseId).setPreRequisiteCourseName(preRequisiteCourseNameMap.get(preRequisteCourseId));
                if (courseOtherPrerequisiteMap.containsKey(preRequisteCourseId)) {
                    courseListItemMap.get(preRequisteCourseId).setOtherPreRequisite(courseOtherPrerequisiteMap.get(preRequisteCourseId));
                }
            }
        }

        for (Integer keyId : languageMap.keySet()) {
            courseListItemMap.get(keyId).setLanguageItem(languageMap.get(keyId).toArray(new CourseLanguageListItem[]{}));
        }
        getCourseScheduleValidateData(courseListItemMap, courseBookingItem.getLocation().getId(), courseUniqNumber, languageUniqNumber);

        courseBookingItem.setCourseListItemList(listItems.toArray(new CourseListItem[]{}));
        return courseBookingItem;
    }

    private void getCourseScheduleValidateData(Map<Integer, CourseListItem> courseListItemMap, Integer locationId, List<Integer> courseUniqNumber, List<Integer> languageUniqNumber) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);

        List<Object[]> courseScheduleBooking = scheduledCourseManager.getCourseScheduleBooking(locationId, ServerUtils.getAsCommoDelimited(courseUniqNumber, "0"), ServerUtils.getAsCommoDelimited(languageUniqNumber, "0"), cal.getTime());
        for (Object[] schCourseObjects : courseScheduleBooking) {
            Integer courseScheduleId = (Integer) schCourseObjects[0];
            Integer courseid = (Integer) schCourseObjects[1];
            Integer languageId = (Integer) schCourseObjects[2];
            Integer instructorId = (Integer) schCourseObjects[3];
            String instructorName = (String) schCourseObjects[4];
            Integer numOfSeats = (Integer) schCourseObjects[5];
            Date startDate = (Date) schCourseObjects[6];
            Date endDate = (Date) schCourseObjects[7];
            BigInteger attendStudentCount = (BigInteger) schCourseObjects[9];

            if (courseListItemMap.containsKey(courseid)) {
                for (CourseLanguageListItem languageItem : courseListItemMap.get(courseid).getLanguageItem()) {
                    if (languageItem.getId().equals(languageId)) {
                        CourseScheduleListItem courseScheduleListItem = new CourseScheduleListItem();
                        courseScheduleListItem.setCourseScheduleId(courseScheduleId);
                        courseScheduleListItem.setInstructoId(instructorId);
                        courseScheduleListItem.setInstructorName(instructorName != null ? instructorName : "N/A");
                        courseScheduleListItem.setAttendStudentCount(attendStudentCount.intValue());
                        courseScheduleListItem.setNumOfSeatsCount(numOfSeats);
                        courseScheduleListItem.setStartDate(startDate);
                        courseScheduleListItem.setEndDate(endDate);
                        if (!languageItem.getCourseScheduleListItemMap().containsKey(startDate)) {
                            languageItem.getCourseScheduleListItemMap().put(startDate, new HashMap<>());
                        }

                        EdsCourse course = courseManager.get(courseid);
                        courseScheduleListItem.setCourseName(course.getName());
                        courseScheduleListItem.setCourseCode(course.getNumber());
                        languageItem.getCourseScheduleListItemMap().get(startDate).put(courseScheduleListItem.getCourseScheduleId(), courseScheduleListItem);
                    }
                }
            }
        }
    }

    private void createStudentAttendedForScheduleCourse(StudentItem studentItem, EdsInstructorScheduledCourse edsInstructorScheduledCourse, Map<Integer, EdsStudent> edsStudentMap) {
        EdsStudentAttended edsStudentAttended = new EdsStudentAttended();
        edsStudentAttended.setAttended(studentItem.isAttended());
        edsStudentAttended.setStudent(edsStudentMap.get(studentItem.getObjectId()));
        edsStudentAttended.setInstructorScheduledCourse(edsInstructorScheduledCourse);
        studentAttendedManager.create(edsStudentAttended);
    }

    @Override
    public Integer[] saveCourseBooking(CourseBookingItem courseBookingData) {
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            edsUser = userManager.getAdmins(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())).get(0);
        }
        Integer customerId = null;
        EdsCourseBooking edsCourseBooking = new EdsCourseBooking();
        edsCourseBooking.setCustomFields(createCourseBookingCustomFields(edsCourseBooking.getCustomFields(), courseBookingData.getCustomFieldItems()));

        if (courseBookingData.getObjectID() != null) {
            edsCourseBooking = courseBookingManager.get(courseBookingData.getObjectID());
        } else {
            edsCourseBooking.setCreator(edsUser);
            edsCourseBooking.setCreationDate(new Date());
            courseBookingManager.create(edsCourseBooking);
        }

        if (courseBookingData.getNumberData() != null) {
            edsCourseBooking.setNumber(courseBookingData.getNumberData().getNumberString());
            edsCourseBooking.setIntNumber(courseBookingData.getNumberData().getIntNumber());
        } else {
            Integer intNumber = courseBookingManager.getCourseBookingLastIntNumber();
            if (intNumber == null) {
                intNumber = 0;
            }

            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            edsCourseBooking.setIntNumber(intNumber + 1);
            if (settings != null && settings.getEnquiryNumberingFormat() != null) {
                edsCourseBooking.setNumber(settings.parseNumberData(intNumber + 1, settings.getEnquiryNumberingFormat()).getNumberString());
            } else {
                edsCourseBooking.setNumber(EdsNumberingSettings.getDefaultData(intNumber + 1, EdsNumberingSettings.DEF_COURSE_BOOKING_PREFIX).getNumberString());
            }

        }
        if (courseBookingData.getLocation().getId() != null) {
            edsCourseBooking.setLocation(locationManager.get(courseBookingData.getLocation().getId()));
        }

        if (courseBookingData.getCustomer() != null && courseBookingData.getCustomer().getId() != null) {
            edsCourseBooking.setCustomer(crmAccountManager.get(courseBookingData.getCustomer().getId()));
        } else if (courseBookingData.getCustomerItems() != null) {
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            if (fs != null && fs.getCurrency() != null) {
                courseBookingData.getCustomerItems().setCurrencyId(fs.getCurrency().getObjectID());
            }
            //courseBookingData.getCustomerItems().setOwnerID(edsUser.getObjectID());
            courseBookingData.getCustomerItems().setSelectedOwners(Lists.newArrayList(edsUser.getAsSelectItem()));
            customerId = crmService.saveAccount(courseBookingData.getCustomerItems(), CrmAccountItem.CUSTOMER, edsUser.getObjectID(), false, false, false, true);
            edsCourseBooking.setCustomer(crmAccountManager.get(customerId));
        }

        EdsCrmAccount pdoCompany = crmAccountManager.get(33);
        if (edsCourseBooking.getCustomer() != null && pdoCompany != null && !pdoCompany.getObjectID().equals(edsCourseBooking.getCustomer().getObjectID())) {
            if (courseBookingData.isPDOCustomer()) {
                edsCourseBooking.getCustomer().setParent(pdoCompany);
                crmAccountManager.update(edsCourseBooking.getCustomer(), true);
            } else if (edsCourseBooking.getCustomer().getParent() != null && pdoCompany.getObjectID().equals(edsCourseBooking.getCustomer().getParent().getObjectID())) {
                edsCourseBooking.getCustomer().setParent(null);
                crmAccountManager.update(edsCourseBooking.getCustomer(), true);
            }
        }

        if (courseBookingData.getContact() != null && courseBookingData.getContact().getId() != null) {
            edsCourseBooking.setContact(crmContactManager.get(courseBookingData.getContact().getId()));
        }

        if (courseBookingData.getStatusCode() != null && !courseBookingData.getStatusCode().isEmpty()) {
            edsCourseBooking.setStatus(referenceManager.findReference(COURSE_BOOKING_STATUS, courseBookingData.getStatusCode()));
        }

        if (courseBookingData.getTypeID() != null) {
            edsCourseBooking.setType(referenceManager.get(courseBookingData.getTypeID()));
        } else if (courseBookingData.getTypeCode() != null) {
            edsCourseBooking.setType(referenceManager.getByCode(courseBookingData.getTypeCode()));
        }

        edsCourseBooking.setCustomFields(createCourseBookingCustomFields(edsCourseBooking.getCustomFields(), courseBookingData.getCustomFieldItems()));

        try {
            solrManager.addCourseBookingToIndex(edsCourseBooking);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
        return new Integer[]{edsCourseBooking.getObjectID(), courseBookingData.getLocation().getId()};
    }

    private EdsCourseBookingCustomFields createCourseBookingCustomFields(EdsCourseBookingCustomFields edsCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsCustomFields = new EdsCourseBookingCustomFields();
                courseBookingCFManager.create(edsCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCustomFields, customFieldItems);
            return edsCustomFields;
        }
        return null;
    }

    @Override
    public SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs, Integer type) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        if (type == LookUpConstants.STUDENT_ID) {
            filterParametrs.setLookUp(true);
            List<ContactListItem> studentList = studentManager.getStudentsForLookUp(filterParametrs);
            ArrayList<SelectItem> studentItems = new ArrayList<>(studentList.size());

            for (ContactListItem student : studentList) {
                studentItems.add(new SelectItem(student.getObjectId(), student.getFullName()));
            }

            return studentItems.toArray(new SelectItem[0]);
        } else if (type == LookUpConstants.COURSE_PASSED_STUDENT_ID) {
            return getCoursePassedStudentsAsSelectItem(filterParametrs);
        } else if (type == LookUpConstants.COURSE_ID) {
            return courseManager.getCoursesAsSelectItems(new ListingFilterParameter());
        } else if (type == LookUpConstants.LANGUAGE_ID) {
            return getLanguages();
        } else if (type == LookUpConstants.INSTRUCTOR_ID) {
            return getAllInstructors();
        } else if (type == LookUpConstants.ASSESSOR_ID) {
            return getAssessorList();
        } else {
            List<EdsCourseSchedule> list = scheduledCourseManager.list(filterParametrs);
            if (list != null && list.size() > 0) {
                SelectItem[] result = new SelectItem[list.size()];
                int i = 0;
                for (EdsCourseSchedule schedule : list) {
                    result[i++] = new SelectItem(schedule.getObjectID(), schedule.getNumber());
                }
                return result;
            }
        }
        return new SelectItem[0];
    }

    private static final int EXPIRE_MINUTE = 15;

    /**
     * set temporary lock of a seat for customer booking
     *
     * @param lockItem
     * @param prePaid
     * @return
     */
    public CourseScheduleListItem setTemporaryLock(Integer courseBookingID, CourseScheduleListItem lockItem, boolean prePaid) {
        EdsSeatTemporaryLock temporaryLock = temporaryLockManager.getLockByBookingID(courseBookingID);

        if (temporaryLock == null) {
            temporaryLock = new EdsSeatTemporaryLock();
        }

        temporaryLock.setBookingID(courseBookingID);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + EXPIRE_MINUTE);
        temporaryLock.setExpireDate(cal.getTime());
        temporaryLockManager.createOrUpdate(temporaryLock);

        Integer attendStudentCount = courseScheduleStudentManager.getCourseScheduleAttendCount(lockItem.getCourseScheduleId());
        //clear old temporary registration
        courseScheduleStudentManager.deleteTemporaryRegistration(courseBookingID, lockItem.getItemUUID());
        EdsCourseSchedule courseSchedule = scheduledCourseManager.get(lockItem.getCourseScheduleId());
        if (attendStudentCount < courseSchedule.getNumberOfSeats()) {
            if (courseSchedule != null || courseSchedule.getObjectID() != null) {
                EdsCourseScheduleStudent courseScheduleStudent = new EdsCourseScheduleStudent();
                courseScheduleStudent.setCourseScheduleBooking(courseSchedule);
                courseScheduleStudent.setCourseBooking(courseBookingManager.get(courseBookingID));
                courseScheduleStudent.setItemUUID(lockItem.getItemUUID());
                courseScheduleStudentManager.create(courseScheduleStudent);
            }
        }
        lockItem.setNumOfSeatsCount(courseSchedule.getNumberOfSeats());
        lockItem.setAttendStudentCount(attendStudentCount);
        if (prePaid && lockItem.getNumOfSeatsCount() <= lockItem.getAttendStudentCount()) {
            Integer droppableStudentCount = scheduledCourseManager.getCourseScheduleDroppableStudentCount(lockItem.getCourseScheduleId());
            lockItem.setDroppableStudentCount(droppableStudentCount);
        }
        return lockItem;
    }

    @Override
    public StudentItem findStudentByResidenneNum(String residenceNum, Integer courseBookingID, Integer locationID) {
        EdsCourseBooking edsCourseBooking = courseBookingManager.get(courseBookingID);
        EdsStudent edsStudent = studentManager.findStudentByResidenceNum(residenceNum, edsCourseBooking.getCustomer());
        if (edsStudent != null) {
            StudentItem studentItem = edsStudent.getRPC();
            List<Object[]> objectsList = scheduledCourseManager.getStudentAttendedCourseSchedule(edsStudent.getObjectID(), locationID, new Date());
            for (Object[] objects : objectsList) {
                Integer courseShceduleId = (Integer) objects[0];
                Date startDate = (Date) objects[1];
                Date endDate = (Date) objects[2];

                CourseScheduleListItem courseScheduleItem = new CourseScheduleListItem();
                courseScheduleItem.setStartDate(startDate);
                courseScheduleItem.setEndDate(endDate);
                courseScheduleItem.setCourseScheduleId(courseShceduleId);
                courseScheduleItem.setCourseName((String) objects[3]);
                courseScheduleItem.setCourseCode((String) objects[4]);

                studentItem.getStudentCourseBookingItems().add(courseScheduleItem);
            }
            return studentItem;
        }
        return null;
    }

    @Override
    public CourseBookingItem saveCourseBookingAttendedStudents(CourseBookingItem courseBookingItem) {
        try {
            // delete temporary lock
            temporaryLockManager.deleteByBookingID(courseBookingItem.getObjectID());
            courseScheduleStudentManager.deleteTemporaryRegistration(courseBookingItem.getObjectID());
            // save Course Booking Client Contact
            EdsCourseBooking edsCourseBooking = courseBookingManager.get(courseBookingItem.getObjectID());
            if (courseBookingItem.getContactItems() != null) {
                ContactListItem contactListItem = courseBookingItem.getContactItems();
                EdsUser edsUser = courseManager.getUser();
                if (contactListItem.getObjectId() != null) {
                    edsCourseBooking.setContact(crmContactManager.get(contactListItem.getObjectId()));
                } else {
                    contactListItem.getCrmAccount().setObjectId(edsCourseBooking.getCustomer() != null ? edsCourseBooking.getCustomer().getObjectID() : null);
                    Integer contactId = contactServiceLocal.saveContact(contactListItem, null, edsUser, true, true);
                    edsCourseBooking.setContact(crmContactManager.get(contactId));
                }
            }
            List<EdsTrainingContract> edsTrainingContractList = trainingContractManager.getKeyClientList(edsCourseBooking.getCustomer(), new Date());

            //course booking item
            if (edsTrainingContractList != null && edsTrainingContractList.size() > 0) {
                courseBookingItem.setKeyClient(true);
                courseBookingItem.setPrePaid(edsTrainingContractList.get(0).getPrePaid() != null ? edsTrainingContractList.get(0).getPrePaid() : false);
            }
            // save Student Item
            for (StudentItem studentItem : courseBookingItem.getStudentItems()) {
                studentItem.setCustomerID(edsCourseBooking.getCustomer() != null ? edsCourseBooking.getCustomer().getObjectID() : null);
                Integer studentID = saveStudentItem(studentItem);
                EdsStudent edsStudent = studentManager.get(studentID);
                for (CourseScheduleListItem courseScheduleListItem : studentItem.getStudentCourseBookingItems()) {
                    saveCourseScheduleStudentItems(courseScheduleListItem, edsCourseBooking, edsStudent, courseBookingItem.isPrePaid());
                }
            }

            //update course booking by status and type
            updateCourseBooking(courseBookingItem);

            courseBookingItem.setMasterCardPaymentURL(getMasterCardPaymentURL(courseBookingItem.getObjectID(), courseBookingItem.getUserDefinedUrl()));

            return courseBookingItem;
        } catch (Throwable throwable) {
            log.error("Unexpected exception:", throwable);
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public CourseBookingItem saveGymCourseBookingAttendedStudents(CourseBookingItem courseBookingItem) {
        try {
            // save Course Booking Client Contact
            EdsCourseBooking edsCourseBooking = courseBookingManager.get(courseBookingItem.getObjectID());
            if (courseBookingItem.getContactItems() != null) {
                ContactListItem contactListItem = courseBookingItem.getContactItems();
                EdsUser edsUser = courseManager.getUser();
                if (contactListItem.getObjectId() != null) {
                    edsCourseBooking.setContact(crmContactManager.get(contactListItem.getObjectId()));
                } else {
                    contactListItem.getCrmAccount().setObjectId(edsCourseBooking.getCustomer() != null ? edsCourseBooking.getCustomer().getObjectID() : null);
                    Integer contactId = contactServiceLocal.saveContact(contactListItem, null, edsUser, true, true);
                    edsCourseBooking.setContact(crmContactManager.get(contactId));
                }
            }
            List<EdsTrainingContract> edsTrainingContractList = trainingContractManager.getKeyClientList(edsCourseBooking.getCustomer(), new Date());

            //course booking item
            if (edsTrainingContractList != null && edsTrainingContractList.size() > 0) {
                courseBookingItem.setKeyClient(true);
                courseBookingItem.setPrePaid(edsTrainingContractList.get(0).getPrePaid() != null ? edsTrainingContractList.get(0).getPrePaid() : false);
            }
            // save Student Item
            for (StudentItem studentItem : courseBookingItem.getStudentItems()) {
                EdsStudent edsStudent = studentManager.get(studentItem.getObjectId());
                for (CourseScheduleListItem courseScheduleListItem : studentItem.getStudentCourseBookingItems()) {
                    saveCourseScheduleStudentItems(courseScheduleListItem, edsCourseBooking, edsStudent, courseBookingItem.isPrePaid());
                    try {
                        solrManager.addCourseScheduleToIndex(scheduledCourseManager.get(courseScheduleListItem.getCourseScheduleId()));
                    } catch (SolrServerException | IOException e) {
                        log.error("Solr indexing error", e);
                    }
                }
            }

            //update course booking by status and type
            updateCourseBooking(courseBookingItem);

            courseBookingItem.setMasterCardPaymentURL(getMasterCardPaymentURL(courseBookingItem.getObjectID(), courseBookingItem.getUserDefinedUrl()));

            return courseBookingItem;
        } catch (Throwable throwable) {
            log.error("Unexpected exception:", throwable);
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public CourseBookingItem getBookingStudentItems(Integer courseBookingID) {
        EdsCourseBooking courseBooking = courseBookingManager.get(courseBookingID);
        CourseBookingItem bookingRpc = courseBooking.getRPC();

        if (bookingRpc.getInvoiceID() != null) {
            bookingRpc.setInvoiceNumber(invoiceManager.get(bookingRpc.getInvoiceID()).getNumber());
        }

        //initialize course booking customer information
        if (courseBooking.getCustomer() != null) {
            EdsCrmAccount customer = courseBooking.getCustomer();
            CrmAccountItem crmAccountItem = customer.getRPC(null, false);
            bookingRpc.setCustomerItems(crmAccountItem);

            List keyClientList = trainingContractManager.getKeyClientList(customer.getObjectID(), new Date());
            if (keyClientList != null && keyClientList.size() > 0) {
                bookingRpc.setKeyClient(true);
            }
        }

        //initialize course booking manager information
        if (courseBooking.getContact() != null) {
            EdsCrmContact contact = courseBooking.getContact();
            ContactListItem contactListItem = contact.getRPC(null);
            bookingRpc.setContactItems(contactListItem);
        }

        //initialize course booking student list information
        List<EdsCourseScheduleStudent> bookingStudentList = null;
        if (BOOKING_REJECTED.equals(courseBooking.getStatus().getCode())) {
            bookingStudentList = courseScheduleStudentManager.getRejectedCourseBookingStudentList(courseBookingID);
        } else {
            bookingStudentList = courseScheduleStudentManager.getCourseScheduleStudentByBookingId(courseBookingID);
        }
        if (bookingStudentList != null && bookingStudentList.size() > 0) {
            ArrayList<StudentItem> studentItems = new ArrayList<>();
            for (EdsCourseScheduleStudent bt : bookingStudentList) {
                StudentItem studentItem = bt.getStudent().getRPC();

                if (bt.getStatus() != null) {
                    studentItem.setStatus(bt.getStatus().getName());
                }
                EdsCourseSchedule courseSchedule = bt.getCourseScheduleBooking();
                studentItem.setCourse(courseSchedule.getCourse().getNumber() + " " + courseSchedule.getCourse().getName());
                studentItem.setCourseSchedulerNumber(courseSchedule.getNumber());
                studentItem.setCourseScheduleID(courseSchedule.getObjectID());
                studentItem.setInstructor(courseSchedule.getInstructor() != null ? courseSchedule.getInstructor().getFullName() : null);
                studentItem.setLanguage(courseSchedule.getLanguage() != null && courseSchedule.getLanguage().getName() != null ? courseSchedule.getLanguage().getName() : "");
                studentItem.setCourseSchedulerStartDate(courseSchedule.getStartDate());
                studentItems.add(studentItem);
            }
            bookingRpc.setStudentItems(studentItems);
        }

        if (!PAID.equals(bookingRpc.getStatusCode())) {
            bookingRpc.setMasterCardPaymentURL(getMasterCardPaymentURL(courseBookingID));
        }
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.CourseBooking);
        bookingRpc.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(courseBooking.getCustomFields(), customFieldsItems));

        return bookingRpc;
    }

    private void saveCourseScheduleStudentItems(CourseScheduleListItem courseScheduleListItem, EdsCourseBooking edsCourseBooking, EdsStudent edsStudent, boolean prePaid) {
        EdsCourseScheduleStudent edsCourseScheduleStudent = new EdsCourseScheduleStudent();
        edsCourseScheduleStudent.setStudent(edsStudent);
        edsCourseScheduleStudent.setCourseBooking(edsCourseBooking);
        edsCourseScheduleStudent.setCourseScheduleBooking(scheduledCourseManager.get(courseScheduleListItem.getCourseScheduleId()));
        edsCourseScheduleStudent.setStatus(referenceManager.findReference(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_PARENT_STATUS, EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_PENDING));
        edsCourseScheduleStudent.setDroppable(!prePaid);
        courseScheduleStudentManager.create(edsCourseScheduleStudent);
    }

    public HashMap<Integer, TimeSlotItem> getTimeSlotItem(Integer locationID) {
        EdsTimeSlot timeSlot = null;
        EdsCompany company = null;

        if (locationID != null) {
            EdsLocation location = locationManager.get(locationID);
            if (location.getTimeSlotID() != null) {
                timeSlot = timeSlotManager.get(location.getTimeSlotID());
            }

            if (timeSlot == null) {
                company = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
                timeSlot = company.getDefaultTimeSlot();
            }
        } else {
            company = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
            timeSlot = company.getDefaultTimeSlot();
        }

        if (timeSlot != null) {
            HashMap<Integer, TimeSlotItem> map = new HashMap<>();
            for (EdsTimeSlotItem item : timeSlot.getItems()) {
                TimeSlotItem timeSlotItem = new TimeSlotItem();
                timeSlotItem.setDay(item.getDay());
                timeSlotItem.setStartTime(item.getStartTime());
                timeSlotItem.setEndTime(item.getEndTime());
                timeSlotItem.setLunchStart(item.getLunchStart());
                timeSlotItem.setLunchEnd(item.getLunchEnd());
                timeSlotItem.setCoffeeStart(item.getCoffeeStart());
                timeSlotItem.setCoffeeEnd(item.getCoffeeEnd());
                map.put(item.getDay(), timeSlotItem);
            }

            return map;
        }
        return null;
    }

    @Override
    public SelectItem[] getStudentCustomerListAsSelectItem() {
        List<Object[]> studentCustomerList = studentManager.getStudentCustomerList();
        List<SelectItem> itemList = new ArrayList<>();

        if (studentCustomerList != null && studentCustomerList.size() > 0) {
            for (Object[] objects : studentCustomerList) {
                SelectItem item = new SelectItem((Integer) objects[0], (String) objects[1]);
                itemList.add(item);
            }

            return itemList.toArray(new SelectItem[]{});
        }
        return new SelectItem[0];
    }

    @Override
    public ArrayList<StudentItem> getStudentListForMerge(Integer[] studentIds) {
        List<EdsStudent> studentList = studentManager.getStudentListByIds(ServerUtils.getAsCommoDelimited(Arrays.asList(studentIds), "0", ","));
        if (studentList != null && studentList.size() > 0) {
            ArrayList<StudentItem> itemList = new ArrayList<>();
            itemList.add(getStudentItem(null));

            for (EdsStudent student : studentList) {
                itemList.add(student.getRPC());
            }

            return itemList;
        }
        return null;
    }

    @Override
    public Boolean mergeStudents(StudentItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIDs) {
        try {
            Integer objectID = saveStudent(mainItem);
            if (otherObjectIDs != null) {
                otherObjectIDs.remove(objectID);
            }
            studentManager.mergeDuplicateStudentsToMaster(otherObjectIDs, objectID);
            studentManager.updateNative("DELETE FROM " + "\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"" + ".student WHERE id in (" + ServerUtils.getAsCommoDelimited(otherObjectIDs, "0", ",") + ")");
            //studentManager.deleteStudentsByIds(otherObjectIDs);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CourseSubjectItem getCourseSubject(Integer objectId) {
        CourseSubjectItem item = new CourseSubjectItem();
        if (objectId != null) {
            EdsCourseSubject subject = courseSubjectManager.get(objectId);
            if (subject != null) {
                item.setName(subject.getName());
                item.setDescription(subject.getDescription());
                if (subject.getParent() != null) {
                    item.setParent(subject.getParent().getAsSelectItem());
                }
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.CourseSubject);
                item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(subject.getCustomFields(), customFieldsItems));
            }
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.CourseSubject);
            item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(subject.getCustomFields(), customFieldsItems));

        }
        return item;
    }

    @Override
    public Integer saveCourseSubject(CourseSubjectItem courseSubjectItem) {
        if (courseSubjectItem != null) {
            EdsCourseSubject subject = new EdsCourseSubject();
            if (courseSubjectItem.getObjectId() != null) {
                subject = courseSubjectManager.get(courseSubjectItem.getObjectId());
            }
            if (courseSubjectItem.getParent() != null) {
                subject.setParent(courseSubjectManager.get(courseSubjectItem.getParent().getId()));
            } else {
                subject.setParent(null);
            }
            if (courseSubjectItem.getDescription() != null) {
                subject.setDescription(courseSubjectItem.getDescription());
            }
            subject.setName(courseSubjectItem.getName());
            subject.setCustomFields(createCourseSubjectCustomFields(subject.getCustomFields(), courseSubjectItem.getCustomFields()));

            courseSubjectManager.createOrUpdate(subject);
            return subject.getObjectID();
        }
        return 0;
    }

    @Override
    public List<SelectItem> getCourseSubjectParent(Integer objectId) {
        List<SelectItem> list = new ArrayList<>();
        List<EdsCourseSubject> subjectList = courseSubjectManager.getParentCourseSubject(objectId);
        if (subjectList == null || subjectList.size() == 0) {
            return list;
        } else {
            for (EdsCourseSubject subject : subjectList) {
                list.add(subject.getAsSelectItem());
            }
            return list;
        }
    }

    @Override
    public ListResult<CourseSubjectItem> getCourseSubjectList(ListingFilterParameter filterParameter) {
        ListResult<CourseSubjectItem> result = new ListResult<>(new ArrayList<>(), 0);
        List<EdsCourseSubject> subjects = courseSubjectManager.list(filterParameter);
        Integer coursesSubjectTotal = courseSubjectManager.getCourseSubjectTotalCount(filterParameter);
        if (subjects != null) {
            ArrayList<CourseSubjectItem> courseItems = new ArrayList<>();
            CourseSubjectItem item;
            for (EdsCourseSubject course : subjects) {
                item = new CourseSubjectItem();
                item.setObjectId(course.getObjectID());
                item.setName(course.getName());
                item.setDescription(course.getDescription());
                if (course.getParent() != null) {
                    item.setParent(courseSubjectManager.get(course.getParent().getObjectID()).getAsSelectItem());
                }
                courseItems.add(item);
            }
            result = new ListResult<>(courseItems, coursesSubjectTotal);
        }
        return result;
    }

    @Override
    public Integer deleteCourseSubject(Integer objectId) {
        Integer id = 0;
        if (objectId != null) {
            EdsCourseSubject parent = courseSubjectManager.get(objectId);
            Integer count = courseSubjectManager.getCountParent(parent);
            if (count == 0) {
                courseSubjectManager.deleteChild(parent);
                return id;
            }
            return parent.getObjectID();
        }
        return id;
    }

    @Override
    public TreeSelectItem[] getCourseSubjectAsSelectItem(Integer objectId) {
        List<EdsCourseSubject> subjects = courseSubjectManager.getParentCourseSubject(null);
        if (subjects == null || subjects.size() == 0) {
            return new TreeSelectItem[0];
        } else {
            return wrapSubjectsAsTreeSelectItems(subjects);
        }
    }

    @Override
    public ContractCoursePriceItem[] getContractCoursePrices(Integer contractID) {
        List<ContractCoursePriceItem> items = new ArrayList<>();
        List<EdsContractCoursePrice> pricesList = contractCoursePriceManager.getContractCoursePrices(contractID);
        for (EdsContractCoursePrice price : pricesList) {
            ContractCoursePriceItem item = new ContractCoursePriceItem();
            item.setObjectID(price.getObjectID());
            if (price.getCourse() != null) {
                item.setCourseID(price.getCourse().getObjectID());
                item.setCourseName(price.getCourse().getName());
            }
            if (price.getLocation() != null) {
                item.setLocationID(price.getLocation().getObjectID());
                item.setLocationName(price.getLocation().getName());
            }
            item.setCoursePrice(price.getPrice());
            item.setStopFee(price.getStopFee());
            items.add(item);
        }
        return items.toArray(new ContractCoursePriceItem[]{});
    }

    @Override
    public void changeContractCoursePrices(Integer contractID, List<ContractCoursePriceItem> items) {
        EdsTrainingContract contract = trainingContractManager.get(contractID);
        for (ContractCoursePriceItem item : items) {
            EdsContractCoursePrice newPrice = new EdsContractCoursePrice();
            if (item.getObjectID() != null) {
                newPrice = contractCoursePriceManager.get(item.getObjectID());
            } else {
                newPrice.setContract(contract);
                newPrice.setCourse(courseManager.get(item.getCourseID()));
                newPrice.setLocation(locationManager.getLocationByName(item.getLocationName()));
            }
            newPrice.setPrice(item.getCoursePrice());
            newPrice.setStopFee(item.getStopFee());
            contractCoursePriceManager.createOrUpdate(newPrice);
        }
    }

    @Override
    public ArrayList<ContractCoursePriceItem> updatePrices(Integer contractID) {
        ArrayList<ContractCoursePriceItem> items = new ArrayList<>();
        EdsTrainingContract contract = trainingContractManager.get(contractID);
        List<EdsCourse> courses = contract.getCourses();
        for (EdsCourse course : courses) {
            List<EdsCoursePrice> originalPrices = coursePriceManager.getCoursePrices(course.getObjectID());
            List<EdsLocation> locations = contractCoursePriceManager.getOnlyLocations(contractID, course.getObjectID());
            for (EdsCoursePrice price : originalPrices) {
                if (price.getLocation() != null && !locations.contains(price.getLocation())) {
                    ContractCoursePriceItem item = new ContractCoursePriceItem();
                    item.setCourseID(course.getObjectID());
                    item.setCourseName(course.getName());
                    item.setLocationID(price.getLocation().getObjectID());
                    item.setLocationName(price.getLocation().getName());
                    item.setCoursePrice(price.getPrice());
                    item.setStopFee(price.getStopFee());
                    items.add(item);
                }
            }
        }
        return items;
    }

    @Override
    public ListResult<PassportData> getPassportsList(ListingFilterParameter filterParameter) {
        ListResult<PassportData> result = new ListResult<>(new ArrayList<>(), 0);
        List<EdsPassport> passports = passportManager.getList(filterParameter);
        Integer totalCount = passportManager.getTotalCount(filterParameter);
        if (passports != null && passports.size() > 0) {
            ArrayList<PassportData> passportDatas = new ArrayList<>();
            for (EdsPassport pass : passports) {
                passportDatas.add(pass.getRPC());
            }
            result = new ListResult<>(passportDatas, totalCount);
        }
        return result;
    }

    @Override
    public PassportData getPassportData(Integer objectID) {
        PassportData passport = new PassportData();
        if (objectID != null) {
            EdsPassport pass = passportManager.get(objectID);
            passport = pass.getRPC();
            List<EdsPassportCourse> passportCourses = passportCourseManager.getPassportCourses(objectID);
            List<CourseItem> courses = new ArrayList<>();
            if (passportCourses != null && passportCourses.size() > 0) {
                for (EdsPassportCourse pc : passportCourses) {
                    courses.add(pc.getRPC());
                }
            }
            passport.setCourses(courses.toArray(new CourseItem[]{}));
        }
        passport.setStatuses(ServerUtils.getAsSelectItem(referenceManager.listReferences(PASSPORT_STATUS), ServerUtils.REFERENCE));
        return passport;
    }

    @Override
    public CourseItem[] getPassportCourses(Integer studentID) {
        List<Object[]> courses = courseScheduleStudentManager.getStudentPassedCourses(studentID);
        if (courses != null && courses.size() > 0) {
            List<CourseItem> courseItems = new ArrayList<>();
            for (Object[] c : courses) {
                CourseItem course = new CourseItem();
                course.setNumber((String) c[0]);
                course.setCourseName((String) c[1]);
                course.setCourseDate((Date) c[2]);
                course.setExpireDate((Date) c[3]);
                courseItems.add(course);
            }
            return courseItems.toArray(new CourseItem[]{});
        }
        return new CourseItem[0];
    }

    @Override
    public Integer savePassport(PassportData passport, boolean isNew) {
        EdsPassport pass = isNew ? new EdsPassport() : passportManager.get(passport.getObjectID());
        pass.setCreationDate(new Date());
        pass.setLevel(passport.getLevel());
        pass.setNumber(passport.getNumber());
        pass.setNumberString(passport.getNumberString());
        pass.setStatus(referenceManager.get(passport.getStatusID()));
        pass.setType(passport.getType());
        pass.setStudent(studentManager.get(passport.getStudentID()));
        passportManager.createOrUpdate(pass);
        if (!isNew) {
            passportCourseManager.deletePassportCourses(passport.getObjectID());
        }
        if (passport.getCourses() != null) {
            passport.getCourses();
            for (CourseItem item : passport.getCourses()) {
                EdsPassportCourse pc = new EdsPassportCourse();
                pc.setCourseCode(item.getNumber());
                pc.setCourseName(item.getCourseName());
                pc.setStartDate(item.getCourseDate());
                pc.setExpireDate(item.getExpireDate());
                pc.setPassport(pass);
                passportCourseManager.create(pc);
            }
        }
        return pass.getObjectID();
    }

    @Override
    public Boolean deletePassport(Integer passportID) {
        if (passportID != null) {
            passportManager.deletePassport(passportID);
            passportCourseManager.deletePassportCourses(passportID);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean checkPassportNumber(String numberString, String number) {
        EdsPassport passport = passportManager.findPassportByNumber(numberString, number);
        return passport != null;
    }

    @Override
    public Date[] checkDayForAvailibility(Integer courseScheduleID, Date startDate, Date endDate) {
        Integer timeSlotID = null;
        if (courseScheduleID != null) {
            EdsCourseSchedule courseSchedule = scheduledCourseManager.get(courseScheduleID);
            EdsLocation location = courseSchedule != null && courseSchedule.getLocation() != null ? courseSchedule.getLocation() : null;
            timeSlotID = location != null ? location.getTimeSlotID() : null;
        }
        timeSlotID = timeSlotID == null ? userManager.getUser().getCompany().getDefaultTimeSlot().getObjectID() : timeSlotID;
        List<Date> dates = new ArrayList<>();
        int days = DateUtil.countDays(startDate, endDate);
        int increment = 0;
        boolean isFirstTime = true;

        for (int i = 0; i < days; i++) {
            Date tempDate = ServerUtils.addDays(startDate, i);
            int day = ServerUtils.getDayOfWeek(tempDate);
            Integer minutes = timeSlotItemManager.getStartMinutesByDay(day, timeSlotID);
            Date dat = (Date) tempDate.clone();
            if (minutes != null && minutes > 0) {
                if (isFirstTime) {
                    isFirstTime = false;
                } else {
                    dat.setHours(0);
                    dat.setMinutes(0);
                    dat.setSeconds(0);
                    dat.setMinutes(dat.getMinutes() + minutes);
                }
                dates.add(dat);
            }
        }
        return dates.toArray(new Date[]{});
    }

    @Override
    public Integer checkGeneratorSchedule(DateNonConvertable startDate, DateNonConvertable endDate) {
        EdsInvoiceGeneratorSchedule invoiceGeneratorSchedule = scheduledCourseManager.getInvoiceGeneratorSchedule(startDate.getNonConvertedDate(), endDate.getNonConvertedDate());

        if (invoiceGeneratorSchedule != null && invoiceGeneratorSchedule.getObjectID() != null) {
            return -1;
        } else {
            invoiceGeneratorSchedule = new EdsInvoiceGeneratorSchedule();
        }

        invoiceGeneratorSchedule.setStartDate(startDate.getNonConvertedDate());
        invoiceGeneratorSchedule.setEndDate(endDate.getNonConvertedDate());
        invoiceGeneratorSchedule.setStatus(InvoiceGeneratorStatus.IN_PROCESS);
        scheduledCourseManager.persist(invoiceGeneratorSchedule);

        return invoiceGeneratorSchedule.getObjectID();
    }

    public SelectItem[] getCoursePassedStudentsAsSelectItem(ListingFilterParameter filterParameter) {
        List<Object[]> students = courseScheduleStudentManager.getCoursePassedStudents(filterParameter);
        if (students != null && students.size() > 0) {
            ArrayList<SelectItem> items = new ArrayList<>();
            for (Object[] st : students) {
                SelectItem item = new SelectItem();
                String name = (String) st[1];
                String customer = st.length > 2 && st[2] != null ? (String) st[2] : null;
                name = customer != null ? (name + " (" + customer + ")") : name;
                item.setId((Integer) st[0]);
                item.setName(name);
                items.add(item);
            }
            return items.toArray(new SelectItem[]{});
        }
        return new SelectItem[0];
    }

    private TreeSelectItem[] wrapSubjectsAsTreeSelectItems(List<EdsCourseSubject> subjects) {
        List<TreeSelectItem> subjectItemList = new ArrayList<>();

        if (subjects != null && subjects.size() > 0) {
            for (EdsCourseSubject subject : subjects) {
                generateRecursiveCategoryItem(subject, subjectItemList);
            }
        }

        return subjectItemList.toArray(new TreeSelectItem[]{});
    }

    private void generateRecursiveCategoryItem(EdsCourseSubject subject, List<TreeSelectItem> subjectItemList) {
        if (subject.getChildList() != null && subject.getChildList().size() > 0) {
            TreeSelectItem categoryItem = wrapSubject(subject);
            for (EdsCourseSubject child : subject.getChildList()) {
                generateRecursiveCategoryItem(child, categoryItem.getChildren());
            }
            subjectItemList.add(categoryItem);
        } else {
            TreeSelectItem categoryItem = wrapSubject(subject);
            subjectItemList.add(categoryItem);
        }
    }

    private TreeSelectItem wrapSubject(EdsCourseSubject subject) {
        TreeSelectItem item = new TreeSelectItem();
        item.setId(subject.getObjectID());
        item.setName(subject.getName());
        item.setSelected(false);
        item.setShowInDropDown(true);
        if (subject.getParent() != null) {
            item.setParent(wrapSubject(subject.getParent()));
        }

        return item;
    }

    /**
     * Expire temporary locks
     * <p/>
     * this process will run from reccurence job
     */
    public void expireTemporaryLocks(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        List<EdsSeatTemporaryLock> temporaryLocks = temporaryLockManager.list();

        //current time
        Date currentTime = new Date();

        for (EdsSeatTemporaryLock temporaryLock : temporaryLocks) {
            int scheduleMinute = Long.valueOf((currentTime.getTime() - temporaryLock.getExpireDate().getTime()) / 60000).intValue();

            if (scheduleMinute >= EXPIRE_MINUTE) {
                expireTemporaryLock(temporaryLock.getBookingID());
            }
        }
    }

    /**
     * clear all temporary lock items from lock table and course schedule students
     *
     * @param bookingID
     */
    public void expireTemporaryLock(Integer bookingID) {
        //remove temporary lock by booking uuid from lock table
        temporaryLockManager.deleteByBookingID(bookingID);

        //remove temporary registration from course schedule students table
        courseScheduleStudentManager.deleteTemporaryRegistration(bookingID);
    }

    @Override
    public void expireTemporaryLock(Integer bookingID, String itemUUID) {
        courseScheduleStudentManager.deleteTemporaryRegistration(bookingID, itemUUID);
    }

    @Override
    public boolean isExistStudentWithResidenceNumber(Integer objectID, String residenceNumber, Integer customerID) {
        EdsCrmAccount customer = crmAccountManager.get(customerID);
        EdsStudent edsStudent = studentManager.findExistingStudentByResidenceNum(objectID, residenceNumber, customer);
        return edsStudent != null && edsStudent.getObjectID() != null;
    }

    public ListResult<AssessmentItem> getAssessmentList(ListingFilterParameter filterParametrs) {
        List<EdsTCStudentQuestionaire> questionaires = questionaireManager.getQuestionairesList(filterParametrs);
        Integer questionairTotal = questionaireManager.getQuestionairesTotal(filterParametrs);

        ArrayList<AssessmentItem> assessmentItems = new ArrayList<>();
        AssessmentItem assessmentItem;

        for (EdsTCStudentQuestionaire questionaire : questionaires) {
            assessmentItem = new AssessmentItem();
            assessmentItem.setName(questionaire.getQuestionaire().getName());
            assessmentItem.setTotalPoints(questionaire.getQuestionaire().getTotalPoints().toString());
            assessmentItem.setObjectId(questionaire.getQuestionaire().getObjectID());
            assessmentItem.setStudentQuestionaireId(questionaire.getObjectID());
            assessmentItems.add(assessmentItem);
        }

        return new ListResult<>(assessmentItems, questionairTotal);
    }

    @Override
    public ListResult<CourseBookingItem> getCourseBoookigList(ListingFilterParameter filterParametrs) {
        List<EdsCourseBooking> edsCourseBoookingList = courseBookingManager.getCourseBookingList(filterParametrs);
        Integer courseBookingTotal = courseBookingManager.getCourseBookingTotal(filterParametrs);
        ArrayList<CourseBookingItem> courseBookingItems = new ArrayList<>();
        for (EdsCourseBooking edsCourseBooking : edsCourseBoookingList) {
            courseBookingItems.add(edsCourseBooking.getRPC());
        }
        return new ListResult<>(courseBookingItems, courseBookingTotal);
    }

    @Override
    public ListResult<CourseBookingItem> getCourseBookingListFromSolr(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        FacetFilterRpc bookingFacetFilter = filterParametrs.getFacetFilter();
        if (bookingFacetFilter != null && !bookingFacetFilter.isFilterChanges()) {
            bookingFacetFilter = commonServiceLocal.getUserFacetFilter(bookingFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getCourseBookingCoreSolrQuery(filterParametrs));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQuery(bookingFacetFilter, edsUser.getCompany(),
                SolrCourseBookingRepresenter.FIELD_CREATED_DATE, null,
                FacetContentType.CourseBookingFacetFilter.getContentCode()[0],
                FacetContentType.CourseBookingFacetFilter.getContentCode()[1],
                FacetContentType.CourseBookingFacetFilter.getContentCode()[2],
                FacetContentType.CourseBookingFacetFilter.getContentCode()[3]));
        return getCourseBookingListResponse(filterParametrs, edsUser, solrQuery.toString());
    }

    private ListResult<CourseBookingItem> getCourseBookingListResponse(ListingFilterParameter filterParameter, EdsUser edsUser, String solrQuery) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_COURSE_BOOKING_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getCourseBookingSolrQuery(filterParameter, solrQuery), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getCourseBookingFromSolrResult(resp, edsUser, filterParameter);
    }

    private ListResult<CourseBookingItem> getCourseBookingFromSolrResult(QueryResponse resp, EdsUser currentUser, ListingFilterParameter filterParameter) {
        int totalNumber = (int) resp.getResults().getNumFound();
        ArrayList<CourseBookingItem> itemList = new ArrayList<>();

        if (resp.getResults() != null) {
            for (SolrDocument doc : resp.getResults()) {

                if (doc != null) {
                    CourseBookingItem item = EdsCourseBooking.wrapSolrDocumentToRPC(doc);
                    itemList.add(item);
                }
            }
        }
        return new ListResult<>(itemList, totalNumber);
    }

    private SolrQuery getCourseBookingSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(filterParameter.getLimit()));

        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                if (CourseBookingItem.NUMBER.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseBookingRepresenter.SORTABLE_COURSE_BOOKING_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (CourseBookingItem.CUSTOMER.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseBookingRepresenter.SORTABLE_CUSTOMER_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (CourseBookingItem.LOCATION.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseBookingRepresenter.SORTABLE_LOCATION_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (CourseBookingItem.STATUS.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseBookingRepresenter.SORTABLE_STATUS_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (CourseBookingItem.TYPE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseBookingRepresenter.SORTABLE_TYPE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (CourseBookingItem.CREATIONDATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCourseBookingRepresenter.FIELD_CREATED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                }
            } else {
                query.setSort(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID, SolrQuery.ORDER.desc);
            }
        } else {
            query.setSort(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID, SolrQuery.ORDER.desc);
        }
        return query;
    }

    @Override
    public void deleteCourseBookingByIds(Integer[] objectIDs) {
        if (objectIDs != null) {
            for (Integer objectID : objectIDs) {
                deleteCourseBooking(objectID);
            }
        }
    }

    @Override
    public void deleteCourseBooking(Integer objectID) {
        EdsCourseBooking courseBooking = courseBookingManager.get(objectID);

        List<EdsCourseSchedule> courseScheduleList = courseScheduleStudentManager.getScheduleListByBooking(objectID);

        courseScheduleStudentManager.deleteByCourseBooking(objectID);
        courseBookingManager.deleteCourseBooking(objectID);

        try {
            solrManager.removeCourseBookingByIds(courseBooking.getObjectID());
            solrManager.addCourseScheduleToIndex(courseScheduleList.toArray(new EdsCourseSchedule[]{}));
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(CourseBookingEventListenerImpl.TYPE, CourseBookingEventListenerImpl.EVENT_REJECT_BOOKING, courseBooking, userManager.getUser());
        event.setCustomStringField(courseBooking.getStatus().getCode());
    }

    public boolean cancelGymStudentCourseBooking(Integer studentId, Integer scheduleCourseId) {
        EdsCourseScheduleStudent courseScheduleList = courseScheduleStudentManager.getCourseScheduleStudentByStudentId(scheduleCourseId, studentId);
        courseScheduleStudentManager.deleteCourseAndStudentFromCourseScheduledStudent(scheduleCourseId, studentId);

        Integer bookingIdByScheduledCourseId = courseScheduleStudentManager.getBookingIdByScheduledCourseId(scheduleCourseId);
        if (bookingIdByScheduledCourseId == null) {
            courseBookingManager.deleteCourseBooking(courseScheduleList.getCourseBooking().getObjectID());
        }

        try {
            solrManager.addCourseScheduleToIndex(courseScheduleList.getCourseScheduleBooking());
            solrManager.removeCourseBookingByIds(courseScheduleList.getCourseBooking().getObjectID());
            return true;
        } catch (SolrServerException | IOException e) {
            log.error("Solr indexing error for scheduleCourseId: {}", scheduleCourseId, e);
            return false;
        }
    }

    @Override
    public ListResult<CertificateData> getCertificateList(ListingFilterParameter filterParameter) {
        List<EdsCertificate> certificateList = certificateManager.getCertificateList(filterParameter);
        Integer certificateTotalCount = certificateManager.getCertificateTotalCount(filterParameter);
        ArrayList<CertificateData> certificateDataList = new ArrayList<>();
        for (EdsCertificate cert : certificateList) {
            certificateDataList.add(cert.createCertificateData());
        }
        return new ListResult<>(certificateDataList, certificateTotalCount);
    }

    @Override
    public Integer saveCertificateData(CertificateData certificateData) {
        EdsCertificate certificate;
        if (certificateData.getObjectID() != null) {
            certificate = certificateManager.get(certificateData.getObjectID());
            certificateManager.deleteCertificateItems(certificateData.getObjectID());
        } else {
            certificate = new EdsCertificate();
        }

        if (certificateManager.isCertificateNumberExists(certificateData.getCertificateTypeData().getNumberData().getNumberString(), certificateData.getCertificateTypeData().getObjectID(), certificateData.getObjectID())) {
            NumberData numberData = EdsNumberingSettings.getDefaultDataForTraining(certificateData.getCertificateTypeData().getNumberData().getIntNumber() + 1, certificateData.getCertificateTypeData().getNumberData().getFirstNumberString());
            certificate.setIntNumber(numberData.getIntNumber());
            certificate.setNumber(numberData.getNumberString());
        } else {
            certificate.setIntNumber(certificateData.getCertificateTypeData().getNumberData().getIntNumber());
            certificate.setNumber(certificateData.getCertificateTypeData().getNumberData().getNumberString());
        }

        certificate.setStudent(studentManager.get(certificateData.getStudentID()));
        certificate.setCertificateType(certificateTypeManager.get(certificateData.getCertificateTypeData().getObjectID()));
        certificate.setCreationDate(new Date());

        List<EdsCertificateItem> edsCertificateItemList = new LinkedList<>();
        CertificateItemData[] items = certificateData.getItems();
        if (items != null) {
            for (CertificateItemData item : items) {
                if (certificate.getCertificateType() != null && certificate.getCertificateType().getExpiryDateSorder() != null
                        && certificate.getCertificateType().getExpiryDateSorder() == item.getSorder()) {
                    if (item.getValues().contains("/")) {
                        Date date = null;
                        try {
                            date = ServerUtils.parseDate(item.getValues(), SHORT_DATE_FORMAT_2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        certificate.setExpiryDate(date);
                    } else if (item.getValues().contains("-")) {
                        Date date = null;
                        try {
                            date = ServerUtils.parseDate(item.getValues(), SHORT_DATE_FORMAT_6);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        certificate.setExpiryDate(date);
                    } else {
                        Date date = null;
                        try {
                            date = ServerUtils.parseDate(item.getValues());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        certificate.setExpiryDate(date);
                    }
                }
                EdsCertificateItem edsCertificateItem = new EdsCertificateItem();
                edsCertificateItem.setSorder(item.getSorder());
                edsCertificateItem.setValues(item.getValues());
                edsCertificateItem.setColor(item.getColor());
                edsCertificateItemList.add(edsCertificateItem);
            }
        }
        certificate.setItems(edsCertificateItemList);

        certificateManager.createOrUpdate(certificate);

        if (certificateData.getObjectID() != null) {
            EdsBusinessEvent workflowRule = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, certificate, userManager.getUser());
            workflowRule.setEntityType(RelationItem.TYPE_CERTIFICATE);
        } else {
            EdsBusinessEvent workflowRule = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, certificate, userManager.getUser());
            workflowRule.setEntityType(RelationItem.TYPE_CERTIFICATE);
        }

        return certificate.getObjectID();
    }

    @Override
    public CertificateData getCertificateData(Integer certificateID, boolean fullData) {
        CertificateData certificateData;
        if (certificateID != null) {
            EdsCertificate certificate = certificateManager.get(certificateID);
            certificateData = certificate.createCertificateData();

            List<EdsCertificateItem> edsCertificateItemList = certificate.getItems();
            CertificateItemData[] items = new CertificateItemData[edsCertificateItemList.size()];
            if (edsCertificateItemList != null && edsCertificateItemList.size() > 0) {
                int i = 0;
                for (EdsCertificateItem ci : edsCertificateItemList) {
                    items[i++] = ci.createItemData();
                }
            }
            certificateData.setItems(items);
        } else {
            certificateData = new CertificateData();
        }

        if (fullData) {
            if (certificateData.getCertificateTypeData() != null && certificateData.getCertificateTypeData().getObjectID() != null) {
                certificateData.getCertificateTypeData().setStudents(getCoursePassedStudents(certificateData.getCertificateTypeData().getObjectID()));
            }

            int i = 0;
            List<EdsCertificateType> certificateTypes = certificateTypeManager.getCertificateTypes();
            SelectItem[] certificateTypeItems = new SelectItem[certificateTypes.size()];
            for (EdsCertificateType ct : certificateTypes) {
                certificateTypeItems[i++] = ct.getAsSelectItem();
            }
            certificateData.setCertificateTypes(certificateTypeItems);
        }

        return certificateData;
    }

    @Override
    public CertificateTypeData getCertificateTypeTemplateData(Integer certificateTypeID) {
        EdsCertificateType certificateType = certificateTypeManager.get(certificateTypeID);

        CertificateTypeData certificateTypeData = new CertificateTypeData();
        certificateTypeData.setObjectID(certificateType.getObjectID());
        certificateTypeData.setNumberData(certificateManager.generateNumberData(certificateTypeID));
        certificateTypeData.setImageURL(certificateType.getBackImageURL());
        certificateTypeData.setFieldsCount(certificateType.getFieldCount());
        certificateTypeData.setStudents(getCoursePassedStudents(certificateTypeID));

        return certificateTypeData;
    }

    public Boolean deleteCertificate(Integer certificateID) {
        if (certificateID != null) {
            EdsCertificate certificate = certificateManager.get(certificateID);
            if (certificate != null) {
                certificateManager.deleteCertificateItems(certificateID);
                certificateManager.deleteCertificate(certificateID);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean unAssignInstructorFromScheduledCourse(Integer scheduledCourseID) {
        try {
            EdsCourseSchedule courseSchedule = scheduledCourseManager.get(scheduledCourseID);
            scheduledCourseManager.deleteScheduledCourseInstructors(courseSchedule.getObjectID());
            courseSchedule.setInstructor(null);
            scheduledCourseManager.update(courseSchedule);
            solrManager.addCourseScheduleToIndex(courseSchedule);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public SelectItem[] getCoursePassedStudents(Integer certificateTypeID) {
        List<Integer> courseIDs = certificateTypeManager.getCertificateTypeCourses(certificateTypeID);
        if (courseIDs == null || courseIDs.size() == 0) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(0);
            fp.setLimit(10000);
            List<EdsStudent> students = studentManager.getStudentList(fp);
            SelectItem[] items = new SelectItem[students.size()];
            int i = 0;
            for (EdsStudent st : students) {
                items[i++] = st.getAsSelectItem();
            }

            return items;
        }

        List<Integer> courseScheduleIDs = scheduledCourseManager.getNotExpiredCourseSchedulesByCourses(courseIDs);
        HashMap<Integer, List<Integer>> studentsPassedCourses = courseScheduleStudentManager.getStudentsPassedCourses(courseScheduleIDs);

        List<Integer> passedStudentsList = new LinkedList<>();

        Set<Integer> studentKeySet = studentsPassedCourses.keySet();
        for (Integer studentID : studentKeySet) {
            boolean passed = true;
            List<Integer> coursesPassed = studentsPassedCourses.get(studentID);
            for (Integer course : courseIDs) {
                if (!coursesPassed.contains(course)) {
                    passed = false;
                    break;
                }
            }
            if (passed) {
                passedStudentsList.add(studentID);
            }
        }

        SelectItem[] items = new SelectItem[passedStudentsList.size()];
        int i = 0;
        for (Integer studentID : passedStudentsList) {
            EdsStudent student = studentManager.get(studentID);
            items[i++] = student.getAsSelectItem();
        }

        return items;
    }

    public String getMasterCardPaymentURL(Integer courseBookingID) {
        return getMasterCardPaymentURL(courseBookingID, null);
    }

    public String getMasterCardPaymentURL(Integer courseBookingID, String userDefinedUrl) {
        if (courseBookingID != null) {
            EdsCourseBooking courseBooking = courseBookingManager.get(courseBookingID);
            if (courseBooking.getStatus() != null && !"".equals(courseBooking.getStatus())) {
                if (BOOKING_PAID.equals(courseBooking.getStatus().getCode())) {
                    return null;
                }
            }


            BigDecimal calculatedAmount = courseBooking.getCalculatedAmount();
            if (calculatedAmount.compareTo(BigDecimal.ZERO) > 0) {
                Integer companyID = courseBookingManager.getUser().getCompany().getObjectID();
                StringBuilder mastercardPaymentURL = new StringBuilder();
                mastercardPaymentURL.append(EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/mastercardPaymentValidate?");
                mastercardPaymentURL.append("user_amount=" + EncryptionHelper.encryptURL(calculatedAmount.toString()));
                mastercardPaymentURL.append("&user_cid=" + EncryptionHelper.encryptURL(companyID.toString()));
                mastercardPaymentURL.append("&user_key=" + EncryptionHelper.encryptURL(courseBookingID.toString()));
                mastercardPaymentURL.append("&user_type=" + EncryptionHelper.encryptURL(MastercardPaymentHandler.COURSE_BOOKING));

                if (userDefinedUrl != null && !userDefinedUrl.isEmpty()) {
                    mastercardPaymentURL.append("&user_url=" + EncryptionHelper.encryptURL(userDefinedUrl));
                }

                System.out.print("MASTER CARD URL: ");
                System.out.println(mastercardPaymentURL);
                return mastercardPaymentURL.toString();
            }
        }
        System.out.println("MASTER CARD URL: NULL");
        return null;
    }

    public String[] importXML(InputStream inputStream) {
        String message = "File imported successfully!";
        String[] values = new String[2];
        values[0] = "0";
        ParentClass xml = null;
        try {
            Unmarshaller m = JAXBContext.newInstance(ParentClass.class).createUnmarshaller();
            xml = (ParentClass) m.unmarshal(inputStream);
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
        if (xml != null) {
            String className = xml.getClassName();
            EdsCourseSchedule courseSchedule = scheduledCourseManager.getScheduledCourseByNumber(className);
            if (courseSchedule != null) {
                if (xml.getAssignmentList() != null) {
                    Assignment assignment = xml.getAssignmentList().getAssignment();
                    String questionaireName = assignment.getAssignmentName();
                    EdsTCStudentQuestionaire questionaire1 = studentQuestionaireManager.checkQuestionaireForImporting(courseSchedule, questionaireName);
                    if (questionaire1 != null) {
                        message = "This file already imported!";
                        values[1] = message;
                        return values;
                    }
                    EdsTCQuestionaire questionaire = new EdsTCQuestionaire();
                    questionaire.setName(questionaireName);
                    questionaire.setTotalPoints(assignment.getPointsPossible().intValue());
                    questionaire.setType(referenceManager.findReference("_TC_QUESTIONAIRE_TYPE", "TC_QUESTION_TYPE_ASSESMENT"));
                    questionaireManager.create(questionaire);

                    ResultsList resultList = assignment.getResultsList();
                    if (resultList != null) {
                        for (Results result : resultList.getResults()) {
                            EdsTCStudentQuestionaire studentQuestionaire = new EdsTCStudentQuestionaire();
                            studentQuestionaire.setQuestionaire(questionaire);
                            studentQuestionaire.setCourseSchedule(courseSchedule);
                            studentQuestionaire.setStudent(studentManager.get(result.getId().intValue()));
                            studentQuestionaireManager.create(studentQuestionaire);

                            List<Response> responseList = result.getResponseList().getResponse();
                            if (responseList != null) {
                                for (Response response : responseList) {
                                    Integer pointsEarned = response.getPointsEarned().intValue();
                                    Integer questionID = response.getQuestionId().intValue();
                                    EdsTCResponse edsTCResponse = new EdsTCResponse();
                                    edsTCResponse.setAnswer(response.getText());
                                    edsTCResponse.setPointsEarnet(response.getPointsEarned().intValue());
                                    edsTCResponse.setQuestionNumber(response.getQuestionId().intValue());
                                    edsTCResponse.setSudentQuestionaire(studentQuestionaire);
                                    responseManager.create(edsTCResponse);
                                }
                            }
                        }
                    }
                }
            } else {
                values[1] = "Scheduled course not found!";
            }
        }
        return values;
    }

    @Override
    public TCScheduleData getTCScheduleData(DateNonConvertable startDate, DateNonConvertable endDate, ListingFilterParameter filterParameter) {
        filterParameter.setStartDate(startDate.getNonConvertedDate());
        filterParameter.setEndDate(endDate.getNonConvertedDate());
        TCScheduleData scheduleData = new TCScheduleData();
        scheduleData.setItems(invoiceManager.getTCInvoicesForSchedule(filterParameter).toArray(new TCScheduleItem[]{}));
        return scheduleData;
    }

    @Override
    public Integer saveTCScheduleData(TCScheduleData scheduleData) {
        EdsUser user = tcScheduledTaskManager.getUser();

        EdsTCScheduledTask scheduledTask = new EdsTCScheduledTask();
        scheduledTask.setCompanyID(user.getCompany().getObjectID());
        scheduledTask.setUserID(user.getObjectID());
        scheduledTask.setPeriodStart(scheduleData.getStartDate() != null ? scheduleData.getStartDate().getNonConvertedDate() : null);
        scheduledTask.setPeriodEnd(scheduleData.getEndDate() != null ? scheduleData.getEndDate().getNonConvertedDate() : null);
        scheduledTask.setCustomerID(scheduleData.getCustomerID());
        scheduledTask.setStatus(EdsTCScheduledTask.STATUS_PENDING);
        tcScheduledTaskManager.create(scheduledTask);

        TCScheduleItem[] invoicesAsItems = scheduleData.getItems();
        for (TCScheduleItem schItem : invoicesAsItems) {
            EdsTCScheduledTaskItem scheduledTaskItem = new EdsTCScheduledTaskItem();
            scheduledTaskItem.setScheduledTask(scheduledTask);
            scheduledTaskItem.setInvoiceID(schItem.getObjectID());
            scheduledTaskItem.setLocationID(schItem.getLocationID());
            scheduledTaskItem.setStatus(EdsTCScheduledTask.STATUS_PENDING);
            tcScheduledTaskItemManager.create(scheduledTaskItem);
        }

        return scheduledTask.getObjectID();
    }

    public void executeScheduledTasker() {
        zippingPDFGeneratedScheduleTask();
        sendZippedScheduledTasksAsMail();
        generateScheduledTaskInvoicesPDF();
    }

    private Map<Integer, Set<Integer>> studentInvoices = null;
    private Map<Integer, Set<Integer>> scheduleInvoices = null;

    @Override
    public void scheduleGenerateInvoice(Integer scheduleID) {
        EdsInvoiceGeneratorSchedule invoiceGeneratorSchedule = scheduledCourseManager.getInvoiceGeneratorSchedule(scheduleID);
        baseEventPostProcessor.registerEvent(CourseScheduleCustomEventListenerImpl.TYPE_INVOICE_GENERATOR_SCHEDULE, CourseScheduleCustomEventListenerImpl.EVENT_GENERATE_INVOICE, invoiceGeneratorSchedule, scheduledCourseManager.getUser());
    }

    @Override
    public void scheduleRegenerateInvoice(Integer scheduleID) {
        EdsInvoiceGeneratorSchedule invoiceGeneratorSchedule = scheduledCourseManager.getInvoiceGeneratorSchedule(scheduleID);
        baseEventPostProcessor.registerEvent(CourseScheduleCustomEventListenerImpl.TYPE_INVOICE_GENERATOR_SCHEDULE, CourseScheduleCustomEventListenerImpl.EVENT_REGENERATE_INVOICE, invoiceGeneratorSchedule, scheduledCourseManager.getUser());
    }

    /**
     * Generate invoice for customer per location by attended students
     *
     * @param fp
     */
    public void generateInvoices(ListingFilterParameter fp) {
        EdsInvoiceGeneratorSchedule invoiceGeneratorSchedule = scheduledCourseManager.getInvoiceGeneratorSchedule(fp.getCaseID());
        try {
            studentInvoices = new HashMap<>();
            scheduleInvoices = new HashMap<>();

            List<Object[]> itemList = scheduledCourseManager.getCourseScheduleForInvoiceByPeriod(fp);
            if (itemList != null && itemList.size() > 0) {
                for (Object[] item : itemList) {
                    Integer customerID = (Integer) item[0];

                    String[] scheduledCourseIds = ((String) item[1]).split("[,]");
                    ArrayList<String> sCIdList = new ArrayList<>(Arrays.asList(scheduledCourseIds));
                    sCIdList = ServerUtils.removeDuplicates(sCIdList);
                    List<StudentAsInvoiceItem> studentAsInvoiceItemList = courseScheduleStudentManager.getCourseStudentAsInvoiceItems(customerID, sCIdList);

                    if (studentAsInvoiceItemList != null && studentAsInvoiceItemList.size() > 0) {
                        //create daily invoice for customer from attended students.
                        Integer invoiceID = invoiceServiceLocal.createDailyInvoiceForCustomerByCustomerStaff(invoiceGeneratorSchedule.getEndDate(), customerID, studentAsInvoiceItemList, null);

                        scheduledCourseManager.updateScheduledCoursesInvoice(sCIdList, invoiceID);
                        courseScheduleStudentManager.updateStudentsInvoice(buildCourseScheduleStudentIds(studentAsInvoiceItemList), invoiceID);

//                        registrationStudentAndScheduleInvoices(studentAsInvoiceItemList, invoiceID);

                        courseScheduleStudentManager.flushAndClear();
                    }
                }

//                updateStudentAndScheduleInvoices();
            }
            invoiceGeneratorSchedule.setStatus(InvoiceGeneratorStatus.COMPLETED);
        } catch (Exception e) {
            invoiceGeneratorSchedule.setStatus(InvoiceGeneratorStatus.FAILED);
            e.printStackTrace();
        }

        scheduledCourseManager.merge(invoiceGeneratorSchedule);
    }

    @Override
    public void reGenerateInvoices(ListingFilterParameter fp) {
        studentInvoices = new HashMap<>();
        EdsInvoiceGeneratorSchedule invoiceGeneratorSchedule = scheduledCourseManager.getInvoiceGeneratorSchedule(fp.getCaseID());
        try {
            List<Object[]> itemList = scheduledCourseManager.getInvoicedScheduleCourseList(fp);
            if (itemList != null && itemList.size() > 0) {
                for (Object[] item : itemList) {
                    Integer invoiceID = (Integer) item[0];
                    Integer customerID = (Integer) item[1];

                    //clear invoice from scheduled course students
                    courseScheduleStudentManager.removeInvoiceFromCourseStudents(invoiceID);

                    String[] scheduledCourseIds = ((String) item[2]).split("[,]");
                    ArrayList<String> sCIdList = new ArrayList<>(Arrays.asList(scheduledCourseIds));
                    sCIdList = ServerUtils.removeDuplicates(sCIdList);
                    List<StudentAsInvoiceItem> studentAsInvoiceItemList = courseScheduleStudentManager.getCourseStudentAsInvoiceItems(customerID, sCIdList);

                    if (studentAsInvoiceItemList != null && studentAsInvoiceItemList.size() > 0) {
                        //update daily invoice for customer from attended students.
                        invoiceID = invoiceServiceLocal.createDailyInvoiceForCustomerByCustomerStaff(null, customerID, studentAsInvoiceItemList, invoiceID);

                        courseScheduleStudentManager.updateStudentsInvoice(buildCourseScheduleStudentIds(studentAsInvoiceItemList), invoiceID);
                        courseScheduleStudentManager.flushAndClear();
                    }
                }
            }

            invoiceGeneratorSchedule.setStatus(InvoiceGeneratorStatus.COMPLETED);
        } catch (Exception e) {
            invoiceGeneratorSchedule.setStatus(InvoiceGeneratorStatus.FAILED);
            e.printStackTrace();
        }
        scheduledCourseManager.merge(invoiceGeneratorSchedule);
    }

    private List<Integer> buildCourseScheduleStudentIds(List<StudentAsInvoiceItem> studentAsInvoiceItemList) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (StudentAsInvoiceItem item : studentAsInvoiceItemList) {
            if (item.includedInInvoice)
                ids.add(item.getObjectID());
        }
        return ids;
    }

    private void generateScheduledTaskInvoicesPDF() {
        EdsTCScheduledTask scheduledTask = tcScheduledTaskManager.getLastPendingScheduledTask();
        if (scheduledTask != null && scheduledTask.getCompanyID() != null && scheduledTask.getUserID() != null) {
            ServerSecurityContext.getInstance().setCompanyId(scheduledTask.getCompanyID().toString());
            ServerSecurityContext.getInstance().setStaticUserID(scheduledTask.getUserID());

            log.info("SCHEDULED_TASK_ID:" + scheduledTask.getObjectID());
            List<EdsTCScheduledTaskItem> scheduledTaskItemList = tcScheduledTaskItemManager.getLastPendingScheduledTaskItems(scheduledTask.getObjectID());
            if (scheduledTaskItemList != null && scheduledTaskItemList.size() > 0) {
                for (EdsTCScheduledTaskItem scheduledTaskItem : scheduledTaskItemList) {
                    log.info("SCHEDULED_TASK_ITEM_ID:" + scheduledTaskItem.getObjectID());
                    EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(scheduledTaskItem.getInvoiceID());
                    if (saleInvoice != null && !saleInvoice.isDeleted()) {
                        log.info("CREATING_PDF_FILE");
                        ByteArrayOutputStream baos = savedSaleInvoiceViewPDFHandler.getPDFStream(new InvoiceQuoteRequestObject(saleInvoice.getObjectID()));
                        writeStreamToFile(baos, saleInvoice.getNumber(), scheduledTask);
                        scheduledTaskItem.setStatus(EdsTCScheduledTask.STATUS_PDF_GENERATED);
                        tcScheduledTaskItemManager.update(scheduledTaskItem);
                    }
                }
            } else {
                ListingFilterParameter filterParameter = new ListingFilterParameter();
                filterParameter.setObjectId(scheduledTask.getObjectID());
                ByteArrayOutputStream baos = tcScheduledInvoicePDFHandler.getPDFStream(filterParameter);
                ByteArrayOutputStream consolidatedBySubjectsBaos = tcConsolidatedInvoicesPDFHandler.getPDFStream(filterParameter);
                writeStreamToFile(baos, "Consolidated_Invoice", scheduledTask);
                writeStreamToFile(consolidatedBySubjectsBaos, "Consolidated_Invoice_By_Subjects", scheduledTask);

                scheduledTask.setStatus(EdsTCScheduledTask.STATUS_PDF_GENERATED);
                tcScheduledTaskManager.update(scheduledTask);
            }
        }
    }

    private boolean zippingPDFGeneratedScheduleTask() {
        EdsTCScheduledTask pdfGeneratedScheduledTask = tcScheduledTaskManager.getLastPDFGeneratedScheduledTask();
        if (pdfGeneratedScheduledTask != null) {
            ServerSecurityContext.getInstance().setCompanyId(pdfGeneratedScheduledTask.getCompanyID().toString());
            ServerSecurityContext.getInstance().setStaticUserID(pdfGeneratedScheduledTask.getUserID());

            if (EdsTCScheduledTask.STATUS_ZIP_IN_PROGRESS.equals(pdfGeneratedScheduledTask.getStatus())) {
                log.info("Zipping in progress");
                return true;
            }
            log.info("Zipping started");
            pdfGeneratedScheduledTask.setStatus(EdsTCScheduledTask.STATUS_ZIP_IN_PROGRESS);
            tcScheduledTaskManager.update(pdfGeneratedScheduledTask);

            boolean zipFileCreated = false;
            try {
                CreateZipFile.Compress(new File(pdfGeneratedScheduledTask.getFolderURL()), "pdf", true);
                zipFileCreated = true;
                log.info("Zip file created");
            } catch (Exception e) {
                zipFileCreated = false;
                pdfGeneratedScheduledTask.setStatus(EdsTCScheduledTask.STATUS_PDF_GENERATED);
                tcScheduledTaskManager.update(pdfGeneratedScheduledTask);
                e.printStackTrace();
            }

            pdfGeneratedScheduledTask.setStatus(EdsTCScheduledTask.STATUS_ZIPPED);
            tcScheduledTaskManager.update(pdfGeneratedScheduledTask);

            if (zipFileCreated) {
                DeleteDirectory.delete(pdfGeneratedScheduledTask.getFolderURL());
            }
            log.info("Zipping completed");
        }
        return false;
    }

    private void sendZippedScheduledTasksAsMail() {
        EdsTCScheduledTask zippedScheduledTask = tcScheduledTaskManager.getLastZippedScheduledTask();
        if (zippedScheduledTask != null) {
            ServerSecurityContext.getInstance().setCompanyId(zippedScheduledTask.getCompanyID().toString());
            ServerSecurityContext.getInstance().setStaticUserID(zippedScheduledTask.getUserID());

            log.info("SENDING_SCHEDULED_TASK_REPORT_START");
            messageManager.sendScheduledTaskAsMail(zippedScheduledTask);
            log.info("SENDING_SCHEDULED_TASK_REPORT_END");
        }
    }

    private void writeStreamToFile(ByteArrayOutputStream baos, String invoiceNumber, EdsTCScheduledTask scheduledTask) {
        try {
            String folderURL = scheduledTask.getFolderURL();
            File folder = new File(folderURL);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(folderURL + "/" + (invoiceNumber + ".pdf").replace(" ", ""));
            baos.writeTo(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registrationStudentAndScheduleInvoices(List<StudentAsInvoiceItem> studentAsInvoiceItemList, Integer invoiceID) {
        for (StudentAsInvoiceItem studentAsInvoiceItem : studentAsInvoiceItemList) {

            scheduleInvoices.computeIfAbsent(studentAsInvoiceItem.getCourseScheduleID(), k -> new HashSet<>());

            studentInvoices.computeIfAbsent(invoiceID, k -> new HashSet<>());

            if (studentAsInvoiceItem.includedInInvoice) {
                studentInvoices.get(invoiceID).add(studentAsInvoiceItem.getObjectID());
            }
            scheduleInvoices.get(studentAsInvoiceItem.getCourseScheduleID()).add(invoiceID);
        }
    }

    private void updateStudentAndScheduleInvoices() {
        String query = generateScheduleInvoiceQuery();
        query += "\n";
        query += generateStudentInvoiceQuery();
        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(CourseBookingEventListenerImpl.TYPE, CourseBookingEventListenerImpl.EVENT_UPDATE_STUDENT_AND_SCHEDULE_INVOICES, null, courseBookingManager.getUser());
        event.setCustomStringField(query);
    }

    private void updateStudentInvoices() {
        String query = generateStudentInvoiceQuery();
        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(CourseBookingEventListenerImpl.TYPE, CourseBookingEventListenerImpl.EVENT_UPDATE_STUDENT_AND_SCHEDULE_INVOICES, null, courseBookingManager.getUser());
        event.setCustomStringField(query);
    }

    private String generateScheduleInvoiceQuery() {
        StringBuilder builder = new StringBuilder();
        for (Integer key : scheduleInvoices.keySet()) {
            if (scheduleInvoices.get(key) != null && scheduleInvoices.get(key).size() > 0) {
                builder.append("DELETE FROM ").append("\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"").append(".courseschedule_invoice WHERE courseschedule_id = ").append(key).append(";\n");

                Set<Integer> invoiceIds = scheduleInvoices.get(key);
                builder.append("INSERT INTO ").append("\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"").append(".courseschedule_invoice(courseschedule_id, invoice_id) values");

                int index = 0;
                for (Integer invID : invoiceIds) {
                    builder.append("(").append(key).append(",").append(invID).append(")" + (index == invoiceIds.size() - 1 ? ";" : ", ") + "\n");
                    index++;
                }
            }
        }

        return builder.toString();
    }

    private String generateStudentInvoiceQuery() {
        StringBuilder builder = new StringBuilder();
        for (Integer key : studentInvoices.keySet()) {
            builder.append("UPDATE ").append("\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"").append(".courseschedulestudent SET invoiceid = ").append(key)
                    .append(" WHERE id in (").append(ServerUtils.getAsCommoDelimited(new ArrayList<>(studentInvoices.get(key)), "0", ",")).append("); \n");
        }

        return builder.toString();
    }

    private NumberData generateScheduleNumber() {
        Integer intNumber = scheduledCourseManager.getScheduleLasNumber();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings != null && settings.getProductNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getProductNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_PROD_PREFIX);
        }
    }

    /**
     * This is temporary method for schedules times fixing
     *
     * @param courseSchedule
     */
    private void initCalculationEndDate(EdsCourseSchedule courseSchedule) {
        EdsUser user = employeeManager.getUser();
        int timeZoneRawOffset = user.getUserTimezone().getRawOffset() / 60000;

        //get time slot of the Schedule course Instructor/Company
        HashMap<Integer, TimeSlotItem> timeSlotItems = getTimeSlotItem(courseSchedule.getLocation().getObjectID());

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(courseSchedule.getStartDate());
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);

        //optional params
        Long sTime = (courseSchedule.getStartDate().getTime() - startTime.getTime().getTime()) / 60000;
        Integer scStartTime = sTime.intValue();  //start course schedule time
        Integer cDuration = courseSchedule.getCourse().getDuration() * 60;    //course duration time

        Calendar date = (Calendar) startTime.clone();
        int duration = 0;
        int scheduleDay = 0;
        do {
            TimeSlotItem timeSlotItem = timeSlotItems.get(date.get(Calendar.DAY_OF_WEEK) - 1);
            int tsStartTime = timeSlotItem.getStartTime() - timeZoneRawOffset; //time slot start time
            int tsEndTime = timeSlotItem.getEndTime() - timeZoneRawOffset; //time slot end time
            int tsLunchStart = timeSlotItem.getLunchStart() - timeZoneRawOffset;//time slot lunch start time
            int tsCoffeeStart = timeSlotItem.getCoffeeStart() - timeZoneRawOffset;//time slot coffee start time

            if (timeSlotItem.getStartTime() != 0 && timeSlotItem.getEndTime() != 0) {
                int totalDurationInDay = 0; //total duration in day
                Integer scTime = scStartTime; //scheduled course start time

                //calculate lunch time from time slot
                int lunchTime = timeSlotItem.getLunchEnd() - timeSlotItem.getLunchStart();

                //calculate break time from time slot
                int breakTime = timeSlotItem.getCoffeeEnd() - timeSlotItem.getCoffeeStart();

                //calculate duration hour(s) in day
                if (scTime != 0) {
                    duration = scTime + cDuration;

                    //lunch time applying to schedule duration
                    if (scTime <= tsLunchStart && duration > tsLunchStart) {
                        totalDurationInDay += lunchTime;
                    }

                    //break time applying to schedule duration
                    if (scTime <= tsCoffeeStart && (duration + lunchTime) > tsCoffeeStart) {
                        totalDurationInDay += breakTime;
                    }

                    totalDurationInDay += duration;

                    scTime = 0;
                } else {
                    duration = tsStartTime + cDuration;

                    totalDurationInDay = duration + lunchTime + breakTime;
                }

                //split day(s) of course schedule duration
                if (tsEndTime < totalDurationInDay) {
                    cDuration = totalDurationInDay - tsEndTime;
                    date.add(Calendar.DAY_OF_MONTH, 1);
                } else {
                    //apply lunch time to duration in day
                    if (scStartTime <= tsLunchStart && duration > tsLunchStart) {
                        duration += lunchTime;
                    }

                    //apply break time to duration in day
                    if (scStartTime <= tsCoffeeStart && (duration + lunchTime) > tsCoffeeStart) {
                        duration += breakTime;
                    }

                    date.set(Calendar.MINUTE, duration);
                    cDuration = 0;
                }

                scheduleDay++;
            } else {
                date.add(Calendar.DAY_OF_MONTH, 1);
            }
        } while (cDuration > 0);

        courseSchedule.setScheduleDuration(scheduleDay);
        courseSchedule.setEndDate(date.getTime());
        if (courseSchedule.getEndDate() != null && courseSchedule.getCourse() != null && courseSchedule.getCourse().getValidity() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(courseSchedule.getEndDate());
            cal.add(Calendar.MONTH, courseSchedule.getCourse().getValidity());
            Date expireDate = cal.getTime();
            courseSchedule.setExpireDate(expireDate);
        }
    }

    @Override
    public Integer getCourseLastIntNumber() {
        return courseManager.getCourseLastIntNumber();
    }
}
