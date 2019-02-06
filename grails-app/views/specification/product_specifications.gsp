<%@ page import="io.pelican.Catalog" %>
<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>
<% def currencyService = grailsApplication.classLoader.loadClass('io.pelican.CurrencyService').newInstance()%>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	
	<title>Product Specifications</title>
</head>
<body>
	
	<div class="form-outer-container">	
		
		<div class="form-container">
			
			<h2>Product Specifications
				<g:link action="edit" class="btn btn-default pull-right" id="${specificationInstance.id}">Back to Specification</g:link>
				<br class="clear"/>
			</h1>
			
			<br class="clear"/>
			
			<div class="messages">
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${flash.message}</div>
				</g:if>
			</div>


			<g:form method="post" action="set_product_specifications" id="${specificationInstance?.id}">

				<g:hiddenField name="catalogId" value="${catalogInstance?.id}" />
				<g:hiddenField name="id" value="${specificationInstance?.id}" />
				<input type="hidden" name="productSpecifications" id="productSpecifications" name="product_specifications" value=""/>
				<input type="hidden" name="max" value="${params.max}"/>
				<input type="hidden" name="offset" value="${params.offset}"/>


                <style type="text/css">
                    .form-row{
                        margin-bottom:8px !important;
                    }
                    .information{
                        color:rgba(0,0,0,0.54)
                    }
                </style>
				<div class="form-row">
					<span class="form-label twohundred secondary">Specification</span>
					<span class="input-container">
					    <h3 style="margin:0px !important"><span class="label label-default">${specificationInstance.name}</span></h3>
					</span>
					<br class="clear"/>
				</div>

                <div class="form-row">
					<span class="form-label twohundred secondary">Catalogs</span>
					<span class="input-container" id="selected-catalogs-span">
						<g:each in="${catalogInstances}" var="catalog">
							<span class="label label-default">${catalog.name}</span>
						</g:each>
						<p class="information">Only products that are associated with the catalogs listed above will be displayed</p>
					</span>
					<br class="clear"/>
				</div>


				<div class="form-row">
					<span class="form-label twohundred secondary">Select Catalog</span>
					<span class="input-container">
						<select name="catalog" class="form-control" id="catalog" style="width:auto">
						    <option value="">Select Catalog</option>
							${raw(catalogHtmlOptions)}
						</select>
					</span>
					<br class="clear"/>
				</div>
                
                <g:if test="${lowestPriceProduct && highestPriceProduct}">
                    <div id="price-range" style="padding-top:10px;text-align:left;">
                        <strong>Price Range : </strong>${currencyService.format(applicationService.formatPrice(lowestPriceProduct.price))} - ${currencyService.format(applicationService.formatPrice(highestPriceProduct.price))}
                    </div>
                </g:if>
            
                <g:if test="${products}">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th style="text-align:center">Price</th>
                                <th style="text-align:center">Quantity</th>
                                <th style="text-align:right;width:150px">Specification: ${specificationInstance.name}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${products}" var="product">
                                <tr>
                                    <td style="text-align:left">${product.name}</td>
                                    <td>${currencyService.format(applicationService.formatPrice(product.price))}</td>
                                    <td>${product.quantity}</td>
                                    <td style="text-align:right;">
                                        <select class="product_specification" name="product_specification_${product.id}">
                                            <option value="${product.id}-NONE">None</option>
                                            <%
                                                def specificationOptions = specificationInstance.specificationOptions.sort{ it.name }
                                                specificationOptions = specificationOptions.sort{ it.position }
                                            %>
                                            <g:each in="${specificationOptions}" var="option">
                                                <%
                                                    def selected = ""
                                                    product.productSpecifications.each{
                                                        if(it.specificationOption.id == option.id){
                                                            selected = "selected"
                                                        }
                                                    }
                                                %>
                                                <option value="${product.id}-${option.id}" ${selected}>${option.name}</option>
                                            </g:each>
                                        </select>
                                    </td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                    <div class="btn-group">
                        <g:paginate
                        total="${productsTotal}"
                            id="${specificationInstance?.id}" 
                            controller="specification"
                            action="product_specifications"
                            params="[catalogId : params.catalogId]"/>
                    </div>

                    <div class="buttons-container">
                        <g:actionSubmit class="btn btn-primary" action="set_product_specifications" value="Set Product ${specificationInstance.name} Options" />
                    </div>
                </g:if>
                <g:else>
                    <div class="alert alert-info">Select a catalog to view products and assign product specifications</div>
                </g:else>
			</g:form>
		</div>
	</div>


<%
    def catalogId = "00"
    if(catalogInstance){
        if(catalogInstance?.id){
            catalogId = catalogInstance.id
        }
    }
%>

<script type="text/javascript">

	$(document).ready(function(){

		var protocol = window.location.protocol,
    	    host = window.location.host,
    	    path = window.location.pathname,
    	    href = window.location.href;

        var $specifications = $('.product_specification')
        var $productSpecifications = $('#productSpecifications');

        var pre = protocol + '//' + host + path;
        var $catalog = $('#catalog');
		var catalogId = ${catalogId}

        $catalog.change(changeCatalogs)
        $specifications.change(updateSpecification)
        setProductSpecifications();

        if(catalogId != "00"){
            $catalog.val(catalogId)
        }


        function setProductSpecifications(){
            var productSpecifications = ""
            $specifications.each(function(index, specification){
               var $specification = $(specification)
               var value = $specification.val()
            });
        }


        function updateSpecification(event){
            var specifications = $('#specifications').val()
            var $target = $(event.target);
            var optionId = $target.val()
            var productSpecifications = ""

            $productSpecifications.val("")
            $specifications.each(function(index, specification){
                var $option = $(specification)
                if($option.val() != "None"){
                    productSpecifications += $(specification).val() + ","
                }
            });
            productSpecifications = productSpecifications.substring(0, productSpecifications.length - 1);
            $productSpecifications.val(productSpecifications)
        }


        function changeCatalogs(){
            var id = $catalog.val()
            var url = pre + "?catalogId=" + id + "&offset=0&max=10";
            window.location = url
        }


	});

</script>
			
</body>
</html>
