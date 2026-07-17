package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsIntroductionPage;
import com.edatasite.workforce.gwt.core.server.db.IntroPageManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("introPageManager")
public class IntroPageManagerImpl extends BaseManager<EdsIntroductionPage> implements IntroPageManager {
    public IntroPageManagerImpl() {
        super(EdsIntroductionPage.class);
    }

    @Override
    public EdsIntroductionPage findByParentFormId(String parentFormId) {
        String sql = "select *from " + getCompanyId() + ".introduction_page ip where ip.parent_form_id = '" + parentFormId + "'";

        List<EdsIntroductionPage> objects = findNative(sql, EdsIntroductionPage.class);
        EdsIntroductionPage page = objects != null && objects.size() > 0  ? objects.get(0) : null;

        return page;
    }

    @Override
    public boolean deleteByIdAndParentForm(String parentFormId, Integer id) {
        String query = "delete from " + getCompanyId() + ".introduction_page ip where ip.parent_form_id =? and p.id = ?";
        try {
            update(query,parentFormId,id);
        }catch (Exception ex){
            return false;
        }
        return true;
    }
}
