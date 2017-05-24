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
			 $("#file").focus();
			 
			var cveMat = $("#form1 #hiddenCveMat").val();
			var pathDirectory = $("#form1 #pathDirectory").val();
			$("#hiddenCveMatAgregaArchivo").val(cveMat);
			$("#hiddenPathDirAgregaArchivo").val(pathDirectory);
			 
			  } );
		 
		</script>  
		<form action="AgregaArchivo" method="post" name="form1" id="formAgregaArchivo" enctype="multipart/form-data">

		    <div style="text-align: center;">
				<div class="ui-widget" id="messageChild">
					<div class="ui-state-highlight ui-corner-all msg" >
						<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
						<strong id="innerMessageChild">${message}</strong></p>
					</div>
				</div>
				
				<div style="margin-top: 30px">
					<input type="file" name="file" id="file" multiple="multiple">
				</div>
			</div>
			
			<input type="hidden" id="hiddenCveMatAgregaArchivo" name="hiddenCveMatAgregaArchivo" value="" />
			<input type="hidden" id="hiddenPathDirAgregaArchivo" name="hiddenPathDirAgregaArchivo" value="" />

		</form>
    </body>
</html>
