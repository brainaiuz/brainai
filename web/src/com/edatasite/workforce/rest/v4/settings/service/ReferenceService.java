package com.edatasite.workforce.rest.v4.settings.service;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.profile.client.rpc.request.CreateReferenceReq;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ReferenceService {

    private final ReferenceManager referenceManager;
    private final CompanyManager companyManager;

    public ReferenceService(ReferenceManager referenceManager, CompanyManager companyManager) {
        this.referenceManager = referenceManager;
        this.companyManager = companyManager;
    }

    @Transactional
    public ReferenceItem createReference(CreateReferenceReq request) {
        EdsReference edsReference = new EdsReference();
        if (request.getParentId() != null) {
            edsReference.setParent(referenceManager.getReference(request.getParentId()));
        }
        edsReference.setName(request.getName());
        edsReference.setCode(request.getCode());
        edsReference.setDescription(request.getDescription());
        edsReference.setAntonym(request.getAntonym());
        edsReference.setSorder(request.getSorder());
        edsReference.setShortName(request.getShortName());
        edsReference.setColor(request.getColor());
        edsReference.setUpdatedDate(new Date());
        edsReference.setUpdater(((EdsUser) SecurityContext.getInstance().getUser()).getObjectID());

        referenceManager.create(edsReference);

        return edsReference.getRPC();
    }

    @Transactional(readOnly = true)
    public ReferenceItem getById(Integer id) {
        return referenceManager.getReference(id).getRPC();
    }

    @Transactional
    public ReferenceItem getOrCreateOrgBoardReference(String code) {
        EdsReference ref = referenceManager.getByCode(code);
        if (ref == null) {
            ref = new EdsReference();
            Integer companyID = SecurityContext.getCompanyID();
            EdsCompany company = companyManager.get(companyID);
            ref.setName(company.getName());
            ref.setDescription(company.getName());
            ref.setColor("#E3E3E3");
            ref.setCode(code);
            referenceManager.create(ref);
        }
        return ref.getRPC();
    }

    @Transactional
    public ReferenceItem updateReferenceByCode(CreateReferenceReq request) {
        EdsReference reference = referenceManager.getByCode(request.getCode());

        if (reference != null) {
            if (request.getName() != null) {
                reference.setName(request.getName());
            }
            if (request.getDescription() != null) {
                reference.setDescription(request.getDescription());
            }
            if (request.getAntonym() != null) {
                reference.setAntonym(request.getAntonym());
            }
            if (request.getShortName() != null) {
                reference.setShortName(request.getShortName());
            }
            if (request.getColor() != null) {
                reference.setColor(request.getColor());
            }
            if (request.getSorder() != null) {
                reference.setSorder(request.getSorder());
            }
            if (request.getShortName() != null) {
                reference.setShortName(request.getShortName());
            }
            if (request.getCode() != null) {
                reference.setCode(request.getCode());
            }
            reference.setUpdatedDate(new Date());
            reference.setUpdater(((EdsUser) SecurityContext.getInstance().getUser()).getObjectID());
            referenceManager.update(reference);
            return reference.getRPC();
        }
        return null;
    }
}
