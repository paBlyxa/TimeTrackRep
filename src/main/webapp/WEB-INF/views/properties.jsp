<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<head>
	<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script src="<c:url value="/resources/script/myFunctions.js" />"></script>
<script src="<c:url value="/resources/script/properties.js" />"></script>
<script src="<c:url value="/resources/script/myNav.js" />"></script>
<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">

.inline {
  display: inline-block;
  vertical-align: middle;
}
.divContainer {
	background-color: #A9A9A9;height: 45px;width: 400px;
  vertical-align: middle;
}
.divContainer div {padding: 10px 10px;}
#statPeriod {font-family: sans-serif;font-size: 12pt;}
.subSection {margin-bottom:30px;}
.SumoSelect, select {min-width: 220px;}
</style>
</head>

<div class="menuHelp-wrap" id="menuHelp-wrap">
	
</div>


<div class="container">
	<article id="roles" class="help-article" title="Роли">
		<div class="propertyContainer">
			<h2>Роли</h2>
			<div class="subSection" id="sub-section-1" title="Новая роль">
				<h3>Новая роль</h3>
				<div class="divContainer">
					<div>
						<span class="inline">
							<label>Имя: </label>
						</span>
						<span class="inline">
							<input id="addRole" type="text" data-position="bottom left"	required="required" />
						</span>
						<span>
							<button id="buttonAddRole">Добавить</button>
						</span>
					</div>						
				</div>
			</div>
			<div class="subSection" id="sub-section-2" title="Удалить роль">
				<h3>Удалить роль</h3>
				<div>
					 <span class="inline">
							<select class="selectRoles" multiple="multiple"></select>
					</span>
					<span class="inline">	
							<button id="buttonDeleteRole" class="inline">Удалить</button>
					</span>
				</div>
			</div>
			<div class="subSection" id="sub-section-3" title="Назначить привилегии">
				<h3>Назначить привелегии</h3>
				<div>
					<span class="inline">
						<select class="selectRoles2"></select>
					</span>
				</div>
				<div style="margin-top: 10px;">
					<span class="inline">
						<select class="selectPrivileges1" multiple="multiple"></select>
					</span>
				</div>
				<div style="margin-top: 10px;">
					<button id="buttonSave">Назначить</button>
				</div>
			</div>
		</div>
	</article>
	<article id="privileges" class="help-article" title="Привилегии">
		<div class="propertyContainer">
			<h2>Привилегии</h2>
			<div class="subSection" id="sub-section-4" title="Новая привилегия">
				<h3>Новая привилегия</h3>
				<div class="divContainer">
					<div>
						<span class="inline">
							<label>Имя: </label>
						</span>
						<span class="inline">
							<input id="addPrivilege" type="text" data-position="bottom left"
										required="required" />
						</span>
						<span>
							<button id="buttonAddPrivilege">Добавить</button>
						</span>
					</div>						
				</div>
			</div>
			<div class="subSection" id="sub-section-5" title="Удалить привилегию">
				<h3>Удалить привилегию</h3>
				<div>
					<span class="inline">
						<select class="selectPrivileges2" multiple="multiple">
						</select>
					</span>
					<span class="inline">	
						<button id="buttonDeletePrivilege" class="inline">Удалить</button>
					</span>
				</div>
			</div>
		</div>
	</article>
	<article id="other" class="help-article" title="Разное">
		<div class="propertyContainer">
			<h2>Разное</h2>
		</div>
	</article>
</div>

<script type="text/javascript">

