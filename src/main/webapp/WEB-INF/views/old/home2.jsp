<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ page session="false" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<h1>Учет рабочего времени</h1>
<div>
	<h2>Войдите в систему</h2>

	<s:url var="authUrl" value="/static/j_spring_security_check" />
	<!--<co id="co_securityCheckPath"/>-->
	<form method="post" class="signin" action="${authUrl}">

		<fieldset>
			<table cellspacing="0">
				<tr>
					<th><label for="username_or_email">Имя пользователя</label></th>
					<td><input id="username_or_email" name="j_username"
						type="text" /> <!--<co id="co_usernameField"/>--></td>
				</tr>
				<tr>
					<th><label for="password">Пароль</label></th>
					<td><input id="password" name="j_password" type="password" />
						<!--<co id="co_passwordField"/>--> <small><a
							href="/account/resend_password">Забыли?</a></small></td>
				</tr>
				<tr>
					<th></th>
					<td><input id="remember_me"
						name="_spring_security_remember_me" type="checkbox" /> <!--<co id="co_rememberMe"/>-->
						<label for="remember_me" class="inline">Запомнить меня</label></td>
				</tr>
				<tr>
					<th></th>
					<td><input name="commit" type="submit" value="Войти" /></td>
				</tr>
			</table>
		</fieldset>
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>

	<script type="text/javascript">
    document.getElementById('username_or_email').focus();
   </script>
</div>