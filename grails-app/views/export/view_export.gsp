<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Pelican : View Export</title>
		<style type="text/css">
			/*.borderless td, .borderless th {
			    border: none;
			}
			*/
			hr{
				margin:10px 0px;
			}
		</style>
	</head>
	<body>
		
		<div id="export-data-view" class="content" role="main">
		
			<h2>Export Data</h2>
			<p class="secondary">All Pelican data can be exported from this screen. No data will be altered, only exported. <br/>You can use this data to import into a new instance of Pelican or use at your own will.</p>
		
			<g:form action="export_data" >
			
				&nbsp;All Data&nbsp;<input type="checkbox" class="export-options" name="export-all" id="export-all">&nbsp;
				<br/>
				<hr/>
				
				<table class="table borderless">
					<tr>
						<td>
							<input type="checkbox" class="export-options" name="exportAccounts">&nbsp;Accounts
						</td>
						<td>
							<input type="checkbox" class="export-options" name="exportCatalogs">&nbsp;Catalogs
						</td>
						<td>
							<input type="checkbox" class="export-options" name="exportProducts">&nbsp;Products
						</td>
						<!-- TODO: might be unnecessary
						<td>
							<input type="checkbox" class="export-options" name="exportPermissions">&nbsp;Permissions
						</td>
						-->
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="export-options" name="exportProductOptions">&nbsp;Product Options &amp; Variants
						</td>
						<td>
							<input type="checkbox" class="export-options" name="exportSpecifications">&nbsp;Product Specification Data
						</td>
						<td>
							<input type="checkbox" class="export-options" name="exportAdditionalPhotos">&nbsp;Additional Photos
						</td>
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="export-options" name="exportShoppingCarts">&nbsp;Shopping Carts
						</td>
						<td>
							<input type="checkbox" class="export-options" name="exportOrders">&nbsp;Orders
						</td>
						<td>
							<input type="checkbox" class="export-options" name="exportPages">&nbsp;Pages
						</td>
					</tr>
					<tr>
						<td>
							<input type="checkbox" class="export-options" name="exportUploads">&nbsp;Uploads
						</td>
						<td>
							<input type="checkbox" class="export-options" name="exportLayout">&nbsp;Layout
						</td>
						<td>
							<input type="checkbox" class="export-options" name="exportLogs">&nbsp;Logs
						</td>
					</tr>
				</table>
			
				<br/>
				<div style="text-align:center">
					<g:submitButton name="export-data" class="btn btn-primary" value="Export Data" style="margin:auto"/>
				</div>
			</g:form>
		
		</div>
		
		<script type="text/javascript">
			$(document).ready(function(){
				var $checkboxes = $('.export-options')
				
				$checkboxes.change(function(checkbox){
					var $checkbox = $(this)
					if(this.checked){
						if($checkbox.attr('name') == 'export-all'){
							$checkboxes.prop('checked', true)
						}
					}else{
						if($checkbox.attr('name') == 'export-all'){
							$checkboxes.prop('checked', false)
						}else{
							$('#export-all').prop('checked', false)
						}
					}
				});
				
				$('#export-all').trigger('click')		
			})
		</script>
	</body>
</html>	