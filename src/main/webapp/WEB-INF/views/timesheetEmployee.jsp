<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div>
	<h2>
		<c:out value="${employee.surname} ${employee.name}" />
	</h2>
	<span> </span>
</div>

<div class="listTimesheet">
	<div>
		<div style="float: left;">
			<h2>
				<fmt:parseDate value="${timesheetsByDays[0].date}"
					pattern="yyyy-MM-dd" var="parsedDate" type="date" />
				<fmt:formatDate pattern="w" value="${parsedDate}" />
				неделя
				<fmt:formatDate pattern="y" value="${parsedDate}" />
				года:
				<fmt:formatDate pattern="dd MMMM" value="${parsedDate}" />
				-
				<fmt:parseDate value="${timesheetsByDays[6].date}"
					pattern="yyyy-MM-dd" var="parsedDate" type="date" />
				<fmt:formatDate value="${parsedDate}" pattern="dd MMMM" />
			</h2>
		</div>
	</div>
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
			<c:forEach var="timesheetByDay" items="${timesheetsByDays}"
				varStatus="stat">
				<c:choose>
					<c:when test="${stat.index % 2 == 0}">
						<c:set var="classRow" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="classRow" value="even" />
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${fn:length(timesheetByDay.timesheets) gt 0}">
						<c:forEach var="timesheet" items="${timesheetByDay.timesheets}"
							varStatus="status">
							<tr class="${classRow}">
								<c:if test="${status.index == 0}">
									<td rowspan="${fn:length(timesheetByDay.timesheets)}"><fmt:parseDate
											value="${timesheetByDay.date}" pattern="yyyy-MM-dd"
											var="parsedDate" type="date" /> <fmt:formatDate
											pattern="EEEE" value="${parsedDate}" /> <br /> <fmt:formatDate
											pattern="dd-MM-yyyy" value="${parsedDate}" /></td>
								</c:if>
								<td><c:out value="${timesheet.project.name}" /></td>
								<td><c:out value="${timesheet.task.name}" /></td>
								<td><c:out value="${timesheet.countTime}" /></td>
								<c:if test="${status.index == 0}">
									<td rowspan="${fn:length(timesheetByDay.timesheets)}"><c:out
											value="${timesheetByDay.hours}" /></td>
								</c:if>
								<td><c:out value="${timesheet.comment}" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="${classRow}">
							<td><fmt:parseDate value="${timesheetByDay.date}"
									pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
									pattern="EEEE" value="${parsedDate}" /> <br /> <fmt:formatDate
									pattern="dd-MM-yyyy" value="${parsedDate}" /></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:otherwise>
				</c:choose>

			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td style="text-align: right;" colspan="4">Общее количество
					часов:</td>
				<td colspan="2"><c:out value="${countTime}" /></td>
			</tr>
		</tfoot>
	</table>

	<div id="moreUrl">
		<s:url value="/employees/${employee.employeeId}/?week=${param.week + 1}"
			var="more_url" />
		<a href="${more_url}">Предыдующая неделя</a>
		<s:url value="/employees/${employee.employeeId}/?week=${param.week - 1}"
			var="less_url" />
		<a style="float: right;" href="${less_url}">Следующая неделя</a>
	</div>
</div>
