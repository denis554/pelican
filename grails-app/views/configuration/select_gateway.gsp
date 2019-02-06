<%@ page import="io.pelican.Catalog" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Pelican : Select Gateway</title>

	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	
	
	<style type="text/css">
		.gateway-select-container{
			text-align:center;
			float:left;
			width:321px;
			padding-top:73px;
			padding-bottom:5px;
			border:dashed 3px #ccc;
			background:#FBFFFF;
			opacity:0.32;
			-webkit-border-radius: 3px !important;
			-moz-border-radius: 3px !important;
			border-radius: 3px !important;
		}

		.gateway-select-container:hover{
			cursor:pointer;
			background:#FBFFFF;
			opacity:0.70;
			border:dashed 3px #ccc;
		}
		
		.gateway-select-container.pressed:active{
			margin-top:1px;
			opacity:1.0;
			border:dashed 3px #9D9D9D;
		}
		
		.gateway-select-container.processing{
			opacity:1.0;
			background:#FBFFFF;
			border:dashed 3px #111;
		}
		
		..gateway-select-container.processing:hover{
			background:#F7F7F7;
		}
		
		.gateway-select-container.active{
			opacity:1.0;
			background:#fff;
			border:dashed 3px #555;
		}
		
		
		.gateway-select-container input[type="radio"]{
			display:block;
			margin:20px auto 81px auto;
		}
	</style>
	
	<script type="text/javascript">

	$(document).ready(function(){
	
		var braintreeEnabled = "${gateway_settings.braintreeEnabled}";
	
		var $form = $("#gateway-form")
	
		var $stripeEnabled = $("#stripe-enabled"),
			$braintreeEnabled = $("#braintree-enabled");
			$braintreeEnabledHidden = $("#braintree-enabled-hidden");
		
		
		var $stripeEnabledContainer = $("#stripe-enabled-container"),
			$braintreeEnabledContainer = $("#braintree-enabled-container");
	
		var $radios = $(".gateway");
		var $containers = $(".gateway-select-container");
	
		if(braintreeEnabled == "false"){
			$stripeEnabled.prop("checked", true)
			$braintreeEnabled.prop("checked", false)
			$stripeEnabledContainer.addClass("active")
			$braintreeEnabledContainer.removeClass("active")
		}else{
			$braintreeEnabled.prop("checked", true)
			$stripeEnabled.prop("checked", false)
			$braintreeEnabledContainer.addClass("active")
			$stripeEnabledContainer.removeClass("active")
		}
	
	
		$containers.click(function(event){
			var $container = $(this)
			var $radio = $container.find(".gateway");
		
			if(!$container.hasClass("active")){
				processing($radio, $container)
			}
			
		})
	
		$radios.change(function(event){
			console.log(event, this)
			var $radio = $(this);
			if(!$radio.parent(".gateway-select-container").hasClass("active")){
				processing($radio, $radio.parent(".gateway-select-container"));
				$form.submit();
			}
		});
	
	
		function processing($radio, $container){
			$container.addClass("pressed").removeClass("pressed");
			$container.addClass("processing")
			$radio.prop("checked", true);
			if($container.prop("id") == "stripe-enabled-container"){
				$braintreeEnabledHidden.val("false")
			}
		
			if($container.prop("id") == "braintree-enabled-container"){
				$braintreeEnabledHidden.val("true")
			}
		
			$form.submit()
		}
	
	});

	</script>
</head>
<body>

	<h2>Gateway Settings</h2>
		

	<p class="information" style="width:91%">In order for Pelican to work, you need to have a payment gateway account with either <a href="http://www.stripe.com" target="_blank">Stripe</a> or <a href="http://www.braintreepayments.com" target="_blank">Braintree</a>. These services allow you to quickly setup Merchant accounts to 
	start accepting payments (recommended : <a href="http://www.stripe.com" target="_blank">Stripe</a>). 
			<g:link controller="configuration" action="payment_settings">Return to Payment Settings</g:link></p>

	
	<div id="" style="border:solid 0px #ddd; width:100%;text-align:center; margin-top:37px;">
	
		<div id="" style="margin:auto; width:667px; border:solid 0px #ddd;">
			<div class="gateway-select-container" id="stripe-enabled-container">
				<img src="${resource(dir:'images/app', file:'stripe-logo.png')}" style="height:43%; width:43%;outline:none"/>
				<input type="radio" name="stripe-enabled" class="gateway" id="stripe-enabled"/>
			</div>
			<div class="gateway-select-container" style="margin-left:20px" id="braintree-enabled-container">
				<img src="${resource(dir:'images/app', file:'braintree-logo.png')}" style="height:60%; width:60%;outline:none; margin-top:19px;"/>
				<input type="radio" name="braintree-enabled" class="gateway" id="braintree-enabled"/>
			</div>
			<br class="clear"/>
		</div>
		

		<g:if test="${flash.message}">
			<div class="alert alert-info" role="status" style="margin-top:40px;">${flash.message}
			<br/>
			<br/>
			<g:link controller="configuration" action="payment_settings">Return to Payment Settings</g:link>
			<br class="clear"/>
			</div>
		</g:if>
		<g:else>
			<div class="buttons-container">
				<g:link controller="configuration" action="payment_settings">Return to Payment Settings</g:link>
			</div>
		</g:else>
			
	</div>
	

	
	
	<form action="update_gateway" class="form-horizontal" id="gateway-form">
		<input type="hidden" name="enabled" id="braintree-enabled-hidden" value=""/>
	</form>

	
</body>
</html>