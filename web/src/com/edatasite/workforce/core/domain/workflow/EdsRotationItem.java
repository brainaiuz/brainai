package com.edatasite.workforce.core.domain.workflow;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsPosition;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EdsRotationItem extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "empId")
    private Integer empId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "empId", updatable = false, insertable = false)
    private EdsEmployee employee;

    @Column(name = "curDepId")
    private Integer curDepId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "curDepId", updatable = false, insertable = false)
    private EdsDepartment currentDepartment;

    @Column(name = "curPosId")
    private Integer curPosId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "curPosId", updatable = false, insertable = false)
    private EdsPosition currentPosition;

    @Column(name = "newDepID")
    private Integer newDepID;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "newDepID", updatable = false, insertable = false)
    private EdsDepartment newDepartment;

    @Column(name = "newPosId")
    private Integer newPosId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "newPosId", updatable = false, insertable = false)
    private EdsPosition newPosition;


    @Override
    public Integer getObjectID() {
        return null;
    }
}
