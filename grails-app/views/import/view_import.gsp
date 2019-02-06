<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Pelican : View Import</title>
	</head>
	<body>
		
		<div id="export-data-view" class="content" role="main">
	
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${raw(flash.message)}</div>
			</g:if>
		
			<div id="data-check-container">

				<div class="alert alert-info" role="status">Data Import could take upwards around 15+ minutes depending on file size. Please be patient.
				</div>
			
				<div id="data-check-form">
					<h2 style="margin:10px 0px; padding:0px;">Data Check</h2>
					<p class="secondary">Import &amp; Export is meant as an alternate backup mechanism of data<br/>
					No Data will be imported, this will just display data to be imported<br/>
					Expecting Pelican Json formatted data</p>
            		
            		
					<g:uploadForm action="import_data" class="form-horizontal" >
						<input type="file" name="json-data" id="json-data" />
						<br/>
						<g:submitButton name="importCheck" class="btn btn-primary" value="Perform Import Data Check"/>		
					</g:uploadForm>
			
			
			
					<g:if test="${checkResults}">
			
						<h4 class="pull-left">Data Check Results</h4>
			
						<p class="secondary pull-left" style="margin:10px 10px">- Data to be imported on "Import Data"</p>
						
						<table class="table">
							<tr>
								<td><strong>${accountsCount ? accountsCount : 0}</strong>&nbsp;:&nbsp;Accounts</td>
								<td><strong>${catalogsCount ? catalogsCount : 0}</strong>&nbsp;:&nbsp;Catalogs</td>
								<td><strong>${productsCount ? productsCount : 0}</strong>&nbsp;:&nbsp;Products</td>
							</tr>
							
							<tr>
								<td><strong>${productOptionsCount ? productOptionsCount : 0}</strong>&nbsp;:&nbsp;Product Options</td>
								<td><strong>${productSpecificationsCount ? productSpecificationsCount : 0}</strong>&nbsp;:&nbsp;Product Specifications</td>
								<td><strong>${additionalPhotosCount ? additionalPhotosCount : 0}</strong>&nbsp;:&nbsp;Additional Photos</td>
							</tr>
							<tr>
								<td><strong>${shoppingCartsCount ? shoppingCartsCount : 0}</strong>&nbsp;:&nbsp;Shopping Carts</td>
								<td><strong>${ordersCount ? ordersCount : 0}</strong>&nbsp;:&nbsp;Orders</td>
								<td><strong>${pagesCount ? pagesCount : 0}</strong>&nbsp;:&nbsp;Pages</td>
							</tr> 
							<tr>
								<td><strong>${uploadsCount ? uploadsCount : 0}</strong>&nbsp;:&nbsp;Uploads</td>
								<td><strong>${layoutCount ? layoutCount : 0}</strong>&nbsp;:&nbsp;Layout</td>
								<td><strong>${logsCount ? logsCount : 0}</strong>&nbsp;:&nbsp;Logs</td>
							</tr>
						</table>
					</g:if>
				</div>
			</div>
			
			<br class="clear"/>
			<hr/>
			<br/>
			
	
			<div id="data-check-container">
			
				<div id="data-check-form" style="margin-top:0px;">
					<h2 style="margin-top:0px">Import Data</h2>
            		
					<p class="secondary">This will perform the actual data import<br/>
					Expecting Pelican Backup Json formatted data. <br/>Run 'Data Check' to insure data to be imported is correct</p>
					
					
					<g:uploadForm action="import_data" class="form-horizontal" >
						
						<input type="hidden" name="performImport" value="true"/>
						<input type="file" name="json-data" id="json-data" />
						
						<br/>
						<g:submitButton name="import" class="btn btn-primary import-button-unique" value="Perform Import Data"/>					<br/>
						<span id="processing" style="display:none; margin-top:20px;">
							<img src="/${applicationService.getContextName()}/images/loading.gif" >
							&nbsp;&nbsp;Processing import, please wait&nbsp;
						</span>
						<br/>
					</g:uploadForm>
					
					
					<g:if test="${importResults}">
						<h4 class="pull-left">Data Import Results</h4>
						<p class="secondary pull-left" style="margin:10px 10px;">- Data imported</p>
						
						<table class="table">
							<tr>
								<td><strong>${accountsImported ? accountsImported : 0}</strong>&nbsp;:&nbsp;Accounts</td>
								<td><strong>${catalogsImported ? catalogsImported : 0}</strong>&nbsp;:&nbsp;Catalogs</td>
								<td><strong>${productsImported ? productsImported : 0}</strong>&nbsp;:&nbsp;Products</td>
							</tr>
							
							<tr>
								<td><strong>${productOptionsImported ? productOptionsImported : 0}</strong>&nbsp;:&nbsp;Product Options</td>
								<td><strong>${specificationsImported ? specificationsImported : 0}</strong>&nbsp;:&nbsp;Product Specifications</td>
								<td><strong>${additionalPhotosImported ? additionalPhotosImported : 0}</strong>&nbsp;:&nbsp;Additional Photos</td>
							</tr>
							<tr>
								<td><strong>${shoppingCartsImported ? shoppingCartsImported : 0}</strong>&nbsp;:&nbsp;Shopping Carts</td>
								<td><strong>${ordersImported ? ordersImported : 0}</strong>&nbsp;:&nbsp;Orders</td>
								<td><strong>${pagesImported ? pagesImported : 0}</strong>&nbsp;:&nbsp;Pages</td>
							</tr> 
							<tr>
								<td><strong>${uploadsImported ? uploadsImported : 0}</strong>&nbsp;:&nbsp;Uploads</td>
								<td><strong>${layoutImported ? layoutImported : 0}</strong>&nbsp;:&nbsp;Layout</td>
								<td><strong>${logsImported ? logsImported : 0}</strong>&nbsp;:&nbsp;Logs</td>
							</tr>
						</table>
					</g:if>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			var $processing = $('#processing')
			var $importButton = $('.import-button-unique')
			
			$importButton.click(function(){
				$processing.css({
					"display" : "block"
				})
			})
		</script>
	</body>
</html>	