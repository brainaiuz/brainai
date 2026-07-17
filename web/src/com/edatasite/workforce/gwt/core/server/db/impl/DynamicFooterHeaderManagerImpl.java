package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.pdf.EdsPdfDynamicFooterHeader;
import com.edatasite.workforce.gwt.core.server.db.DynamicFooterHeaderManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("dynamicFooterHeaderManager")
public class DynamicFooterHeaderManagerImpl extends BaseManager<EdsPdfDynamicFooterHeader> implements DynamicFooterHeaderManager {
    public DynamicFooterHeaderManagerImpl() {
        super(EdsPdfDynamicFooterHeader.class);
    }

    @Override
    public EdsPdfDynamicFooterHeader getByKeyAndTemplateSettingId(String key, Integer templateId) {
        return (EdsPdfDynamicFooterHeader) findSingle("SELECT dfh FROM EdsPdfDynamicFooterHeader dfh WHERE dfh.key = ? and dfh.template.id = ?", key.trim(), templateId);
    }

    @Override
    public void updateDynamicSettingsBytemplateId(Integer pdfId) {
        String deleteQuery = "delete from " + getCompanyId() + ".dynamic_footer_header where templateid is not null and templateid = " + pdfId;
        updateNative(deleteQuery);

        String query = "INSERT INTO " + getCompanyId() + ".dynamic_footer_header(enable, key, value, templateid) " +
                " select true, 'HEADER_RIGHT', '<div style=\"text-align: right;\"><span style=\"font-size: 10px;\">${pagination}</span><br><br><span style=\"font-size: 14px;\">${document_title}</span></div>', id " +
                " from " + getCompanyId() + ".pdftemplate_settings where id = " + pdfId;
        updateNative(query);

        String query2 = "INSERT INTO " + getCompanyId() + ".dynamic_footer_header(enable, key, value, templateid) " +
                " select true, 'HEADER_CENTER', '<div>${company_name}</div>', id " +
                " from " + getCompanyId() + ".pdftemplate_settings where id = " + pdfId;
        updateNative(query2);

        String query3 = "INSERT INTO " + getCompanyId() + ".dynamic_footer_header(enable, key, value, templateid) " +
                " select true, 'HEADER_LEFT', '<div style=\"text-align: left;\">${company_logo}</div>', id " +
                " from " + getCompanyId() + ".pdftemplate_settings where id = " + pdfId;
        updateNative(query3);

        String query4 = "INSERT INTO " + getCompanyId() + ".dynamic_footer_header(enable, key, value, templateid) " +
                " select true, 'FOOTER_RIGHT', '<div style=\"text-align: right;\">${powered_by}</div>', id " +
                " from " + getCompanyId() + ".pdftemplate_settings where id = " + pdfId;
        updateNative(query4);

        String query5 = "INSERT INTO " + getCompanyId() + ".dynamic_footer_header(enable, key, value, templateid) " +
                " select true, 'FOOTER_CENTER', '<div style=\"text-align: justify; vertical-align:top;\"><span style=\"font-size: 12px;\">${company_main_address}</span><div style=\"font-size: 7pt; color:#939598; line-height:1.4;\"><span>${phone_number} ${email_id} ${website}</span></div></div>', id " +
                " from " + getCompanyId() + ".pdftemplate_settings where id = " + pdfId;
        updateNative(query5);

        String query6 = "INSERT INTO " + getCompanyId() + ".dynamic_footer_header(enable, key, value, templateid) " +
                " select true, 'FOOTER_LEFT', '<div style=\"text-align: left;\">${qr_code}</div>', id " +
                " from " + getCompanyId() + ".pdftemplate_settings where id = " + pdfId;
        updateNative(query6);
    }

    @Override
    public List<EdsPdfDynamicFooterHeader> getDefaultFooterHeaderValues() {
        return findNative("select pdf.* from " + getCompanyId() + ".dynamic_footer_header pdf where pdf.key like 'DEFAULT_%' and pdf.templateid is null", EdsPdfDynamicFooterHeader.class);
    }
}
