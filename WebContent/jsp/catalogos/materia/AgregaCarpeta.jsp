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
			 iniciaMessageDialogChild();
			 $("#nombreCarpeta").focus();
			  } );
		 
		</script>  
		<form action="AgregaCarpeta" method="post" name="form1" id="form1">

		    <div style="text-align: center;">
				<div class="ui-widget" id="messageChild">
					<div class="ui-state-highlight ui-corner-all msg" >
						<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
						<strong id="innerMessageChild">${message}</strong></p>
					</div>
				</div>
				
				<div style="margin-top: 30px">
					<input type="text" id="nombreCarpeta" name="nombreCarpeta" ${disabled} value="${valNombreCarpeta}" size="50"/>
				</div>
			</div>

		</form>
    </body>
</html>
