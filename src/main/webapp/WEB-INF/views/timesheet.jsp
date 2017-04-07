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
								class="datepicker-here input" id="datepickerTS"
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
					pattern="yyyy-MM-dd" var="parsedDate" type="date" />
				<fmt:formatDate pattern="w" value="${parsedDate}" />
				неделя
				<fmt:formatDate pattern="y" value="${parsedDate}" />
				года:
				<fmt:formatDate pattern="dd MMMM" value="${parsedDate}" />
				-
				<fmt:parseDate value="${timesheetsByDays[6].date}"
					pattern="yyyy-MM-dd" var="parsedDate" type="date" />
				<fmt:formatDate value="${parsedDate}" pattern="dd MMMM" />
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
</script>
