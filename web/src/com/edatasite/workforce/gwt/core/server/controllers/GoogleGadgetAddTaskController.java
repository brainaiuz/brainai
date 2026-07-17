package com.edatasite.workforce.gwt.core.server.controllers;


import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetService;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 16.05.12
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
//@RequestMapping("/googleGadget/addTask")
public class GoogleGadgetAddTaskController implements Constants {

    @Autowired
    GoogleGadgetService googleGadgetService;

    @Autowired
    CommonService commonService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserManager userManager;

    @Autowired
    AllInOneService allInOneService;



    private static final String PROJECT_ID = "getProjectId";
    private static final String DEFAULT_FORM = "getDefaultForm";
    private static final String SAVE_PROJECT = "saveProject";
    private static final String FIRST_NUMBER = "firstNumber";
    private static final String SECOND_NUMBER = "secondNumber";
    private static final String THIRD_NUMBER = "thirdNumber";
    private static final String PROJECT_NAME = "projectName";
    private static final String TASK_NAME = "taskName";
    private static final String DESCRIPTION = "description";
    private static final String PRIORITY = "priority";
    private static final String STATUS = "status";
    private static final String START_DATE = "startDate";
    private static final String DUE_DATE = "dueDate";
    private static final String EMPLOYEE = "employee";
    private static final String ESTIMATED_TIME = "estimatedTime";
    private static final String RELATIONS = "relations";
    private static final String LINK_TO_EMAIL = "linkToEmail";
    private static final String EMAIL_SUBJECT = "emailSubject";
    private static final String EMAIL_DESCIPTION = "emailDescription";
    private static final String EMAIL_FROM_EMAIL = "emailFromEmail";
    private static final String EMAIL_TO_EMAIL = "emailToEmail";
    private static final String EMAIL_EMAIL_ID = "emailEmailId";

    private static final String PROJECT_ITEMS = "projectItems";
    private static final String PRIORITIES_ITEMS = "prioritiesItems";
    private static final String STATUS_ITEMS = "statusItems";
    private static final String EMPLOYEES_LIST = "employeesList";

    @RequestMapping(value = "/googleGadget/addTask")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(GoogleGadgetService.JSON_CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        boolean isSigned = googleGadgetService.checkSignedRequest(request);


        String openSocialViewerId = request.getParameter(GoogleGadgetService.OPEN_SOCIAL_VIEWER_ID);
        Integer companyId = googleGadgetService.getInteger(request.getParameter(GoogleGadgetService.COMPANY_ID));


        if (isSigned) {
            boolean isUserExist = googleGadgetService.googleGadgetSignIn(openSocialViewerId, companyId);
            if (isUserExist) {
                if (request.getParameter(DEFAULT_FORM) != null && request.getParameter(DEFAULT_FORM).equals(GoogleGadgetService.TRUE)) {
                    return getForm();
                } else if (request.getParameter(PROJECT_ID) != null && !request.getParameter(PROJECT_ID).equals("")) {
                    Integer projectId = Integer.parseInt(request.getParameter(PROJECT_ID));
                    if (projectId != null) {
                        writer.write(getDataForForm(projectId));
                    }
                } else if (request.getParameter(SAVE_PROJECT) != null && request.getParameter(SAVE_PROJECT).equals(GoogleGadgetService.TRUE)) {
                    writer.write(saveTask(request));
                }
            } else {
                writer.write(GoogleGadgetService.YOU_ARE_NOT_AUTHORIZED);
            }
        } else {
            writer.write(GoogleGadgetService.YOUR_REQUEST_IS_NOT_SIGNED);
        }
        writer.close();
        return null;
    }

    private ModelAndView getForm() {
        ModelAndView modelAndView = new ModelAndView("googleGadgetAddTask");
        modelAndView.addObject(PROJECT_ITEMS, getProjectsList());
        modelAndView.addObject(PRIORITIES_ITEMS, getPriorities());
        modelAndView.addObject(STATUS_ITEMS, getStatus());
        return modelAndView;
    }

