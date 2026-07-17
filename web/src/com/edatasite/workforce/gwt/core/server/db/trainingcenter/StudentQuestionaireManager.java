package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCStudentQuestionaire;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 15.09.12
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */

public interface StudentQuestionaireManager extends Manager<EdsTCStudentQuestionaire> {

	EdsTCStudentQuestionaire checkQuestionaireForImporting(EdsCourseSchedule courseSchedule, String questionaireName);
}
