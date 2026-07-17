package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/21/12
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bookingitemreservation")
public class EdsBookingItemReservation extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private EdsBookingItem bookingItem;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private EdsUser reservedBy;

    @Column(name = "fromdate")
    private Date from;

    @Column(name = "todate")
    private Date to;

	@Column(name = "deleted")
	private Boolean deleted = false;

    public EdsBookingItem getBookingItem() {
        return bookingItem;
    }

    public void setBookingItem(EdsBookingItem bookingItem) {
        this.bookingItem = bookingItem;
    }

    public EdsUser getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(EdsUser reservedBy) {
        this.reservedBy = reservedBy;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
