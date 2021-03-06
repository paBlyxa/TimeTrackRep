<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<head>
<script src="<c:url value="/resources/script/jquery-3.0.0.min.js" />"></script>
</head>
<script>
	$(window).scroll(function() {
		if ($(this).scrollTop() > 60) {
			$("#mainMenu").addClass("fixed");
		} else {
			$("#mainMenu").removeClass("fixed");
		}
	});
</script>


<c:set var="currentUrl"
	value="${requestScope['javax.servlet.forward.request_uri']}"></c:set>
<ul class="menu" id="mainMenu">
	<li><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'timesheet')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/timesheet" />">Учет</a></li>

	<li class="dropdown"><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'projects')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/projects" />">Проекты</a> <sec:authorize
			access="hasAuthority('modify')">
			<div id="dropdown-content">
				<a href="<c:url value="/projects/new" />">Новый проект</a>
				<%-- 					<a href="<c:url value="/projects" />">Мои проекты</a> --%>
			</div>
		</sec:authorize></li>
	<li><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'tasks')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/tasks" />">Задачи</a></li>
	<li><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'employees') and not fn:contains(currentUrl, 'account')  and not fn:contains(currentUrl, 'stat')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/employees" />">Сотрудники</a></li>
	<li class="dropdown"><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'stat')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/employees/mystat" />">Статистика</a>
			<div id="dropdown-content">
				<a href="<c:url value="/stat" />">Общая статистика</a>
			</div>
	</li>
	<li class="dropdown"><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'calendar')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/calendar" />">Календарь</a>
		<div id="dropdown-content">
			<a href="https://west-e.bitrix24.ru/timeman">Отпусков</a>
		</div>
	</li>
	<li><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'help')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/help" />">Помощь</a></li>
	<sec:authorize access="hasAuthority('administration')">
		<li class="dropdown"><a
			<c:choose>
				<c:when test="${fn:contains(currentUrl, 'properties')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
			href="<c:url value="/properties" />">Настройки</a>
			<div id="dropdown-content">
			<a href="<c:url value="/properties/users" />">Пользователи</a>
			</div>
		</li>
	</sec:authorize>
	<li id="currentUser"><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'account')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/employees/account" />"> <sec:authentication
				property="principal.username" />
	</a></li>
</ul>