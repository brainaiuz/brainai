package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsBookingItemCustomFields;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/21/12
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bookingitem")
public class EdsBookingItem extends EdsObject{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	@Column(name = "name")
	private String name;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	private EdsReference category;

	@Column(name = "description")
	private String description;

	@Column(name = "itemnumber")
	private String itemNumber;

	@Column(name = "intNumber")
	private Integer intNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private EdsLocation location;

	@Column(name="deleted", columnDefinition = "Boolean default false")
	private Boolean deleted = false;

	@OneToOne
	@JoinColumn(name = "customfieldsid")
	private EdsBookingItemCustomFields customFields;

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public Integer getIntNumber() {
		return intNumber;
	}

	public void setIntNumber(Integer intNumber) {
		this.intNumber = intNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EdsReference getCategory() {
		return category;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setCategory(EdsReference category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	private boolean status;

	@Override
	public Integer getObjectID() {
		return objectID;
	}

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

	public EdsBookingItemCustomFields getCustomFields() {
		return customFields;
	}

	public void setCustomFields(EdsBookingItemCustomFields customFields) {
		this.customFields = customFields;
	}
}
