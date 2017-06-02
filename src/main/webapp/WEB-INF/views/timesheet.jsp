<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
<script
	src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
<link href="<s:url value="/resources/sumoselect.css"/>" rel="stylesheet"
	type="text/css">

<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
<link href="<s:url value="/resources/datepicker.min.css"/>"
	rel="stylesheet" type="text/css">
</head>

<div class="timesheetForm">
	<h1>Новая запись</h1>
	<form:form method="POST" modelAttribute="timesheetForm"
		onSubmit="return validate()">
		<table class="newRecordTable">
			<thead>
				<tr>
					<th class="colDate">Дата</th>
					<th class="colProject">Проект</th>
					<th class="colTask">Задача</th>
					<th class="colCount">Часы</th>
					<th class="colComment">Комментарий</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><s:bind path="dates">
							<form:input type="text" required="true"
								class="input" id="datepickerTS"
								data-position="bottom left" data-multiple-dates="true"
								data-multiple-dates-separator=", " path="dates" />
							<form:errors path="dates" class="errorMessage" />
						</s:bind></td>
					<td><form:select path="projectId" class="input"
							required="true" id="selectProject">
							<form:option value="" label="--Выберите проект" />
							<form:options items="${projectList}" itemValue="projectId"
								itemLabel="name" />
						</form:select></td>
					<td><form:select path="taskId" class="input" required="true"
							id="selectTask">
							<form:option value="" label="--Выберите задачу" />
							<form:options items="${taskList}" itemValue="taskId"
								itemLabel="name" />
						</form:select></td>
					<td><form:input class="input" type="number" step="0.5"
							path="countTime" min="0.5" max="24" list="listTimes"
							required="true" id="inputCountTime" /> <datalist id="listTimes">
							<option value="0,5" />
							<option value="1,0" />
							<option value="2,0" />
							<option value="4,0" />
							<option value="8,0" />
						</datalist></td>
					<td><form:input class="input" type="text" path="comment" /></td>
				</tr>
			</tbody>
			<tfoot></tfoot>
		</table>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div>
			<input type="submit" value="Добавить" class="buttonAdd" />
			<p class="errorMessage" id="errorValidate"
				style="display: inline-block;"></p>
		</div>
	</form:form>
</div>

