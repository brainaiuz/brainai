package com.edatasite.workforce.gwt.core.server.app.hmrc.service;

import com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile.UserTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;

public interface UserService {
    UserTO getUserProfile(String accessToken) throws RestException;
    UserTO getUserProfile(String accessToken, Integer id) throws RestException;
}
