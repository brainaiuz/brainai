<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 19.05.12
  Time: 13:51
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form action="#" method="get" id="newOpportunityform" name="newOpportunityform" autocomplete="off">

    <div id="topContentContainer" class="inputForm">
        <label class="linkToEmail">Link to this email</label>
        <input type="checkbox" value="true" checked="checked" name="linkToEmail">

        <div class="leftColumn">

            <div id="opportunityAssigneeError" class="errorMessage" style="display: none;">Please, select Assignee</div>
            <div class="selectBlock">
                <label for="opportunityAssignee">Assignee<span>*</span>:</label>
                <select name="opportunityAssignee" id="opportunityAssignee">
                    <option value="">Please Select</option>
                    <c:forEach items="${assigneeItems}" var="assigneeItem">
                        <option value="${assigneeItem.id}">${assigneeItem.name}</option>
                    </c:forEach>
                </select>
            </div>


            <div id="opportunityNameError" class="errorMessage" style="display: none;">Please, enter Opportunity name
            </div>
            <div class="selectBlock">
                <label for="opportunityName">Opportunity Name<span>*</span>:</label>
                <input type="text" name="opportunityName" id="opportunityName"/>
            </div>

            <div id="opportunityAccountNameError" class="errorMessage" style="display: none;">Please, enter Opportunity
                Account Name
            </div>
            <div class="selectBlock">
                <label for="opportunityAccountName">Account Name<span>*</span>:</label>
                <input type="hidden" style="margin-top: 8px; width: 100%; display:block" tabindex="-1" class="select2-offscreen opportunityAccountName" id="opportunityAccountName" name="opportunityAccountName"/>
            </div>

            <div id="opportunityContactNameError" class="errorMessage" style="display: none;">Please, enter Opportunity
                Contact Name
            </div>
            <div class="selectBlock">
                <label for="opportunityContactName">Contact Name<span style="display: none;" id="contactNameAsterisk">*</span>:</label>
                <input type="hidden" style="margin-top: 8px; width: 100%; display:block" tabindex="-1" class="select2-offscreen opportunityContactName" id="opportunityContactName" name="opportunityContactName"/>
            </div>


            <div class="selectBlock">
                <label for="opportunityDescription">Description:</label>
                <textarea rows="11" id="opportunityDescription" name="opportunityDescription"></textarea>
            </div>


        </div>

        <div class="rightColumn">

            <div class="selectBlock">
                <label for="opportunityAmount">Amount:</label>
                <input type="text" onkeyup="this.value=this.value.replace(/[^\d]/,'')" name="opportunityAmount" id="opportunityAmount"/>
            </div>

            <div id="opportunityDateError" class="errorMessage" style="display: none;">Please, enter Opportunity Closing
                Date
            </div>
            <div class="selectBlock">
                <label for="opportunityDate">Closing Date<span>*</span>:</label>
                <input name="opportunityDate" id="opportunityDate" type="text"/>

            </div>

            <div id="opportunityStageError" class="errorMessage" style="display: none;">Please, enter Opportunity
                Stage
            </div>
            <div class="selectBlock">
                <label for="opportunityStage">Stage<span>*</span>:</label>
                <select name="opportunityStage" id="opportunityStage">
                    <option value="">Please Select</option>
                    <c:forEach items="${stageItems}" var="stageItem">
                        <option percent="${stageItem.description}" value="${stageItem.id}">${stageItem.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="selectBlock">
                <label for="opportunityCompaignSource">Compaign Source:</label>
                <input type="hidden" style="margin-top: 8px; width: 100%; display:block" tabindex="-1" class="select2-offscreen opportunityCompaignSource" id="opportunityCompaignSource" name="opportunityCompaignSource"/>
            </div>

            <div class="selectBlock">
                <label for="opportunityLeadSource">Lead Source:</label>
                <select name="opportunityLeadSource" id="opportunityLeadSource">
                    <option value="">Please Select</option>
                    <c:forEach items="${leadItems}" var="leadItem">
                        <option value="${leadItem.id}">${leadItem.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="selectBlock">
                <label for="opportunityProbability">Probability (%):</label>
                <input readonly="readonly" type="text" name="opportunityProbability" id="opportunityProbability" value="0"/>
            </div>

            <div class="selectBlock">
                <label for="opportunityExpectedRevenue">Expected Revenue:</label>
                <input readonly="readonly" type="text" name="opportunityExpectedRevenue" id="opportunityExpectedRevenue" value="0"/>
            </div>

        </div>


        <div class="footButtons">
            <input type="hidden" name="saveOpportunity" id="saveOpportunity" value="false"/>
            <input type="hidden" name="companyId" id="companyId" value=""/>
            <input type="hidden" name="emailSubject" id="emailSubject" value=""/>
            <input type="hidden" name="emailDescription" id="emailDescription" value=""/>
            <input type="hidden" name="emailFromEmail" id="emailFromEmail" value=""/>
            <input type="hidden" name="emailToEmail" id="emailToEmail" value=""/>
            <input type="hidden" name="emailEmailId" id="emailEmailId" value=""/>
            <input class="FormButton Blue" type="submit" id="saveOpportunityButton" value="Save"/>
            <input class="FormButton Blue" type="submit" id="cancelOpportunityButton" value="Cancel"/>
        </div>
    </div>
</form>
