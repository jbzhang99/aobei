<!DOCTYPE html>

<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">
<head>
  <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
  
    <title>鳌背科技 登录中心</title>
  
    <link href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
  	<link rel="stylesheet" href="<c:url value="/css/css_aobei.css" />" />
    <link rel="stylesheet" href="<c:url value="${customCssFile}" />" />
    <link rel="icon" href="<c:url value="/favicon.ico" />" type="image/x-icon" />
  	<link rel="stylesheet" href="<c:url value="/css/aobei.css?v=3" />" />
  <!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.1/html5shiv.js" type="text/javascript"></script>
  <![endif]-->
</head>
<body id="cas" class="login">
  <div id="container">
      <!-- BEGIN LOGO -->
	  <div class="logo">
			<img src="<c:url value="/css/img/logo-white.png"/>" alt="" style="width: 140px;height: 30px;"/>
			<!-- <a href="index.html">
			</a>-->	  
	  </div>
	  <!-- END LOGO -->

      <div id="content" class="content">
      
      

<div id="cookiesDisabled" class="errors" style="display:none;">
    <h2><spring:message code="screen.cookies.disabled.title" /></h2>
    <p><spring:message code="screen.cookies.disabled.message" /></p>
</div>


<c:if test="${not empty registeredService}">
    <c:set var="registeredServiceLogo" value="images/webapp.png"/>
    <c:set var="registeredServiceName" value="${registeredService.name}"/>
    <c:set var="registeredServiceDescription" value="${registeredService.description}"/>

    <c:choose>
        <c:when test="${not empty mduiContext}">
            <c:if test="${not empty mduiContext.logoUrl}">
                <c:set var="registeredServiceLogo" value="${mduiContext.logoUrl}"/>
            </c:if>
            <c:set var="registeredServiceName" value="${mduiContext.displayName}"/>
            <c:set var="registeredServiceDescription" value="${mduiContext.description}"/>
        </c:when>
        <c:when test="${not empty registeredService.logo}">
            <c:set var="registeredServiceLogo" value="${registeredService.logo}"/>
        </c:when>
    </c:choose>
    <p/>
</c:if>

<div class="box" id="login">
    <form:form method="post" cssClass="login-form" id="fm1" commandName="${commandName}" htmlEscape="true">

        <form:errors id="msg" cssClass="alert alert-danger display-hide" element="div" htmlEscape="false" />
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9"><spring:message var="netid" code="screen.welcome.label.netid" /></label>
			<div class="input-icon">
				<i class="fa fa-user"></i>
				<c:choose>
                <c:when test="${not empty sessionScope.openIdLocalId}">
                    <strong><c:out value="${sessionScope.openIdLocalId}" /></strong>
                    <input type="hidden" id="username" name="username" value="<c:out value="${sessionScope.openIdLocalId}" />" />
                </c:when>
                <c:otherwise>
                    <spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
                    <form:input placeholder="用户名" cssClass="required form-control placeholder-no-fix" id="username" size="25" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="off" htmlEscape="true" />
                	<form:errors path="username" cssClass="help-block" element="span" htmlEscape="false" />
                </c:otherwise>
            	</c:choose>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label visible-ie8 visible-ie9"><spring:message var="password" code="screen.welcome.label.password" /></label>
			<div class="input-icon">
				<i class="fa fa-lock"></i>
				<spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey" />
	            <form:password placeholder="密码" cssClass="required form-control placeholder-no-fix" id="password" size="25" tabindex="2" path="password"  accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" />
	            <form:errors path="password" cssClass="help-block" element="span" htmlEscape="false" />
	            <span id="capslock-on" style="display:none;" class="help-block"><img src="images/warning.png" valign="top"> <spring:message code="screen.capslock.on" /></span>
			</div>
		</div>

        <!--
        <section class="row check">
            <p>
                <input id="warn" name="warn" value="true" tabindex="3" accesskey="<spring:message code="screen.welcome.label.warn.accesskey" />" type="checkbox" />
                <label for="warn"><spring:message code="screen.welcome.label.warn" /></label>
                <br/>
                <input id="publicWorkstation" name="publicWorkstation" value="false" tabindex="4" type="checkbox" />
                <label for="publicWorkstation"><spring:message code="screen.welcome.label.publicstation" /></label>
                <br/>
                <input type="checkbox" name="rememberMe" id="rememberMe" value="true" tabindex="5"  />
                <label for="rememberMe"><spring:message code="screen.rememberme.checkbox.title" /></label>
            </p>
        </section>
        -->
		<div class="form-actions">
            <input type="hidden" name="lt" value="${loginTicket}" />
            <input type="hidden" name="execution" value="${flowExecutionKey}" />
            <input type="hidden" name="Duuid" id="Duuid" />
            <input type="hidden" name="_eventId" value="submit" />
			<!-- 
			<label class="checkbox">
			<input type="checkbox" name="remember" value="1"/> Remember me </label> 
			-->
			<button class="btn blue pull-right" name="submit" accesskey="l" tabindex="6" type="submit">
			登录  <i class="fa fa-arrow-circle-o-right fa-lg" aria-hidden="true"></i>
			</button>
            <%-- <input class="btn btn-link" name="reset" accesskey="c" value="<spring:message code="screen.welcome.button.clear" />" tabindex="7" type="reset" /> --%>
		</div>
    </form:form>
</div>


</div> <!-- END #content -->

<footer>
    <div id="copyright">
        2018 &copy; 鳌背（北京）科技有限公司
    </div>
</footer>

</div> <!-- END #container -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/headjs/1.0.3/head.min.js"></script>
<spring:theme code="cas.javascript.file" var="casJavascriptFile" text="" />
<script type="text/javascript" src="<c:url value="${casJavascriptFile}" />?v=20180402"></script>
</body>
</html>
