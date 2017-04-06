<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
	<script src="<c:url value="/resources/script/moment.min.js" />"></script>
	<script src="<c:url value="/resources/script/Chart.js" />"></script>
	<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
	<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/script/showStatistic.js" />"></script>
	<script src="<c:url value="/resources/script/handleFilter.js" />"></script>
</head>

<nav class="filter">
	<h3>Параметры статистики</h3>
	<h4>Тип</h4>
	<select id="statType">
		<option value="1">по сотрудникам</option>
		<option value="2">по задачам</option>
		<option value="3">по времени</option>
	</select>
	<div id="filterItems">
		<h4>Фильтр</h4>
		<input type="checkbox" name="typeAllItems" value="true" id="typeAllItems" checked="checked">
		<span id="typeAllItemsText">Все задачи</span>
		<div id="items" class="items">
			<select id="selectItems" name="selectItems" multiple="multiple">
			</select>
		</div>
		<hr/>
	</div>
	
	<h4>Период: </h4>
	<input
				id="statPeriod" class="statPeriod datepicker-here"
				type="text" data-position="bottom left"
				data-range="true" data-multiple-dates-separator=" - "
				name="statPeriod" value="${statPeriod}" required="required" />
	<hr/>
	<button id="buttonRefresh">Обновить</button>
</nav>

<article>

<div class="divWithBorder">
	<div>
		<div>
			<h2><c:out value="${project.name}" /></h2>
			<span>
			
			</span>
		</div>
		<table class="mainTable" id="tableStat">
			<thead>
				<tr>
					<th scope="col" class="colTask" id="colName">Сотрудник</th>
					<th scope="col" class="colCount">Часы</th>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
	</div>
	<div class="chartContainer">
		<div class="leftChart">
			<canvas id="chartBy" width="300" height="300"></canvas>
		</div>
		<div class="rightChart">
			<canvas id="barChartBy" width="600" height="300"></canvas>
		</div>
	</div>
</div>

</article>

<script>
var ctx1 = document.getElementById("chartBy").getContext("2d");
var ctx2 = document.getElementById("barChartBy").getContext("2d");
var projectId = "<c:out value="${project.projectId}" />";

$(document).ready(function() {
	getStatistic(0, projectId, ctx1, ctx2, "Сотрудник", "Задача");
	$("#buttonRefresh").click(function(){
		getStatistic(1, projectId, ctx1, ctx2, "Сотрудник", "Задача");
	});
	handleFilter(projectId, "Все задачи", "Все сотрудники");
 });
</script>	
