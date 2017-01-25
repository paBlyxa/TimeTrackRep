<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
	<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
	<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
</head>

<div class="divWithBorder" style="max-width: 600px">
	<h1>Данные сотрудника</h1>
	<table class="mainTable">
		<thead>
		</thead>
		<tbody>
			<tr>
				<td>Имя пользователя</td>
				<td><c:out value="${employee.username}" /></td>
			</tr>
			<tr>
				<td>Имя</td>
				<td><c:out value="${employee.name}" /></td>
			</tr>
			<tr>
				<td>Фамилия</td>
				<td><c:out value="${employee.surname}" /></td>
			</tr>
			<tr>
				<td>Адрес почты</td>
				<td><c:out value="${employee.mail}" /></td>
			</tr>
			<tr>
				<td>Должность</td>
				<td><c:out value="${employee.post}" /></td>
			</tr>
			<tr>
				<td>Отдел</td>
				<td><c:out value="${employee.department}" /></td>
			</tr>
		</tbody>
	</table>
</div>

<div id="saveContainer" >
		<form action="xls" method="GET">
			<div id="saveInnerContainer">
				<label>Отчет: </label>
				<input name="id" value="${employee.employeeId}" type="hidden" />
				<input type="text"
					class="datepicker-here" data-position="bottom left"
					data-range="true" data-multiple-dates-separator=" - "
					name="period" required="required" />
				<input type="submit" value="сохранить" />
			</div>
		</form>
</div>