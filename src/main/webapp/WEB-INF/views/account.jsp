<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<head>
	<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
	<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/script/myFunctions.js" />"></script>
</head>

<div class="divWithBorder" style="max-width: 600px;overflow:visible;">
	<h1>Данные сотрудника</h1>
	<table class="order-table">
		<thead>
		</thead>
		<tbody>
			<tr>
				<td>Имя пользователя</td>
				<td><c:out value="${employee.username}" /></td>
			</tr>
			<tr>
				<td>Имя</td>
				<td><c:out value="${employee.name}" /></td>
			</tr>
			<tr>
				<td>Фамилия</td>
				<td><c:out value="${employee.surname}" /></td>
			</tr>
			<tr>
				<td>Адрес почты</td>
				<td><c:out value="${employee.mail}" /></td>
			</tr>
			<tr>
				<td>Должность</td>
				<td><c:out value="${employee.post}" /></td>
			</tr>
			<tr>
				<td>Отдел</td>
				<td><c:out value="${employee.department.name}" /></td>
			</tr>
			<tr>
				<td>Права</td>
				<td><sec:authentication property="principal.authorities"/></td>
			</tr>
			<tr>
				<td>Напоминания</td>
				<td>
					<select class="selectRemember">
						<c:forEach var="rem" items="${rememberList}">
							<option value="${rem}"
							<c:if test="${rem == remember}"> selected</c:if>
							>${rem.text}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Автоэкспорт квартала</td>
				<td><input id="autoSave" type="checkbox" name="autoSave" value="true"
					<c:if test="${autoSave}"> checked </c:if>
				title="Автоэкспорт выполнится в первый понедельник следующего квартала"><br></td>
			</tr>
		</tbody>
	</table>
</div>

<div id="saveContainer" >
		<form action="download" method="GET">
			<div id="saveInnerContainer">
				<label>Отчет: </label>
				<input name="id" value="${employee.employeeId}" type="hidden" />
				<input type="text"
					class="datepicker-here" data-position="bottom left"
					data-range="true" data-multiple-dates-separator=" - "
					name="period" required="required" />
				<input type="submit" value="сохранить" />
			</div>
		</form>
</div>

<script>
$(document).ready(function() {

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var remember =  "<c:out value="${remember}"/>";

	$(".selectRemember").SumoSelect({
		placeholder: 'Выберите из списка',
		search: true,
		csvDispCount: 3,
		searchText: 'Введите название...'
	});
	
	// При изменении напоминания
	$(".selectRemember")[0].onchange = function() {
		
		var rem = $(".selectRemember option:selected").val();
		
		if (remember != rem) {
			ajaxPost("saveProperty", "POST", JSON.stringify({
				name: "remember",
				value: rem
			}),
					function(responseJsonObject){
		    	 console.log("Success: " + responseJsonObject);
			});
			
		}
	};
	
	// При изменении автосохранения
	$("#autoSave").on("click", function(){
		var autoSave = this.checked;
		ajaxPost("saveProperty", "POST", JSON.stringify({
			name: "autoSave",
			value: autoSave
		}),
				function(responseJsonObject){
	    	 console.log("Success: " + responseJsonObject);
		});			
	});
});
</script>