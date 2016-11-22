<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<head>
	<script src="<c:url value="/resources/script/Chart.js" />"></script>
	<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
	<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
</head>

<h1><c:out value="${project.name}" /></h1>

<div id="saveContainer" style="width: 480px;">
	<form method="get">
		<div id="saveInnerContainer">
			<label>Статистика за период: </label>
			<input type="text" class="datepicker-here"
							data-position="bottom left"
							data-range="true"
							data-multiple-dates-separator=" - "
							name="period" value="${period}"/>
			 <input type="hidden" name="id" value="${project.projectId}"/>
			<input type="submit" value="Обновить" />
		</div>
	</form>
</div>



<div class="divWithBorder">
	<h2>Статистика проекта по задачам</h2>
	<div>
		<table class="mainTable">
			<thead>
				<tr>
						<th scope="col" class="colTask">Задача</th>
						<th scope="col" class="colCount">Часы</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entry" items="${projectSummaryByTasks}" varStatus="status">
					<tr>
						<td><c:out value="${entry.key}"/></td>
						<td><c:out value="${entry.value}"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="chartContainer">
		<div class="leftChart">
			<canvas id="chartByTasks" width="300" height="300"></canvas>
		</div>
		<div class="rightChart">	
			<canvas id="barChartByTasks" width="600" height="300" ></canvas>
		</div>
	</div>
</div>

<div class="divWithBorder">
	<h2>Статистика проекта по сотрудникам</h2>
	<div>
		<table class="mainTable">
			<thead>
				<tr>
						<th scope="col" class="colTask">Сотрудник</th>
						<th scope="col" class="colCount">Часы</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entry" items="${projectSummaryByEmployees}" varStatus="status">
					<tr>
						<td><c:out value="${entry.key}"/></td>
						<td><c:out value="${entry.value}"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="chartContainer">
		<div class="leftChart">
			<canvas id="chartByEmployees" width="300" height="300"></canvas>
		</div>
		<div class="rightChart">	
			<canvas id="barChartByEmployees" width="600" height="300" ></canvas>
		</div>
	</div>
</div>	
	
<script>
	var labelsByTasks = [];
	var countsByTasks = [];
	var labelsByEmployees = [];
	var countsByEmployees = [];
	
	<c:forEach items="${projectSummaryByTasks}" var="entry">
		labelsByTasks.push("<c:out value="${entry.key}" />");
		countsByTasks.push("<c:out value="${entry.value}" />");
	</c:forEach>
	
	<c:forEach items="${projectSummaryByEmployees}" var="entry">
		labelsByEmployees.push("<c:out value="${entry.key}" />");
		countsByEmployees.push("<c:out value="${entry.value}" />");
	</c:forEach>
	
var dataByTasks = {
	    labels: labelsByTasks,
	    datasets: [
	        {
	            data: countsByTasks,
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
	
var dataByEmployees = {
	    labels: labelsByEmployees,
	    datasets: [
	        {
	            data: countsByEmployees,
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
	
var ctx = document.getElementById("chartByTasks").getContext("2d");
var myPieChart = new Chart(ctx,{
    type: 'pie',
    data: dataByTasks,
    options: {
    	common: {
    		responsive: false
    	}
    }
});
var ctx2 = document.getElementById("barChartByTasks").getContext("2d");
var myBarChart = new Chart(ctx2, {
    type: 'bar',
    data: dataByTasks,
    options: {
    	scales : {
    		yAxes: [{
    			ticks: {
    				beginAtZero: true
    			}
    		}]
    	}
    }
});

var ctx3 = document.getElementById("chartByEmployees").getContext("2d");
var myPieChart = new Chart(ctx3,{
    type: 'pie',
    data: dataByEmployees,
    options: {
    	common: {
    		responsive: false
    	}
    }
});
var ctx4 = document.getElementById("barChartByEmployees").getContext("2d");
var myBarChart = new Chart(ctx4, {
    type: 'bar',
    data: dataByEmployees,
    options: {
    	scales : {
    		yAxes: [{
    			ticks: {
    				beginAtZero: true
    			}
    		}]
    	}
    }
});
</script>