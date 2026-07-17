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
            <div id="projectsListError" class="errorMessage" style="display: none;">Please, choose project</div>

            <div class="selectBlock">
                <label for="projectName">Project <span>*</span>:</label>
                <select name="projectName" id="projectName">
                    <option value="">Please Select</option>
                    <c:forEach items="${projectItems}" var="projectItem">
                        <option value="${projectItem.id}">${projectItem.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div id="taskNumberError" class="errorMessage" style="display: none;">Please, enter task number</div>

            <div class="selectBlock">
                <label>Task Number <span>*</span>:</label>

                <div class="taskNumbersInputs">
                    <input type="text" name="firstNumber" class="taskPref" id="firstNumber"/>
                    <input type="text" name="secondNumber" class="taskNum" id="secondNumber"/>
                    <input type="text" name="thirdNumber" class="taskPost" id="thirdNumber" style="display: none;"/>
                </div>
            </div>

            <div id="taskNameError" class="errorMessage" style="display: none;">Please, enter task name</div>

            <div class="selectBlock">
                <label>Task Name <span>*</span>:</label>
                <input type="text" name="taskName" id="taskName"/>
            </div>

            <div class="selectBlock">
                <label for="description">Task Description:</label>
                <textarea rows="11" id="description" name="description"></textarea>
            </div>
        </div>

        <div class="rightColumn">
            <div id="dateError">
                <div id="dateStartError" class="errorMessage" style="display: none;">Please, choose start date</div>
                <div id="dateDueError" class="errorMessage" style="display: none;"> Please, choose due date</div>
                <div id="dateDueStartError" class="errorMessage" style="display: none;">Due date cannot be before start
                    date
                </div>
            </div>

            <div class="selectBlock">
                <label id="startDateLabel" for="startDate">Start Date <span>*</span>:</label>
                <input name="startDate" id="startDate" type="text"/>
                <label id="dueDateLabel" for="dueDate">Due Date <span>*</span>:</label>
                <input name="dueDate" id="dueDate" type="text"/>
            </div>

            <div id="employeesError">
                <div id="employeesPleaseChooseError" class="errorMessage" style="display: none;">Please, choose
                    assignees
                </div>
                <div id="employeesNotFoundError" class="errorMessage" style="display: none;">No assignees found</div>
            </div>

            <div id="estimatedTimeError" class="errorMessage" style="display: none;">Please, enter estimated time</div>

            <div class="selectBlock">
                <div class="labels">
                    <label class="desc" for="employeesList">Assignees <span>*</span>:</label>
                    <label class="estimTime">Estimated Time <span>*</span>:</label>
                </div>


                <div id="employeesList"></div>
            </div>


            <div id="priorityError" class="errorMessage" style="display: none;">Please, choose priority</div>

            <div class="selectBlock">
                <label for="priority">Priority <span>*</span>:</label>
                <select name="priority" id="priority">
                    <option value="">Please Select</option>
                    <c:forEach items="${prioritiesItems}" var="prioritiesItem">
                        <option value="${prioritiesItem.id}">${prioritiesItem.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div id="statusError" class="errorMessage" style="display: none;">Please, choose status</div>

            <div class="selectBlock">
                <label for="status">Status <span>*</span>:</label>
                <select name="status" id="status">
                    <option value="">Please Select</option>
                    <c:forEach items="${statusItems}" var="statusItem">
                        <option value="${statusItem.id}">${statusItem.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>


        <div class="footButtons">
            <div id="relationLinks"></div>
            <input type="hidden" name="saveProject" id="saveProject" value="false"/>
            <input type="hidden" name="companyId" id="companyId" value=""/>
            <input type="hidden" name="emailSubject" id="emailSubject" value=""/>
            <input type="hidden" name="emailDescription" id="emailDescription" value=""/>
            <input type="hidden" name="emailFromEmail" id="emailFromEmail" value=""/>
            <input type="hidden" name="emailToEmail" id="emailToEmail" value=""/>
            <input type="hidden" name="emailEmailId" id="emailEmailId" value=""/>
            <input class="FormButton Blue" type="submit" id="saveTaskButton" value="Save"/>
            <input class="FormButton Blue" type="submit" id="cancelTaskButton" value="Cancel"/>
        </div>
    </div>
</form>
