package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/16/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "enquiryItem")
public class EdsEnquiryItem extends EdsBaseInvoiceItem {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enquiry_id")
    private EdsEnquiry enquiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session")
    private EdsReference session;

    @Column(name = "venue")
    private String venue;

    @Column(name = "dateRequired")
    private Date dateRequired;

    public EdsEnquiry getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(EdsEnquiry enquiry) {
        this.enquiry = enquiry;
    }

    public EdsReference getSession() {
        return session;
    }

    public void setSession(EdsReference session) {
        this.session = session;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Date getDateRequired() {
        return dateRequired;
    }

    public void setDateRequired(Date dateRequired) {
        this.dateRequired = dateRequired;
    }
}
