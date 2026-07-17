package com.finnetlimited.reportservice.core.server.validators;

import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 12.06.2010
 * Time: 19:21:13
 * To change this template use File | Settings | File Templates.
 */
public class SignUpValidator implements Validator {

    // Messages
    private static final String FIRST_NAME_REQUARED="First name is required";
    private static final String LAST_NAME_REQUARED="Last name is required";
    private static final String COMPANY_NAME_REQUARED="Company name is required";
    private static final String COUNTRY_REQUARED="Country is required";
    private static final String AGREEMENT_REQUARED="You should agree with \"Terms and Conditions\"";
    private static final String PHONE_REQUIRED="Phone is required";
    private static final String PHONE_INVALID="Please enter valid phone number";

    public boolean supports(Class aClass) {
        return NewCompany.class.isAssignableFrom(aClass);
    }

    public void validate(Object command, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"adminFName","required",FIRST_NAME_REQUARED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","required",COMPANY_NAME_REQUARED);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"countryID","required",COUNTRY_REQUARED);

        Object phoneValue = errors.getFieldValue("phone");
        if(phoneValue == null ||!StringUtils.hasText(phoneValue.toString())){
          ValidationUtils.rejectIfEmpty(errors,"phone","PHONE_REQUIRED",PHONE_REQUIRED);
        }

//        Object agreeWithCondition = errors.getFieldValue("agreeWithCondition");
//        if(agreeWithCondition!=null){
//            Boolean agree=(Boolean)agreeWithCondition;
//            if(!agree.booleanValue()){
//                errors.rejectValue("agreeWithCondition","required",AGREEMENT_REQUARED);
//            }
//        }




    }

}
