<%--
  Created by IntelliJ IDEA.
  User: romeo
  Date: 11/30/12
  Time: 3:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<a href="#" id="link-button">+ Add Link</a>

<div id="link-form">
    <div class="topColumn">
        <div class="rowFields trioFields">
            <div class="selectBlock">
                <label for="linkType">Link To :</label>
                <select name="linkTypeDropdown" id="linkTypeDropdown">
                    <option value="">Please Select</option>
                    <option value="TASK">Task</option>
                    <option value="case">Case</option>
                </select>
            </div>

            <div id="linkProject" class="selectBlock" style="display: none;">
                <label for="linkProjectDropdown">Project :</label>
                <select name="linkProjectDropdown" id="linkProjectDropdown">

                </select>
            </div>

            <div id="linkOther" class="selectBlock" style="display: none;">
                <label id="linkOtherDropdownLabel" for="linkOtherDropdown"></label>
                <select name="linkOtherDropdown" id="linkOtherDropdown">

                </select>
            </div>

        </div>

    </div>


    <div id="choosedLinks" class="bottomColumn">

    </div>

</div>