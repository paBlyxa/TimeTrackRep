<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<s:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
	
	<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
	<link href="<s:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
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
					<td><form:input type="text" required="true"
							class="datepicker-here input" id="datepickerTS"
							data-position="bottom left" data-multiple-dates="true"
							data-multiple-dates-separator=", " path="dates" /></td>
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
					<td><form:input class="input" type="text" path="countTime"
							list="listTimes" required="true" id="inputCountTime" /> <datalist
							id="listTimes">
							<option value="0.5" />
							<option value="1.0" />
							<option value="2.0" />
							<option value="4.0" />
							<option value="8.0" />
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
	<c:url var="modifyUrl" value="/timesheet/modify" />
	<h1>Недавние записи</h1>
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
				<tr class="${classRow}">
					<c:choose>
						<c:when test="${fn:length(timesheetByDay.timesheets) gt 0}">
						
							<td rowspan="${fn:length(timesheetByDay.timesheets)}"><fmt:parseDate
								value="${timesheetByDay.date}" pattern="yyyy-MM-dd"
								var="parsedDate" type="date" /> <fmt:formatDate pattern="EEEE"
								value="${parsedDate}" /> <br /> <fmt:formatDate
								pattern="dd-MM-yyyy" value="${parsedDate}" /></td>

							<c:forEach var="timesheet" items="${timesheetByDay.timesheets}" varStatus="status">
								<c:if test="${status.index > 0}">
									<tr class="${classRow}">
								</c:if>
								<td><c:out value="${timesheet.project.name}" /></td>
								<td><c:out value="${timesheet.task.name}" /></td>
								<td>
									<form action="${modifyUrl}" method="POST">
										<input name="week" type="hidden" value="${param.week}"/>
										<input name="timesheetId" type="hidden" value="${timesheet.id}" />
										<input name="countTime" class="input" type="text" value="${timesheet.countTime}" />
										<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									</form>
								</td>
								<c:if test="${status.index == 0}">
									<td rowspan="${fn:length(timesheetByDay.timesheets)}"><c:out
											value="${timesheetByDay.hours}" /></td>
								</c:if>
								<td><c:out value="${timesheet.comment}" /></td>
								<td id="colLast">
									<form action="${deleteUrl}" method="POST">
										<input name="week" type="hidden" value="${param.week}"/>
										<input name="timesheetId" type="hidden" value="${timesheet.id}" />
										<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" />
										<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									</form>
								</td>
								<c:if test="${status.index > 0}">
									</tr>
								</c:if>								
							</c:forEach>
						</c:when>
						<c:otherwise>
							<td><fmt:parseDate
								value="${timesheetByDay.date}" pattern="yyyy-MM-dd"
								var="parsedDate" type="date" /> <fmt:formatDate pattern="EEEE"
								value="${parsedDate}" /> <br /> <fmt:formatDate
								pattern="dd-MM-yyyy" value="${parsedDate}" /></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td style="text-align: right;" colspan="4">Общее
					количество часов:</td>
				<td colspan="2"><c:out value="${countTime}" /></td>
			</tr>
		</tfoot>
	</table>

	<div id="moreUrl">
		<s:url value="/timesheet?week=${param.week + 1}" var="more_url" />
		<a href="${more_url}">Предыдующая неделя</a>
		<s:url value="/timesheet?week=${param.week - 1}" var="less_url" />
		<a style="float: right;" href="${less_url}">Следующая
			неделя</a>
	</div>
</div>

<script type="text/javascript">
	function validate() {
		if (isNaN(document.getElementById("inputCountTime").value)) {
			document.getElementById("errorValidate").innerHTML="Введите числовое значение в поле часы";
			return false;
		} else {
			if (document.getElementById("inputCountTime").value <= 0) {
				document.getElementById("errorValidate").innerHTML="Введите положительное и ненулевое значение в поле часы";
				return false;
			}
		}
		return true;
	};
	$(document).ready(function() {
		$('#selectProject').SumoSelect({
			placeholder: 'Выберите проект из списка',
			search: true,
			searchText: 'Поиск по названию проекта'
			});
	});
	$(document).ready(function() {
		$('#selectTask').SumoSelect({
			placeholder: 'Выберите задачу из списка',
			search: true,
			searchText: 'Поиск по названию задачи'
			});
	});
</script>
