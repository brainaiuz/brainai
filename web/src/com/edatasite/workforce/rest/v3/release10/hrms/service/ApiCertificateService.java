package com.edatasite.workforce.rest.v3.release10.hrms.service;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployment;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentFields;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentType;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.solr.component.CertificateSolrComponent;
import com.edatasite.workforce.core.solr.document.CertificateSolrDoc;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentTypeManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.CertificateDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.CertificateDynamicFieldsDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

@Service
public class ApiCertificateService implements Constants {
    private final HrmsService hrmsService;
    private final EmployeeManager employeeManager;
    private final CertificateOfEmploymentManager certificateOfEmploymentManager;
    private final HrmsServiceLocal hrmsServiceLocal;
    private final CommonServiceLocal commonServiceLocal;
    private final UploadManager uploadManager;
    private final CertificateOfEmploymentTypeManager certificateOfEmploymentTypeManager;
    private final AllInOneService allInOneService;
    private final CertificateSolrComponent certificateSolrComponent;

    @Autowired
    public ApiCertificateService(HrmsService hrmsService, EmployeeManager employeeManager, CertificateOfEmploymentManager certificateOfEmploymentManager, HrmsServiceLocal hrmsServiceLocal, CommonServiceLocal commonServiceLocal, UploadManager uploadManager, CertificateOfEmploymentTypeManager certificateOfEmploymentTypeManager, AllInOneService allInOneService, CertificateSolrComponent certificateSolrComponent) {
        this.hrmsService = hrmsService;
        this.employeeManager = employeeManager;
        this.certificateOfEmploymentManager = certificateOfEmploymentManager;
        this.hrmsServiceLocal = hrmsServiceLocal;
        this.commonServiceLocal = commonServiceLocal;
        this.uploadManager = uploadManager;
        this.certificateOfEmploymentTypeManager = certificateOfEmploymentTypeManager;
        this.allInOneService = allInOneService;
        this.certificateSolrComponent = certificateSolrComponent;
    }

    public ListResultTO<CertificateDto> getCertificateList(ListingFilterParameter filterParameter) {
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_CERTIFICATE_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(hrmsServiceLocal.getCertificateSolrQuery(filterParameter), SolrRequest.METHOD.POST);
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
        Page<CertificateSolrDoc> certificateSolrDocs = certificateSolrComponent.getList(filterParameter);
        String ids = certificateSolrDocs.getContent().stream().map(doc -> String.valueOf(doc.getCertificateId())).collect(Collectors.joining(","));
        ArrayList<CertificateDto> items = new ArrayList<>();
        if (StringUtils.isNotBlank(ids)) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.Certificates);

