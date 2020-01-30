<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<head>
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
</head>

<c:url var="modifyUrl" value="/tasks/new" />
<c:url var="deleteUrl" value="/tasks/delete" />

<div class="projectForm">
	<h1>Изменить задачу</h1>
	<form:form id="formChange" method="POST" modelAttribute="taskForm" action="${modifyUrl}">
		<form:input type="hidden" path="taskId"/>
		<label for="pname">Наименование задачи</label>
		<form:input id="pname" required="required" class="input" type="text" path="name" />
		<label for="status">Статус</label>
		<form:select id="status" class="selectStatus" path="status"	multiple="false">
			<form:options items="${taskStatusList}" itemLabel="name"/>
		</form:select>
		<label for="projects">Проект</label>
		<form:select id="projects" class="selectProjects" path="projects"	multiple="true">
			<form:options items="${projects}" itemLabel="name" itemValue="projectId"/>
		</form:select>
		<label for="selectDepartments">Отделы</label>
		<form:select id="selectDepartments" class="selectDepartments" path="departments" multiple="true">
			<form:options items="${departmentList}" itemLabel="name" itemValue="departmentId"/>
		</form:select>
		<label for="comment">Комментарий</label>
		<form:input id="comment" class="input" type="text" path="comment" />
<%-- 	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
		</form:form>
		<input type="submit" form="formChange" class="buttonAdd" value="Обновить" />
		<form id="formDelete" action="${deleteUrl}" method="POST" style="float: right;">
			<input name="taskId" type="hidden" value="${taskForm.taskId}" />
			<input type="submit" form="formDelete" class="buttonAdd" value="Удалить" onClick="return confirm('Удалить задачу?')" />
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
</div>


<script type="text/javascript">
	$(document).ready(function() {
		$('.selectStatus').SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			searchText: 'Введите статус...'
			});

		$('.selectDepartments').SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			searchText: 'Введите наименование...'
			});

		$('.selectProjects').SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			searchText: 'Введите наименование...'
			});
	});
</script>