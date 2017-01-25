<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div>
	<h2>Войдите в систему</h2>

	<s:url var="authUrl" value="/static/j_spring_security_check" />
	<!--<co id="co_securityCheckPath"/>-->
	<form method="post" class="signin" action="${authUrl}">

		<fieldset>
			<table cellspacing="0">
				<tr>
					<th><label for="username_or_email">Username or Email</label></th>
					<td><input id="username_or_email" name="j_username"
						type="text" /> <!--<co id="co_usernameField"/>--></td>
				</tr>
				<tr>
					<th><label for="password">Password</label></th>
					<td><input id="password" name="j_password" type="password" />
						<!--<co id="co_passwordField"/>--> <small><a
							href="/account/resend_password">Forgot?</a></small></td>
				</tr>
				<tr>
					<th></th>
					<td><input id="remember_me"
						name="_spring_security_remember_me" type="checkbox" /> <!--<co id="co_rememberMe"/>-->
						<label for="remember_me" class="inline">Remember me</label></td>
				</tr>
				<tr>
					<th></th>
					<td><input name="commit" type="submit" value="Sign In" /></td>
				</tr>
			</table>
		</fieldset>
	</form>

	<script type="text/javascript">
    document.getElementById('username_or_email').focus();
   </script>
</div>