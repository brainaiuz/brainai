package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsTCResponse;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 15.09.12
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */

public interface ResponseManager extends Manager<EdsTCResponse> {

    List<EdsTCResponse> getResponseByStudentQuestionarie(Integer stdQuestionariesID);
}
