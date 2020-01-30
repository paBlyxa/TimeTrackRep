<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
<script src="<c:url value="/resources/script/clickableCell.js" />"></script>
<script src="<c:url value="/resources/script/myFunctions.js" />"></script>
<script src="<c:url value="/resources/script/timesheet.js" />"></script>
</head>

<div class="timesheetForm">
	<h1>Новая запись</h1>
	<form:form method="POST" modelAttribute="timesheetForm"
		onSubmit="return validate()">
		<table class="newRecordTable">
			<thead>
				<tr>
					<th class="colDateForm">Дата</th>
					<th class="colProject">Проект</th>
					<th class="colTask">Задача</th>
					<th class="colCount">Часы</th>
					<th class="colComment">Комментарий</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><s:bind path="dates">
							<form:input type="text" required="true" class="input" id="datepickerTS"
								data-position="bottom left" data-multiple-dates="true"
								data-multiple-dates-separator=", " path="dates" />
							<form:errors path="dates" class="errorMessage" />
						</s:bind></td>
					<td><form:select path="projectId" class="input" required="true" id="selectProject">
						</form:select></td>
					<td><form:select path="taskId" class="input" required="true" id="selectTask">
						</form:select></td>
					<td><form:input class="input" type="number" step="0.5"
							path="countTime" min="0.5" max="24" list="listTimes"
							required="true" id="inputCountTime" value="1"/> <datalist id="listTimes">
							<option value="0,5" />
							<option value="1,0" />
							<option value="2,0" />
							<option value="4,0" />
							<option value="8,0" />
						</datalist></td>
					<td><form:input class="input" type="text" path="comment" id="inputComment"/></td>
				</tr>
			</tbody>
			<tfoot></tfoot>
		</table>
		<div>
			<!-- <button class="buttonAdd" onclick="newTimesheet()">Добавить</button> -->
			<input type="submit" value="Добавить" class="buttonAdd" />
			<p class="errorMessage" id="errorValidate"style="display: inline-block;"></p>
		</div>
	</form:form>
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
		<div id="saveCurrentTimesheet">
			<form action="employees/xls" method="GET">
				<input name="id" value="" type="hidden" />
				<button type="submit" class="btnWithIcon">
                    <i class="fa fa-download fa-2x"></i>
                </button>
			</form>
		</div>
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
var prjMap;
var tasks;
var allTasks;
var startDate, endDate;
var datepicker;



$(document).ready(function() {
	$('#selectProject').SumoSelect({
		placeholder : 'Выберите проект из списка',
		search : true,
		searchText : 'Поиск по названию проекта'
	});
	$('#selectTask').SumoSelect({
		placeholder : 'Выберите задачу из списка',
		search : true,
		searchText : 'Поиск по названию задачи'
	});
	
	// Запрос задач
	$.getJSON("timesheet/getTasks.do", {},
	// Разбор полученных данных
	function(data) {
		tasks = data;
		fillSelectTask(data);
		allTasks = true;
	});
	
	// Запрос проектов
	$.getJSON("timesheet/getProjects.do", {},
	// Разбор полученных данных
	function(data) {
		prjMap = new Map();
		$("#selectProject")[0].sumo.add("", "--Выберите проект");
		for (var i = 0; i < data.length; i++) {
			prjMap.set(data[i].projectId, data[i]);
			$("#selectProject")[0].sumo.add(data[i].projectId, data[i].name);
		}
		$("#selectProject")[0].sumo.reload();
	});
	
	// По выбору нового проекта
	$("#selectProject")[0].onchange = function() {
		var prjId = $("#selectProject option:selected").val();
		var prj = prjMap.get(parseInt(prjId));
		if (prj.tasks.length > 0){
			fillSelectTask(prj.tasks);
			allTasks = false;
		} else if (!allTasks){
			fillSelectTask(tasks);
			allTasks = true;
		}
	};
	
	getDate(week);
	
	var elem = document.getElementById("inputCountTime");
	if (elem.addEventListener) {
		  if ('onwheel' in document) {
		  	// IE9+, FF17+, Ch31+
		    elem.addEventListener("wheel", onWheel);
		  } else if ('onmousewheel' in document) {
		    // устаревший вариант события
		    elem.addEventListener("mousewheel", onWheel);
		  } else {
		    // Firefox < 17
		    elem.addEventListener("MozMousePixelScroll", onWheel);
		  }
	} else { // IE8-
	  elem.attachEvent("onmousewheel", onWheel);
	}
});

// Для сравнения дат
var dates = {
    convert:function(d) {
        // Converts the date in d to a date-object. The input can be:
        //   a date object: returned without modification
        //  an array      : Interpreted as [year,month,day]. NOTE: month is 0-11.
        //   a number     : Interpreted as number of milliseconds
        //                  since 1 Jan 1970 (a timestamp) 
        //   a string     : Any format supported by the javascript engine, like
        //                  "YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.
        //  an object     : Interpreted as an object with year, month and date
        //                  attributes.  **NOTE** month is 0-11.
        return (
            d.constructor === Date ? d :
            d.constructor === Array ? new Date(d[0],d[1],d[2]) :
            d.constructor === Number ? new Date(d) :
            d.constructor === String ? new Date(d) :
            typeof d === "object" ? new Date(d.year,d.month,d.date) :
            NaN
        );
    },
    compare:function(a,b) {
        // Compare two dates (could be of any type supported by the convert
        // function above) and returns:
        //  -1 : if a < b
        //   0 : if a = b
        //   1 : if a > b
        // NaN : if a or b is an illegal date
        // NOTE: The code inside isFinite does an assignment (=).
        return (
            isFinite(a=this.convert(a).valueOf()) &&
            isFinite(b=this.convert(b).valueOf()) ?
            (a>b)-(a<b) :
            NaN
        );
    },
    inRange:function(d,start,end) {
        // Checks if date in d is between dates in start and end.
        // Returns a boolean or NaN:
        //    true  : if d is between start and end (inclusive)
        //    false : if d is before start or after end
        //    NaN   : if one or more of the dates is illegal.
        // NOTE: The code inside isFinite does an assignment (=).
       return (
            isFinite(d=this.convert(d).valueOf()) &&
            isFinite(start=this.convert(start).valueOf()) &&
            isFinite(end=this.convert(end).valueOf()) ?
            start <= d && d <= end :
            NaN
        );
    }
};

// Изменение количества часов колесиком мышки
onWheel = function(e) {
	  e = e || window.event;
	  var elem = document.getElementById("inputCountTime");
	  // wheelDelta не дает возможность узнать количество пикселей
	  var delta = e.deltaY || e.detail || e.wheelDelta;
	  if (delta > 0){
		  elem.value -= 0.5;
	  } else {
		  elem.value -= -0.5;
	  }
	  if (elem.value < 0.5) {
		  elem.value = 0.5;
	  } else if (elem.value > 24){
		  elem.value = 24;
	  }
	  
	  e.preventDefault ? e.preventDefault() : (e.returnValue = false);
}
// Заполняем поле выбора задач
fillSelectTask = function(data) {
	$("#selectTask").empty();
	$("#selectTask")[0].sumo.add("", "--Выберите задачу");
	for (var i = 0; i < data.length; i++) {
		$("#selectTask")[0].sumo.add(data[i].taskId, data[i].name)
	}
	if (data.length == 1){
		$('#selectTask')[0].sumo.selectItem(1);
	}
	$('#selectTask')[0].sumo.reload();
}


</script>
