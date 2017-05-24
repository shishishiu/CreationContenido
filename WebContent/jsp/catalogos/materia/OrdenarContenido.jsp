<%-- 
    Document   : index
    Created on : 1/03/2016, 01:26:58 PM
    Author     : UDEM
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <body>
		      
		<script>
		 $( function() {
				iniciaMessageDialog();
			    $( ".sortable" ).sortable();
			    $( ".sortable" ).disableSelection();
			  } );
		 
		</script>  
		<form action="OrdenarContenido" method="post" name="form1" id="form1">
		    
				<div class="ui-widget" id="message">
					<div class="ui-state-highlight ui-corner-all msg" >
						<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
						<strong id="innerMessage">${message}</strong></p>
					</div>
				</div>
				
		    	<div>
            		${menuHtml}
            	</div>
		    	<c:if test="${menuHtml!=null}">
                <div style="margin-top:10px;text-align: center">
		    		<input type="button" id="ordenar" value="Ordenar" onclick="funcOrdenarContenido('${menuId}')"/>
				</div>
				</c:if>
		</form>
    </body>
</html>