            List<EdsCertificateOfEmployment> certificateList = certificateOfEmploymentManager.getCertificatesByIds(ids);
            for (EdsCertificateOfEmployment certificate : certificateList) {
                items.add(toDto(certificate, customFieldItems));
            }
        }
        return new ListResultTO<>((int) certificateSolrDocs.getTotalElements(), items);
    }

    @Transactional
    public void save(final CertificateDto dto, final boolean isNew) throws RestException {
        CertificateItem item;
        if (!isNew && dto.getId() != null) {
            item = Optional.ofNullable(hrmsService.getCertificateData(dto.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Certificate with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
        } else {
            item = new CertificateItem();
        }

        if (dto.getCertificateNumber() != null) {
            item.setCertificateNumber(new NumberData(dto.getCertificateNumber()));
        } else if (isNew) {
            NumberData number = certificateOfEmploymentManager.getCertificateNumber();
            int splitterIndex = number.getNumberFormat().lastIndexOf("_");
            String numbering = number.getNumberFormat().substring(splitterIndex + 1);
            DecimalFormat numberFormat;
            if (numbering.length() > 0) {
                StringBuilder nf = new StringBuilder();
                nf.append("0".repeat(numbering.length()));

                numberFormat = new DecimalFormat(nf.toString());
            } else {
                numberFormat = new DecimalFormat("0000");
            }
            item.setCertificateNumber(new NumberData(number.getNumberString() + numberFormat.format(number.getIntNumber())));
        }
        if (dto.getEmployee() != null) {
            EdsEmployee employee = null;
            if (dto.getEmployee().getId() != null) {
                employee = employeeManager.get(dto.getEmployee().getId());
            } else if (dto.getEmployee().getName() != null) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(dto.getEmployee().getName());
            } else if (dto.getEmployee().getCode() != null) {
                employee = employeeManager.getEmployeeByNumber(dto.getEmployee().getCode());
            }
            if (employee == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Employee is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            item.setEmployee(employee.getAsSelectItem());
        }
        if (dto.getType() != null) {
            EdsCertificateOfEmploymentType employmentType = null;
            if (dto.getType().getId() != null) {
                employmentType = certificateOfEmploymentManager.getCertificateType(dto.getType().getId());
            } else if (dto.getType().getName() != null) {
                employmentType = certificateOfEmploymentTypeManager.getByName(dto.getType().getName());
            }
            if (employmentType == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Type is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            item.setCertificateType(employmentType.getAsSelectItem());
        }
        item.setUpdatedDate(new Date());
        item.setStatusCode(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED);
        if (dto.getApprovers() != null) {
            ArrayList<ApproverItemMini> approvers = new ArrayList<>();
            ApprovalListResult approvalListResult = allInOneService.getApprovers(TYPE_CERTIFICATE_OF_EMPLOYMENT, null, false, null, false);
            for (ItemDto approver : dto.getApprovers()) {
                for (ApproverItemMini approverItemMini : approvalListResult.getList()) {
                    if (approver.getId() != null && approver.getId().equals(approverItemMini.getExactEmployee().getId())) {
                        approvers.add(approverItemMini);
                        break;
                    } else if (approver.getName() != null && approver.getName().equals(approverItemMini.getExactEmployee().getName())) {
                        approvers.add(approverItemMini);
                        break;
                    }
                }
            }
            item.setApprovers(approvers);
        }
        EdsCertificateOfEmployment edsCertificate = null;
        if (!isNew) {
            edsCertificate = certificateOfEmploymentManager.get(dto.getId());
        }
        item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Certificates), !isNew ? edsCertificate.getCustomFields() : null));

        if (dto.getDynamicFields() != null) {
            CertificateDynamicFieldsDto dynamicFields = dto.getDynamicFields();
            item.setTextBox1(dynamicFields.getTextBox1());
            item.setTextBox2(dynamicFields.getTextBox2());
            item.setTextBox3(dynamicFields.getTextBox3());
            item.setTextBox4(dynamicFields.getTextBox4());
            item.setTextBox5(dynamicFields.getTextBox5());
            item.setTextBox6(dynamicFields.getTextBox6());
            item.setTextBox7(dynamicFields.getTextBox7());
            item.setTextBox8(dynamicFields.getTextBox8());
            item.setTextBox9(dynamicFields.getTextBox9());
            item.setTextBox10(dynamicFields.getTextBox10());
            item.setTextBox11(dynamicFields.getTextBox11());
            item.setTextBox12(dynamicFields.getTextBox12());
            item.setTextBox13(dynamicFields.getTextBox13());
            item.setTextBox14(dynamicFields.getTextBox14());
            item.setTextBox15(dynamicFields.getTextBox15());
            item.setTextBox16(dynamicFields.getTextBox16());
            item.setTextBox17(dynamicFields.getTextBox17());
            item.setTextBox18(dynamicFields.getTextBox18());
            item.setTextArea1(dynamicFields.getTextArea1());
            item.setTextArea2(dynamicFields.getTextArea2());
            item.setTextArea3(dynamicFields.getTextArea3());
            item.setTextArea4(dynamicFields.getTextArea4());
            item.setTextArea5(dynamicFields.getTextArea5());
            item.setTextArea6(dynamicFields.getTextArea6());
            item.setTextArea7(dynamicFields.getTextArea7());
            item.setTextArea8(dynamicFields.getTextArea8());
        }
        Integer id = hrmsService.saveCertificate(item);
        dto.setId(id);
    }

    @Transactional
    public CertificateDto savePatch(final CertificateDto dto) throws RestException {
        CertificateItem item = Optional.ofNullable(hrmsService.getCertificateData(dto.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Certificate with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));

        Optional.ofNullable(dto.getCertificateNumber()).ifPresent(number -> item.setCertificateNumber(new NumberData(number, null)));
        if (dto.getEmployee() != null) {
            EdsEmployee employee = null;
            if (dto.getEmployee().getId() != null) {
                employee = employeeManager.get(dto.getEmployee().getId());
            } else if (dto.getEmployee().getName() != null) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(dto.getEmployee().getName());
            } else if (dto.getEmployee().getCode() != null) {
                employee = employeeManager.getEmployeeByNumber(dto.getEmployee().getCode());
            }
            if (employee != null) {
                item.setEmployee(employee.getAsSelectItem());
            }
        }
        if (dto.getType() != null) {
            EdsCertificateOfEmploymentType employmentType = null;
            if (dto.getType().getId() != null) {
                employmentType = certificateOfEmploymentManager.getCertificateType(dto.getType().getId());
            } else if (dto.getType().getName() != null) {
                employmentType = certificateOfEmploymentTypeManager.getByName(dto.getType().getName());
            }
            if (employmentType == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Type is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            item.setCertificateType(employmentType.getAsSelectItem());
        }

        Optional.ofNullable(dto.getApprovers()).ifPresent(apps -> {
            if (item.getApprovers() != null) {
                item.getApprovers().clear();
            }
            ArrayList<ApproverItemMini> approvers = new ArrayList<>();
            ApprovalListResult approvalListResult = allInOneService.getApprovers(TYPE_CERTIFICATE_OF_EMPLOYMENT, null, false, null, false);
            for (ItemDto approver : apps) {
                for (ApproverItemMini approverItemMini : approvalListResult.getList()) {
                    if (approver.getId() != null && approver.getId().equals(approverItemMini.getExactEmployee().getId())) {
                        approvers.add(approverItemMini);
                        break;
                    } else if (approver.getName() != null && approver.getName().equals(approverItemMini.getExactEmployee().getName())) {
                        approvers.add(approverItemMini);
                        break;
                    }
                }
            }
            item.setApprovers(approvers);
        });

        Optional.ofNullable(dto.getCustomFields()).ifPresent(c -> {
            EdsCertificateOfEmployment edsCertificate = certificateOfEmploymentManager.get(dto.getId());
            item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Certificates), edsCertificate.getCustomFields()));
        });
        item.setUpdatedDate(new Date());

        if (dto.getDynamicFields() != null) {
            CertificateDynamicFieldsDto dynamicFields = dto.getDynamicFields();
            Optional.ofNullable(dynamicFields.getTextBox1()).ifPresent(item::setTextBox1);
            Optional.ofNullable(dynamicFields.getTextBox2()).ifPresent(item::setTextBox2);
            Optional.ofNullable(dynamicFields.getTextBox3()).ifPresent(item::setTextBox3);
            Optional.ofNullable(dynamicFields.getTextBox4()).ifPresent(item::setTextBox4);
            Optional.ofNullable(dynamicFields.getTextBox5()).ifPresent(item::setTextBox5);
            Optional.ofNullable(dynamicFields.getTextBox6()).ifPresent(item::setTextBox6);
            Optional.ofNullable(dynamicFields.getTextBox7()).ifPresent(item::setTextBox7);
            Optional.ofNullable(dynamicFields.getTextBox8()).ifPresent(item::setTextBox8);
            Optional.ofNullable(dynamicFields.getTextBox9()).ifPresent(item::setTextBox9);
            Optional.ofNullable(dynamicFields.getTextBox10()).ifPresent(item::setTextBox10);
            Optional.ofNullable(dynamicFields.getTextBox11()).ifPresent(item::setTextBox11);
            Optional.ofNullable(dynamicFields.getTextBox12()).ifPresent(item::setTextBox12);
            Optional.ofNullable(dynamicFields.getTextBox13()).ifPresent(item::setTextBox13);
            Optional.ofNullable(dynamicFields.getTextBox14()).ifPresent(item::setTextBox14);
            Optional.ofNullable(dynamicFields.getTextBox15()).ifPresent(item::setTextBox15);
            Optional.ofNullable(dynamicFields.getTextBox16()).ifPresent(item::setTextBox16);
            Optional.ofNullable(dynamicFields.getTextBox17()).ifPresent(item::setTextBox17);
            Optional.ofNullable(dynamicFields.getTextBox18()).ifPresent(item::setTextBox18);
        }
        hrmsService.saveCertificate(item);
        dto.setId(item.getObjectId());
        return dto;
    }

    @Transactional(readOnly = true)
    public CertificateDto getById(final Integer id) throws RestException {
        Optional.ofNullable(certificateOfEmploymentManager.get(id)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Certificate with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
        CertificateItem item = hrmsService.getCertificateData(id);
        return ConvertUtils.toDTO(item);
    }

    private CertificateDto toDto(EdsCertificateOfEmployment item, ArrayList<CompanyCustomFieldItem> customFieldsItems) {
        CertificateDto dto = new CertificateDto();
        dto.setId(item.getObjectID());
        dto.setCertificateNumber(item.getNumber());
        EdsEmployee employee = employeeManager.get(item.getEmployeeid());
        dto.setEmployee(new ItemDto(employee.getObjectID(), employee.getFullName(), employee.getProfile().getEmployeeCode()));
        dto.setType(item.getCertificateType() != null ? new IdName(item.getCertificateType().getObjectID(), item.getCertificateType().getName()) : null);
        final ArrayList<FileResource> files = new ArrayList<>();
        if (item.getAttachmentIDs() != null && !"".equals(item.getAttachmentIDs())) {
            for (final String id : item.getAttachmentIDs().split(",")) {
                if (id == null)
                    continue;
                final EdsFileBody body = (EdsFileBody) this.uploadManager.get(Integer.valueOf(id));
                if (body == null)
                    continue;
                final FileResource fr = new FileResource();
                fr.setBodyId(body.getObjectID());
                fr.setDescription(body.getDescription());
                fr.setAmazonLink(commonServiceLocal.getImageUrl(body.getObjectID()));
                files.add(fr);
            }
            dto.setAttachments(files.stream().map(f -> new AttachmentTO(f.getFileName(), f.getDownloadUrl())).collect(Collectors.toList()));
        }
        if (item.getCertificateType() != null && item.getCertificateType().getCustomHTML() != null && !"".equals(item.getCertificateType().getCustomHTML())) {
            dto.setContent(hrmsServiceLocal.replaceVelocity(item.getCertificateType().getCustomHTML(), employee.getObjectID(), files, item.getObjectID()));
        } else {
            dto.setContent(hrmsServiceLocal.replaceVelocity(item.getContentHTML(), employee.getObjectID(), files, item.getObjectID()));
        }

        if (customFieldsItems == null) {
            customFieldsItems = commonServiceLocal.getCompanyCustomFields(ViewName.Certificates);
        } else {
            customFieldsItems = new ArrayList<>(customFieldsItems);
        }

        if (item.getCustomFields() != null && !CollectionUtils.isEmpty(customFieldsItems)) {
            customFieldsItems = CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), customFieldsItems);
            dto.setCustomFields(customFieldsItems.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }

        if (item.getFields() != null) {
            EdsCertificateOfEmploymentFields dynamicFields = item.getFields();
            CertificateDynamicFieldsDto fields = new CertificateDynamicFieldsDto();
            fields.setTextBox1(dynamicFields.getTextBox1());
            fields.setTextBox2(dynamicFields.getTextBox2());
            fields.setTextBox3(dynamicFields.getTextBox3());
            fields.setTextBox4(dynamicFields.getTextBox4());
            fields.setTextBox5(dynamicFields.getTextBox5());
            fields.setTextBox6(dynamicFields.getTextBox6());
            fields.setTextBox7(dynamicFields.getTextBox7());
            fields.setTextBox8(dynamicFields.getTextBox8());
            fields.setTextBox9(dynamicFields.getTextBox9());
            fields.setTextBox10(dynamicFields.getTextBox10());
            fields.setTextBox11(dynamicFields.getTextBox11());
            fields.setTextBox12(dynamicFields.getTextBox12());
            fields.setTextBox13(dynamicFields.getTextBox13());
            fields.setTextBox14(dynamicFields.getTextBox14());
            fields.setTextBox15(dynamicFields.getTextBox15());
            fields.setTextBox16(dynamicFields.getTextBox16());
            fields.setTextBox17(dynamicFields.getTextBox17());
            fields.setTextBox18(dynamicFields.getTextBox18());
            fields.setTextArea1(dynamicFields.getTextArea1());
            fields.setTextArea2(dynamicFields.getTextArea2());
            fields.setTextArea3(dynamicFields.getTextArea3());
            fields.setTextArea4(dynamicFields.getTextArea4());
            fields.setTextArea5(dynamicFields.getTextArea5());
            fields.setTextArea6(dynamicFields.getTextArea6());
            fields.setTextArea7(dynamicFields.getTextArea7());
            fields.setTextArea8(dynamicFields.getTextArea8());
            dto.setDynamicFields(fields);
        }
        return dto;
    }
}


