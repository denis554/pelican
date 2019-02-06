<%@ page import="io.pelican.Product" %>
<%@ page import="io.pelican.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.pelican.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Specifications</title>
	</head>
	<body>
		
		<div class="content">
		
			<h2>Specifications
				<g:link controller="specification" action="create" name="edit" class="btn btn-primary pull-right" style="margin-left:5px;">Add Specification</g:link>
				<g:link controller="specification" action="manage_positions" name="manage_positions" class="btn btn-default pull-right" style="margin-left:5px;">Order Specifications</g:link>
				<g:link controller="catalog" action="list" name="list" class="btn btn-default pull-right">Back to Catalogs</g:link>
			</h2>

		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


			<g:if test="${specifications.size() > 0}">
				<table class="table table-condensed">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Options</th>
                            <th>Catalogs</th>
                            <th></th>
                        </tr>
					</thead>
					<tbody>
                        <g:each in="${specifications}" status="i" var="specification">
                            <tr>
                                <td>${specification.name}</td>
                                <td>
                                    <g:if test="${specification.specificationOptions.size() > 0 }">
                                        <%
                                        def specificationOptions = specification.specificationOptions?.sort{ it.name }
                                        specificationOptions = specificationOptions.sort{ it.position }
                                        %>
                                        <g:each in="${specificationOptions}" var="option">
                                            <span class="label label-default" style="margin:1px !important; display:inline-block">${option.name}</span>
                                        </g:each>
                                    </g:if>
                                    <g:else>
                                        <span class="information">No Specification Options Added Yet</span>
                                    </g:else>
                                </td>
                                <td>
                                    <g:if test="${specification.catalogs.size() > 0}">
                                        <g:each in="${specification.catalogs}" var="catalog">
                                            <span class="label label-default" style="margin:1px !important; display:inline-block">${catalog.name}</span>
                                        </g:each>
                                    </g:if>
                                    <g:else>
                                        <span class="information">No Catalogs Defined</span>
                                    </g:else>
                                </td>
                                <td><g:link controller="specification" action="edit" name="edit" class="edit-specification-${specification.id}" id="${specification.id}">Edit</g:link></td>
                            </tr>
                        </g:each>
					</tbody>
				</table>
			</g:if>			
			<g:else>
				<div class="alert alert-info">No Specifications added yet</div>
			</g:else>

		</div>

	</body>
</html>
