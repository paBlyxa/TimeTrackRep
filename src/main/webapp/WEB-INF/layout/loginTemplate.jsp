<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>TimeX</title>
<link rel="stylesheet" type="text/css"
	href="<s:url value="/resources/style.css" />">
<link rel="stylesheet" type="text/css"
	href="<s:url value="/resources/bootstrap.css" />">
<link rel="stylesheet"
	href="<s:url value="/resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css" />">
</head>

<body id="loginTemp">
	<div id="content">
		<t:insertAttribute name="body" />
	</div>
</body>

</html>