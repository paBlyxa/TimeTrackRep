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
		<label for="pname">Наименование проекта</label>
		<form:input id="pname" required="required" class="input" type="text" path="name" />
		<label for="contract">Номер договора</label>
		<form:input id="contract" class="input" type="text" path="contract" />
		<label for="startDate">Дата заключения</label>
		<form:input id="startDate" class="input" type="date" path="startDate" />
		<label for="endDate">Дата окончания</label>
		<form:input id="endDate" class="input" type="date" path="endDate" />
		<label for="status">Статус</label>
		<form:select id="status" class="selectStatus" path="status"	multiple="false">
			<form:options items="${statusList}" itemLabel="name"/>
		</form:select>
		<label for="selectManagers">Ведущие сотрудники</label>
		<form:select id="selectManagers" class="selectManagers" path="projectLeaders" multiple="true">
			<form:options items="${employeeList}" />
		</form:select>
		<label for="tasks">Задача</label>
		<form:select id="tasks" class="selectStatus" path="tasks"	multiple="true">
			<form:options items="${tasks}" itemLabel="name" itemValue="taskId"/>
		</form:select>
		<label for="comment">Комментарий</label>
		<form:input id="comment" class="input" type="text" path="comment" />

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