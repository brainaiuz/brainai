package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "landing")
public class EdsLanding extends EdsObject implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

//	@OneToMany(cascade = { CascadeType.PERSIST }, mappedBy = "landing")
//	private EdsUser user;

    private String pmFirstView = LANDING_PAGE;
    private String paFirstView = LANDING_PAGE;
    private String availabilityFirstView = LANDING_PAGE;
    private String invoiceFirstView = LANDING_PAGE;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getPMFirstView() {
        return pmFirstView;
    }

    public void setPMFirstView(String pmFirstView) {
        this.pmFirstView = pmFirstView;
    }

    public String getPAFirstView() {
        return paFirstView;
    }

    public void setPAFirstView(String paFirstView) {
        this.paFirstView = paFirstView;
    }

    public String getAvailabilityFirstView() {
        return availabilityFirstView;
    }

    public void setAvailabilityFirstView(String availabilityFirstView) {
        this.availabilityFirstView = availabilityFirstView;
    }

    public String getInvoiceFirstView() {
        return invoiceFirstView;
    }

    public void setInvoiceFirstView(String invoiceFirstView) {
        this.invoiceFirstView = invoiceFirstView;
    }

//	public void setUser(EdsUser user) {
//		this.user = user;
//	}
//
//	public EdsUser getUser() {
//		return user;
//	}
}
