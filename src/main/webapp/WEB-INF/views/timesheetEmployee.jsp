<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
	<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<script src="<c:url value="/resources/script/timesheet.js" />"></script>
	<script src="<c:url value="/resources/script/myFunctions.js" />"></script>
</head>


<div>
	<h2>
		<c:out value="${employee.surname} ${employee.name}" />
	</h2>
	<span> </span>
</div>

<div class="listTimesheet">
	<div>
		<div id="moreUrl">
			<button class="btnWithIcon" onclick="lastWeek()">
				<i class="fa fa-arrow-circle-left fa-2x" aria-hidden="true"></i>
			</button>
		</div>
		<div id="moreUrl">
			<button class="btnWithIcon" onclick="nextWeek()" style="float: right;">
				<i class="fa fa-arrow-circle-right fa-2x" aria-hidden="true"></i>
			</button>
		</div>
		<div style="float: left;padding:5px;">
			<h2 id="dateRange">
			</h2>
		</div>
		<%-- <div id="saveCurrentTimesheet">
			<form action="employees/xls" method="GET">
				<input name="id" value="${employee.employeeId}" type="hidden" />
				<button type="submit" class="btnWithIcon">
                    <i class="fa fa-download fa-2x"></i>
                </button>
			</form>
		</div> --%>
	</div>
	<table class="timesheetTable">
		<thead>
			<tr>
				<th scope="col" class="colDate">Дата</th>
				<th scope="col" class="colProject">Проект</th>
				<th scope="col" class="colTask">Задача</th>
				<th scope="col" class="colCount"><i class="fa fa-clock-o fa-1x" aria-hidden="true" title="Время выделенное на задачу, часы"></i></th>
				<th scope="col" class="colHours"><i class="fa fa-clock-o fa-1x" aria-hidden="true" title="Общее время работы за день, часы"></i></th>
				<th scope="col" class="colOverHours"><i class="fa fa-clock-o fa-1x" aria-hidden="true" title="Переработки/опоздания за день, часы"></i></th>
				<th scope="col" class="colComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
		<tfoot>
			<tr>
				<td style="text-align: right;" colspan="4">Общее количество	времени работы, ч:</td>
				<td colspan="3">
					<input id= "countTime" class="input" type="number" step="0.5" style="text-align: left;" 
											min="0.5" max="24" readonly="readonly"/></td>
			</tr>
			<tr>
				<td style="text-align: right;" colspan="4">Общее количество	времени переработок, ч:</td>
				<td colspan="3">
					<input id="countOverTime" class="input" type="number" step="0.5" style="text-align: left;" 
											min="0.5" max="24" readonly="readonly"/></td>
			</tr>
		</tfoot>
	</table>

</div>
<script type="text/javascript">
var week = "<c:out value="${param.week}"/>";
var employeeId = "<c:out value="${employee.employeeId}"/>";
$(document).ready(function() {
	getDate(week);
});
</script>