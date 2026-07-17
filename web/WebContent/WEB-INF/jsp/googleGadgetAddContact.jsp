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


        </div>

        <div class="rightColumn">


            <div class="selectBlock">
                <label for="companyName">Company Name:</label>
                <input type="text" name="companyName" id="companyName"/>
            </div>


            <div class="selectBlock">
                <label for="note">Note:</label>
                <textarea rows="11" id="note" name="note"></textarea>
            </div>

        </div>


        <div class="footButtons">
            <input type="hidden" name="saveContact" id="saveContact" value="false"/>
            <input type="hidden" name="companyId" id="companyId" value=""/>
            <input type="hidden" name="emailSubject" id="emailSubject" value=""/>
            <input type="hidden" name="emailDescription" id="emailDescription" value=""/>
            <input type="hidden" name="emailFromEmail" id="emailFromEmail" value=""/>
            <input type="hidden" name="emailToEmail" id="emailToEmail" value=""/>
            <input type="hidden" name="emailEmailId" id="emailEmailId" value=""/>
            <input class="FormButton Blue" type="submit" id="saveContactButton" value="Save"/>
            <input class="FormButton Blue" type="submit" id="cancelContactButton" value="Cancel"/>
        </div>
    </div>
</form>
