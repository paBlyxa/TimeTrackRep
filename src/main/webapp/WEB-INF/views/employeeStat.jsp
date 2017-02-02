<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
	<script src="<c:url value="/resources/script/Chart.js" />"></script>
	<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
	<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
</head>

<nav class="filter">
	<h3>Параметры статистики</h3>
	<h4>Тип</h4>
	<select id="statType">
		<option value="1">по проектам</option>
		<option value="2">по задачам</option>
	</select>
	<h4>Фильтр</h4>
	<input type="checkbox" name="typeAllItems" value="true" id="typeAllItems" checked="checked">
	<span id="typeAllItemsText">Все задачи</span>
	<div id="items" class="items">
		<select id="selectItems" name="selectItems" multiple="multiple">
		</select>
	</div>
	<hr/>
	
	<h4>Период: </h4>
	<input
				id="statPeriod" class="statPeriod"
				type="text" class="datepicker-here" data-position="bottom left"
				data-range="true" data-multiple-dates-separator=" - "
				name="statPeriod" value="${statPeriod}" required="required" />
	<hr/>
	<button id="buttonRefresh">Обновить</button>

</nav>

<article>

<div class="divWithBorder">
	<div>
		<div>
			<h2><c:out value="${employee.surname} ${employee.name}" /></h2>
			<span>
			
			</span>
		</div>
		
		<table class="mainTable" id="tableStat">
			<thead>
				<tr>
					<th scope="col" class="colProject">Проект</th>
					<th scope="col" class="colCount">Часы</th>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
	</div>
	<div class="chartContainer">
		<div class="leftChart">
			<canvas id="chartByProjects" width="300" height="300"></canvas>
		</div>
		<div class="rightChart">
			<canvas id="barChartByProjects" width="600" height="300"></canvas>
		</div>
	</div>
</div>
	
</article>
<script>
var ctx1 = document.getElementById("chartByProjects").getContext("2d");
var ctx2 = document.getElementById("barChartByProjects").getContext("2d");
var employeeId = "<c:out value="${employee.employeeId}"/>";
	
$(document).ready(function() {
	getStatistic();
	$("#buttonRefresh").click(function(){
		getStatistic();
	});
 });
	
function getStatistic() {
	var items = [];
	//Если галочка все снята, то сохраняем в массив выбранные элементы
	if (! $("#typeAllItems").prop("checked")) {
		$("#selectItems option:selected").each(function() {
			items.push($(this).val());
		})
	}
	//Запрос статистики
	$.getJSON("stat/getData.do",
		{ 
			//Id сотрудника
			employeeId: employeeId,
			//Диапазон дат для выборки данных
			statPeriod: $("#statPeriod").val(),
			//Тип выборки (1 - по проектам, 2 - по задачам)
			type: $("#statType").val(),
			//Выборка по всем элементам ( или выбранным
			itemsAll: $("#typeAllItems").prop("checked"), items: items},
		
		function(data){
	
		$("#tableStat tbody").empty();
		var labels = [];
		var counts = [];
		
		$.each(data, function(str, val){
			
			$("#tableStat tbody").append('<tr><td>' + str + '</td><td>' + val + '</td></tr>');
			labels.push(str);
			counts.push(val);
		});
		
		dataBy = {
			labels: labels,
			datasets: [
			{
				data: counts,
				backgroundColor: [
					"#FF6384",
					"#36A2EB",
					"#FFCE56"
				],
				hoverBackgroundColor: [
				"#FF6384",
				"#36A2EB",
				"#FFCE56"
				]
			}]
		};
				
		var myPieChart = new Chart(ctx1,{
			type: 'pie',
			data: dataBy,
			options: {
				common: {
			   	responsive: false
			    }
			}
		});
				
		var myBarChart = new Chart(ctx2, {
		    type: 'bar',
		    data: dataBy,
		    options: {
			   	scales : {
			  		yAxes: [{
			   			ticks: {
			   				beginAtZero: true
			   			}
			   		}]
			   	},
			   	legend: {
		            display: false
			   	}
			}
		});
	})
};
</script>
<script type="text/javascript">
	$(document).ready(function() {
		//В зависимости от выбора checkbox-а (Все проекты/Все задачи)
		//скрываем или отображаем возможные варианты
		$("#typeAllItems").click(function() {
			if ($("#typeAllItems").prop("checked")) {
				$("#items").hide();
			}
			else {
				//Сброс вариантов
				$("#selectItems").empty();
				$(".optWrapper .options").empty();
				// Загружаем возможные варианты (проекты или задачи)
				$.getJSON("stat/getItems.do",
						{ statPeriod: $('#statPeriod').val(), type: $("#statType").val()},
						function(data){
							$.each(data, function(str, val){
								$("#selectItems")[0].sumo.add(val, str);
							});
						}
				);
				$("#selectItems").SumoSelect({
					placeholder: 'Выберите из списка',
					search: true,
					searchText: 'Введите имя проекта...'
				});
				$("#items").slideDown("slow");
			}
		});
		// Изменяем текст checkbox-а в зависимости от выбора типа фильтра
		$("#statType").change(function() {
			$("#typeAllItems").prop( "checked", true );
			$("#items").hide();
			if ($("#statType").val() == 1){
				$("#typeAllItemsText").text("Все задачи");
			}
			else {
				$("#typeAllItemsText").text("Все проекты");
			}
		});
	});
</script>