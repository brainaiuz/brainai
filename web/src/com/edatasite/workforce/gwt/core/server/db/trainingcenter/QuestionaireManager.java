package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsTCQuestionaire;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCStudentQuestionaire;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 15.09.12
 * Time: 12:42
 * To change this template use File | Settings | File Templates.
 */

public interface QuestionaireManager extends Manager<EdsTCQuestionaire> {

    List<EdsTCStudentQuestionaire> getQuestionairesList(ListingFilterParameter filterParametrs);

    Integer getQuestionairesTotal(ListingFilterParameter filterParametrs);
}
