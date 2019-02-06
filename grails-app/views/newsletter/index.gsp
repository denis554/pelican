<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()
%>

${raw(applicationService.getDefaultHeader("Newsletter Signup"))}

	<g:if test="${flash.message}">
		<div class="alert alert-info" style="margin-top:10px;">${raw(flash.message)}</div>
	</g:if>


${raw(applicationService.getDefaultFooter())}