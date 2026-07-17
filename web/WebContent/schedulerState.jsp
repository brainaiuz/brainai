<%@ page import="com.edatasite.workforce.appContext.ApplicationContextProvider" %>
<%@ page import="org.quartz.JobDetail" %>
<%@ page import="org.quartz.Trigger" %>
<%@ page import="org.quartz.impl.StdScheduler" %>
<%--
  Created by IntelliJ IDEA.
  User: Anvar
  Date: Jan 14, 2011
  Time: 5:45:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
<table border="1" cellpadding="5">
    <%
        StdScheduler scheduler = (StdScheduler) ApplicationContextProvider.applicationContext.getBean("schedFactoryBean");
        if (scheduler != null) {
            for (String s : scheduler.getJobGroupNames()) {
    %>
    <tr>
        <th colspan="4"><%=s%>
        </th>
    </tr>
    <tr>
        <th>Job Name</th>
        <th>Job Class</th>
        <th>Company ID</th>
        <th>busObjectId</th>
    </tr>
    <%

        for (String s1 : scheduler.getJobNames(s)) {
            JobDetail jobDetail = scheduler.getJobDetail(s1, s);
            Integer companyID = (Integer) jobDetail.getJobDataMap().get("companyID");
            Integer busObjectId = (Integer) jobDetail.getJobDataMap().get("busObjectId");
            for (Trigger t : scheduler.getTriggersOfJob(s1, s)) {

            }
    %>
    <tr>
        <td><%=s1%>
        </td>
        <td><%=jobDetail.getJobClass()%>
        </td>
        <td><%=companyID != null ? companyID : "&nbsp;"%>
        </td>
        <td><%=busObjectId != null ? busObjectId : "&nbsp;"%>
        </td>
    </tr>
    <%
                }
            }
        }
        for (Object o : scheduler.getCurrentlyExecutingJobs()) {
            out.println(o + "<br>");
        }
    %>
</table>
</body>
</html>