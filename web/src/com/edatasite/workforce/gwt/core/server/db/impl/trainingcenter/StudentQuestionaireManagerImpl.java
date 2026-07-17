package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCStudentQuestionaire;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.StudentQuestionaireManager;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 15.09.12
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */

@Repository("studentQuestionaireManager")
public class StudentQuestionaireManagerImpl extends BaseManager<EdsTCStudentQuestionaire> implements StudentQuestionaireManager {

	public StudentQuestionaireManagerImpl() {
		super(EdsTCStudentQuestionaire.class);
	}

	@Override
	public EdsTCStudentQuestionaire checkQuestionaireForImporting(EdsCourseSchedule courseSchedule, String questionaireName) {
		return (EdsTCStudentQuestionaire) findSingle("select sq from EdsTCStudentQuestionaire sq where sq.courseSchedule=? and sq.questionaire.name=?",
				courseSchedule, questionaireName);
	}
}
