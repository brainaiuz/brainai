package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.CandidateAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.CandidateEditSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: hayot
 * Date: 7/3/12
 * Time: 4:54 PM
 */
public class CandidateEditHistoryProcessor implements HistoryProcessor {

	private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

	@Override
	public SinksContainer process(String containerName, String[] strings) {
		return new CandidateEditSinksContainer(containerName + strings[0], hrmsStrings.editCandidate(), strings);
	}

	@Override
	public SinksContainer processAdd(String[] params) {
		return new CandidateAddSinksContainer("candidateadd", hrmsStrings.addCandidate(), params);
	}
}
