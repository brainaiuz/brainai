<%--
  Created by IntelliJ IDEA.
  User: Normurod
  Date: 8/20/12
  Time: 6:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tiles:insertDefinition name="frontEndLayoutNew">
    <tiles:putAttribute name="title">
        <fmt:message key="coursebooking.coursebookingconfirmation"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="style">
        <link href="/customisation/trainingcenter/confirmation.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript">
            function approveBooking(approve) {
                form = document.getElementById('courseBookingConfirmationForm');
                data = document.createElement('input');
                data.type = 'hidden';
                data.name = 'approvebooking';
                data.value = approve;
                form.appendChild(data);
                form.submit();
            }
        </script>
    </tiles:putAttribute>
    <tiles:putAttribute name="body">
        <div id="cover">
            <!--begin #main-->
            <div id="main">
                <!-- START COURSE BOOKING CONFIRMATION PAGE  -->
                <h2 class="title"><fmt:message key="coursebooking.coursebookingconfirmation"/></h2>

                <div class="infoSet frame_1 group sectBig">
                    <strong class="infoSetCaption"><fmt:message key="signup.companyInformation"/></strong>

                    <div class="half left">
                        <table class="tableFree">
                            <tr>
                                <th><fmt:message key="coursebooking.bookingnumber"/></th>
                                <td>: ${bookingItemForApprove.number}</td>
                            </tr>

                            <tr>
                                <th><fmt:message key="signup.companyName"/></th>
                                <td>: ${bookingItemForApprove.customer}</td>
                            </tr>

                            <tr>
                                <th><fmt:message key="coursebooking.companynumber"/></th>
                                <td>: ${bookingItemForApprove.customerNumber}</td>
                            </tr>
                        </table>
                    </div>

                    <div class="half left">
                        <table class="tableFree">
                            <tr>
                                <th><fmt:message key="coursebooking.clientname"/></th>
                                <td>: ${bookingItemForApprove.contact}</td>
                            </tr>

                            <tr>
                                <th><fmt:message key="coursebooking.trainingvenue"/></th>
                                <td>: ${bookingItemForApprove.location}</td>
                            </tr>
                        </table>
                    </div>
                </div>

                <h2 class="title"><fmt:message key="coursebooking.students"/></h2>

                <div class="frame_1 sectBig">
                    <table class="massData zebra fullWidth">
                        <thead>
                        <tr>
                            <th>Resedent #</th>
                            <th>Company Employee #</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Course Name</th>
                                <%--<th>Language</th>--%>
                            <th>Start Date</th>
                        </tr>
                        </thead>
                        <tbody>
                            <%--<% int i = 0; %>--%>
                        <c:forEach var="student" items="${bookingItems}">
                            <%--<tr class="<%= (i%2 == 0) ? "odd" : "even" %>">--%>
                            <tr class="odd">
                                <td>${student.residenceNumber}</td>
                                <td>${student.companyEmployeeNumber}</td>
                                <td>${student.firstName}</td>
                                <td>${student.lastName}</td>
                                <td>${student.course}</td>
                                    <%--<td>${student.language}</td>--%>
                                <td>${student.startDate}</td>
                            </tr>
                            <%--<% i++; %>--%>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="sect btnSet r">
                    <form:form id="courseBookingConfirmationForm" method="post" action="/bookingConfirmation.html">
                        <input type="hidden" id="dbname" name="dbname" value="${dbName}"/>
                        <input type="hidden" id="companyid" name="companyid" value="${companyID}"/>
                        <input type="hidden" id="bookingid" name="bookingid" value="${bookingID}"/>
                        <button type="button" class="btn-1" onclick="approveBooking('true')"><span>Approve</span>
                        </button>
                        <button type="button" class="btn-2" onclick="approveBooking('false')"><span>Reject</span>
                        </button>
                    </form:form>
                </div>
            </div>
            <!--END #main-->
        </div>

    </tiles:putAttribute>

</tiles:insertDefinition>