<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
<script src="<c:url value="/resources/script/myContextMenu.js" />"></script>
<script src="<c:url value="/resources/script/tabs.js" />"></script>
<script src="<c:url value="/resources/script/FilterTable.js" />"></script>
<style type="text/css">
	.divWithBorder {min-width: 930px;}
</style>
</head>


<div class="divWithBorder">
	<button class="tablink defaultOpen" onclick="openPage('now', this)">Текущие сотрудники</button>
	<button class="tablink" onclick="openPage('old', this)">Бывшие сотрудники</button>	
	<div class="tablink"></div>
	<div class="input-group">
		<i class="fa fa-search" aria-hidden="true"></i>
		<input type="search" class="light-table-filter" data-table="order-table" placeholder="Поиск">
	</div>
	<div id="now" class="tabcontent">
		<table class="order-table table">
			<thead>
				<tr>
					<th>Фамилия</th>
					<th>Имя</th>
					<th>Адрес почты</th>
					<th>Должность</th>
					<th>Отдел</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${employeeList}">
					<c:if test="${e.isActive()}">
						<c:url var="showTimesheetUrl" value="/employees/${e.employeeId}/" />
						<c:url var="statUrl" value="/employees/stat?id=${e.employeeId}" />
						<tr class="clickable-m-row" data-url-view="${showTimesheetUrl}" data-url-stat="${statUrl}">
							<td><c:out value="${e.surname}" /></td>
							<td><c:out value="${e.name}" /></td>
							<td><c:out value="${e.mail}" /></td>
							<td><c:out value="${e.post}" /></td>
							<td><c:out value="${e.department.name}" /></td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div id="old" class="tabcontent">
		<table class="order-table table">
			<thead>
				<tr>
					<th>Фамилия</th>
					<th>Имя</th>
					<th>Адрес почты</th>
					<th>Должность</th>
					<th>Отдел</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="e" items="${employeeList}">
					<c:if test="${!e.isActive()}">
						<c:url var="showTimesheetUrl" value="/employees/${e.employeeId}/" />
						<c:url var="statUrl" value="/employees/stat?id=${e.employeeId}" />
						<tr class="clickable-m-row" data-url-view="${showTimesheetUrl}" data-url-stat="${statUrl}">
							<td><c:out value="${e.surname}" /></td>
							<td><c:out value="${e.name}" /></td>
							<td><c:out value="${e.mail}" /></td>
							<td><c:out value="${e.post}" /></td>
							<td><c:out value="${e.department.name}" /></td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>	
	</div>
</div>

<script type="text/javascript">
$(document).ready(function() {
	myContextMenu.create([
	{name : "<i class='fa fa-table'></i>Учет", action : function() {
		window.document.location = $(myContextMenu.element).data("url-view");
	} },
	{name : "<i class='fa fa-pie-chart'></i>Статистика", action : function() {
		window.document.location = $(myContextMenu.element).data("url-stat");
	}}]);
	
	$(".clickable-m-row").dblclick(function(e) {
		myContextMenu.element = this;
		myContextMenu.setPosition(e.pageY, e.pageX);
	});
});
</script>