<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('io.pelican.CurrencyService').newInstance()%>

${raw(applicationService.getDefaultHeader("Select Shipping Option"))}


<h2 style="margin-top:0px; margin-bottom:20px;">Select Shipping Option</h2>
	
<div style="float:left; width:325px;">
	<g:each in="${carriers}" var="carrier">
		<%
			def logoUri = ""
			switch(carrier.key) {
				case "CanadaPost" :
					logoUri = "images/carriers/canada-post-logo.png"
					break
				case "ColisPrive" :
					logoUri = "images/carriers/colis-prive-logo.png"
					break
				case "DHL" :
					logoUri = "images/carriers/dhl-express-logo.png"
					break
				case "DHLGlobalmailInternational" :
					logoUri = "images/carriers/dhl-global-mail-logo.png"
					break
				case "DHLExpress" :
					logoUri = "images/carriers/dhl-express-logo.png"
					break
				case "DHLGlobalMail" :
					logoUri = "images/carriers/dhl-global-mail-logo.png"
					break
				case "FedEx" :
					logoUri = "images/carriers/fedex-logo.png"
					break
				case "FedExSmartPost" :
					logoUri = "images/carriers/fedex-smartpost-logo.png"
					break
				case "GSO" :
					logoUri = "images/carriers/gso-logo.png"
					break
				case "LaserShip" :
					logoUri = "images/carriers/lasership-logo.png"
					break
				case "LSO" :
					logoUri = "images/carriers/lso-logo.png"
					break
				case "Norco" :
					logoUri = "images/carriers/norco-logo.png"
					break
				case "OnTrac" :
					logoUri = "images/carriers/ontrac-logo.png"
					break
				case "Purolator" :
					logoUri = "images/carriers/purolator-logo.png"
					break
				case "UPS" :
					logoUri = "images/carriers/ups-logo.png"
					break
				case "UPSSurePost" :
					logoUri = "images/carriers/ups-surepost-logo.png"
					break
				case "UPSMailInnovations" :
					logoUri = "images/carriers/ups-mail-innovations-logo.png"
					break
				case "USPS" :
					logoUri = "images/carriers/usps-logo.png"
					break
				case "RoyalMail" :
					logoUri = ""
					break
			}
		%>
		
		
		<div style="background:#f8f8f8;padding:15px;border:solid 1px #ddd; margin:0px 0px 30px 0px;">
			<h3 style="margin-top:0px">
				<g:if test="${logoUri}">
					<img src="/${applicationService.getContextName()}/${logoUri}" title="${carrier.key}"/>
				</g:if>
				<g:else>
					${carrier.key}
				</g:else>
			</h3>
			
			<g:each in="${carrier.value}" var="option">
				<div style="padding:15px; background:#fff; border:solid 1px #ddd; margin:10px 0px;">
					<div class="pull-left">
					${option.service} : <strong>${option.currency}&nbsp;${applicationService.formatPrice(option.rate)}</strong>
					<br/>
					<span style="">Days: ${option.days}</span>
					</div>
					<g:link action="set" params="[ anonymous: anonymous, optionId : option.id, rate : option.rate, carrier : carrier.key, service : option.service, days : option.days, rateId : option.rateId, currency: option.currency, id: shoppingCart?.id ]" class="btn btn-default btn-sm pull-right">Select</g:link>
					<div class="clear"></div>
				</div>
			</g:each>
			
		</div>
	</g:each>
</div>


<div style="float:left; margin-left:30px; width:270px;background:#f8f8f8;padding:15px;border:solid 1px #ddd">
	<h3 style="margin-top:0px">Shipping Address</h3>

	<address>
		${accountInstance.name}<br/>
		${accountInstance.address1}<br/>
		${accountInstance.address2}<br/>
		${accountInstance.city}, 
		
		<%
		def state
		if(accountInstance.state?.name){
			state = accountInstance.state.name
		}else{
			state = accountInstance.state
		}
			
		def country
		if(accountInstance.country?.name){
			country = accountInstance.country.name
		}else{
			country = accountInstance.country
		}
		%>
		
		${state}<br/>
		${country}<br/>
		
		${accountInstance.zip}		
	</address>
	
	<g:link controller="account" action="customer_profile" class="btn btn-default" style="margin:10px auto">Change Address</g:link>
	
</div>
			
${raw(applicationService.getDefaultFooter())}