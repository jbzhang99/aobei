package com.aobei.cas;

import java.security.GeneralSecurityException;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.aobei.cas.applicationevent.LoginEvent;

public class AobeiAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler
		implements InitializingBean {

	@NotNull
	private String fieldUser;

	@NotNull
	private String fieldPassword;

	@NotNull
	private String tableUsers;

	private String sql;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
			throws GeneralSecurityException, PreventedException {
		final String username = credential.getUsername();
		final String password;
		
		try {
			password = getJdbcTemplate().queryForObject(this.sql, String.class, username);
		} catch (final DataAccessException e) {
			throw new PreventedException("SQL exception while executing query for " + username, e);
		}
		if (password == null) {
			throw new FailedLoginException(username + " not found with SQL query.");
		} else if (passwordEncoder.matches(credential.getPassword(), password)) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
			//发布Login事件
			LoginEvent loginEvent = new LoginEvent(this, username, request.getRemoteAddr(), request.getHeader("X-Real-IP"), request.getHeader("User-Agent"), request.getParameter("Duuid"));
			applicationContext.publishEvent(loginEvent);
			return createHandlerResult(credential, this.principalFactory.createPrincipal(username), null);
		} else {
			throw new PreventedException("passwordError ", new RuntimeException());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.sql = String.format("select %s from %s where %s=?", this.fieldPassword, this.tableUsers, this.fieldUser);
	}

	/**
	 * @param fieldPassword
	 *            The fieldPassword to set.
	 */
	public final void setFieldPassword(final String fieldPassword) {
		this.fieldPassword = fieldPassword;
	}

	/**
	 * @param fieldUser
	 *            The fieldUser to set.
	 */
	public final void setFieldUser(final String fieldUser) {
		this.fieldUser = fieldUser;
	}

	/**
	 * @param tableUsers
	 *            The tableUsers to set.
	 */
	public final void setTableUsers(final String tableUsers) {
		this.tableUsers = tableUsers;
	}

}
