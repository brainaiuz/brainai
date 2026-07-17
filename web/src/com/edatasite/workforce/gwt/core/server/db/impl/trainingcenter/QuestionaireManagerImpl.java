package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsTCQuestionaire;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCStudentQuestionaire;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.QuestionaireManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 15.09.12
 * Time: 12:41
 */

@Repository("questionaireManager")
public class QuestionaireManagerImpl extends BaseManager<EdsTCQuestionaire> implements QuestionaireManager {

	public QuestionaireManagerImpl() {
		super(EdsTCQuestionaire.class);
	}

    @Override
    public List<EdsTCStudentQuestionaire> getQuestionairesList(ListingFilterParameter filterParametrs) {
        StringBuilder stringBuffer = new StringBuilder("SELECT studentQue FROM EdsTCStudentQuestionaire studentQue ");
        stringBuffer.append(" left join studentQue.questionaire que");
        stringBuffer.append(" left join studentQue.courseSchedule couSchedule");
        stringBuffer.append(" left join que.type t");
        stringBuffer.append(" where couSchedule.objectID =").append(filterParametrs.getObjectId());
        stringBuffer.append(" AND t.code = 'ASSESSMENT' ");
        String searchKey = filterParametrs.getSqlSearchKey();
        if (searchKey != null && !"".equals(searchKey)) {
            stringBuffer.append(" AND (lower(que.name) like '").append(searchKey).append("' ");
        }
        return (List<EdsTCStudentQuestionaire>) findInterval(stringBuffer.toString(), filterParametrs.getStart(), filterParametrs.getLimit());
    }

    @Override
    public Integer getQuestionairesTotal(ListingFilterParameter filterParametrs) {
        StringBuilder stringBuffer = new StringBuilder("SELECT DISTINCT count(studentQue.objectID) FROM EdsTCStudentQuestionaire studentQue ");
        stringBuffer.append(" left join studentQue.questionaire que");
        stringBuffer.append(" left join studentQue.courseSchedule couSchedule");
        stringBuffer.append(" left join que.type t");
        stringBuffer.append(" where couSchedule.objectID =").append(filterParametrs.getObjectId());
        String searchKey = filterParametrs.getSqlSearchKey();
        if (searchKey != null && !"".equals(searchKey)) {
            stringBuffer.append(" AND (lower(que.name) like '").append(searchKey).append("' ");
        }
        Long count = (Long) findSingle(stringBuffer.toString());
        return count != null ? count.intValue() : 0;
    }

}
