
<%@ page import="io.pelican.Transaction" %>
<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('io.pelican.CurrencyService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="completed.orders"/></title>
	</head>
	<body>


		<div id="list-transaction" class="content scaffold-list" role="main">

			<h2><g:message code="completed.orders"/></h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<g:if test="${transactionInstanceList}">
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="id" title="${message(code: 'transaction.id', default: 'Order #')}" />
										
						<g:sortableColumn property="orderDate" title="${message(code: 'order.date', default: 'Order Date')}" />
						
						<th><g:message code="account" default="Account" /></th>
						
						<g:sortableColumn property="total" title="${message(code: 'total', default: 'Total')}" />
						
						<g:sortableColumn property="taxes" title="${message(code: 'taxes', default: 'Taxes')}" />
						
						<g:sortableColumn property="shipping" title="${message(code: 'shipping', default: 'Shipping')}" />
						
						<g:sortableColumn property="status" title="${message(code: 'status', default: 'Status')}" />
						
						<th></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${transactionInstanceList}" status="i" var="transactionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${transactionInstance.id}">${transactionInstance.id}</g:link></td>
					
						<td><g:link action="show" id="${transactionInstance.id}"><g:formatDate date="${transactionInstance.orderDate}" format="dd MMM yyyy hh:mm a" /></g:link></td>
						
						<td>${transactionInstance.account.username}</td>
				
						<td style="font-weight:bold;">${currencyService.format(applicationService.formatPrice(transactionInstance.total))}</td>
				
						<td>${currencyService.format(applicationService.formatPrice(transactionInstance.taxes))}</td>
					
						<td>${currencyService.format(applicationService.formatPrice(transactionInstance.shipping))}</td>			
						<td>${transactionInstance.status}</td>
						
						
						<td><g:link action="show" params="[id: transactionInstance.id]" class="show-transaction-${transactionInstance.id}"><g:message code="show"/></g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${transactionInstanceTotal}" />
			</div>
			
			</g:if>
			<g:else>
				<p>No orders found...</p>
			</g:else>
		</div>
	</body>
</html>