    private String getDataForForm(Integer projectId) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put(EMPLOYEES_LIST, getEmployees(projectId));
        Map<String, String> numbers = getTaskNumber(projectId);
        jsonResponse.put(FIRST_NUMBER, numbers.get(FIRST_NUMBER));
        jsonResponse.put(SECOND_NUMBER, numbers.get(SECOND_NUMBER));
        jsonResponse.put(THIRD_NUMBER, numbers.size() > 2 ? numbers.get(THIRD_NUMBER) : "");
        return jsonResponse.toJSONString();
    }

    private ProjectItem[] getProjectsList() {
        return commonService.getProjects(false);
    }

    private ArrayList<String> getEmployees(Integer projectId) {
        SelectItem[] employeeItems = taskService.getAssigneesWithPositions1(projectId);
        ArrayList<String> employees = new ArrayList<>();
        for (SelectItem employeeItem : employeeItems) {
            employees.add(employeeItem.getId() + "::" + employeeItem.getName());

        }
        return employees;
    }

    private SelectItem[] getPriorities() {
        return taskService.getPriorities();
    }

    private SelectItem[] getStatus() {
        return commonService.getAddTaskStatusDrop();
    }

    private Map<String, String> getTaskNumber(Integer projectId) {
        NumberData numberData = taskService.generateTaskNumber(projectId, new Date(), null);
        Map<String, String> num = new HashMap<>();


        DecimalFormat numberFormat = initializeNumberFormat(numberData.getNumberFormat());
        String numbering = "";
        String prefix = "";
        String lastString = "";

        if (numberData.getIntNumber() != null) {
            numbering = numberFormat.format(numberData.getIntNumber().doubleValue());
            num.put(SECOND_NUMBER, numbering);
        }
        if (!isInvalid(numberData.getFirstNumberString())) {
            int endIndex = numberData.getFirstNumberString().lastIndexOf(numbering);
            prefix = endIndex != -1 ? numberData.getFirstNumberString().substring(0, endIndex) : numberData.getFirstNumberString();
            num.put(FIRST_NUMBER, prefix);
        } else if (!isInvalid(numberData.getNumberString())) {
            prefix = getPrefixValue(numbering, numberData, numberFormat);
            num.put(FIRST_NUMBER, prefix);
        }


        if (!isInvalid(numberData.getLastNumberString())) {
            lastString = numberData.getLastNumberString();
            num.put(THIRD_NUMBER, lastString);
        }


        return num;
    }

    private String getPrefixValue(String numbering, NumberData numberData, DecimalFormat numberFormat) {
        if (numbering == null || numbering.isEmpty()) {
            return numberData.getNumberString();
        }


        try {
            String numberField = String.valueOf((int) numberFormat.parse(numbering).doubleValue());
            if (numberData.getNumberString().lastIndexOf(numbering) != -1) {
                int endIndex = numberData.getNumberString().lastIndexOf(numbering);
                return numberData.getNumberString().substring(0, endIndex);
            } else if (numberData.getNumberString().lastIndexOf(numberField) != -1) {
                int numEndIndex = numberData.getNumberString().indexOf(numberField) + numberField.length();
                int prefEndIndex = numEndIndex - numberFormat.format(numberFormat.parse(numbering)).length();
                return prefEndIndex != -1 ? numberData.getNumberString().substring(0, prefEndIndex) : numberData.getNumberString();
            } else {
                return numberData.getNumberString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return numberData.getNumberString();
        }

    }

    private DecimalFormat initializeNumberFormat(String objectNumberFormat) {
        String numbering = "";
        if (objectNumberFormat.contains(WIDGET_NUMBERS)) {
            String[] firstString = objectNumberFormat.split("/");
            for (String value : firstString) {
                String[] split = value.split(":");
                if (split[0].equals(WIDGET_NUMBERS)) {
                    numbering = split[1];
                }
            }
        } else if (objectNumberFormat.contains(SAV_NUM_DEL)) {
            String[] firstString = objectNumberFormat.split(SAV_NUM_DEL);
            numbering = firstString[1];
        } else {
            int splitterIndex = objectNumberFormat.lastIndexOf("_");
            numbering = objectNumberFormat.substring(splitterIndex + 1);
        }
        if (numbering != null && numbering.length() > 0) {
            StringBuilder nf = new StringBuilder();
            for (int i = 0; i < numbering.length(); i++) {
                nf.append("0");
            }

            return new DecimalFormat(nf.toString());
        } else {
            return new DecimalFormat("0000");
        }
    }

    private String saveTask(HttpServletRequest taskData) {
        JSONObject jsonResponse = new JSONObject();
        if (validate(taskData)) {

            NumberData taskNumber = getNumberData(taskData.getParameter(FIRST_NUMBER), taskData.getParameter(SECOND_NUMBER), taskData.getParameter(THIRD_NUMBER));

            Integer projectId = Integer.parseInt(taskData.getParameter(PROJECT_NAME));
            String taskName = taskData.getParameter(TASK_NAME);
            String description = taskData.getParameter(DESCRIPTION);
            String linkToEmail = taskData.getParameter(LINK_TO_EMAIL);

            String emailSubject = taskData.getParameter(EMAIL_SUBJECT);
            String emailDescription = taskData.getParameter(EMAIL_DESCIPTION);
            String emailFromEmail = taskData.getParameter(EMAIL_FROM_EMAIL);
            String emailToEmail = taskData.getParameter(EMAIL_TO_EMAIL);
            String emailEmailId = taskData.getParameter(EMAIL_EMAIL_ID);

            Integer priorityId = Integer.parseInt(taskData.getParameter(PRIORITY));
            Integer statusId = Integer.parseInt(taskData.getParameter(STATUS));


            SimpleDateFormat dateFormat = new SimpleDateFormat(GoogleGadgetService.DATE_PATTERN, Locale.US);
            EdsUser user = userManager.getUser();

            GregorianCalendar startDate = new GregorianCalendar();
            GregorianCalendar endDate = new GregorianCalendar();

            try {
                startDate.setTime(dateFormat.parse(taskData.getParameter(START_DATE)));
                endDate.setTime(dateFormat.parse(taskData.getParameter(DUE_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            ServerUtils.setBeginningOfTheDay(startDate);
            ServerUtils.setEndOfTheDay(endDate);

            String[] employees = taskData.getParameterValues(EMPLOYEE);
            String[] estimatedTime = taskData.getParameterValues(ESTIMATED_TIME);


            IdTime[] assignees = new IdTime[employees.length];
            for (int i = 0; i < employees.length; i++) {
                Integer employeeId = Integer.parseInt(employees[i]);
                Integer employeeTime = Utils.parseMinutes(estimatedTime[i]);
                assignees[i] = new IdTime(employeeId, employeeTime);
            }

            try {
                TaskSingleItem newTask = new TaskSingleItem();
                newTask.setProjectID(projectId);
                newTask.setName(taskName);
                newTask.setDescription(description);
                newTask.setPriorityID(priorityId);
                newTask.setStartDate(user.getServerDateByUserDate(startDate.getTime()));
                newTask.setDueDate(user.getServerDateByUserDate(endDate.getTime()));
                newTask.setStatusID(statusId);
                newTask.setAllDay(true);


                newTask.setProjectEmployees(assignees);
                newTask.setInstancesCount(1);

                newTask.setNumberData(taskNumber);

                Integer[] result = taskService.saveTask(newTask);
                if (result.length > 0) {
                    if (!isArrayInvalid(taskData.getParameterValues(RELATIONS)) || GoogleGadgetService.TRUE.equals(linkToEmail)) {
                        Email email = new Email();
                        email.setSubject(emailSubject);
                        email.setContent(emailDescription);
                        email.setFromEmail(emailFromEmail);
                        email.setToEmails(emailToEmail);
                        email.setGeneratedGoogleID(emailEmailId);
                        boolean saveRelationResult = googleGadgetService.saveRelation(taskData.getParameterValues(RELATIONS),
                                result[1], RelationItem.TYPE_TASK, email, GoogleGadgetService.TRUE.equals(linkToEmail));
                        if (!saveRelationResult) {
                            jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.RELATION_SAVED_FAILED);
                        }
                    }
                    jsonResponse.put(GoogleGadgetService.SAVED, true);
                } else {
                    jsonResponse.put(GoogleGadgetService.SAVED, false);
                }
            } catch (Throwable t) {
                t.getMessage();
            }

        } else {
            jsonResponse.put(GoogleGadgetService.SAVED, false);
            jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.VALIDATION_FAILED);
        }


        return jsonResponse.toJSONString();
    }

    private NumberData getNumberData(String prefix, String number, String postfix) {
        NumberData taskNumber = new NumberData();

        taskNumber.setFirstNumberString(prefix);
        taskNumber.setIntNumber(Integer.valueOf(number));
        taskNumber.setLastNumberString(postfix);
        taskNumber.setNumberString(prefix + number);
        taskNumber.setNumberFormat(googleGadgetService.getTaskNumberingFormat(Integer.valueOf(number)));

        return taskNumber;
    }

    private boolean validate(HttpServletRequest taskData) {
        int errors = 0;
        if (isInvalid(taskData.getParameter(FIRST_NUMBER))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(SECOND_NUMBER))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(PROJECT_NAME))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(TASK_NAME))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(PRIORITY))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(STATUS))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(START_DATE))) {
            errors++;
        }
        if (isInvalid(taskData.getParameter(DUE_DATE))) {
            errors++;
        }
        if (taskData.getParameterValues(EMPLOYEE) == null || taskData.getParameterValues(EMPLOYEE).length <= 0) {
            errors++;
        } else {
            String[] employees = taskData.getParameterValues(EMPLOYEE);
            for (String employee : employees) {
                if (employee == null) {
                    errors++;
                }
            }
        }
        if (taskData.getParameterValues(ESTIMATED_TIME) == null || taskData.getParameterValues(ESTIMATED_TIME).length <= 0) {
            errors++;
        } else {
            String[] estimatedTime = taskData.getParameterValues(ESTIMATED_TIME);
            for (String anEstimatedTime : estimatedTime) {
                if (anEstimatedTime == null) {
                    errors++;
                }
            }
        }

        return errors <= 0;
    }

    private boolean isInvalid(String param) {
        return param == null || param.equals("");
    }

    private boolean isArrayInvalid(String[] array) {
        return array == null || array.length <= 0;
    }

}
