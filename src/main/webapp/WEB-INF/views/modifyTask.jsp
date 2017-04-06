<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<head>
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
</head>

<c:url var="modifyUrl" value="/tasks/new" />

<sec:authorize access="hasAuthority('Операторы архива Projects')">
	<div class="taskForm">
		<h1>Изменить задачу</h1>
		<form:form method="POST" modelAttribute="taskForm" action="${modifyUrl}">
			<form:input type="hidden" path="taskId"/>
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
						<td><form:input class="input" type="text" path="name" /></td>
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
			<input type="submit" class="buttonAdd" value="Обновить" />
		</form:form>
	</div>
</sec:authorize>


<script type="text/javascript">
	$(document).ready(function() {
		$('.selectStatus').SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			searchText: 'Введите статус...'
			});
	});
</script>