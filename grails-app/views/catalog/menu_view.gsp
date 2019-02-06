
<%@ page import="io.pelican.Catalog" %>
<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Pelican : Catalog Administration</title>
		<style type="text/css">
			#menu-view-container{
				padding:20px;
				background:#f8f8f8;
				border:solid 1px #ddd;
			}
			#menu-view-container ul li{
				list-style:none !important;
				padding:3px 0px;
			}
		</style>
	</head>
	<body>

		<div id="list-catalog" class="content scaffold-list" role="main">
			
			<h2 class="">Catalogs Menu View
				<g:link controller="catalog" action="create" class="btn btn-primary pull-right" >New Catalog</g:link>
				<g:link controller="catalog" action="list" class="btn btn-default pull-right" style="display:inline-block;margin-right:5px">Back to List View</g:link>
			</h2>
			<p class="instructions">How the menu will display on store front</p>
			
			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<div id="menu-view-container">
				${raw(catalogMenuString)}
			</div>

		</div>	
	</body>
</html>