<div class="listTimesheet">
	<c:url var="deleteUrl" value="/timesheet/delete" />
	<c:url var="modifyCountTimeUrl" value="/timesheet/modifyCountTime" />
	<c:url var="modifyCommentUrl" value="/timesheet/modifyComment" />
	<div>
		<div style="float: left;">
			<h2>
				<fmt:parseDate value="${timesheetsByDays[0].date}"
					pattern="yyyy-MM-dd" var="startWeekDate" type="date" />
				<fmt:formatDate pattern="w" value="${startWeekDate}" />
				неделя
				<fmt:formatDate pattern="y" value="${startWeekDate}" />
				года:
				<fmt:formatDate pattern="dd MMMM" value="${startWeekDate}" />
				-
				<fmt:parseDate value="${timesheetsByDays[6].date}"
					pattern="yyyy-MM-dd" var="endWeekDate" type="date" />
				<fmt:formatDate value="${endWeekDate}" pattern="dd MMMM" />
			</h2>
		</div>
		<div id="saveCurrentTimesheet">
			<form action="employees/xls" method="GET">
				<input name="id" value="${employee.employeeId}" type="hidden" /> <input
					name="week" value="${param.week}" type="hidden" /> <input
					type="submit" value="Сохранить" />
			</form>
		</div>
	</div>
	<table class="timesheetTable">
		<thead>
			<tr>
				<th scope="col" class="colDate">Дата</th>
				<th scope="col" class="colProject">Проект</th>
				<th scope="col" class="colTask">Задача</th>
				<th scope="col" class="colCount">Часы</th>
				<th scope="col" class="colHours">Часы</th>
				<th scope="col" class="colComment">Комментарий</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="timesheetByDay" items="${timesheetsByDays}"
				varStatus="stat">
				<c:choose>
					<c:when test="${stat.index % 2 == 0}">
						<c:set var="classRow" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="classRow" value="even" />
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${fn:length(timesheetByDay.timesheets) gt 0}">
						<c:forEach var="timesheet" items="${timesheetByDay.timesheets}"
							varStatus="status">
							<tr class="${classRow}">
								<c:if test="${status.index == 0}">
									<td rowspan="${fn:length(timesheetByDay.timesheets)}"><fmt:parseDate
											value="${timesheetByDay.date}" pattern="yyyy-MM-dd"
											var="parsedDate" type="date" /> <fmt:formatDate
											pattern="EEEE" value="${parsedDate}" /> <br /> <fmt:formatDate
											pattern="dd-MM-yyyy" value="${parsedDate}" /></td>
								</c:if>
								<td>${timesheet.project.name}</td>
								<td>${timesheet.task.name}</td>
								<td>
									<form action="${modifyCountTimeUrl}" method="POST">
										<input name="week" type="hidden" value="${param.week}" /> <input
											name="timesheetId" type="hidden" value="${timesheet.id}" />
										<input name="countTime" class="input" type="number" step="0.5"
											min="0.5" max="24" value="${timesheet.countTime}" /> <input
											type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" />
									</form>
								</td>
								<c:if test="${status.index == 0}">
									<td rowspan="${fn:length(timesheetByDay.timesheets)}"><input class="input" type="number" step="0.5"
											min="0.5" max="24" readonly="readonly" value="${timesheetByDay.hours}" /></td>
								</c:if>
								<td>
									<form action="${modifyCommentUrl}" method="POST">
										<input name="week" type="hidden" value="${param.week}" /> <input
											name="timesheetId" type="hidden" value="${timesheet.id}" />
										<input name="comment" class="input" type="text"
											value="${timesheet.comment}" /> <input type="hidden"
											name="${_csrf.parameterName}" value="${_csrf.token}" />
									</form>
								</td>
								<td id="colLast">
									<form action="${deleteUrl}" method="POST">
										<input name="week" type="hidden" value="${param.week}" /> <input
											name="timesheetId" type="hidden" value="${timesheet.id}" />
										<input type="submit" value="Удалить"
											onClick="return confirm('Удалить запись?')" /> <input
											type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" />
									</form>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="${classRow}">
							<td><fmt:parseDate value="${timesheetByDay.date}"
									pattern="yyyy-MM-dd" var="parsedDate" type="date" /> <fmt:formatDate
									pattern="EEEE" value="${parsedDate}" /> <br /> <fmt:formatDate
									pattern="dd-MM-yyyy" value="${parsedDate}" /></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:otherwise>
				</c:choose>

			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td style="text-align: right;" colspan="4">Общее количество
					часов:</td>
				<td colspan="2"><input class="input" type="number" step="0.5"
											min="0.5" max="24" readonly="readonly" value="${countTime}" /></td>
			</tr>
		</tfoot>
	</table>

	<div id="moreUrl">
		<s:url value="/timesheet?week=${param.week + 1}" var="more_url" />
		<a href="${more_url}">Предыдующая неделя</a>
		<s:url value="/timesheet?week=${param.week - 1}" var="less_url" />
		<a style="float: right;" href="${less_url}">Следующая неделя</a>
	</div>
</div>

<script type="text/javascript">
	function validate() {
		if (isNaN(document.getElementById("inputCountTime").value)) {
			document.getElementById("errorValidate").innerHTML = "Введите числовое значение в поле часы";
			return false;
		} else {
			if (document.getElementById("inputCountTime").value <= 0) {
				document.getElementById("errorValidate").innerHTML = "Введите положительное и ненулевое значение в поле часы";
				return false;
			}
		}
		return true;
	};
	$(document).ready(function() {
		$('#selectProject').SumoSelect({
			placeholder : 'Выберите проект из списка',
			search : true,
			searchText : 'Поиск по названию проекта'
		});
	});
	$(document).ready(function() {
		$('#selectTask').SumoSelect({
			placeholder : 'Выберите задачу из списка',
			search : true,
			searchText : 'Поиск по названию задачи'
		});
	});
	var day = <fmt:formatDate pattern="dd" value="${startWeekDate}" />;
	var month = <fmt:formatDate pattern="MM" value="${startWeekDate}" />;
	var year = <fmt:formatDate pattern="yyyy" value="${startWeekDate}" />;
	var startDate = new Date(year, month - 1, day);
	day = <fmt:formatDate pattern="dd" value="${endWeekDate}" />;
	month = <fmt:formatDate pattern="MM" value="${endWeekDate}" />;
	year = <fmt:formatDate pattern="yyyy" value="${endWeekDate}" />;
	var endDate = new Date(year, month - 1, day);

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
var datepicker = $('#datepickerTS').datepicker({
	startDate: startDate,
	// Передаем функцию, которая добавляет 11 числу каждого месяца класс 'my-class'
    // и делает их невозможными к выбору.
    onRenderCell: function(date, cellType) {
        if (cellType == 'day' && dates.inRange(date, startDate, endDate)) {
            return {
                classes: '-currentWeek-'
            }
        }
    }
	}).data('datepicker');
//	datepicker.selectDate(startDate);
//	var stDate = new Date(2017, 3, 5);
//	datepicker.date = stDate;
</script>
