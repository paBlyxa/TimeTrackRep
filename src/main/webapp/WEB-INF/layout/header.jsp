<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<head>
	<style type="text/css">
		.logo {
			float: left;
			margin: 10px;
		}
		.mailto {
			float: right;
			margin-top: 2px;
		}
		.mailto a {
			box-sizing: border-box;
    		text-decoration: none;
    		display: block;
    		background-color: #fafafa;
    		color: #444;
    		line-height: 50px;
			padding: 0 15px;
    		border: 1px solid transparent;
    		border-left-color: rgba(0,0,0,.1);
    		border-right-color: rgba(0,0,0,.1);
    		border-top-color: rgba(0,0,0,.1);
		}
	</style>
</head>
<div>
	<div class="logo">
		<a href="<s:url value="/" />"> <img src="<s:url value="/resources/" />images/logo.png" border="0" />
		</a>
	</div>
	<div class="mailto">
		<a href="mailto:p.fakadey@west-e.ru?subject=Ошибка в программе учета времени&body=Найдена ошибка в программе учета времени:">
			Сообщить об ошибке
		</a>
	</div>
</div>