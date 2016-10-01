<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<div>
	<h1>Данные сотрудника</h1>
	<form:form method="POST" modelAttribute="employee">
		<table class="newTable">
			<thead>
				<tr>
					<th>Имя пользователя</th>
					<th>Пароль</th>
					<th>Имя</th>
					<th>Фамилия</th>
					<th>Адрес почты</th>
					<th>Должность</th>
					<th>Отдел</th>					
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input class="input" type="text" name="username" /></td>
					<td><input class="input" type="password" name="password" /></td>
					<td><input class="input" type="text" name="name" /></td>
					<td><input class="input" type="text" name="surname" /></td>
					<td><input class="input" type="text" name="mail" /></td>
					<td><input class="input" type="text" name="post" /></td>
					<td><input class="input" type="text" name="department" /></td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    	<input type="submit" value="Добавить" />
	</form:form>
</div>

<h2>Статистика по проектам</h2>
<div>
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