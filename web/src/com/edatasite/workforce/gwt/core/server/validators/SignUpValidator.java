package com.edatasite.workforce.gwt.core.server.validators;

import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Sep 29, 2009
 * Time: 10:11:48 AM
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

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"adminFName","firstNameRequared", "Name is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","companyNameRequared");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"countryID","countrNameRequared");
        Object phoneValue = errors.getFieldValue("phone");
        String countryCode = (String) errors.getFieldValue("countryCode");

        //Validate Phone Number with com.google.i18n.phonenumbers
        String phone = (String) phoneValue;
        if(!(org.apache.commons.lang3.StringUtils.isNotBlank(phone) && phone.replaceAll("\\D", "").matches("\\d+"))) {
            errors.rejectValue("phone", "PHONE_REQUIRED", PHONE_INVALID);
        }

        /*if(phoneValue == null ||!StringUtils.hasText(phoneValue.toString().trim())){
          ValidationUtils.rejectIfEmptyOrWhitespace(errors,"phone", "phoneNuberRequared");
        }else{
             PhoneNumberValidator phonevalid = new PhoneNumberValidator((String)phoneValue);
             if (!phonevalid.checkPhone()) {
                 errors.rejectValue("phone","required",PHONE_INVALID);
            }
        }*/

//        Object agreeWithCondition = errors.getFieldValue("agreeWithCondition");
//        if (agreeWithCondition != null) {
//            Boolean agree = false;
//            if (agreeWithCondition instanceof Boolean) {
//                agree = (Boolean) agreeWithCondition;
//            } else if (agreeWithCondition instanceof String) {
//                agree = Boolean.valueOf((String) agreeWithCondition);
//            }
//            if (!agree) {
//                errors.rejectValue("agreeWithCondition", "agreeWithConditionRequared");
//            }
//        } else {
//            System.out.println("Agree with conditions is null");
//            errors.rejectValue("agreeWithCondition", "agreeWithConditionRequared");
//        }


    }

}
