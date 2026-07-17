<%--
  Created by IntelliJ IDEA.
  User: Extreme
  Date: 5/17/13
  Time: 11:40 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<form action="#" method="get" id="emailLinkForm" name="emailLinkForm"
      autocomplete="off">
    <div id="topContentContainer" class="inputForm">


        <div id="dinamicContentEmailLink" class="rowFields dropDowns">

            <c:set var="flag" value="true"/>
            <c:forEach items="${relationItems}" var="relationItem">
            <c:choose>
            <c:when test="${flag}">
            <fieldset class="clear" id="dinamicContentEmailLinkPrototype">
                </c:when>
                <c:otherwise>
                <fieldset>
                </c:otherwise>
                </c:choose>
                <c:set var="flag" value="false"/>

                    <div class="selectBlock">
                        <label for="linkType">Link To :</label>

                        <c:choose>
                            <c:when test="${relationItem.exist}">
                                <c:forEach items="${relationItem.linkTypes}" var="typeItem">
                                    <c:choose>
                                        <c:when test="${typeItem.selected}">
                                            <label>${typeItem.description}</label>
                                            <input  type="hidden" name="linkTypeDropdown" value="${typeItem.name}"/>
                                        </c:when>
                                        <c:otherwise>

                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <select name="linkTypeDropdown" class="linkTypeDropdown">
                                    <option value="">Please Select</option>
                                    <c:forEach items="${relationItem.linkTypes}" var="typeItem">
                                        <c:choose>
                                            <c:when test="${typeItem.selected}">
                                                <option value="${typeItem.name}" selected="selected">${typeItem.description}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${typeItem.name}">${typeItem.description}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>

                            </c:otherwise>
                        </c:choose>




                        <c:choose>
                            <c:when test="${relationItem.exist}">
                                <c:choose>
                                    <c:when test="${relationItem.linkProjects!=null}">
                                        <label>${relationItem.linkProjects.name}</label>
                                        <input type="hidden" name="linkProjectDropdown" value="${relationItem.linkProjects.id}::${relationItem.linkProjects.name}"/>
                                    </c:when>
                                    <c:otherwise>

                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" style="width:200px; display:none;" tabindex="-1" class="select2-offscreen linkProjectDropdown" name="linkProjectDropdown"/>
                            </c:otherwise>
                        </c:choose>




                        <c:choose>
                            <c:when test="${relationItem.exist}">
                                <label>${relationItem.linkItems.name}</label>
                                <input type="hidden" name="linkItemDropdown" value="${relationItem.linkItems.id}::${relationItem.linkItems.name}"/>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" style="width:200px; display:none;" tabindex="-1" class="select2-offscreen linkItemDropdown" name="linkItemDropdown"/>
                            </c:otherwise>
                        </c:choose>

                            <a href="#" class="addEmailLinkFields">+</a>
                            <a href="#" class="removeEmailLinkFields">-</a>

                            <div class="errorMessage itemListError" style="display: none;">Please, choose Item</div>
                    </div>


                </fieldset>
                </c:forEach>

        </div>
        <div class="noteText">Please follow <a target="_blank" href="http://wiki.kpi.com/document/crm/email-integration#email_integration_email_settings">this</a> instruction to setup your 'Email account settings' in your kpi account to see the linkage.</div>

        <div class="footButtons">
            <input type="hidden" name="saveEmailLink" id="saveEmailLink" value="false"/>
            <input type="hidden" name="companyId" id="companyId" value=""/>
            <input type="hidden" name="emailSubject" id="emailSubject" value=""/>
            <input type="hidden" name="emailDescription" id="emailDescription" value=""/>
            <input type="hidden" name="emailFromEmail" id="emailFromEmail" value=""/>
            <input type="hidden" name="emailToEmail" id="emailToEmail" value=""/>
            <input type="hidden" name="emailEmailId" id="emailEmailId" value=""/>
            <input class="FormButton Blue" type="submit" id="saveEmailLinkButton" value="Save"/>
            <input class="FormButton Blue" type="submit" id="cancelEmailLinkButton" value="Cancel"/>
        </div>
    </div>
</form>