<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="timesheetForm">
  <h1>Новая запись</h1>
  <form:form method="POST" modelAttribute="timesheetForm">
	<table class="newTable">
		<thead>
			<tr>
				<th class="colDate">Дата</th>
				<th class="colProject">Проект</th>
				<th class="colTask">Задача</th>
				<th class="colCount">Часы</th>
				<th class="colComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><form:input class="input" type="date" path="dateTask" /></td>
				<td>
					<form:select path="projectId" >
						<form:option value="" label="--Please Select"/>
              			<form:options items="${projectList}" itemValue="projectId" itemLabel="name"/>
              		</form:select>
				</td>
				<td>
					<form:select path="taskId" >
						<form:option value="" label="--Please Select"/>
              			<form:options items="${taskList}" itemValue="taskId" itemLabel="name"/>
              		</form:select>
				</td>
				<td><form:input class="input" type="text" path="countTime" /></td>
				<td><form:input class="input" type="text" path="comment" /></td>
			</tr>
		</tbody>
		<tfoot></tfoot>
	</table>
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="submit" value="Добавить" />
  </form:form>
</div>

<div class="listTitle">
	<c:url var="deleteUrl" value="/timesheet/delete"/> 
	<h1>Недавние записи</h1>
		<table class="timesheetTable">
			<thead>
				<tr>
					<th scope="col" class="colDate">Дата</th>
					<th scope="col" class="colProject">Проект</th>
					<th scope="col" class="colTask">Задача</th>
					<th scope="col" class="colCount">Часы</th>
					<th scope="col" class="colHours">Часы</th>
					<th scope="col" class="colComment">Комментарий</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="timesheetByDay" items="${timesheetsByDays}" varStatus="stat">
					<c:choose>
						<c:when test="${stat.index % 2 == 0}" >
							<c:set var="classRow" value="odd" />
						</c:when>
						<c:otherwise>					
							<c:set var="classRow" value="even" />
						</c:otherwise>
					</c:choose>
					<tr class="${classRow}">
						<td rowspan="${fn:length(timesheetByDay.timesheets)}">
							<fmt:formatDate pattern="EEEE" value="${timesheetByDay.date}" />
							<br/>
							<fmt:formatDate pattern="dd-MM-yyyy" value="${timesheetByDay.date}" />
						</td>
						<c:forEach var="timesheet" items="${timesheetByDay.timesheets}" varStatus="status">
								<c:if test="${status.index > 0}">
									<tr class="${classRow}">
								</c:if>				
										<td><c:out value="${timesheet.project.name}" /></td>
										<td><c:out value="${timesheet.task.name}" /></td>
										<td><c:out value="${timesheet.countTime}" /></td>
										<c:if test="${status.index == 0}">
											<td rowspan="${fn:length(timesheetByDay.timesheets)}">
												<c:out value="${timesheetByDay.hours}" />
											</td>
										</c:if>								
										<td><c:out value="${timesheet.comment}" /></td>
										<td id="colLast">				  
											<form action="${deleteUrl}" method="POST">
									      		<input name="timesheetId" type="hidden" value="${timesheet.id}"/>
									      		<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')"/>
									      		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
											</form>				
										</td>	
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		<tfoot>
			<tr>
				<td colspan="4">Общее количество часов:</td>
				<td colspan="2"><c:out value="${countTime}"/></td>
			</tr>
		</tfoot>
	</table>
	<br/>
    <s:url value="/timesheet?week=${param.week + 1}" var="more_url" />
    <a href="${more_url}">Предыдующая неделя</a>

</div>
