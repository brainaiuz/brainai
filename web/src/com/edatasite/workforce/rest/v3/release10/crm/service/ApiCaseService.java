package com.edatasite.workforce.rest.v3.release10.crm.service;

import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.RelationHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.CaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.edatasite.workforce.core.domain.crm.EdsCase.NEW;
import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_CASE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

@Service
public class ApiCaseService implements Constants {

    private final CRMService crmService;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final AllInOneService allInOneService;
    private final CaseManager caseManager;
    private final ReferenceManager referenceManager;
    private final EmployeeManager employeeManager;
    private final DocumentsServiceLocal documentsServiceLocal;
    private final CommonService commonService;
    private final RelationHelperV3 relationHelper;

    @Autowired
    public ApiCaseService(CRMService crmService, AttachmentUtilsManager attachmentUtilsManager, AllInOneService allInOneService, CaseManager caseManager, ReferenceManager referenceManager, EmployeeManager employeeManager, DocumentsServiceLocal documentsServiceLocal, CommonService commonService, RelationHelperV3 relationHelper) {
        this.crmService = crmService;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.allInOneService = allInOneService;
        this.caseManager = caseManager;
        this.referenceManager = referenceManager;
        this.employeeManager = employeeManager;
        this.documentsServiceLocal = documentsServiceLocal;
        this.commonService = commonService;
        this.relationHelper = relationHelper;
    }

    public ListResultTO<CaseDto> getCasesList(ListingFilterParameter fp) {
        CaseList caseList = crmService.getCases(fp);

        ListResultTO<CaseDto> cases = new ListResultTO<>();
        if (caseList != null) {
            List<Integer> ids = caseList.getList().stream().map(doc -> Objects.requireNonNull(doc.getObjectId())).toList();
            cases.setTotalNumber(ids.size());
            ArrayList<CaseDto> items = new ArrayList<>();
            ids.forEach(id -> {
                CaseItem item = crmService.getCase(id, true);
                List<FileResource> files = attachmentUtilsManager.getAttachments(F_CASE, id, id);
                item.setNotes(allInOneService.getNotes(id, TYPE_CASE));
                items.add(ConvertUtils.toDto(item, files));
            });
            cases.setItems(items);
        }
        return cases;
    }

