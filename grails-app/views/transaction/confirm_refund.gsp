
<%@ page import="io.pelican.Transaction" %>
<%@ page import="io.pelican.ApplicationService" %>
<%@ page import="io.pelican.common.OrderStatus" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('io.pelican.CurrencyService').newInstance()%>

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
	
	
	<g:form controller="transaction" action="refund" id="${transactionInstance.id}">
		<div style="background:#efefef; border:solid 1px #ddd; padding:30px; margin-top:20px;width:400px;">
			<h2>Confirm Refund</h2>
			<p>Refunding the Order will refund the full amount of :
			<br> <strong>${currencyService.format(applicationService.formatPrice(transactionInstance.total))}</strong> to
				<strong>account : ${transactionInstance.account.username}</strong>'s credit card. The card was charged
				<%if(transactionInstance.gateway == "Braintree"){%>
					on Braintree
				<%}else{%>
					on Stripe
				<%}%>
				</p>
			
			<br/>
			
			<g:link controller="transaction" action="show" id="${transactionInstance.id}" class="btn btn-default">Cancel</g:link>	
			&nbsp;
			<input type="submit" value="Proceed with Refund" class="btn btn-primary"/>
		</div>
	
	</g:form>
	
</body>
</html>
