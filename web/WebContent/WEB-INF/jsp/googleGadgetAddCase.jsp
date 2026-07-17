<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 19.05.12
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form action="#" method="get" id="newTaskform" name="newTaskform" autocomplete="off">

    <div id="topContentContainer" class="inputForm">
        <label class="linkToEmail">Link to this email</label>
        <input type="checkbox" value="true" checked="checked" name="linkToEmail">

        <div class="leftColumn">


            <div id="caseSubjectError" class="errorMessage" style="display: none;">Please, enter case name</div>

            <div class="selectBlock">
                <label>Subject <span>*</span>:</label>
                <input type="text" name="caseSubject" id="caseSubject"/>
            </div>

            <div class="selectBlock">
                <label for="caseDescription">Description:</label>
                <textarea rows="11" id="caseDescription" name="caseDescription"></textarea>
            </div>
        </div>

        <div class="rightColumn">
            <div id="caseStatusError" class="errorMessage" style="display: none;">Please, choose status</div>

            <div class="selectBlock">
                <label for="caseStatus">Status <span>*</span>:</label>
                <select name="caseStatus" id="caseStatus">
                    <c:forEach items="${statusItems}" var="statusItem">
                        <option value="${statusItem.id}">${statusItem.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>


        <div class="footButtons">
            <div id="relationLinks"></div>
            <input type="hidden" name="saveCase" id="saveCase" value="false"/>
            <input type="hidden" name="companyId" id="companyId" value=""/>
            <input type="hidden" name="emailSubject" id="emailSubject" value=""/>
            <input type="hidden" name="emailDescription" id="emailDescription" value=""/>
            <input type="hidden" name="emailFromEmail" id="emailFromEmail" value=""/>
            <input type="hidden" name="emailToEmail" id="emailToEmail" value=""/>
            <input type="hidden" name="emailEmailId" id="emailEmailId" value=""/>
            <input class="FormButton Blue" type="submit" id="saveCaseButton" value="Save"/>
            <input class="FormButton Blue" type="submit" id="cancelCaseButton" value="Cancel"/>
        </div>
    </div>
</form>
