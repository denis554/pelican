<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('io.pelican.CurrencyService').newInstance()%>

${raw(applicationService.getScreenHeader("Checkout Preview"))}

	<h2>Shopping Cart</h2>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	<g:if test="${shoppingCartInstance?.shoppingCartItems}">

		<table class="table table-bordered">
			<thead>
				<tr style="background:#efefef">
					<th style="text-align:center">ID</th>
					<th>Name</th>
					<th style="text-align:center">Price</th>
					<th style="text-align:center">Quantity</th>
					<th style="text-align:center">Extended Price</th>
					<th></th>
				</tr>
			</thead>		
			<tbody>
				<g:each in="${shoppingCartInstance?.shoppingCartItems}" status="i" var="item">
					<%
						def optionsTotal = 0
						if(item.shoppingCartItemOptions?.size() > 0){
							item.shoppingCartItemOptions.each(){ option ->
								optionsTotal += option.variant.price
							}	
						}
						def productTotal = item.product.price + optionsTotal
						if(item.product.salesPrice){
							productTotal = item.product.salesPrice + optionsTotal
						}
						def extendedPrice = productTotal * item.quantity
					%>
					<tr>
						<td style="text-align:center">${item.product.id}</td>
						<td><g:link controller="product" action="details" id="${item.product.id}">${item.product.name}</g:link>
							<br/>
							<g:if test="${item.shoppingCartItemOptions?.size() > 0}">
								<div style="font-size:11px; color:#777">
									<strong>options :&nbsp;</strong>
									<g:each in="${item.shoppingCartItemOptions}" var="option">
										<span class="option">${option.variant.name}
											(${currencyService.format(applicationService.formatPrice(option.variant.price))})
										</span>
										<br/>
									</g:each>
								</div>
							</g:if>
						</td>
						<td style="text-align:center">
						    <g:if test="${item.product.salesPrice}">
								${currencyService.format(applicationService.formatPrice(item.product.salesPrice))}
							    <span class="regular-price">${currencyService.format(applicationService.formatPrice(item.product.price))}</span>
							</g:if>
							<g:else>
								${currencyService.format(applicationService.formatPrice(item.product.price))}
							</g:else>
						</td>
						<td style="text-align:center">${item.quantity}</td>
						<td  style="text-align:center" id="extended_price">
							${currencyService.format(applicationService.formatPrice(extendedPrice))}
						</td>
						<td>
							<g:form controller="shoppingCart" action="remove_item" method="get" id="${shoppingCartInstance.id}">
								<input type="hidden" name="itemId" value="${item.id}"/>
								<input type="hidden" name="id" value="${shoppingCartInstance.id}"/>
								<input type="submit" class="btn btn-sm btn-default" name="submit" value="Remove Item"/>
							</g:form>
						</td>
					</tr>
				</g:each>
				<tr>
					<td colspan="4" style="text-align:right;">Subtotal</td>
					<td style="text-align:center; ">${currencyService.format(applicationService.formatPrice(shoppingCartInstance.subtotal))}</td>
					<td></td>
				</tr>
			</tbody>
		</table>
		
		<div class="form-group" style="position:relative; text-align:center;">
			<g:form controller="shoppingCart" action="checkout_preview" id="${shoppingCartInstance?.id}">
				<input type="hidden" name="id" value="${shoppingCartInstance?.id}"/> 
				<g:submitButton name="submit"  value="Continue Checkout" class="btn btn-primary pull-right bt-lg" id="checkout-btn"/>
			</g:form>

			<span class="pull-right" id="processing" style="display:none; margin-right:20px;padding-top:7px;">
				Calculating shipping, please wait&nbsp;
				<img src="/${applicationService.getContextName()}/images/loading.gif" >
			</span>
		</div>
		
		
		
	</g:if>
	<g:else>
		<p>Your Shopping Cart is currently empty...</p>
	</g:else>
	
	
	<script type="text/javascript">
		$(document).ready(function(){
			var $processing = $("#processing"),
				$checkoutBtn = $("#checkout-btn");
			
			$checkoutBtn.click(showProcessing)
			
			function showProcessing(){
				$processing.show()
			}
		})
	</script>

${raw(applicationService.getScreenFooter("Checkout Preview"))}	