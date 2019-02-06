
<%@ page import="io.pelican.Transaction" %>
<%@ page import="io.pelican.ApplicationService" %>
<%@ page import="io.pelican.common.OrderStatus" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'transaction.label', default: 'Transaction')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
	
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" style="margin-top:20px;">${flash.message}</div>
	</g:if>
	
	
	<div style="background:#efefef; border:solid 1px #ddd; padding:30px; margin-top:20px;width:400px;">
		<h2>Confirm Purchase Shipping Label</h2>
		<p>Continuing will purchase a shipping label.  You must have an active account with 
		EasyPost or Shippo (not yet configured) with payments enabled/configured.  
		The current charge per label is <strong>$0.05 USD</strong>.</p>
		<br/>
		
		<g:link controller="transaction" action="show" id="${transactionInstance.id}" class="btn btn-default">Cancel</g:link>	
		&nbsp;
		<g:link controller="transaction" 
			action="purchase_shipping_label" 
			id="${transactionInstance.id}"
			class="btn btn-primary">Purchase Label</g:link>
	</div>
	
</body>
</html>
