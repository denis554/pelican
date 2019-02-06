
<%@ page import="io.pelican.State" %>
<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Account Info"))}




<h1>Reset Successful</h1>

<p>Successfully reset password.  Please login with new password to continue</p>



${raw(applicationService.getFooter())}

