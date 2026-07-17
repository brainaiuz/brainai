<%--
  Created by IntelliJ IDEA.
  User: romeo
  Date: 10/9/12
  Time: 6:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form action="#" method="get" id="newTaskform" name="newTaskform" autocomplete="off">

    <div id="topContentContainer" class="inputForm">
        <label class="linkToEmail">Link to this email</label>
        <input type="checkbox" value="true" checked="checked" name="linkToEmail">

        <div class="leftColumn">


            <div class="selectBlock">
                <label for="firstName">First Name:</label>
                <input type="text" name="firstName" id="firstName"/>
            </div>


            <div id="lastNameError" class="errorMessage" style="display: none;">Please, enter Last Name</div>
            <div class="selectBlock">
                <label for="lastName">Last Name <span>*</span>:</label>
                <input type="text" name="lastName" id="lastName"/>
            </div>


            <div class="selectBlock">
                <label for="email">E-mail:</label>
                <input type="text" name="email" id="email"/>
            </div>


            <div class="selectBlock">
                <label for="status">Lead Status:</label>
                <select name="status" id="status">
                    <option value="">Please Select</option>
                    <c:forEach items="${statusItems}" var="statusItem">
                        <option value="${statusItem.id}">${statusItem.name}</option>
                    </c:forEach>
                </select>
            </div>

        </div>

        <div class="rightColumn">


            <div class="selectBlock">
                <label for="companyName">Company Name:</label>
                <input type="text" name="companyName" id="companyName"/>
            </div>


            <div class="selectBlock">
                <label for="source">Lead Source:</label>
                <select name="source" id="source">
                    <option value="">Please Select</option>
                    <c:forEach items="${sourceItems}" var="sourceItem">
                        <option value="${sourceItem.id}">${sourceItem.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="selectBlock">
                <label for="note">Note:</label>
                <textarea rows="11" id="note" name="note"></textarea>
            </div>


            <div class="selectBlock">
                <label for="assignee">Assignee:</label>
                <select name="assignee" id="assignee">
                    <option value="">Please Select</option>
                    <c:forEach items="${assigneeItems}" var="assigneeItem">
                        <option value="${assigneeItem.id}">${assigneeItem.name}</option>
                    </c:forEach>
                </select>
            </div>


        </div>


        <div class="footButtons">
            <input type="hidden" name="saveLead" id="saveLead" value="false"/>
            <input type="hidden" name="companyId" id="companyId" value=""/>
            <input type="hidden" name="emailSubject" id="emailSubject" value=""/>
            <input type="hidden" name="emailDescription" id="emailDescription" value=""/>
            <input type="hidden" name="emailFromEmail" id="emailFromEmail" value=""/>
            <input type="hidden" name="emailToEmail" id="emailToEmail" value=""/>
            <input type="hidden" name="emailEmailId" id="emailEmailId" value=""/>
            <input class="FormButton Blue" type="submit" id="saveLeadButton" value="Save"/>
            <input class="FormButton Blue" type="submit" id="cancelLeadButton" value="Cancel"/>
        </div>
    </div>
</form>
