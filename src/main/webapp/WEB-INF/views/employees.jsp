<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
<script src="<c:url value="/resources/script/myContextMenu.js" />"></script>
<script src="<c:url value="/resources/script/tabs.js" />"></script>
<script src="<c:url value="/resources/script/FilterTable.js" />"></script>
<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
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
						<tr class="clickable-m-row" data-url-view="${showTimesheetUrl}" data-url-stat="${statUrl}" data-employeeid="${e.employeeId}">
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
						<tr class="clickable-m-row" data-url-view="${showTimesheetUrl}" data-url-stat="${statUrl}"
							data-employeeId="${e.employeeId}">
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

	<div id="saveWindow" class="modal">
	  <!-- Modal content -->
		  <div class="modal-content">
			  <div class="modal-header">
			    <span class="close closeSaveWindow">&times;</span>
			    	<h2 id="header" >Сохранить отчет</h2>
			  </div>
			  <div class="modal-body" >
				  <form action="employees/download" method="GET" style="margin: 10px;">
						<div id="saveInnerContainer">
						<label   style="font-size:12pt;">Период: </label>
						<input name="id" id="fieldEmployeeid" type="hidden" />
						<input type="text"  style="font-size:12pt;min-width: 180px;"
							class="datepicker-here" data-position="bottom left"
							data-range="true" data-multiple-dates-separator=" - "
							name="period" required="required" />
						<input type="submit" value="сохранить"   style="font-size:12pt;"/>
						</div>
					</form>
			  </div>	  
		  </div>
	</div>
	
<script type="text/javascript">
var saveWindow = document.getElementById("saveWindow");

document.getElementsByClassName("closeSaveWindow")[0].onclick = function(){
	saveWindow.style.display = "none";
}
window.onclick = function(event) {
	if (event.target == modal) {
		saveWindow.style.display = "none";
	}
}

$(document).ready(function() {
	myContextMenu.create([
	{name : "<i class='fa fa-table'></i>Учет", action : function() {
		window.document.location = $(myContextMenu.element).data("url-view");
	} },
	{name : "<i class='fa fa-pie-chart'></i>Статистика", action : function() {
		window.document.location = $(myContextMenu.element).data("url-stat");
	}},
	{name : "<i class='fa fa-download'></i>Сохранить", action : function() {
		console.log($(myContextMenu.element).data("employeeid"));
		document.getElementById("fieldEmployeeid").value = $(myContextMenu.element).data("employeeid");
		saveWindow.style.display = "block";
	}}]);
	
	$(".clickable-m-row").dblclick(function(e) {
		myContextMenu.element = this;
		myContextMenu.setPosition(e.pageY, e.pageX);
	});
});
</script>