<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class = "divWithBorder" style="max-width:600px">
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