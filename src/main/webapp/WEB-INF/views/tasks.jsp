<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<sec:authorize access="hasAuthority('Операторы архива Projects')">
	<div class="taskForm">
		<h1>Новая задача</h1>
		<form:form method="POST" modelAttribute="taskForm">
			<table class="newRecordTable">
				<thead>
					<tr>
						<th class="colTaskName">Задача</th>
						<th class="colTaskActive">Статус</th>
						<th class="colTaskComment">Комментарий</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><input class="input" type="text" name="name" /></td>
						<td>Актив</td>
						<td><form:input class="input" type="text" path="comment" /></td>
					</tr>
				</tbody>
				<tfoot></tfoot>
			</table>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<input type="submit" class="buttonAdd" value="Добавить" />
		</form:form>
	</div>
</sec:authorize>

<div class="divWithBorder">
	<c:url var="deleteUrl" value="/tasks/delete" />
	<h1>Задачи</h1>
	<table class="mainTable">
		<thead>
			<tr>
				<th class="colTaskName">Задача</th>
				<th class="colTaskActive">Статус</th>
				<th class="colTaskComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="task" items="${taskList}" varStatus="status">
				<%-- 				<c:choose>
					<c:when test="${stat.index % 2 == 0}" >
						<c:set var="classRow" value="odd" />
					</c:when>
					<c:otherwise>					
						<c:set var="classRow" value="even" />
					</c:otherwise>
				</c:choose> --%>
				<tr<%-- class="${classRow}" --%>>
					<td><c:out value="${task.name}" /></td>
					<td>Актив</td>
					<td><c:out value="${task.comment}" /></td>
					<sec:authorize access="hasAuthority('Операторы архива Projects')">
						<td id="colLast">
							<form action="${deleteUrl}" method="POST">
								<input name="taskId" type="hidden" value="${task.taskId}" /> <input
									type="submit" value="Удалить"
									onClick="return confirm('Удалить задачу?')" /> <input
									type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
							</form>
						</td>
					</sec:authorize>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot></tfoot>
	</table>

</div>
