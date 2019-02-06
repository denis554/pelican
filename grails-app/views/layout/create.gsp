<%@ page import="io.pelican.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Pelican : Store Layout</title>
		
		<script type="text/javascript" src="${resource(dir:'js/allow-tab.js')}"></script>
		
	</head>
	<body>

		<g:if test="${flash.message}">
			<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
		</g:if>
		
		<g:if test="${flash.error}">
			<div class="alert alert-danger" style="margin-top:10px;">${flash.error}</div>
		</g:if>
		
		<g:form controller="layout" action="save">
			
			<div class="form-group" style="margin-top:30px">
				<g:submitButton class="btn btn-primary pull-right" name="save" value="Save Store Layout" />
				<g:link class="btn btn-default pull-right" controller="layout" action="index" style="display:inline-block;margin-right:10px;">Back to Layouts</g:link>
			<br class="clear"/>
			</div>
			
			<h2>New Layout</h2>
			
			<div class="form-row">
				<span class="form-label twohundred secondary">Name 
					<span class="information secondary block">Name must be unique</span>
				</span>
				<span class="input-container">
					<input name="name" type="text" class="form-control threefifty" value="${layoutInstance?.name}" id="name"/>
				</span>
				<br class="clear"/>
			</div>
			<div class="form-row">
				<span class="form-label twohundred secondary">Default 
					<span class="information secondary block">Selecting default will automatically unassign the other</span>
				</span>
				<span class="input-container">
					<g:checkBox name="defaultLayout" value="${layoutInstance?.defaultLayout}" checked="${layoutInstance?.defaultLayout}"/>
				</span>
				<br class="clear"/>
			</div>
			
			<hr/>
			
			<h3>Layout HTML</h3>
			
			<p class="instructions">Layout code will automatically be wrapped by necessary wrapper html that includes everything necessary for your store front. <g:link controller="layout" action="edit_wrapper">Edit Main HTML Wrapper</g:link>. All that is needed here is all html within your <strong>body</strong> tag. Libraries included jQuery, jQuery UI &amp; Bootstrap CSS.</p>
			
			<p class="instructions">Place all layout code below. Be sure to include <strong>[[CONTENT]]</strong> tag.  <g:link controller="layout" action="tags">View All Available Tags</g:link>.</p>  
			
			<p class="instructions">[[CONTENT]] tag is where all store front product, catalog, page and other content is displayed automatically</p>
			
			<p class="instructions"><g:link controller="layout" action="how">How the Layout Engine works</g:link></p>
			
			<%
			def layoutContent = layoutInstance?.content
			if(!layoutContent)layoutContent = "[[CONTENT]]"
			%>
			
				
			<div style="border:solid 1px #ddd; background:#333;background:#384248">
				<span class="layout-code">&lt;html&gt;</span>
				<span class="layout-code">&nbsp;&nbsp;&nbsp;&lt;body&gt;</span>
				<textarea id="layout-textarea"
						name="content" 
						class="form-control">${layoutContent}</textarea>
				<span class="layout-code">&nbsp;&nbsp;&nbsp;&lt;/body&gt;</span>
				<span class="layout-code">&lt;/html&gt;</span>
			</div>
			
			<h3>Layout CSS</h3>
			<p class="instructions">Much of the storefront has css code, however your layout will need its own  supporting css code below</p>
			
			
			<div id="layout-code-wrapper" class="css">
				<span class="layout-code">&lt;style&gt;</span>
				<textarea id="css-textarea" 
						name="css" 
						class="form-control">${layoutInstance?.css}</textarea>
				<span class="layout-code">&lt;/style&gt;</span>
			</div>
			
			
			<br class="clear"/>
			
			
			<h3>Layout Javascript</h3>
			
			<p class="instructions">This section is for such things as home page carousels, sliding panes or any other Javascript goodness to add to your storefront</p>
			
			<p>Please use single quotes for javascript variables</p>
			
			<p class="instructions">This will be added at the bottom of your page layout. Warning, there are additional scripts on a per page basis that may conflict. Please check variable names on page load to ensure no duplicates or interference with base store front.</p>
			

			<div id="layout-code-wrapper" class="javascript">
				<span class="layout-code">&lt;script&gt;</span>
				<textarea id="javascript-textarea" 
						name="javascript" 
						class="form-control">${layoutInstance?.javascript}</textarea>
				<span class="layout-code">&lt;/script&gt;</span>
			</div>
					
			
			<div class="form-group" style="margin-top:30px">
				<g:submitButton class="btn btn-primary pull-right" name="save" value="Save Store Layout" />
			</div>
			
		</g:form>
		
		

<script type="text/javascript">
$(document).ready(function(){
	$("#layout-textarea").allowTabChar();
	$("#css-textarea").allowTabChar();
	$("#javascript-textarea").allowTabChar();
});
</script>		
		
	</body>
</html>
