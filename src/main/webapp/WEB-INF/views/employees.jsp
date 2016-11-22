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
					<tr class="clickable-row" data-url="/TimeTrack/employees/employee?id=<c:url value="${e.employeeId}" />">
						<td><c:out value="${e.username}"/></td>
						<td><c:out value="${e.name}"/></td>
						<td><c:out value="${e.surname}"/></td>
						<td><c:out value="${e.mail}"/></td>
						<td><c:out value="${e.post}"/></td>
						<td><c:out value="${e.department}"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</div>