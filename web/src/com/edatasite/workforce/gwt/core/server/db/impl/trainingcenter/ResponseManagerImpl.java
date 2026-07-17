package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsTCResponse;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ResponseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 15.09.12
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */

@Repository("responseManager")
public class ResponseManagerImpl extends BaseManager<EdsTCResponse> implements ResponseManager {

	public ResponseManagerImpl() {
		super(EdsTCResponse.class);
	}

    public List<EdsTCResponse> getResponseByStudentQuestionarie(Integer stdQuestionarieID) {
        return (List<EdsTCResponse>)find("SELECT res FROM EdsTCResponse res left join res.sudentQuestionaire sq where sq.objectID =?", stdQuestionarieID);
    }
}
