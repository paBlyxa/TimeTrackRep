<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
<script src="<c:url value="/resources/script/myFunctions.js" />"></script>
<script src="<c:url value="/resources/script/summaryStat.js" />"></script>
<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
<link href="<c:url value="/resources/datepicker.min.css"/>"
	rel="stylesheet" type="text/css">
<link href="<c:url value="/resources/summaryStyle.css"/>"
	rel="stylesheet" type="text/css">
<style type="text/css">
html, body {
	height: 100%;
	padding: 0px;
	margin: 0px;
	overflow: hidden;
}

.pageBody {
	padding: 0px;
	margin: 0px;
}
</style>
</head>

<div id="paramsContainer">
	<div id="filterContainer">
		<div>
			<div id="filterInnerContainer">
				<label>Период: </label> <input id="statPeriod"
					class="datepicker-here" type="text" data-position="bottom left"
					data-range="true" data-multiple-dates-separator=" - "
					name="statPeriod" value="${statPeriod}" required="required" />
				<button id="buttonRefresh">Обновить</button>
			</div>
		</div>
	</div>

	<div id="saveStatistic">
		<form action="stat/xls" method="GET">
			<input name="statPeriod" id="formValue" value="${statPeriod}" type="hidden" /> <input
				name="summaryData" value="${summaryData}" type="hidden" /> <input
				type="submit" value="Сохранить статистику" />
		</form>
	</div>
</div>

<div id="summary_here">

</div>

<script>
	$(document).ready(function() {
		getStatistic($("#summary_here")[0]);
		$("#buttonRefresh").click(function() {
			$("#formValue").val($("#statPeriod").val());
			getStatistic($("#summary_here")[0]);
		});
	});
</script>