<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
<script src="<c:url value="/resources/script/clickableRow.js" />"></script>
</head>

<div class="divWithBorder">
	<h1>Сотрудники</h1>
	<table class="mainTable">
		<thead>
			<tr>
				<th>Имя пользователя</th>
				<th>Имя</th>
				<th>Фамилия</th>
				<th>Адрес почты</th>
				<th>Должность</th>
				<th>Отдел</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="e" items="${employeeList}">
				<c:url var="statUrl" value="/employees/stat?id=${e.employeeId}" />
				<tr>
					<td class="clickable-row" data-url="${statUrl}"><c:out value="${e.username}" /></td>
					<td class="clickable-row" data-url="${statUrl}"><c:out value="${e.name}" /></td>
					<td class="clickable-row" data-url="${statUrl}"><c:out value="${e.surname}" /></td>
					<td class="clickable-row" data-url="${statUrl}"><c:out value="${e.mail}" /></td>
					<td class="clickable-row" data-url="${statUrl}"><c:out value="${e.post}" /></td>
					<td class="clickable-row" data-url="${statUrl}"><c:out value="${e.department}" /></td>
					<td id="colLast"><c:url value="/employees/${e.employeeId}/"
								var="showTimesheetUrl" />
							<button class="btn btn-primary"
								onclick="location.href='${showTimesheetUrl}'">Учет</button></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>