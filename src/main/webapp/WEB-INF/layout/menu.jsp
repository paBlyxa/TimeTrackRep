<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<head>
<script src="<c:url value="/resources/script/jquery-3.0.0.min.js" />"></script>
</head>
<script>
  $(window).scroll(function(){
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
			access="hasAuthority('Операторы архива Projects')">
			<div id="dropdown-content">
				<a href="<c:url value="/projects/new" />">Новый
					проект</a>
				<%-- 					<a href="<c:url value="/projects" />">Мои проекты</a> --%>
			</div>
		</sec:authorize></li>
	<sec:authorize
		access="hasAuthority('Операторы архива Projects')">
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
	</sec:authorize>
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
	<li><a
		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'stat')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
		href="<c:url value="/employees/mystat" />">Статистика</a></li>
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