<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
	<script src="<c:url value="/resources/script/Chart.js" />"></script>
	<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
	<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
</head>

<div class="inlineContainer">
		<h1><c:out value="${employee.surname} ${employee.name}" /></h1>
		<div id="saveContainer" style="float: right;">
			<form action="employees/xls" method="GET">
				<div id="saveInnerContainer">
					<label>Отчет: </label>
					<input name="id" value="${employee.employeeId}" type="hidden"/>
					<input type="text" class="datepicker-here"
									data-position="bottom left"
									data-range="true"
									data-multiple-dates-separator=" - "
									name="savePeriod"
									required="required"/>
			    	<input type="submit" value="сохранить" />
		    	</div>
			</form>
		</div>
</div>

<div id="saveContainer" style="width: 480px;">
	<form method="get">
		<div id="saveInnerContainer">
			<label>Статистика за период: </label>
			<input type="text" class="datepicker-here"
							data-position="bottom left"
							data-range="true"
							data-multiple-dates-separator=" - "
							name="statPeriod" value="${statPeriod}"
							required="required"/>
			<input type="submit" value="Обновить" />
		</div>
	</form>
</div>

<div class = "divWithBorder">
	<div>
		<h2>Статистика по проектам</h2>
		<table class="mainTable">
			<thead>
				<tr>
						<th scope="col" class="colProject">Проект</th>
						<th scope="col" class="colCount">Часы</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entry" items="${summaryByProjects}" varStatus="status">
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
			<canvas id="chartByProjects" width="300" height="300"></canvas>
		</div>
		<div class="rightChart">	
			<canvas id="barChartByProjects" width="600" height="300" ></canvas>
		</div>
	</div>
</div>

<div class = "divWithBorder">
	<h2>Статистика по задачам</h2>
	<div>
		<table class="mainTable">
			<thead>
				<tr>
						<th scope="col" class="colTask">Задача</th>
						<th scope="col" class="colCount">Часы</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="entry" items="${summaryByTasks}" varStatus="status">
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

<script>
	var labelsByTasks = [];
	var countsByTasks = [];
	var labelsByProjects = [];
	var countsByProjects = [];
	
	<c:forEach items="${summaryByTasks}" var="entry">
		labelsByTasks.push("<c:out value="${entry.key}" />");
		countsByTasks.push("<c:out value="${entry.value}" />");
	</c:forEach>
	
	<c:forEach items="${summaryByProjects}" var="entry">
		labelsByProjects.push("<c:out value="${entry.key}" />");
		countsByProjects.push("<c:out value="${entry.value}" />");
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
	
var dataByProjects = {
	    labels: labelsByProjects,
	    datasets: [
	        {
	            data: countsByProjects,
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

var ctx3 = document.getElementById("chartByProjects").getContext("2d");
var myPieChart = new Chart(ctx3,{
    type: 'pie',
    data: dataByProjects,
    options: {
    	common: {
    		responsive: false
    	}
    }
});
var ctx4 = document.getElementById("barChartByProjects").getContext("2d");
var myBarChart = new Chart(ctx4, {
    type: 'bar',
    data: dataByProjects,
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