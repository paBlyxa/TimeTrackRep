<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
	<script src="<c:url value="/resources/script/jquery-3.0.0.min.js" />"></script>
</head>
<script>
  $(window).scroll(function(){
      if ($(this).scrollTop() > 60) {
          $(".menu").addClass("fixed");
      } else {
          $(".menu").removeClass("fixed");
      }
  });
</script>


<c:set var = "currentUrl" value ="${requestScope['javax.servlet.forward.request_uri']}"></c:set>
<ul class="menu">
		<li><a
			<c:choose>
				<c:when test="${fn:contains(currentUrl, 'timesheet')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>		
			href="<c:url value="/timesheet" />">Учет</a>
		</li>
		<li><a
			<c:choose>
				<c:when test="${fn:contains(currentUrl, 'projects')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
			href="<c:url value="/projects" />">Проекты</a>
		</li>
		<li><a 
	 		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'tasks')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
	 		href="<c:url value="/tasks" />">Задачи</a>
	 	</li>
		<li><a 
	 		<c:choose>
				<c:when test="${fn:contains(currentUrl, 'employees')}">
					class="activeMenuItem"
				</c:when>
				<c:otherwise>
					class="menuItem" 
				</c:otherwise>
			</c:choose>
	 		href="<c:url value="/employees" />">Сотрудники</a>
	 	</li>
</ul>