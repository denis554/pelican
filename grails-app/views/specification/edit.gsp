<%@ page import="io.pelican.Product" %>
<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Edit Specification</title>
	</head>
	<body>

		<div id="catalog-selection-backdrop"></div>

    	<div id="catalog-selection-modal">
    		<h3>Product Catalogs</h3>
    		<p class="information secondary">Selecting a Subcatalog will automatically select all parent Catalogs up to the top level Catalog.</p>
    		<div id="catalog-selection-container">
    			${raw(catalogIdSelectionList)}
    		</div>        
            <div class="alert alert-danger pull-left" style="margin-top: 8px; padding:5px 10px;width:385px">
                <strong>Warning:</strong> Changing catalogs will remove any product specifications for this product
            </div>
    		<a href="javascript:" class="btn btn-default pull-right" style="margin-top:15px;" id="close-catalogs-select-modal">Accept &amp; Close</a>
    		<br class="clear"/>
    	</div>



		<div class="content">

			<h2>Edit Specification
				&nbsp;
				<g:form action="delete" method="post" id="${specificationInstance.id}" style="float:right;display:inline-block;margin:0px;">
                    <g:actionSubmit class="btn btn-danger" action="delete" value="Delete"
                        id="${specificationInstance.id}" formnovalidate=""
                        style="float:right;display:inline-block;margin-left:5px;"
                        onclick="return confirm('Are you sure you want to delete this specification?');" />
                </g:form>
				<g:link controller="specification" action="list" name="list" class="btn btn-default pull-right">Back to Specifications</g:link>
            </h2>

			<br class="clear"/>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


			<div id="edit-specification" style="width:410px;float:left">

				<g:form action="update" method="post">

					<input type="hidden" name="id" value="${specificationInstance.id}"/>

					<div class="form-row">
						<span class="form-label secondary" style="width:120px;">Name</span>
						<input type="text" name="name" class="form-control" value="${specificationInstance?.name}" style="width:200px;margin-left:15px;display:inline-block"/>

						<br class="clear"/>
					</div>

					<div class="form-row">
						<span class="form-label secondary">Catalogs<br/>
							<a href="javascript:" id="catalog-selection-modal-link">Add/Remove Catalogs</a>
						</span>
						<span class="input-container" id="selected-catalogs-span" style="width:240px;">
							<g:each in="${specificationInstance?.catalogs}" var="catalog">
								<span class="label label-default">${catalog.name}</span>
							</g:each>
						</span>
						<input type="hidden" value="" id="catalogIds" name="catalogIds"/>
						<br class="clear"/>
					</div>

					<div style="text-align:center">
						<g:submitButton name="update" class="btn btn-primary" value="Update Specification Settings" style="margin: 0px auto 0px auto;"/>
					    <br/><br/>
					    <g:link action="product_specifications" id="${specificationInstance.id}" class="btn btn-default">Product Associations</g:link>
					</div>

					<br class="clear"/>
				</g:form>


				<g:if test="${specificationInstance.specificationOptions?.size() > 0}">

					<h4 style="margin-top:20px">Current Options</h4>

					<table class="table table-condensed">
						<thead>
							<tr>
								<th>Name</th>
								<th><g:link action="manage_option_positions" id="${specificationInstance.id}" class="btn btn-default btn-xs pull-right">Edit Ordering <span class="glyphicon glyphicon-sort"></span></g:link></th>
							</tr>
						</thead>
						<tbody>
                        <%
                        def specificationOptions = specificationInstance.specificationOptions?.sort{ it.name }
                        specificationOptions = specificationOptions.sort{ it.position }
                        %>
						<g:each in="${specificationOptions}" status="i" var="option">
							<tr id="variant_${option.id}">
								<td>${option.name}</td>
								<td style="text-align:right; width:150px;">
									<g:link class="btn btn-default" action="edit_option" id="${option.id}">Edit</g:link>
									<g:form action="delete_option" method="post" id="${option.id}" style="display:inline-block;">
										<g:actionSubmit class="btn btn-default" controller="specification" action="delete_option" value="Delete" id="${option.id}" formnovalidate="" onclick="return confirm('Are you sure?');" />
									</g:form>
								</td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</g:if>
				<g:else>
					<div class="alert alert-info">No options added yet</div>
				</g:else>

			</div>



			<g:form controller="specification" action="add_option" method="post" >

				<input type="hidden" name="id" value="${specificationInstance.id}"/>
                    
				<div style="width:290px; float:left; margin-left:40px; border:solid 1px #ddd; padding:15px; background:#f8f8f8">

					<g:if test="${flash.optionMessage}">
						<div class="alert alert-info" role="status">${flash.optionMessage}</div>
					</g:if>

					<h3 style="margin:0px auto 20px; text-align:center">Add Option</h3>

					<div class="form-row">
						<span class="form-label secondary">Name</span>
						<span class="input-container">
							<g:field class="form-control" name="name" value="${name}" style="width:175px;"/>
						</span>
						<br class="clear"/>
					</div>

            		<g:submitButton name="add" class="btn btn-primary pull-right" value="Add Option" />

				</div>

			</g:form>

			<div class="clear"></div>

		</div>


		<script type="text/javascript" src="${resource(dir:'js/specification_catalogs.js')}"></script>

		<script type="text/javascript">

			var catalogIds = [];
			<g:if test="${catalogIdsArray}">
				catalogIds = ${catalogIdsArray};
			</g:if>
			var catalogIdsString = catalogIds.join();

		</script>

	</body>
</html>
