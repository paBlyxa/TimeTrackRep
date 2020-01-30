<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
	<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script src="<c:url value="/resources/script/myFunctions.js" />"></script>
<script src="<c:url value="/resources/script/properties.js" />"></script>
<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
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
.nav--section-title {
	font-size: 16px;
}
.tabMenu {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #dddddd;
}
.tabMenu li {
  float:left;
}
.tabMenu li a {
  display: block;
  text-align: center;
  padding: 14px 5px;
  text-decoration: none;
}
.tabMenu li a:hover {
  background-color: #777;color: white;
}
.activeItem {
	background-color: white;color: black;fontWeight: bold;
}
.classFired {
	display: none;
}
</style>
</head>

<div class="menuHelp-wrap" id="menuHelp-wrap">
	<script>
		$(window).scroll(function() {
			if ($(this).scrollTop() > 60) {
				$("#menuHelp-wrap").addClass("menuHelp-wrap-fixed");
			} else {
				$("#menuHelp-wrap").removeClass("menuHelp-wrap-fixed");
			}
		});
	</script>
	<div>
	<ul class="tabMenu">
		<li><a data-value="1">Все</a></li>
		<li><a data-value="2" class="activeItem">Текущие</a></li>
		<li><a data-value="3">Бывшие</a></li>
	</ul>
	</div>
	<nav class="menuHelp" id="menu">
	</nav>
</div>


<div class="container">
	<article id="intro" class="help-article">
		<div class="propertyContainer" style="height: 100%;">
			<h2>Роли</h2>
			<div id="roleContainer" style="max-width: 600px;">
				<select class="selectRoles" multiple="multiple">
				</select>
			</div>
			<h2>Напоминания</h2>
			<div id="rememberContainer" style="max-width: 200px;">
				<select class="selectRemember">
					<c:forEach var="remember" items="${rememberList}">
						<option value="${remember}">${remember.text}</option>
					</c:forEach>
				</select>
			</div>
			<h2>Сохранение</h2>
			<div style="max-width: 400px;">
				<input id="autoSave" type="checkbox" name="autoSave" value="true">Автоматический экспорт раз в квартал<br>
			</div>
		</div>
	</article>
</div>

<script type="text/javascript">

