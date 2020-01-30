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
	<script src="<c:url value="/resources/script/FilterTable.js" />"></script>
	<script src="<c:url value="/resources/script/myContextMenu.js" />"></script>
</head>

<c:url var="saveUrl" value="/tasks/new" />
<c:url var="deleteUrl" value="/tasks/delete" />

<sec:authorize access="hasAuthority('modify')">
	<div class="projectForm">
		<h1>Новая задача</h1>
		<form:form method="POST" modelAttribute="taskForm" action="${saveUrl}">
			<label for="pname">Наименование задачи</label>
			<input id="pname" required="required" class="input" type="text" name="name" />
			<label for="status">Статус</label>
			<form:select id="status" class="selectStatus" path="status"	multiple="false">
				<form:options items="${taskStatusList}" itemLabel="name"/>
			</form:select>
			<label for="selectDepartments">Отделы</label>
			<form:select id="selectDepartments" class="selectDepartments" path="departments" multiple="true">
				<form:options items="${departmentList}" itemLabel="name" itemValue="departmentId"/>
			</form:select>
			<label for="comment">Комментарий</label>
			<input id="comment" class="input" type="text" name="comment" />
<%-- 			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
			<input type="submit" class="buttonAdd" value="Добавить" />
		</form:form>
	</div>
</sec:authorize>

<div class="divWithBorder">
	<h1>Все задачи</h1>
	<div class="input-group">
		<i class="fa fa-search" aria-hidden="true"></i>
		<input type="search" class="light-table-filter" data-table="order-table" placeholder="Поиск">
	</div>
	<table class="order-table table">
		<thead>
			<tr>
				<th class="colTaskName">Задача</th>
				<th class="colTaskActive">Статус</th>
				<th class="colDepartment">Отделы</th>
				<th class="colTaskComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="task" items="${taskList}" varStatus="status">
				<c:url value="/tasks/${task.taskId}/modify" var="updateUrl" />
				<tr 
				<sec:authorize access="hasAuthority('modify')">
					class="clickable-m-row" data-url-change="${updateUrl}"
					data-taskid="${task.taskId}"
				</sec:authorize>>
					<td>${task.name}</td>
					<td>${task.status.name}</td>
					<td>
						<c:forEach var="dep" items="${task.departments}" varStatus="stat">
							<c:if test="${stat.index > 0}">, </c:if>
							<c:out value="${dep.name}"/></c:forEach></td>
					<td>${task.comment}</td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot></tfoot>
	</table>
	
	<form id="formDelete" action="${deleteUrl}" method="POST" style="display: none;">
		<input id="taskId" name="taskId" type="hidden" value="" />
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
		myContextMenu.create([{name : "<i class='fa fa-pencil fa-fw'></i>Изменить", action : function() {
			window.document.location = $(myContextMenu.element).data("url-change");
		} },
		{name : "<i class='fa fa-trash-o fa-fw'></i>Удалить", action : function() {
			if (confirm('Удалить задачу?')) {
				$("#taskId")[0].setAttribute("value", $(myContextMenu.element).data("taskid"));
				$("#formDelete").submit();
			}
		}}]);
		
		$(".clickable-m-row").dblclick(function(e) {
			myContextMenu.element = this;
			myContextMenu.setPosition(e.pageY, e.pageX);
		});
	});
	

</script>