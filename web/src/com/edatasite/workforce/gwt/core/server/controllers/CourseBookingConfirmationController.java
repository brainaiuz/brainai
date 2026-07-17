package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.trainingcenter.server.BookingItemForApprove;
import com.edatasite.workforce.gwt.trainingcenter.server.TCServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/19/12
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class CourseBookingConfirmationController {

    @Qualifier("tcService")
    @Autowired
    private TCServiceLocal tcServiceLocal;

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @RequestMapping(value = "/bookingConfirmation.html", method = RequestMethod.GET)
    public ModelAndView bookingConfirmation(HttpServletRequest request) throws Exception {
        ServerUtils.fillHostParameters(request);

        String companyID = request.getParameter("cid");
        String bookingID = request.getParameter("bid");

        while (companyID.contains("%") || bookingID.contains("%")) {
            if (companyID.contains("%")) {
                companyID = EncryptionHelper.decodeURL(companyID);
            }
            if (bookingID.contains("%")) {
                bookingID = EncryptionHelper.decodeURL(bookingID);
            }
        }

        companyID = EncryptionHelper.decrypt(companyID);
        bookingID = EncryptionHelper.decrypt(bookingID);

        System.out.println("companyID: " + companyID);
        System.out.println("bookingID: " + bookingID);

        String dataBaseName = globalAuthJdbcSpringManager.getCompanyDatabaseName(Integer.valueOf(companyID));

        if (dataBaseName != null && !dataBaseName.isEmpty()) {
            SecurityContext.getInstance().setDatabase(dataBaseName);
        }
        BookingItemForApprove bookingItemForApprove = tcServiceLocal.getCourseBookingForConfirmation(Integer.valueOf(bookingID), Integer.valueOf(companyID));
        if ("BOOKING_APPROVED".equals(bookingItemForApprove.getStatusCode())) {
            return new ModelAndView("courseBookingConfirmationResponse");
        }

        ModelAndView model = new ModelAndView("courseBookingConfirmation");
        model.addObject("bookingItemForApprove", bookingItemForApprove);
        model.addObject("bookingItems", bookingItemForApprove.getScheduleStudents());
        model.addObject("dbName", dataBaseName);
        model.addObject("companyID", companyID);
        model.addObject("bookingID", bookingID);
        return model;
    }

    @RequestMapping(value = "/bookingConfirmation.html", method = RequestMethod.POST)
    public ModelAndView bookingConfirmation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);
        String dataBaseName = request.getParameter("dbname");
        String companyID = request.getParameter("companyid");
        String bookingID = request.getParameter("bookingid");
        String approveBooking = request.getParameter("approvebooking");

        if (dataBaseName != null && !dataBaseName.equals("")) {
            SecurityContext.getInstance().setDatabase(dataBaseName);
        }

        String status = "BOOKING_APPROVED";
        if ("false".equals(approveBooking)) {
            status = "BOOKING_REJECTED";
        }

        tcServiceLocal.updateCourseBookingStatus(Integer.valueOf(bookingID), Integer.valueOf(companyID), status);

        ModelAndView model = null;
        model = new ModelAndView("courseBookingConfirmationResponse");
        return model;
    }
}