    @Transactional(readOnly = true)
    public CaseDto getCaseById(Integer id) throws RestException {
        Optional.ofNullable(caseManager.get(id)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Case with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));
        CaseItem item = crmService.getCase(id, true);
        List<FileResource> files = attachmentUtilsManager.getAttachments(F_CASE, id, id);
        return ConvertUtils.toDto(item, files);
    }

    @Transactional
    public CaseDto save(final CaseDto caseDto, boolean isNew) throws RestException {
        CaseItem caseItem;
        if (!isNew) {
            Optional.ofNullable(caseManager.get(caseDto.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Case with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
            caseItem = crmService.editCase(caseDto.getId(), null, null);
        } else {
            caseItem = new CaseItem();
        }

        caseItem.setSubject(caseDto.getSubject());
        caseItem.setAccountId(null);
        caseItem.setCrmContactID(null);
        caseItem.setLeadId(null);
        if (caseDto.getReportedBy() != null) {
            switch (caseDto.getReportedBy().getReportedByType()) {
                case "Lead":
                    caseItem.setLeadId(caseDto.getReportedBy().getReporter().getId());
                    caseItem.setLead(caseDto.getReportedBy().getReporter().getCode());
                case "Contact":
                    caseItem.setCrmContactID(caseDto.getReportedBy().getReporter().getId());
                    caseItem.setCrmContact(caseDto.getReportedBy().getReporter().getCode());
                case "Account":
                    caseItem.setAccountId(caseDto.getReportedBy().getReporter().getId());
                    caseItem.setAccountName(caseDto.getReportedBy().getReporter().getCode());
                case "Other":
                    caseItem.setFirstName(caseDto.getReportedBy().getFirstName());
                    caseItem.setLastName(caseDto.getReportedBy().getLastName());
                    caseItem.setCompany(caseDto.getReportedBy().getCompany());
                    caseItem.setEmail(caseDto.getReportedBy().getEmail());
                    caseItem.setPhone(caseDto.getReportedBy().getPhone());
                    caseItem.setFax(caseDto.getReportedBy().getFax());
            }
        }

        caseItem.setDescription(caseDto.getDescription());
        if (caseDto.getStatus() != null) {
            if (!caseDto.getStatus().idIsNull()) {
                caseItem.setStatus(new SelectItem(caseDto.getStatus().getId()));
            } else if (!caseDto.getStatus().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getStatus().getCode())).ifPresent(s -> caseItem.setStatus(new SelectItem(s.getObjectID())));
            }
        } else {
            Optional.ofNullable(referenceManager.getByCode(NEW)).ifPresent(s -> caseItem.setStatus(new SelectItem(s.getObjectID())));
        }

        if (caseDto.getPriority() != null) {
            if (!caseDto.getPriority().idIsNull()) {
                caseItem.setPriorityId(caseDto.getPriority().getId());
            } else if (!caseDto.getPriority().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getPriority().getCode())).ifPresent(s -> caseItem.setPriorityId(s.getObjectID()));
            }
        }

        if (caseDto.getType() != null) {
            if (!caseDto.getType().idIsNull()) {
                caseItem.setTypeId(caseDto.getType().getId());
            } else if (!caseDto.getType().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getType().getCode())).ifPresent(s -> caseItem.setTypeId(s.getObjectID()));
            }
        }

        caseItem.setDepartmentID(null);
        caseItem.setCaseAssigneeId(null);
        if (caseDto.getAssignee() != null) {
            if (caseDto.getAssignee().getName() != null && caseDto.getAssignee().getName().contains("(Department)")) {
                caseItem.setDepartmentID(caseDto.getAssignee().getId());
                caseItem.setDepartment(caseDto.getAssignee().getName());
            } else {
                caseItem.setCaseAssigneeId(caseDto.getAssignee().getId());
                caseItem.setCaseAssigneeName(caseDto.getAssignee().getName());
            }
        }

        if (caseDto.getResolver() != null) {
            caseItem.setResolverId(caseDto.getResolver().getId());
        }

        if (caseDto.getOrigin() != null) {
            if (!caseDto.getOrigin().idIsNull()) {
                caseItem.setCaseOriginId(caseDto.getOrigin().getId());
            } else if (!caseDto.getOrigin().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getOrigin().getCode())).ifPresent(s -> caseItem.setCaseOriginId(s.getObjectID()));
            }
        }

        if (caseDto.getReason() != null) {
            if (!caseDto.getReason().idIsNull()) {
                caseItem.setCaseReasonId(caseDto.getReason().getId());
            } else if (!caseDto.getReason().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getReason().getCode())).ifPresent(s -> caseItem.setCaseReasonId(s.getObjectID()));
            }
        }

        if (caseDto.getNotes() != null && !caseDto.getNotes().isEmpty()) {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            for (NoteDto noteDto : caseDto.getNotes()) {
                notes.add(ConvertUtils.toEntity(noteDto, employeeManager.getUser().getName()));
            }
            caseItem.setNotes(notes);
        }

        EdsCase edsCase = null;
        if (!isNew) {
            edsCase = caseManager.get(caseDto.getId());
        }
        caseItem.setCustomFields(CustomFieldsUtils.convertCustomFields(caseDto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmCase), !isNew ? edsCase.getCustomFields() : null));

        if (caseDto.getRelations() != null && !caseDto.getRelations().isEmpty()) {
            ArrayList<RelationItem> relations = new ArrayList<>();
            caseDto.getRelations().forEach(relation -> relations.add(relationHelper.convertRelation(relation, caseItem.getObjectId(), caseItem.getSubject(), TYPE_CASE)));
            caseItem.setRelations(relations);
        }

        SelectItem result = crmService.saveCase(caseItem, false);

        caseDto.setId(result.getId());
        caseDto.setCreatedAt(caseItem.getCreatedDate());
        caseDto.setUpdatedAt(caseItem.getLastUpdatedDate());
        caseDto.setNumber(result.getNumber());

        return caseDto;
    }

    @Transactional
    public CaseDto savePatch(final CaseDto caseDto) throws RestException {
        Optional.ofNullable(caseManager.get(caseDto.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Case with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));

        CaseItem caseItem = crmService.editCase(caseDto.getId(), null, null);

        Optional.ofNullable(caseDto.getSubject()).ifPresent(caseItem::setSubject);
        Optional.ofNullable(caseDto.getReportedBy()).ifPresent(r -> {
            switch (caseDto.getReportedBy().getReportedByType()) {
                case "Lead":
                    caseItem.setLeadId(caseDto.getReportedBy().getReporter().getId());
                    caseItem.setLead(caseDto.getReportedBy().getReporter().getCode());
                case "Contact":
                    caseItem.setCrmContactID(caseDto.getReportedBy().getReporter().getId());
                    caseItem.setCrmContact(caseDto.getReportedBy().getReporter().getCode());
                case "Account":
                    caseItem.setAccountId(caseDto.getReportedBy().getReporter().getId());
                    caseItem.setAccountName(caseDto.getReportedBy().getReporter().getCode());
                case "Other":
                    caseItem.setFirstName(caseDto.getReportedBy().getFirstName());
                    caseItem.setLastName(caseDto.getReportedBy().getLastName());
                    caseItem.setCompany(caseDto.getReportedBy().getCompany());
                    caseItem.setEmail(caseDto.getReportedBy().getEmail());
                    caseItem.setPhone(caseDto.getReportedBy().getPhone());
                    caseItem.setFax(caseDto.getReportedBy().getFax());
            }
        });
        Optional.ofNullable(caseDto.getDescription()).ifPresent(caseItem::setDescription);
        Optional.ofNullable(caseDto.getStatus()).ifPresent(s -> {
            if (!caseDto.getStatus().idIsNull()) {
                caseItem.setStatus(new SelectItem(caseDto.getStatus().getId()));
            } else if (!caseDto.getStatus().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getStatus().getCode())).ifPresent(st -> caseItem.setStatus(new SelectItem(st.getObjectID())));
            }
        });

        Optional.ofNullable(caseDto.getPriority()).ifPresent(p -> {
            if (!caseDto.getPriority().idIsNull()) {
                caseItem.setPriorityId(caseDto.getPriority().getId());
            } else if (!caseDto.getPriority().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getPriority().getCode())).ifPresent(s -> caseItem.setPriorityId(s.getObjectID()));
            }
        });

        Optional.ofNullable(caseDto.getType()).ifPresent(t -> {
            if (!caseDto.getType().idIsNull()) {
                caseItem.setTypeId(caseDto.getType().getId());
            } else if (!caseDto.getType().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getType().getCode())).ifPresent(s -> caseItem.setTypeId(s.getObjectID()));
            }
        });

        Optional.ofNullable(caseDto.getAssignee()).ifPresent(a -> {
            caseItem.setDepartmentID(null);
            caseItem.setCaseAssigneeId(null);
            if (caseDto.getAssignee().getName() != null && caseDto.getAssignee().getName().contains("(Department)")) {
                caseItem.setDepartmentID(caseDto.getAssignee().getId());
                caseItem.setDepartment(caseDto.getAssignee().getName());
            } else {
                caseItem.setCaseAssigneeId(caseDto.getAssignee().getId());
                caseItem.setCaseAssigneeName(caseDto.getAssignee().getName());
            }
        });

        Optional.ofNullable(caseDto.getResolver()).ifPresent(r -> caseItem.setResolverId(caseDto.getResolver().getId()));
        Optional.ofNullable(caseDto.getOrigin()).ifPresent(o -> {
            if (!caseDto.getOrigin().idIsNull()) {
                caseItem.setCaseOriginId(caseDto.getOrigin().getId());
            } else if (!caseDto.getOrigin().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getOrigin().getCode())).ifPresent(s -> caseItem.setCaseOriginId(s.getObjectID()));
            }
        });

        Optional.ofNullable(caseDto.getReason()).ifPresent(r -> {
            if (!caseDto.getReason().idIsNull()) {
                caseItem.setCaseReasonId(caseDto.getReason().getId());
            } else if (!caseDto.getReason().codeIsBlank()) {
                Optional.ofNullable(referenceManager.getByCode(caseDto.getReason().getCode())).ifPresent(s -> caseItem.setCaseReasonId(s.getObjectID()));
            }
        });

        Optional.ofNullable(caseDto.getNotes()).ifPresent(n -> {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            for (NoteDto noteDto : caseDto.getNotes()) {
                notes.add(ConvertUtils.toEntity(noteDto, employeeManager.getUser().getName()));
            }
            caseItem.setNotes(notes);
        });

        Optional.ofNullable(caseDto.getCustomFields()).ifPresent(c -> {
            EdsCase edsCase = caseManager.get(caseDto.getId());
            caseItem.setCustomFields(CustomFieldsUtils.convertCustomFields(caseDto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmCase), edsCase.getCustomFields()));
        });

        Optional.ofNullable(caseDto.getRelations()).ifPresent(relationDtos -> {
            ArrayList<RelationItem> relations = new ArrayList<>();
            relationDtos.forEach(relation -> relations.add(relationHelper.convertRelation(relation, caseItem.getObjectId(), caseItem.getSubject(), TYPE_CASE)));
            caseItem.setRelations(relations);
        });

        crmService.saveCase(caseItem, false);
        caseDto.setCreatedAt(caseItem.getCreatedDate());
        caseDto.setUpdatedAt(caseItem.getLastUpdatedDate());
        caseDto.setNumber(caseItem.getCaseNumber());
        return caseDto;
    }
}
