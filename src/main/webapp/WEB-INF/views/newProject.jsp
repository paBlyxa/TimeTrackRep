<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <%@ page session="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%> --%>
<head>
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<s:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/script/clickableRow.js" />"></script>
</head>


<div class="projectForm">
	<h1>Новый проект</h1>
	<form:form method="POST" modelAttribute="projectForm">
		<table class="newRecordTable">
			<thead>
				<tr>
					<th class="colProjectName">Проект</th>
					<th class="colProjectActive">Статус</th>
					<th class="colProjectLeaders">Ведущие сотрудники</th>
					<th class="colProjectComment">Комментарий</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input required="required" class="input" type="text"
						name="name" /></td>
					<td>Актив</td>
					<td><form:select id="selectManagers" path="projectLeaders"
							multiple="true">
							<form:options items="${employeeList}" />
						</form:select></td>
					<td><form:input class="input" type="text" path="comment" /></td>
				</tr>
			</tbody>
			<tfoot></tfoot>
		</table>
		<input type="submit" value="Добавить" class="buttonAdd" />
	</form:form>
</div>

<div class="divWithBorder">
	<c:url var="deleteUrl" value="/projects/delete" />
	<h1>Проекты</h1>

	<table class="mainTable">
		<thead>
			<tr>
				<th class="colProjectName">Проект</th>
				<th class="colProjectActive">Статус</th>
				<th class="colProjectLeaders">Ведущие сотрудники</th>
				<th class="colProjectComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="project" items="${projectList}" varStatus="status">
				<tr class="clickable-row"
					data-url="<c:url value="/"/>projects/project?id=<c:out value="${project.projectId}" />">
					<td><c:out value="${project.name}" /></td>
					<td>Актив</td>
					<td><c:forEach var="leader" items="${project.managers}" varStatus="stat"><c:if test="${stat.index > 0}">, </c:if><c:out value="${leader.shortName}"/></c:forEach></td>
					<td><c:out value="${project.comment}" /></td>
					<td id="colLast">
						<form action="${deleteUrl}" method="POST">
							<input name="projectId" type="hidden"
								value="${project.projectId}" /> <input type="submit"
								value="Удалить" onClick="return confirm('Удалить проект?')" /> <input
								type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot></tfoot>
	</table>

</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('#selectManagers').SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			searchText: 'Введите имя или фамилию...'
			});
	});
</script>
