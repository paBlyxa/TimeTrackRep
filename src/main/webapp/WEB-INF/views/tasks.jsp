<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<head>
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<s:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
</head>

<c:url var="saveUrl" value="/tasks/new" />

<sec:authorize access="hasAuthority('Timex руководители')">
	<div class="taskForm">
		<h1>Новая задача</h1>
		<form:form method="POST" modelAttribute="taskForm" action="${saveUrl}">
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
						<td><form:select class="selectStatus" path="status"
							multiple="false">
							<form:options items="${taskStatusList}" itemLabel="name"/>
						</form:select></td>
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
	<h1>Все задачи</h1>
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
					<td>${task.name}</td>
					<td>${task.status.name}</td>
					<td>${task.comment}</td>
					<sec:authorize access="hasAuthority('Timex руководители')">
						<td id="colLast"><c:url value="/tasks/${task.taskId}/modify"
								var="updateUrl" />
							<button class="btn btn-primary"
								onclick="location.href='${updateUrl}'">Изменить</button></td>
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

<script type="text/javascript">
	$(document).ready(function() {
		$('.selectStatus').SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			searchText: 'Введите статус...'
			});
	});
</script>