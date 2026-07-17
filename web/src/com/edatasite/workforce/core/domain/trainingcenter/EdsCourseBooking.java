package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCourseBookingCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseBookingRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking.CourseBookingItem;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "coursebooking")
public class EdsCourseBooking extends EdsObject implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "number")
    private String number;

    @Column(name = "intNumber")
    private Integer intNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private EdsCrmAccount customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private EdsCrmContact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private EdsLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;    //status contains {DRAFT, SUBMITTED_TO_MANAGER, APPROVE, PAID, REJECTED}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type; //type contains {BY APPROVAL, PAY ONLINE, PAY UPON ARRIVAL, PAY BY BANK TRANSFER}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id")
    private EdsUser updater;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "courseBooking")
    private List<EdsCourseScheduleStudent> students = new ArrayList<>();

    @Column(name = "invoiceid")
    private Integer invoiceID;

    @Column(name = "creationDate")
    private Date creationDate;

    private Boolean deleted = false;

    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsCourseBookingCustomFields customFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public EdsCrmAccount getCustomer() {
        return customer;
    }

    public void setCustomer(EdsCrmAccount customer) {
        this.customer = customer;
    }

    public EdsCrmContact getContact() {
        return contact;
    }

    public void setContact(EdsCrmContact contact) {
        this.contact = contact;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public List<EdsCourseScheduleStudent> getStudents() {
        return students;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getCalculatedAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (EdsCourseScheduleStudent st : students) {
            if (st.getStatus() != null && !st.getStatus().getCode().equals(EdsCourseScheduleStudent.STUDENT_COURSE_SCHEDULE_REJECTED)) {
                amount = amount.add(st.getCourseScheduleBooking().getPrice());
                amount = amount.add(st.getCourseScheduleBooking().getStopFee());
            }
        }
        return amount;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public EdsCourseBookingCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCourseBookingCustomFields customFields) {
        this.customFields = customFields;
    }

    public CourseBookingItem getRPC() {
        CourseBookingItem courseBookingItem = new CourseBookingItem();
        courseBookingItem.setObjectID(getObjectID());
        courseBookingItem.setNumber(getNumber());
        if (getCustomer() != null) {
            courseBookingItem.setCustomer(new SelectItem(getCustomer().getObjectID(), getCustomer().getName()));
        }

        if (getContact() != null) {
            courseBookingItem.setContact(new SelectItem(getContact().getObjectID(), getContact().getName()));
        }
        if (getLocation() != null) {
            courseBookingItem.setLocation(getLocation().getAsSelectItem());
        }
        if (getStatus() != null) {
            SelectItem status = new SelectItem(getStatus().getObjectID(), getStatus().getName());
            status.setCode(getStatus().getCode());
            courseBookingItem.setStatus(status);
        }
        if (getUpdater() != null) {
            courseBookingItem.setUpdater(new SelectItem(getUpdater().getObjectID(), getUpdater().getName()));
        }
        if (getCreator() != null) {
            courseBookingItem.setCreator(new SelectItem(getCreator().getObjectID(), getCreator().getName()));
        }
        if (getType() != null) {
            SelectItem type = new SelectItem(getStatus().getObjectID(), getStatus().getName());
            type.setCode(getStatus().getCode());
            courseBookingItem.setType(type);
        }
        if (getCreationDate() != null) {
            courseBookingItem.setCreationDate(getCreationDate());
        }
        courseBookingItem.setInvoiceID(getInvoiceID());
        return courseBookingItem;
    }

    public SolrInputDocument wrapToSolrDocument() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrCourseBookingRepresenter.FIELD_COMPANY_ID, SecurityContext.getCompanyID());
        doc.addField(SolrCourseBookingRepresenter.FIELD_COMPOSITE_ID, SecurityContext.getCompanyID() + "_" + getObjectID());
        doc.addField(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID, getObjectID());
        doc.addField(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_NUMBER, getNumber());

        if (getCustomer() != null) {
            doc.addField(SolrCourseBookingRepresenter.FIELD_CUSTOMER_ID, getCustomer().getObjectID());
            doc.addField(SolrCourseBookingRepresenter.FIELD_CUSTOMER_NAME, getCustomer().getName());
            doc.addField(SolrCourseBookingRepresenter.FIELD_CUSTOMER_ID_NAME, getCustomer().getObjectID() + SolrCourseBookingRepresenter.SPLIT + getCustomer().getName());
        }

        if (getContact() != null) {
            doc.addField(SolrCourseBookingRepresenter.FIELD_MANAGER_ID, getContact().getObjectID());
            doc.addField(SolrCourseBookingRepresenter.FIELD_MANAGER_NAME, getContact().getFullName());
            doc.addField(SolrCourseBookingRepresenter.FIELD_MANAGER_ID_NAME, getContact().getObjectID() + SolrCourseBookingRepresenter.SPLIT + getContact().getName());
        }

        if (getLocation() != null) {
            doc.addField(SolrCourseBookingRepresenter.FIELD_LOCATION_ID, getLocation().getObjectID());
            doc.addField(SolrCourseBookingRepresenter.FIELD_LOCATION_NAME, getLocation().getName());
            doc.addField(SolrCourseBookingRepresenter.FIELD_LOCATION_ID_NAME, getLocation().getObjectID() + SolrCourseBookingRepresenter.SPLIT + getLocation().getName());
        }

        if (getStatus() != null) {
            doc.addField(SolrCourseBookingRepresenter.FIELD_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrCourseBookingRepresenter.FIELD_STATUS_NAME, getStatus().getName());
            doc.addField(SolrCourseBookingRepresenter.FIELD_STATUS_ID_NAME, getStatus().getObjectID() + SolrCourseBookingRepresenter.SPLIT + getStatus().getName());
            doc.addField(SolrCourseBookingRepresenter.FIELD_STATUS_CODE, getStatus().getCode());

            if (BOOKING_APPROVED.equals(getStatus().getCode()) || BOOKING_REJECTED.equals(getStatus().getCode())) {
                if (getUpdater() == null) {
                    doc.addField(SolrCourseBookingRepresenter.FIELD_UPDATER, "Client");
                } else {
                    doc.addField(SolrCourseBookingRepresenter.FIELD_UPDATER, getUpdater().getFullName());
                    doc.addField(SolrCourseBookingRepresenter.FIELD_UPDATER_ID, getUpdater().getObjectID());
                }
            }
        }

        if (getType() != null) {
            doc.addField(SolrCourseBookingRepresenter.FIELD_TYPE_ID, getType().getObjectID());
            doc.addField(SolrCourseBookingRepresenter.FIELD_TYPE_NAME, getType().getName());
            doc.addField(SolrCourseBookingRepresenter.FIELD_TYPE_ID_NAME, getType().getObjectID() + SolrCourseBookingRepresenter.SPLIT + getType().getName());
            doc.addField(SolrCourseBookingRepresenter.FIELD_TYPE_CODE, getType().getCode());
        }

        if (getCreator() != null) {
            doc.addField(SolrCourseBookingRepresenter.FIELD_CREATOR_ID, getCreator().getObjectID());
            doc.addField(SolrCourseBookingRepresenter.FIELD_CREATOR, getCreator().getFullName());
            doc.addField(SolrCourseBookingRepresenter.FIELD_CREATOR_ID_NAME, getCreator().getObjectID() + SolrCourseBookingRepresenter.SPLIT + getCreator().getName());
        }

        doc.addField(SolrCourseBookingRepresenter.FIELD_CREATED_DATE, getCreationDate());
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    public static CourseBookingItem wrapSolrDocumentToRPC(SolrDocument doc) {
        CourseBookingItem bookingItem = new CourseBookingItem();
        bookingItem.setObjectID(SolrUtils.asInteger(doc, SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID));
        bookingItem.setNumber(SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_NUMBER));
        bookingItem.setCustomer(new SelectItem(SolrUtils.asInteger(doc, SolrCourseBookingRepresenter.FIELD_CUSTOMER_ID), SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_CUSTOMER_NAME)));
        bookingItem.setContact(new SelectItem(SolrUtils.asInteger(doc, SolrCourseBookingRepresenter.FIELD_MANAGER_ID), SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_MANAGER_NAME)));
        bookingItem.setLocation(new SelectItem(SolrUtils.asInteger(doc, SolrCourseBookingRepresenter.FIELD_LOCATION_ID), SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_LOCATION_NAME)));
        SelectItem status = new SelectItem(SolrUtils.asInteger(doc, SolrCourseBookingRepresenter.FIELD_STATUS_ID), SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_STATUS_NAME));
        status.setCode(SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_STATUS_CODE));
        bookingItem.setStatus(status);
        SelectItem type = new SelectItem(SolrUtils.asInteger(doc, SolrCourseBookingRepresenter.FIELD_TYPE_ID), SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_TYPE_NAME));
        type.setCode(SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_TYPE_CODE));
        bookingItem.setType(type);
        bookingItem.setCreationDate(SolrUtils.asDate(doc, SolrCourseBookingRepresenter.FIELD_CREATED_DATE));
        bookingItem.setCreator(new SelectItem(SolrUtils.asInteger(doc, SolrCourseBookingRepresenter.FIELD_CREATOR_ID), SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_CREATOR)));
        bookingItem.setUpdater(new SelectItem(SolrUtils.asInteger(doc, SolrCourseBookingRepresenter.FIELD_UPDATER_ID), SolrUtils.asString(doc, SolrCourseBookingRepresenter.FIELD_UPDATER)));
        return bookingItem;
    }
}
