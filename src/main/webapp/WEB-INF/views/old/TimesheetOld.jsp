<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="timesheetForm">
  <h1>Spit it out...</h1>
  <form method="POST" name="timesheetForm">
    <input type="hidden" name="latitude">
    <input type="hidden" name="longitude">
    <textarea name="message" cols="80" rows="5"></textarea><br/>
    <input type="submit" value="Add" />
  </form>
</div>

<div class="listTitle">
  <h1>Недавние записи</h1>
  <ul class="timesheetList">
    <c:forEach items="${timesheetList}" var="timesheet" >
      <li id="timesheet_<c:out value="timesheet.id"/>">
        <div class="timesheetProject"><c:out value="${timesheet.project.name}" /></div>
        <div>
          <span class="timesheetDate"><c:out value="${timesheet.dateTask}" /></span>
          <span class="timesheetCountTime"><c:out value="${timesheet.countTime}" /></span>
        </div>
      </li>
    </c:forEach>
  </ul>
  <c:if test="${fn:length(timesheetList) gt 20}">
    <hr />
    <s:url value="/{timesheet?count=${nextCount}" var="more_url" />
    <a href="${more_url}">Show more</a>
  </c:if>
</div>
