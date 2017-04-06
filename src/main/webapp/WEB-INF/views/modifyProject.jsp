<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<head>
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
</head>

<c:url var="modifyUrl" value="/projects/new" />

<div class="projectForm">
	<h1>Изменить проект</h1>
	<form:form method="POST" modelAttribute="project" action="${modifyUrl}">
		<form:input type="hidden" path="projectId"/>
		<table class="newRecordTable">
			<thead>
				<tr>
					<th class="colProjectName">Проект</th>
					<th class="colProjectContract">Номер договора</th>
					<th class="colProjectActive">Статус</th>
					<th class="colProjectLeaders">Ведущие сотрудники</th>
					<th class="colProjectComment">Комментарий</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><form:input required="required" class="input" type="text"
						path="name" /></td>
					<td><form:input class="input" type="text"
						path="contract" /></td>
					<td><form:select class="selectStatus" path="status"
							multiple="false">
							<form:options items="${statusList}" itemLabel="name"/>
						</form:select></td>
					<td><form:select class="selectManagers" path="projectLeaders"
							multiple="true">
							<form:options items="${employeeList}" />
						</form:select></td>
					<td><form:input class="input" type="text" path="comment" /></td>
				</tr>
			</tbody>
			<tfoot></tfoot>
		</table>
		<input type="submit" value="Обновить" class="buttonAdd" />
	</form:form>
</div>


<script type="text/javascript">
	$(document).ready(function() {
		$('.selectManagers').SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			searchText: 'Введите имя или фамилию...'
			});
		$('.selectStatus').SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			searchText: 'Введите имя или фамилию...'
			});
	});
</script>