var roles;
var privileges;


	$(document).ready(function() {
		
		navMenu.create($("#menuHelp-wrap")[0]);
	
		$(".selectRoles").SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			csvDispCount: 3,
			searchText: 'Введите название...'
		});
	
		$(".selectRoles2").SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			csvDispCount: 3,
			searchText: 'Введите название...'
		});
	
		$(".selectPrivileges1").SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			csvDispCount: 3,
			searchText: 'Введите название...'
		});
	
		$(".selectPrivileges2").SumoSelect({
			placeholder: 'Выберите из списка',
			search: true,
			csvDispCount: 3,
			searchText: 'Введите название...'
		});

		// Запрос ролей
		$.getJSON("properties/getRoles.do", {},
		// Разбор полученных данных
		function(data) {
			roles = data;
			for (var i = 0; i < data.length; i++) {
				$(".selectRoles")[0].sumo.add(data[i]);
				$(".selectRoles2")[0].sumo.add(data[i]);
			}
		});

		// Запрос привилегий
		$.getJSON("properties/getPrivileges.do", {},
		// Разбор полученных данных
		function(data) {
			privileges = data;
			for (var i = 0; i < data.length; i++) {
				$(".selectPrivileges1")[0].sumo.add(data[i]);
				$(".selectPrivileges2")[0].sumo.add(data[i]);
			}
		});
		
		// Добавление новой привилегии
		$('#buttonAddPrivilege').on('click', function() {
			var privilege = $('#addPrivilege').val();
			// Проверка, что ввели данные
			if (privilege.length > 0){
				// Проверка не сущесвует ли уже
				for (var i = 0; i < privileges.length; i++) {
					if (privilege === privileges[i]){
						alert("Привелегия с именем '" + privilege + "' уже существует!")
						return;
					}
				}
				ajaxPost("properties/addPrivilege", "POST", privilege, function(){
					$(".selectPrivileges1")[0].sumo.add(privilege);
					$(".selectPrivileges2")[0].sumo.add(privilege);
					privileges.push(privilege);
					$('#addPrivilege').val("");
				});
			}
		});
		
		// Удаление привилегии
		$('#buttonDeletePrivilege').on('click', function() {
			var privilegeToDelete = [];
			$(".selectPrivileges2 option:selected").each(function(element){
				privilegeToDelete.push($(this).val());
			});
			if (privilegeToDelete.length > 0){
				ajaxPost("properties/deletePrivilege", "POST", JSON.stringify(privilegeToDelete), function(data){
					privileges = data;
					var selected = [];
					$(".selectPrivileges1 option:selected").each(function(element){
						selected.push($(this).val());
					});
					$(".selectPrivileges1").empty();
					$(".selectPrivileges1")[0].sumo.reload();
					$(".selectPrivileges2").empty();
					$(".selectPrivileges2")[0].sumo.reload();
					for (var i = 0; i < data.length; i++) {
						$(".selectPrivileges1")[0].sumo.add(data[i]);
						$(".selectPrivileges2")[0].sumo.add(data[i]);
					}					
					for (var i = 0; i < selected.length; i++) {
						$(".selectPrivileges1")[0].sumo.selectItem(selected[i]);
					}
				});
			}
		});
		
		// Добавление новой роли
		$('#buttonAddRole').on('click', function() {
			var role = $('#addRole').val();
			// Проверка, что ввели данные
			if (role.length > 0){
				// Проверка не сущесвует ли уже
				for (var i = 0; i < roles.length; i++) {
					if (role === roles[i]){
						alert("Привелегия с именем '" + role + "' уже существует!")
						return;
					}
				}
				ajaxPost("properties/addRole", "POST", role, function(){
					$(".selectRoles")[0].sumo.add(role);
					$(".selectRoles2")[0].sumo.add(role);
					roles.push(role);
					$('#addRole').val("");
				});
			}
		});
		
		// Удаление роли
		$('#buttonDeleteRole').on('click', function() {
			var roleToDelete = [];
			$(".selectRoles option:selected").each(function(element){
				roleToDelete.push($(this).val());
			});
			if (roleToDelete.length > 0){
				ajaxPost("properties/deleteRole", "POST", JSON.stringify(roleToDelete), function(data){
					roles = data;
					$(".selectRoles").empty();
					$(".selectRoles")[0].sumo.reload();
					$(".selectRoles2").empty();
					$(".selectRoles2")[0].sumo.reload();
					for (var i = 0; i < data.length; i++) {
						$(".selectRoles")[0].sumo.add(data[i]);
						$(".selectRoles2")[0].sumo.add(data[i]);
					}					
				});
			}
		});
		
		// Назначение привилегий
		$(".selectRoles2")[0].onchange = function() {
			var role = $(".selectRoles2 option:selected").val();
			ajaxPost("properties/getRolePrivileges", "POST", role, function(data){
				$(".selectPrivileges1")[0].sumo.unSelectAll();
				for (var i = 0; i < data.length; i++) {
					$(".selectPrivileges1")[0].sumo.selectItem(data[i]);
				}
			});
		}
		
		$('#buttonSave').on('click', function() {
			var role = $(".selectRoles2 option:selected").val();
			if ((role != null) && (role.length > 0)){
				var privileges = [];
				$(".selectPrivileges1 option:selected").each(function(element){
					privileges.push($(this).val());
				});
				var data = {};
				data.role = role;
				data.privileges = privileges;
				ajaxPost("properties/saveRolePrivileges", "POST", JSON.stringify(data), function(msg){
					console.log(msg);
					
				});
			}
		});
	});
</script>