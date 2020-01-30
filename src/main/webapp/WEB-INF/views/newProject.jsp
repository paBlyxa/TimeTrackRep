<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%> --%>
<head>
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<s:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/script/myContextMenu.js" />"></script>
	<script src="<c:url value="/resources/script/FilterTable.js" />"></script>
</head>


<div class="projectForm">
	<h1>Новый проект</h1>
	<form:form method="POST" modelAttribute="projectForm">
		<label for="pname">Наименование проекта</label>
		<input id="pname" required="required" class="input" type="text" name="name" />
		<label for="contract">Номер договора</label>
		<input id="contract" class="input" type="text" name="contract" />
		<label for="startDate">Дата заключения</label>
		<input id="startDate" class="input" type="date" name="startDate" />
		<label for="endDate">Дата окончания</label>
		<input id="endDate" class="input" type="date" name="endDate" />
		<label for="status">Статус</label>
		<form:select id="status" class="selectStatus" path="status"	multiple="false">
			<form:options items="${statusList}" itemLabel="name"/>
		</form:select>
		<label for="selectManagers">Ведущие сотрудники</label>
		<form:select id="selectManagers" class="selectManagers" path="projectLeaders" multiple="true">
			<form:options items="${employeeList}" />
		</form:select>
		<label for="comment">Комментарий</label>
		<input id="comment" class="input" type="text" name="comment" />

		<input type="submit" value="Создать" class="buttonAdd" />
	</form:form>
</div>

<div class="divWithBorder">
	<c:url var="deleteUrl" value="/projects/delete" />
	
	<h1>Все проекты</h1>

	<div class="input-group">
		<i class="fa fa-search" aria-hidden="true"></i>
		<input type="search" class="light-table-filter" data-table="order-table" placeholder="Поиск">
	</div>
	<table class="order-table table">
		<thead>
			<tr>
				<th class="colProjectName">Проект</th>
				<th class="colProjectContract">Номер договора</th>
				<th class="colProjectDate">Дата заключения</th>
				<th class="colProjectDate">Дата окончания</th>
				<th class="colProjectActive">Статус</th>
				<th class="colProjectLeaders">Ведущие сотрудники</th>
				<th class="colProjectComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="project" items="${projectList}" varStatus="status">
				<c:url var="statUrl" value="/projects/project?id=${project.projectId}" />
				<c:url value="/projects/${project.projectId}/modify" var="updateUrl" />
				<tr class="clickable-m-row" data-url-stat="${statUrl}" data-url-change="${updateUrl}" data-projectid="${project.projectId}">
					<td>${project.name}</td>
					<td>${project.contract}</td>
					<fmt:parseDate value="${project.startDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
					<fmt:formatDate pattern="dd.MM.yyyy" value="${parsedDate}" var="dayDate"/>
					<td data-value="${dayDate}">
						<c:out value="${dayDate}"/>
					</td>
					<fmt:parseDate value="${project.endDate}" pattern="yyyy-MM-dd" var="parsedDate" type="date" />
					<fmt:formatDate pattern="dd.MM.yyyy" value="${parsedDate}" var="dayDate"/>
					<td data-value="${dayDate}">
						<c:out value="${dayDate}"/>
					</td>
					<td>${project.status.name}</td>
					<td>
						<c:forEach var="leader" items="${project.managers}" varStatus="stat"><c:if test="${stat.index > 0}">, </c:if><c:out value="${leader.shortName}"/></c:forEach>
					</td>
					<td>${project.comment}</td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot></tfoot>
	</table>
	
	<form id="formDelete" action="${deleteUrl}" method="POST" style="display: none;">
		<input id="projectId" name="projectId" type="hidden" value="" />
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>

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
			searchText: 'Введите статус...'
			});
		myContextMenu.create([
			{
				name : "<i class='fa fa-pie-chart'></i>Статистика", action : function() {
					window.document.location = $(myContextMenu.element).data("url-stat");
				}
			},
			{
				name : "<i class='fa fa-pencil fa-fw'></i>Изменить", action : function() {
					window.document.location = $(myContextMenu.element).data("url-change");
				}
			},
			{
				name : "<i class='fa fa-trash-o fa-fw'></i>Удалить", action : function() {
					if (confirm('Удалить проект?')) {
						$("#projectId")[0].setAttribute("value", $(myContextMenu.element).data("projectid"));
						$("#formDelete").submit();
					}
				}
			}
		]);
		
		$(".clickable-m-row").dblclick(function(e) {
			myContextMenu.element = this;
			myContextMenu.setPosition(e.pageY, e.pageX);
		});
	});
</script>