var employees;
var roles;
var employee;
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

	$(document).ready(function() {
	
		$(".propertyContainer")[0].style.height = window.innerHeight - 188 + "px";
		$(window).resize(function() {
			$(".propertyContainer")[0].style.height = window.innerHeight - 188 + "px";
		});
		// Очистка содержимого таблица
		$("#menu").empty();
		
		$(".selectRoles").SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			csvDispCount: 3,
			searchText: 'Введите название...',
			okCancelInMulti: true
		});
		
		$(".selectRemember").SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			csvDispCount: 3,
			searchText: 'Введите название...'
		});
		
		// Запрос сотрудников
		$.getJSON("getEmployees.do", {},
		// Разбор полученных данных
		function(data) {
			employees = data;
			for (var i = 0; i < data.length; i++) {
				var line = '<div class="nav--section navButton';
				if (!data[i].active) {
					line += ' classFired';
				} else {
					line += ' classActive';
				}
				line += '" rowNumber="' 
						+ i + '"><h2 class="nav--section-title"><a href="#">' + data[i].viewName + '</a></h2></div>';
				$("#menu").append(line);
			}
		});
		
		// Запрос ролей
		$.getJSON("getRoles.do", {},
		// Разбор полученных данных
		function(data) {
			roles = data;
			for (var i = 0; i < data.length; i++) {
				$(".selectRoles")[0].sumo.add(data[i]);
			}
		});
		
		// При выборе другого сотрудника
		$(document).on('click', 'div.navButton', function() {
			$(".choosen").removeClass("choosen");
			employee = employees[this.getAttribute("rowNumber")]; 
			$(".selectRoles")[0].sumo.unSelectAll();
			for (var i = 0; i < employee.roles.length; i++) {
				$(".selectRoles")[0].sumo.selectItem(employee.roles[i]);	
			}
			
			$(".selectRemember")[0].sumo.selectItem(employee.scheduleType);
			document.getElementById("autoSave").checked = employee.autoSave;		

			
			$(this.lastChild).addClass("choosen");
		});
		
		// При изменении ролей
		$('.btnOk').on('click', function() {
			var changes = false;
			var newRoles = [];
			var roleChecked = [];
			$(".selectRoles option:selected").each(function(element){
				newRoles.push($(this).val());
				roleChecked.push(false);
			});
			for (var i = 0; i < employee.roles.length; i++) {
				var find = false;
				for (var j = 0; j < newRoles.length; j++) {
					if (employee.roles[i] == newRoles[j]){
						roleChecked[j] = true;
						find = true;
						continue;
					}
				}
				if (!find) {
					changes = true;
					continue;
				}
			}
			if (!changes) {
				for (var j = 0; j < roleChecked.length; j++){
					if (!roleChecked[j]) {
						changes = true;
						continue;
					}
				}
			}
			// Есть изменения сохраняем
			if (changes) {
				console.log("Save roles for " + employee.viewName);
				$.ajaxSetup({beforeSend: function(xhr) {xhr.setRequestHeader('X-CSRF-TOKEN', token);}});
				$.ajax({ 
				    url:"saveRoles",
				    type:"POST", 
				    contentType: "application/json; charset=utf-8",
				    dataType : "json",
				    data: JSON.stringify({employeeId : employee.employeeId,
				    	viewName : employee.viewName, 
				    	roles : newRoles
				    }),
				    async: true,    // Cross-domain requests and dataType: "jsonp"
									// requests
									// do not support synchronous operation
				    cache: false,    // This will force requested pages not to be
										// cached
										// by the browser
				     processData:false, // To avoid making query String instead of JSON
				     success: function(responseJsonObject){
				        // Success Action
				    	 console.log("Success: " + responseJsonObject);
				    	 employee.roles = newRoles;
				    },
				    error: function(jqXhr, textStatus, errorMessage){
				        console.log("Error: ", errorMessage);
				     }
				});
			}
			return false;
		});

		// При изменении напоминания
		$(".selectRemember")[0].onchange = function() {
			var rem = $(".selectRemember option:selected").val();
			if (employee.scheduleType != rem) {
				ajaxPost("saveRemember", "POST", JSON.stringify({
					employeeId : employee.employeeId,
			    	scheduleType : rem}),
						function(responseJsonObject){
			    	 console.log("Success: " + responseJsonObject);
			    	 employee.scheduleType = rem;
				});

			}
		};
		
		// При изменении автосохранения
		$("#autoSave").on("click", function(){
			var autoSave = this.checked;
			ajaxPost("saveAutoSave", "POST", JSON.stringify({
				employeeId : employee.employeeId,
		    	autoSave : autoSave}),
					function(responseJsonObject){
		    	 console.log("Success: " + responseJsonObject);
		    	 employee.autoSave = autoSave;
			});			
		});
		
		$(document).on('click', '.tabMenu li a', function() {
			$(".tabMenu li a").removeClass();
			this.className = "activeItem";
			var value = $(this).attr("data-value");
			if (value == "1"){
				var links = document.getElementsByClassName("classFired");
				for (i = 0; i < links.length; i++) {
				    links[i].style.display = "block";
				}
				links = document.getElementsByClassName("classActive");
				for (i = 0; i < links.length; i++) {
				    links[i].style.display = "block";
				}
			}
			if (value == "2"){
				var links = document.getElementsByClassName("classFired");
				for (i = 0; i < links.length; i++) {
				    links[i].style.display = "none";
				}
				links = document.getElementsByClassName("classActive");
				for (i = 0; i < links.length; i++) {
				    links[i].style.display = "block";
				}
			}
			if (value == "3"){
				var links = document.getElementsByClassName("classActive");
				for (i = 0; i < links.length; i++) {
				    links[i].style.display = "none";
				}
				links = document.getElementsByClassName("classFired");
				for (i = 0; i < links.length; i++) {
				    links[i].style.display = "block";
				}
			}
		});
	});
</script>